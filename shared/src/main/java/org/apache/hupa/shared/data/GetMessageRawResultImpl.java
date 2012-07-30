package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.GetMessageRawResult;

public class GetMessageRawResultImpl implements GetMessageRawResult {

	private String rawMessage;

	protected GetMessageRawResultImpl() {

	}

	public GetMessageRawResultImpl(String rawMessage) {
		this.rawMessage = rawMessage;
	}

	public String getRawMessage() {
		return rawMessage;
	}
}
