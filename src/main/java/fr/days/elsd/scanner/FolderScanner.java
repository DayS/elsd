package fr.days.elsd.scanner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderScanner {
	private final static Logger LOGGER = LoggerFactory.getLogger(FolderScanner.class);

	/**
	 * Looking for a list of videos whithout subtitles in the folder passed in parameter.
	 * 
	 * @param folder
	 *            The folder and his sub-folders to scan.
	 * @return an array of File instances representing videos whithout subtitles or an empty array.
	 */
	public static File[] findVideosInFolder(File folder) {
		try {
			return folder.listFiles(new VideoExtensionFileFilter(true));
		} catch (SecurityException e) {
			LOGGER.error("Can't found any video files", e);
		}
		return null;
	}
}
