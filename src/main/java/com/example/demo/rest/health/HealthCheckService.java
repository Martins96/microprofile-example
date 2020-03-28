package com.example.demo.rest.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

@Health
@ApplicationScoped
public class HealthCheckService implements HealthCheck{

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder builder = HealthCheckResponse.named("alive");
		builder.up();
		return builder.build();
	}
	
}
