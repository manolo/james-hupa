package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.MailHeader;

public class MailHeaderImpl implements MailHeader {

	private String name;
	private String value;

	public MailHeaderImpl(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public MailHeaderImpl() {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
