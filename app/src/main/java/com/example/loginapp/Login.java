package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button LoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NeedNewAccountlink, forgotTextLink;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        NeedNewAccountlink = (TextView) findViewById(R.id.createText);
        UserEmail = (EditText) findViewById(R.id.email);
        UserPassword = (EditText) findViewById(R.id.password);
        LoginButton = (Button) findViewById(R.id.loginBtn);
        forgotTextLink = (TextView) findViewById(R.id.forgotTextLink);

        NeedNewAccountlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegister();
            }

            private void SendUserToRegister() {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
                finish();//解決上一頁問題
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AllowingUserToLogin();
            }


            private void AllowingUserToLogin() {
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
                else{
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        SendUserToMainActivity();

                                        Toast.makeText(Login.this, "登入成功", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(Login.this, "錯誤" + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }



        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("重設密碼");
                passwordResetDialog.setMessage("輸入你的信箱以取得重設密碼連結");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "重設連結已寄至信箱", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "ERROR!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });
                passwordResetDialog.create().show();

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
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
