package it.polito.tdp.rivers.model;

import java.util.List;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	private RiversDAO dao;
	
	public Model() {
		this.dao= new RiversDAO();
	}
	
	/*public void setRiverFlows(){
		for (River r : this.getAllRivers())
			r.setFlows(this.getFlowsByRiver(r));
	}*/
	
	public List<River> getAllRivers(){
		return dao.getAllRivers();
	}
	
	public List<Flow> getFlowsByRiver(River river){
		dao.setFlowAvgByRiver(river);
		return dao.getFlowsByRiver(river);
	}
	
}
