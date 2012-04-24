package fr.days.elsd.processors.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.Processor;

public class OpenSubtitlesProcessor implements Processor {

	private final static Logger LOGGER = LoggerFactory.getLogger(OpenSubtitlesProcessor.class);
	private final static String FIELD_DATA = "data";
	private final static String USER_AGENT = "OS Test User Agent";
	private final static long LOGIN_TICKET_TIME_OUT = 900000;

	private XmlRpcClient client;
	private String loginTicket;
	private Long lastCall;

	public OpenSubtitlesProcessor() {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL("http://api.opensubtitles.org/xml-rpc"));
			config.setUserAgent(USER_AGENT);
		} catch (MalformedURLException e) {
			LOGGER.error("Unable to configure the XML-RPC client : ", e);
		}

		client = new XmlRpcClient();
		client.setConfig(config);
	}

	@Override
	public List<SubtitleResult> searchSubtitles(File video, String[] languages) {
		if (video == null || !video.isFile()) {
			throw new IllegalArgumentException("You have to specify an existing video file");
		}
		if (languages == null || languages.length == 0) {
			throw new IllegalArgumentException("You have to specify at least one language");
		}

		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		// Retrieve the video hash
		String videoHash = null;
		try {
			videoHash = VideoHasher.computeHash(video);
		} catch (IOException e1) {
			LOGGER.error("Hash can't be computed : ", e1);
		}

		if (videoHash == null || videoHash.length() == 0) {
			LOGGER.error("The computed hash is invalid");
			return subtitles;
		}
		LOGGER.debug("Video hash = " + videoHash);

		String languagesString = makeLanguageString(languages);
		String basename = FilenameUtils.getBaseName(video.getName());

		// Configure params
		Map<String, String> paramsHash = new HashMap<String, String>();
		paramsHash.put("sublanguageid", languagesString);
		paramsHash.put("moviebytesize", String.valueOf(video.length()));
		paramsHash.put("moviehash", videoHash);

		// Extract imdb from .NFO
		// /imdb\.[^\/]+\/title\/tt(\d+)/i
		// Map<String, String> paramsIMDB = new HashMap<String, String>();
		// paramsIMDB.put("imdbid", imdbId);

		Map<String, String> paramsQuery = new HashMap<String, String>();
		paramsQuery.put("sublanguageid", languagesString);
		paramsQuery.put("query", basename);

		Object[] datas = new Object[] { getLoginTicket(), new Object[] { paramsHash }, new Object[] { paramsQuery } };
		Object[] results = callRPCToArray("SearchSubtitles", datas);

		if (results != null) {
			String subtitleFileName = basename + ".srt";

			for (Object result : results) {
				Map<?, ?> resultCast = (Map<?, ?>) result;

				String imdbId = (String) resultCast.get("IDMovieImdb");
				String hash = (String) resultCast.get("MovieHash");
				String name = (String) resultCast.get("MovieName");
				int season = Integer.valueOf((String) resultCast.get("SeriesSeason"));
				int episode = Integer.valueOf((String) resultCast.get("SeriesEpisode"));
				String link = (String) resultCast.get("SubDownloadLink");
				float subRating = Float.valueOf((String) resultCast.get("SubRating"));
				String subLanguageID = (String) resultCast.get("SubLanguageID");

				SubtitleResult subtitle = new SubtitleResult(getClass(), imdbId, hash, subtitleFileName, name, season,
						episode, link, "ZIP", subLanguageID, subRating);
				subtitles.add(subtitle);
			}
		}

		return subtitles;
	}

	private Object callRPC(String methodName, String resultField, Object... params) {
		HashMap<?, ?> result = null;
		try {
			LOGGER.debug("Calling RPC '{}' [{}]", methodName, params);
			result = (HashMap<?, ?>) client.execute(methodName, params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}

		if (result != null && "200 OK".equals(result.get("status"))) {
			Object data = result.get(resultField);
			LOGGER.debug("RPC response = {}", data);
			if (data instanceof Boolean && !((Boolean) data)) {
				return null;
			}
			return data;
		}
		return null;
	}

	private Object[] callRPCToArray(String methodName, Object... params) {
		Object data = callRPC(methodName, FIELD_DATA, params);
		if (data != null) {
			if (data instanceof Object[]) {
				return (Object[]) data;
			} else {
				LOGGER.error("Can't parse result into Array : " + data);
			}
		}
		return null;
	}

	private String getLoginTicket() {
		if (loginTicket == null || (System.currentTimeMillis() - lastCall) > LOGIN_TICKET_TIME_OUT) {
			Object data = callRPC("LogIn", "token", "", "", "fr", USER_AGENT);
			if (data != null && data instanceof String) {
				loginTicket = (String) data;
				lastCall = System.currentTimeMillis();
			} else {
				LOGGER.error("Login failed. Can't find a login ticket in the following stream : " + data);
			}
		}
		return loginTicket;
	}

	private String makeLanguageString(String[] languages) {
		StringBuffer languagesString = new StringBuffer();
		for (int i = 0; i < languages.length; i++) {
			languagesString.append(languages[i]);
			if (i < languages.length - 1) {
				languagesString.append(",");
			}
		}
		return languagesString.toString();
	}

	/**
	 * Hash code is based on Media Player Classic. In natural language it calculates: size + 64bit checksum of the first
	 * and last 64k (even if they overlap because the file is smaller than 128k).
	 */
	public static class VideoHasher {
		/**
		 * Size of the chunks that will be hashed in bytes (64 KB)
		 */
		private static final int HASH_CHUNK_SIZE = 64 * 1024;

		public static String computeHash(File file) throws IOException {
			long size = file.length();
			long chunkSizeForFile = Math.min(HASH_CHUNK_SIZE, size);

			FileChannel fileChannel = new FileInputStream(file).getChannel();
			try {
				long head = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, 0, chunkSizeForFile));
				long tail = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, Math.max(size - HASH_CHUNK_SIZE, 0),
						chunkSizeForFile));

				return String.format("%016x", size + head + tail);
			} finally {
				fileChannel.close();
			}
		}

		private static long computeHashForChunk(ByteBuffer buffer) {
			LongBuffer longBuffer = buffer.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
			long hash = 0;
			while (longBuffer.hasRemaining()) {
				hash += longBuffer.get();
			}
			return hash;
		}
	}

}
