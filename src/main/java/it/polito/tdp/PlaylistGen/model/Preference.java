package it.polito.tdp.PlaylistGen.model;

import java.util.Objects;

public class Preference {
	
	private Double energy;
	private Double danceability;
	private Double valence;
	private Double acousticness;
	private Double popularty;
	
	public Preference(Double energy, Double danceability, Double valence, Double acousticness, Double popularty) {
		super();
		this.energy = energy;
		this.danceability = danceability;
		this.valence = valence;
		this.acousticness = acousticness;
		this.popularty = popularty;
	}

	public Double getEnergy() {
		return energy;
	}

	public Double getDanceability() {
		return danceability;
	}

	public Double getValence() {
		return valence;
	}

	public Double getAcousticness() {
		return acousticness;
	}

	public Double getPopularty() {
		return popularty;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acousticness, danceability, energy, popularty, valence);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Preference other = (Preference) obj;
		return Objects.equals(acousticness, other.acousticness) && Objects.equals(danceability, other.danceability)
				&& Objects.equals(energy, other.energy) && Objects.equals(popularty, other.popularty)
				&& Objects.equals(valence, other.valence);
	}

	@Override
	public String toString() {
		return "Preference [energy=" + energy + ", danceability=" + danceability + ", valence=" + valence
				+ ", acousticness=" + acousticness + ", popularty=" + popularty + "]";
	}

	
	
	

}
