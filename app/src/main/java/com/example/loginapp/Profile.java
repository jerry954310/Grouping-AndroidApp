package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    private EditText Username, Fullname, phonenumber;
    private Button SaveInformationbutton;

    private RadioButton boy;

    private RadioGroup group;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    private ImageView photo;
    String currentUserId;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);






        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        Username = (EditText) findViewById(R.id.Username);
        Fullname = (EditText) findViewById(R.id.Fullname);
        phonenumber = (EditText) findViewById(R.id.phone_number);
        SaveInformationbutton = (Button) findViewById(R.id.ProfileBtn);

        SaveInformationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null){
            Uri ImageUri = data.getData();

        }
    }

    private void SaveAccountSetupInformation() {
        String username = Username.getText().toString();
        String fullname = Fullname.getText().toString();
        String phone = phonenumber.getText().toString();

        if(TextUtils.isEmpty(username)){
            Username.setError("你必須輸入暱稱");
            return;
        }
        if(TextUtils.isEmpty(fullname)){
            Username.setError("你必須輸入全名");
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Username.setError("你必須輸入電話");
            return;
        }
        else{
            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("phonenumber", phone);
            userMap.put("status", "你好，我正在使用grouping");
            userMap.put("profileimage", "*****");
            userMap.put("gender", "none");
            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(Profile.this, "個人資料創立成功", Toast.LENGTH_LONG).show() ;
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(Profile.this, "錯誤"+ message, Toast.LENGTH_LONG).show() ;

                    }
                }
            });


        }


    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(Profile.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
