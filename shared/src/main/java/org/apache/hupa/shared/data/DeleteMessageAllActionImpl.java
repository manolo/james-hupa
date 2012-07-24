package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class DeleteMessageAllActionImpl extends DeleteMessageActionImpl implements DeleteMessageAllAction {
	public DeleteMessageAllActionImpl(){}
	public DeleteMessageAllActionImpl(ImapFolder folder){
		super(folder);
	}
}
