/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.processors;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.ProcessorManager;
import fr.days.elsd.processors.impl.FakeProcessor;
import fr.days.elsd.processors.impl.FakeProcessorBestResult;
import fr.days.elsd.processors.impl.FakeProcessorNoResults;
import fr.days.elsd.selector.BestRateSelector;
import fr.days.elsd.selector.FirstSelector;

/**
 * @author dvilleneuve
 * 
 */
public class ProcessorManagerTest {

	private static ProcessorManager processorManager;
	private static File video;

	public ProcessorManagerTest() {
		processorManager = new ProcessorManager("fr");
		processorManager.addProcessor(new FakeProcessor());
		processorManager.addProcessor(new FakeProcessorNoResults());
		processorManager.addProcessor(new FakeProcessorBestResult());
		processorManager.setSubtitleSelector(new FirstSelector());

		video = new File("src/Test/resources/Falling Skies - 1x04 - Grace.HDTV.fqm.fr.avi");
	}

	@Test(expected = IllegalArgumentException.class)
	public void searchWithoutProcessorsTest() {
		ProcessorManager illegalProcessorManager = new ProcessorManager("fr");
		illegalProcessorManager.searchSubtitle(video);
	}

	@Test(expected = IllegalArgumentException.class)
	public void searchWithoutSelectorTest() {
		ProcessorManager illegalProcessorManager = new ProcessorManager("fr");
		illegalProcessorManager.addProcessor(new FakeProcessor());
		illegalProcessorManager.searchSubtitle(video);
	}

	@Test(expected = IllegalArgumentException.class)
	public void searchWithoutVideoTest() {
		processorManager.searchSubtitle(null);
	}

	@Test
	public void searchFirstResultTest() {
		SubtitleResult searchSubtitle = processorManager.searchSubtitle(video);
		SubtitleResult expectedSubtitle = new SubtitleResult("FakeProcessor", "12345",
				"7c0100307de11000002078031000c00d", "Falling Skies", 1, 4, "http://localhost/dl.zip", "ZIP",
				"sub-1234", 5.0f);

		Assert.assertEquals(expectedSubtitle, searchSubtitle);
	}

	@Test
	public void searchBestResultTest() {
		processorManager.setSubtitleSelector(new BestRateSelector());
		SubtitleResult searchSubtitle = processorManager.searchSubtitle(video);
		SubtitleResult expectedSubtitle = new SubtitleResult("FakeProcessorBestResult", "12345",
				"7c0100307de11000002078031000c00d", "Falling Skies", 1, 4, "http://localhost/dl.zip", "ZIP",
				"sub-7654", 10.0f);

		Assert.assertEquals(expectedSubtitle, searchSubtitle);
	}

}
