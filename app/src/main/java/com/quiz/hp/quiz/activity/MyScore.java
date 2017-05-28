package com.quiz.hp.quiz.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quiz.hp.quiz.R;
import com.quiz.hp.quiz.app.AppConfig;
import com.quiz.hp.quiz.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hp on 5/3/2017.
 */

public class MyScore extends AppCompatActivity {

    private static final String TAG = MyScore.class.getSimpleName();
    RecyclerView mRecyclerView;
    ProgressDialog pDialog;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String email,xalgo,xcd,xcn,xco,xdatas,xdbms,xdiss,xflat,xmicro,xos,xpl,xscore;
    TextView cvTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscores);

        cvTotal=(TextView)findViewById(R.id.myScoreTotalScoreValue);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle bun=getIntent().getExtras();
        email=bun.getString("key");
        loadScores(email);
    }

    private void loadScores(final String email) {
        String tag_string_req = "req_load";

        pDialog.setMessage("Loading scores...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOADSCORES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject task=jObj.getJSONObject("tasks");
                        int algo=task.getInt("algo");
                        int cd=task.getInt("cd");
                        int cn=task.getInt("cn");
                        int co=task.getInt("co");
                        int datas=task.getInt("datas");
                        int dbms=task.getInt("dbms");
                        int diss=task.getInt("diss");
                        int flat=task.getInt("flat");
                        int micro=task.getInt("micro");
                        int os=task.getInt("os");
                        int pl=task.getInt("pl");
                        int score=task.getInt("score");

                        cvTotal.setText(String.valueOf(score));
                        setScores(algo,cd,cn,co,datas,dbms,diss,flat,micro,os,pl);
                        recyclerMethod();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                //Toast.makeText(*****getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                hideDialog();
                finish();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setScores(int algo, int cd, int cn, int co, int datas, int dbms, int diss, int flat, int micro, int os, int pl) {
        xalgo=String.valueOf(algo);
        xcd=String.valueOf(cd);
        xcn=String.valueOf(cn);
        xco=String.valueOf(co);
        xdatas=String.valueOf(datas);
        xdbms=String.valueOf(dbms);
        xdiss=String.valueOf(diss);
        xflat=String.valueOf(flat);
        xmicro=String.valueOf(micro);
        xos=String.valueOf(os);
        xpl=String.valueOf(pl);
    }

    private void recyclerMethod() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_myscores);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapterMyScore(getDataSet(),getScoreSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<String> getScoreSet() {
        ArrayList results = new ArrayList<String>();
        results.add(0,xalgo);
        results.add(1,xcd);
        results.add(2,xcn);
        results.add(3,xco);
        results.add(4,xdatas);
        results.add(5,xdbms);
        results.add(6,xdiss);
        results.add(7,xflat);
        results.add(8,xmicro);
        results.add(9,xos);
        results.add(10,xpl);
        return results;
    }

    private ArrayList<String> getDataSet() {
        ArrayList results = new ArrayList<String>();
        results.add(0,"Algorithms");
        results.add(1,"Compiler Design");
        results.add(2,"Computer Networks");
        results.add(3,"Computer Organization");
        results.add(4,"Data Structures");
        results.add(5,"DBMS");
        results.add(6,"Discrete Structures");
        results.add(7,"FLAT");
        results.add(8,"Microprocessor");
        results.add(9,"Operating Systems");
        results.add(10,"Programming Languages");
        return results;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
