package it.unisa.walletmanagement.Control.GestioneConti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.R;

public class HomeActivity extends AppCompatActivity {

    ContoAdapter contoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ListView listViewConto = findViewById(R.id.list_view_conti);
        contoAdapter = new ContoAdapter(this, R.layout.list_view_conto_element, new ArrayList<Conto>());
        listViewConto.setAdapter(contoAdapter);

        for (int i = 0; i<100; i++){
            Conto esempio_conto = new Conto("Lavoro", 2000f, null, "");
            contoAdapter.add(esempio_conto);
        }

        listViewConto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


}