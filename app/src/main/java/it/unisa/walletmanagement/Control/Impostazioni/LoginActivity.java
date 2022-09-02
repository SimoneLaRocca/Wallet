package it.unisa.walletmanagement.Control.Impostazioni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import it.unisa.walletmanagement.Control.GestioneConti.Activity.HomeActivity;
import it.unisa.walletmanagement.R;

public class LoginActivity extends AppCompatActivity {

    EditText etPassword;
    Button button_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword = findViewById(R.id.edit_text_password);
        button_password = findViewById(R.id.button_password);

        button_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                SecurityManager securityManager = new SecurityManager(getApplicationContext());
                if(securityManager.checkLogin(password)){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("login", true);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Password errata", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}