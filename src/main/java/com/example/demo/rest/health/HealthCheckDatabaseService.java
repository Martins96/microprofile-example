package com.example.demo.rest.health;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import com.example.demo.rest.persistency.DatabaseManager;

@Health
@ApplicationScoped
public class HealthCheckDatabaseService implements HealthCheck {
	
	@EJB
	private DatabaseManager db;

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder builder = HealthCheckResponse.named("database");
		if (db.checkIsUp()) {
			return builder.up().build();
		} else {
			return builder.down().build();
		}
	}

}
