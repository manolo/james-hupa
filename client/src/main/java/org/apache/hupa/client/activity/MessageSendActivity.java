package org.apache.hupa.client.activity;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStatusChangedHandler;

import java.util.ArrayList;
import java.util.List;


import org.apache.hupa.client.evo.HupaEvoCallback;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.validation.EmailListValidator;
import org.apache.hupa.shared.Util;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.ContactsUpdatedEvent;
import org.apache.hupa.shared.events.ContactsUpdatedEventHandler;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;
import org.apache.hupa.shared.rpc.ForwardMessage;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.ReplyMessage;
import org.apache.hupa.shared.rpc.SendMessage;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;

public class MessageSendActivity extends AbstractActivity {

    private ArrayList<MessageAttachment> attachments = new ArrayList<MessageAttachment>();
    private Type type = Type.NEW;
    private ImapFolder folder;
    private Message oldmessage;
    
    protected SMTPMessage message = null;
    @SuppressWarnings("unused")
    private MessageDetails oldDetails;
    
//	@Inject private DispatchAsync dispatcher;
    @Inject private Displayable display;
    @Inject private EventBus eventBus;
    @Inject private PlaceController placeController;
	
	private MessageSendPlace place;
	
	private User user;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		revealDisplay(user, folder, oldmessage, oldDetails, type);
		container.setWidget(display.asWidget());
	}
	
	public MessageSendActivity with(MessageSendPlace place){
		this.place = place;
		this.user = place.getUser();
		this.folder = place.getFolder();
		this.oldmessage = place.getMessage();
		this.oldDetails = place.getMessageDetails();
		this.type = place.getForward();
		return this;
	}

	private void bind(){
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
            public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
                reset();
            }
        });
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {
            public void onFolderSelectionEvent(FolderSelectionEvent event) {
                reset();
            }
        });
		eventBus.addHandler(ContactsUpdatedEvent.TYPE, new ContactsUpdatedEventHandler() {
            public void onContactsUpdated(ContactsUpdatedEvent event) {
                display.fillContactList(event.getContacts());
            }
        });
		display.getSendClick().addClickHandler(sendClickHandler);
		display.getBackButtonClick().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new BackEvent());
            }
        });

        display.getUploader().addOnStatusChangedHandler(onStatusChangedHandler);
        display.getUploader().addOnFinishUploadHandler(onFinishUploadHandler);
        display.getUploader().addOnCancelUploadHandler(onCancelUploadHandler);

        reset();
	}

    protected ClickHandler sendClickHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (validate()) {
                message = new SMTPMessage();
                message.setFrom(display.getFromText().getText());
                message.setSubject(display.getSubjectText().getText());
                message.setText(display.getMessageHTML().getHTML());
                message.setMessageAttachments(attachments);
                message.setTo(emailTextToArray(display.getToText().getText()));
                message.setCc(emailTextToArray(display.getCcText().getText()));
                message.setBcc(emailTextToArray(display.getBccText().getText()));

                SendMessage command;
                if (type == Type.NEW) {
                    command = new SendMessage(message);
                } else if (type == Type.FORWARD) {
                    command = new ForwardMessage(message, folder, oldmessage.getUid());
                } else {
                    command = new ReplyMessage(message, folder, oldmessage.getUid());
                }
//                dispatchMessage(dispatcher, eventBus, command);
            }
        }
    };

    protected boolean validate() {
        // Don't trust only in view validation
        return  display.validate() 
                && display.getToText().getText().trim().length() > 0  
                && EmailListValidator.isValidAddressList(display.getToText().getText()) 
                && EmailListValidator.isValidAddressList(display.getCcText().getText()) 
                && EmailListValidator.isValidAddressList(display.getBccText().getText());
    }

    protected ArrayList<String> emailTextToArray(String emails) {
        ArrayList<String> cc = new ArrayList<String>();
        for (String ccRaw : emails.split("[,;]+")) {
            String ccRecip = ccRaw.trim();
            if (ccRecip.length() > 0) {
                cc.add(ccRaw.trim());
            }
        }
        return cc;
    }

    // Although dispatcher and eventBus parameters are not necessary, they are needed for testability
