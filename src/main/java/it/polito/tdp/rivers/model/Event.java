package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Event implements Comparable<Event> {

	public enum EventType{
		INGRESSO,
		USCITA,
		TRACIMAZIONE,
		IRRIGAZIONE
	};
	private LocalDate data;
	private EventType type;
	private Flow flow;
	
	public Event(LocalDate data, EventType type, Flow flow) {
		this.data = data;
		this.type = type;
		this.flow = flow;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	@Override
	public String toString() {
		return "Event [data=" + data + ", type=" + type + ", flow=" + flow + "]";
	}

	@Override
	public int compareTo(Event other) {
		if(this.data.equals(other.data)) {
			if(this.type==EventType.INGRESSO) 
				return -1;
			else
				return 1;
		}
		else
			return this.data.compareTo(other.data);
	}
	
	
}
