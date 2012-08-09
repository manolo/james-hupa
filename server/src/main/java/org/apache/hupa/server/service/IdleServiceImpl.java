/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

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
