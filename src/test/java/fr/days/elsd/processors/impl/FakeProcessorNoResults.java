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
public class FakeProcessorNoResults implements Processor {

	@Override
	public List<SubtitleResult> searchSubtitles(TVShowMetadatas metadatas, String[] languages) {
		return new ArrayList<SubtitleResult>();
	}
}
