package com.example.demo.rest.persistency;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.ejb.Singleton;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import com.example.demo.rest.vo.ProductVO;

@Singleton
class DBUtils {
	
	private static class CredentialsDB {
		
		private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
		
		private static final String DB_URL_DEFAULT_VALUE = "jdbc:mariadb://127.0.0.1/test";
//		private static final String DB_URL_DEFAULT_VALUE = "jdbc:mariadb://172.30.112.35/test";
		private static final String USER_DEFAULT_VALUE = "app_user";
	    private static final String PASS_DEFAULT_VALUE = "atp830udm02kg";
	    
	    private final static Config config = ConfigProvider.getConfig();
	    
	    private CredentialsDB() {}

		static String getDB_URL() {
			return config.getOptionalValue("db.maria.url.endpoint", String.class)
					.orElse(DB_URL_DEFAULT_VALUE);
		}

		static String getUSER() {
			return config.getOptionalValue("db.maria.username", String.class)
					.orElse(USER_DEFAULT_VALUE);
		}

		static String getPASS() {
			return config.getOptionalValue("db.maria.password", String.class)
					.orElse(PASS_DEFAULT_VALUE);
		}

		static String getJDBC_DRIVER() {
			return JDBC_DRIVER;
		}
		
	    
	}
    
    private static Properties propFile = null;
    
    private DBUtils() {}

	static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName(CredentialsDB.getJDBC_DRIVER());
		return DriverManager.getConnection(CredentialsDB.getDB_URL(), 
				CredentialsDB.getUSER(), CredentialsDB.getPASS());
	}
	
	static String getQueryFromProperties(String key) throws IOException {
		if (propFile == null) {
			final InputStream is =
					DBUtils.class.getResourceAsStream("/persistency-query.properties");
			
			if (is == null) {
				System.err.println("Query File not exists!!");
				throw new FileNotFoundException("Query File not exists!!");
			}
			propFile = new Properties();
			try {
				propFile.load(is);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		return propFile.getProperty(key);
	}
	
	static ProductVO mapProduct(ResultSet rs) throws SQLException {
		final ProductVO p = new ProductVO();
		p.setId(rs.getInt("ID"));
		p.setName(rs.getString("Name"));
		p.setCategory(rs.getString("Category"));
		p.setPrice(rs.getBigDecimal("Price"));
		return p;
	}
	
	
	
	
	
	
	
	
	
}
