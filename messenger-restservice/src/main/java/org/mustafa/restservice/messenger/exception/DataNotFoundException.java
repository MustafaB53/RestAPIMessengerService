package org.mustafa.restservice.messenger.exception;

public class DataNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7166068750856934056L;
	
	public DataNotFoundException(String message){
		super(message);
	}
}
