package fr.days.elsd.model;

import fr.days.elsd.processors.Processor;

public class SubtitleResult {
	private final Class<? extends Processor> processor;
	private final String imdbId;
	private final String hash;
	private final String filename;
	private final String name;
	private final int season;
	private final int episode;
	private final String url;
	private final String urlType;
	private final String subLanguageID;
	private final float subRating;

	public SubtitleResult(Class<? extends Processor> processor, String imdbId, String hash, String filename,
			String name, int season, int episode, String url, String urlType, String subLanguageID, float subRating) {
		super();
		this.processor = processor;
		this.imdbId = imdbId;
		this.hash = hash;
		this.filename = filename;
		this.name = name;
		this.season = season;
		this.episode = episode;
		this.url = url;
		this.urlType = urlType;
		this.subLanguageID = subLanguageID;
		this.subRating = subRating;
	}

	@Override
	public String toString() {
		return "SubtitleResult [processor=" + processor + ", imdbId=" + imdbId + ", hash=" + hash + ", filename="
				+ filename + ", name=" + name + ", season=" + season + ", episode=" + episode + ", url=" + url
				+ ", urlType=" + urlType + ", subLanguageID=" + subLanguageID + ", subRating=" + subRating + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + episode;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((imdbId == null) ? 0 : imdbId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((processor == null) ? 0 : processor.hashCode());
		result = prime * result + season;
		result = prime * result + ((subLanguageID == null) ? 0 : subLanguageID.hashCode());
		result = prime * result + Float.floatToIntBits(subRating);
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((urlType == null) ? 0 : urlType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubtitleResult other = (SubtitleResult) obj;
		if (episode != other.episode)
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (imdbId == null) {
			if (other.imdbId != null)
				return false;
		} else if (!imdbId.equals(other.imdbId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		if (season != other.season)
			return false;
		if (subLanguageID == null) {
			if (other.subLanguageID != null)
				return false;
		} else if (!subLanguageID.equals(other.subLanguageID))
			return false;
		if (Float.floatToIntBits(subRating) != Float.floatToIntBits(other.subRating))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (urlType == null) {
			if (other.urlType != null)
				return false;
		} else if (!urlType.equals(other.urlType))
			return false;
		return true;
	}

	public Class<? extends Processor> getProcessor() {
		return processor;
	}

	public String getImdbId() {
		return imdbId;
	}

	public String getHash() {
		return hash;
	}

	public String getFilename() {
		return filename;
	}

	public String getName() {
		return name;
	}

	public int getSeason() {
		return season;
	}

	public int getEpisode() {
		return episode;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlType() {
		return urlType;
	}

	public String getSubLanguageID() {
		return subLanguageID;
	}

	public float getSubRating() {
		return subRating;
	}

}
