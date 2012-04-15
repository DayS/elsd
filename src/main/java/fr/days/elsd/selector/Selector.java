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
public interface Selector {

	SubtitleResult selectOne(List<SubtitleResult> subtitleResults);

}
