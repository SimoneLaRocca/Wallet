package it.unisa.walletmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;

@RunWith(RobolectricTestRunner.class)
public class MovimentoDaoUnitLocalTest {

    MovimentoDAO movimentoDAO;
    ContoDAO contoDAO;
    DatabaseHelper databaseHelper;
    ArrayList<Movimento> listaMovimenti;
    String[] listaNomi;
    Random random;

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        movimentoDAO = new MovimentoDAO(context);
        contoDAO = new ContoDAO(context);
        listaMovimenti = new ArrayList<>();
        listaNomi = new String[]{"Spesa", "Investimento", "Abbonamento", "Risparmio", "Svago"};
        random = new Random();
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.clearDatabase();
        contoDAO.insertConto("Conto corrente", 1200);
        for(int i = 0; i < 5; i++){
            listaMovimenti.add(new Movimento(-1, listaNomi[i], random.nextFloat()*10000,
                    random.nextInt(2), new GregorianCalendar(), listaNomi[i]));
        }
        for(Movimento m: listaMovimenti){
            movimentoDAO.insertMovimento(m, "Conto corrente");
        }
    }

    @After
    public void clear(){
        databaseHelper.clearDatabase();
    }

    @Test
    public void insertMovimento_MultipleInputs_True(){
        Movimento movimento = new Movimento(-1, "Abbonamento a Netflix effettuato " +
                "in data 12/10/2021 e condiviso con altre 2 persone (Marco ed Andrea)",
                999999999.99f, 0, new GregorianCalendar(), "abbonamenti");
        movimentoDAO.insertMovimento(movimento, "Conto corrente");
        movimento = new Movimento(-1, "Pranzo al ristorante",
                120.9999999f, 0, new GregorianCalendar(),
                "Lavoro presso Consorzio Farmaceutico Intercomunale " +
                        "Salerno solo il lunedì e il venerdì");
        movimentoDAO.insertMovimento(movimento, "Conto corrente");

        ArrayList<Movimento> list = (ArrayList<Movimento>) movimentoDAO.doRetrieveAll();
        assertEquals("La lista dovrebbe contenere 7 movimenti", 7, list.size());
    }

    @Test(expected = Exception.class)
    public void updateMovimento_NullNameField_ExceptionThrown(){
        listaMovimenti = (ArrayList<Movimento>) movimentoDAO.doRetrieveAll();
        listaMovimenti.get(0).setNome(null);
        movimentoDAO.updateMovimento(listaMovimenti.get(0));
    }

    @Test
    public void deleteMovimento_NoRecordsFound_True(){
        listaMovimenti = (ArrayList<Movimento>) movimentoDAO.doRetrieveAll();
        for(Movimento m : listaMovimenti){
            movimentoDAO.deleteMovimento(m.getId());
        }

        ArrayList<Movimento> list = (ArrayList<Movimento>) movimentoDAO.doRetrieveAll();
        assertNull("La lista dovrebbe essere vuota", list);
    }

    @Test
    public void doRetrieveCurrentBalance_BalanceLessThan50000_True(){
        float balance = movimentoDAO.doRetrieveCurrentBalance("Conto corrente");
        assertTrue("La lista dovrebbe essere vuota", balance < 50000f);
    }

}


































