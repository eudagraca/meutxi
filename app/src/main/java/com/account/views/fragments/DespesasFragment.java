package com.account.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.account.R;
import com.account.views.RegisterMonthActivity_;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import adapters.MonthAdapter;
import controllers.MonthAsyncDespesa;
import controllers.MonthController;
import controllers.ParseListenerDespesa;
import models.Despesa;
import utils.Utils;

public class DespesasFragment extends Fragment {

    public String datames;
    private Context context;
    private BottomSheetBehavior bottomSheetBehavior;
    private MonthAdapter monthAdapter;
    private MonthController monthController;

    public DespesasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_despesas, container, false);
        context = getContext();

        MaterialCardView bottomSheet = view.findViewById(R.id.bottomSheetLayout);
        RecyclerView rv_lancamentos = view.findViewById(R.id.rv_lancamentos);

        //BottomSheet
        TextView description = view.findViewById(R.id.desctxt);
        TextView deleteItem = view.findViewById(R.id.delete_item);
        TextView editItem = view.findViewById(R.id.edit_item);
        TextView tag = view.findViewById(R.id.tag);
        TextView itemId = view.findViewById(R.id.idItem);
        TextView categoriaItem = view.findViewById(R.id.lancamento_sheet_categoria);
        TextView moneyItem = view.findViewById(R.id.lancamento_sheet_money);
        TextView dataItem = view.findViewById(R.id.lancamento_sheet_data);
        MaterialLetterIcon letterIconItem = view.findViewById(R.id.mliItem);
        LinearLayout emptyLancamenntos = view.findViewById(R.id.empty_lancamentos);
        ScrollView scrollView = view.findViewById(R.id.sroll_receitas);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        monthController = new MonthController(context);

        rv_lancamentos.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        rv_lancamentos.setHasFixedSize(true);

        List<Despesa> despesaList = new ArrayList<>();
        monthAdapter = new MonthAdapter(context, despesaList, null);
        MonthAsyncDespesa asyncDespesa = new MonthAsyncDespesa(context);


        asyncDespesa.despesas(new ParseListenerDespesa() {
            @Override
            public void onDataRetrievedDespesas(List<Despesa> despesas) {

                if (despesas.size() == 0) {
                    scrollView.setVisibility(View.GONE);
                    emptyLancamenntos.setVisibility(View.VISIBLE);
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    emptyLancamenntos.setVisibility(View.GONE);
                    monthAdapter = new MonthAdapter(context, despesas, null);
                    monthAdapter.notifyDataSetChanged();
                    rv_lancamentos.setAdapter(monthAdapter);

                    monthAdapter.setOnClickListener(position -> {
                        bottomSheetBehavior.setPeekHeight(500);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        description.setText(despesas.get(position).getDesricao());
                        dataItem.setText(despesas.get(position).getData());
                        letterIconItem.setLetter(despesas.get(position).getCategoria().getName());
                        categoriaItem.setText(despesas.get(position).getCategoria().getName());
                        moneyItem.setText(Utils.convertMoney(despesas.get(position).getValor()));
                        itemId.setText(String.valueOf(despesas.get(position).getId()));
                        tag.setText(despesas.get(position).getSituacao());
                    });

                    monthAdapter.OnItemLongClickListener(position -> new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Deseja excluir este registo?")
                            .setConfirmText("apagar")
                            .setCancelText("cancelar")
                            .setConfirmClickListener(sweetAlertDialog -> monthController.deleteItem(String.valueOf(despesas.get(position).getId())))
                            .show());

                    deleteItem.setOnClickListener(v -> monthController.deleteItem(String.valueOf(itemId.getText())));
                }
            }

            @Override
            public void onDataRetrieveFail(ParseException e) {

            }
        });
        deleteItem.setOnClickListener(v -> monthController.deleteItem(String.valueOf(itemId.getText())));

        editItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, RegisterMonthActivity_.class);
            intent.putExtra("itemId", String.valueOf(itemId.getText()));
            intent.putExtra("categoria", String.valueOf(categoriaItem.getText()));
            intent.putExtra("data", String.valueOf(dataItem.getText()));
            intent.putExtra("dinheiro", moneyItem.getText().toString());
            intent.putExtra("descricao", String.valueOf(description.getText()));
            intent.putExtra("tag", String.valueOf(tag.getText()));
            startActivity(intent);
        });
//
//        Toast.makeText(context, String.valueOf(bottomSheetBehavior.isHideable()), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, String.valueOf(rv_lancamentos.hasOnClickListeners()), Toast.LENGTH_SHORT).show();

        return view;
    }
}
