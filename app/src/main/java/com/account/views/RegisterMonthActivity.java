package com.account.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.umut.soysal.lib.CurrencyEditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import adapters.CategoriesAdapter;
import controllers.CategoriesController;
import controllers.MonthController;
import controllers.ParseListenerCategorias;
import models.Categorias;
import models.Despesa;
import models.MonthData;
import models.Receita;
import utils.Utils;

@EActivity(R.layout.activity_register_month)
public class RegisterMonthActivity extends AppCompatActivity {

    //View Childs
    @ViewById(R.id.idCAT)
    TextView idCAT;
    @ViewById(R.id.progress)
    ProgressBar progressBar;
    @ViewById(R.id.currencyEn)
    CurrencyEditText money;
    @ViewById(R.id.ll_month)
    LinearLayout layout;
    @ViewById(R.id.mTi_date)
    TextInputLayout mTi_date;
    @ViewById(R.id.btn_categories)
    MaterialButton btn_categories;
    @ViewById(R.id.btn_save_details)
    MaterialButton btn_save;
    @ViewById(R.id.btn_update_details)
    MaterialButton btn_update;
    @ViewById(R.id.mTi_description)
    TextInputLayout mTi_description;
    @ViewById(R.id.scrollMonth)
    ScrollView viewMonth;
    //Vars
    private List<Categorias> categorias;
    private Context context;
    private int value;
    private Despesa despesa;
    private String id;

    //After views
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @AfterViews
    void init() {
        //get Activity Context
        context = this;
        //init vars
        categorias = new ArrayList<>();
        CategoriesController categoriesController = new CategoriesController();
        money.setLocale(new Locale("pt", "MOZ"));

        //Get intent
        if (getIntent().getStringExtra("itemId") != null) {

            id = getIntent().getStringExtra("itemId");
            String categoria = getIntent().getStringExtra("categoria");
            String valor = getIntent().getStringExtra("dinheiro");
            String description = getIntent().getStringExtra("descricao");

            //Seting values comes to intent
            Objects.requireNonNull(mTi_description.getEditText()).setText(description);
            btn_categories.setText(categoria);
            money.setText(valor);
            btn_save.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);

        } else {
            progressBar.setVisibility(View.VISIBLE);
            viewMonth.setVisibility(View.GONE);
        }
        despesa = new Despesa();

        btn_categories.setAllCaps(false);
        value = getIntent().getIntExtra("situacao", 0);
        mTi_date.setEnabled(false);
        Objects.requireNonNull(mTi_date.getEditText()).setText(Utils.totalDate());

        if (value == +1 || Objects.equals(getIntent().getStringExtra("tag"), "recebido")) {
            layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            btn_save.setText(getResources().getString(R.string.registar_receita));
            btn_update.setText(getResources().getString(R.string.alterar_receita));

        } else if (value == -1 || Objects.equals(getIntent().getStringExtra("tag"), "pago")) {
            layout.setBackgroundColor(getResources().getColor(R.color.default_red));
            btn_save.setText(getResources().getString(R.string.registar_despesa));
            btn_save.setBackgroundColor(getResources().getColor(R.color.md_red_400));
            btn_update.setText(getResources().getString(R.string.alterar_despesa));
            btn_update.setBackgroundColor(getResources().getColor(R.color.md_red_400));
        }

