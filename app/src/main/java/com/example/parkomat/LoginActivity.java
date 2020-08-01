package com.example.parkomat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    private String correctLogin = "admin";
    private String correctPassword = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText login = findViewById(R.id.loginBlank);
        final EditText password = findViewById(R.id.passwordBlank);
        Button loginButton = findViewById(R.id.loginButton);
        Button continueButton = findViewById(R.id.continueButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String adminLogin = login.getText().toString();
                String adminPassword = password.getText().toString();

                if (adminLogin.equals(correctLogin) && adminPassword.equals(correctPassword)) {
                    Intent User = new Intent(LoginActivity.this, AdminScreen.class);
                    startActivity(User);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Błąd");
                    builder.setMessage("Błędny login lub hasło");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Admin = new Intent(LoginActivity.this, MainScreen.class);
                startActivity(Admin);
            }
        });
    }
}
