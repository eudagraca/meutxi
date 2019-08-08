package controllers;

import androidx.annotation.NonNull;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import models.Categorias;

public class CategoriesController {

    @NonNull
    private ArrayList<Categorias> categories = new ArrayList<>();

    public CategoriesController() {
    }

    @NonNull
    public ArrayList<Categorias> onCompleteGetData(String tableName, @NonNull final ParseListenerCategorias ioListener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.orderByAscending("name").findInBackground((objects, e) -> {
            for (ParseObject objectList : objects) {
                Categorias categ = new Categorias();
                categ.setName(String.valueOf(objectList.get("name")));
                categ.setCategoriaId(objectList.getObjectId());
                this.categories.add(categ);
            }
            ioListener.onDataRetrieved(categories);
        });
        return this.categories;
    }


}
