package controllers;

import com.parse.ParseException;

public interface LoadingUpdate {

    boolean onUpdate();

    void onDataRetrieveFail(ParseException e);
}
