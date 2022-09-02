package it.unisa.walletmanagement.Control.GestioneConti.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.R;

public class CreaContoDialog extends DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etNome, etSaldo;

    public interface ContoListener{
        void sendConto(Conto conto);
    }

    private ContoListener contoListener;

    public CreaContoDialog() {
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
        View view = inflater.inflate(R.layout.fragment_crea_conto, container, false);

        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOK = view.findViewById(R.id.tv_ok);
        etNome = view.findViewById(R.id.edit_text_nome_conto);
        etSaldo = view.findViewById(R.id.edit_text_saldo_conto);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // crea il conto o restituisci l'input all'activity
                if(CheckAllFields()){
                    Conto c = new Conto();
                    c.setNome(etNome.getText().toString());
                    c.setSaldo(Float.parseFloat(etSaldo.getText().toString()));
                    c.setMovimenti(null);
                    contoListener.sendConto(c);
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

        if(!Pattern.compile("[A-zÀ-ù0-9 -,]{3,30}").matcher(etNome.getText().toString()).matches()){
            if (etNome.getText().toString().length() == 0) {
                etNome.setError("Questo campo è richiesto");
                return false;
            } else if(etNome.getText().toString().length() > 30){
                etNome.setError("Questo campo non deve superare i 30 caratteri");
                return false;
            }
        }

        if(!Pattern.compile("[0-9]{1,9}[.]{0,1}[0-9]{0,2}").matcher(etSaldo.getText().toString()).matches()) {
            float saldo;
            try {
                saldo = Float.parseFloat(etSaldo.getText().toString());
            }catch (Exception e){
                etSaldo.setError("Utilizza il formato (123.45)");
                return false;
            }
            if (saldo == 0) {
                etSaldo.setError("Questo campo è richiesto");
                return false;
            } else if (saldo < 0) {
                etSaldo.setError("Il saldo deve essere positivo");
                return false;
            } else {
                etSaldo.setError("Utilizza il formato (123.45)");
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
            contoListener = (ContoListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}