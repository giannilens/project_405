package com.example.gianni.project_405;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.gianni.project_405.model.User;

import static com.example.gianni.project_405.menu.user;


public class login_activity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = login_activity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    static TextInputEditText textInputEditTextEmail;
    static TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide(); // check of dit er voor zorgt dat de pijl terug weg is

        initViews();
        initListeners();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                 new login_async().execute("https://project.vangehugten.org/listener.php");
                //verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }


    /**
     * This method is to empty all input edit text
     * if we add reset button
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

    public String POST_login(String url, TextInputEditText _email,TextInputEditText _password){
        InputStream inputStream = null;
        String result = "";
        String passwd = _password.getText().toString().trim();
        String email = _email.getText().toString().trim().toLowerCase();
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject data = new JSONObject();
            data.accumulate("email", email);
            data.accumulate("password", passwd);
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
            Log.d("response", String.valueOf(inputStream));

            // 10. convert inputstream to string
            if(inputStream != null ) {
                String Return = convertInputStreamToString(inputStream);
                Log.d("input", Return);
                JSONObject jsoninput  = new JSONObject(Return);
                user.setId(jsoninput.getString("id"));
                user.setName(jsoninput.getString("name"));
                Log.d("luser",user.getId());
                Log.d("luser",user.getName());
                result="login succes";
            } else {
                result = "false user combo";
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "false user combo";
        }

        // 11. return result
        // Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        //Log.d("user",String.valueOf(menu.user.getId()));
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
    private class login_async extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            return POST_login(urls[0],textInputEditTextEmail,textInputEditTextPassword);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            if(result != "false user combo") {
                emptyInputEditText();
                Intent intent = new Intent(getApplicationContext(), menu.class);
                startActivity(intent);
            }

        }
    }

}
