package it.polito.tdp.PlaylistGen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PlaylistGen.db.TracksDAO;

public class Model {

	// DB
	private TracksDAO dao;

	// Grafo
	private Graph<Vertex, DefaultWeightedEdge> grafo;
	private List<Vertex> vertici;
	private List<Vertex> bestPlaylist = new LinkedList<Vertex>();
	Double bestScore = Double.MAX_VALUE;

	// Lista finale in output al controller
	private List<Track> finalPlaylist = new ArrayList<>();;

	// Variabili sezione Surprise Me
	private Preference preference;

	// Variabili sezione MyMix
	private Map<Integer, Track> tracksInput = new HashMap<Integer, Track>();
	private Integer countInput = 0;

	public Model() {
		grafo = new SimpleWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		dao = new TracksDAO();
		bestPlaylist = new LinkedList<Vertex>();
		finalPlaylist = new ArrayList<Track>();
		vertici = new LinkedList<Vertex>();
	}

	public void creaGrafoSurprise(String genre, String artist, Preference preference) {

		this.vertici.clear();
		grafo = new SimpleWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		// Creo Vertici
		Graphs.addAllVertices(this.grafo, this.getVertici(genre, preference));
		
		// Creo Archi
		this.creaArchi();
	}

	public void creaGrafoMix(String genre) {

		this.vertici.clear();
		grafo = new SimpleWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// Calcolo preferenze in base ai brani in input
		Preference preference = this.calcolaPreferenze();

		// Creo Vertici
		Graphs.addAllVertices(this.grafo, this.getVertici(genre, preference));

		// Creo Archi
		this.creaArchi();

	}

	private void creaArchi() {

		// Creo Archi
		for (Vertex vi : this.grafo.vertexSet()) {
			for (Vertex vj : this.grafo.vertexSet()) {
				Integer viTrackId = vi.getTrack().getTrackId();
				Integer vjTrackId = vj.getTrack().getTrackId();
				if (viTrackId != vjTrackId) {
					Double weight = Math.abs(vi.getScore() - vj.getScore());
					Graphs.addEdgeWithVertices(this.grafo, vi, vj, weight);
				}
			}
		}
		// Al fine di ottimizzare la ricerca, elimino ogni arco di peso inferiore alla
		// media degli archi
		Integer avg = this.trovaAvg();
		for (Vertex vi : this.grafo.vertexSet()) {
			for (Vertex vj : this.grafo.vertexSet()) {
				if (this.grafo.getEdge(vi, vj) != null) {
					if (this.grafo.getEdgeWeight(this.grafo.getEdge(vi, vj)) < avg) {
						this.grafo.removeEdge(vi, vj);
					}
				}
			}
		}
	}

	private Preference calcolaPreferenze() {
		// Funzione per calcolare le preferenze dell'utente basandosi sui brani in input

		// Energy
		Double sumEnergy = 0.0;
		// Danceability
		Double sumDanceability = 0.0;
		// Valence
		Double sumValence = 0.0;
		// Acoustic
		Double sumAcoustic = 0.0;
		// Popularity
		Double sumPopularity = 0.0;

		for (Track ti : this.tracksInput.values()) {
			sumEnergy += ti.getEnergy();
			sumDanceability += ti.getDanceability();
			sumValence += ti.getValence();
			sumAcoustic += ti.getAcousticness();
			sumPopularity += ti.getPopularty();
		}

		Double avgEnergy = sumEnergy / this.tracksInput.size();
		Double avgDanceability = sumDanceability / this.tracksInput.size();
		Double avgValence = sumValence / this.tracksInput.size();
		Double avgAcoustic = sumAcoustic / this.tracksInput.size();
		Double avgPopularity = sumPopularity / this.tracksInput.size();

		// Divido ciascun fattore per 10 per rendere i punteggi consistenti con quelli
		// della prima sezione (da 1 a 10)
		Double finalEnergy = (double) Math.round(avgEnergy / 10);
		Double finalDanceability = (double) Math.round(avgDanceability / 10);
		Double finalValence = (double) Math.round(avgValence / 10);
		Double finalAcoustic = (double) Math.round(avgAcoustic / 10);
		Double finalPopularity = (double) Math.round(avgPopularity / 10);

		preference = new Preference(finalEnergy, finalDanceability, finalValence, finalAcoustic, finalPopularity);
		return preference;

	}

	private Integer trovaAvg() {
		// Trova media dei pesi degli archi del grafo creato

		Double sum = 0.0;
		for (DefaultWeightedEdge ei : this.grafo.edgeSet()) {
			sum += this.grafo.getEdgeWeight(ei);
		}

		Integer avg = (int) Math.round(sum / this.grafo.edgeSet().size());
		return avg;

	}

