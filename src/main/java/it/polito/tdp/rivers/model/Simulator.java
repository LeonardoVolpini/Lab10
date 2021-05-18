package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.model.Event.EventType;

public class Simulator {

	private Model model;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//modello del mondo
	private float Q; //capienza totale del bacino
	private float C; //acqua presente nel bacino
	private List<Flow> fIn; //lista di flussi entranti
	private float fOut; //valore flusso uscente
	private float fOutMin; //il minimo valore del fOut
	
	//parametri di input
	private float k; //fattore di scala
	private float fMed; //valore medio dei flussi
	private River river;
	
	//parametri di output
	private int numGiorniDisservizio;
	private float Cmed;
	
	public void init(float k, float f, River r) {
		this.model=new Model();
		this.k=k;
		this.fMed=f*3600*24; //da m^3/s a m^3/gg
		this.river=r;
		
		this.queue= new PriorityQueue<>();
		this.fIn=model.getFlowsByRiver(river);
		this.Q=k*fMed*30;
		this.C=Q/2;
		this.fOutMin=(float) (fMed*0.8);
		
		this.numGiorniDisservizio=0;
		this.Cmed=0;
		
		for (Flow flow: fIn) {
			this.queue.add(new Event(flow.getDay(),EventType.INGRESSO,flow) );
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		Flow f = e.getFlow();
		LocalDate data= e.getData();
		switch(e.getType()) {
		case INGRESSO: //modificare, non setto bene fOut e quindi credo di modificare male C
			this.C += e.getFlow().getFlow(); //prendo il Flow di e e poi prendo il valore di quel Flow
			if (C>Q) { 
				//schedulo evento di tracimazione
				this.queue.add(new Event(data,EventType.TRACIMAZIONE,f) );
			}
			int prob = (int)(Math.random()*100); //probabilità del 5% di Irrigazione
			if (prob<5)
				this.queue.add(new Event(data,EventType.IRRIGAZIONE,f) ); //schedulo evento di irrigazione
			else
				this.queue.add(new Event(data,EventType.USCITA,f) ); //senno' schedulo evento di uscita
			break;
		case USCITA:
			if (C<fOutMin) {
				this.numGiorniDisservizio++;
				this.C=0;
			}
			else {
				this.C -= fOutMin;
				this.Cmed += C;
			}
			break;
		case TRACIMAZIONE:
			float diffDaScaricare = C-Q;
			C-=diffDaScaricare;
			break;
		case IRRIGAZIONE:
			this.fOut=10*this.fOutMin;
			if (fOut>C) {
				//irrigazione completa non disponibile
				this.fOut=C;
				this.C=0;
				this.numGiorniDisservizio++;
			}
			else {
				this.C-=this.fOut;
				this.Cmed+=C;
			}
			break;
		}
	}
	
	public int getNumGioriDissservizio() {
		return this.numGiorniDisservizio;
	}
	
	public float getCmed() {
		return this.Cmed/this.fIn.size();
	}
	
	
}
