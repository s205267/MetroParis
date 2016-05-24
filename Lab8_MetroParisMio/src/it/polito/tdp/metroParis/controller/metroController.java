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
    	
    	Fermata stazioneDiPartenza = this.cmbPartenza.getValue();
		Fermata stazioneDiArrivo = this.cmbArrivo.getValue();

		if (stazioneDiPartenza != null && stazioneDiArrivo != null) {

			if (!stazioneDiPartenza.equals(stazioneDiArrivo)) {

				try {
					// Calcolo il percorso tra le due stazioni
					model.calcolaPercorso(stazioneDiPartenza, stazioneDiArrivo);

					// Ottengo il tempo di percorrenza
					int tempoTotaleInSecondi = (int) model.getPercorsoTempoTotale();
					int ore = tempoTotaleInSecondi / 3600;
					int minuti = (tempoTotaleInSecondi % 3600) / 60;
					int secondi = tempoTotaleInSecondi % 60;
					String timeString = String.format("%02d:%02d:%02d", ore, minuti, secondi);

					StringBuilder risultato = new StringBuilder();
					// Ottengo il percorso
					risultato.append(model.getPercorsoEdgeList());
					risultato.append("\n\nTempo di percorrenza stimato: " + timeString + "\n");

					// Aggiorno la TextArea
					txtRisultato.setText(risultato.toString());
					
				} catch (RuntimeException e) {
					txtRisultato.setText(e.getMessage());
				}

			} else {

				txtRisultato.setText("Inserire una stazione di arrivo diversa da quella di partenza.");
			}
			
		} else {
			
			txtRisultato.setText("Inserire una stazione di arrivo ed una di partenza.");
		}
    	
    }

    @FXML
    void initialize() {
        assert cmbArrivo != null : "fx:id=\"cmbArrivo\" was not injected: check your FXML file 'Controller.fxml'.";
        assert cmbPartenza != null : "fx:id=\"cmbPartenza\" was not injected: check your FXML file 'Controller.fxml'.";
        assert bttCalcola != null : "fx:id=\"bttCalcola\" was not injected: check your FXML file 'Controller.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Controller.fxml'.";

    }

	
}
