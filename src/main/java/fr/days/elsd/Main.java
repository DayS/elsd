package fr.days.elsd;

import java.io.File;

import org.apache.log4j.Logger;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.ProcessorManager;
import fr.days.elsd.scanner.FolderScanner;

public class Main {
	private final static Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			LOGGER.error("You must specify a folder to scan");
			return;
		}

		// Scan videos folder
		File folderToScan = new File(args[0]);
		File[] videos = FolderScanner.findVideosInFolder(folderToScan);

		// Create the processor manager
		ProcessorManager processorManager = new ProcessorManager(new String[] { "fr" });

		if (videos == null || videos.length == 0) {
			LOGGER.warn("No videos found");
		} else {
			LOGGER.info("Found " + videos.length + " videos whithout subtitles");

			for (File video : videos) {
				LOGGER.debug("Processing video : " + video.getPath());
				SubtitleResult foundSubtitle = processorManager.searchSubtitle(video);
				LOGGER.info("Final subtitle : " + foundSubtitle);
			}
		}
	}

}
