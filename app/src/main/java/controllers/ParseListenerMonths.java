package controllers;

import com.parse.ParseException;

import java.util.ArrayList;

import models.MonthData;

public interface ParseListenerMonths {

    void onDataRetrievedDataMonth(ArrayList<MonthData> listOfData);

    void onDataRetrieveFail(ParseException e);

}
