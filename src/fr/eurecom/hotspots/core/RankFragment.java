package fr.eurecom.hotspots.core;
import java.util.Comparator;

import fr.eurecom.hotspots.datastructures.Fragment;
import fr.eurecom.hotspots.datastructures.TRTopic;

	public class RankFragment implements Comparator<Fragment> {
		public int compare(Fragment e1, Fragment e2) {

			return Double.compare(e2.getRelevance(), e1.getRelevance());
		}
	}