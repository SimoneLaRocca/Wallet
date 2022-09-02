package it.unisa.walletmanagement;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unisa.walletmanagement.Control.GestioneConti.Activity.CategorieActivity;
import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Entity.ListaCategorie;
import it.unisa.walletmanagement.Model.Storage.FileManager;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategorieActivityIntegrationInstrumentedTest {
    @Rule
    public final ActivityTestRule<CategorieActivity> mActivityRule =
            new ActivityTestRule<>(CategorieActivity.class, true, false);
    ListaCategorieDAO listaCategorieDAO;

    @Before
    public void init(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        listaCategorieDAO = new ListaCategorieDAO(appContext);
        FileManager.writeToFile(appContext, "categorie.txt", "", false);
    }

    @Test
    public void creaCategoria_CheckSavingInFile_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.fab_crea_categoria)).perform(click());
        onView(withId(R.id.edit_text_nome_categoria)).perform(typeText("Shopping"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento interfaccia
        onData(anything()).
                inAdapterView(withId(R.id.list_view_categorie)).
                atPosition(0).
                onChildView(withId(R.id.text_view_categoria)).
                check(matches(withText("Shopping")));

        // verifica aggiornamento database
        ListaCategorie listaCategorie = listaCategorieDAO.doRetrieveListaCategorie();
        assertEquals("La categoria dovrebbe essere stata salvata nel database",
                "Shopping", listaCategorie.getCategorie().get(0));
    }

    @Test
    public void cancellaMovimento_CheckDeletionFromFile_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.fab_crea_categoria)).perform(click());
        onView(withId(R.id.edit_text_nome_categoria)).perform(typeText("Shopping"));
        onView(withId(R.id.tv_ok)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.list_view_categorie))
                .atPosition(0)
                .onChildView(withId(R.id.image_view_cancella_categoria))
                .perform(click());

        // verifica aggiornamento database
        ListaCategorie listaCategorie = listaCategorieDAO.doRetrieveListaCategorie();
        assertNull("La categoria dovrebbe essere stata rimossa dal database", listaCategorie);
    }
}
