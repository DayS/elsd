package fr.days.elsd.processors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.impl.OpenSubtitlesProcessor;
import fr.days.elsd.processors.impl.TheSubDBProcessor;

/**
 * this class manage the different processors.
 * 
 * @author dvilleneuve
 * 
 */
public class ProcessorManager {
	private final static Logger LOGGER = Logger.getLogger(ProcessorManager.class);

	private List<AbstractProcessor> processors;

	public ProcessorManager(String... languages) {
		processors = new ArrayList<AbstractProcessor>();

		processors.add(new OpenSubtitlesProcessor(languages));
		processors.add(new TheSubDBProcessor(languages));
	}

	public SubtitleResult searchSubtitle(File video) {
		LOGGER.info("===============================================");
		LOGGER.info("Search subtitle for video : " + video.getAbsolutePath());

		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		// Search subtitles with every processors
		for (AbstractProcessor processor : processors) {
			LOGGER.debug("# Using " + processor.getClass().getSimpleName());
			subtitles.addAll(processor.searchSubtitle(video));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Found " + subtitles.size() + " subtitle(s)");
			for (SubtitleResult subtitle : subtitles) {
				LOGGER.debug(" * " + subtitle);
			}
		}

		// Return the first subtitle
		// TODO : Search the best one
		if (subtitles.size() > 0) {
			return subtitles.get(0);
		}
		return null;
	}
}
