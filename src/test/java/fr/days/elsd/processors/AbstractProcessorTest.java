package fr.days.elsd.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.days.elsd.processors.AbstractProcessor;
import fr.days.elsd.processors.ProcessorManager;

public class AbstractProcessorTest {
	private final static Logger log = Logger.getLogger(ProcessorManager.class);

	private static final String[] TVSHOW_FILENAMES_DEFAULTS = new String[] { 
			"foo.s06e01.*", "foo.s06.e01.*",
			"foo.s06_e01.*", "foo_[s06]_[e01]_*", 
			"foo.6x01.*", "06-01 - *", "foo.601.*" };

	private static final String[] TVSHOW_FILENAMES_TWO_PARTS = new String[] { 
			"foo.s06e01-02.*", "foo_[s06]_[e01-02]_*",
			"foo.6x01.6x02.*" };

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFileSeasonPattern_defaults() {
		for (String filename : TVSHOW_FILENAMES_DEFAULTS) {
			log.debug("Try to parse : " + filename);

			Matcher matcher = AbstractProcessor.fileSeasonEpisodePattern.matcher(filename);
			assertTrue(matcher.find());
			assertEquals(matcher.groupCount(), 2);
			assertEquals(matcher.group(1), "6");
			assertEquals(matcher.group(2), "1");

			log.debug("extract season " + matcher.group(1) + ", episode " + matcher.group(2));
		}
	}

	@Test
	public void testFileSeasonPattern_multiparts() {
		for (String filename : TVSHOW_FILENAMES_TWO_PARTS) {
			log.debug("Try to parse : " + filename);

			Matcher matcher = AbstractProcessor.fileSeasonEpisodePattern.matcher(filename);
			int foundCount = 1;
			while (matcher.find()) {
				assertTrue(matcher.groupCount() > 1);

				for (int i = 1; i <= matcher.groupCount(); i++) {
					log.debug("	found " + foundCount + " : " + matcher.group(i));
				}
				foundCount++;
			}
		}
	}
}
