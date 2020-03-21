package com.example.demo.rest.persistency;

import java.io.IOException;
import java.sql.SQLException;

import com.example.demo.rest.vo.ProductVO;

public interface DatabaseManager {
	
	public ProductVO getById(Integer id) throws SQLException, IOException;
	public ProductVO[] getAllProducts() throws SQLException, IOException;
	
	
}
