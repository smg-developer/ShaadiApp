package com.challenge.shaadiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.challenge.shaadiapp.adapters.UserListAdapter;
import com.challenge.shaadiapp.helpers.DatabaseOpenHelper;
import com.challenge.shaadiapp.helpers.Utility;
import com.challenge.shaadiapp.modal.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String request_url = "https://randomuser.me/api/?results=10";

    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter userRCAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    TextView tvNoData;

    DatabaseOpenHelper dbOpenHelper;

    Cursor cursor;
    ArrayList<UserInfo> arrUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNoData = findViewById(R.id.tv_no_data_available);
        userRecyclerView = (RecyclerView) findViewById(R.id.rc_view_users);

        userLayoutManager = new LinearLayoutManager(this);

        dbOpenHelper = new DatabaseOpenHelper(this);
        dbOpenHelper.open();
        cursor = dbOpenHelper.fetchInfo();

        //Check Internet connection and load Data if network available.
        if(isNetworkAvailable(MainActivity.this)) {
            loadData();
        }
        //Check if database has data if there is no internet.. So load data from database
        else {
            loadDataFromCursor();
        }

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void loadData() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, request_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: "+response);

                processDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG,"Error :" + error.toString());
                loadDataFromCursor();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    public void processDataResponse(String responseString){
        try {
            JSONObject jObjectResponse = new JSONObject(responseString);
            if(jObjectResponse.has("results")) {
                JSONArray jArrUsers = jObjectResponse.getJSONArray("results");

                arrUsers = Utility.parseUserList(jArrUsers);

                if(arrUsers.size() == 0){
                    loadDataFromCursor();
                }
                else {
                    userRecyclerView.setLayoutManager(userLayoutManager);
                    userRCAdapter = new UserListAdapter(arrUsers, MainActivity.this);
                    userRecyclerView.setAdapter(userRCAdapter);

                    //Also add data to local database for future reference
                    for (int pos = 0; pos< arrUsers.size(); pos++) {
                        dbOpenHelper.insert(arrUsers.get(pos));
                    }
                }
            }
            else {
                loadDataFromCursor();
            }

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void loadDataFromCursor(){

        //Check if cursor has data
        if(cursor != null) {

            arrUsers = Utility.fetchInfoFromCursor(cursor);

            if(arrUsers.size() == 0){
                userRecyclerView.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
            else {
                userRecyclerView.setLayoutManager(userLayoutManager);
                userRCAdapter = new UserListAdapter(arrUsers, MainActivity.this);
                userRecyclerView.setAdapter(userRCAdapter);
            }
            cursor.close();
        }
        //No network available... and no data in database.
        else {
            userRecyclerView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbOpenHelper.close();
    }

    public void updateStatusForUser(String u_name, String status){
        dbOpenHelper.updateStatus(u_name, status);
    }
}