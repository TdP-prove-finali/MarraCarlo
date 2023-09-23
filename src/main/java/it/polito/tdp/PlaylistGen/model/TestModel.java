package it.polito.tdp.PlaylistGen.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafoMix("Rock");
		System.out.println(model.getTotalDurataGraph());
	}

}
