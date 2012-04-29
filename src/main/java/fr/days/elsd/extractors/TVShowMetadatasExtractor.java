package fr.days.elsd.extractors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.metadatas.TVShowMetadatas;

public class TVShowMetadatasExtractor implements MetadatasExtractor {

	private final static Logger LOGGER = LoggerFactory.getLogger(TVShowMetadatasExtractor.class);

	private final static Pattern filenameSeasonEpisodePattern = Pattern.compile("s?(0?[1-2][0-9]|0?[1-9])" //
			+ "[\\[\\]_.-]{0,4}" //
			+ "[ex_.-]?(0?[1-2][0-9]|0?[1-9])", Pattern.CASE_INSENSITIVE);
	private final static Pattern filenameSeasonNamePattern = Pattern.compile("[._-]*([a-z]+(?:[._-][a-z]+)*)[._-]*",
			Pattern.CASE_INSENSITIVE);

	@Override
	public TVShowMetadatas extractMetadatas(File video) {
		LOGGER.debug("Try to extract metadatas from '" + video.getPath() + "'");

		TVShowMetadatas pathMetadatas = extractMetadatasFromPath(video);
		TVShowMetadatas fileMetadatas = extractMetadatasFromFileName(video);

		if (pathMetadatas != null && fileMetadatas != null) {

			// TODO: Merge the to TVShowMetadatas instances

		} else if (pathMetadatas != null) {
			return pathMetadatas;
		} else if (fileMetadatas != null) {
			return fileMetadatas;
		}
		return null;
	}

	/**
	 * Extract metadatas from path of the video.
	 * 
	 * @param video
	 * @return a FileMetadatas instance
	 */
	public TVShowMetadatas extractMetadatasFromPath(File video) {
		// TODO Find a regexp to extract metadata from path

		return null;
	}

	/**
	 * Extract metadatas from filename of the video.
	 * 
	 * @param video
	 * @return a FileMetadatas instance
	 */
	public TVShowMetadatas extractMetadatasFromFileName(File video) {
		TVShowMetadatas metaDatas = new TVShowMetadatas(video);
		String filename = video.getName();

		// Extract season and episode numbers
		Matcher matcher = filenameSeasonEpisodePattern.matcher(filename);
		if (matcher.find()) {
			metaDatas.setSeasonNumber(Integer.parseInt(matcher.group(1)));
			metaDatas.setEpisodeNumber(Integer.parseInt(matcher.group(2)));

			filename = filename.replace(matcher.group(), "");
		}

		// Extract names
		matcher = filenameSeasonNamePattern.matcher(filename);
		List<String> names = new ArrayList<String>();
		while (matcher.find()) {
			names.add(matcher.group(1));
			filename = filename.replace(matcher.group(), "");
		}
		if (names.size() > 0) {
			metaDatas.setShowName(names.get(0));
		}

		return metaDatas;
	}
}
