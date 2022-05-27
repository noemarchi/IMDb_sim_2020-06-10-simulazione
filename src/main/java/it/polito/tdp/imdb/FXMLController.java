/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
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
    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader
    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader
    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader
    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader
    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader
    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader
    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) 
    {
    	Actor attore = this.boxAttore.getValue();
    	
    	if(attore == null)
    	{
    		this.txtResult.setText("ERRORE! Seleziona un attore!");
    		return;
    	}
    	
    	List<Actor> lista = model.getSimili(attore);
    	
    	this.txtResult.setText("Attori simili a:\n");
    	this.txtResult.appendText(attore.toString() +"\n\n");
    	
    	for(Actor a: lista)
    	{
    		this.txtResult.appendText(a.toString() + "\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) 
    {
    	String genere = this.boxGenere.getValue();
    	
    	if(genere == null)
    	{
    		this.txtResult.setText("ERRORE! Seleziona un genere!");
    		return;
    	}
    	
    	model.creaGrafo(genere);
    	
    	this.txtResult.setText("Grafo creato!\n");
    	this.txtResult.appendText(String.format("# vertici: %d\n", model.nVertici()));
    	this.txtResult.appendText(String.format("# archi: %d\n", model.nArchi()));
    	
    	this.boxAttore.getItems().clear();
    	this.boxAttore.getItems().addAll(model.getVertici());
    }

    @FXML
    void doSimulazione(ActionEvent event) 
    {
    	String g = this.txtGiorni.getText();
    	
    	int giorni;
    	
    	try
    	{
    		giorni = Integer.parseInt(g);
    	}
    	catch (NumberFormatException e)
    	{
    		e.printStackTrace();
    		this.txtResult.setText("ERRORE! Inserisci un numero gi giorni (numero intero)");
    		return;
    	}
    	
    	boolean ok = model.simula(giorni);
    	
    	if(!ok)
    	{
    		this.txtResult.setText("ERRORE! Crea il grafo prima di simulare le interviste");
    		return;
    	}
    	
    	int pause = model.getPauseSimulazione();
    	
    	Collection<Actor> intervistati = model.getIntervistati();
    	
    	this.txtResult.setText(String.format("Simulazione interviste per %d giorni \n", giorni));
    	this.txtResult.appendText(String.format("Pause prese dal produttore: %d \n", pause));
    	this.txtResult.appendText("Sequenza attori intervistati: \n");
    	
    	for(Actor a: intervistati)
    	{
    		this.txtResult.appendText(a.toString() + "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    	
    	this.boxGenere.getItems().addAll(model.getGeneri());
    }
}
