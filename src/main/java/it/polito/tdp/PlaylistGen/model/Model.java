package it.polito.tdp.PlaylistGen.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PlaylistGen.db.TracksDAO;

public class Model {
	
	//DB
	private TracksDAO dao;
	
	//Grafo
	private Graph<Vertex, DefaultWeightedEdge> grafo;
	private List<Vertex> vertici;
	
	//Mappa finale in output al controller
	private Map<Integer, Track> idMap;
	
	//Variabili sezione Surprise Me
	private Preference preference;
	
	//Variabili sezione MyMix
	private Map<Integer, Track> tracksInput = new HashMap<Integer, Track>();
	private Integer countInput = 0;
	
	public Model() {
		dao = new TracksDAO();
		idMap = new HashMap<Integer, Track>();
		vertici = new LinkedList<Vertex>();
	}
	
	public void creaGrafoSurprise(String genre, String artist, Preference preference) {
		
		System.out.println("Preferenze Surprise: "+preference.toString());
		
		this.vertici.clear();
		grafo = new SimpleWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Creo Vertici
		Graphs.addAllVertices(this.grafo, this.getVertici(genre, preference));
		System.out.println("Numero vertici: "+this.grafo.vertexSet().size());
		
		//Creo Archi
		this.creaArchi();
	}
	
	public void creaGrafoMix(String genre) {
		
		this.vertici.clear();
		grafo = new SimpleWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		
		//Calcolo preferenze in base ai brani in input
		Preference preference = this.calculatePreference();
		
		//Creo Vertici
		Graphs.addAllVertices(this.grafo, this.getVertici(genre, preference));
		System.out.println("Numero vertici: "+this.grafo.vertexSet().size());
		
		//Creo Archi
		this.creaArchi();
		
	}
	
	private void creaArchi() {
		
		//Creo Archi
		for (Vertex vi: this.grafo.vertexSet()) {
			for (Vertex vj: this.grafo.vertexSet()) {
				Integer viTrackId = vi.getTrack().getTrackId();
				Integer vjTrackId = vj.getTrack().getTrackId();
				if (viTrackId!=vjTrackId) {
					Integer weight = (int) Math.abs(vi.getScore()-vj.getScore());
					Graphs.addEdgeWithVertices(this.grafo, vi, vj, weight);
				}
			}
		}
		System.out.println("Archi pre-ottimizzazione: "+this.grafo.edgeSet().size());
		//Al fine di ottimizzare la ricerca, elimino ogni arco di peso inferiore alla metà della media degli archi
		Integer avg = this.findAvg();
		for (Vertex vi: this.grafo.vertexSet()) {
			for (Vertex vj: this.grafo.vertexSet()) {
				if (this.grafo.getEdge(vi, vj)!=null) {
					if (this.grafo.getEdgeWeight(this.grafo.getEdge(vi, vj)) < avg) {
						this.grafo.removeEdge(vi, vj);
					}
				}	
			}
		}
		System.out.println("Archi post-ottimizzazione: "+this.grafo.edgeSet().size());
	}

	private Preference calculatePreference() {
		
		//Energy
		Double sumEnergy = 0.0;
		//Danceability
		Double sumDanceability = 0.0;
		//Valence
		Double sumValence = 0.0;
		//Acoustic
		Double sumAcoustic = 0.0;		
		//Popularity
		Double sumPopularity = 0.0;
		
		
		for (Track ti: this.idMap.values()) {
			sumEnergy += ti.getEnergy();
			sumDanceability += ti.getDanceability();
			sumValence += ti.getValence();
			sumAcoustic += ti.getAcousticness();
			sumPopularity += ti.getPopularty();
		}
		
		Double avgEnergy = sumEnergy/this.idMap.size();
		Double avgDanceability = sumDanceability/this.idMap.size();
		Double avgValence = sumValence/this.idMap.size();
		Double avgAcoustic = sumAcoustic/this.idMap.size();
		Double avgPopularity = sumPopularity/this.idMap.size();
		
		//Divido ciascun fattore per 10 per rendere i punteggi consistenti con quelli della prima sezione (da 1 a 10)
		Double finalEnergy = (double) Math.round(avgEnergy/10);
		Double finalDanceability = (double) Math.round(avgDanceability/10);
		Double finalValence = (double) Math.round(avgValence/10);
		Double finalAcoustic = (double) Math.round(avgAcoustic/10);
		Double finalPopularity = (double) Math.round(avgPopularity/10);
		
		preference = new Preference(finalEnergy, finalDanceability, finalValence, finalAcoustic, finalPopularity);
		System.out.println("Preferenze Mix post/10: "+preference.toString());
		return preference;
		
	}

	private Integer findAvg() {
		
		Double sum = 0.0;
		for (DefaultWeightedEdge ei: this.grafo.edgeSet()) {
			sum += this.grafo.getEdgeWeight(ei);
		}
		
		Integer avg = (int) Math.round(sum/this.grafo.edgeSet().size());
		return avg;
		
	}
	
	
	

	private List<Vertex> getVertici(String genre, Preference preference) {
		
		if (this.vertici.isEmpty()) {
			List<Track> tracks = dao.listTracksByGenre(genre);
			
			//Calcolo punteggi brani
			for (int i=0; i<tracks.size(); i++) {
				Double punteggio = this.calcolaPunteggio(tracks.get(i), preference);
				Vertex vertice = new Vertex(tracks.get(i), punteggio);
				vertici.add(vertice);
			}
		}
		
		return vertici;
	}

	private Double calcolaPunteggio(Track track, Preference preference) {
		
		Double energy = track.getEnergy()*preference.getEnergy();
		Double dance = track.getDanceability()*preference.getDanceability();
		Double valence = track.getValence()*preference.getValence();
		Double acoustic = track.getAcousticness()*preference.getAcousticness();
		Double popularity = track.getPopularty()*preference.getPopularty();
		
		Double punteggio = energy+dance+valence+acoustic+popularity;
		
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
		if (result!=null && !tracksInput.containsKey(result.getTrackId())) {
			this.tracksInput.put(result.getTrackId(), result);
			countInput++;
			return result;
		}
		return null;
		
	}

	public void resetTableMix() {
		
		this.countInput=0;
		this.tracksInput.clear();
		
	}

	public void createPreference(Double energy, Double dance, Double valence, Double acoustic, Double popularity) {
		
		this.preference = new Preference(energy, dance, valence, acoustic, popularity);
		
	}

	public Preference getPreference() {
		
		if (this.preference!=null) {
			return this.preference;
		}
		return null;
		
	}

	public void addTrackToMapId(Track track) {
		
		this.idMap.put(track.getTrackId(), track);
		
	}
	
	

}
