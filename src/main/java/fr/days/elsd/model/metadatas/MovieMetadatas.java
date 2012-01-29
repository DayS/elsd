package fr.days.elsd.model.metadatas;

import java.io.File;

public class MovieMetadatas extends FileMetadatas {
	private String name;
	private int releaseYear;

	public MovieMetadatas(File sourceFile) {
		super(sourceFile);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

}
