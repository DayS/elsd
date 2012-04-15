/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.selector;

import java.util.List;

import fr.days.elsd.model.SubtitleResult;

/**
 * @author dvilleneuve
 * 
 */
public class BestRateSelector implements Selector {

	public SubtitleResult selectOne(List<SubtitleResult> subtitleResults) {
		if (subtitleResults.size() > 1) {
			SubtitleResult bestResult = subtitleResults.get(0);

			SubtitleResult subtitleResult;
			for (int i = 1; i < subtitleResults.size(); i++) {
				subtitleResult = subtitleResults.get(i);
				if (subtitleResult.getSubRating() > bestResult.getSubRating()) {
					bestResult = subtitleResult;
				}
			}
			return bestResult;
		}
		return null;
	}
	
}
