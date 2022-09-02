package it.unisa.walletmanagement.Control.GestioneConti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.R;

public class CategorieAdapter extends ArrayAdapter<String> {

    private Context context;

    public CategorieAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_categoria_element, parent, false);
        }

        String categoria = getItem(position);

        TextView tvCategoria = convertView.findViewById(R.id.text_view_categoria);
        ImageView ivCancella = convertView.findViewById(R.id.image_view_cancella_categoria);
        ivCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoria = getItem((position));
                ListaCategorieDAO listaCategorieDAO = new ListaCategorieDAO(context);
                listaCategorieDAO.deleteCategoria(categoria);
                CategorieAdapter.this.remove(categoria);
                CategorieAdapter.this.notifyDataSetChanged();
            }
        });

        tvCategoria.setText(categoria);

        return convertView;
    }
}
