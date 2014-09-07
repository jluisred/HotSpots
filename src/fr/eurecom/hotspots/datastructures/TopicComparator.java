package fr.eurecom.hotspots.datastructures;

import java.util.Comparator;

public class TopicComparator implements Comparator<TRTopic> {
	public int compare(TRTopic e1, TRTopic e2) {
		return Double.compare(e2.getRelevance(), e1.getRelevance());
	}
}