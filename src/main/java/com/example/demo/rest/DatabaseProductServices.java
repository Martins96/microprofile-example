package com.example.demo.rest;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import com.example.demo.rest.persistency.DatabaseManager;

@Path("/database/product")
public class DatabaseProductServices {

	@EJB
	private DatabaseManager dbManager;
	
	@Path("/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, 
		MediaType.APPLICATION_XML, 
		MediaType.TEXT_PLAIN})
	public Response getByID(@PathParam("id") String id) {
		Integer intID = NumberUtils.toInt(id, -1);
		if (intID == -1)
			return Response.status(400).entity("Invalid input ID").build();
		try {
			return Response.ok().entity(dbManager.getById(intID)).build();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path("/")
	@Produces({
		MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN
	})
	public Response getAll() {
		try {
			return Response.ok().entity(dbManager.getAllProducts()).build();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Gauge(name = "productSize",
			displayName = "Database Products size",
			description = "Display the number of product inside the Database",
			unit = MetricUnits.NONE)
	public int gaugeProductNum() {
		try {
			return dbManager.getAllProducts().length;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
