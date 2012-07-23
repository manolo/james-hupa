package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.GenericResult;

public class GenericResultImpl implements GenericResult {

	private String message = null;
	private boolean success = true;

	public GenericResultImpl() {
	}
	public GenericResultImpl(String message, boolean success) {
		this.message = message;
		this.success = success;
	}
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;

	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public void setError(String message) {
		setMessage(message);
		setSuccess(false);
	}

}
