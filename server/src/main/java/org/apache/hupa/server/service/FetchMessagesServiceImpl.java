package org.apache.hupa.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.BodyTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.apache.hupa.shared.domain.FetchMessagesAction;

import com.sun.mail.imap.IMAPFolder;

public class FetchMessagesServiceImpl extends FetchMessagesBaseServiceImpl implements FetchMessagesService{

	@Override
    protected MessageConvertArray getMessagesToConvert(IMAPFolder f, FetchMessagesAction action) throws MessagingException {
        
        String searchString = action.getSearchString();
        int start = action.getStart();
        int offset = action.getOffset();
        int end = start + offset;
        Message[] messages;
        int exists;
        // check if a searchString was given, and if so use it
        if (searchString == null) {
            exists = f.getMessageCount();

            if (end > exists) {
                end = exists;
            }

            int firstIndex = exists - end + 1;
            if (firstIndex < 1) {
                firstIndex = 1;
            }
            int lastIndex = exists - start;
            
            messages = f.getMessages(firstIndex, lastIndex);
        } else {
            SearchTerm subjectTerm = new SubjectTerm(searchString);
            SearchTerm fromTerm = new FromStringTerm(searchString);
            SearchTerm bodyTerm = new BodyTerm(searchString);
            SearchTerm orTerm = new OrTerm(new SearchTerm[] { subjectTerm,
                    fromTerm, bodyTerm });
            Message[] tmpMessages = f.search(orTerm);
            if (end > tmpMessages.length) {
                end = tmpMessages.length;
            }
            exists = tmpMessages.length;

            int firstIndex = exists - end;        
            
            if (tmpMessages.length > firstIndex) {
                List<Message> mList = new ArrayList<Message>();
                for (int i = firstIndex; i < tmpMessages.length; i++) {
                    if (i == end) break;
                    mList.add(tmpMessages[i]);
                }
                messages = mList.toArray(new Message[mList.size()]);
            } else {
                messages = new Message[0];
            }
          
        }
        logger.debug("Fetching messages for user: " + getUser() + " returns: " + messages.length + " messages in " + f.getFullName());

        return new MessageConvertArray(exists, messages);
    }

}
