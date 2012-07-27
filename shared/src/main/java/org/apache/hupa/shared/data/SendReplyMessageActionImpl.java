package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.SendReplyMessageAction;

public class SendReplyMessageActionImpl extends SendForwardMessageActionImpl implements SendReplyMessageAction {
	public SendReplyMessageActionImpl() {
	}
	public SendReplyMessageActionImpl(SmtpMessage msg, ImapFolder folder, long uid) {
		super(msg, folder, uid);
	}
}