	private List<Vertex> getVertici(String genre, Preference preference) {

		if (this.vertici.isEmpty()) {
			List<Track> tracks = dao.listTracksByGenre(genre);

			// Calcolo punteggi brani
			for (int i = 0; i < tracks.size(); i++) {
				Double punteggio = this.calcolaPunteggio(tracks.get(i), preference);
				Vertex vertice = new Vertex(tracks.get(i), punteggio);
				vertici.add(vertice);
			}
		}

		return vertici;
	}

	private Double calcolaPunteggio(Track track, Preference preference) {

		Double energy = track.getEnergy() * preference.getEnergy();
		Double dance = track.getDanceability() * preference.getDanceability();
		Double valence = track.getValence() * preference.getValence();
		Double acoustic = track.getAcousticness() * preference.getAcousticness();
		Double popularity = track.getPopularty() * preference.getPopularty();

		Double punteggio = energy + dance + valence + acoustic + popularity;

		return punteggio;

	}

	public List<String> getGenres() {

		List<String> result = dao.listAllGenre();
		Collections.sort(result);
		return result;

	}

	public List<String> getArtistsByGenre(String genre) {

		List<String> result = dao.listAllArtistByGenre(genre);
		Collections.sort(result);
		return result;

	}

	public List<String> getTracksByArtist(String artist) {

		List<String> result = dao.listAllTracksByArtist(artist);
		Collections.sort(result);
		return result;

	}

	public Track getTrackbyInput(String genre, String artist, String title) {

		Track result = dao.trackByInput(genre, artist, title);
		if (result != null && !tracksInput.containsKey(result.getTrackId())) {
			this.tracksInput.put(result.getTrackId(), result);
			countInput++;
			return result;
		}
		return null;

	}

	public void resetTableMix() {

		this.countInput = 0;
		this.tracksInput.clear();

	}

	public void createPreference(Double energy, Double dance, Double valence, Double acoustic, Double popularity) {

		this.preference = new Preference(energy, dance, valence, acoustic, popularity);

	}

	public Preference getPreference() {

		if (this.preference != null) {
			return this.preference;
		}
		return null;

	}

	public void addTrackToMapId(Track track) {

		this.tracksInput.put(track.getTrackId(), track);

	}

	public Integer getNVertici() {
		if (this.grafo != null) {
			return this.grafo.vertexSet().size();
		}
		return null;
	}

	public Integer getNArchi() {
		if (this.grafo != null) {
			return this.grafo.edgeSet().size();
		}
		return null;
	}

	public List<Track> calcolaPlaylist(String artist, Boolean isVincoloBrani, Integer vincolo) {

		this.resetSearch();

		List<Vertex> canzoniValide = new ArrayList<Vertex>();
		ConnectivityInspector<Vertex, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		List<Vertex> parziale = new ArrayList<>();
		
		//Controllo se i vincoli richiesti sono raggiungibili dal grafo
		if (isVincoloBrani && this.getNVertici()<vincolo) {
			System.out.println("Vincolo richiesto: "+vincolo);
			System.out.println("Massimi brani possibili: "+ this.getNVertici());
			vincolo = this.getNVertici();
			System.out.println("Nuovo vincolo brani: "+vincolo);
		} else if (!isVincoloBrani && this.getTotalDurataGraph() < vincolo) {
			System.out.println("Vincolo richiesto: "+vincolo);
			System.out.println("Massima durata possibile: "+ this.getTotalDurataGraph());
			vincolo = this.getTotalDurataGraph();
			System.out.println("Nuovo vincolo minuti: "+vincolo);
			
		}

		// Sezione Mix, aggiungo vertici corrispondenti ai brani inseriti
		if (!this.tracksInput.isEmpty()) {
			for (Track ti : this.tracksInput.values()) {
				for (Vertex vi : this.getVertici(artist, preference)) {
					if (ti.equals(vi.getTrack())) {
						parziale.add(vi);
					}
				}
			}
			// Comincio ricerca a partire dall'ultimo brano inserito
			canzoniValide.addAll(ci.connectedSetOf(parziale.get(parziale.size() - 1)));

		} else { // Sezione SurpriseMe, aggiungo miglior inizio possibile
			Vertex start = findBestStart(artist);
			parziale.add(start);

			// Trovo componente connessa di "start"
			canzoniValide.addAll(ci.connectedSetOf(start));

		}

		calcola(parziale, canzoniValide, isVincoloBrani, vincolo);

		for (Vertex vi : this.bestPlaylist) {
			this.finalPlaylist.add(vi.getTrack());
		}
		return this.finalPlaylist;

	}

