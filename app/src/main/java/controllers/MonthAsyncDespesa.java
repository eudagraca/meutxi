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
import models.Despesa;
import utils.Utils;

public class MonthAsyncDespesa {
    private Context context;
    private List<Despesa> despesaList;
    private ParseUser parseUser;
    private ProgressDialog progressDialog;

    public MonthAsyncDespesa(Context ctx) {
        context = ctx;
        parseUser = ParseUser.getCurrentUser();
    }


    public List<Despesa> despesas(@NonNull final ParseListenerDespesa ioListener) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Carregando registos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                .whereEqualTo("mes", Utils.currentMonth())
                .whereEqualTo("ano", Utils.currentYear())
                .whereEqualTo("tipo", Utils.DESPESA)
                .whereEqualTo("uid", parseUser.getObjectId());
        query.orderByDescending("updatedAt").findInBackground((objects, e) -> {
            if (e == null) {
                despesaList = new ArrayList<>();

                for (ParseObject data : objects) {
                    Despesa despesa = new Despesa();
                    Categorias categoria = new Categorias();
                    categoria.setName((String) data.get("categoria"));
                    despesa.setCategoria(categoria);
                    despesa.setSituacao((String) data.get("situacao"));
                    despesa.setData((String.valueOf(Utils.currentDateAndMonth(data.getCreatedAt()))));
                    despesa.setDesricao((String) data.get("descricao"));
                    despesa.setTipo((String) data.get("tipo"));
                    despesa.setId(data.getObjectId());
                    despesa.setValor(Double.parseDouble(String.valueOf(data.get("valor"))));
                    despesaList.add(despesa);
                }
                progressDialog.dismiss();
                ioListener.onDataRetrievedDespesas(despesaList);
            } else {
                progressDialog.dismiss();
                ioListener.onDataRetrieveFail(e);
            }
        });
        return despesaList;
    }
}
