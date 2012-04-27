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
 * <pre>
 * Requested : spa, fre
 * Response : {
 * 	[fre, 0]
 * 	[spa, 0] *
 * 	[spa, 0]
 * }
 * Response : {
 * 	[fre, 0]
 * 	[spa, 0]
 * 	[spa, 4] *
 * }
 * Response : {
 * 	[fre, 7] *
 * 	[spa, 0]
 * 	[spa, 4]
 * }
 * </pre>
 * 
 * @author dvilleneuve
 */
public class BestRateSelector implements Selector {

	public SubtitleResult selectOne(List<SubtitleResult> subtitleResults) {
		// TODO: Filter with languages order
		
		if (subtitleResults != null && subtitleResults.size() > 0) {
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
