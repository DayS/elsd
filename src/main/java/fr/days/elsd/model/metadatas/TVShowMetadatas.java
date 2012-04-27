package fr.days.elsd.model.metadatas;

import java.io.File;
import java.util.Date;

public class TVShowMetadatas extends FileMetadatas {
	private String showName;
	private String episodeName;
	private int seasonNumber;
	private int episodeNumber;
	private Date releaseDate;

	public TVShowMetadatas(File sourceFile) {
		super(sourceFile);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((episodeName == null) ? 0 : episodeName.hashCode());
		result = prime * result + episodeNumber;
		result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
		result = prime * result + seasonNumber;
		result = prime * result + ((showName == null) ? 0 : showName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TVShowMetadatas other = (TVShowMetadatas) obj;
		if (episodeName == null) {
			if (other.episodeName != null)
				return false;
		} else if (!episodeName.equals(other.episodeName))
			return false;
		if (episodeNumber != other.episodeNumber)
			return false;
		if (releaseDate == null) {
			if (other.releaseDate != null)
				return false;
		} else if (!releaseDate.equals(other.releaseDate))
			return false;
		if (seasonNumber != other.seasonNumber)
			return false;
		if (showName == null) {
			if (other.showName != null)
				return false;
		} else if (!showName.equals(other.showName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TVShowMetadatas [showName=" + showName + ", episodeName=" + episodeName + ", seasonNumber="
				+ seasonNumber + ", episodeNumber=" + episodeNumber + ", releaseDate=" + releaseDate + "]";
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getEpisodeName() {
		return episodeName;
	}

	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	public int getSeasonNumber() {
		return seasonNumber;
	}

	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}

	public int getEpisodeNumber() {
		return episodeNumber;
	}

	public void setEpisodeNumber(int episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

}
