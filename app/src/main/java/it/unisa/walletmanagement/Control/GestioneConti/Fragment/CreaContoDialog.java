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

import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.R;

public class CreaContoDialog extends DialogFragment {

    TextView tvCancel, tvOK;
    EditText etNome, etSaldo;

    public interface ContoListener{
        void sendConto(Conto conto);
    }

    public ContoListener contoListener;

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
                Conto c = new Conto();
                c.setNome(etNome.getText().toString());
                c.setSaldo(Float.parseFloat(etSaldo.getText().toString()));
                c.setMovimenti(null);
                contoListener.sendConto(c);
                getDialog().dismiss();
            }
        });

        return view;
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