package controllers;

import com.parse.ParseException;

import java.util.List;

import models.Despesa;

public interface ParseListenerDespesa {


    void onDataRetrievedDespesas(List<Despesa> despesas);

    void onDataRetrieveFail(ParseException e);


}
