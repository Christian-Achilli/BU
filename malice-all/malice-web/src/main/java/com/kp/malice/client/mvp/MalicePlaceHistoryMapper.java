package com.kp.malice.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.kp.malice.client.place.UserPlace;

@WithTokenizers({UserPlace.Tokenizer.class})
public interface MalicePlaceHistoryMapper extends PlaceHistoryMapper {
}
