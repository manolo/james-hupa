package org.apache.hupa.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MessageSendPlace extends Place {

  @Prefix("MessageSend")
  public static class Tokenizer implements PlaceTokenizer<MessageSendPlace> {

    @Override
    public MessageSendPlace getPlace(String token) {
      return new MessageSendPlace();
    }

    @Override
    public String getToken(MessageSendPlace place) {
      return "MessageSend";
    }
  }
  
  public String toString(){
	  return this.getClass().getName()+"->[MessageSend]";
  }

}
