package com.example.demo.rest.persistency;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.example.demo.rest.vo.ProductVO;

class DBUtils {
    
    private static Properties propFile = null;

	static Connection getConnection() throws SQLException, ClassNotFoundException {
		CredentialsDB cred = CredentialsDB.getCredentials();
		Class.forName(cred.getJDBC_DRIVER());
		return DriverManager.getConnection(cred.getDB_URL(), cred.getUSER(), cred.getPASS());
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
