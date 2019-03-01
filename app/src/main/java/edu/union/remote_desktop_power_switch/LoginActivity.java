package edu.union.remote_desktop_power_switch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity  extends AppCompatActivity {
    private static final int COUNTER_START_VALUE = 3;

    Button b1,b2;
    EditText ed1,ed2;
    TextView tx1;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);

        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);

        counter = COUNTER_START_VALUE;

        tx1 = (TextView) findViewById(R.id.textView3);
        tx1.setBackgroundColor(getResources().getColor(R.color.green));
        tx1.setText(Integer.toString(counter));
    }

   public void attemptLogin(View view) {
        if(ed1.getText().toString().equals("admin") && ed2.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(),getString(R.string.redirecting),Toast.LENGTH_SHORT).show();
            this.transitionToMainActivity(view);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials),Toast.LENGTH_SHORT).show();
            counter--;
            tx1.setText(Integer.toString(counter));
            if (counter == 2) {
                tx1.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (counter == 0) {
                b1.setEnabled(false);
            }
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void transitionToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}