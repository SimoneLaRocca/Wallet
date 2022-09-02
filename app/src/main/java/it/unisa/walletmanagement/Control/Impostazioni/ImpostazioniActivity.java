package it.unisa.walletmanagement.Control.Impostazioni;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import it.unisa.walletmanagement.Control.Impostazioni.Fragment.*;
import it.unisa.walletmanagement.R;
import it.unisa.walletmanagement.Utilities.MenuManager;

public class ImpostazioniActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            ImpostaPasswordDialog.PasswordListener,
                CambiaPasswordDialog.PasswordListener,
                    RimuoviPasswordDialog.PasswordListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityManager securityManager = new SecurityManager(getApplicationContext());

        if(!securityManager.isSecurityEnabled()){
            setContentView(R.layout.nav_activity_impostazioni);
        }else {
            setContentView(R.layout.nav_activity_impostazioni2);
        }

        // navigation drawer code
        drawerLayout = findViewById(R.id.drawer_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("IMPOSTAZIONI");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void setPassword(View view) {
        ImpostaPasswordDialog impostaPasswordDialog = new ImpostaPasswordDialog();
        impostaPasswordDialog.show(getSupportFragmentManager(), "Imposta password");
    }

    public void changePassword(View view) {
        CambiaPasswordDialog cambiaPasswordDialog = new CambiaPasswordDialog();
        cambiaPasswordDialog.show(getSupportFragmentManager(), "Cambia password");
    }

    public void removePassword(View view) {
        RimuoviPasswordDialog rimuoviPasswordDialog = new RimuoviPasswordDialog();
        rimuoviPasswordDialog.show(getSupportFragmentManager(), "Rimuovi password");
    }

    /**
     * Metodo dell'interfaccia ImpostaPasswordDialog.PasswordListener.
     * Imposta la password attraverso il SecurityManager e rilancia l'activity.
     * @param password selezionata dall'utente
     */
    @Override
    public void sendPassword(String password) {
        SecurityManager securityManager = new SecurityManager(getApplicationContext());
        securityManager.doSavePassword(password);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * Metodo dell'interfaccia CambiaPasswordDialog.PasswordListener.
     * Cambia la password attraverso il SecurityManager.
     * @param password nuova password selezionata dall'utente
     */
    @Override
    public void sendNewPassword(String password) {
        SecurityManager securityManager = new SecurityManager(getApplicationContext());
        securityManager.doSavePassword(password);
    }

    /**
     * Metodo dell'interfaccia RimuoviPasswordDialog.PasswordListener.
     * Rimuove la password attraverso il SecurityManager e rilancia l'activity.
     * @param password password attuale
     */
    @Override
    public void sendDeletedPassword(String password) {
        SecurityManager securityManager = new SecurityManager(getApplicationContext());
        securityManager.doRemovePassword(password);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
        startActivity(MenuManager.menuItemSelected(item, ImpostazioniActivity.this));
        return true;
    }
}