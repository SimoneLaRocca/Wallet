package it.unisa.walletmanagement.Control.GestioneConti.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.regex.Pattern;

import it.unisa.walletmanagement.R;

public class CreaCategoriaDialog extends DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etNome;

    public interface CategoriaListener{
        void sendCategoria(String categoria);
    }

    private CategoriaListener categoriaListener;

    public CreaCategoriaDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crea_categoria, container, false);

        etNome = view.findViewById(R.id.edit_text_nome_categoria);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOK = view.findViewById(R.id.tv_ok);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckAllFields()){
                    String categoria = etNome.getText().toString();
                    categoriaListener.sendCategoria(categoria);
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    /**
     * Verifica la correttezza di tutti i campi del fragment dialog.
     * Se un campo non rispetta tutti i requisiti viene lanciato un errore.
     * @return un valore booleano per segnalare se tutti i campi sono corretti
     */
    private boolean CheckAllFields() {

        if(!Pattern.compile("[A-zÀ-ù0-9 -,]{3,30}").matcher(etNome.getText().toString()).matches()) {
            if (etNome.getText().toString().length() == 0) {
                etNome.setError("Questo campo è richiesto");
                return false;
            }else if (etNome.getText().toString().length() > 30){
                etNome.setError("Questo campo non deve superare i 30 caratteri");
                return false;
            } else if(etNome.getText().toString().contains("\n")){
                etNome.setError("Il carattere 'a capo' non è consentito");
                return false;
            }
        }
        // after all validation return true.
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            categoriaListener = (CategoriaListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}