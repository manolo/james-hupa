package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.FetchMessagesAction;

public class FetchMessagesActionImpl implements FetchMessagesAction {

	private ImapFolder folder;
	private int start;
	private int offset;
	private String searchString;

	public FetchMessagesActionImpl() {
	}

	public FetchMessagesActionImpl(ImapFolder folder, int start, int offset, String searchString) {
		this.folder = folder;
		this.start = start;
		this.offset = offset;
		this.searchString = searchString;
	}

	@Override
	public ImapFolder getFolder() {
		return folder;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getSearchString() {
		return searchString;
	}

	@Override
	public void setFolder(ImapFolder folder) {
		this.folder = folder;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}
