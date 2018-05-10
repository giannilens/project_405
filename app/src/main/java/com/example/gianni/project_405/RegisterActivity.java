package com.example.gianni.project_405;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gianni.project_405.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.gianni.project_405.menu.user;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    static TextInputEditText textInputEditTextName;
    static TextInputEditText textInputEditTextEmail;
    static TextInputEditText textInputEditTextPassword;
    static TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;


    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }



    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                if(inputvalidation()&& password_check()) {
                    new register_async().execute("https://project.vangehugten.org/listener_register.php");
                }else if(!inputvalidation()){
                Toast.makeText(getBaseContext(), "fill in everything", Toast.LENGTH_LONG).show();
                }else{
                Toast.makeText(getBaseContext(), "passwords don't match", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }


    private boolean inputvalidation(){
        if(textInputEditTextName.getText().toString().trim() == ""){
            return false;
        }else if(textInputEditTextEmail.getText().toString().trim() == ""){
            return false;
        }else if((textInputEditTextPassword.getText().toString().trim() == "")||(textInputEditTextConfirmPassword.getText().toString().trim() == "")){
            return false;
        }
        return true;
    }
    private boolean password_check(){
        if(textInputEditTextConfirmPassword.getText().toString().trim()!=textInputEditTextPassword.getText().toString().trim()){
            return  false;
        }
        return true;
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
    public String POST_register(String url, TextInputEditText _email,TextInputEditText _password,TextInputEditText _name ){
        InputStream inputStream = null;
        String result = "";
        String name= _name.getText().toString().trim();
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
            data.accumulate("name", name);
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
                String id =jsoninput.getString("id");

                result="email already exists";
            } else {
                result = "didn't work";
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "registered";
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
    private class register_async extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            return POST_register(urls[0],textInputEditTextEmail,textInputEditTextPassword,textInputEditTextPassword);
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
