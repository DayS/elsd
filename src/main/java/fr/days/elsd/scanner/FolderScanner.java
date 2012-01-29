package fr.days.elsd.scanner;

import java.io.File;

import org.apache.log4j.Logger;

public class FolderScanner {
	private final static Logger LOGGER = Logger.getLogger(FolderScanner.class);

	/**
	 * Looking for a list of videos whithout subtitles in the folder passed in
	 * parameter.
	 * 
	 * @param folder
	 *            The folder and his sub-folders to scan.
	 * @return an array of File instances representing videos whithout subtitles
	 *         or an empty array.
	 */
	public static File[] findVideosInFolder(File folder) {
		try {
			return folder.listFiles(new VideoExtensionFileFilter(true));
		} catch (SecurityException e) {
			LOGGER.error(e);
		}
		return null;
	}
}
