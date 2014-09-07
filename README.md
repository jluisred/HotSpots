HotSpots
========

This service detects and enables the exploration of relevant fragments (called Hot Spots) inside online videos. 

The approach combines visual analysis techniques and background knowledge from the web of data in order to quickly get an overview about the video content and therefore promote media consumption at the fragment level. First, we perform a chapter segmentation by combining visual features and semantic units (paragraphs) available in transcripts. Second, we semantically annotate those segments via Named Entity Extraction and topic detection. We then identify consecutive segments talking about similar topics and entities that we merge into bigger and semantic independent media units. Finally, we rank those segments and filter out the lowest scored candidates, in order to pro- pose a summary that illustrates the Hot Spots in a dedicated media player. 

An online demo is available at http://linkedtv.eurecom.fr/ mediafragmentplayer.
