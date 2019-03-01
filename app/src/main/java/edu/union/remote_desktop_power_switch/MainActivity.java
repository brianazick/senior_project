package edu.union.remote_desktop_power_switch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private boolean power_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getParticlePowerStatus();
    }

    public void updatePowerStatus(View view) { getParticlePowerStatus(); }


    // Called when the user taps the power button
    public void sendPowerSignal(View view) {
        sendParticleSignal();
        getParticlePowerStatus();
    }

    private void updatePowerVars(){
        View button = findViewById(R.id.button2);
        TextView textView = findViewById(R.id.textView4);

        if(power_state) {
            textView.setText(R.string.power_on_string);
            button.setBackgroundResource(R.drawable.green);
        } else {
            textView.setText(R.string.power_off_string);
            button.setBackgroundResource(R.drawable.red);
        }
    }

    private void setPower_state(boolean new_power_state){
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
                    updatePowerVars();
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
}
