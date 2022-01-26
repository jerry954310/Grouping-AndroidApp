package com.example.loginapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class feedback extends AppCompatActivity {


    Button send;
    EditText name;
    EditText department;
    EditText opinion;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        send=findViewById(R.id.send);
        name= findViewById(R.id.name);
        department=findViewById(R.id.department);
        opinion=findViewById(R.id.editText);
        email=findViewById(R.id.email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dp =department.getText().toString();
                String op =opinion.getText().toString();
                String em =email.getText().toString();

                if(em.equals("")||dp.equals("")||op.equals("")){

                    new AlertDialog.Builder(feedback.this)
                            .setTitle("訊息")
                            .setMessage("請輸入完整資料")
                            .setPositiveButton("ok",null)
                            .show();





                }else {

                    new AlertDialog.Builder(feedback.this)
                            .setTitle("訊息")
                            .setMessage("發送成功")
                            .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();



                }


            }
        });
    }

}

