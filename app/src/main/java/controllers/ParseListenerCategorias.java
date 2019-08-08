package controllers;

import com.parse.ParseException;

import java.util.ArrayList;

import models.Categorias;

public interface ParseListenerCategorias {

    void onDataRetrieved(ArrayList<Categorias> listOfData);

    void onDataRetrieveFail(ParseException e);
}
