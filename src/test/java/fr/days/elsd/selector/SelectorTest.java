/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd.selector;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.Processor;

/**
 * @author dvilleneuve
 * 
 */
public abstract class SelectorTest {

	protected static Selector selector;

	@Test
	public void selectOne_NullList() {
		SubtitleResult selectedSubtitle = selector.selectOne(null);
		Assert.assertNull(selectedSubtitle);
	}

	@Test
	public void selectOne_EmptyList() {
		SubtitleResult selectedSubtitle = selector.selectOne(new ArrayList<SubtitleResult>());
		Assert.assertNull(selectedSubtitle);
	}

	@Test
	public void selectOne_OneElement() {
		ArrayList<SubtitleResult> list = new ArrayList<SubtitleResult>();

		SubtitleResult subtitleResult = new SubtitleResult(Processor.class, "1234", "7c0100307de11000002078031000c00d",
				"Falling Skies - s01e04.srt", "Falling Skies", "fre", 1, 4, "http://localhost/dl.zip", "ZIP",
				"sub-7654", 10.0f);
		list.add(subtitleResult);

		SubtitleResult selectedSubtitle = selector.selectOne(list);
		Assert.assertEquals(subtitleResult, selectedSubtitle);
	}

}
