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
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;

public class LoginActivity  extends AppCompatActivity {
    private static final int COUNTER_START_VALUE = 3;
    private static final int TIME_OUT_IN_MINUTES = 5;
    private static final int TIME_OUT_IN_MILLISECONDS = 60000;//300000;


    private Button login_button, cancel_button;

    private EditText username_field, password_field;
    private TextView attempts_counter_display;
    private int login_counter;
    private SharedPreferences prefs;
    private long timeLeft;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = (Button) findViewById(R.id.button2);
        username_field = (EditText) findViewById(R.id.editText);
        password_field = (EditText) findViewById(R.id.editText2);

        attempts_counter_display = (TextView) findViewById(R.id.textView3);
        reset_attempt_counter();

        initTasks();
        checkTimer();
    }

   public void attemptLogin(View view) {
       SharedPreferences prefs = getSharedPreferences("encryption", Context.MODE_PRIVATE);
       Boolean user_exists = prefs.getBoolean("user_exists", false);
       String encrypted_password = prefs.getString("password", "none");
       String encrypted_username = prefs.getString("username", "none");
       String password_salt = prefs.getString("password_salt", "none");
       String username_salt = prefs.getString("username_salt", "none");

       Log.d("user", user_exists.toString());
       Log.d("encrypted_pw", encrypted_password);
       Log.d("encrypted_username", encrypted_username);
       Log.d("pw_salt", password_salt);
       Log.d("username_salt", username_salt);

       String passed_password = password_field.getText().toString();
       String passed_username = username_field.getText().toString();

        if(!user_exists
           || encrypted_password.equals("none")
           || encrypted_username.equals("none")
           || password_salt.equals("none")
           || username_salt.equals("none")) {
            Toast.makeText(getApplicationContext(),"There are no credentials currently stored.",Toast.LENGTH_SHORT).show();
        } else if(passed_password.equals("") || passed_username.equals("")) {
            login_counter--;
            check_login_attempts();
            update_attempt_counter();
        } else {

            try {
                char[] passed_password_chars = passed_password.toCharArray();
                char[] passed_username_chars = passed_username.toCharArray();

                SecretKey passed_password_encrypted = EncryptionHelper.generateKey(passed_password_chars, password_salt.getBytes());
                SecretKey passed_username_encrypted = EncryptionHelper.generateKey(passed_username_chars, username_salt.getBytes());

                String passed_password_encrypted_string = new String(passed_password_encrypted.getEncoded());
                String passed_username_encrypted_string = new String(passed_username_encrypted.getEncoded());

                Log.d("passed_pw", passed_password_encrypted_string);
                Log.d("passed_username", passed_username_encrypted_string);

                if(passed_password_encrypted_string.equals(encrypted_password) && passed_username_encrypted_string.equals(encrypted_username)) {
                    Toast.makeText(getApplicationContext(),getString(R.string.redirecting),Toast.LENGTH_SHORT).show();
                    this.transitionToMainActivity(view);
                } else {
                    login_counter--;
                    check_login_attempts();
                    update_attempt_counter();
                }

            } catch (NoSuchAlgorithmException e) {
                Toast.makeText(getApplicationContext(),"Failure to encrypt credentials. Try again.",Toast.LENGTH_SHORT).show();
            } catch (InvalidKeySpecException e) {
                Toast.makeText(getApplicationContext(),"Failure to encrypt credentials. Try again.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void createNew(View view) {
        finish();
    }

    public void transitionToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initTasks() {
       prefs = getSharedPreferences("file", Context.MODE_PRIVATE);
    }

    private void checkTimer() {
        if (prefs.contains("time"))
            setTimer();
        else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("time", -1L);
            editor.apply();
        }
    }

    private void setTimer() {
        timeLeft = prefs.getLong("time", -1L);
        if (timeLeft != -1L)
            startTimer(timeLeft);
        else
            login_button.setEnabled(true);

    }

    private void startTimer(long time) {
        login_button.setEnabled(false);
        timer = new CountDownTimer(time, 1000) {

            @Override
            public void onFinish() {
                reset_attempt_counter();
                login_button.setEnabled(true);
                saveToPref(-1L);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //update UI, if required
                timeLeft = millisUntilFinished;
                saveToPref(timeLeft);
            }
        }.start();
    }

    private void saveToPref(long timeLeft){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("time", timeLeft);
        editor.apply();
    }

    private void reset_attempt_counter(){
        login_counter = COUNTER_START_VALUE;
        update_attempt_counter();
    }

    private void check_login_attempts(){
        if (login_counter <= 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.timeout, TIME_OUT_IN_MINUTES),Toast.LENGTH_SHORT).show();
            this.startTimer(TIME_OUT_IN_MILLISECONDS);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials),Toast.LENGTH_SHORT).show();
        }
    }

    private void update_attempt_counter(){
        if (login_counter <= 2 && login_counter >= 0) {
            attempts_counter_display.setBackgroundColor(getResources().getColor(R.color.red));
        } else if (login_counter >= 3) {
            attempts_counter_display.setBackgroundColor(getResources().getColor(R.color.green));
        }
        attempts_counter_display.setText(Integer.toString(login_counter));
    }

    protected void onResume() {
        super.onResume();
        password_field.setText("");
        username_field.setText("");
        reset_attempt_counter();
    }
}