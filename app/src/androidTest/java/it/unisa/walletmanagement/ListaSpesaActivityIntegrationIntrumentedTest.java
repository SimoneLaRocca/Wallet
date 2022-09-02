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

import it.unisa.walletmanagement.Control.ListaSpesa.ListaSpesaActivity;
import it.unisa.walletmanagement.Model.Dao.ListaSpesaDAO;
import it.unisa.walletmanagement.Model.Entity.ListaSpesa;
import it.unisa.walletmanagement.Model.Storage.FileManager;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListaSpesaActivityIntegrationIntrumentedTest {
    @Rule
    public final ActivityTestRule<ListaSpesaActivity> mActivityRule =
            new ActivityTestRule<>(ListaSpesaActivity.class, true, false);
    ListaSpesaDAO listaSpesaDAO;

    @Before
    public void init(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        listaSpesaDAO = new ListaSpesaDAO(appContext);
        FileManager.writeToFile(appContext, "lista_spesa.txt", "", false);
    }

    @Test
    public void creaVoce_CheckSavingInFile_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.fab_aggiungi_voce)).perform(click());
        onView(withId(R.id.edit_text_voce)).perform(typeText("Comprare il latte"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento interfaccia
        onData(anything()).
                inAdapterView(withId(R.id.list_view_lista_spesa)).
                atPosition(0).
                onChildView(withId(R.id.text_view_voce)).
                check(matches(withText("Comprare il latte")));

        // verifica aggiornamento database
        ListaSpesa listaSpesa = listaSpesaDAO.doRetrieveListaSpesa();
        assertEquals("La voce dovrebbe essere stata salvata nel database",
                "Comprare il latte", listaSpesa.getLista().get(0));
    }

    @Test
    public void cancellaVoce_CheckDeletionFromFile_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.fab_aggiungi_voce)).perform(click());
        onView(withId(R.id.edit_text_voce)).perform(typeText("Comprare il latte"));
        onView(withId(R.id.tv_ok)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.list_view_lista_spesa))
                .atPosition(0)
                .onChildView(withId(R.id.checkbox_cancella))
                .perform(click());

        // verifica aggiornamento interfaccia
        onData(anything()).
                inAdapterView(withId(R.id.list_view_lista_spesa)).
                atPosition(0).
                onChildView(withId(R.id.text_view_voce)).
                check(matches(withText("Comprare il latte")));

        // verifica aggiornamento database
        ListaSpesa listaSpesa = listaSpesaDAO.doRetrieveListaSpesa();
        assertNull("La voce dovrebbe essere stata cancellata dal database", listaSpesa);
    }
}

































