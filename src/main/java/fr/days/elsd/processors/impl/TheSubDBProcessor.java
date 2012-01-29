package fr.days.elsd.processors.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.AbstractProcessor;
import fr.days.elsd.utils.VideoHasher;

public class TheSubDBProcessor extends AbstractProcessor {
	
	public TheSubDBProcessor(String[] languages) {
		super(languages);
	}

	@Override
	public List<SubtitleResult> searchSubtitle(File video) {
		List<SubtitleResult> subtitles = new ArrayList<SubtitleResult>();
		
		if (video == null)
			return subtitles;

		String videoHash = null;
		try {
			videoHash = VideoHasher.computeHash(video);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (videoHash == null || videoHash.length() == 0)
			return subtitles;

		try {
			System.out.println("hash = " + videoHash);

			Client client = Client.create();
			WebResource webResource = client.resource("http://api.thesubdb.com/?action&search&hash=" + videoHash);
			ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
//			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return subtitles;
	}

}
