package it.polito.tdp.PlaylistGen.model;

import java.util.Objects;

public class Track {

	private Integer trackId;
	private String title;
	private String artist;
	private String topGenre;
	private Integer year;
	private Integer energy;
	private Integer danceability;
	private Integer valence;
	private Integer acousticness;
	private Integer popularty;
	
	public Track(Integer trackId, String title, String artist, String topGenre, Integer year, Integer energy,
			Integer danceability, Integer valence, Integer acousticness, Integer popularty) {
		super();
		this.trackId = trackId;
		this.title = title;
		this.artist = artist;
		this.topGenre = topGenre;
		this.year = year;
		this.energy = energy;
		this.danceability = danceability;
		this.valence = valence;
		this.acousticness = acousticness;
		this.popularty = popularty;
	}

	public Integer getTrackId() {
		return trackId;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getTopGenre() {
		return topGenre;
	}

	public Integer getYear() {
		return year;
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
		return Objects.hash(acousticness, artist, danceability, energy, popularty, title, topGenre, trackId, valence,
				year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		return Objects.equals(acousticness, other.acousticness) && Objects.equals(artist, other.artist)
				&& Objects.equals(danceability, other.danceability) && Objects.equals(energy, other.energy)
				&& Objects.equals(popularty, other.popularty) && Objects.equals(title, other.title)
				&& Objects.equals(topGenre, other.topGenre) && Objects.equals(trackId, other.trackId)
				&& Objects.equals(valence, other.valence) && Objects.equals(year, other.year);
	}

	@Override
	public String toString() {
		return "Track [trackId=" + trackId + ", title=" + title + ", artist=" + artist + ", topGenre=" + topGenre
				+ ", year=" + year + ", energy=" + energy + ", danceability=" + danceability + ", valence=" + valence
				+ ", acousticness=" + acousticness + ", popularty=" + popularty + "]";
	}
	
	
	
	
}
