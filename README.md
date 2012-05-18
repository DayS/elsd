# What is ELSD ?

ELSD (the acronym for Easy & Light Subtitle Downloader) is a tool for downloading video subtitles.
Development is currently focusing on one main source : OpenSubtitle.

# Motivation

* Other subtitles downloader (like Periscope) are not maintained anymore ;
* Based on Java which is installed on almost all computers.

# Usage

`java -jar elsd.jar <commands>`

Where commands could be :
* `-f,--files <file,...>` : list of files to process. each file can be a video file or a directory to scan. The scanner will search any videos files without associated subtitle
* `-h,--help` : prints the help content
* `-l,--languages <language,...>` : ordered list of languages for the subtitles. Each language is ISO-639-3 formated (3 characters)
* `-s,--selector <first|rate>` : selector strategy to select the best subtitle among all downloaded subtitles. 'rate' selector use users rating and 'first' will just pick the first subtitle found. Default is 'rate'

**Files** and **languages** are mandatory fields.