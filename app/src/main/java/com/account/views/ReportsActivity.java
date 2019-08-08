package com.account.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.BalancoAdapter;
import controllers.MonthController;
import controllers.ParseBalances;
import models.Balanco;
import utils.Utils;

public class ReportsActivity extends AppCompatActivity {
    List<Balanco> balancoList;
    BalancoAdapter balancoAdapter;

    private TextView anoCorrente;
    private ProgressDialog dialog;
    private LinearLayout layout_empty;
    private boolean backPressToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Context context = this;

        RecyclerView rv_balanco = findViewById(R.id.rv_balanco);
        //Components
        TextView menosUmAno = findViewById(R.id.menos_um_ano);
        TextView maisUmAno = findViewById(R.id.mais_um_ano);
        anoCorrente = findViewById(R.id.ano_corrente);
        MaterialToolbar materialToolbar = findViewById(R.id.mToolbar_balanco);
        layout_empty = findViewById(R.id.empty_layout);

        rv_balanco.setHasFixedSize(true);
        rv_balanco.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        balancoList = new ArrayList<>();
        balancoList.clear();

        anoCorrente.setText(String.valueOf(Utils.currentYear()));

        dialog = new ProgressDialog(ReportsActivity.this);
        dialog.setMessage("A carregar os registos");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();

        MonthController monthController = new MonthController(context);
        monthController.balanceTotal(Integer.parseInt(String.valueOf(anoCorrente.getText())), new ParseBalances() {
            @Override
            public void readBalances(List<Balanco> balanceList) {
                if (balanceList.size() > 0) {
                    rv_balanco.setVisibility(View.VISIBLE);
                    layout_empty.setVisibility(View.GONE);
                    balancoAdapter = new BalancoAdapter(context, balanceList);
                    balancoAdapter.notifyDataSetChanged();
                    rv_balanco.setAdapter(balancoAdapter);
                } else {
                    rv_balanco.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void error(ParseException e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        maisUmAno.setOnClickListener(v -> {
            anoCorrente.setText(String.valueOf(Integer.parseInt(String.valueOf(anoCorrente.getText())) + 1));

            dialog = new ProgressDialog(ReportsActivity.this);
            dialog.setMessage("A carregar os registos");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();

            monthController.balanceTotal(Integer.parseInt(String.valueOf(anoCorrente.getText())), new ParseBalances() {
                @Override
                public void readBalances(List<Balanco> balancos) {

                    if (balancos.size() > 0) {
                        rv_balanco.setVisibility(View.VISIBLE);
                        layout_empty.setVisibility(View.GONE);
                        balancoAdapter = new BalancoAdapter(context, balancos);
                        balancoAdapter.notifyDataSetChanged();
                        rv_balanco.setAdapter(balancoAdapter);
                    } else {
                        rv_balanco.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.VISIBLE);
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void error(ParseException e) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        });
        menosUmAno.setOnClickListener(v -> {
            anoCorrente.setText(String.valueOf(Integer.parseInt(String.valueOf(anoCorrente.getText())) - 1));

            dialog = new ProgressDialog(ReportsActivity.this);
            dialog.setMessage("A carregar os registos");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();

            monthController.balanceTotal(Integer.parseInt(String.valueOf(anoCorrente.getText())), new ParseBalances() {
                @Override
                public void readBalances(List<Balanco> balancos) {

                    if (balancos.size() > 0) {
                        rv_balanco.setVisibility(View.VISIBLE);
                        layout_empty.setVisibility(View.GONE);
                        balancoAdapter = new BalancoAdapter(context, balancos);
                        balancoAdapter.notifyDataSetChanged();
                        rv_balanco.setAdapter(balancoAdapter);
                    } else {
                        rv_balanco.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.VISIBLE);
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void error(ParseException e) {

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        });

        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        materialToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MainActivity_.class)));
    }

    @Override
    public void onBackPressed() {
        if (backPressToExit) {
            Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
            homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
            homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeScreenIntent);
        }
        this.backPressToExit = true;
        Snackbar.make(findViewById(R.id.report), getString(R.string.sair_snackbar), Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> backPressToExit = false, 1000);
    }
}