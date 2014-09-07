package fr.eurecom.hotspots.core;
import java.util.Comparator;

import fr.eurecom.hotspots.datastructures.TRTopic;

	public class RankTopic implements Comparator<TRTopic> {
		public int compare(TRTopic e1, TRTopic e2) {
			
			//double score1 = e1.getRelevance();
			//if (e1.getFrequency() >0) score1 = score1 * (e1.getFrequency()+1);
			//if (e1.getInverseFrequency() >0) score1 = score1 / e1.getInverseFrequency();
			//e1.setFinalScore(score1);
			
			//double score2 = e2.getRelevance();
			//if (e2.getFrequency() >0) score2 = score2 * (e2.getFrequency()+1);
			//if (e2.getInverseFrequency() >0) score2 = score2 / e2.getInverseFrequency();
			//e2.setFinalScore(score2);

			return Double.compare(e2.getFinalScore(), e1.getFinalScore());
		}
	}