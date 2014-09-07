package fr.eurecom.hotspots.datastructures;

public class TRTopic {
	String label;
	double relevance;
	String url;
	int frequency = 0;
	int inverseFrequency;
	
	double finalScore;
	

	public TRTopic(String label, String uri, Double score) {
		this.label = label;
		this.url = uri;
		this.relevance = score;
	}
	public TRTopic() {
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getRelevance() {
		return relevance;
	}
	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	
	public int getInverseFrequency() {
		return inverseFrequency;
	}
	public void setInverseFrequency(int inverseFrequency) {
		this.inverseFrequency = inverseFrequency;
	}
	
	public double getFinalScore() {
		return finalScore;
	}
	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}
	
	
}
