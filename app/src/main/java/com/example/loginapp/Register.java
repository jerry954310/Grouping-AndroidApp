package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText UserFullname, UserEmail, UserPassword;
    private Button CreateAccountButton;
    private TextView HasAccountlink;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.email);
        UserPassword = (EditText) findViewById(R.id.password);
        UserFullname = (EditText) findViewById(R.id.Fullname);
        CreateAccountButton = (Button) findViewById(R.id.RegisterBtn);
        HasAccountlink = (TextView) findViewById(R.id.createText);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }

            private void CreateNewAccount() {
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    UserEmail.setError("你必須輸入email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    UserPassword.setError("你必須輸入密碼");
                    return;
                }
                if(password.length() < 6){
                    UserPassword.setError("密碼長度需大於6");
                    return;
                }
                else{
                    loadingBar.setTitle("帳號註冊中");
                    loadingBar.setMessage("帳號註冊中，請稍後...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        SendUserToProfile();

                                        Toast.makeText(Register.this, "用戶創立成功", Toast.LENGTH_SHORT).show() ;
                                        loadingBar.dismiss();
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(Register.this, "錯誤! " + message, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }

            }

            private void SendUserToProfile() {
                Intent profileIntent = new Intent(Register.this, Profile.class);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(profileIntent);
                finish();
            }

        });

        HasAccountlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLogin();
            }

            private void SendUserToLogin() {
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
                finish();//解決上一頁問題
            }
        });


    }
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            SendUserToMainActivity();
        }
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(Register.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}