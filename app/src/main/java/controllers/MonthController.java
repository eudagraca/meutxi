package controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.account.views.MainActivity_;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.Balanco;
import models.Categorias;
import models.Data;
import models.Despesa;
import models.MonthData;
import models.Receita;
import utils.Utils;

public class MonthController {

    private MonthData monthData;
    private Context context;
    private Receita receita;
    private Despesa despesa;
    private SweetAlertDialog dialog;
    private ParseUser parseUser = ParseUser.getCurrentUser();
    private List<Despesa> despesaList;

    public MonthController(Context context, Despesa despesa) {
        this.context = context;
        this.despesa = despesa;
    }

    public MonthController(Context context, MonthData monthData) {
        this.context = context;
        this.monthData = monthData;
    }

    public MonthController(Context context, Receita receita) {
        this.context = context;
        this.receita = receita;
    }

    public MonthController(Context context) {
        this.context = context;
    }

    //Save
    public void saveDespesa() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Aguarde um instante...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Data data = new Data();

        ParseObject despesas = new ParseObject(Utils.DATAOFMONTH);
        despesas.put("uid", parseUser.getObjectId());
        despesas.put("situacao", despesa.getSituacao());
        despesas.put("data", despesa.getDataAgora());
        despesas.put("descricao", despesa.getDesricao());
        despesas.put("valor", despesa.getValor());
        despesas.put("tipo", despesa.getTipo());
        despesas.put("mes", data.getMes());
        despesas.put("ano", data.getAno());
        despesas.put("categoria", despesa.getCategoria().getName());
        despesas.saveEventually(e -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            alert("Registou", "despesa");
        });
    }

    public void saveReceita() {
        //Declare progressDialog before so you can use .hide() later!
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Aguarde um instante...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Data data = new Data();

        ParseObject receitas = new ParseObject(Utils.DATAOFMONTH);
        receitas.put("uid", parseUser.getObjectId());
        receitas.put("situacao", receita.getSituacao());
        receitas.put("data", receita.getDataAgora());
        receitas.put("descricao", receita.getDesricao());
        receitas.put("valor", receita.getValor());
        receitas.put("tipo", receita.getTipo());
        receitas.put("mes", data.getMes());
        receitas.put("ano", data.getAno());
        receitas.put("categoria", receita.getCategoria().getName());
        receitas.saveInBackground(e -> {
            if (e == null) {
                progressDialog.dismiss();
                alert("Registou", "receita");
            } else {
                progressDialog.dismiss();
                dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText(e.getMessage());
                dialog.setConfirmClickListener(v -> dialog.dismissWithAnimation());
                dialog.show();
            }
        });
    }

    //Updating
    public void update(String itemId) {
        //Declare progressDialog before so you can use .hide() later!
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = new ParseQuery<>(Utils.DATAOFMONTH);
        query.getInBackground(itemId, (despesas, e) -> {

            despesas.put("valor", monthData.getValor());
            despesas.put("descricao", monthData.getDesricao());
            despesas.put("categoria", monthData.getCategoria().getName());
            despesas.saveInBackground(e1 -> {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                alert("", "");
            });

        });
    }

    public void getAllDespesas(@NonNull final ParseListenerDespesa ioListener) {
        despesaList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                .whereEqualTo("mes", Utils.currentMonth())
                .whereEqualTo("ano", Utils.currentYear()).whereEqualTo("tipo", Utils.DESPESA)
                .whereEqualTo("uid", parseUser.getObjectId());
        query.orderByDescending("updatedAt").findInBackground((objects, e) -> {
            if (e == null) {
                for (ParseObject data : objects) {
                    Despesa despesa = new Despesa();
                    Categorias categoria = new Categorias();
                    categoria.setName((String) data.get("categoria"));
                    despesa.setCategoria(categoria);
                    despesa.setSituacao((String) data.get("situacao"));
                    despesa.setData((String.valueOf(Utils.currentDateAndMonth(data.getCreatedAt()))));
                    despesa.setDesricao((String) data.get("descricao"));
                    despesa.setTipo((String) data.get("tipo"));
                    despesa.setValor(Double.parseDouble(String.valueOf(data.get("valor"))));
                    this.despesaList.add(despesa);
                }
                ioListener.onDataRetrievedDespesas(despesaList);
            } else {
                ioListener.onDataRetrieveFail(e);
            }
        });
    }

    public void situacaoMensalCorrente(@NonNull final ParseListenerEquilibrio ioListener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                .whereEqualTo("mes", Utils.currentMonth())
                .whereEqualTo("ano", Utils.currentYear())
                .whereEqualTo("uid", parseUser.getObjectId());
        query.findInBackground((objects, e) -> {
            if (e == null) {
                double moneyDespesa = 0;
                double moneyReceita = 0;
                for (ParseObject data : objects) {
                    if (Objects.equals(data.get("tipo"), "despesa")) {
                        moneyDespesa += Double.parseDouble(String.valueOf(data.get("valor")));
                    } else if (Objects.equals(data.get("tipo"), "receita")) {
                        moneyReceita += Double.parseDouble(String.valueOf(data.get("valor")));
                    }
                }
                ioListener.onDataRetrievedMoney(moneyReceita, moneyDespesa, moneyReceita - moneyDespesa);
            } else {
                ioListener.onDataRetrieveFail(e);
            }
        });
    }

    public void balanceTotal(int year, @NonNull final ParseBalances ioListener) {
        String[] months = {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho",
                "agosto", "setembro", "outubro", "novembro", "dezembro"};
        List<Balanco> balanceList = new ArrayList<>();
        for (String mes : months) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                    .whereEqualTo("mes", mes)
                    .whereEqualTo("ano", year)
                    .whereEqualTo("uid", parseUser.getObjectId());
            query.findInBackground((objects, e) -> {
                if (e == null) {
                    double valorDespesa = 0;
                    double valorReceita = 0;
                    for (ParseObject data : objects) {
                        if (Objects.equals(data.get("tipo"), "receita")) {
                            valorReceita += Double.parseDouble(String.valueOf(data.get("valor")));
                        } else if (Objects.equals(data.get("tipo"), "despesa")) {
                            valorDespesa += Double.parseDouble(String.valueOf(data.get("valor")));
                        }
                    }
                    if (valorDespesa > 0) {
                        Balanco balanco = new Balanco(valorReceita, valorDespesa, mes);
                        balanceList.add(balanco);
                    }
                    ioListener.readBalances(balanceList);
                }
            });
        }
    }

    public void myCash(ParseListenerMoney money) {
        String[] months = {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho",
                "agosto", "setembro", "outubro", "novembro", "dezembro"};
        List<Double> mCash = new ArrayList<>();
        for (String mes : months) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH)
                    .whereEqualTo("mes", mes)
                    .whereEqualTo("uid", parseUser.getObjectId());
            query.findInBackground((objects, e) -> {
                if (e == null) {
                    double valorDespesa = 0;
                    double valorReceita = 0;

                    for (ParseObject data : objects) {
                        if (Objects.equals(data.get("tipo"), "receita")) {
                            valorReceita += Double.parseDouble(String.valueOf(data.get("valor")));
                        } else if (Objects.equals(data.get("tipo"), "despesa")) {
                            valorDespesa += Double.parseDouble(String.valueOf(data.get("valor")));
                        }
                    }
                    mCash.add(valorReceita - valorDespesa);
                }
                money.moneyOnWallet(mCash);
            });
        }
    }

    //Delete
    public void deleteItem(String itemId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Utils.DATAOFMONTH);
        query.getInBackground(itemId, (item, e) -> item.deleteEventually(e1 -> {
            alert("Registo excluído", "");
        }));
    }

    private void alert(String firstMsg, String msg) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(firstMsg + " " + msg)
                .setConfirmText("OK")
                .setConfirmClickListener(v -> context.startActivity(new Intent(context, MainActivity_.class)))
                .show();

    }
}