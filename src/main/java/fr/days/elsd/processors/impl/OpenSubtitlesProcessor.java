package fr.days.elsd.processors.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.AbstractProcessor;
import fr.days.elsd.utils.VideoHasher;

public class OpenSubtitlesProcessor extends AbstractProcessor {
	private final static Logger LOGGER = Logger.getLogger(OpenSubtitlesProcessor.class);
	private final static String FIELD_DATA = "data";
	private final static String USER_AGENT = "OS Test User Agent";
	private final static long LOGIN_TICKET_TIME_OUT = 900000;

	private XmlRpcClient client;
	private String loginTicket;
	private Long lastCall;

	public OpenSubtitlesProcessor(String... languages) {
		super(languages);

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
	public List<SubtitleResult> searchSubtitle(File video) {
		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		if (video == null) {
			LOGGER.error("Video is null");
			return subtitles;
		}

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

		// Configure params
		Map<String, String> paramsHash = new HashMap<String, String>();
		paramsHash.put("sublanguageid", makeLanguageString(languages));
		paramsHash.put("moviebytesize", String.valueOf(video.length()));
		paramsHash.put("moviehash", videoHash);

		// Extract imdb from .NFO
		// /imdb\.[^\/]+\/title\/tt(\d+)/i
		// Map<String, String> paramsIMDB = new HashMap<String, String>();
		// paramsIMDB.put("imdbid", imdbId);

		Map<String, String> paramsQuery = new HashMap<String, String>();
		paramsQuery.put("sublanguageid", makeLanguageString(languages));
		paramsQuery.put("query", video.getName());

		Object[] datas = new Object[] { getLoginTicket(), new Object[] { paramsHash }, new Object[] { paramsQuery } };
		Object[] results = callRPCToArray("SearchSubtitles", datas);

		if (results != null) {
			for (Object result : results) {
				Map<?, ?> resultCast = (Map<?, ?>) result;

				String imdbId = (String) resultCast.get("IDMovieImdb");
				String hash = (String) resultCast.get("MovieHash");
				int season = Integer.valueOf((String) resultCast.get("SeriesSeason"));
				int episode = Integer.valueOf((String) resultCast.get("SeriesEpisode"));
				String name = (String) resultCast.get("MovieReleaseName");
				String link = (String) resultCast.get("ZipDownloadLink");
				float subRating = Float.valueOf((String) resultCast.get("SubRating"));
				String subLanguageID = (String) resultCast.get("SubLanguageID");

				SubtitleResult subtitle = new SubtitleResult(getClass().getSimpleName(), imdbId, hash, name, season,
						episode, link, "ZIP", subLanguageID, subRating);
				subtitles.add(subtitle);
			}
		}

		return subtitles;
	}

	private Object callRPC(String methodName, String resultField, Object... params) {
		HashMap<?, ?> result = null;
		try {
			result = (HashMap<?, ?>) client.execute(methodName, params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}

		if (result != null && "200 OK".equals(result.get("status"))) {
			Object data = result.get(resultField);
			if (data instanceof Boolean && !((Boolean) data)) {
				return null;
			}
			return data;
		}
		return null;
	}

	private Object[] callRPCToArray(String methodName, Object... params) {
		Object data = callRPC(methodName, FIELD_DATA, params);
		if (data instanceof Object[]) {
			return (Object[]) data;
		} else {
			LOGGER.error("Can't parse result into Array : " + data);
		}
		return null;
	}

	private String getLoginTicket() {
		if (loginTicket == null || (System.currentTimeMillis() - lastCall) > LOGIN_TICKET_TIME_OUT) {
			Object data = callRPC("LogIn", "token", "", "", "fr", USER_AGENT);
			if (data instanceof String) {
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
}
