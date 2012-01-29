package fr.days.elsd.processors;

import java.io.File;
import java.util.List;

import fr.days.elsd.model.SubtitleResult;

public abstract class AbstractProcessor {

	protected String[] languages;

	public AbstractProcessor(String... languages) {
		this.languages = languages;
	}

	/**
	 * Abstract method to implement. It receive an instance of File representing
	 * a video, and have to return a list of potential results.
	 * 
	 * @param video
	 * @return
	 */
	public abstract List<SubtitleResult> searchSubtitle(File video);

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}
}
