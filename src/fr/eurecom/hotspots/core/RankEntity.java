package fr.eurecom.hotspots.core;

import java.util.Comparator;

import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;

public class RankEntity implements Comparator<NERDEntity> {
		public int compare(NERDEntity e1, NERDEntity e2) {
			
			double score1 = e1.getRelevance();
			if (e1.getFrequency() >0) score1 = score1 * (e1.getFrequency() + e1.getInverseFrequency());
			//if (e1.getInverseFrequency() >0) score1 = score1 / e1.getInverseFrequency();
			e1.setFinalScore(score1);

			double score2 = e2.getRelevance();
			if (e2.getFrequency() >0) score2 = score2 * (e2.getFrequency()+ e2.getInverseFrequency());
			//if (e2.getInverseFrequency() >0) score2 = score2 / e2.getInverseFrequency();
			e2.setFinalScore(score2);

			return Double.compare(score2, score1);
		}
}