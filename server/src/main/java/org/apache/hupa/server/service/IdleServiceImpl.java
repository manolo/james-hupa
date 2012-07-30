package org.apache.hupa.server.service;

import org.apache.hupa.shared.data.IdleResultImpl;
import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;

import com.sun.mail.imap.IMAPStore;

public class IdleServiceImpl extends AbstractService implements IdleService {
	@Override
	public IdleResult idle(IdleAction action) throws Exception {
		try {
			IMAPStore store = cache.get(getUser());

			if (store.getURLName() != null) {
				// check if the store supports the IDLE command
				if (store.hasCapability("IDLE")) {
					// just send a noop to keep the connection alive
					store.idle();
				} else {
					return new IdleResultImpl(false);
				}
			}
			return new IdleResultImpl(true);
		} catch (Exception e) {
			throw new Exception("Unable to send NOOP " + e.getMessage());
		}
	}
}
