package com.example.demo.rest.persistency;


public class CredentialsDB {
	
	private final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	
//	private final String DB_URL = "jdbc:mariadb://127.0.0.1/test";
	private final String DB_URL = "jdbc:mariadb://172.30.112.35/test";
	private final String USER = "app_user";
    private final String PASS = "atp830udm02kg";
	
    private CredentialsDB() {
	}
    
    public static CredentialsDB getCredentials() {
    	return new CredentialsDB();
    }

	@Override
	public String toString() {
		return "CredentialsDB [JDBC_DRIVER=" + JDBC_DRIVER + ", DB_URL=" + DB_URL + ", USER=" + USER + ", PASS=" + PASS
				+ "]";
	}

	public String getDB_URL() {
		return DB_URL;
	}

	public String getUSER() {
		return USER;
	}

	public String getPASS() {
		return PASS;
	}

	public String getJDBC_DRIVER() {
		return JDBC_DRIVER;
	}
	
    
}
