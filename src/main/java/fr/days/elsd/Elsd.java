/**
###############################################################################
# Contributors:
#     Damien VILLENEUVE - initial API and implementation
###############################################################################
 */
package fr.days.elsd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.days.elsd.download.DownloadManager;
import fr.days.elsd.model.SubtitleResult;
import fr.days.elsd.processors.ProcessorManager;
import fr.days.elsd.processors.impl.OpenSubtitlesProcessor;
import fr.days.elsd.scanner.FolderScanner;
import fr.days.elsd.selector.BestRateSelector;
import fr.days.elsd.selector.FirstSelector;

/**
 * @author dvilleneuve
 * 
 */
public class Elsd {

	private final static Logger LOGGER = LoggerFactory.getLogger(Elsd.class);

	private final DownloadManager downloadManager;
	private final ProcessorManager processorManager;
	private final Parser parser;
	private final HelpFormatter helpFormatter;
	private final Options options;

	public static void main(String[] args) {
		new Elsd(args);
	}

	public Elsd(String[] args) {
		downloadManager = new DownloadManager();
		processorManager = new ProcessorManager();
		processorManager.addProcessor(new OpenSubtitlesProcessor());
		// processorManager.addProcessor(new TheSubDBProcessor());

		options = getOptions();
		parser = new GnuParser();
		helpFormatter = new HelpFormatter();
		helpFormatter.setWidth(120);

		List<File> filesToProcess = new ArrayList<File>();
		try {
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("h")) {
				printHelp();
				System.exit(0);
			} else {
				if (line.hasOption("l")) {
					processorManager.setLanguages(line.getOptionValues("l"));
				}
				if (line.hasOption("s")) {
					String selector = line.getOptionValue("s");
					if ("first".equalsIgnoreCase(selector)) {
						processorManager.setSubtitleSelector(new FirstSelector());
					} else if ("rate".equalsIgnoreCase(selector)) {
						processorManager.setSubtitleSelector(new BestRateSelector());
					} else {
						System.err.println("Please use on these value for the subtitle selector : first, rate");
					}
				}
				if (line.hasOption("f")) {
					String[] optionValues = line.getOptionValues("f");
					for (String optionValue : optionValues) {
						File file = new File(optionValue);
						if (file.exists()) {
							filesToProcess.add(file);
						}
					}
				}
			}

		} catch (MissingOptionException e) {
			System.err.println(e.getMessage());
			printHelp();

		} catch (MissingArgumentException e) {
			System.err.println(e.getMessage());
			printHelp();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		LOGGER.info("Elsd configuration :");
		LOGGER.info("* Searching languages : {}", processorManager.getLanguages());
		LOGGER.info("* Subtitle selector : {}", processorManager.getSubtitleSelector().getClass().getSimpleName());
		LOGGER.info("* Files to process : {}", filesToProcess);

		for (File fileToProcess : filesToProcess) {
			if (fileToProcess.isDirectory()) {
				LOGGER.info("Scanning folder : {}", fileToProcess);

				// Scan videos folder
				File[] videos = FolderScanner.findVideosInFolder(fileToProcess);
				if (videos == null || videos.length == 0) {
					LOGGER.warn("No videos found");
				} else {
					LOGGER.info("Found " + videos.length + " videos whithout subtitles");
					for (File video : videos) {
						processFile(video);
					}
				}
			} else if (fileToProcess.isFile()) {
				processFile(fileToProcess);
			}
		}
	}

	@SuppressWarnings("static-access")
	private Options getOptions() {
		Options options = new Options();
		options.addOption("h", "help", false, "prints the help content");
		options.addOption(OptionBuilder
				.isRequired()
				.hasArgs()
				.withArgName("language,...")
				.withValueSeparator(',')
				.withDescription(
						"ordered list of languages for the subtitles. Each language is ISO-639-3 formated (3 characters)")
				.withLongOpt("languages").create("l"));
		options.addOption(OptionBuilder
				.hasArg()
				.withArgName("first|rate")
				.withDescription(
						"selector strategy to select the best subtitle among all downloaded subtitles. 'rate' selector use users rating and 'first' will just pick the first subtitle found. Default is 'rate'")
				.withLongOpt("selector").create("s"));
		options.addOption(OptionBuilder
				.isRequired()
				.hasArgs()
				.withArgName("file,...")
				.withValueSeparator(',')
				.withDescription(
						"list of files to process. each file can be a video file or a directory to scan. The scanner will search any videos files without associated subtitle")
				.withLongOpt("files").create("f"));
		return options;
	}

	private void printHelp() {
		helpFormatter.printHelp("elsd", options, true);
	}

	private void processFile(File fileToProcess) {
		LOGGER.info("Searching subtitle for video : " + fileToProcess.getAbsolutePath());
		SubtitleResult foundSubtitle = processorManager.searchSubtitle(fileToProcess);

		if (foundSubtitle != null) {
			LOGGER.info("Final subtitle : " + foundSubtitle);
			byte[] subtitleStream = downloadManager.downloadManager(foundSubtitle);

			if (subtitleStream != null && subtitleStream.length > 0) {
				File subtitleFile = new File(fileToProcess.getParentFile(), foundSubtitle.getFilename());
				try {
					LOGGER.info("Writing subtitle file to '{}'", subtitleFile.getAbsoluteFile());

					FileUtils.writeByteArrayToFile(subtitleFile, subtitleStream);
				} catch (IOException e) {
					LOGGER.error("Can't write the subtitle", e);
				}
			}
		} else {
			LOGGER.warn("No subtitle found");
		}
	}
}
