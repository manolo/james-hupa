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


package org.apache.hupa.shared.rpc;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.SmtpMessage;

public class ForwardMessage extends SendMessage {

    private static final long serialVersionUID = 1656671247843122192L;
    private long uid;
    private ImapFolder folder;
    private String inReplyTo;
    private String references;

    public ForwardMessage(SmtpMessage msg, ImapFolder folder, long uid) {
        super(msg);
        this.uid = uid;
        this.folder = folder;
    }
    
    protected ForwardMessage() {
    }

    public long getReplyMessageUid() {
        return uid;
    }
    
    public ImapFolder getFolder() {
        return folder;
    }
    
    @Override
    public String getInReplyTo() {
		return inReplyTo;
	}

    @Override
    public String getReferences() {
		return references;
	}

  public ForwardMessage setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo;
		return this;
	}
   
  public ForwardMessage setReferences(String references) {
		this.references = references;
		return this;
	}
}
