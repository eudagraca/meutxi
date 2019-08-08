package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.account.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import models.Balanco;

public class BalancoAdapter extends RecyclerView.Adapter<BalancoAdapter.BalancoViewHolder> {
    private LayoutInflater inflater;
    private List<Balanco> balancoList;


    public BalancoAdapter(Context context, List<Balanco> balancos) {
        inflater = LayoutInflater.from(context);
        this.balancoList = balancos;
    }

    @NonNull
    @Override
    public BalancoAdapter.BalancoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.balanco_mensal, parent, false);

        return new BalancoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalancoAdapter.BalancoViewHolder holder, int position) {

        if (balancoList.size() > 0) {
            Balanco balanco = balancoList.get(position);
            holder.despesa.setText(balanco.getDespesa() + "0 MZN");
            holder.receita.setText(balanco.getReceita() + "0 MZN");
            holder.balanco.setText(balanco.getBalanco() + "0 MZN");
            holder.mes.setText(StringUtils.capitalize(balanco.getMes()));
        }
    }

    @Override
    public int getItemCount() {
        return balancoList.size();
    }

    class BalancoViewHolder extends RecyclerView.ViewHolder {
        TextView despesa, receita, balanco, mes;

        BalancoViewHolder(@NonNull View itemView) {
            super(itemView);
            despesa = itemView.findViewById(R.id.valor_despesa);
            receita = itemView.findViewById(R.id.valor_receita);
            balanco = itemView.findViewById(R.id.valor_balanco);
            mes = itemView.findViewById(R.id.mes);
        }
    }
}
