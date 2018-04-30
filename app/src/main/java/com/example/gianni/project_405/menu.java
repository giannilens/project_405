package com.example.gianni.project_405;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class menu extends AppCompatActivity implements OnClickListener {

    private Button scanBtn, loginBtn;
    private TextView formatTxt, contentTxt;
    private TextView tvIsConnected;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        scanBtn = (Button) findViewById(R.id.scan_button);
        formatTxt = (TextView) findViewById(R.id.scan_format);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        loginBtn = (Button) findViewById(R.id.login_button);
        scanBtn.setOnClickListener(this);//makes programe listen to the click
        //loginBtn.setOnClickListener(sendMessage());//terug boven zetten alsz dit niet werkt
        loginBtn.setOnClickListener(this);
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }

    }


    public void onClick(View v) {
        //respond to clicks
        if (v.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();//vraagt om de app te downloaden als die nog niet is gedownload
        }
        if (v.getId() == R.id.login_button) {
            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult.getContents() != null) {//hier gaat die kijken of er een data zit in intentResult zit deze functie
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

   /* public void sendMessage(View view) {

        Intent intent = new Intent(this, login_activity.class);
        @SuppressLint("WrongViewCast") EditText editText = (EditText)findViewById(R.id.login_button);//
        String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);//geeft data mee met intent
        startActivity(intent);
    }*/

}


