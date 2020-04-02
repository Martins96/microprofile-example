package com.example.demo.rest;

import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

@Path("/fault")
public class FaultTestServices {
	
	private static final Logger log = LogManager.getLogManager()
			.getLogger(FaultTestServices.class.getName());
	private static final Random rand = new Random();
	
	@Timeout(1000)
	@Retry(maxRetries = 1)
	@Fallback(fallbackMethod = "fallbackAlternative")
	@GET
	@Path("/fallback")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public Response fallback() {
		int time = rand.nextInt(2000);
		log.log(Level.INFO, "Waiting random time is [" + time + "]");
		
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			log.log(Level.SEVERE, "Interrupted sleep, timeout");
		}
		
		return Response.ok().entity("OK").build();
	}
	
	
	public Response fallbackAlternative() {
		return Response.accepted().entity("Fallback_Response!").build();
	}
	
	
	
	@GET
	@Path("/circuit-breaker")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@CircuitBreaker(delay = 10, delayUnit = ChronoUnit.SECONDS, successThreshold = 1, 
			failOn = RuntimeException.class, failureRatio = .01, requestVolumeThreshold = 1)
	public Response circuitBreaker() {
		
		boolean success = rand.nextBoolean();
		log.log(Level.INFO, "Random success is [" + success + "]");
		
		if (!success) {
			log.warning("Fault detected, state OPEN - WAITING 10 seconds to pass to half-open");
			throw new RuntimeException("Success-is-false");
		}
		
		return Response.ok().entity("OK").build();
	}
	
}
