/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import fr.days.elsd.utils.VideoHasher;

/**
 * @author dvilleneuve
 * 
 */
public class VideoHasherTest {

	@Test
	public void computeHash_EmptyFile() throws IOException {
		File file = new File("src/test/resources/empty_file");
		String hash = VideoHasher.computeHash(file);
		Assert.assertEquals("0000000000000000", hash);
	}

	@Test
	public void computeHash_TestFile() throws IOException {
		File file = new File("src/test/resources/test_file");
		String hash = VideoHasher.computeHash(file);
		Assert.assertEquals("6d9c16f67c3c3f0b", hash);
	}

}
