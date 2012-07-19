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

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which fetch all Folders for an user
 * 
 */
public class FetchFoldersHandler extends AbstractSessionHandler<FetchFolders, FetchFoldersResult>{

    @Inject
    public FetchFoldersHandler(IMAPStoreCache cache, Log logger,Provider<HttpSession> provider) {
        super(cache,logger,provider);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public FetchFoldersResult executeInternal(FetchFolders action, ExecutionContext arg1)
    throws ActionException {
        User user = getUser();
        try {

            // get the store for the user
            IMAPStore store = cache.get(user);
            com.sun.mail.imap.IMAPFolder folder = (com.sun.mail.imap.IMAPFolder) store.getDefaultFolder();
            
            // List of mail 'root' imap folders
            List<ImapFolder> imapFolders = new ArrayList<ImapFolder>();

            // Create IMAPFolder tree list
            for (Folder f : folder.list()) {
            	ImapFolder imapFolder = createIMAPFolder(f);
                imapFolders.add(imapFolder);
                walkFolders(f, imapFolder);
            }
            
            // Create the tree and return the result
            FetchFoldersResult fetchFolderResult = new FetchFoldersResult(imapFolders);
            logger.debug("Fetching folders for user: " + user + " returns:\n" + fetchFolderResult.toString());

            return fetchFolderResult;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to get folders for User " + user,e);
            throw new ActionException("Unable to get folders for User "
                    + user);
        }
    }

    /**
     * Walk through the folder's sub-folders and add sub-folders to current imapFolder
     *   
     * @param folder Folder to walk
     * @param imapFolder Current IMAPFolder
     * @throws ActionException If an error occurs
     * @throws MessagingException If an error occurs
     */
    private void walkFolders(Folder folder, ImapFolder imapFolder) throws ActionException, MessagingException{
        for (Folder f : folder.list()) {
        	ImapFolder iFolder = createIMAPFolder(f);
            imapFolder.getChildren().add(iFolder);
            walkFolders(f, iFolder);
        }
    }
    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<FetchFolders> getActionType() {
        return FetchFolders.class;
    }

    /**
     * Create a new IMAPFolder from the given Folder
     * 
     * @param folder Current folder
     * @return imapFolder Created IMAPFolder
     * @throws ActionException If an error occurs
     * @throws MessagingException If an error occurs
     */
    private ImapFolder createIMAPFolder(Folder folder) throws ActionException {

        String fullName = folder.getFullName();
        String delimiter;
        ImapFolder iFolder = null;
        
        try {
            logger.debug("Creating folder: " + fullName + " for user: " + getUser());
            delimiter = String.valueOf(folder.getSeparator());
            iFolder = (ImapFolder)new ImapFolderImpl(fullName);
            iFolder.setDelimiter(delimiter);
            if("[Gmail]".equals(folder.getFullName()))
                return iFolder;
            iFolder.setMessageCount(folder.getMessageCount());
            iFolder.setSubscribed(folder.isSubscribed());
            iFolder.setUnseenMessageCount(folder.getUnreadMessageCount());
        } catch (MessagingException e) {
            logger.error("Unable to construct folder " + folder.getFullName(),e);
        }
        
        return iFolder;
    }

}
