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

package org.apache.hupa.shared.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hupa.shared.SConsts;

public class MessageDetails implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String text;
    private ArrayList<MessageAttachment> aList;
    private long uid;
    private String raw;
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();

    
    public String toString() {
        return "uid=" + String.valueOf(getUid()) +
        " text.length=" + (text != null ? text.length() : 0) + 
        " raw.length=" + (raw != null ? raw.length() : 0) + 
        " attachments=" + (aList != null ? aList.size() : 0) + 
        " headers=" + (headers != null ? headers.size() : 0); 
    }
    
    
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
    
    /**
     * Set a raw String representation of the header
     * 
     * @param raw
     */
    public void setRawHeader(String raw) {
        this.raw = raw;
    }

    /**
     * Return a raw String representation of the header
     * 
     * @return raw
     */
    public String getRawHeader() {
        return raw;
    }
    
    public Map<String, List<String>> getHeaders() {
		return headers;
	}
    
    public void addHeader(String name, String value) {
    	List<String> list = headers.get(name);
    	if (list == null) {
    		list = new ArrayList<String>();
    		headers.put(name, list);
    	}
    	list.add(value);
    }
    
    
    
    /**
     * Set the body text of the content
     * 
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Return the body text of the content
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the attachments 
     * 
     * @param aList
     */
    public void setMessageAttachments(ArrayList<MessageAttachment> aList) {
        this.aList = aList;
    }

    /**
     * 
     * @return the In-Reply-To header field.
     */
    public String getInReplyTo() {
        return getHeaderValue(SConsts.HEADER_IN_REPLY_TO);
    }

    public String getHeaderValue(String key) {
        List<String> h = headers.get(key);
        return h != null && !h.isEmpty() ? h.get(0) : null;
    }

    /**
     * 
     * @return the References header field.
     */
    public String getReferences() {
        return getHeaderValue(SConsts.HEADER_REFERENCES);
    }

    public String getMessageId() {
        return getHeaderValue(SConsts.HEADER_MESSAGE_ID);
    }    
    
    /**
     * Return the attachments 
     * 
     * @return aList
     */
    public ArrayList<MessageAttachment> getMessageAttachments() {
        return aList;
    }


    public boolean equals(Object obj) {
        if (obj instanceof MessageDetails) {
            if (((MessageDetails)obj).getUid() == getUid()) {
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        Long l = Long.valueOf(getUid());
        return l.intValue() * 16;
    }
}
