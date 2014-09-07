package fr.eurecom.tvrdfizator.core.datastructures;


public class NERDEntity {

	private int idEntity;
	private String label;
	private long startChar;
	private long endChar;
	private String extractorType;
	private String nerdType;
	private double confidence;
	private double relevance;
	private String extractor;
	private double startNPT;
	private double endNPT;
	private String uri;
	private int inverseFrequency = 0;
	private int frequency = 0;
	private double finalScore;
	
	public NERDEntity (String label, int startChar, int endChar, String type, String nerdType, String uri, double confidence, double relevance){
		this.label = label;
		this.startChar = startChar;
		this.endChar = endChar;
		this.extractorType = type;
		this.nerdType = nerdType;
		this.uri = uri;
		this.confidence =  confidence;
		this.relevance = relevance;
	}
	public int getIdEntity() {
		return idEntity;
	}
	public void setIdEntity(int idEntity) {
		this.idEntity = idEntity;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getStartChar() {
		return startChar;
	}
	public void setStartChar(long startChar) {
		this.startChar = startChar;
	}
	public long getEndChar() {
		return endChar;
	}
	public void setEndChar(long endChar) {
		this.endChar = endChar;
	}
	public String getExtractorType() {
		return extractorType;
	}
	public void setExtractorType(String extractorType) {
		this.extractorType = extractorType;
	}
	public String getNerdType() {
		return nerdType;
	}
	public void setNerdType(String nerdType) {
		this.nerdType = nerdType;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public double getRelevance() {
		return relevance;
	}
	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}
	public String getExtractor() {
		return extractor;
	}
	public void setExtractor(String extractor) {
		this.extractor = extractor;
	}
	public double getStartNPT() {
		return startNPT;
	}
	public void setStartNPT(double startNPT) {
		this.startNPT = startNPT;
	}
	public double getEndNPT() {
		return endNPT;
	}
	public void setEndNPT(double endNPT) {
		this.endNPT = endNPT;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
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
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
}
