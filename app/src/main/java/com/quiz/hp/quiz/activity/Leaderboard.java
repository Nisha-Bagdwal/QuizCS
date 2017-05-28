package com.quiz.hp.quiz.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hp on 5/3/2017.
 */

public class Leaderboard extends AppCompatActivity {

    private static final String TAG = Leaderboard.class.getSimpleName();
    RecyclerView mRecyclerView;
    ProgressDialog pDialog;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView tvuserTotalScore,tvuserRank;
    ArrayList<String> arrName=new ArrayList<String>();
    ArrayList<String> arrEmail=new ArrayList<String>();
    ArrayList<String> arrScore=new ArrayList<String>();
    ArrayList<String> arrRank=new ArrayList<String>();
    String email,test="xyz";
    int index=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        tvuserTotalScore=(TextView)findViewById(R.id.usertotalScoreLeaderboard);
        tvuserRank=(TextView)findViewById(R.id.userRankLeaderboard);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle bun=getIntent().getExtras();
        email=bun.getString("key");

        showLeaderboard(email);
    }

    private void showLeaderboard(final String email) {
        String tag_string_req = "req_load";

        pDialog.setMessage("Loading scores...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SHOWLEADERBOARD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user=jObj.getJSONObject("user");
                        int userScore=user.getInt("score");
                        int userRank=user.getInt("rank");

                        JSONArray task=jObj.getJSONArray("tasks");
                        for(int i=0;i<task.length();i++){
                            JSONObject tmp=task.getJSONObject(i);
                            String lbname= tmp.getString("name");
                            String lbemail=tmp.getString("email");
                            String lbscore=String.valueOf(tmp.getInt("score"));

                            setCardLB(lbname,lbemail,lbscore,i);
                            //Toast.makeText(getApplicationContext(),arrName.get(i)+arrEmail.get(i)+arrScore.get(i), Toast.LENGTH_LONG).show();
                        }

                        tvuserTotalScore.setText("Your score: "+String.valueOf(userScore));
                        tvuserRank.setText("Rank: "+String.valueOf(userRank));

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

    private void recyclerMethod() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_leaderboard);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapterLeaderboard(arrName,arrEmail,arrScore,arrRank);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setCardLB(String lbname, String lbemail, String lbscore, int i) {
        arrName.add(i,lbname);
        arrEmail.add(i,lbemail);
        arrScore.add(i,lbscore);
        arrRank.add(i,String.valueOf(i+1));
        test=lbscore;
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