	public Integer getTotalDurataGraph() {
		
		Integer totalDurata = 0;
		
		for (Vertex vi: this.grafo.vertexSet()) {
			totalDurata += vi.getTrack().getLenght();
		}
		
		return totalDurata/60;
	}

	private void resetSearch() {

		this.bestPlaylist.clear();
		this.finalPlaylist.clear();
		this.bestScore = Double.MAX_VALUE;

	}

	private void calcola(List<Vertex> parziale, List<Vertex> canzoniValide, Boolean isVincoloBrani, Integer vincolo) {

		for (Vertex vi : canzoniValide) {
			if (!parziale.contains(vi)) {
				if (!this.isVincoloSuperato(vi, parziale, isVincoloBrani, vincolo)) {
					parziale.add(vi);
					Double sommaArchiProvvisoria = this.getSommaArchi(parziale);
					if (sommaArchiProvvisoria < bestScore) {
						this.calcola(parziale, canzoniValide, isVincoloBrani, vincolo);
					}
					parziale.remove(parziale.size() - 1);
				} else {
					// Controllo soluzione migliore La soluzione migliore corrisponde al cammino con
					// somma dei pesi degli archi più bassa
					Double sommaArchiProvvisoria = this.getSommaArchi(parziale);
					if (sommaArchiProvvisoria < bestScore) {
						this.bestPlaylist = new ArrayList<>(parziale);
						this.bestScore = sommaArchiProvvisoria;
					}
				}
			}
		}

	}

	private Double getSommaArchi(List<Vertex> canzoni) {

		Double sommaArchi = 0.0;

		for (int i = 0; i < canzoni.size(); i++) {
			if (i != canzoni.size() - 1) {
				DefaultWeightedEdge edge = this.grafo.getEdge(canzoni.get(i), canzoni.get(i + 1));
				if (edge != null) {
					sommaArchi += this.grafo.getEdgeWeight(edge);
				}
			}
		}

		return sommaArchi;
	}

	private boolean isVincoloSuperato(Vertex nextTrack, List<Vertex> parziale, Boolean isVincoloBrani,
			Integer vincolo) {

		if (isVincoloBrani && (parziale.size() + 1) > vincolo) {
			return true;
		}
		if (!isVincoloBrani && (this.sommaDurataInMin(parziale) + (nextTrack.getTrack().getLenght() / 60) >= vincolo)) {
			return true;
		}

		return false;
	}

	private Integer sommaDurataInMin(List<Vertex> tracks) {
		Integer somma = 0;

		for (Vertex vi : tracks) {
			somma += vi.getTrack().getLenght();
		}

		return somma / 60;
	}

	private Vertex findBestStart(String artist) {

		Double bestScore = Double.MIN_VALUE;
		Vertex bestVertex = null;

		// Sezione Surprise
		if (this.tracksInput.isEmpty()) {
			// Artista selezionato
			if (artist != null) {
				for (Vertex vi : this.grafo.vertexSet()) {
					if (vi.getScore() > bestScore && vi.getTrack().getArtist().equals(artist)) {
						bestScore = vi.getScore();
						bestVertex = vi;
					}
				}
				// Artista non selezionato
			} else {
				for (Vertex vi : this.grafo.vertexSet()) {
					if (vi.getScore() > bestScore) {
						bestScore = vi.getScore();
						bestVertex = vi;
					}
				}
			}
			// Sezione MyMix
		} else {
			for (Vertex vi : this.grafo.vertexSet()) {
				if (this.tracksInput.containsKey(vi.getTrack().getTrackId())) {
					if (vi.getScore() > bestScore) {
						bestScore = vi.getScore();
						bestVertex = vi;
					}
				}
			}
		}

		return bestVertex;
	}

	public String getDurataPlaylistSec(List<Track> playlist) {

		Integer durata = 0;
		Integer numeroOre;
		Integer numeroMinuti;
		Integer numeroSecondi;

		for (Track ti : playlist) {
			durata += ti.getLenght();
		}

		numeroOre = (durata % 86400) / 3600;
		numeroMinuti = ((durata % 86400) % 3600) / 60;
		numeroSecondi = ((durata % 86400) % 3600) % 60;

		String output = numeroOre + " ore, " + numeroMinuti + " minuti e " + numeroSecondi + " secondi.";

		return output;
	}

	public void removeLastTrack(Track lastTrack) {
		
		this.tracksInput.remove(lastTrack.getTrackId());
		
	}

}
