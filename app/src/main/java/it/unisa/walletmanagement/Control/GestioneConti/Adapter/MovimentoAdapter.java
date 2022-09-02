package it.unisa.walletmanagement.Control.GestioneConti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;

import it.unisa.walletmanagement.Control.GestioneConti.Activity.ContoActivity;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.R;

// Fragment usato nell'Activity ContoActivity per modificare un
// movimento della lista dei movimenti di uno specifico conto
public class MovimentoAdapter extends ArrayAdapter<Movimento> {

    public interface MovimentoListener {
        void deleteMovimento(Movimento movimento);
    }

    private Context context;
    private MovimentoListener movimentoListener;

    public MovimentoAdapter(@NonNull Context context, int resource, @NonNull List<Movimento> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public MovimentoAdapter(@NonNull Context context, int resource, @NonNull List<Movimento> objects, MovimentoListener movimentoListener) {
        super(context, resource, objects);
        this.context = context;
        this.movimentoListener = movimentoListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_movimento_element, parent, false);
        }
        Movimento movimento = getItem(position);

        FrameLayout flColor = convertView.findViewById(R.id.frame_layout_color);
        TextView tvNome = convertView.findViewById(R.id.text_view_nome_movimento);
        TextView tvCategoria = convertView.findViewById(R.id.text_view_categoria_movimento);
        TextView tvImporto = convertView.findViewById(R.id.text_view_importo_movimento);
        TextView tvData = convertView.findViewById(R.id.text_view_data_movimento);
        ImageView ivCancella = convertView.findViewById(R.id.image_view_cancella_movimento);
        ivCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movimento m = getItem(position);
                MovimentoDAO movimentoDAO = new MovimentoDAO(context);
                movimentoDAO.deleteMovimento(m.getId());
                MovimentoAdapter.this.remove(getItem(position));
                MovimentoAdapter.this.notifyDataSetChanged();
                if(context.getClass().equals(ContoActivity.class)){
                    movimentoListener.deleteMovimento(m);
                }
            }
        });

        if(movimento.getTipo() == 0){
            flColor.setBackgroundColor(0xFFFF0000);
        }else {
            flColor.setBackgroundColor(0xFF4CAF50);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        tvNome.setText(movimento.getNome());
        tvCategoria.setText(movimento.getCategoria());
        tvImporto.setText("€ "+Float.toString(movimento.getImporto()));
        tvData.setText(simpleDateFormat.format(movimento.getData().getTime()));

        return convertView;
    }
}
