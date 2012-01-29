package fr.days.elsd.scanner;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;

/**
 * <p>
 * A filter for video files based on the filename extension. It's possible to
 * filter video whith or without associated subtitles. A subtitle is associated
 * with a video if the basename (filename without extension) is the same as the
 * video. A subtitle has a '.srt' extension.
 * </p>
 * 
 * <p>
 * Instances of this interface may be passed to the <code>{@link
 * File#listFiles(java.io.FileFilter) listFiles(FileFilter)}</code> method of
 * the <code>{@link java.io.File}</code> class.
 * 
 * @author dvilleneuve
 * 
 */
public class VideoExtensionFileFilter implements FileFilter {
	private final static Logger LOGGER = Logger.getLogger(VideoExtensionFileFilter.class);

	public static String SUBTITLE_EXTENSIONS = "srt";
	public static String[] VALID_EXTENSIONS = new String[] { "3g2", "3gp", "3gp2", "3gpp", "60d", "ajp", "asf", "asx",
			"avchd", "avi", "bik", "bix", "box", "cam", "dat", "divx", "dmf", "dv", "dvr-ms", "evo", "flc", "fli",
			"flic", "flv", "flx", "gvi", "gvp", "h264", "m1v", "m2p", "m2ts", "m2v", "m4e", "m4v", "mjp", "mjpeg",
			"mjpg", "mkv", "moov", "mov", "movhd", "movie", "movx", "mp4", "mpe", "mpeg", "mpg", "mpv", "mpv2", "mxf",
			"nsv", "nut", "ogg", "ogm", "omf", "ps", "qt", "ram", "rm", "rmvb", "swf", "ts", "vfw", "vid", "video",
			"viv", "vivo", "vob", "vro", "wm", "wmv", "wmx", "wrap", "wvx", "wx", "x264", "xvid" };

	private boolean withoutSubtitle;

	public VideoExtensionFileFilter(boolean withoutSubtitle) {
		this.withoutSubtitle = withoutSubtitle;
	}

	@Override
	public boolean accept(File videoFile) {
		if (videoFile == null)
			return false;
		if (!videoFile.isFile())
			return false;

		int extensionIndex = videoFile.getName().lastIndexOf('.');
		if (extensionIndex == -1)
			return false;

		String videoName = videoFile.getName();
		String extension = videoName.substring(extensionIndex + 1, videoName.length());
		for (String validExtension : VALID_EXTENSIONS) {
			if (extension.equalsIgnoreCase(validExtension)) {
				if (withoutSubtitle) {
					File subtitleFile = getSubtitleForVideo(videoFile);
					if (subtitleFile != null) {
						LOGGER.debug(videoName + " has already a subtitle file... Skipped");
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is looking for a subtitle according to a video.
	 * 
	 * @param videoFile
	 * @return a file instance representing the subitle, or <code>null</code> if
	 *         no subtitle was found.
	 */
	private File getSubtitleForVideo(File videoFile) {
		if (videoFile == null)
			return null;

		if (!videoFile.isFile())
			return null;

		String videoPath = videoFile.getAbsolutePath();
		int extensionIndex = videoPath.lastIndexOf('.');
		if (extensionIndex == -1)
			return null;

		String subtitlePath = videoPath.substring(0, extensionIndex + 1) + SUBTITLE_EXTENSIONS;
		File subtitleFile = new File(subtitlePath);

		if (subtitleFile.exists()) {
			return subtitleFile;
		}
		return null;
	}

	public boolean isWithoutSubtitle() {
		return withoutSubtitle;
	}

	public void setWithoutSubtitle(boolean withoutSubtitle) {
		this.withoutSubtitle = withoutSubtitle;
	}
}
