package controllers;

import com.parse.ParseException;

import java.util.List;

import models.Receita;

public interface ParseListenerReceita {


    void onDataRetrievedReceita(List<Receita> receitas);

    void onDataRetrieveFail(ParseException e);


}
