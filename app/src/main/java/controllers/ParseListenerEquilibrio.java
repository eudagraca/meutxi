package controllers;

import com.parse.ParseException;

public interface ParseListenerEquilibrio {

    void onDataRetrievedMoney(Double receita, Double despesa, Double equilibrio);

    void onDataRetrieveFail(ParseException e);
}