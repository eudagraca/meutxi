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

import models.Categorias;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<Categorias> categoriasList;
    private LayoutInflater inflater;

    private OnItemClickListener mListener;

    public CategoriesAdapter(Context context, List<Categorias> categoriasList) {
        this.categoriasList = categoriasList;

        inflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = inflater.inflate(R.layout.categories_list, viewGroup, false);

        return new CategoriesViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoriesViewHolder holder, int position) {
        if (categoriasList.size() > 0) {
            Categorias categorias = categoriasList.get(position);
            holder.nameOfCategory.setText(categorias.getName());
            String firstSub = categorias.getName().substring(0, 1);
            String secondSub = categorias.getName().substring(2, 3);
            holder.letterIcon.setLetter(firstSub + secondSub);
            holder.categoryID.setText(categorias.getCategoriaId());
        }
    }

    @Override
    public int getItemCount() {

        return categoriasList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder {
        private MaterialLetterIcon letterIcon;
        private TextView nameOfCategory, categoryID;

        CategoriesViewHolder(@NonNull View itemView, @Nullable final OnItemClickListener listener) {
            super(itemView);

            letterIcon = itemView.findViewById(R.id.mli);
            nameOfCategory = itemView.findViewById(R.id.tv_categories);
            categoryID = itemView.findViewById(R.id.catID);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // this check, is to prevent errors when clicking a card that has no position bound to it (for example, a card that we deleted seconds ago)
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }
}
