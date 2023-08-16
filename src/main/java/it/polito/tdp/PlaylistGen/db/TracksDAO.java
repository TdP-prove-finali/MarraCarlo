package it.polito.tdp.PlaylistGen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TracksDAO {
	
	public List<String> listAllGenre(){
		String sql = "SELECT DISTINCT Tracks.`Top Genre` FROM Tracks";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
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
}
