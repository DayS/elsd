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
	private Selector selector;
	private String[] languages;

	public ProcessorManager(String... languages) {
		this.languages = languages;
	}

	public SubtitleResult searchSubtitle(File video) {
		LOGGER.info("===============================================");
		LOGGER.info("Search subtitle for video : " + video.getAbsolutePath());

		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		// Search subtitles with every processors
		for (Processor processor : processors) {
			LOGGER.debug("# Using " + processor.getClass().getSimpleName());
			subtitles.addAll(processor.searchSubtitle(video, languages));
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Found " + subtitles.size() + " subtitle(s)");
			for (SubtitleResult subtitle : subtitles) {
				LOGGER.debug(" * " + subtitle);
			}
		}

		return selector.selectOne(subtitles);
	}

	public List<Processor> getProcessors() {
		return processors;
	}

	public void addProcessor(Processor processor) {
		processors.add(processor);
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSubtitleSelector(Selector selector) {
		this.selector = selector;
	}

	public String[] getLanguages() {
		return languages;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}

}
