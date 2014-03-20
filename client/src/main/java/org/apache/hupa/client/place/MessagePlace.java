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

package org.apache.hupa.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MessagePlace extends HupaPlace {

    public static class TokenWrapper {
        String folder;
        String uid;

        public TokenWrapper(String folder, String uid) {
            this.folder = folder;
            this.uid = uid;
        }
        public String getUid() {
            return uid;
        }
        public String getFolder() {
            return folder;
        }

        @Override
        public String toString() {
            return folder + SPLITTER + uid;
        }
    }

    TokenWrapper tokenWrapper;

    public TokenWrapper getTokenWrapper() {
        return tokenWrapper;
    }

    public MessagePlace(String token) {
        String[] params = token.split(SPLITTER);
        if (params.length == 2) {
            this.tokenWrapper = new TokenWrapper(params[0], params[1]);
        }
    }

    @Prefix("message")
    public static class Tokenizer implements PlaceTokenizer<MessagePlace> {

        @Override
        public MessagePlace getPlace(String token) {
            return new MessagePlace(token);
        }

        @Override
        public String getToken(MessagePlace place) {
            return place.getTokenWrapper().getFolder() + SPLITTER + place.getTokenWrapper().getUid();
        }
    }

}
