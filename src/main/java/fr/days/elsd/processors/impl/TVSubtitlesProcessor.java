package fr.days.elsd.processors.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.model.metadatas.TVShowMetadatas;
import fr.days.elsd.processors.AbstractProcessor;
import fr.days.elsd.utils.VideoHasher;

public class TVSubtitlesProcessor extends AbstractProcessor {

	public TVSubtitlesProcessor(String... languages) {
		super(languages);
	}

	@Override
	public List<SubtitleResult> searchSubtitle(File video) {
		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();

		if (video == null)
			return subtitles;

		String tvShowName = "";

		try {
			Client client = Client.create();
			WebResource webResource = client.resource("http://www.tvsubtitles.net/search.php");
			ClientResponse response = webResource.queryParam("search", tvShowName).type("application/json")
					.post(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			// System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return subtitles;
	}
}
