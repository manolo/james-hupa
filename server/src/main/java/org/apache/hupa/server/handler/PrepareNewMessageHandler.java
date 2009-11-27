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

package org.apache.hupa.server.handler;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.shared.rpc.Noop;
import org.apache.hupa.shared.rpc.NoopResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handle Noops
 * 
 *
 */
public class PrepareNewMessageHandler extends AbstractSessionHandler<Noop, NoopResult>{


    @Inject
    public PrepareNewMessageHandler(IMAPStoreCache cache, Log logger, Provider<HttpSession> provider) {
        super(cache,logger,provider);
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public NoopResult executeInternal(Noop action, ExecutionContext context)
            throws ActionException {
        try {
            IMAPStore store = cache.get(getUser());
            if (store.getURLName() != null &&
                !DemoModeConstants.DEMO_MODE.equals(store.getURLName().getHost()) ) {
                // just send a noop to keep the connection alive
                store.idle();
            }
            return new NoopResult();
        } catch (Exception e) {
            throw new ActionException("Unable to send NOOP " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<Noop> getActionType() {
        return Noop.class;
    }

}