package com.example.gianni.project_405;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gianni.project_405.model.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class menu extends AppCompatActivity implements OnClickListener {

    private Button scanBtn, loginBtn;
    private TextView formatTxt, contentTxt;
    private TextView appIsConnected;
    public String scanContent ;
    public static User user = new User();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        scanBtn = (Button) findViewById(R.id.scan_button);
        formatTxt = (TextView) findViewById(R.id.scan_format);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        loginBtn = (Button) findViewById(R.id.login_button);
        appIsConnected = (TextView) findViewById(R.id.AppIsConnected);
        scanBtn.setOnClickListener(this);//makes programe listen to the click
        //loginBtn.setOnClickListener(sendMessage());//terug boven zetten alsz dit niet werkt
        loginBtn.setOnClickListener(this);
        if(isConnected()){//kijkt of je internet verbinding hebt
            appIsConnected.setBackgroundColor(0xFF00CC00);
            appIsConnected.setText("You are conncted");
        }
        else{
            appIsConnected.setText("You are NOT conncted");
        }
        if(user.getName()!=null){
            loginBtn.setText(user.getName()+" logout");
        }
        if (user.getId()==null){//als je niet ingelogd bent is er geen scan button te zein
            scanBtn.setVisibility(View.GONE);
        }

    }


    public void onClick(View v) {//opent dit als er op een button wordt geklikt en checked het id van die button
        //respond to clicks
        if (v.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);//maakt een object aan van de intentIntegratior class
            scanIntegrator.initiateScan();//opent de scanner
        }
        if (v.getId() == R.id.login_button) {
            if(user.getName()!=null) {//kijkt of er al een user is anders logt die uit
                user.setnull();
                finish();
                startActivity(getIntent());
            }
            else {//als er nog geen user is dan gaat hij naar de login pagina
                Intent intent = new Intent(this, login_activity.class);
                startActivity(intent);
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);//de barcode en type wordt oppgeslagen in de intentResult classe
        if (scanningResult.getContents() != null) {//hier gaat die kijken of er een data zit in intentResult zit deze functie
            //we have a result
            scanContent = scanningResult.getContents();//de barcode wordt "public opgeslagen"
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);//data wordt getoont op het scerm
            contentTxt.setText("CONTENT: " + scanContent);

            new HttpAsyncTask().execute("https://project.vangehugten.org/listener_barcode.php"); //de functie voor de http json comunicatie wordt opgestart

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);//hier haalt die de connectivity status up
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    public String POST_barcode(String url, String scanContent){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject data = new JSONObject();
            data.accumulate("user_id", user.getId());
            data.accumulate("barcode", scanContent);
            // url where the data will be posted




            // 4. convert JSONObject to JSON to String
            json = data.toString();
            Log.d("json", json  );

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.d("string",se.toString());

            // 6. set httpPost Entity
           httpPost.setEntity(se);


            // 7. Set some headers to inform server about the type of the content
           // httpPost.setHeader("Accept", "application/json");
            //httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("url", String.valueOf(httpPost.getURI()));

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
       // Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null){
            result += line;
            }
        inputStream.close();
        return result;

    }


   final public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST_barcode(urls[0], scanContent);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


   /* public void sendMessage(View view) {

        Intent intent = new Intent(this, login_activity.class);
        @SuppressLint("WrongViewCast") EditText editText = (EditText)findViewById(R.id.login_button);//
        String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);//geeft data mee met intent
        startActivity(intent);
    }*/

}


