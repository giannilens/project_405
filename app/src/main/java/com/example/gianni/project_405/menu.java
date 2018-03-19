package com.example.gianni.project_405;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

public class menu extends AppCompatActivity implements OnClickListener {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";//need to figure out why
    private Button scanBtn,loginBtn;
    private TextView formatTxt, contentTxt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        loginBtn = (Button)findViewById(R.id.login_button);
        scanBtn.setOnClickListener(this);//makes programe listen to the click
        //loginBtn.setOnClickListener(sendMessage());//terug boven zetten alsz dit niet werkt
        loginBtn.setOnClickListener(this);

    }


    public void onClick(View v){
    //respond to clicks
        if(v.getId()==R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();//vraagt om de app te downloaden als die nog niet is gedownload
        }
        else if (v.getId()==R.id.login_button){
            Intent intent = new Intent(this, login_activity.class);
            EditText editText = (EditText) findViewById(R.id.login_button);//
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);//geeft data mee met intent
            startActivity(intent);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {//hier gaat die kijken of er een data zit in intentResult zit deze functie
        //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

      public void sendMessage(View view) {

      Intent intent = new Intent(this, login_activity.class);
      EditText editText = (EditText) findViewById(R.id.login_button);//
      String message = editText.getText().toString();
      intent.putExtra(EXTRA_MESSAGE, message);//geeft data mee met intent
      startActivity(intent);
    }

}


