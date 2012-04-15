package fr.days.elsd.processors;

import java.io.File;
import java.util.List;

import fr.days.elsd.model.SubtitleResult;

public interface Processor {

	/**
	 * Abstract method to implement. It receive an instance of File representing a video, and have to return a list of
	 * potential results.
	 * 
	 * @param video
	 * @return
	 */
	List<SubtitleResult> searchSubtitles(File video, String[] languages);

}
