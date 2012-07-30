package org.apache.hupa.shared.data;

import java.util.List;

import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.SetFlagAction;

public class SetFlagActionImpl implements SetFlagAction {

    private IMAPFlag flag;
    private List<Long> uids;
    private ImapFolder folder;
    private boolean value;
    
    public SetFlagActionImpl(ImapFolder folder, IMAPFlag flag, boolean value, List<Long> uids) {
        this.flag = flag;
        this.value = value;
        this.uids = uids;
        this.folder = folder;
    }
    
    protected SetFlagActionImpl() {
    }
    
    public ImapFolder getFolder() {
        return folder;
    }
    
    public boolean getValue() {
        return value;
    }
    public IMAPFlag getFlag() {
        return flag;
    }
    
    public List<Long> getUids() {
        return uids;
    }

	public void setFlag(IMAPFlag flag) {
    	this.flag = flag;
    }

	public void setUids(List<Long> uids) {
    	this.uids = uids;
    }

	public void setFolder(ImapFolder folder) {
    	this.folder = folder;
    }

	public void setValue(boolean value) {
    	this.value = value;
    }
}
