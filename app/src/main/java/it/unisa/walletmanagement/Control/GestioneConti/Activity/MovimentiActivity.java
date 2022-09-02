package it.unisa.walletmanagement.Control.GestioneConti.Activity;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import it.unisa.walletmanagement.Control.GestioneConti.Fragment.CreaMovimentoGenericoDialog;
import it.unisa.walletmanagement.Control.GestioneConti.Fragment.ModificaMovimentoDialog;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.R;
import it.unisa.walletmanagement.Utilities.MenuManager;

public class MovimentiActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ModificaMovimentoDialog.ModificaMovimentoListener,
        CreaMovimentoGenericoDialog.CreaMovimentoGenericoListener{

    private ListView listViewMovEntrate;
    private ListView listViewMovUscite;
    private MovimentoAdapter movimentoAdapterEntrate;
    private MovimentoAdapter movimentoAdapterUscite;
    private MovimentoDAO movimentoDAO;
    private ContoDAO contoDAO;
    private ListaCategorieDAO listaCategorieDAO;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_movimenti);

        // navigation drawer code
        drawerLayout = findViewById(R.id.drawer_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MOVIMENTI");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        listViewMovEntrate = findViewById(R.id.list_view_movimenti_entrate);
        movimentoAdapterEntrate = new MovimentoAdapter(this, R.layout.list_view_movimento_element, new ArrayList<Movimento>());
        listViewMovEntrate.setAdapter(movimentoAdapterEntrate);

        listViewMovUscite = findViewById(R.id.list_view_movimenti_uscite);
        movimentoAdapterUscite = new MovimentoAdapter(this, R.layout.list_view_movimento_element, new ArrayList<Movimento>());
        listViewMovUscite.setAdapter(movimentoAdapterUscite);

        movimentoDAO = new MovimentoDAO(getApplicationContext());

        new LoadMovimenti().execute();

        contoDAO = new ContoDAO(getApplicationContext());
        listaCategorieDAO = new ListaCategorieDAO(getApplicationContext());

        listViewMovEntrate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // recupera il movimento, aggiungilo al bundle, crea il fragment, chiama show()
                if(listaCategorieDAO.doRetrieveListaCategorie() == null) {
                    showToastCustomizzato(R.layout.custom_toast_categoria);
                } else {
                    Movimento movimento = (Movimento) listViewMovEntrate.getItemAtPosition(i);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movimento", movimento);
                    ModificaMovimentoDialog dialog = new ModificaMovimentoDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "Movimento");
                }
            }
        });

        listViewMovUscite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // recupera il movimento, aggiungilo al bundle, crea il fragment, chiama show()
                if(listaCategorieDAO.doRetrieveListaCategorie() == null) {
                    showToastCustomizzato(R.layout.custom_toast_categoria);
                } else {
                    Movimento movimento = (Movimento) listViewMovUscite.getItemAtPosition(i);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movimento", movimento);
                    ModificaMovimentoDialog dialog = new ModificaMovimentoDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "Movimento");
                }
            }
        });
    }

    public void creaMovimentoGenerico(View view) {
        // mostra toast customizzato: per aggiungere movimenti devi prima creare un conto
        if(contoDAO.doCount() < 1){
            showToastCustomizzato(R.layout.custom_toast_conto);
        } else if(listaCategorieDAO.doRetrieveListaCategorie() == null){
            showToastCustomizzato(R.layout.custom_toast_categoria);
        } else {
            CreaMovimentoGenericoDialog creaMovimentoGenericoDialog = new CreaMovimentoGenericoDialog();
            creaMovimentoGenericoDialog.show(getSupportFragmentManager(), "Crea movimento generico");
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
        if(oldMovimento.getTipo() == 1)
            movimentoAdapterEntrate.remove(oldMovimento);
        else
            movimentoAdapterUscite.remove(oldMovimento);

        if(newMovimento.getTipo() == 1){
            movimentoDAO.updateMovimento(newMovimento);
            movimentoAdapterEntrate.add(newMovimento);
            movimentoAdapterEntrate.notifyDataSetChanged();
        }else {
            movimentoDAO.updateMovimento(newMovimento);
            movimentoAdapterUscite.add(newMovimento);
            movimentoAdapterUscite.notifyDataSetChanged();
        }
    }

    /**
     * Metodo dell'interfaccia ModificaMovimentoDialog.ModificaMovimentoListener.
     * Riceve il nuovo movimento creato, lo salva nel db ed
     * aggiorna il listView del layout dell'activity.
     * @param movimento nuovo movimento creato
     * @param nome_conto nome del conto in cui inserire il movimento
     */
    @Override
    public void sendNewMovimentoGenerico(Movimento movimento, String nome_conto) {
        movimentoDAO.insertMovimento(movimento, nome_conto);
        // aggiorna la list view
        movimentoAdapterEntrate.clear();
        movimentoAdapterUscite.clear();
        new LoadMovimenti().execute();
        movimentoAdapterUscite.notifyDataSetChanged();
        movimentoAdapterEntrate.notifyDataSetChanged();
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
        startActivity(MenuManager.menuItemSelected(item, MovimentiActivity.this));
        return true;
    }

    class LoadMovimenti extends AsyncTask<Void, Void, ArrayList<Movimento>> {

        @Override
        protected ArrayList<Movimento> doInBackground(Void... voids) {
            ArrayList<Movimento> list = (ArrayList<Movimento>) movimentoDAO.doRetrieveAll();
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Movimento> list) {
            if(list != null) {
                for (Movimento movimento : list) {
                    if(movimento.getTipo() == 0){
                        movimentoAdapterUscite.add(movimento);
                    }else {
                        movimentoAdapterEntrate.add(movimento);
                    }
                }
            }
        }

    }
}