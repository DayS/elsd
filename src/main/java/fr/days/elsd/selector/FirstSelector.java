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
public class FirstSelector implements Selector {

	@Override
	public SubtitleResult selectOne(List<SubtitleResult> subtitleResults) {
		if (subtitleResults != null && subtitleResults.size() > 0) {
			return subtitleResults.get(0);
		}
		return null;
	}

}
