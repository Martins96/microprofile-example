package com.example.demo.rest.test;

import java.io.File;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.restassured.http.ContentType;

import static org.junit.Assert.assertEquals;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(Arquillian.class)
public class ResourceHelloWorldTest {
	
	@ArquillianResource
	private URL url;
	
	@Deployment
    public static WebArchive deploy() {

        final File[] deps = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME)
            .resolve().withTransitivity().asFile();
        
        final WebArchive wrap = ShrinkWrap.create(WebArchive.class
                , ResourceHelloWorldTest.class.getName() + ".war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource(new File("src/main/resources/persistency-query.properties"))
        		.addPackages(true, "com.example.demo")
                .addAsLibraries(deps);
		return wrap;
    }
	
	@Test
	@RunAsClient
	public void testGet() {
		String endpoint = this.url.toExternalForm() + "hello";
		System.out.println("Calling endpoint: " + endpoint);
		final Response respGet = ClientBuilder.newBuilder()
				.build()
				.target(endpoint)
				.request(MediaType.TEXT_PLAIN)
				.get();
		assertEquals(200, respGet.getStatus());
		assertEquals("Hello world!\n This is my first thorntail app", 
				respGet.readEntity(String.class));
		
		final JsonObject json = Json.createObjectBuilder()
				.add("name", "TEST").build();
		final Response respPost = ClientBuilder.newBuilder()
				.build()
				.target(endpoint)
				.request()
				.post(Entity.json(json.toString()));
		assertEquals(200, respPost.getStatus());
		assertEquals("Ciao TEST!", respPost.readEntity(String.class));
	}
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(1111));
	
	@Test
	@RunAsClient
	public void testRestCall() {
		wireMockRule.stubFor(get(urlMatching("/getproduct/1"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("content-type", "application/json")
						.withBody("{\"id\": \"001\", \"name\": \"stubbed product\", "
								+ "\"price\": \"10\"}")
						));
		
		given()
			.when()
				.get("/rest/product/1")
			.then()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("name", equalTo("stubbed product"));
	}
	
	@Test
	@RunAsClient
	public void testDBConnection() {
		given()
			.accept(ContentType.JSON)
			.get("/database/product/1")
		.then()
			.assertThat()
			.statusCode(200)
			.body("id", equalTo(1))
			.and()
			.body("name", notNullValue())
			.contentType(ContentType.JSON);
		
	}
	
	
	
}
