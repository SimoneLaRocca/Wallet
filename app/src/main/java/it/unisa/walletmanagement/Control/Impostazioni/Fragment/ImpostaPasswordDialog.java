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

import it.unisa.walletmanagement.R;

public class ImpostaPasswordDialog extends DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etFirstPassword, etSecondPassword;

    public interface PasswordListener{
        void sendPassword(String password);
    }

    private PasswordListener passwordListener;

    public ImpostaPasswordDialog() {
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
        View view = inflater.inflate(R.layout.fragment_imposta_password_dialog, container, false);

        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOK = view.findViewById(R.id.tv_ok);
        etFirstPassword = view.findViewById(R.id.edit_text_first_password);
        etSecondPassword = view.findViewById(R.id.edit_text_second_password);

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
                    passwordListener.sendPassword(etFirstPassword.getText().toString());
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

        if(!Pattern.compile("[\\w]{4,30}").matcher(etFirstPassword.getText().toString()).matches()) {
            if (etFirstPassword.getText().toString().length() == 0) {
                etFirstPassword.setError("Questo campo è richiesto");
                return false;
            } else if (etFirstPassword.getText().toString().length() < 4) {
                etFirstPassword.setError("Almeno 4 caratteri/cifre");
                return false;
            }
        }

        if (etSecondPassword.getText().toString().length() == 0) {
            etSecondPassword.setError("Questo campo è richiesto");
            return false;
        } else if(!etFirstPassword.getText().toString().equals(etSecondPassword.getText().toString())){
            etSecondPassword.setError("I campi non sono uguali");
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