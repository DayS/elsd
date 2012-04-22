/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.SubtitleResult;

/**
 * @author dvilleneuve
 * 
 */
public class DownloadManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

	public byte[] downloadManager(SubtitleResult subtitleResult) {
		try {
			URL subtitleURL = new URL(subtitleResult.getUrl());
			InputStream subtitleInputStream = subtitleURL.openStream();
			return IOUtils.toByteArray(subtitleInputStream);
		} catch (IOException e) {
			LOGGER.error("Can't download the selected subtitle", e);
		}
		return null;
	}

}
