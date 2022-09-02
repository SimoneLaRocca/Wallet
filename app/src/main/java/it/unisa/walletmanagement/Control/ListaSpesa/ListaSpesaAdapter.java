package it.unisa.walletmanagement.Control.ListaSpesa;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import it.unisa.walletmanagement.Model.Dao.ListaSpesaDAO;
import it.unisa.walletmanagement.R;

public class ListaSpesaAdapter extends ArrayAdapter<String> {

    private TextView tvVoce;
    private CheckBox cbCancella;
    private Context context;

    public ListaSpesaAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_voce_element, parent, false);
        }

        String voce = getItem(position);

        tvVoce = convertView.findViewById(R.id.text_view_voce);
        cbCancella = convertView.findViewById(R.id.checkbox_cancella);

        cbCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    String voce = getItem(position);
                    ListaSpesaDAO listaSpesaDAO = new ListaSpesaDAO(getContext());
                    listaSpesaDAO.deleteVoce(voce);
                    LinearLayout layout = (LinearLayout) view.getParent();
                    TextView tvVoce = layout.findViewById(R.id.text_view_voce);
                    tvVoce.setPaintFlags(tvVoce.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    view.setVisibility(View.INVISIBLE);
                }
            }
        });

        tvVoce.setText(voce);

        return convertView;
    }
}
