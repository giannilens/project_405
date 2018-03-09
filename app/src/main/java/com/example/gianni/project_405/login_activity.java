package com.example.gianni.project_405;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class login_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        Intent intent = getIntent();
        String message = intent.getStringExtra(menu.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }
}
