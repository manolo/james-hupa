package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.MessageDetails;

public class GetMessageDetailsResultImpl implements GetMessageDetailsResult{
    private MessageDetails messageDetails;

	public GetMessageDetailsResultImpl() {
	    super();
    }

	public GetMessageDetailsResultImpl(MessageDetails messageDetails) {
	    super();
	    this.messageDetails = messageDetails;
    }

	@Override
	public MessageDetails getMessageDetails() {
    	return messageDetails;
    }

	@Override
	public void setMessageDetails(MessageDetails messageDetails) {
    	this.messageDetails = messageDetails;
    }
}
