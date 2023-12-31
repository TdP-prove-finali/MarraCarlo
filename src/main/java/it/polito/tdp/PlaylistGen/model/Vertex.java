package it.polito.tdp.PlaylistGen.model;

import java.util.Objects;

public class Vertex {

	private Track track;
	private Double score;

	public Vertex(Track track, Double score) {
		super();
		this.track = track;
		this.score = score;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		return Objects.hash(score, track);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		return Objects.equals(score, other.score) && Objects.equals(track, other.track);
	}

	@Override
	public String toString() {
		return "'"+track.getTitle()+"' - "+track.getArtist()+" (Score = "+score+")";
	}

}
