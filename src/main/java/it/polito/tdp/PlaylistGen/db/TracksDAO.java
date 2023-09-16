package it.polito.tdp.PlaylistGen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
