package com.example.demo.rest;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.math.NumberUtils;

import com.example.demo.rest.vo.ProductVO;

@Path("/rest/product")
public class CallProductService {
	
	private static final String externalEndpoint = "http://localhost:1111/getproduct";
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN})
	public Response getByID(@PathParam("id") String id) {
		final Response extResp = ClientBuilder.newClient()
				.target(externalEndpoint)
				.path(id)
				.request(MediaType.APPLICATION_JSON)
				.get();
		
		if (extResp == null || extResp.getStatus() != 200) {
			System.err.println("External service return a wrong response");
			return Response.serverError().entity("External service error").build();
		}
		final String jsonString = extResp.readEntity(String.class);
		if (jsonString == null || jsonString.isEmpty()) {
			System.out.println("No product found");
			return Response.ok().build();
		}
		
		final JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
		
		final ProductVO product = new ProductVO();
		product.setId(NumberUtils.toInt(id, 0));
		product.setName(json.getString("name"));
		product.setCategory("External");
		product.setPrice(new BigDecimal(json.getString("price")));
		
		return Response.ok().entity(product).build();
	}
	
	
}
