
package com.example.indianpincode;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {
    private EditText inputEdt;
    private Button getInfoBtn;
    private TextView detailsTxt;

    String pinCode;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        inputEdt = findViewById(R.id.inputEdt);
        getInfoBtn = findViewById(R.id.getInfoBtn);
        detailsTxt = findViewById(R.id.detailsTxt);

        mRequestQueue = Volley.newRequestQueue(Dashboard.this);

        getInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode = inputEdt.getText().toString();


                if (TextUtils.isEmpty(pinCode)) {
                    Toast.makeText(Dashboard.this, "Empty value!", Toast.LENGTH_SHORT).show();
                } else {
                    getDataFromPinCode(pinCode);
                }
            }
        });
    }

    private void getDataFromPinCode(String pinCode) {

        mRequestQueue.getCache().clear();
        

        String url = "https://api.postalpincode.in/pincode/" + pinCode;

        RequestQueue queue = Volley.newRequestQueue(Dashboard.this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                    if (response.getString("Status").equals("Error")) {
                        detailsTxt.setText("Pin code is not valid.");
                    }
                    else {
                        JSONObject obj = postOfficeArray.getJSONObject(0);

                        // inside json array getting district name,
                        // state and country from data.
                        String district = obj.getString("District");
                        String state = obj.getString("State");
                        String country = obj.getString("Country");

                        detailsTxt.setText("Details of pin code is : \n" + "District is : " + district + "\n" + "State : "
                                + state + "\n" + "Country : " + country);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dashboard.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                detailsTxt.setText("Pin code is not valid");
            }
        });
        queue.add(objectRequest);
    }
}
