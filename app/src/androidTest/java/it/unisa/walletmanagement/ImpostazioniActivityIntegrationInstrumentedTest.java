package it.unisa.walletmanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unisa.walletmanagement.Control.Impostazioni.ImpostazioniActivity;
import it.unisa.walletmanagement.Control.Impostazioni.SecurityManager;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ImpostazioniActivityIntegrationInstrumentedTest {

    @Rule
    public final ActivityTestRule<ImpostazioniActivity> mActivityRule =
            new ActivityTestRule<>(ImpostazioniActivity.class, true, false);
    SecurityManager securityManager;

    @Before
    public void init(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        securityManager = new SecurityManager(appContext);
    }

    @After
    public void clear(){
        securityManager.doRemovePassword("5eyjT9Y38%J@");
    }

    @Test
    public void impostaPassword_CheckSecurityEnabled_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.button_imposta_password)).perform(click());
        onView(withId(R.id.edit_text_first_password)).perform(typeText("5eyjT9Y38%J@"));
        onView(withId(R.id.edit_text_second_password)).perform(typeText("5eyjT9Y38%J@"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento file
        boolean login = securityManager.checkLogin("5eyjT9Y38%J@");
        assertTrue("La password dovrebbe essere stata salvata nel file", login);
    }

    @Test
    public void cambiaPassword_CheckFileUpdate_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.button_imposta_password)).perform(click());
        onView(withId(R.id.edit_text_first_password)).perform(typeText("wBRQ*K02K6Ay"));
        onView(withId(R.id.edit_text_second_password)).perform(typeText("wBRQ*K02K6Ay"));
        onView(withId(R.id.tv_ok)).perform(click());

        onView(withId(R.id.button_cambia_password)).perform(click());
        onView(withId(R.id.edit_text_old_password)).perform(typeText("wBRQ*K02K6Ay"));
        onView(withId(R.id.edit_text_new_first_password)).perform(typeText("5eyjT9Y38%J@"));
        onView(withId(R.id.edit_text_new_second_password)).perform(typeText("5eyjT9Y38%J@"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento file
        boolean login = securityManager.checkLogin("5eyjT9Y38%J@");
        assertTrue("La password dovrebbe essere stata aggiornata nel file", login);
    }

    @Test
    public void rimuoviPassword_CheckDeletionFromFile_True() {
        mActivityRule.launchActivity(null);

        // inserimento input
        onView(withId(R.id.button_imposta_password)).perform(click());
        onView(withId(R.id.edit_text_first_password)).perform(typeText("wBRQ*K02K6Ay"));
        onView(withId(R.id.edit_text_second_password)).perform(typeText("wBRQ*K02K6Ay"));
        onView(withId(R.id.tv_ok)).perform(click());

        onView(withId(R.id.button_rimuovi_password)).perform(click());
        onView(withId(R.id.edit_text_password)).perform(typeText("wBRQ*K02K6Ay"));
        onView(withId(R.id.tv_ok)).perform(click());

        // verifica aggiornamento file
        boolean login = securityManager.checkLogin("wBRQ*K02K6Ay");
        assertFalse("La password dovrebbe essere stata rimossa dal file", login);
    }
}




















