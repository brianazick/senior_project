package com.example.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ResponseHandler;

public class MainActivity extends AppCompatActivity {
    private boolean power_state;
    private String power_string;
    private int power_color;

    private final static String power_on_string = "The computer is in the on state.";
    private final static String power_off_string = "The computer is in the off state.";
    private final static int power_off_color = R.color.red;
    private final static int power_on_color = R.color.green;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PowerStatusUpdate();

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);
    }

    private void TimerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            PowerStatusUpdate();
        }
    };


    // Called when the user taps the send button
    public void sendMessage(View view) {
        sendParticleSignal();
    }

    private void updatePowerString(){
        if(power_state) {
            power_string = power_on_string;
        } else {
            power_string = power_off_string;
        }
    }

    private void updatePowerColor(){
        if(power_state) {
            power_color = power_on_color;
        } else {
            power_color = power_off_color;
        }
    }

    private void updatePowerImage(){
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(power_color);
    }

    private void updateMainTextDisplay(){
        TextView textView = findViewById(R.id.textView4);
        textView.setText(power_string);
    }

    public void setPower_state(boolean new_power_state){
        power_state = new_power_state;
    }

    private void sendParticleSignal(){
        RequestParams params = new RequestParams();
        params.put("access_token", "013b79d222f5e7dd12698c8ad79684755d8468cf");
        params.put("arg", "power_button");

        String additional_url = "mosfet";

        ParticleRestClient.post(additional_url, params,  new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("ws", "---->>onSuccess JSONObject:" + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("ws", "---->>onSuccess JSONArray");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                Log.i("ws", "---->>onSuccess responseString");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("ws", "---->>onFailure:" + throwable.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        } );
    }

    private void getParticlePowerStatus(){
        String additional_url = "power_state?access_token=013b79d222f5e7dd12698c8ad79684755d8468cf";

        ParticleRestClient.get(additional_url, null,  new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    setPower_state(response.getBoolean("result"));
                } catch (JSONException e) {
                    Log.e("ws", "Getting power status boolean from http_response failed" + e.toString());
                }
                Log.i("ws", "---->>onSuccess JSONObject:" + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("ws", "---->>onSuccess JSONArray");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                Log.i("ws", "---->>onSuccess responseString");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("ws", "---->>onFailure:" + throwable.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        } );
    }

    public void PowerStatusUpdate(){
        getParticlePowerStatus();
        updatePowerString();
        updatePowerColor();
        updateMainTextDisplay();
        updatePowerImage();
    }
}
