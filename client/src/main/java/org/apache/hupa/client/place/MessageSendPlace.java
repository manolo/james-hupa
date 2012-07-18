package org.apache.hupa.client.place;

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MessageSendPlace extends Place {

	
	private User user ;
	private ImapFolder folder;
	private Message message;
	private MessageDetails messageDetails;
	private Type forward;
	
	
	@Prefix("send")
	public static class Tokenizer implements PlaceTokenizer<MessageSendPlace> {

		@Override
		public MessageSendPlace getPlace(String token) {
			return new MessageSendPlace();
		}

		@Override
		public String getToken(MessageSendPlace place) {
			return place.getForward().toString();
		}
	}

	public String toString() {
		return this.getClass().getName() + "->[MessageSend]";
	}

	public Place with(User user, ImapFolder folder, Message message, MessageDetails messageDetails, Type forward) {
		this.forward = forward;
		this.user = user;
		this.folder = folder;
		this.message = message;
		this.messageDetails = messageDetails;
		return this;
	}

	public User getUser() {
		return user;
	}

	public ImapFolder getFolder() {
		return folder;
	}

	public Message getMessage() {
		return message;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public Type getForward() {
		return forward;
	}
	
	

	
	
}
