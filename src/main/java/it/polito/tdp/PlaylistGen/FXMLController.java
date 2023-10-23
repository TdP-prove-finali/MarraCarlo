package it.polito.tdp.PlaylistGen;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.PlaylistGen.model.Model;
import it.polito.tdp.PlaylistGen.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {

	private Model model;
	private Integer vincolo = 0;
	private Boolean isVincoloBrani = null;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button addTrack;

	@FXML
	private Button buttonremoveLastTrack;

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
	private TableView<Track> playlistTableSurprise;

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
	private TextArea textAreaProgressMix;

	@FXML
	private TextArea textAreaProgressSurprise;

	@FXML
	private ToggleGroup vincoloMix;

	@FXML
	private ToggleGroup vincoloSurprise;

	@FXML
	void addTrackMix(ActionEvent event) {

		// Acquisisco valori scelti
		String genre = cmbGenreMix.getValue();
		String artist = cmbArtistMix.getValue();
		String title = cmbTrackMix.getValue();

		if (playlistTableMix.getItems().size() < 5) {
			if (title != null) {

				Track track = model.getTrackbyInput(genre, artist, title);
				if (track != null) {

					// Resetto ErrorLabel
					errorLabelMix.setText(null);

					// Aggiungo tracia a tabella MyMix e tabella di riferimento nel modello
					playlistTableMix.getItems().add(track);
					model.addTrackToMapId(track);

					// Blocco cambio del genere
					cmbGenreMix.setDisable(true);
					cmbGenreMix.setValue(genre);

					// Preparo prossima scelta
					this.resetChoiceMix(event);

				} else {
					errorLabelMix.setText("Brano già inserito!\nRiprovare");
				}
			} else {
				errorLabelMix.setText("Impossibile continuare!\nSelezionare un brano e riprovare");
			}
		} else {
			errorLabelMix.setText("Raggiungo il limite massimo di brani inseribili");
		}

	}

	@FXML
	void avviaRicercaMix(ActionEvent event) {
		
		if (this.findVincolo()) {
			this.disableVincoliMix();
			if (!this.playlistTableMix.getItems().isEmpty()) {
				// Blocco opzione "Aggiungi" e "Reset"
				addTrack.setDisable(true);
				resetChoiceMix.setDisable(true);
				buttonremoveLastTrack.setDisable(true);

				// Creo Grafo
				model.creaGrafoMix(cmbGenreMix.getValue());

				// Avvio ricerca
				List<Track> playlist = new ArrayList<>();
				playlist = model.calcolaPlaylist(null, this.isVincoloBrani, this.vincolo, true);

				// Aggiungo output alla tabella
				this.playlistTableMix.getItems().clear();
				this.playlistTableMix.getItems().addAll(playlist);

				// Informazioni sulla playlist creata
				String durata = model.getDurataPlaylistSec(playlist);
				textAreaProgressMix
						.setText("Playlist Creata!\nNumero brani: " + playlist.size() + ".\nDurata totale: " + durata);

			} else {
				errorLabelMix.setText("Impossibile continuare!\nInserire almeno un brano per continuare");
			}
		}

	}


	@FXML
	void avviaRicercaSurprise(ActionEvent event) {
		
		if (this.findVincolo()) {
			this.disableVincoliSurprise();
			this.collectSliders();
			String genre = cmbGenreSurprise.getValue();
			String artist = cmbArtistSurprise.getValue();
			if (genre != null) {
				// Creo Grafo
				this.model.creaGrafoSurprise(genre, artist, model.getPreference());

				// Avvio ricerca
				List<Track> playlist = new ArrayList<>();
				playlist = model.calcolaPlaylist(artist, this.isVincoloBrani, this.vincolo, false);

				// Aggiungo output alla tabella
				this.playlistTableSurprise.getItems().clear();
				this.playlistTableSurprise.getItems().addAll(playlist);

				// Informazioni sulla playlist creata
				String durata = model.getDurataPlaylistSec(playlist);
				textAreaProgressSurprise
						.setText("Playlist Creata!\nNumero brani: " + playlist.size() + ".\nDurata totale: " + durata);

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

		if (genreSurprise != null) {
			List<String> artists = model.getArtistsByGenre(genreSurprise);
			cmbArtistSurprise.getItems().addAll(artists);
		}

		if (genreMix != null) {
			List<String> artists = model.getArtistsByGenre(genreMix);
			cmbArtistMix.getItems().addAll(artists);
		}

	}

	@FXML
	void doPopolaBrani(ActionEvent event) {

		this.cmbTrackMix.getItems().clear();

		String artistMix = cmbArtistMix.getValue();

		if (artistMix != null) {
			List<String> tracks = model.getTracksByArtist(artistMix);
			cmbTrackMix.getItems().addAll(tracks);
		}

	}

	@FXML
	void removeLastTrack(ActionEvent event) {

		if (playlistTableMix.getItems().size() >= 1) {
			Track lastTrack = playlistTableMix.getItems().get(playlistTableMix.getItems().size() - 1);
			playlistTableMix.getItems().remove(lastTrack);
			model.removeLastTrack(lastTrack);
		}

	}

	@FXML
	Boolean findVincolo() {

		Integer nTab = tabPane.getSelectionModel().getSelectedIndex(); // 0 = Surprise Me, 1 = My Mix

		// Surprise Me
		if (nTab == 0) {
			if (nBraniSurprise.isSelected()) {
				Integer vincolo = cmbNBraniSurprise.getValue();
				if (vincolo != null) {
					errorLabelSurprise.setText(null);
					this.setVincolo(vincolo);
					this.setIsVincoloBrani(true);
					return true;
				} else {
					errorLabelSurprise.setText("Inserire numero di brani massimo richiesto e riprovare");
					return false;
				}
			} else if (minBraniSurprise.isSelected()) {
				String input = choiceDurataMaxSurprise.getText();
				try {
					Integer vincolo = Integer.parseInt(input);
					if (vincolo > 180) {
						errorLabelSurprise.setText("Inserire valore inferiore a 180 minuti e riprovare");
						return false;
					} else {
						errorLabelSurprise.setText(null);
						this.setVincolo(vincolo);
						this.setIsVincoloBrani(false);
						return true;
					}
				} catch (Exception e) {
					errorLabelSurprise.setText("Inserire valore valido e riprovare");
					return false;
				}
			} else {
				errorLabelSurprise.setText("Selezionare un vincolo e riprovare");
				return false;
			}
		}

		// My Mix
		if (nTab == 1) {
			if (nBraniMix.isSelected()) {
				Integer vincolo = cmbNBraniMix.getValue();
				if (vincolo != null) {
					errorLabelMix.setText(null);
					this.setVincolo(vincolo);
					this.setIsVincoloBrani(true);
					return true;
				} else {
					errorLabelMix.setText("Inserire numero di brani massimo richiesto e riprovare");
					return false;
				}
			} else if (minBraniMix.isSelected()) {
				String input = choiceDurataMaxMix.getText();
				try {
					Integer vincolo = Integer.parseInt(input);
					if (vincolo > 180) {
						errorLabelMix.setText("Inserire valore inferiore a 180 minuti e riprovare");
						return false;
					} else {
						errorLabelMix.setText(null);
						this.setVincolo(vincolo);
						this.setIsVincoloBrani(false);
						return true;
					}
				} catch (Exception e) {
					errorLabelMix.setText("Inserire valore valido e riprovare");
					return false;
				}
			} else {
				errorLabelMix.setText("Selezionare un vincolo e riprovare");
				return false;
			}
		}
		
		return false;

	}

	@FXML
	void resetChoiceMix(ActionEvent event) {

		// Reset scelta brano
		if (!cmbGenreMix.isDisabled()) { 
			cmbGenreMix.setValue(null);
			cmbArtistMix.getItems().clear();
		} else {
			this.doPopolaArtista(event);
		}
		cmbTrackMix.getItems().clear();

		// Reset error label e text area
		errorLabelMix.setText(null);
	}

	@FXML
	void resetPlaylistMix(ActionEvent event) {

		playlistTableMix.getItems().clear();

		// Reset textArea e Label
		textAreaProgressMix.setText(null);
		errorLabelMix.setText(null);

		// Resetto mappa degli input nel model
		model.resetTableMix();

		// Rendo modificabile ComboBox genere
		cmbGenreMix.setDisable(false);
		cmbGenreMix.setPromptText(null);
		cmbGenreMix.setValue(null);

		// Attivazione bottoni "Aggiungi" e "Reset"
		addTrack.setDisable(false);
		resetChoiceMix.setDisable(false);
		buttonremoveLastTrack.setDisable(false);

		// Reset scelta vincolo
		cmbNBraniMix.setValue(null);
		choiceDurataMaxMix.setText(null);
		vincoloMix.selectToggle(null);
		this.enableVincoliMix();

	}

	@FXML
	void resetSuprise(ActionEvent event) {

		// Reset ComboBox
		cmbGenreSurprise.setValue(null);
		cmbArtistSurprise.getItems().clear();

		// Reset Vincoli, Error Label e Text Area
		cmbNBraniSurprise.setValue(null);
		choiceDurataMaxSurprise.setText(null);
		vincoloSurprise.selectToggle(null);
		errorLabelSurprise.setText(null);
		textAreaProgressSurprise.setText(null);
		this.enableVincoliSurprise();

		// Reset Sliders e Table
		this.playlistTableSurprise.getItems().clear();
		sliderEnergy.setValue(1);
		sliderDance.setValue(1);
		sliderValence.setValue(1);
		sliderAcoustic.setValue(1);
		sliderPop.setValue(1);
		

	}
	
	private void disableVincoliMix() {
		this.choiceDurataMaxMix.setDisable(true);;
		this.minBraniMix.setDisable(true);
		this.nBraniMix.setDisable(true);
		this.cmbNBraniMix.setDisable(true);
		this.buttonStartMix.setDisable(true);
	}
	
	private void enableVincoliMix() {
		this.choiceDurataMaxMix.setDisable(false);;
		this.minBraniMix.setDisable(false);
		this.nBraniMix.setDisable(false);
		this.cmbNBraniMix.setDisable(false);
		this.buttonStartMix.setDisable(false);
	}
	
	private void disableVincoliSurprise() {
		this.choiceDurataMaxSurprise.setDisable(true);;
		this.minBraniSurprise.setDisable(true);
		this.nBraniSurprise.setDisable(true);
		this.cmbNBraniSurprise.setDisable(true);
		this.buttonStartSurprise.setDisable(true);
	}
	
	private void enableVincoliSurprise() {
		this.choiceDurataMaxSurprise.setDisable(false);;
		this.minBraniSurprise.setDisable(false);
		this.nBraniSurprise.setDisable(false);
		this.cmbNBraniSurprise.setDisable(false);
		this.buttonStartSurprise.setDisable(false);
	}

	@FXML
	void initialize() {
		assert addTrack != null : "fx:id=\"addTrack\" was not injected: check your FXML file 'Scene.fxml'.";
		assert buttonremoveLastTrack != null
				: "fx:id=\"buttonremoveLastTrack\" was not injected: check your FXML file 'Scene.fxml'.";
		assert buttonStartMix != null : "fx:id=\"buttonStartMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert buttonStartSurprise != null
				: "fx:id=\"buttonStartSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert choiceDurataMaxMix != null
				: "fx:id=\"choiceDurataMaxMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert choiceDurataMaxSurprise != null
				: "fx:id=\"choiceDurataMaxSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert columnArtistMix != null
				: "fx:id=\"columnArtistMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert columnArtistSurprise != null
				: "fx:id=\"columnArtistSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert columnTrackMix != null : "fx:id=\"columnTrackMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert columnTrackSurprise != null
				: "fx:id=\"columnTrackSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbArtistMix != null : "fx:id=\"cmbArtistMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbArtistSurprise != null
				: "fx:id=\"cmbArtistSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbGenreMix != null : "fx:id=\"cmbGenreMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbGenreSurprise != null
				: "fx:id=\"cmbGenreSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbNBraniMix != null : "fx:id=\"cmbNBraniMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbNBraniSurprise != null
				: "fx:id=\"cmbNBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbTrackMix != null : "fx:id=\"cmbTrackMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert labelIstruzioniSurprise != null
				: "fx:id=\"labelIstruzioniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert labelIstruzioniMix != null
				: "fx:id=\"labelIstruzioniMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert errorLabelSurprise != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'Scene.fxml'.";
		assert errorLabelMix != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'Scene.fxml'.";
		assert minBraniSurprise != null
				: "fx:id=\"minBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert minBraniMix != null : "fx:id=\"minBraniSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
		assert nBraniSurprise != null : "fx:id=\"nBraniSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert nBraniMix != null : "fx:id=\"nBraniSurprise1\" was not injected: check your FXML file 'Scene.fxml'.";
		assert playlistTableMix != null
				: "fx:id=\"playlistTableMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert playlistTableSurprise != null
				: "fx:id=\"playlistTableSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert resetChoiceMix != null : "fx:id=\"resetChoiceMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert resetPlaylistMix != null
				: "fx:id=\"resetPlaylistMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert resetSurprise != null : "fx:id=\"resetSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert sliderAcoustic != null : "fx:id=\"sliderAcoustic\" was not injected: check your FXML file 'Scene.fxml'.";
		assert sliderDance != null : "fx:id=\"sliderDance\" was not injected: check your FXML file 'Scene.fxml'.";
		assert sliderEnergy != null : "fx:id=\"sliderEnergy\" was not injected: check your FXML file 'Scene.fxml'.";
		assert sliderPop != null : "fx:id=\"sliderPop\" was not injected: check your FXML file 'Scene.fxml'.";
		assert sliderValence != null : "fx:id=\"sliderValence\" was not injected: check your FXML file 'Scene.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'Scene.fxml'.";
		assert textAreaProgressMix != null
				: "fx:id=\"textAreaProgressMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert textAreaProgressSurprise != null
				: "fx:id=\"textAreaProgressSurprise\" was not injected: check your FXML file 'Scene.fxml'.";
		assert vincoloMix != null : "fx:id=\"vincoloMix\" was not injected: check your FXML file 'Scene.fxml'.";
		assert vincoloSurprise != null
				: "fx:id=\"vincoloSurprise\" was not injected: check your FXML file 'Scene.fxml'.";

		// Inizializzo Tabelle
		columnArtistSurprise.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
		columnArtistMix.setCellValueFactory(new PropertyValueFactory<Track, String>("artist"));
		columnTrackSurprise.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));
		columnTrackMix.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));

		// Inizializzo Label Surprise e My Mix
		String istruzioniSurprise = "Benvenuto nella sezione Surprise Me! "
				+ "Seleziona il tuo genere preferito e, facoltativamente, il relativo artista.\n"
				+ "Dichiara poi il grado di presenza richiesta attraverso un punteggio da 1 a 10 delle cinque caratteristiche principali:\n"
				+ "- Energy: Più alto è il valore, più sarà energica la playlist.\n"
				+ "- Danceability: Più alto è il valore, più facile sarà ballare.\n"
				+ "- Valence: Più alto è il valore, più positivo sarà l'umore della playlist.\n"
				+ "- Acoustic: Più alto è il valore, più la playlist comprenderà musica che usa principalmente strumenti acustici, anziché elettrici o elettronici.\n"
				+ "- Popularity: Più alto è il valore, più popolari saranno i brani della playlist.\n"
				+ "Seleziona infine il vincolo che preferisci tra un numero di canzoni o un minutaggio massimo e clicca \"Crea playlist\". \n"
				+ "ATTENZIONE: Ai fini di ottenere un risultato in tempi ragionevoli, si sconsiglia di richiedere una playlist di durata massima di più di 3 ore (180 minuti).";
		labelIstruzioniSurprise.setText(istruzioniSurprise);

		String istruzioniMix = "Benvenuto nella sezione MyMix! \n"
				+ "Aggiungi all’interno della tabella fino a 5 brani del tuo genere preferito.\n"
				+ "Seleziona infine il vincolo che preferisci tra un numero di canzoni o un minutaggio massimo e clicca “Crea Playlist”.\n";
		labelIstruzioniMix.setText(istruzioniMix);
	}

	public void setModel(Model model) {
		this.model = model;

		// Inizializzazione ComboBox "Genere preferito" di "Surprise Me" e "My Mix"
		List<String> genres = model.getGenres();
		cmbGenreSurprise.getItems().addAll(genres);
		cmbGenreMix.getItems().addAll(genres);

		// Inizializzazione ComboBox vincolo "Numero Brani"
		for (int i = 10; i <= 40; i = i + 2) {
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
