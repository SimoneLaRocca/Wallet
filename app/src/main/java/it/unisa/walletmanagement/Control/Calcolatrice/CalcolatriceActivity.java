package it.unisa.walletmanagement.Control.Calcolatrice;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import it.unisa.walletmanagement.R;
import it.unisa.walletmanagement.Utilities.MenuManager;

public class CalcolatriceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private float value1;
    private float value2;
    private boolean[] check_value;
    private String operator;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_calcolatrice);

        // navigation drawer code
        drawerLayout = findViewById(R.id.drawer_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CALCOLATRICE");
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        check_value = new boolean[2];

        display = findViewById(R.id.display);
    }

    public void digitClick(View view){
        Button b = (Button) view;
        if(display.getText().toString().length() > 15){
            return;
        }
        String digit = b.getText().toString();
        display.setText(display.getText().toString()+digit);
    }

    public void dotClick(View view){
        Button b = (Button) view;
        if(display.getText().toString().contains(".")) {
            return;
        }
        if(!display.getText().toString().equals("")) {
            String digit = b.getText().toString();
            display.setText(display.getText().toString()+digit);
        }else {
            return;
        }
    }

    public void cancelClick(View view){
        display.setText("");
        value1 = 0;
        value2 = 0;
        check_value[0] = false;
        check_value[1] = false;
    }

    public void operatorClick(View view){
        Button b = (Button) view;
        if(!display.getText().toString().equals("")) {
            value1 = Float.parseFloat(display.getText().toString());
            check_value[0] = true;
            operator = b.getText().toString();
            display.setText("");
        }else {
            check_value[0] = false;
            return;
        }
    }

    public void equalsClick(View view){

        if(!check_value[0]){
            return;
        }

        if(!display.getText().toString().equals("")) {
            value2 = Float.parseFloat(display.getText().toString());
        }else {
            return;
        }

        if(operator.length() == 0){
            Toast.makeText(getApplicationContext(), "Operatore nullo", Toast.LENGTH_LONG).show();
            return;
        }

        display.setText("");
        float result;
        switch (operator){
            case "+":
                result = value1 + value2;
                break;
            case "-":
                result = value1 - value2;
                break;
            case "*":
                result = value1 * value2;
                break;
            case "/":
                if(value2 == 0){
                    Toast.makeText(getApplicationContext(), "Divisione per 0 non ammessa",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                result = value1 / value2;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
        display.setText(""+result);
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
        startActivity(MenuManager.menuItemSelected(item, CalcolatriceActivity.this));
        return true;
    }
}