        if (value == -1 || Objects.equals(getIntent().getStringExtra("tag"), "pago")) {
            categorias.clear();
            categorias = categoriesController.onCompleteGetData(Utils.CATEGORIA_DESPESA, new ParseListenerCategorias() {
                @Override
                public void onDataRetrieved(ArrayList listOfData) {
                    progressBar.setVisibility(View.GONE);
                    viewMonth.setVisibility(View.VISIBLE);
                }

                @Override
                public void onDataRetrieveFail(ParseException e) {
                    onBackPressed();
                }
            });
        } else if (value == +1 || getIntent().getStringExtra("tag").equals("recebido")) {
            categorias.clear();
            categorias = categoriesController.onCompleteGetData(Utils.CATEGORIA_RECEITA, new ParseListenerCategorias() {
                @Override
                public void onDataRetrieved(ArrayList listOfData) {
                    progressBar.setVisibility(View.GONE);
                    viewMonth.setVisibility(View.VISIBLE);
                }

                @Override
                public void onDataRetrieveFail(ParseException e) {
                    onBackPressed();
                }
            });
        }
    }

    //update month details
    @Click(R.id.btn_update_details)
    void update() {
        if (isvalidDesc() && isvalidCategory() && isValidMoney()) {
            MonthData monthData = new MonthData();
            Categorias categoria = new Categorias();

            categoria.setName(String.valueOf(btn_categories.getText()));
            monthData.setValor(Utils.isNum(String.valueOf(money.getText())));
            monthData.setDesricao(String.valueOf(Objects.requireNonNull(mTi_description.getEditText()).getText()));
            monthData.setCategoria(categoria);

            MonthController controller = new MonthController(context, monthData);
            controller.update(id);
        }
    }

    //save  month details
    @Click(R.id.btn_save_details)
    void onSave() {

        if (isvalidDesc() && isvalidCategory() && isValidMoney()) {
            Categorias categoria = new Categorias();
            categoria.setName(String.valueOf(btn_categories.getText()));

            if (value == -1 || Objects.equals(getIntent().getStringExtra("tag"), "recebido")) {
                despesa = new Despesa();
                despesa.setValor(Utils.isNum(String.valueOf(money.getText())));
                despesa.setDesricao(String.valueOf(Objects.requireNonNull(mTi_description.getEditText()).getText()));
                despesa.setCategoria(categoria);
                despesa.setSituacao("pago");
                despesa.setTipo("despesa");
                despesa.getDataAgora();
                MonthController controller = new MonthController(context, despesa);
                controller.saveDespesa();

            } else if (value == +1 || Objects.equals(getIntent().getStringExtra("tag"), "pago")) {
                Receita receita = new Receita();
                receita.setValor(Utils.isNum(String.valueOf(money.getText())));
                receita.setDesricao(String.valueOf(Objects.requireNonNull(mTi_description.getEditText()).getText()));
                receita.setCategoria(categoria);
                receita.getDataAgora();
                receita.setTipo("receita");
                receita.setSituacao("recebido");
                MonthController controller = new MonthController(context, receita);
                controller.saveReceita();
            }
        }
    }

    @Click(R.id.btn_categories)
    void setupCategories() {
        addDailog();
    }

    //Like dialog fragment
    protected void addDailog() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_categories, null);
        RecyclerView listItens = view.findViewById(R.id.rv_categorias);

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(context, categorias);
        categoriesAdapter.notifyDataSetChanged();
        listItens.setHasFixedSize(true);
        listItens.setLayoutManager(new LinearLayoutManager(context));
        listItens.setAdapter(categoriesAdapter);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(getResources().getString(R.string.categoria))
                .setView(view)
                .setPositiveButton(getResources().getString(R.string.cancelar), (dialog1, which) -> dialog1.dismiss())
                .create();
        dialog.show();

        categoriesAdapter.setOnClickListener(position -> {
            dialog.dismiss();
            btn_categories.setText(categorias.get(position).getName());
            idCAT.setText(categorias.get(position).getCategoriaId());
        });
    }

    private boolean isvalidCategory() {
        TextInputLayout layout = findViewById(R.id.TextInputCategories);

        if (String.valueOf(btn_categories.getText()).isEmpty()) {
            layout.setError("Seleccione a categoria");
            layout.requestFocus();
            return false;

        } else {
            layout.setFocusable(false);
            layout.setErrorEnabled(false);
            despesa.setDesricao(String.valueOf((btn_categories.getText())));
            return true;
        }
    }

    private boolean isvalidDesc() {
        if (String.valueOf(Objects.requireNonNull(mTi_description.getEditText()).getText()).isEmpty()) {
            mTi_description.setError("Forneça uma descrição");
            mTi_description.requestFocus();
            return false;
        } else {
            mTi_description.setErrorEnabled(false);
            mTi_description.setFocusable(false);
            despesa.setDesricao(String.valueOf(mTi_description.getEditText().getText()));
            return true;
        }
    }

    private boolean isValidMoney() {
        if (money.getText().toString().isEmpty()) {
            money.setError("Forneça  o valor gasto");
            money.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}