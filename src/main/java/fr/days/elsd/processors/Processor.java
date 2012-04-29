package fr.days.elsd.processors;

import java.util.List;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.model.metadatas.TVShowMetadatas;

public interface Processor {

	/**
	 * Abstract method to implement. It receive an instance of File representing a video, and have to return a list of
	 * potential results.
	 * 
	 * @param metadatas
	 * @param languages
	 * @return
	 */
	List<SubtitleResult> searchSubtitles(TVShowMetadatas metadatas, String[] languages);

}
