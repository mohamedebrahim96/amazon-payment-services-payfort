package com.payfort.fortpaymentsdk.exceptions;

/**
 * Type of Security class Exception
 */
public class FortException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String msg;

	public FortException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public FortException(String msg, Throwable throwable) {
		super(throwable);
		this.msg = msg;
	}

	public String getCode() {
		return msg;
	}

}
