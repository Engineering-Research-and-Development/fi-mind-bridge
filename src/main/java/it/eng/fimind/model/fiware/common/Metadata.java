package it.eng.fimind.model.fiware.common;

public class Metadata {
	private TimeInstant TimeInstant;
	private DateCreated DateCreated;
	private DateModified DateModified;
	
	public Metadata() {}

	public TimeInstant getTimeInstant() {
		return TimeInstant;
	}

	public void setTimeInstant(TimeInstant timeInstant) {
		TimeInstant = timeInstant;
	};
	
	public DateCreated getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(DateCreated dateCreated) {
		DateCreated = dateCreated;
	};
	public DateModified getDateModified() {
		return DateModified;
	}

	public void setDateModified(DateModified dateModified) {
		DateModified = dateModified;
	};
	
	
	
	
}
