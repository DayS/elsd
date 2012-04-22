package fr.days.elsd.processors.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.Processor;

public class TheSubDBProcessor implements Processor {

	private final static Logger LOGGER = LoggerFactory.getLogger(TheSubDBProcessor.class);

	@Override
	public List<SubtitleResult> searchSubtitles(File video, String[] languages) {
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
			LOGGER.debug("Video hash = " + videoHash);

			Client client = Client.create();
			WebResource webResource = client.resource("http://api.thesubdb.com/?action=search&hash=" + videoHash);
			ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
			System.out.println(output);
		} catch (Exception e) {
			LOGGER.error("Can't retrieve subtitles", e);
		}

		return subtitles;
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
			if (size == 0) {
				return String.format("%032d", 0);
			}

			long chunkSizeForFile = Math.min(HASH_CHUNK_SIZE, size);
			FileChannel fileChannel = new FileInputStream(file).getChannel();
			try {
				MappedByteBuffer head = fileChannel.map(MapMode.READ_ONLY, 0, chunkSizeForFile);
				MappedByteBuffer tail = fileChannel.map(MapMode.READ_ONLY, Math.max(size - HASH_CHUNK_SIZE, 0),
						chunkSizeForFile);

				return DigestUtils.md5Hex(convertChunkToString(head) + convertChunkToString(tail));
			} finally {
				fileChannel.close();
			}
		}

		private static String convertChunkToString(ByteBuffer byteBuffer) {
			CharBuffer charBuffer = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).asCharBuffer();
			StringBuilder buffer = new StringBuilder();
			while (charBuffer.hasRemaining()) {
				buffer.append(charBuffer.get());
			}
			return buffer.toString();
		}
	}

}
