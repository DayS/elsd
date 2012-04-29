package fr.days.elsd.extractors;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.metadatas.TVShowMetadatas;
import fr.days.elsd.processors.ProcessorManager;

public class TVShowMetadatasExtractorTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProcessorManager.class);

	private static final String[] TVSHOW_FILENAMES_DEFAULTS = new String[] { "foo.s06e01.*", "foo.s06.e01.*.bar",
			"foo.s06_e01.*", "foo_[s06]_[e01]_*", "6x01.foo", "foo_06-01 - *", "601.foo" };
	private static final String[] NFO_TEAMS = new String[] { "fqm", "lol", "immerse", "2hd" };

	private TVShowMetadatasExtractor tvShowMetadatasExtractor;

	@Before
	public void setUp() throws Exception {
		tvShowMetadatasExtractor = new TVShowMetadatasExtractor();
	}

	@Test
	public void testFileSeasonPattern_defaults() {
		for (String filename : TVSHOW_FILENAMES_DEFAULTS) {
			LOGGER.debug("Try to parse : " + filename);

			TVShowMetadatas metadatas = tvShowMetadatasExtractor.extractMetadatasFromFileName(new File(filename));
			assertEquals(6, metadatas.getSeasonNumber());
			assertEquals(1, metadatas.getEpisodeNumber());
			assertEquals("foo", metadatas.getShowName());
		}
	}

	@Test
	public void testNFO_defaults() {
		for (String nfoTeam : NFO_TEAMS) {
			LOGGER.debug("Try to parse " + nfoTeam + "'s nfo");

			TVShowMetadatas metadatas = tvShowMetadatasExtractor.extractMetadatasFromNFO(null, new File(
					"src/test/resources/nfos/" + nfoTeam));

			// TODO: unify NFOs to allow asserts

		}
	}
}
