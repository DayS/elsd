package fr.days.elsd.processors;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.model.metadatas.FileMetadatas;
import fr.days.elsd.model.metadatas.TVShowMetadatas;

public abstract class AbstractProcessor {
	private final static Logger LOGGER = Logger.getLogger(ProcessorManager.class);

	protected static Pattern fileSeasonEpisodePattern = Pattern
			.compile("[sS]?0?([1-2][0-9]|[1-9])[\\[\\]_.-]{0,4}[eExX_.-]?0?(\\d{1,2})");
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

	/**
	 * Extract metadatas from path of the video.
	 * 
	 * @param video
	 * @return a FileMetadatas instance
	 */
	public FileMetadatas extractMetadatasFromPath(File video) {
		FileMetadatas metaDatas = new TVShowMetadatas(video);

		// TODO Find a regexp to extract metadata from path

		return metaDatas;
	}

	/**
	 * Extract metadatas from filename of the video.
	 * 
	 * @param video
	 * @return a FileMetadatas instance
	 */
	public FileMetadatas extractMetadatasFromFileName(File video) {
		FileMetadatas metaDatas = new TVShowMetadatas(video);

		// TODO Find a regexp to extract metadata from filename

		return metaDatas;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}
}
