package com.kalam.analytics.platformz.common;

import java.io.Serializable;
import java.util.List;

public class UnkotiApiResponse<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 9081468061891803751L;

	public static enum Status {
		SUCCESS("success"), FAILURE("failure");

		String status;

		private Status(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return status;
		}
	}

	Status status;
	String message;
	T data;

	boolean hasErrors;
	List<String> errors;

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the hasErrors
	 */
	public boolean isHasErrors() {
		return hasErrors;
	}

	/**
	 * @param hasErrors
	 *            the hasErrors to set
	 */
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AndLabApiResponse [status = " + status + ", message = " + message + ", data = " + data
				+ ", hasErrors = " + hasErrors + ", errors=" + errors + "]";
	}

}
