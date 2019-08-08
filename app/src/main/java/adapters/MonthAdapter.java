package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.account.R;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.List;

import models.Despesa;
import models.Receita;
import utils.Utils;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthDataViewHolder> {

    private int selected_position = -1;
    private List<Despesa> despesaList;
    private List<Receita> receitaList;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public MonthAdapter(Context context, List<Despesa> despesas, List<Receita> receitas) {
        this.despesaList = despesas;
        this.receitaList = receitas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void OnItemLongClickListener(OnItemLongClickListener mListener) {
        mLongListener = mListener;
    }

    @NonNull
    @Override
    public MonthDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.lancamentos_list, viewGroup, false);
        return new MonthDataViewHolder(view, mListener, mLongListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthDataViewHolder holder, int position) {
        if (despesaList != null && despesaList.size() > 0) {
            Despesa despesa = despesaList.get(position);
            holder.tv_categoria_lancamento.setText(despesa.getCategoria().getName());
            holder.mli_lancamento.setLetter(despesa.getCategoria().getName());//set color red on despesas. Symbol of danger
            holder.mT_money_lancamento.setTextColor(context.getResources().getColor(R.color.default_red));

            holder.mT_money_lancamento.setText((Utils.convertMoney(despesa.getValor())));
            holder.tv_data_lancamento.setText(despesa.getData());
            holder.tv_pago_recebido.setText(despesa.getSituacao());
            holder.mli_lancamento.setShapeColor(context.getResources().getColor(R.color.md_red_500));
            holder.lancamento_id.setText(despesa.getId());
        } else if (receitaList != null && receitaList.size() > 0) {
            Receita receita = receitaList.get(position);
            holder.tv_categoria_lancamento.setText(receita.getCategoria().getName());
            holder.mli_lancamento.setLetter(receita.getCategoria().getName());
            //set color red on receitas. Symbol of success
            holder.mT_money_lancamento.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.mT_money_lancamento.setText((Utils.convertMoney(receita.getValor())));
            holder.tv_data_lancamento.setText(receita.getData());
            holder.tv_pago_recebido.setText(receita.getSituacao());
            holder.lancamento_id.setText(receita.getId());
        }
    }

    @Override
    public int getItemCount() {
        if (despesaList != null) {
            return despesaList.size();
        } else {
            return receitaList.size();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(int position);
    }

    class MonthDataViewHolder extends RecyclerView.ViewHolder {
        private MaterialLetterIcon mli_lancamento;
        private TextView tv_data_lancamento, tv_categoria_lancamento, tv_pago_recebido, lancamento_id;
        private TextView mT_money_lancamento;

        MonthDataViewHolder(@NonNull View itemView, @Nullable final OnItemClickListener listener, @Nullable final OnItemLongClickListener longClickListener) {
            super(itemView);
            mli_lancamento = itemView.findViewById(R.id.mli_lancamento);
            tv_data_lancamento = itemView.findViewById(R.id.tv_data_lancamento);
            tv_categoria_lancamento = itemView.findViewById(R.id.tv_categoria_lancamento);
            tv_pago_recebido = itemView.findViewById(R.id.tv_pago_recebido);
            mT_money_lancamento = itemView.findViewById(R.id.mT_money_lancamento);
            lancamento_id = itemView.findViewById(R.id.lancamento_id);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // this check, is to prevent errors when clicking a card that has no position bound to it (for example, a card that we deleted seconds ago)
                        listener.onItemClick(position);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // this check, is to prevent errors when clicking a card that has no position bound to it (for example, a card that we deleted seconds ago)
                        longClickListener.onItemLongClicked(position);
                    }
                }
                return false;
            });
        }
    }
}
