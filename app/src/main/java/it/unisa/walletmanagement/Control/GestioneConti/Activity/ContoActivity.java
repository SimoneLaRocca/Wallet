package it.unisa.walletmanagement.Control.GestioneConti.Activity;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import it.unisa.walletmanagement.Control.GestioneConti.Adapter.MovimentoAdapter;
import it.unisa.walletmanagement.Control.GestioneConti.Fragment.CreaMovimentoDialog;
import it.unisa.walletmanagement.Control.GestioneConti.Fragment.ModificaMovimentoDialog;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.R;
import it.unisa.walletmanagement.Utilities.MenuManager;

public class ContoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            ModificaMovimentoDialog.ModificaMovimentoListener,
                CreaMovimentoDialog.CreaMovimentoListener, MovimentoAdapter.MovimentoListener {

    private MovimentoAdapter movimentoAdapter;
    private ListView listViewMovimenti;
    private Conto conto;
    private ContoDAO contoDAO;
    private MovimentoDAO movimentoDAO;
    private ListaCategorieDAO listaCategorieDAO;

    private TextView tvNomeConto, tvSaldoConto;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_conto);

        // navigation drawer
        drawerLayout = findViewById(R.id.drawer_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CONTO");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Imposta le info specifiche del conto selezionato
        // saldo totale: somma iniziale + somma di tutti i movimenti
        conto = (Conto) getIntent().getSerializableExtra("conto");
        tvNomeConto = findViewById(R.id.text_view_nome_conto);
        tvSaldoConto = findViewById(R.id.text_view_saldo_conto);

        listViewMovimenti = findViewById(R.id.list_view_movimenti_conto);
        movimentoAdapter = new MovimentoAdapter(this, R.layout.list_view_movimento_element, new ArrayList<Movimento>(), this);
        listViewMovimenti.setAdapter(movimentoAdapter);

        contoDAO = new ContoDAO(getApplicationContext());
        movimentoDAO = new MovimentoDAO(getApplicationContext());
        listaCategorieDAO = new ListaCategorieDAO(getApplicationContext());

        new LoadMovimenti().execute();

        tvNomeConto.setText("CONTO: " + conto.getNome());
        tvSaldoConto.setText("Saldo: " + movimentoDAO.doRetrieveCurrentBalance(conto.getNome()));

        listViewMovimenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // recupera il movimento, aggiungilo al bundle, crea il fragment, chiama show()
                if(listaCategorieDAO.doRetrieveListaCategorie() == null) {
                    showToastCustomizzato(R.layout.custom_toast_categoria);
                } else {
                    Movimento movimento = (Movimento) listViewMovimenti.getItemAtPosition(i);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movimento", movimento);
                    ModificaMovimentoDialog dialog = new ModificaMovimentoDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "Movimento");
                }
            }
        });
    }

    public void creaMovimento(View view) {
        if(listaCategorieDAO.doRetrieveListaCategorie() == null) {
            showToastCustomizzato(R.layout.custom_toast_categoria);
        } else {
            CreaMovimentoDialog creaMovimentoDialog = new CreaMovimentoDialog();
            creaMovimentoDialog.show(getSupportFragmentManager(), "Crea movimento");
        }
    }

    /**
     * Metodo dell'interfaccia ModificaMovimentoDialog.ModificaMovimentoListener.
     * Riceve il movimento, lo aggiorna nel db ed
     * aggiorna il listView del layout dell'activity.
     * @param oldMovimento vecchia versione del movimento, necessaria
     *                     per rimuovere l'istanza dall'adapter del list view
     * @param newMovimento movimento aggiornato
     */
    @Override
    public void sendUpdatedMovimento(Movimento oldMovimento, Movimento newMovimento) {
        movimentoAdapter.remove(oldMovimento);
        movimentoDAO.updateMovimento(newMovimento);
        movimentoAdapter.add(newMovimento);
        movimentoAdapter.notifyDataSetChanged();
        tvSaldoConto.setText("Saldo: " + movimentoDAO.doRetrieveCurrentBalance(conto.getNome()));
    }

    /**
     * Metodo dell'interfaccia CreaMovimentoDialog.CreaMovimentoListener.
     * Riceve il nuovo movimento creato, lo salva nel db ed
     * aggiorna il listView del layout dell'activity.
     * @param movimento nuovo movimento creato
     */
    @Override
    public void sendNewMovimento(Movimento movimento) {
        movimentoDAO.insertMovimento(movimento, conto.getNome());
        movimentoAdapter.clear();
        new LoadMovimenti().execute();
        movimentoAdapter.notifyDataSetChanged();
        tvSaldoConto.setText("Saldo: " + movimentoDAO.doRetrieveCurrentBalance(conto.getNome()));
    }

    /**
     * Metodo dell'interfaccia MovimentoAdapter.MovimentoListener.
     * Usato per sapere quando viene cancellato un movimento e di
     * conseguenza quando aggiornare il layout dell'activity
     * @param movimento movimento rimosso dal db
     */
    @Override
    public void deleteMovimento(Movimento movimento) {
        tvSaldoConto.setText("Saldo: " + movimentoDAO.doRetrieveCurrentBalance(conto.getNome()));
    }

    public void showToastCustomizzato(int layout) {
        Toast toast = new Toast(getApplicationContext());
        toast.setView(getLayoutInflater().inflate(layout, null));
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            this.finishAffinity();
            System.exit(0);
        }
        startActivity(MenuManager.menuItemSelected(item, ContoActivity.this));
        return true;
    }

    class LoadMovimenti extends AsyncTask<Void, Void, Conto>{

        @Override
        protected Conto doInBackground(Void... voids) {
            Conto c = contoDAO.doRetrieveByName(conto.getNome());
            return c;
        }

        @Override
        protected void onPostExecute(Conto c) {
            if(c != null) {
                for (Movimento movimento : c.getMovimenti()) {
                    movimentoAdapter.add(movimento);
                }
                movimentoAdapter.notifyDataSetChanged();
            }
        }

    }

}