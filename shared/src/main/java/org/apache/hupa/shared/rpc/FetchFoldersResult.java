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

import java.io.Serializable;
import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.apache.hupa.shared.proxy.ImapFolder;

public class FetchFoldersResult implements Result, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6215610133650989605L;
    private List<ImapFolder> folders;

    public FetchFoldersResult(List<ImapFolder> folders) {
        this.folders=folders;
    }
    
    @SuppressWarnings("unused")
    private FetchFoldersResult() {
    }
    
    public List<ImapFolder> getFolders() {
        return folders;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer("");
        for (ImapFolder folder : folders) {
            ret.append(folder.getFullName()).append("\n");
            for (ImapFolder f : folder.getChildren()) {
                childFolder(f, ret);
            }
        }
        return ret.toString();
    }
    
    private void childFolder(ImapFolder child, StringBuffer ret) {
        ret.append(child.getFullName()).append("\n");
        for (ImapFolder folder : child.getChildren()) {
            childFolder(folder, ret);
        }
    }
    
}
