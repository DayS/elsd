package fr.days.elsd.processors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.selector.Selector;

/**
 * this class manage the different processors.
 * 
 * @author dvilleneuve
 * 
 */
public class ProcessorManager {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProcessorManager.class);

	private final List<Processor> processors = new ArrayList<Processor>();
	private Selector subtitleSelector;
	private String[] languages;

	public ProcessorManager(String... languages) {
		this.languages = languages;
	}

	public SubtitleResult searchSubtitle(File video) {
		if (processors.size() == 0) {
			throw new IllegalArgumentException("You have to specify at least one processor");
		}
		if (subtitleSelector == null) {
			throw new IllegalArgumentException("You have to specify a subtitle selector");
		}
		if (video == null || !video.isFile()) {
			throw new IllegalArgumentException("You have to specify an existing video file");
		}

		LOGGER.info("===============================================");
		LOGGER.info("Search subtitle for video : " + video.getAbsolutePath());

		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		// Search subtitles with every processors
		for (Processor processor : processors) {
			List<SubtitleResult> searchSubtitles = processor.searchSubtitles(video, languages);;
			subtitles.addAll(searchSubtitles);
			LOGGER.debug("# Using {} : {} subtitle(s)", processor.getClass().getSimpleName(), searchSubtitles.size());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Found {} subtitle(s)", subtitles.size());
			for (SubtitleResult subtitle : subtitles) {
				LOGGER.debug(" * " + subtitle);
			}
		}

		return subtitleSelector.selectOne(subtitles);
	}

	public List<Processor> getProcessors() {
		return processors;
	}

	public void addProcessor(Processor processor) {
		processors.add(processor);
	}

	public Selector getSubtitleSelector() {
		return subtitleSelector;
	}

	public void setSubtitleSelector(Selector subtitleSelector) {
		this.subtitleSelector = subtitleSelector;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}

}
