package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.MoveMessageAction;

public class MoveMessageActionImpl implements MoveMessageAction {

    private ImapFolder oldFolder;
    private ImapFolder newFolder;
    private long messageUid;

    public MoveMessageActionImpl(ImapFolder oldFolder, ImapFolder newFolder, long messageUid) {
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
        this.messageUid = messageUid;
    }
    
    protected MoveMessageActionImpl() {
    }
    
    public long getMessageUid() {
        return messageUid;
    }
    
    public ImapFolder getOldFolder() {
        return oldFolder;
    }
    
    public ImapFolder getNewFolder() {
        return newFolder;
    }
    
    public void setMessageUid(long messageUid){
    	this.messageUid = messageUid;
    }

	public void setOldFolder(ImapFolder oldFolder) {
    	this.oldFolder = oldFolder;
    }

	public void setNewFolder(ImapFolder newFolder) {
    	this.newFolder = newFolder;
    }
}
