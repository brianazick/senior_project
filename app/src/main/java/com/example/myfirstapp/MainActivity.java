package com.example.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ResponseHandler;

public class MainActivity extends AppCompatActivity {
    private boolean power_state;
    private String power_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        power_state = false;

        updatePowerString();
        updateMainTextDisplay();
    }

    // Called when the user taps the send button
    public void sendMessage(View view) {
        power_state = !power_state;
        updatePowerString();
        updateMainTextDisplay();
        sendParticleSignal();
    }

    private void updatePowerString(){
        if(power_state) {
            power_string = "The computer is in the on state.";
        } else {
            power_string = "The computer is in the off state.";
        }

    }

    private void updateMainTextDisplay(){
        TextView textView = findViewById(R.id.textView4);
        textView.setText(power_string);
    }

    private void sendParticleSignal(){
        RequestParams params = new RequestParams();
        params.put("access_token", "013b79d222f5e7dd12698c8ad79684755d8468cf");
        params.put("arg", "power_button");

        JsonHttpResponseHandler http_response = new JsonHttpResponseHandler();

        ParticleRestClient.post("", params,  new JsonHttpResponseHandler(){
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
}
