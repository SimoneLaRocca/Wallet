package it.unisa.walletmanagement;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import it.unisa.walletmanagement.Model.Dao.ListaSpesaDAO;
import it.unisa.walletmanagement.Model.Entity.ListaSpesa;
import it.unisa.walletmanagement.Model.Storage.FileManager;

@RunWith(RobolectricTestRunner.class)
public class ListaSpesaDaoUnitLocalTest {

    ListaSpesaDAO listaSpesaDAO;
    Context context;

    @Before
    public void init(){
        context = ApplicationProvider.getApplicationContext();
        //context = Mockito.mock(Context.class);
        listaSpesaDAO = new ListaSpesaDAO(context);
        FileManager.writeToFile(context, "lista_spesa.txt", "", false);

        listaSpesaDAO.insertVoce("Organizzare le vacanze");
        listaSpesaDAO.insertVoce("Fare shopping");
        listaSpesaDAO.insertVoce("Revisione progetto");
    }

    @After
    public void clear(){
        FileManager.writeToFile(context, "lista_spesa.txt", "", false);
    }

    @Test
    public void insertCategoria_MultipleInputs_True(){
        listaSpesaDAO.insertVoce("Shopping: H&M, Zara, Uniqlo, " +
                "Guess, GAP, Victoria’s Secret, Mark&Spencer, Mango, Adidas, Nike");
        listaSpesaDAO.insertVoce("Comprare regalo festa Matteo");
        listaSpesaDAO.insertVoce("Fare prelievo in banca");

        ListaSpesa listaSpesa = listaSpesaDAO.doRetrieveListaSpesa();
        assertEquals("La lista categoria dovrebbe contenere 6 elementi", 6,
                listaSpesa.getLista().size());
    }

    @Test
    public void deleteConto_NoMatchFound_True(){
        listaSpesaDAO.deleteVoce("organizzare le vacanze");
        listaSpesaDAO.deleteVoce("fare shopping");
        listaSpesaDAO.deleteVoce("Investimenti in ETF");

        ListaSpesa listaSpesa = listaSpesaDAO.doRetrieveListaSpesa();
        assertEquals("La lista dovrebbe contenere tre elementi", 3, listaSpesa.getLista().size());
    }
}
