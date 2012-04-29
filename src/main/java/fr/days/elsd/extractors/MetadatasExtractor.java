/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.extractors;

import java.io.File;

import fr.days.elsd.model.metadatas.TVShowMetadatas;

/**
 * @author dvilleneuve
 * 
 */
public interface MetadatasExtractor {
	
	TVShowMetadatas extractMetadatas(File video);

}
