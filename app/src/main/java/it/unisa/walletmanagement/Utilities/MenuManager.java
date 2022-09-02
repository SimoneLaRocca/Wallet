package it.unisa.walletmanagement.Utilities;

import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import it.unisa.walletmanagement.Control.Calcolatrice.CalcolatriceActivity;
import it.unisa.walletmanagement.Control.GestioneConti.Activity.CategorieActivity;
import it.unisa.walletmanagement.Control.GestioneConti.Activity.HomeActivity;
import it.unisa.walletmanagement.Control.GestioneConti.Activity.MovimentiActivity;
import it.unisa.walletmanagement.Control.Grafici.GraficiActivity;
import it.unisa.walletmanagement.Control.Impostazioni.ImpostazioniActivity;
import it.unisa.walletmanagement.Control.ListaSpesa.ListaSpesaActivity;
import it.unisa.walletmanagement.R;

public class MenuManager {

    /**
     * Restituisce l'Intent della prossima activity da aprire.
     * Usato per
     * @param item elemento della barra di navigazione selezionato
     * @param activity corrente
     * @return Intent della specifica activity da avviare
     */
    public static Intent menuItemSelected(MenuItem item, AppCompatActivity activity){
        Intent i = null;
        switch(item.getItemId())
        {
            case R.id.home:
                i = new Intent(activity, HomeActivity.class);
                break;
            case R.id.movimenti:
                i = new Intent(activity, MovimentiActivity.class);
                break;
            case R.id.categorie:
                i = new Intent(activity, CategorieActivity.class);
                break;
            case R.id.calcolatrice:
                i = new Intent(activity, CalcolatriceActivity.class);
                break;
            case R.id.grafici:
                i = new Intent(activity, GraficiActivity.class);
                break;
            case R.id.listaSpesa:
                i = new Intent(activity, ListaSpesaActivity.class);
                break;
            case R.id.impostazioni:
                i = new Intent(activity, ImpostazioniActivity.class);
                break;
            case R.id.logout:
                activity.finishAndRemoveTask();
                break;
        }
        return i;
    }
}
