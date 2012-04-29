package fr.days.elsd.model.metadatas;

import java.io.File;

import fr.days.elsd.model.VideoQualityEnum;

public abstract class FileMetadatas {
	private File sourceFile;
	private VideoQualityEnum quality;
	private String teamName;
	private String language;
	private int imdbid;

	public FileMetadatas(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + imdbid;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
		result = prime * result + ((sourceFile == null) ? 0 : sourceFile.hashCode());
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
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
		FileMetadatas other = (FileMetadatas) obj;
		if (imdbid != other.imdbid)
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (quality != other.quality)
			return false;
		if (sourceFile == null) {
			if (other.sourceFile != null)
				return false;
		} else if (!sourceFile.equals(other.sourceFile))
			return false;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileMetadatas [sourceFile=" + sourceFile + ", quality=" + quality + ", teamName=" + teamName
				+ ", language=" + language + ", imdbid=" + imdbid + "]";
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public VideoQualityEnum getQuality() {
		return quality;
	}

	public void setQuality(VideoQualityEnum quality) {
		this.quality = quality;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getImdbid() {
		return imdbid;
	}

	public void setImdbid(int imdbid) {
		this.imdbid = imdbid;
	}

}
