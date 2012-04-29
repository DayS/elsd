/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.processors.impl;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import fr.days.elsd.processors.impl.TheSubDBProcessor;

/**
 * @author dvilleneuve
 * 
 */
public class TheSubDBProcessorTest {

	@Test
	public void computeHash_EmptyFile() throws IOException {
		File file = new File("src/test/resources/empty_file");
		String hash = TheSubDBProcessor.VideoHasher.computeHash(file);
		Assert.assertEquals("00000000000000000000000000000000", hash);
	}

	@Test
	public void computeHash_TestFile() throws IOException {
		File file = new File("src/test/resources/dexter.mp4");
		String hash = TheSubDBProcessor.VideoHasher.computeHash(file);
//		Assert.assertEquals("ffd8d4aa68033dc03d1c8ef373b9028c", hash);
	}

}
