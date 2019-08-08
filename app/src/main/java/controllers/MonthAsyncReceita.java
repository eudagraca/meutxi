package controllers;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import models.Categorias;
import models.Receita;
import utils.Utils;

public class MonthAsyncReceita {

    private Context context;
    private List<Receita> receitaList;
    private ParseUser parseUser;
    private ProgressDialog progressDialog;

    public MonthAsyncReceita(Context ctx) {
        context = ctx;
        parseUser = ParseUser.getCurrentUser();
    }

    public List<Receita> receitas(@NonNull final ParseListenerReceita ioListener) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Carregando registos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                .whereEqualTo("mes", Utils.currentMonth())
                .whereEqualTo("ano", Utils.currentYear())
                .whereEqualTo("tipo", Utils.RECEITA)
                .whereEqualTo("uid", parseUser.getObjectId());
        query.orderByDescending("updatedAt").findInBackground((objects, e) -> {
            if (e == null) {
                receitaList = new ArrayList<>();

                for (ParseObject data : objects) {
                    Receita receita = new Receita();
                    Categorias categoria = new Categorias();
                    categoria.setName((String) data.get("categoria"));
                    receita.setCategoria(categoria);
                    receita.setSituacao((String) data.get("situacao"));
                    receita.setData((String.valueOf(Utils.currentDateAndMonth(data.getCreatedAt()))));
                    receita.setDesricao((String) data.get("descricao"));
                    receita.setTipo((String) data.get("tipo"));
                    receita.setId(data.getObjectId());
                    receita.setValor(Double.parseDouble(String.valueOf(data.get("valor"))));
                    receitaList.add(receita);
                }
                progressDialog.dismiss();
                ioListener.onDataRetrievedReceita(receitaList);
            } else {
                progressDialog.dismiss();
                ioListener.onDataRetrieveFail(e);
            }
        });
        return receitaList;
    }

}
