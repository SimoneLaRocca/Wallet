package it.unisa.walletmanagement.Control.GestioneConti.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import it.unisa.walletmanagement.Control.GestioneConti.Adapter.CategorieAdapter;
import it.unisa.walletmanagement.Control.GestioneConti.Fragment.CreaCategoriaDialog;
import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Entity.ListaCategorie;
import it.unisa.walletmanagement.R;
import it.unisa.walletmanagement.Utilities.MenuManager;

public class CategorieActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CreaCategoriaDialog.CategoriaListener {

    private ListView listViewCategorie;
    private CategorieAdapter categorieAdapter;
    private ListaCategorieDAO listaCategorieDAO;

    private FloatingActionButton floatingActionButton;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_categorie);

        // navigation drawer
        drawerLayout = findViewById(R.id.drawer_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CATEGORIE");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        floatingActionButton = findViewById(R.id.fab_crea_categoria);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreaCategoriaDialog creaCategoriaDialog = new CreaCategoriaDialog();
                creaCategoriaDialog.show(getSupportFragmentManager(), "Crea categoria");
            }
        });

        // list view
        listViewCategorie = findViewById(R.id.list_view_categorie);
        categorieAdapter = new CategorieAdapter(this, R.layout.list_view_categoria_element, new ArrayList<String>());
        listViewCategorie.setAdapter(categorieAdapter);

        listaCategorieDAO = new ListaCategorieDAO(getApplicationContext());
        ListaCategorie listaCategorie = listaCategorieDAO.doRetrieveListaCategorie();
        if(listaCategorie != null){
            for(String cat : listaCategorie.getCategorie()){
                categorieAdapter.add(cat);
            }
        }
    }

    /**
     * Metodo dell'interfaccia CreaCategoriaDialog.CategoriaListener.
     * Riceve la categoria dal fragment dialog e si occupa del salvataggio nel file.
     * @param categoria ricevuta dal fragment
     */
    @Override
    public void sendCategoria(String categoria) {
        if(listaCategorieDAO.insertCategoria(categoria)){
            categorieAdapter.add(categoria);
            categorieAdapter.notifyDataSetChanged();
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
        startActivity(MenuManager.menuItemSelected(item, CategorieActivity.this));
        return true;
    }
}