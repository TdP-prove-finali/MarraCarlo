package it.polito.tdp.PlaylistGen.model;

import java.util.Objects;

public class Preference {
	
	private Integer energy;
	private Integer danceability;
	private Integer valence;
	private Integer acousticness;
	private Integer popularty;
	
	public Preference(Integer energy, Integer danceability, Integer valence, Integer acousticness, Integer popularty) {
		super();
		this.energy = energy;
		this.danceability = danceability;
		this.valence = valence;
		this.acousticness = acousticness;
		this.popularty = popularty;
	}

	public Integer getEnergy() {
		return energy;
	}

	public Integer getDanceability() {
		return danceability;
	}

	public Integer getValence() {
		return valence;
	}

	public Integer getAcousticness() {
		return acousticness;
	}

	public Integer getPopularty() {
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