//    protected void dispatchMessage(DispatchAsync dispatcher, final EventBus eventBus, Action<GenericResult> command) {
//        display.setLoading(true);
//        dispatcher.execute(command, new HupaEvoCallback<GenericResult>(dispatcher, eventBus) {
//            public void callback(GenericResult result) {
//                if (result.isSuccess()) {
//                    eventBus.fireEvent(new SentMessageEvent());
//                    reset();
//                } else {
//                    eventBus.fireEvent(new FlashEvent(result.getMessage(), 6000));
//                }
//                display.setLoading(false);
//            }
//        });
//    }
    
    public void revealDisplay(User user, ImapFolder folder, Message oldmessage, MessageDetails oldDetails, String mailto, Type type) {
        this.reset();
        this.oldmessage = oldmessage;
        this.oldDetails = oldDetails;
        this.folder = folder;
        this.type = type;

        // Depending on the type, we have to automatically fill the view inputs
        display.getFromText().setText(user.getName());
        display.getMessageHTML().setHTML(wrapMessage(oldmessage, oldDetails, type));
        if (type.equals(Type.NEW) && mailto != null) {
                display.getToText().setText(mailto);
        } else if (type.equals(Type.FORWARD)) {
        	String subject = oldmessage.getSubject() != null ? oldmessage.getSubject().trim() : "";
        	if (!subject.toLowerCase().startsWith("fwd:")){
        		subject = "Fwd: " + subject;
        	}
        	display.getSubjectText().setText(subject);
        } else if (type.equals(Type.REPLY) || type.equals(Type.REPLY_ALL)) {
        	String subject = oldmessage.getSubject() != null ? oldmessage.getSubject().trim() : "";
        	if (!subject.toLowerCase().startsWith("re:")){
        		subject = "Re: " + subject;
        	}
        	display.getSubjectText().setText(subject);
            if (type.equals(Type.REPLY)) { 
                if (oldmessage.getReplyto() != null && !oldmessage.getFrom().contains(oldmessage.getReplyto())) {
                    display.getToText().setText(oldmessage.getReplyto());
                } else {
                    display.getToText().setText(oldmessage.getFrom());
                }
            } else {
                ArrayList<String> list = new ArrayList<String>();
                if (oldmessage.getReplyto() != null && !oldmessage.getFrom().contains(oldmessage.getReplyto())) 
                    list.add(oldmessage.getReplyto());
                if (oldmessage.getTo() != null)
                    list.addAll(oldmessage.getTo());
                if (oldmessage.getCc() != null)
                    list.addAll(oldmessage.getCc());
                list = removeEmailFromList(list, user.getName());
                display.getCcText().setText(Util.listToString(list));
                if (oldmessage.getTo() != null) {
                    oldmessage.getTo().remove(user.getName());
                }
                display.getToText().setText(oldmessage.getFrom());
            }
        } 
        display.refresh();
        display.getEditorFocus().setFocus(true);
    }

    public void revealDisplay(User user, ImapFolder folder, Message oldmessage, MessageDetails oldDetails, Type type) {
        this.revealDisplay(user, folder, oldmessage, oldDetails, null, type);
    }

    public void revealDisplay(User user, String mailto) {
        revealDisplay(user, null, null, null, mailto, Type.NEW);
    }

    /**
     * Bind the given user to the presenter
     * 
     * @param user
     */
    public void revealDisplay(User user) {
        revealDisplay(user, null, null, null, null, Type.NEW);
    }
    
    private static String generateHeader(Message message, Type type) {
        String ret = "<br>";
        if (message != null) {
            if (type.equals(Type.FORWARD)) {
                ret += "--------- Forwarded message --------- <br>";
                ret += "From: " + message.getFrom().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "<br>";
                ret += "Date: " + message.getReceivedDate() + "<br>";
                ret += "Subject: " + message.getSubject() + "<br>";
                ArrayList<String> to = new ArrayList<String>();
                to.addAll(message.getTo());
                to.addAll(message.getCc());
                ret += "To: " + Util.listToString(to).replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "<br>";
            } else if (type.equals(Type.REPLY) || type.equals(Type.REPLY_ALL)) {
                ret += "On " + message.getReceivedDate();
                ret += ", " + message.getFrom().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                ret += ". wrote:<br>";
            }
        }
        return ret + "<br>";
    }

    public static String wrapMessage(Message message, MessageDetails details, Type type) {
        String ret = "";
        if (message != null) {
            ret += generateHeader(message, type);
        }
        if (details != null && details.getText() != null && details.getText().length() > 0) {
            ret += "<blockquote style='border-left: 1px solid rgb(204, 204, 204); margin: 0pt 0pt 0pt 0.8ex; padding-left: 1ex;'>";
            ret += details.getText();
            ret += "</blockquote>";
        }
        return ret;
    }

    
    protected ArrayList<String> removeEmailFromList(List<String> list, String email) {
        ArrayList<String> ret = new ArrayList<String>();
        String regex = ".*<?\\s*" + email.trim() + "\\s*>?\\s*"; 
        for(String e: list) {
            if (! e.matches(regex)) {
                ret.add(e);
            }
        }
        return ret;
    }
    /**
     * Reset everything
     */
    private void reset() {
        display.getUploader().reset();
        display.getBccText().setText("");
        display.getCcText().setText("");
        display.getToText().setText("");
        display.getSubjectText().setText("");
        attachments.clear();
        folder = null;
        oldmessage = null;
        type = Type.NEW;
    }
    /**
     * The Type for which the SendPresenter will get used
     */
    public enum Type {
        NEW, REPLY, REPLY_ALL, FORWARD
    }
    private OnFinishUploaderHandler onFinishUploadHandler = new OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                String name = uploader.getInputName();
                MessageAttachment attachment = new MessageAttachment();
                attachment.setName(name);
                attachments.add(attachment);
                display.getSendEnable().setEnabled(true);
            }
        }
    };

    private OnStatusChangedHandler onStatusChangedHandler = new OnStatusChangedHandler() {
        public void onStatusChanged(IUploader uploader) {
            Status stat = display.getUploader().getStatus();
            if (stat == Status.INPROGRESS)
                display.getSendEnable().setEnabled(false);
            else
                display.getSendEnable().setEnabled(true);
        }
    };

    private OnCancelUploaderHandler onCancelUploadHandler = new OnCancelUploaderHandler() {
        public void onCancel(IUploader uploader) {
            for (MessageAttachment attachment : attachments) {
                if (attachment.getName().equals(uploader.getInputName()))
                    attachments.remove(attachment);
            }
        }
    };
	public interface Displayable extends WidgetDisplayable {
		public HasText getFromText();
		public HasText getToText();
		public HasText getCcText();
		public HasText getBccText();
		public HasText getSubjectText();
		public HasHTML getMessageHTML();
		public Focusable getEditorFocus();
		public HasClickHandlers getSendClick();
		public HasEnable getSendEnable();
		public IUploader getUploader();
		public HasClickHandlers getBackButtonClick();
		public void refresh();
		public void setLoading(boolean loading);
		public void fillContactList(Contact[] contacts);
		public boolean validate();
	}
}
