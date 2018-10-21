package io.ticketcoin.dashboard.persistence.service.exception;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -99961274448053594L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
