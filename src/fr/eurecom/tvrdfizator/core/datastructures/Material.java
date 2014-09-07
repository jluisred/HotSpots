package fr.eurecom.tvrdfizator.core.datastructures;

public class Material {
	
	private String type;
	private String howRelated;
	private String format;
	private String MediaUri;
	private String text;
	private String reference;

	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHowRelated() {
		return howRelated;
	}
	public void setHowRelated(String howRelated) {
		this.howRelated = howRelated;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getMediaUri() {
		return MediaUri;
	}
	public void setMediaUri(String mediaUri) {
		MediaUri = mediaUri;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}


}
