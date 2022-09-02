package it.unisa.walletmanagement.Control.Impostazioni.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import it.unisa.walletmanagement.Control.Impostazioni.SecurityManager;
import it.unisa.walletmanagement.R;

public class RimuoviPasswordDialog extends DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etPassword;

    public interface PasswordListener{
        void sendDeletedPassword(String password);
    }

    private PasswordListener passwordListener;

    public RimuoviPasswordDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rimuovi_password_dialog, container, false);

        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOK = view.findViewById(R.id.tv_ok);
        etPassword = view.findViewById(R.id.edit_text_password);

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
                    passwordListener.sendDeletedPassword(etPassword.getText().toString());
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
        SecurityManager securityManager = new SecurityManager(getActivity().getApplicationContext());
        if (etPassword.getText().toString().length() == 0) {
            etPassword.setError("Questo campo è richiesto");
            return false;
        } else if(!securityManager.checkLogin(etPassword.getText().toString())){
            etPassword.setError("Password errata");
            return false;
        }

        // after all validation return true.
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            passwordListener = (PasswordListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}