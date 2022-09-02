package it.unisa.walletmanagement.Control.ListaSpesa;

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

public class AggiungiVoceDialog extends DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etVoce;

    public interface ListaSpesaListener{
        void sendVoce(String voce);
    }

    private ListaSpesaListener listaSpesaListener;

    public AggiungiVoceDialog() {
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
        View view = inflater.inflate(R.layout.fragment_aggiungi_voce_dialog, container, false);

        etVoce = view.findViewById(R.id.edit_text_voce);
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
                    String voce = etVoce.getText().toString();
                    listaSpesaListener.sendVoce(voce);
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

        if(!Pattern.compile("[A-zÀ-ù0-9 -,]{3,30}").matcher(etVoce.getText().toString()).matches()) {
            if (etVoce.getText().toString().length() == 0) {
                etVoce.setError("Questo campo è richiesto");
                return false;
            }else if (etVoce.getText().toString().length() > 30){
                etVoce.setError("Questo campo non deve superare i 30 caratteri");
                return false;
            } else if(etVoce.getText().toString().contains("\n")){
                etVoce.setError("Il carattere 'a capo' non è consentito");
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
            listaSpesaListener = (ListaSpesaListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}