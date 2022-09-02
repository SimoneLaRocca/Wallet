package it.unisa.walletmanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import it.unisa.walletmanagement.Control.Grafici.GraficiActivity;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.Model.Dao.ListaCategorieDAO;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.Model.Entity.Movimento;
import it.unisa.walletmanagement.Model.Storage.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GraficiActivityUnitInstrumentedTest {

    @Rule
    public final ActivityTestRule<GraficiActivity> mActivityRule =
            new ActivityTestRule<>(GraficiActivity.class, true, false);

    ContoDAO contoDAO;
    MovimentoDAO movimentoDAO;
    ListaCategorieDAO listaCategorieDAO;
    DatabaseHelper databaseHelper;
    ArrayList<Movimento> listaMovimenti;
    String[] listaNomi;
    Random random;

    @Before
    public void init(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        contoDAO = new ContoDAO(appContext);
        movimentoDAO = new MovimentoDAO(appContext);
        listaCategorieDAO = new ListaCategorieDAO(appContext);

        listaMovimenti = new ArrayList<>();
        listaNomi = new String[]{"Spesa", "Investimento", "Abbonamento", "Risparmio", "Svago"};
        random = new Random();

        databaseHelper = new DatabaseHelper(appContext);
        databaseHelper.clearDatabase();

        contoDAO.insertConto("Conto corrente", 1200f);
        listaCategorieDAO.insertCategoria("Shopping");

        for (String categoria: listaNomi){
            listaCategorieDAO.insertCategoria(categoria);
        }

        for(int i = 0; i < 20; i++){
            listaMovimenti.add(new Movimento(-1, listaNomi[random.nextInt(5)], random.nextFloat()*10000,
                    random.nextInt(2), new GregorianCalendar(), listaNomi[random.nextInt(5)]));
        }

        for(Movimento m: listaMovimenti){
            movimentoDAO.insertMovimento(m, "Conto corrente");
        }
    }

    @Test
    public void creazioneGrafici_CheckActivityOpening_True() throws InterruptedException {
        mActivityRule.launchActivity(null);

        Intents.init();
        onView(withId(R.id.scroll_view_grafici)).perform(click());
        Intents.release();
    }
}
