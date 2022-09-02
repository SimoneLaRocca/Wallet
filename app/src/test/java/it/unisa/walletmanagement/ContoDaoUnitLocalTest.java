package it.unisa.walletmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
//import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;

@RunWith(RobolectricTestRunner.class)
public class ContoDaoUnitLocalTest{

    ContoDAO contoDAO;
    DatabaseHelper databaseHelper;

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        contoDAO = new ContoDAO(context);
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.clearDatabase();
        contoDAO.insertConto("Visa", 1200);
    }

    @After
    public void clear(){
        databaseHelper.clearDatabase();
    }

    @Test
    public void insertConto_MultipleInputs_True(){
        contoDAO.insertConto("IT69L0300203280128178434811", 99);
        contoDAO.insertConto("Banca", 999999999.99f);
        contoDAO.insertConto("Binance", 0);
        contoDAO.insertConto("binance", 200.01f);
        contoDAO.insertConto("Investimento in azioni, obbligazioni, " +
                "future, derivati, crypto, ETF, etc... (eToro)", 0.50f);

        ArrayList<Conto> list = (ArrayList<Conto>) contoDAO.doRetrieveAll();
        assertEquals("La lista dovrebbe contenere 6 conti", 6, list.size());
    }

    @Test
    public void deleteConto_NoRecords_True(){
        contoDAO.deleteConto("Visa");

        ArrayList<Conto> list = (ArrayList<Conto>) contoDAO.doRetrieveAll();
        assertNull("La lista dovrebbe essere vuota", list);
    }

    @Test
    public void deleteConto_CancellationFailed_True(){
        contoDAO.deleteConto("visa");

        ArrayList<Conto> list = (ArrayList<Conto>) contoDAO.doRetrieveAll();
        assertEquals("La lista dovrebbe contenere 1 conto", 1, list.size());
    }

    @Test
    public void doRetrieveByName_NoRecordFound_False(){
        assertNull("Nessun conto dovrebbe essere trovato", contoDAO.doRetrieveByName("Conto corrente"));
    }

    @Test
    public void doCount_OneRecord_True(){
        assertEquals("Dovrebbe esserci 1 conto", 1, contoDAO.doCount());
    }
}
