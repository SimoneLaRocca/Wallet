package it.unisa.walletmanagement;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Entity.ListaCategorie;
import it.unisa.walletmanagement.Model.Storage.FileManager;

@RunWith(RobolectricTestRunner.class)
public class ListaCategorieDaoUnitLocalTest {

    ListaCategorieDAO listaCategorieDAO;
    Context context;

    @Before
    public void init(){
        context = ApplicationProvider.getApplicationContext();
        //context = Mockito.mock(Context.class);
        listaCategorieDAO = new ListaCategorieDAO(context);
        FileManager.writeToFile(context, "categorie.txt", "", false);

        listaCategorieDAO.insertCategoria("Vacanze");
        listaCategorieDAO.insertCategoria("Shopping");
        listaCategorieDAO.insertCategoria("Lavoro");
    }

    @After
    public void clear(){
        FileManager.writeToFile(context, "categorie.txt", "", false);
    }

    @Test
    public void insertCategoria_MultipleInputs_True(){
        listaCategorieDAO.insertCategoria("Shopping: H&M, Zara, Uniqlo, " +
                "Guess, GAP, Victoria’s Secret, Mark&Spencer, Mango, Adidas, Nike");
        listaCategorieDAO.insertCategoria("Libreria Feltrinelli");
        listaCategorieDAO.insertCategoria("Musica (Mp3, CD, Vinili)");

        ListaCategorie listaCategorie = listaCategorieDAO.doRetrieveListaCategorie();
        assertEquals("La lista categoria dovrebbe contenere 6 elementi", 6,
                listaCategorie.getCategorie().size());
    }

    @Test
    public void deleteConto_NoMatchFound_True(){
        listaCategorieDAO.deleteCategoria("vacanze");
        listaCategorieDAO.deleteCategoria("lavoro");
        listaCategorieDAO.deleteCategoria("Investimenti");

        ListaCategorie listaCategorie = listaCategorieDAO.doRetrieveListaCategorie();
        assertEquals("La lista dovrebbe contenere tre elementi", 3, listaCategorie.getCategorie().size());
    }
}















































