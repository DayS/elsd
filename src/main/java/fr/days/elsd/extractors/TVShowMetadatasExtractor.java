package fr.days.elsd.extractors;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import fr.days.elsd.model.metadatas.TVShowMetadatas;

public class TVShowMetadatasExtractor {
	private final static Logger LOGGER = Logger.getLogger(TVShowMetadatasExtractor.class);
	
	protected static Pattern filenameSeasonEpisodePattern = Pattern
			.compile("[sS]?0?([1-2][0-9]|[1-9])[\\[\\]_.-]{0,4}[eExX_.-]?0?(\\d{1,2})");

	public static TVShowMetadatas extractMetadatas(File video) {
		LOGGER.debug("Try to extract metadatas from '" + video.getPath() + "'");

		TVShowMetadatas pathMetadatas = extractMetadatasFromPath(video);
		TVShowMetadatas fileMetadatas = extractMetadatasFromFileName(video);

		TVShowMetadatas metadatas = new TVShowMetadatas(video);
		
		
		
		return metadatas;
	}

	/**
	 * Extract metadatas from path of the video.
	 * 
	 * @param video
	 * @return a FileMetadatas instance
	 */
	public static TVShowMetadatas extractMetadatasFromPath(File video) {
		TVShowMetadatas metaDatas = new TVShowMetadatas(video);

		// TODO Find a regexp to extract metadata from path

		return metaDatas;
	}

	/**
	 * Extract metadatas from filename of the video.
	 * 
	 * @param video
	 * @return a FileMetadatas instance
	 */
	public static TVShowMetadatas extractMetadatasFromFileName(File video) {
		TVShowMetadatas metaDatas = new TVShowMetadatas(video);

		// TODO Find a regexp to extract metadata from filename

		return metaDatas;
	}
}
