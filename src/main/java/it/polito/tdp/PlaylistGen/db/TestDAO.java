package it.polito.tdp.PlaylistGen.db;

public class TestDAO {
	
	public static void main(String[] args) {
		TestDAO testDao = new TestDAO();
		testDao.run();
	}
	
	public void run() {
		TracksDAO dao = new TracksDAO();
		System.out.println(dao.listAllGenre());
	}

}
