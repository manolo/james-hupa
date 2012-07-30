package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.GetMessageRawAction;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.rpc.RawMessage;

public class GetMessageRawActionImpl implements GetMessageRawAction {

    private ImapFolder folder;
    private long uid;

    public GetMessageRawActionImpl(ImapFolder folder, long uid) {
        this.folder = folder;
        this.uid = uid;
    }

    public GetMessageRawActionImpl() {
    }
    
    public ImapFolder getFolder() {
        return folder;
    }
    
    public long getUid() {
        return uid;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof RawMessage) {
            RawMessage action = (RawMessage) obj;
            if (action.getFolder().equals(getFolder()) && action.getUid() == getUid()) {
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return (int) (getFolder().hashCode() * getUid());
    }
    
}
