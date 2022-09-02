package it.unisa.walletmanagement;

import static org.junit.Assert.assertEquals;
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
import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;

@RunWith(RobolectricTestRunner.class)
public class ContoDaoIntegrationLocalTest {

    ContoDAO contoDAO;
    MovimentoDAO movimentoDAO;
    DatabaseHelper databaseHelper;
    ArrayList<Movimento> listaMovimenti;
    String[] listaNomi;
    Random random;

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        contoDAO = new ContoDAO(context);
        movimentoDAO = new MovimentoDAO(context);
        listaMovimenti = new ArrayList<>();
        listaNomi = new String[]{"Spesa", "Investimento", "Abbonamento", "Risparmio", "Svago"};
        random = new Random();
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.clearDatabase();
        contoDAO.insertConto("Visa", 1200);
        for(int i = 0; i < 5; i++){
            listaMovimenti.add(new Movimento(-1, listaNomi[i], random.nextFloat()*10000,
                    random.nextInt(2), new GregorianCalendar(), listaNomi[i]));
        }
        for(Movimento m: listaMovimenti){
            movimentoDAO.insertMovimento(m, "Visa");
        }
    }

    @After
    public void clear(){
        databaseHelper.clearDatabase();
    }

    @Test
    public void deleteConto_CheckOnDeleteCascade_True(){
        Conto conto = contoDAO.doRetrieveByName("Visa");
        contoDAO.deleteConto("Visa");
        ArrayList<Movimento> movimenti = (ArrayList<Movimento>) movimentoDAO.doRetrieveAll();
        boolean check = true;
        if(movimenti == null){
            assert true;
        }else {
            for (Movimento m : movimenti){
                if(conto.getMovimenti().contains(m)){
                    check = false;
                    break;
                }
            }
        }
        assertTrue("I movimenti del conto dovrebbero essere stati tutti eliminati", check);
    }

    @Test
    public void doRetrieveAllWithCurrentBalance_CheckBalance_True(){
        ArrayList<Conto> list = (ArrayList<Conto>) contoDAO.doRetrieveAllWithCurrentBalance();
        Conto conto = contoDAO.doRetrieveByName("Visa");
        float balance = 0f;
        for (Movimento m: conto.getMovimenti()){
            if(m.getTipo() == 0){
                balance -= m.getImporto();
            }else {
                balance += m.getImporto();
            }
        }
        balance += conto.getSaldo();
        assertEquals(balance, list.get(0).getSaldo(), 0.1);
    }

}




































