/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.processors.impl;

import java.util.ArrayList;
import java.util.List;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.model.metadatas.TVShowMetadatas;
import fr.days.elsd.processors.Processor;

/**
 * @author dvilleneuve
 * 
 */
public class FakeProcessorBestResult implements Processor {

	@Override
	public List<SubtitleResult> searchSubtitles(TVShowMetadatas metadatas, String[] languages) {
		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		subtitles.add(new SubtitleResult(FakeProcessorBestResult.class, "12345", "7c0100307de11000002078031000c00d",
				"Falling Skies - s01e04.srt", "Falling Skies", "fre", 1, 4, "http://localhost/dl.zip", "ZIP",
				"sub-7654", 10.0f));

		return subtitles;
	}
}
