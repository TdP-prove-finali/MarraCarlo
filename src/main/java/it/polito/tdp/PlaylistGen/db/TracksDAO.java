package it.polito.tdp.PlaylistGen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.PlaylistGen.model.Track;

public class TracksDAO {
	
	public List<String> listAllGenre(){
		
		String sql = "SELECT Tracks.`Top Genre`, COUNT(Tracks.`Top Genre`) "
				+ "FROM Tracks "
				+ "GROUP BY Tracks.`Top Genre` "
				+ "HAVING COUNT(Tracks.`Top Genre`) >= 20 ";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				String genre = new String(res.getString("Top Genre"));
				
				result.add(genre);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllArtistByGenre(String genre){
		
		String sql = "SELECT DISTINCT Tracks.`Artist`\n"
				+ "FROM Tracks\n"
				+ "WHERE Tracks.`Top Genre` = ? ";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				String artist = new String(res.getString("Artist"));
				
				result.add(artist);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllTracksByArtist(String artist){
		
		String sql = "SELECT DISTINCT Tracks.`Title`\n"
				+ "FROM Tracks\n"
				+ "WHERE Tracks.`Artist` = ? ";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, artist);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				String track = new String(res.getString("Title"));
				
				result.add(track);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Track trackByInput(String genre, String artist, String title) {
		
		String sql = "SELECT *\n"
				+ " FROM Tracks\n"
				+ " WHERE Tracks.`Top Genre`= ? AND Tracks.`Artist`= ? AND Tracks.`Title`=? ";
		Track result;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			st.setString(2, artist);
			st.setString(3, title);
			ResultSet res = st.executeQuery();
			
			res.next();

			result = new Track(res.getInt("Index"), title, artist, genre, res.getInt("Length (Duration)"), res.getInt("Year"),
					res.getInt("Energy"), res.getInt("Danceability"), res.getInt("Valence"), res.getInt("Acousticness"), res.getInt("Popularty"));

			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Track> listTracksByGenre(String genre) {
		
		String sql = "SELECT * \n"
				+ "FROM Tracks\n"
				+ "WHERE Tracks.`Top Genre`= ? ";
		List<Track> result = new ArrayList<Track>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				Track track = new Track(res.getInt("Index"), res.getString("Title"), res.getString("Artist"), genre, res.getInt("Length (Duration)"), res.getInt("Year"),
						res.getInt("Energy"), res.getInt("Danceability"), res.getInt("Valence"), res.getInt("Acousticness"), res.getInt("Popularty"));

				
				result.add(track);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
