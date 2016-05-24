package it.polito.tdp.metroParis.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.metroParis.DB.MetroDAO;
import it.polito.tdp.metroParis.model.Fermata;
import it.polito.tdp.metroParis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class metroController {

	private Model model;
	
	public void setModel(Model model) {
		this.model = model ;
		model.getAllStazioni();
		this.cmbArrivo.getItems().addAll(model.getStazioni());
		this.cmbPartenza.getItems().addAll(model.getStazioni());
		model.creaGrafo();
		
	}
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Fermata> cmbArrivo;

    @FXML
    private ComboBox<Fermata> cmbPartenza;

    @FXML
    private Button bttCalcola;

    @FXML
    private TextArea txtRisultato;

    @FXML
    void doCalcola(ActionEvent event) {
    	
    	txtRisultato.clear();
    	Fermata stazP = this.cmbPartenza.getValue();
    	Fermata stazA = this.cmbArrivo.getValue();
    	String stampa = model.calcolaPercorso(stazP,stazA);
    	
    	txtRisultato.appendText(stampa+"\n");
    	
    	
    }

    @FXML
    void initialize() {
        assert cmbArrivo != null : "fx:id=\"cmbArrivo\" was not injected: check your FXML file 'Controller.fxml'.";
        assert cmbPartenza != null : "fx:id=\"cmbPartenza\" was not injected: check your FXML file 'Controller.fxml'.";
        assert bttCalcola != null : "fx:id=\"bttCalcola\" was not injected: check your FXML file 'Controller.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Controller.fxml'.";

    }

	
}
