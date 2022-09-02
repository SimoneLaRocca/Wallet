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

import java.util.regex.Pattern;

import it.unisa.walletmanagement.Control.Impostazioni.SecurityManager;
import it.unisa.walletmanagement.R;

public class CambiaPasswordDialog extends DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etOldPassword, etNewFirstPassword, etNewSecondPassword;

    public interface PasswordListener{
        void sendNewPassword(String password);
    }

    private PasswordListener passwordListener;

    public CambiaPasswordDialog() {
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
        View view = inflater.inflate(R.layout.fragment_cambia_password_dialog, container, false);

        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOK = view.findViewById(R.id.tv_ok);
        etNewFirstPassword = view.findViewById(R.id.edit_text_new_first_password);
        etNewSecondPassword = view.findViewById(R.id.edit_text_new_second_password);
        etOldPassword = view.findViewById(R.id.edit_text_old_password);

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
                    passwordListener.sendNewPassword(etNewFirstPassword.getText().toString());
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
        if (etOldPassword.getText().toString().length() == 0) {
            etOldPassword.setError("Questo campo è richiesto");
            return false;
        } else if(!securityManager.checkLogin(etOldPassword.getText().toString())){
            etOldPassword.setError("Password errata");
            return false;
        }

        if(Pattern.compile("[\\w]{4,30}").matcher(etNewFirstPassword.getText().toString()).matches()) {
            if (etNewFirstPassword.getText().toString().length() == 0) {
                etNewFirstPassword.setError("Questo campo è richiesto");
                return false;
            } else if (etNewFirstPassword.getText().toString().length() < 4) {
                etNewFirstPassword.setError("Almeno 4 caratteri/cifre");
                return false;
            }
        }

        if (etNewSecondPassword.getText().toString().length() == 0) {
            etNewSecondPassword.setError("Questo campo è richiesto");
            return false;
        } else if(!etNewFirstPassword.getText().toString().equals(etNewSecondPassword.getText().toString())){
            etNewSecondPassword.setError("I campi non sono uguali");
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