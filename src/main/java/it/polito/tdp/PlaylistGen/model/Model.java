package it.polito.tdp.PlaylistGen.model;

import java.util.Collections;
import java.util.List;

import it.polito.tdp.PlaylistGen.db.TracksDAO;

public class Model {
	
	private TracksDAO dao;
	
	public Model() {
		dao = new TracksDAO();
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

}
