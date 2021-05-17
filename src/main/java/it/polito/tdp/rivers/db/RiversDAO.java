package it.polito.tdp.rivers.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiversDAO {

	public List<River> getAllRivers() {
		final String sql = "SELECT id, name FROM river";
		List<River> rivers = new LinkedList<River>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				rivers.add(new River(res.getInt("id"), res.getString("name")));
			}
			conn.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return rivers;
	}
	
	public List<Flow> getFlowsByRiver(River river){
		String sql="SELECT f.day, f.flow "
				+ "FROM flow f, river r "
				+ "WHERE r.id=f.river AND f.river=?";
		List<Flow> flows= new ArrayList<Flow>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, river.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Flow f = new Flow(rs.getDate("day").toLocalDate(),rs.getFloat("flow"),river);
				flows.add(f);
			}
			conn.close();
		} catch(SQLException e) {
			throw new RuntimeException("SQL Error",e);
		}
		return flows;
	}
	
	
}
