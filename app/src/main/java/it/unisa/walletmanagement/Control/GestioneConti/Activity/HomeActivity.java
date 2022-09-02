package it.unisa.walletmanagement.Control.GestioneConti.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import it.unisa.walletmanagement.Control.GestioneConti.Adapter.ContoAdapter;
import it.unisa.walletmanagement.Control.GestioneConti.Fragment.CreaContoDialog;
import it.unisa.walletmanagement.Control.Impostazioni.LoginActivity;
import it.unisa.walletmanagement.Control.Impostazioni.SecurityManager;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.R;
import it.unisa.walletmanagement.Utilities.MenuManager;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CreaContoDialog.ContoListener {

    private ContoAdapter contoAdapter;
    private ListView listViewConto;
    private ArrayList<Conto> lista_conti;
    private ContoDAO contoDAO;

    private TextView tvNessunConto;
    private ImageView imageViewIcon;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_home);

        boolean login = getIntent().getBooleanExtra("login", false);
        SecurityManager securityManager = new SecurityManager(getApplicationContext());
        if(securityManager.isSecurityEnabled() && !login){
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            HomeActivity.this.finish();
        }

        // navigation drawer code
        drawerLayout = findViewById(R.id.drawer_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        listViewConto = findViewById(R.id.list_view_conti);
        contoAdapter = new ContoAdapter(this, R.layout.list_view_conto_element, new ArrayList<Conto>());
        listViewConto.setAdapter(contoAdapter);

        tvNessunConto = findViewById(R.id.text_view_nessun_conto);
        imageViewIcon = findViewById(R.id.image_view_icon_conto);

        contoDAO = new ContoDAO(getApplicationContext());
        lista_conti = (ArrayList<Conto>) contoDAO.doRetrieveAllWithCurrentBalance();
        if(lista_conti != null){
            tvNessunConto.setVisibility(View.INVISIBLE);
            imageViewIcon.setVisibility(View.INVISIBLE);
            listViewConto.setVisibility(View.VISIBLE);
            for(Conto conto : lista_conti){
                contoAdapter.add(conto);
            }
        }else {
            tvNessunConto.setVisibility(View.VISIBLE);
            imageViewIcon.setVisibility(View.VISIBLE);
            listViewConto.setVisibility(View.INVISIBLE);
        }

        listViewConto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conto conto = (Conto) listViewConto.getItemAtPosition(i);
                Intent intent = new Intent(HomeActivity.this, ContoActivity.class);
                intent.putExtra("conto", conto);
                startActivity(intent);
            }
        });
    }

    public void creaConto(View view) {
        CreaContoDialog creaContoDialog = new CreaContoDialog();
        creaContoDialog.show(getSupportFragmentManager(), "Crea conto");
    }

    /**
     * Metodo dell'interfaccia CreaContoDialog.ContoListener.
     * Riceve il conto dal fragment dialog, si occupa del suo salvataggio nel db
     * e dell'aggiornamento del layout dell'activity.
     * @param conto ricevuto dal fragment
     */
    @Override
    public void sendConto(Conto conto) {
        tvNessunConto.setVisibility(View.INVISIBLE);
        imageViewIcon.setVisibility(View.INVISIBLE);
        listViewConto.setVisibility(View.VISIBLE);
        if(contoDAO.insertConto(conto.getNome(), conto.getSaldo())){
            contoAdapter.add(conto);
            contoAdapter.notifyDataSetChanged();
        }
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
        startActivity(MenuManager.menuItemSelected(item, HomeActivity.this));
        return true;
    }
}