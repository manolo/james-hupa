package org.apache.hupa.client.ui;

import java.util.Date;

import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.inject.Inject;

public class MessagesCellTable extends CellTable<Message> {

	private static final int PAGE_SIZE = 15;
	
	private HupaImageBundle imageBundle;

	@Inject
	public MessagesCellTable(
	        final HupaImageBundle imageBundle) {
		super(PAGE_SIZE);
		this.imageBundle = imageBundle;
		addColumn(new CheckboxColumn());
		addColumn(new FromColumn());
		addColumn(new SubjectColumn());
		addColumn(new AttachmentColumn());
		addColumn(new DateColumn());
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
	}
	private class CheckboxColumn extends Column<Message, Boolean> {
		public CheckboxColumn() {
			super(new CheckboxCell());
		}
		@Override
		public Boolean getValue(Message object) {
			return true;
		}
	}

	private class FromColumn extends Column<Message, String> {
		public FromColumn() {
			super(new TextCell());
		}
		@Override
		public String getValue(Message object) {
			return object.getFrom();
		}
	}

	private class SubjectColumn extends Column<Message, String> {
		public SubjectColumn() {
			super(new TextCell());
		}
		@Override
		public String getValue(Message object) {
			return object.getSubject();
		}
	}

	private class AttachmentColumn extends Column<Message, ImageResource> {
		public AttachmentColumn() {
			super(new ImageResourceCell());
		}
		@Override
		public ImageResource getValue(Message object) {
			return object.hasAttachment() ? imageBundle.attachmentIcon() : null;
		}
	}

	private class DateColumn extends Column<Message, Date> {
		public DateColumn() {
			super(new DateCell(DateTimeFormat.getFormat("dd.MMM.yyyy")));
		}
		@Override
		public Date getValue(Message object) {
			return object.getReceivedDate();
		}
	}
}
