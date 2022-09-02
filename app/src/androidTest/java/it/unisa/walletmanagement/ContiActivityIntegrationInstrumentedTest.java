package it.unisa.walletmanagement;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import it.unisa.walletmanagement.Control.GestioneConti.Activity.ContoActivity;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ContiActivityIntegrationInstrumentedTest {

    @Rule
    public final ActivityTestRule<ContoActivity> mActivityRule =
            new ActivityTestRule<>(ContoActivity.class, true, false);
    ContoDAO contoDAO;
    MovimentoDAO movimentoDAO;
    ListaCategorieDAO listaCategorieDAO;
    DatabaseHelper databaseHelper;

    @Before
    public void init(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        contoDAO = new ContoDAO(appContext);
        movimentoDAO = new MovimentoDAO(appContext);
        listaCategorieDAO = new ListaCategorieDAO(appContext);
        databaseHelper = new DatabaseHelper(appContext);
        databaseHelper.clearDatabase();
    }

    @Test
    public void creaMovimento_CheckSavingInDatabase_True() {
        Conto conto = new Conto("Visa", 1200f, null);
        contoDAO.insertConto("Visa", 1200f);
        listaCategorieDAO.insertCategoria("Shopping");
        Intent intent = new Intent();
        intent.putExtra("conto", conto);
        mActivityRule.launchActivity(intent);

        // inserimento input
        onView(withId(R.id.button_aggiungi_movimento_conto)).perform(click());
        onView(withId(R.id.edit_text_nome_movimento)).perform(typeText("Spesa"));
        onView(withId(R.id.edit_text_importo_movimento)).perform(typeText("90"));
        onView(withId(R.id.spinner1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Shopping")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.button_uscita_movimento)).perform(click());
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento interfaccia
        onData(anything()).
                inAdapterView(withId(R.id.list_view_movimenti_conto)).
                atPosition(0).
                onChildView(withId(R.id.text_view_nome_movimento)).
                check(matches(withText("Spesa")));

        // verifica aggiornamento database
        conto = contoDAO.doRetrieveByName("Visa");
        assertEquals("Il movimento dovrebbe essere stato salvato nel database",
                "Spesa", conto.getMovimenti().get(0).getNome());
    }

    @Test
    public void cancellaMovimento_CheckDeletionFromDatabase_True() {
        Conto conto = new Conto("Visa", 1200f, null);
        contoDAO.insertConto("Visa", 1200f);
        listaCategorieDAO.insertCategoria("Shopping");
        Intent intent = new Intent();
        intent.putExtra("conto", conto);
        mActivityRule.launchActivity(intent);

        // inserimento input
        onView(withId(R.id.button_aggiungi_movimento_conto)).perform(click());
        onView(withId(R.id.edit_text_nome_movimento)).perform(typeText("Spesa"));
        onView(withId(R.id.edit_text_importo_movimento)).perform(typeText("90"));
        onView(withId(R.id.spinner1)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Shopping")))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.button_uscita_movimento)).perform(click());
        onView(withId(R.id.tv_ok)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.list_view_movimenti_conto))
                .atPosition(0)
                .onChildView(withId(R.id.image_view_cancella_movimento))
                .perform(click());

        // verifica aggiornamento database
        conto = contoDAO.doRetrieveByName("Visa");
        assertEquals("Il movimento dovrebbe essere stato cancellato dal database",
                0, conto.getMovimenti().size());
    }

    @Test
    public void aggiornaMovimento_CheckDatabaseUpdate_True() {
        Conto conto = new Conto("Visa", 1200f, null);
        contoDAO.insertConto("Visa", 1200f);
        listaCategorieDAO.insertCategoria("Shopping");
        movimentoDAO.insertMovimento(new Movimento(-1, "Spesa", 1200f,
                0, new GregorianCalendar(), "Shopping"), "Visa");
        Intent intent = new Intent();
        intent.putExtra("conto", conto);
        mActivityRule.launchActivity(intent);

        // inserimento input
        onData(anything())
                .inAdapterView(withId(R.id.list_view_movimenti_conto))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.edit_text_nome_movimento)).perform(clearText(), typeText("Spesa alla Conad"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento interfaccia
        onData(anything()).
                inAdapterView(withId(R.id.list_view_movimenti_conto)).
                atPosition(0).
                onChildView(withId(R.id.text_view_nome_movimento)).
                check(matches(withText("Spesa alla Conad")));

        // verifica aggiornamento database
        conto = contoDAO.doRetrieveByName("Visa");
        assertEquals("Il movimento dovrebbe essere stato modificato nel database",
                "Spesa alla Conad", conto.getMovimenti().get(0).getNome());
    }

}





































