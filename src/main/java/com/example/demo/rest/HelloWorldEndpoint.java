package com.example.demo.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@ApplicationScoped
@Path("/hello")
public class HelloWorldEndpoint {
	
	@Inject
	@Metric(name = "hello-counter", displayName = "Hello Counter",
			description = "A simple counter auto increment for each invokation",
			absolute = true, tags = {"method=invocation"})
	private Counter helloCounter;
	
	
    @GET
    @Produces("text/plain")
    @Path("/")
    public Response helloGet() {
    	helloCounter.inc();
        return Response.ok("Hello world!"
        		+ "\n This is my first thorntail app").build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/")
    public String helloPost(String json) {
    	JsonReaderFactory factory = Json.createReaderFactory(null);
    	InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    	
    	JsonReader reader = factory.createReader(is);
    	JsonObject obj = reader.readObject();
    	
    	return "Ciao " + obj.getString("name") + "!";
    }
    
}
