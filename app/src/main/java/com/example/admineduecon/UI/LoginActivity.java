package com.example.admineduecon.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admineduecon.R;
import com.example.admineduecon.SharedPref.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private SharedPrefManager sharedPrefManager;
    private EditText etUsername;
    private EditText etPassword;
    private ImageButton btnLogin;
    private ProgressBar pbLogin;
    private ImageView ivLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        sharedPrefManager = new SharedPrefManager(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnSignIn);
        pbLogin = findViewById(R.id.pBLogin);
        ivLogin = findViewById(R.id.ivLogin);

        login();
    }

    private void login() {
        btnLogin.setOnClickListener(v -> {
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            pbLogin.setVisibility(View.VISIBLE);
            ivLogin.setVisibility(View.GONE);

            if (username.isEmpty() || password.isEmpty()){
                pbLogin.setVisibility(View.GONE);
                ivLogin.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Harap isi semua!", Toast.LENGTH_SHORT).show();
            }else{
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String spUsername = sharedPrefManager.getUsername();
                        String spPassword = sharedPrefManager.getPassword();

                        Log.d("username","user"+username);
                        Log.d("password","pass"+password);

                        if (username.equals(spUsername) && password.equals(spPassword)){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            Toast.makeText(LoginActivity.this, "Hai Admin!", Toast.LENGTH_SHORT).show();
                            sharedPrefManager.saveIsLogin(true);
                            finishAffinity();
                            startActivity(i);
                        }else {
                            pbLogin.setVisibility(View.GONE);
                            ivLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Username dan password salah!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);
            }
        });
    }
}