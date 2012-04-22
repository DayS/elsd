package fr.days.elsd;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.download.DownloadManager;
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

		// Create and configure the processor manager
		ProcessorManager processorManager = new ProcessorManager(new String[] { "fre" });
		processorManager.setSubtitleSelector(new BestRateSelector());
		processorManager.addProcessor(new OpenSubtitlesProcessor());
		processorManager.addProcessor(new TheSubDBProcessor());

		// Create the download manager
		DownloadManager downloadManager = new DownloadManager();

		if (videos == null || videos.length == 0) {
			LOGGER.warn("No videos found");
		} else {
			LOGGER.info("Found " + videos.length + " videos whithout subtitles");

			for (File video : videos) {
				SubtitleResult foundSubtitle = processorManager.searchSubtitle(video);
				LOGGER.info("Final subtitle : " + foundSubtitle);

				if (foundSubtitle != null) {
					byte[] subtitleStream = downloadManager.downloadManager(foundSubtitle);

					if (subtitleStream != null && subtitleStream.length > 0) {
						File subtitleFile = new File(folderToScan, foundSubtitle.getFilename());
						try {
							LOGGER.info("Writing subtitle file to '{}'", subtitleFile.getAbsoluteFile());

							FileUtils.writeByteArrayToFile(subtitleFile, subtitleStream);
						} catch (IOException e) {
							LOGGER.error("Can't write the subtitle", e);
						}
					}
				}
			}
		}
	}

}
