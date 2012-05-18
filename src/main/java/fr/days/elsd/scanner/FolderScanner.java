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
	 * @param withoutSubtitle
	 *            true to find videos without subtitles associated, false to find all videos
	 * @return an array of File instances representing videos whithout subtitles or an empty array.
	 */
	public static File[] findVideosInFolder(File folder, boolean withoutSubtitle) {
		try {
			return folder.listFiles(new VideoExtensionFileFilter(withoutSubtitle));
		} catch (SecurityException e) {
			LOGGER.error("Can't found any video files", e);
		}
		return null;
	}

	/**
	 * This method is looking for a file associated to a source file. The filename is used to make a link between the
	 * two files.
	 * 
	 * @param sourceFile
	 * @param extensionToSearch
	 * @return a file instance representing the associated file, or <code>null</code> if no file was found.
	 */
	public static File getAssociatedFile(File sourceFile, String extensionToSearch) {
		if (sourceFile == null)
			return null;
		if (!sourceFile.isFile())
			return null;

		String videoPath = sourceFile.getAbsolutePath();
		int extensionIndex = videoPath.lastIndexOf('.');
		if (extensionIndex == -1)
			return null;

		String subtitlePath = videoPath.substring(0, extensionIndex + 1) + extensionToSearch;
		File subtitleFile = new File(subtitlePath);

		if (subtitleFile.exists()) {
			return subtitleFile;
		}
		return null;
	}
}
