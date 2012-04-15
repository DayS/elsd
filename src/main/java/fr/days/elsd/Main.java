package fr.days.elsd;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.ProcessorManager;
import fr.days.elsd.processors.impl.OpenSubtitlesProcessor;
import fr.days.elsd.processors.impl.TheSubDBProcessor;
import fr.days.elsd.scanner.FolderScanner;
import fr.days.elsd.selector.BestRateSelector;

public class Main {
	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

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

		processorManager.setSubtitleSelector(new BestRateSelector());

		processorManager.addProcessor(new OpenSubtitlesProcessor());
		processorManager.addProcessor(new TheSubDBProcessor());

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
