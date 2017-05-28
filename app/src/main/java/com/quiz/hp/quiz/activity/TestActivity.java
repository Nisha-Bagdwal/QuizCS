package com.quiz.hp.quiz.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hp on 4/30/2017.
 */

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    RadioButton r1,r2,r3,r4;
    TextView ques,number,score,timer;
    ProgressDialog pDialog;
    Button submit,next;
    int scoreCount=0;
    int l=20;
    int i=1;
    String ansValue;
    String subject;
    CountDownTimer mycdt = new CountDownTimer(60000, 1000){
        public void onTick(long millisUntilFinished){
            timer.setText("Time left: "+String.valueOf(millisUntilFinished / 1000 +" secs"));
        }
        public  void onFinish(){
            timer.setText("Time left: 0 secs");
            calculate(ansValue);
            i++;
            if(i<=l) {
                loadQuestions(subject, i);
            }
            else{
                Intent intent = new Intent(TestActivity.this, ScoreActivity.class);
                Bundle bun=new Bundle();
                bun.putString("key", String.valueOf(scoreCount));
                intent.putExtras(bun);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        setVariables();
        Bundle bun=getIntent().getExtras();
        subject=bun.getString("key");

        setTitle(subject);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mycdt.cancel();
                calculate(ansValue);
                i++;
                if(i<=l) {
                    loadQuestions(subject, i);
                }
                else{
                    Intent intent = new Intent(TestActivity.this, ScoreActivity.class);
                    Bundle bun=new Bundle();
                    bun.putString("keyCount", String.valueOf(scoreCount));
                    bun.putString("keySubject", subject);
                    intent.putExtras(bun);
                    startActivity(intent);
                    finish();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mycdt.cancel();
                i++;
                if(i<=l) {
                    loadQuestions(subject, i);
                }
                else{
                    Intent intent = new Intent(TestActivity.this, ScoreActivity.class);
                    Bundle bun=new Bundle();
                    bun.putString("keyCount", String.valueOf(scoreCount));
                    bun.putString("keySubject", subject);
                    intent.putExtras(bun);
                    startActivity(intent);
                    finish();
                }
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        loadQuestions(subject,i);
    }

    private void setVariables() {
        number=(TextView)findViewById(R.id.textViewQuesNumber);
        ques=(TextView)findViewById(R.id.textViewQues);
        score=(TextView)findViewById(R.id.textViewScore);
        timer=(TextView)findViewById(R.id.textViewTimer);

        r1=(RadioButton)findViewById(R.id.radioButton1);
        r2=(RadioButton)findViewById(R.id.radioButton2);
        r3=(RadioButton)findViewById(R.id.radioButton3);
        r4=(RadioButton)findViewById(R.id.radioButton4);

        submit=(Button)findViewById(R.id.buttonSubmit);
        next=(Button)findViewById(R.id.buttonNext);
    }

    private void setQuestion(String test, String op1, String op2, String op3, String op4, String ans) {
        score.setText("Score: "+scoreCount);
        number.setText("Question: "+i);
        ques.setText(test);
        r1.setText(op1);
        r2.setText(op2);
        r3.setText(op3);
        r4.setText(op4);
        ansValue=ans;
        mycdt.start();
    }

    private void loadQuestions(final String subject, final int i) {

        String tag_string_req = "req_load";

        pDialog.setMessage("Loading question...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOADQUESTIONS, new Response.Listener<String>() {

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
                        String test=task.getString("ques");
                        String op1=task.getString("op1");
                        String op2=task.getString("op2");
                        String op3=task.getString("op3");
                        String op4=task.getString("op4");
                        String ans=task.getString("ans");

                        setQuestion(test,op1,op2,op3,op4,ans);
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
                params.put("subject", subject);
                params.put("id", String.valueOf(i));
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void calculate(String ans) {
        if(r1.isChecked()){
            if(ans.equals("1"))
                scoreCount+=4;
            else
                scoreCount--;
        }else if(r2.isChecked()){
            if(ans.equals("2"))
                scoreCount+=4;
            else
                scoreCount--;
        }else if(r3.isChecked()){
            if(ans.equals("3"))
                scoreCount+=4;
            else
                scoreCount--;
        }else if(r4.isChecked()){
            if(ans.equals("4"))
                scoreCount+=4;
            else
                scoreCount--;
        }
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
