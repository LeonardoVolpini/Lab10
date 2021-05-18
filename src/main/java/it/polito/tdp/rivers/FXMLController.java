/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import it.polito.tdp.rivers.model.Simulator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private Simulator sim;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doRiempiCampi(ActionEvent event) {
    	this.txtResult.clear();
    	this.txtEndDate.clear();
    	this.txtFMed.clear();
    	this.txtNumMeasurements.clear();
    	this.txtStartDate.clear();
    	River r = this.boxRiver.getValue();
    	if (r==null) {
    		this.txtResult.setText("nessun fiume selezionato");
    		return;
    	}
    	List<Flow> flows=model.getFlowsByRiver(r);
    	LocalDate start= flows.get(0).getDay();
    	LocalDate end=flows.get(flows.size()-1).getDay();
    	this.txtNumMeasurements.setText(""+flows.size());
    	this.txtStartDate.setText(start.toString());
    	this.txtEndDate.setText(end.toString());
    	this.txtFMed.setText(""+r.getFlowAvg());		
    }
    
    @FXML
    void handleSimula(ActionEvent event) {
    	this.txtResult.clear();
    	River r = this.boxRiver.getValue();
    	if (r==null) {
    		this.txtResult.setText("nessun fiume selezionato");
    		return;
    	}
    	String fString= this.txtFMed.getText();
    	if (fString.isEmpty()) {
    		this.txtResult.setText("Selezionare un fiume e premere il bottone riempiCampi");
    		return;
    	}
    	float f = Float.parseFloat(fString);
    	String kString = this.txtK.getText();
    	if (kString.isEmpty()) {
    		this.txtResult.setText("Inserire un valore di k");
    		return;
    	}
    	float k;
    	try {
    		k= Float.parseFloat(kString);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico di k");
    		return;
    	}
    	if (k<=0) {
    		this.txtResult.setText("Inserire un valore di k >0");
    		return;
    	}
    	sim.init(k, f, r);
    	sim.run();
    	this.txtResult.setText("Numero di giorni di disservizio: "+sim.getNumGioriDissservizio());
    	this.txtResult.appendText("\nLa capienza media e': "+sim.getCmed());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.sim= new Simulator();
    	this.boxRiver.getItems().addAll(model.getAllRivers());	
    	
    }
}
