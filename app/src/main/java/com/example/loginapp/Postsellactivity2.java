package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Postsellactivity2 extends AppCompatActivity {
    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;
    private ProgressDialog loadingBar;
    private TextView BackBtn2;

    private static final int Gallery_pick = 1;
    private Uri ImageUri;
    private String Description;

    private StorageReference PostImageReference;
    private DatabaseReference userRef, Postref2;
    private FirebaseAuth mAuth;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String postRandomName;
    private String downloadurl;
    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsellactivity2);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostImageReference = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Postref2 = FirebaseDatabase.getInstance().getReference().child("Posts2");

        SelectPostImage = (ImageButton) findViewById(R.id.imageButton82);
        UpdatePostButton = (Button) findViewById(R.id.updateBtn2);
        PostDescription = (EditText) findViewById(R.id.editText2);
        BackBtn2 = (TextView) findViewById(R.id.backBtn2);

        loadingBar = new ProgressDialog(this);

        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

        BackBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToSellActivity2();
            }
        });

    }

    private void ValidatePostInfo() {
        Description = PostDescription.getText().toString();

        if(ImageUri == null){
            Toast.makeText(Postsellactivity2.this, "請選取圖片...", Toast.LENGTH_SHORT).show() ;
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(Postsellactivity2.this, "請寫點東西...", Toast.LENGTH_SHORT).show() ;
        }
        else{

            loadingBar.setTitle("上傳貼文");
            loadingBar.setMessage("上傳貼文中，請稍後...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage() {

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        final StorageReference filepath = PostImageReference.child("Post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        //add file on Firebase and got Download Link
        filepath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                    //downloadurl = filepath.getDownloadUrl();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    downloadurl = task.getResult().toString();
                    Toast.makeText(Postsellactivity2.this, "圖片上傳成功...", Toast.LENGTH_SHORT).show();

                    SavingPostInformationToDatabase();
                    //Log.d(TAG,"OnSuccess: uri = " + downUri.toString());
                }
                else{
                    String message = task.getException().getMessage();
                    Toast.makeText(Postsellactivity2.this, "錯誤..." + message, Toast.LENGTH_SHORT).show() ;
                }
            }
        });

        /*filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                    //downloadurl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                    downloadurl = task.getResult();
                    Toast.makeText(Postsellactivity.this, "圖片上傳成功...", Toast.LENGTH_SHORT).show();

                    SavingPostInformationToDatabase();
                }
                else{
                    String message = task.getException().getMessage();
                    Toast.makeText(Postsellactivity.this, "錯誤..." + message, Toast.LENGTH_SHORT).show() ;
                }
            }
        });*/
    }

    private void SavingPostInformationToDatabase() {
        userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String username = dataSnapshot.child("username").getValue().toString();
                    //String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();

                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("postimage", downloadurl);
                    postsMap.put("username", username);
                    //postsMap.put("profileimage", userProfileImage);
                    Postref2.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        SendUserToSellActivity2();
                                        Toast.makeText(Postsellactivity2.this, "新貼文上傳成功", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else{
                                        Toast.makeText(Postsellactivity2.this, "錯誤...", Toast.LENGTH_SHORT).show() ;
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToSellActivity2() {
        Intent mainIntent = new Intent(Postsellactivity2.this, Post_Sell2.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_pick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
    }
}

