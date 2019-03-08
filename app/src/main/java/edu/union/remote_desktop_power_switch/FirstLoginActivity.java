package edu.union.remote_desktop_power_switch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static edu.union.remote_desktop_power_switch.EncryptionHelper.generateKey;

public class FirstLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

//        SharedPreferences prefs = this.getPreferences("encryption", Context.MODE_PRIVATE);
//
//        if (prefs.getBoolean("user_exists", false)) {
//            this.transitionToLoginActivity();
//        }
    }

   public void attemptCreate(View view) {
       EditText username_field = (EditText) findViewById(R.id.editText);
       EditText password_field = (EditText) findViewById(R.id.editText2);
       EditText password_confirm_field = (EditText) findViewById(R.id.editText3);

       String username_string = username_field.getText().toString();
       String password_string = password_field.getText().toString();
       String password_confirm_string = password_confirm_field.getText().toString();

        if(password_string.equals(password_confirm_string)) {
            try {
                SecretKey generated_username_salt = EncryptionHelper.generateKey();
                SecretKey generated_password_salt = EncryptionHelper.generateKey();

                String password_salt_string = new String(generated_username_salt.getEncoded());
                String username_salt_string = new String(generated_password_salt.getEncoded());

                char[] password_chars = password_string.toCharArray();
                char[] username_chars = username_string.toCharArray();

                SecretKey encrypted_password = EncryptionHelper.generateKey(password_chars, password_salt_string.getBytes());
                SecretKey encrypted_username = EncryptionHelper.generateKey(username_chars, username_salt_string.getBytes());

                String encrypted_password_string = new String(encrypted_password.getEncoded());
                String encrypted_username_string = new String(encrypted_username.getEncoded());

                SharedPreferences.Editor e = getSharedPreferences("encryption", Context.MODE_PRIVATE).edit();

                e.putBoolean("user_exists", true);
                e.putString("password", encrypted_password_string);
                e.putString("username", encrypted_username_string);
                e.putString("password_salt", password_salt_string);
                e.putString("username_salt", username_salt_string);
                e.commit();

                Toast.makeText(getApplicationContext(),getString(R.string.redirecting),Toast.LENGTH_SHORT).show();
                this.transitionToLoginActivity();
            } catch (NoSuchAlgorithmException e) {
                Toast.makeText(getApplicationContext(),"Failure to encrypt credentials. Try again.",Toast.LENGTH_SHORT).show();
            } catch (InvalidKeySpecException e) {
                Toast.makeText(getApplicationContext(),"Failure to encrypt credentials. Try again.",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),getString(R.string.bad_pws),Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void transitionToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}