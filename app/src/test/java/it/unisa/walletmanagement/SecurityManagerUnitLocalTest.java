package it.unisa.walletmanagement;

import static org.junit.Assert.assertFalse;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import it.unisa.walletmanagement.Control.Impostazioni.SecurityManager;

@RunWith(RobolectricTestRunner.class)
public class SecurityManagerUnitLocalTest {

    SecurityManager securityManager;

    @Before
    public void init(){
        Context context = ApplicationProvider.getApplicationContext();
        //Context context = Mockito.mock(Context.class);
        securityManager = new SecurityManager(context);
        securityManager.doSavePassword("Password Segreta");
    }

    @After
    public void clear(){
        securityManager.doRemovePassword("Password Segreta");
    }

    @Test
    public void md5_CheckHash_False(){
        boolean check = SecurityManager.md5("Password Segreta11")
                .equals(SecurityManager.md5("password segreta11"));
        assertFalse("Gli hash dovrebbero essere diversi",  check);
    }
}
