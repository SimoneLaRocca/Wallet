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

import java.util.ArrayList;

import it.unisa.walletmanagement.Control.GestioneConti.Activity.HomeActivity;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Entity.Conto;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityIntegrationInstrumentedTest {

    @Rule
    public final ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule<>(HomeActivity.class, true, false);
    ContoDAO contoDAO;
    DatabaseHelper databaseHelper;

    @Before
    public void init(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        contoDAO = new ContoDAO(appContext);
        databaseHelper = new DatabaseHelper(appContext);
        databaseHelper.clearDatabase();
    }

    @Test
    public void creaConto_CheckSavingInDatabase_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.button_crea_conto)).perform(click());
        onView(withId(R.id.edit_text_nome_conto)).perform(typeText("Visa"));
        onView(withId(R.id.edit_text_saldo_conto)).perform(typeText("1200"));
        onView(withId(R.id.tv_ok)).perform(click());

        onView(withId(R.id.button_crea_conto)).perform(click());
        onView(withId(R.id.edit_text_nome_conto)).perform(typeText("Conto corrente"));
        onView(withId(R.id.edit_text_saldo_conto)).perform(typeText("9000"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento interfaccia
        onData(anything()).
                inAdapterView(withId(R.id.list_view_conti)).
                atPosition(0).
                onChildView(withId(R.id.tv_nome_conto)).
                check(matches(withText("Visa")));

        onData(anything()).
                inAdapterView(withId(R.id.list_view_conti)).
                atPosition(1).
                onChildView(withId(R.id.tv_nome_conto)).
                check(matches(withText("Conto corrente")));

        // verifica aggiornamento database
        ArrayList<Conto> conto = (ArrayList<Conto>) contoDAO.doRetrieveAll();
        assertEquals("Il conto dovrebbe essere stato salvato nel database", "Visa", conto.get(0).getNome());
        assertEquals("Il conto dovrebbe essere stato salvato nel database", "Conto corrente", conto.get(1).getNome());
    }

    @Test
    public void cancellaConto_CheckDeletionFromDatabase_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.button_crea_conto)).perform(click());
        onView(withId(R.id.edit_text_nome_conto)).perform(typeText("Visa"));
        onView(withId(R.id.edit_text_saldo_conto)).perform(typeText("1200"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento interfaccia
        onData(anything())
                .inAdapterView(withId(R.id.list_view_conti))
                .atPosition(0)
                .onChildView(withId(R.id.image_view_cancella_conto))
                .perform(click());

        // verifica aggiornamento database
        Conto conto = contoDAO.doRetrieveByName("Visa");
        assertNull("Il conto dovrebbe essere stato cancellato dal database", conto);
    }

}
