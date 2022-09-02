package it.unisa.walletmanagement.Control.GestioneConti.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import java.util.regex.Pattern;

import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Entity.ListaCategorie;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.Model.Entity.MovimentoBuilder;
import it.unisa.walletmanagement.R;

public class ModificaMovimentoDialog extends androidx.fragment.app.DialogFragment {

    private TextView tvCancel, tvOK;
    private EditText etNome, etImporto;
    private Spinner dropdown;
    private ArrayAdapter<String> adapter;
    private ListaCategorie categorie;
    private Button entrata, uscita;
    private Movimento movimento;

    // interfaccia usata per inviare dati all'activity
    public interface ModificaMovimentoListener {
        void sendUpdatedMovimento(Movimento oldMovimento, Movimento newMovimento);
    }

    private ModificaMovimentoListener modificaMovimentoListener;

    public ModificaMovimentoDialog() {
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
        // Possibile riutilizzare il layout fragment_movimento, cambiando la text view iniziale
        View view = inflater.inflate(R.layout.fragment_modifica_movimento, container, false);

        movimento = (Movimento) this.getArguments().getSerializable("movimento");

        dropdown = view.findViewById(R.id.spinner1);

        ListaCategorieDAO listaCategorieDAO = new ListaCategorieDAO(getActivity().getApplicationContext());
        categorie = listaCategorieDAO.doRetrieveListaCategorie();
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, categorie.getCategorie());
        dropdown.setAdapter(adapter);

        etNome = view.findViewById(R.id.edit_text_nome_movimento);
        etImporto = view.findViewById(R.id.edit_text_importo_movimento);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOK = view.findViewById(R.id.tv_ok);
        entrata = view.findViewById(R.id.button_entrata_movimento);
        uscita = view.findViewById(R.id.button_uscita_movimento);

        // imposta i campi con i valori dell'item movimento selezionato
        etNome.setText(movimento.getNome());
        etImporto.setText("" + movimento.getImporto());
        // imposta categoria
        int spinner_position = adapter.getPosition(movimento.getCategoria());
        dropdown.setSelection(spinner_position);

        if(movimento.getTipo() == 0){
            Drawable buttonDrawable = uscita.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, 0xFFFF0000);
            uscita.setBackground(buttonDrawable);
            uscita.setTag(true);
            entrata.setTag(false);
        }else {
            Drawable buttonDrawable = entrata.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, 0xFF4CAF50);
            entrata.setBackground(buttonDrawable);
            entrata.setTag(true);
            uscita.setTag(false);
        }

        uscita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().equals(false)){
                    Drawable buttonDrawable = view.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, 0xFFFF0000);
                    view.setBackground(buttonDrawable);
                    view.setTag(true);

                    buttonDrawable = entrata.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, 0xFF8F8F8F);
                    entrata.setBackground(buttonDrawable);
                    entrata.setTag(false);
                }
            }
        });

        entrata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().equals(false)){
                    Drawable buttonDrawable = view.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, 0xFF4CAF50);
                    view.setBackground(buttonDrawable);
                    view.setTag(true);

                    buttonDrawable = uscita.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, 0xFF8F8F8F);
                    uscita.setBackground(buttonDrawable);
                    uscita.setTag(false);
                }
            }
        });

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
                    Movimento newMovimento = MovimentoBuilder.newBuilder(movimento.getId())
                            .nome(etNome.getText().toString())
                            .importo(Float.parseFloat(etImporto.getText().toString()))
                            .data(movimento.getData())
                            .categoria((String) dropdown.getSelectedItem())
                            .tipo(entrata.getTag().equals(true) ? 1 : 0)
                            .build();
                    modificaMovimentoListener.sendUpdatedMovimento(movimento, newMovimento);
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
            }else if(etNome.getText().toString().length() > 30){
                etNome.setError("Questo campo non deve superare i 30 caratteri");
                return false;
            }
        }

        if(!Pattern.compile("[0-9]{1,9}[.]{0,1}[0-9]{0,2}").matcher(etImporto.getText().toString()).matches()) {
            float importo;
            try {
                importo = Float.parseFloat(etImporto.getText().toString());
            }catch (Exception e){
                etImporto.setError("Utilizza il formato (123.45)");
                return false;
            }
            if (importo == 0) {
                etImporto.setError("Questo campo è richiesto");
                return false;
            } else if (importo < 0) {
                etImporto.setError("L'importo deve essere positivo");
                return false;
            }else {
                etImporto.setError("Utilizza il formato (123.45)");
                return false;
            }
        }

        if (entrata.getTag().equals(false) && uscita.getTag().equals(false)) {
            return false;
        }

        // after all validation return true.
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            modificaMovimentoListener = (ModificaMovimentoListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}