package com.example.loginapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

public class about_us extends AppCompatActivity {



    CardView motivation;
    CardView production;
    CardView future;
    CardView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);



        motivation=findViewById(R.id.motivation);
        future=findViewById(R.id.future);
        production=findViewById(R.id.production);
        setting=findViewById(R.id.setting);


        motivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog a= new AlertDialog.Builder(about_us.this)
                        .setTitle("研究動機")
                        .setMessage("•資訊太雜亂加上發文常被洗掉\n•缺乏整合的平台供中正學生使用" +
                                "\n•臉書貼文無法觸及目標對象")
                        .setPositiveButton("了解了",null)
                        .show();





            }
        });

        future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(about_us.this)
                        .setTitle("開發目標")
                        .setMessage("•快速解決學生日常所需\n•取代現有的多個臉書社團" +
                                "\n•成為學生團購的首選平台")
                        .setPositiveButton("了解了",null)
                        .show();


            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(about_us.this)
                        .setTitle("未來展望")
                        .setMessage("•美食地圖(外帶功能)\n•叫車服務(與車隊合作)" +
                                "\n•簡易學校活動查詢\n•篩選功能")
                        .setPositiveButton("了解了",null)
                        .show();

            }
        });

        production.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(about_us.this)
                        .setTitle("製作團隊")
                        .setMessage("資管四陳新" +"\n資管四陳俊諺"+
                                "\n資管三林哲漳"+"\n資管三宋宜蓁")
                        .setPositiveButton("了解了",null)
                        .show();

            }
        });


    }



    }

