/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Album a = this.cmbA1.getValue();
    	
    	if(a == null) {
    		this.txtResult.appendText("Seleziona un album per continuare.");
    		return;
    	}
    	
    	Set<Album> componente = this.model.getComponente(a);
    	
    	if(componente.isEmpty()) {
    		this.txtResult.appendText("Nessuna componente connessa per l'album selezionato.");
    		return;
    	}
    	
    	this.txtResult.appendText("Componente connessa - "+a+":\n"+
    								"Dimensione componente: "+componente.size()+"\n"+
    								"Durata componente: "+this.model.durataComponete(a));
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Double d = 0.0;
    	
    	try {
    		d = Double.parseDouble(this.txtDurata.getText());
    		
    		if(d<=0){
    			this.txtResult.setText("Inserire un numero positivo.");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Formato non corretto per la durata.");
    		return;
    	}
    	
    	this.model.creaGrafo(d);
    	
    	this.txtResult.appendText(this.model.infoGrafo());
    	
    	this.btnComponente.setDisable(false);
    	this.btnSet.setDisable(false);
    	
    	List<Album> al = this.model.getVertici();
    	
    	Collections.sort(al);
    	
    	this.cmbA1.getItems().addAll(al);
    	
    }

    @FXML
    void doEstraiSet(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Album a = this.cmbA1.getValue();
    	
    	if(a == null) {
    		this.txtResult.appendText("Seleziona un album per continuare.");
    		return;
    	}
    	
    	Double dTot = 0.0;
    	
    	try {
    		dTot = Double.parseDouble(this.txtX.getText());
    		
    		if(dTot<=0){
    			this.txtResult.setText("Inserire un numero positivo.");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Formato non corretto per la durata.");
    		return;
    	}
    	
    	try {
    		Set<Album> setMax = this.model.getSetAlbum(a, dTot);
    		this.txtResult.appendText("Estrazione Set di Album: ");
        	for(Album al : setMax) {
        		this.txtResult.appendText("\n"+al.getTitle()+" "+ al.getDurata());
        	}
    	}
    	catch (NullPointerException e){
    		this.txtResult.appendText("\nLa durata inserita è troppo piccola.");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnComponente.setDisable(true);
    	this.btnSet.setDisable(true);
    }

}