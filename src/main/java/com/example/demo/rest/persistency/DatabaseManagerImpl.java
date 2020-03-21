package com.example.demo.rest.persistency;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.ejb.Stateless;

import com.example.demo.rest.vo.ProductVO;

@Stateless
@Priority(1)
public class DatabaseManagerImpl implements DatabaseManager {
	
	private static final String prodByIDKey = "product.get.byid";
	private static final String prodAllKey = "product.get.all";
	
	@Override
	public ProductVO getById(Integer id) throws SQLException, IOException {
		final String query = DBUtils.getQueryFromProperties(prodByIDKey);
		
		ProductVO prod = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			
			final ResultSet rs = ps.executeQuery();
			rs.next();
			prod = DBUtils.mapProduct(rs);
			rs.close();
		} catch(final ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (final SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return prod;
	}

	@Override
	public ProductVO[] getAllProducts() throws SQLException, IOException {
final String query = DBUtils.getQueryFromProperties(prodAllKey);
		
		List<ProductVO> prods = new ArrayList<>();
		
		Connection conn = null;
		Statement ps = null;
		try {
			conn = DBUtils.getConnection();
			ps = conn.createStatement();
			
			final ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				prods.add(DBUtils.mapProduct(rs));
			}
			rs.close();
		} catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return prods.toArray(new ProductVO[0]);
	}

}
