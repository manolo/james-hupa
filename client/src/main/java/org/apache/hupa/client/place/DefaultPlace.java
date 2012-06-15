package org.apache.hupa.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class DefaultPlace extends Place {

  @Prefix("")
  public static class Tokenizer implements PlaceTokenizer<DefaultPlace> {

    @Override
    public DefaultPlace getPlace(String token) {
      return new DefaultPlace();
    }

    @Override
    public String getToken(DefaultPlace place) {
      return "";
    }
  }
  
  public String toString(){
	  return this.getClass().getName()+"->[home page]";
  }

}
