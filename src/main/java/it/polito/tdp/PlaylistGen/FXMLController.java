package it.polito.tdp.PlaylistGen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PlaylistGen.model.Model;
import it.polito.tdp.PlaylistGen.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {
	
	private Model model;
	private Integer vincolo;
	private Boolean isVincoloBrani;
	
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
    private ComboBox<Integer> cmbNBraniMix;

    @FXML
    private ComboBox<Integer> cmbNBraniSurprise;

    @FXML
    private ComboBox<String> cmbTrackMix;

    @FXML
    private Label labelIstruzioniSurprise;
    
    @FXML
    private Label labelIstruzioniMix;
    
    @FXML
    private Label errorLabelSurprise;
    
    @FXML
    private Label errorLabelMix;

    @FXML
    private RadioButton minBraniSurprise;

    @FXML
    private RadioButton minBraniMix;

    @FXML
    private RadioButton nBraniSurprise;

    @FXML
    private RadioButton nBraniMix;

    @FXML
    private TableView<Track> playlistTableMix;
    
    @FXML
    private TableView<Track> playlistTableSurpise;
    
    @FXML
    private TableColumn<Track, String> columnArtistMix;

    @FXML
    private TableColumn<Track, String> columnArtistSurprise;

    @FXML
    private TableColumn<Track, String> columnTrackMix;

    @FXML
    private TableColumn<Track, String> columnTrackSurprise;
    
    @FXML
    private TabPane tabPane;

    @FXML
    private ProgressBar progressBarSurprise;

    @FXML
    private ProgressBar progressBarMix;

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

    	String genre = cmbGenreMix.getValue();
    	String artist = cmbArtistMix.getValue();
    	String title = cmbTrackMix.getValue();
    	
    	if (genre!=null && artist!=null && title!=null) {
    		
    		Track track = model.getTrackbyInput(genre, artist, title);
    		if (track!=null) {
    			errorLabelMix.setText(null);
    			playlistTableMix.getItems().add(track);
    			model.addTrackToMapId(track);
    			cmbGenreMix.setDisable(true);
    			cmbGenreMix.setValue(genre);
    			this.resetChoiceMix(event);
    		} else {
    			errorLabelMix.setText("Brano già inserito!\nRiprovare");
    		}
    	} else {
    		errorLabelMix.setText("Impossibile continuare!\nSelezionare un brano e riprovare");
    	}
    	
    }

    @FXML
    void avviaRicercaMix(ActionEvent event) {
    	
    	this.findVincolo();
    	if (this.vincolo!=null) {
    		if (!this.playlistTableMix.getItems().isEmpty()) {
    			model.creaGrafoMix(cmbGenreMix.getValue());
    		} else {
    			errorLabelMix.setText("Impossibile continuare!\nInserire almeno un brano per continuare");
    		}
    	}

    }

    @FXML
    void avviaRicercaSurprise(ActionEvent event) {
    	
    	this.findVincolo();
    	if (this.vincolo!=null) {
    		this.collectSliders();
    		String genre = cmbGenreSurprise.getValue();
    		String artist = cmbGenreSurprise.getValue();
    		if (genre!=null) {
    			resetSurprise.setDisable(true);
    			this.progressBarSurprise.setProgress(-1);
    			this.model.creaGrafoSurprise(genre, artist, model.getPreference());
    			this.progressBarSurprise.setProgress(0);
    			
    		} else {
    			errorLabelSurprise.setText("Selezionare un genere e riprovare");
    		}
    	}
    	

    }

    private void collectSliders() {
		
    	Double energy = sliderEnergy.getValue();
    	Double dance = sliderDance.getValue();
    	Double valence = sliderValence.getValue();
    	Double acoustic = sliderAcoustic.getValue();
    	Double popularity = sliderPop.getValue();
    	
    	model.createPreference(energy, dance, valence, acoustic, popularity);

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
    void findVincolo() {
    	
    	Integer nTab = tabPane.getSelectionModel().getSelectedIndex(); //0 = Surprise Me, 1 = My Mix
    	
    	//Surprise Me
    	if (nTab == 0) {
	    	if (nBraniSurprise.isSelected()) {
	    		Integer vincolo = cmbNBraniSurprise.getValue();
	    		if (vincolo!=null) {
	    			errorLabelSurprise.setText(null);
	    			this.setVincolo(vincolo);
	    			this.setIsVincoloBrani(true);
	    		} else {
	    			errorLabelSurprise.setText("Inserire numero di brani massimo richiesto e riprovare");
	    		}
	    	} else if (minBraniSurprise.isSelected()) {
	    		String input = choiceDurataMaxSurprise.getText();
	    		try {
					Integer vincolo = Integer.parseInt(input);
					errorLabelSurprise.setText(null);
					this.setVincolo(vincolo);
					this.setIsVincoloBrani(false);
				} catch (Exception e) {
					errorLabelSurprise.setText("Inserire valore valido e riprovare");
				}
	    	} else {
	    		errorLabelSurprise.setText("Selezionare un vincolo e riprovare");
	    		
	    	}
    	}
    	
    	//My Mix
    	if (nTab == 1) {
    		if (nBraniMix.isSelected()) {
	    		Integer vincolo = cmbNBraniMix.getValue();
	    		if (vincolo!=null) {
	    			errorLabelMix.setText(null);
	    			this.setVincolo(vincolo);
	    			this.setIsVincoloBrani(true);
	    		} else {
	    			errorLabelMix.setText("Inserire numero di brani massimo richiesto e riprovare");
	    		}
	    	} else if (minBraniSurprise.isSelected()) {
	    		String input = choiceDurataMaxMix.getText();
	    		try {
					Integer vincolo = Integer.parseInt(input);
					errorLabelMix.setText(null);
					this.setVincolo(vincolo);
					this.setIsVincoloBrani(false);
				} catch (Exception e) {
					errorLabelMix.setText("Inserire valore valido e riprovare");
				}
	    	} else {
	    		errorLabelMix.setText("Selezionare un vincolo e riprovare");
	    	}
    	}
    	

    }

    @FXML
    void resetChoiceMix(ActionEvent event) {
    	
    	//Reset scelta brano
    	if (!cmbGenreMix.isDisabled()) {
    		cmbGenreMix.setValue(null);
    		cmbArtistMix.getItems().clear();
    	} else {
    		this.doPopolaArtista(event);
    	}
    	cmbTrackMix.getItems().clear();
    	
    	
    	//Reset error label
    	errorLabelMix.setText(null);
    	
    }

    @FXML
    void resetPlaylistMix(ActionEvent event) {
    	
    	playlistTableMix.getItems().clear();
    	
    	//Resetto mappa degli input nel model
    	model.resetTableMix();
    	
    	//Rendo modificabile ComboBox genere
    	cmbGenreMix.setDisable(false);
    	cmbGenreMix.setPromptText(null);
    	cmbGenreMix.setValue(null);
    	
    	//Reset scelta vincolo
    	cmbNBraniMix.setValue(null);
    	choiceDurataMaxMix.setText(null);
    	vincoloMix.selectToggle(null);

    }

    @FXML
    void resetSuprise(ActionEvent event) {

    	//Reset ComboBox
    	cmbGenreSurprise.setValue(null);
    	cmbArtistSurprise.getItems().clear();
    	
    	//Reset Vincoli e Error Label
    	cmbNBraniSurprise.setValue(null);
    	choiceDurataMaxSurprise.setText(null);
    	vincoloSurprise.selectToggle(null);
    	errorLabelSurprise.setText(null);
    	
    	//Reset Sliders
    	sliderEnergy.setValue(1);
    	sliderDance.setValue(1);
    	sliderValence.setValue(1);
    	sliderAcoustic.setValue(1);
    	sliderPop.setValue(1);
    	
    }

    @FXML
    void initialize() {
        assert addTrack != null : "fx:id=\"addTrack\" was not injected: check your FXML file 'Scene.fxml'.";
        assert buttonStartMix != null : "fx:id=\"buttonStartMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert buttonStartSurprise != null : "fx:id=\"buttonStartSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert choiceDurataMaxMix != null : "fx:id=\"choiceDurataMaxMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert choiceDurataMaxSurprise != null : "fx:id=\"choiceDurataMaxSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert columnArtistMix != null : "fx:id=\"columnArtistMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert columnArtistSurprise != null : "fx:id=\"columnArtistSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert columnTrackMix != null : "fx:id=\"columnTrackMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert columnTrackSurprise != null : "fx:id=\"columnTrackSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbArtistMix != null : "fx:id=\"cmbArtistMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbArtistSurprise != null : "fx:id=\"cmbArtistSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenreMix != null : "fx:id=\"cmbGenreMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenreSurprise != null : "fx:id=\"cmbGenreSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNBraniMix != null : "fx:id=\"cmbNBraniMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNBraniSurprise != null : "fx:id=\"cmbNBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbTrackMix != null : "fx:id=\"cmbTrackMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert labelIstruzioniSurprise != null : "fx:id=\"labelIstruzioniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert labelIstruzioniMix != null : "fx:id=\"labelIstruzioniMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert errorLabelSurprise != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'Scene.fxml'.";
        assert errorLabelMix != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'Scene.fxml'.";
        assert minBraniSurprise != null : "fx:id=\"minBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert minBraniMix != null : "fx:id=\"minBraniSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert nBraniSurprise != null : "fx:id=\"nBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert nBraniMix != null : "fx:id=\"nBraniSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert playlistTableMix != null : "fx:id=\"playlistTableMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert playlistTableSurpise != null : "fx:id=\"playlistTableSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert progressBarSurprise != null : "fx:id=\"progressBarSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert progressBarMix != null : "fx:id=\"progressBarMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resetChoiceMix != null : "fx:id=\"resetChoiceMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resetPlaylistMix != null : "fx:id=\"resetPlaylistMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resetSurprise != null : "fx:id=\"resetSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderAcoustic != null : "fx:id=\"sliderAcoustic\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderDance != null : "fx:id=\"sliderDance\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderEnergy != null : "fx:id=\"sliderEnergy\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderPop != null : "fx:id=\"sliderPop\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sliderValence != null : "fx:id=\"sliderValence\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'Scene.fxml'.";
        assert vincoloMix != null : "fx:id=\"vincoloMix\" was not injected: check your FXML file 'Scene.fxml'.";
        assert vincoloSurprise != null : "fx:id=\"vincoloSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
        
        //Inizializzo Tabelle
        columnArtistSurprise.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
        columnArtistMix.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
        columnTrackSurprise.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));
        columnTrackMix.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));

        //Inizializzo Label Surprise e My Mix
        String istruzioniSurprise = "Benvenuto nella sezione Surprise Me! "
        		+ "Seleziona il tuo genere preferito e, facoltativamente, il relativo artista.\n"
        		+ "Dichiara poi il grado di presenza richiesta attraverso un punteggio da 1 a 10 delle cinque caratteristiche principali:\n"
        		+ "- Energy: Più alto è il valore, più sarà energica la playlist.\n"
        		+ "- Danceability: Più alto è il valore, più facile sarà ballare.\n"
        		+ "- Valence: Più alto è il valore, più positivo sarà l'umore della playlist.\n"
        		+ "- Acoustic: Più alto è il valore, più la playlist comprenderà musica che usa principalmente strumenti acustici, anziché elettrici o elettronici.\n"
        		+ "- Popularity: Più alto è il valore, più popolari saranno i brani della playlist.\n"
        		+ "Seleziona infine il vincolo che preferisci tra un numero di canzoni o un minutaggio massimo e clicca \"Crea playlist\". ";
        labelIstruzioniSurprise.setText(istruzioniSurprise);
        
        String istruzioniMix = "Benvenuto nella sezione MyMix! \n"
        		+ "Aggiungi all’interno della tabella i brani del tuo genere preferito.\n"
        		+ "Seleziona infine il vincolo che preferisci tra un numero di canzoni o un minutaggio massimo e clicca “Crea Playlist”.\n";
        labelIstruzioniMix.setText(istruzioniMix);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	//Inizializzazione ComboBox "Genere preferito" di "Surprise Me" e "My Mix"
    	List<String> genres = model.getGenres();
    	cmbGenreSurprise.getItems().addAll(genres);
    	cmbGenreMix.getItems().addAll(genres);
    	
    	//Inizializzazione ComboBox vincolo "Numero Brani"
    	for (int i=5; i<=20; i++) {
    		cmbNBraniSurprise.getItems().add(i);
    		cmbNBraniMix.getItems().add(i);
    	}
    }

	public Integer getVincolo() {
		return vincolo;
	}

	public void setVincolo(Integer vincolo) {
		this.vincolo = vincolo;
	}

	public Boolean getIsVincoloBrani() {
		return isVincoloBrani;
	}

	public void setIsVincoloBrani(Boolean isVincoloBrani) {
		this.isVincoloBrani = isVincoloBrani;
	}
}
