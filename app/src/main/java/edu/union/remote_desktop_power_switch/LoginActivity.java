package edu.union.remote_desktop_power_switch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        login_button = (Button) findViewById(R.id.button);
        cancel_button = (Button) findViewById(R.id.button2);

        username_field = (EditText) findViewById(R.id.editText);
        password_field = (EditText) findViewById(R.id.editText2);

        attempts_counter_display = (TextView) findViewById(R.id.textView3);
        reset_attempt_counter();

        initTasks();
        checkTimer();
    }

   public void attemptLogin(View view) {
        if(username_field.getText().toString().equals("admin") && password_field.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(),getString(R.string.redirecting),Toast.LENGTH_SHORT).show();
            this.transitionToMainActivity(view);
        } else {
            login_counter--;
            check_login_attempts();
            update_attempt_counter();
        }
    }

    public void cancel(View view) {
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