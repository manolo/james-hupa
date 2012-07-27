package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.SendMessageAction;

public class SendMessageActionImpl implements SendMessageAction{
    
    private SmtpMessage message;
    
    public SendMessageActionImpl(SmtpMessage msg) {
        this.message = msg;
    }
    
    protected SendMessageActionImpl() {
        
    }
    
    public SmtpMessage getMessage() {
        return message;
    }
    
    public void setMessage(SmtpMessage message) {
        this.message = message;
    }
    
    public String getInReplyTo() {
		return null;
	}

    public String getReferences() {
		return null;
	}

	@Override
    public void setInReplyTo(String inReplyTo) {
    }

	@Override
    public void setReferences(String references) {
    }

}
