/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.processors.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.Processor;

/**
 * @author dvilleneuve
 * 
 */
public class FakeProcessorBestResult implements Processor {

	@Override
	public List<SubtitleResult> searchSubtitles(File video, String[] languages) {
		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		subtitles.add(new SubtitleResult("FakeProcessorBestResult", "12345", "7c0100307de11000002078031000c00d",
				"Falling Skies", 1, 4, "http://localhost/dl.zip", "ZIP", "sub-7654", 10.0f));

		return subtitles;
	}
}
