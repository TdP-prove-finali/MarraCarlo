package it.polito.tdp.PlaylistGen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PlaylistGen.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addTrack;

    @FXML
    private Button buttonStartMix;

    @FXML
    private Button buttonStartSurprise;

    @FXML
    private TextField choiceDurataMaxMix;

    @FXML
    private TextField choiceDurataMaxSurprise;

    @FXML
    private ComboBox<String> cmbArtistMix;

    @FXML
    private ComboBox<String> cmbArtistSurprise;

    @FXML
    private ComboBox<String> cmbGenreMix;

    @FXML
    private ComboBox<String> cmbGenreSurprise;

    @FXML
    private ComboBox<?> cmbNBraniMix;

    @FXML
    private ComboBox<?> cmbNBraniSurprise;

    @FXML
    private ComboBox<String> cmbTrackMix;

    @FXML
    private Label labelIstruzioni;

    @FXML
    private RadioButton minBraniSurprise;

    @FXML
    private RadioButton minBraniSurprise1;

    @FXML
    private RadioButton nBraniSurprise;

    @FXML
    private RadioButton nBraniSurprise1;

    @FXML
    private TableView<?> playlistTable;

    @FXML
    private ProgressBar progressBarSurprise;

    @FXML
    private ProgressBar progressBarSurprise1;

    @FXML
    private Button resetChoiceMix;

    @FXML
    private Button resetPlaylistMix;

    @FXML
    private Button resetSurprise;

    @FXML
    private Slider sliderAcoustic;

    @FXML
    private Slider sliderDance;

    @FXML
    private Slider sliderEnergy;

    @FXML
    private Slider sliderPop;

    @FXML
    private Slider sliderValence;

    @FXML
    private ToggleGroup vincoloMix;

    @FXML
    private ToggleGroup vincoloSurprise;

    @FXML
    void addTrackMix(ActionEvent event) {

    }

    @FXML
    void avviaRicercaMix(ActionEvent event) {

    }

    @FXML
    void avviaRicercaSurprise(ActionEvent event) {

    }

    @FXML
    void doPopolaArtista(ActionEvent event) {
    	this.cmbArtistSurprise.getItems().clear();
    	this.cmbArtistMix.getItems().clear();
    	
    	String genreSurprise = cmbGenreSurprise.getValue();
    	String genreMix = cmbGenreMix.getValue();
    	
    	if (genreSurprise!=null) {
    		List<String> artists = model.getArtistsByGenre(genreSurprise);
    		cmbArtistSurprise.getItems().addAll(artists);
    	}
    	
    	if (genreMix!=null) {
    		List<String> artists = model.getArtistsByGenre(genreMix);
    		cmbArtistMix.getItems().addAll(artists);
    	}
    	
    }
    

    @FXML
    void doPopolaBrani(ActionEvent event) {
    	this.cmbTrackMix.getItems().clear();
    	
    	String artistMix = cmbArtistMix.getValue();
    	
    	if (artistMix!=null) {
    		List<String> tracks = model.getTracksByArtist(artistMix);
    		cmbTrackMix.getItems().addAll(tracks);
    	}
    	
    }

    @FXML
    void getVincolo(ActionEvent event) {

    }

    @FXML
    void resetChoiceMix(ActionEvent event) {

    }

    @FXML
    void resetPlaylistMix(ActionEvent event) {

    }

    @FXML
    void resetSuprise(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert addTrack != null : "fx:id=\"addTrack\" was not injected: check your FXML file 'Scene.fxml'.";
        assert buttonStartMix != null : "fx:id=\"buttonStartMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert buttonStartSurprise != null : "fx:id=\"buttonStartSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert choiceDurataMaxMix != null : "fx:id=\"choiceDurataMaxMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert choiceDurataMaxSurprise != null : "fx:id=\"choiceDurataMaxSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbArtistMix != null : "fx:id=\"cmbArtistMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbArtistSurprise != null : "fx:id=\"cmbArtistSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenreMix != null : "fx:id=\"cmbGenreMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenreSurprise != null : "fx:id=\"cmbGenreSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNBraniMix != null : "fx:id=\"cmbNBraniMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNBraniSurprise != null : "fx:id=\"cmbNBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbTrackMix != null : "fx:id=\"cmbTrackMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert labelIstruzioni != null : "fx:id=\"labelIstruzioni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert minBraniSurprise != null : "fx:id=\"minBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert minBraniSurprise1 != null : "fx:id=\"minBraniSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert nBraniSurprise != null : "fx:id=\"nBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert nBraniSurprise1 != null : "fx:id=\"nBraniSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert playlistTable != null : "fx:id=\"playlistTable\" was not injected: check your FXML file 'Scene.fxml'.";
        assert progressBarSurprise != null : "fx:id=\"progressBarSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert progressBarSurprise1 != null : "fx:id=\"progressBarSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resetChoiceMix != null : "fx:id=\"resetChoiceMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resetPlaylistMix != null : "fx:id=\"resetPlaylistMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resetSurprise != null : "fx:id=\"resetSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderAcoustic != null : "fx:id=\"sliderAcoustic\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderDance != null : "fx:id=\"sliderDance\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderEnergy != null : "fx:id=\"sliderEnergy\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderPop != null : "fx:id=\"sliderPop\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderValence != null : "fx:id=\"sliderValence\" was not injected: check your FXML file 'Scene.fxml'.";
        assert vincoloMix != null : "fx:id=\"vincoloMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert vincoloSurprise != null : "fx:id=\"vincoloSurprise\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	//Inizializzazione ComboBox "Genere preferito" di "Surprise Me" e "My Mix"
    	List<String> genres = model.getGenres();
    	cmbGenreSurprise.getItems().addAll(genres);
    	cmbGenreMix.getItems().addAll(genres);
    }


}
