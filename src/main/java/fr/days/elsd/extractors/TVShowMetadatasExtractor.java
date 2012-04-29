package fr.days.elsd.extractors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.metadatas.TVShowMetadatas;
import fr.days.elsd.scanner.FolderScanner;

public class TVShowMetadatasExtractor implements MetadatasExtractor {

	private final static Logger LOGGER = LoggerFactory.getLogger(TVShowMetadatasExtractor.class);

	private final static String NFO_EXTENSION = "nfo";
	private final static Pattern nfoInfosPattern = Pattern.compile(
			"([a-z ]+)\\p{Punct}+\\s*([^\\s]+(?:\\s{1,2}[^\\s]+)*)", Pattern.CASE_INSENSITIVE);

	private final static Pattern filenameSeasonEpisodePattern = Pattern.compile("s?(0?[1-2][0-9]|0?[1-9])" //
			+ "[\\[\\]_.-]{0,4}" //
			+ "[ex_.-]?(0?[1-2][0-9]|0?[1-9])", Pattern.CASE_INSENSITIVE);
	private final static Pattern filenameSeasonNamePattern = Pattern.compile("[._-]*([a-z]+(?:[._-][a-z]+)*)[._-]*",
			Pattern.CASE_INSENSITIVE);

	@Override
	public TVShowMetadatas extractMetadatas(File video) {
		TVShowMetadatas baseMetadatas = new TVShowMetadatas(video);

		File nfoFile = FolderScanner.getAssociatedFile(video, NFO_EXTENSION);
		if (nfoFile != null) {
			LOGGER.debug("Trying to extract metadatas from NFO file '" + nfoFile.getPath() + "'");
			baseMetadatas = extractMetadatasFromNFO(video, nfoFile);
		}

		LOGGER.debug("Trying to extract metadatas from '" + video.getPath() + "'");
		TVShowMetadatas pathMetadatas = extractMetadatasFromPath(video);
		TVShowMetadatas fileMetadatas = extractMetadatasFromFileName(video);

		if (pathMetadatas != null && fileMetadatas != null) {

			// TODO: Merge the two TVShowMetadatas instances

		} else if (pathMetadatas != null) {
			return pathMetadatas;
		} else if (fileMetadatas != null) {
			return fileMetadatas;
		}
		return null;
	}

	/**
	 * Extract metadatas from NFO file.
	 * 
	 * @param video
	 * @param nfoFile
	 * @return a FileMetadatas instance
	 */
	public TVShowMetadatas extractMetadatasFromNFO(File video, File nfoFile) {
		TVShowMetadatas metadatas = new TVShowMetadatas(video);

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(nfoFile);
			bufferedReader = new BufferedReader(fileReader);

			// Extract each key / value line we can find
			String line = null;
			Matcher matcher = null;
			Map<String, String> nfoInfos = new HashMap<String, String>();
			while ((line = bufferedReader.readLine()) != null) {
				matcher = nfoInfosPattern.matcher(line);
				if (matcher.find()) {
					String key = matcher.group(1).trim().toLowerCase();
					String value = matcher.group(2).trim();
					if (key.length() > 0) {
						nfoInfos.put(key, value);
					}
				}
			}

			// Extract season and episode numbers
			String episode = nfoInfos.get("episode");
			if (episode != null) {
				extractSeasonEpisodeNumbers(episode, metadatas);
			} else {
				String releaseName = nfoInfos.get("release name");
				if (releaseName != null) {
					extractSeasonEpisodeNumbers(releaseName, metadatas);
				}
			}

			LOGGER.debug("nfoInfos = {}", nfoInfos);
			LOGGER.debug("metadatas = {}", metadatas);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return metadatas;
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
		TVShowMetadatas metadatas = new TVShowMetadatas(video);
		String filename = video.getName();

		// Extract season and episode numbers
		String stringMatch = extractSeasonEpisodeNumbers(filename, metadatas);
		if (metadatas.getEpisodeNumber() != 0) {
			filename = filename.replace(stringMatch, "");
		}

		// Extract names
		List<String> stringsMatches = extractSeasonName(filename, metadatas);
		for (String stringsMatch : stringsMatches) {
			filename = filename.replace(stringsMatch, "");
		}

		return metadatas;
	}

	private String extractSeasonEpisodeNumbers(String value, TVShowMetadatas metadatas) {
		Matcher matcher = filenameSeasonEpisodePattern.matcher(value);
		if (matcher.find()) {
			metadatas.setSeasonNumber(Integer.parseInt(matcher.group(1)));
			metadatas.setEpisodeNumber(Integer.parseInt(matcher.group(2)));
			return matcher.group();
		}
		return null;
	}

	private List<String> extractSeasonName(String value, TVShowMetadatas metadatas) {
		Matcher matcher = filenameSeasonNamePattern.matcher(value);
		List<String> matches = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		while (matcher.find()) {
			names.add(matcher.group(1));
			matches.add(matcher.group());
			value = value.replace(matcher.group(), "");
		}
		if (names.size() > 0) {
			metadatas.setShowName(names.get(0));
		}
		return matches;
	}
}
