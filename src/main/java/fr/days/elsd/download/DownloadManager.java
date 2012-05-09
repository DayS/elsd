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
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.scanner.VideoExtensionFileFilter;

/**
 * @author dvilleneuve
 * 
 */
public class DownloadManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(DownloadManager.class);

	public byte[] downloadManager(SubtitleResult subtitleResult) {
		InputStream subtitleInputStream = null;
		try {
			URL subtitleURL = new URL(subtitleResult.getUrl());
			subtitleInputStream = subtitleURL.openStream();

			switch (subtitleResult.getUrlType()) {
				case ZIP:
					return inflateZip(subtitleInputStream);
				case GZ:
					return inflateGZip(subtitleInputStream);
				default:
					return IOUtils.toByteArray(subtitleInputStream);
			}
		} catch (IOException e) {
			LOGGER.error("Can't download the selected subtitle", e);
		} finally {
			try {
				if (subtitleInputStream != null) {
					subtitleInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Failed to close the input stream", e);
			}
		}
		return null;
	}

	public byte[] inflateZip(InputStream inputStream) {
		// Deflate zip
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream(inputStream);
			ZipEntry nextEntry = zipInputStream.getNextEntry();

			while (nextEntry != null) {
				String extension = FilenameUtils.getExtension(nextEntry.getName());
				if (VideoExtensionFileFilter.SUBTITLE_EXTENSION.equalsIgnoreCase(extension)) {
					byte[] buffer = new byte[(int) nextEntry.getSize()];
					while (zipInputStream.read(buffer) > 0);
					return buffer;
				}
				nextEntry = zipInputStream.getNextEntry();
			}
		} catch (IOException e) {
			LOGGER.error("Can't inflate the subtitle file", e);
		} finally {
			try {
				zipInputStream.closeEntry();
			} catch (IOException e) {
				LOGGER.error("Failed to close the zip entry", e);
			}
			try {
				zipInputStream.close();
			} catch (IOException e) {
				LOGGER.error("Failed to close the zip input stream", e);
			}
		}

		return null;
	}

	public byte[] inflateGZip(InputStream inputStream) {
		// Deflate gzip
		GZIPInputStream zipInputStream = null;
		try {
			zipInputStream = new GZIPInputStream(inputStream);
			return IOUtils.toByteArray(zipInputStream);
		} catch (IOException e) {
			LOGGER.error("Can't inflate the subtitle file", e);
		} finally {
			try {
				zipInputStream.close();
			} catch (IOException e) {
				LOGGER.error("Failed to close the zip input stream", e);
			}
		}

		return null;
	}

}
