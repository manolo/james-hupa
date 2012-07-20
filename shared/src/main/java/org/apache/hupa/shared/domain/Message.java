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

package org.apache.hupa.shared.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.data.Tag;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(Message.class)
public interface Message extends ValueProxy {

	ArrayList<IMAPFlag> getFlags();

	Date getReceivedDate();

	boolean hasAttachment();

	String getFrom();

	void setFrom(String cellValue);

	void setReceivedDate(Date cellValue);

	String getSubject();

	void setSubject(String cellValue);

	List<String> getCc();

	List<String> getTo();

	void setTo(ArrayList<String> to);

	void setCc(ArrayList<String> cc);

	void setReplyto(String string);

	void setUid(long uid);

	void setFlags(ArrayList<IMAPFlag> iFlags);

	void setTags(ArrayList<Tag> tags);

	void setHasAttachments(boolean hasAttachment);

	long getUid();

	String getReplyto();
}
