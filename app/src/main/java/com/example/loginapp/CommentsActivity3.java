package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity3 extends AppCompatActivity {

    private RecyclerView CommentsList;
    private ImageButton PostCommentButton;
    private EditText CommentInputText;

    private DatabaseReference UsersRef, PostsRef1;
    private FirebaseAuth mAuth;

    private String Post_Key, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments3);

        Post_Key = getIntent().getExtras().get("PostKey").toString();

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef1 = FirebaseDatabase.getInstance().getReference().child("Posts3").child(Post_Key).child("Comments");

        CommentsList = (RecyclerView) findViewById(R.id.comment_list3);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);

        CommentInputText = (EditText) findViewById(R.id.comment_input3);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_btn3);

        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String userName = dataSnapshot.child("username").getValue().toString();

                            ValidateComment(userName);

                            CommentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            private void ValidateComment(String userName) {
                String commentText = CommentInputText.getText().toString();

                if(TextUtils.isEmpty(commentText)){
                    Toast.makeText(CommentsActivity3.this, "請寫點東西...", Toast.LENGTH_SHORT).show();

                }
                else{
                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
                    final String saveCurrentDate = currentDate.format(calFordDate.getTime());

                    Calendar calFordTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    final String saveCurrentTime = currentTime.format(calFordDate.getTime());

                    final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime;

                    HashMap commentsMap = new HashMap();
                    commentsMap.put("uid", current_user_id);
                    commentsMap.put("comment", commentText);
                    commentsMap.put("date", saveCurrentDate);
                    commentsMap.put("time", saveCurrentTime);
                    commentsMap.put("username", userName);
                    PostsRef1.child(RandomKey).updateChildren(commentsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CommentsActivity3.this, "留言成功...", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(CommentsActivity3.this, "錯誤...再試一次", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(PostsRef1, Comments.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Comments,CommentsViewHolder>(options)
        {

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_comments_layout, parent, false);

                return new CommentsActivity3.CommentsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder commentsViewHolder, int i, @NonNull Comments comments)
            {
                commentsViewHolder.setUsername(comments.getUsername());
                commentsViewHolder.setComment(comments.getComment());
                commentsViewHolder.setDate(comments.getDate());
                commentsViewHolder.setTime(comments.getTime());
            }

        };
        adapter.startListening();
        CommentsList.setAdapter(adapter);
    }



    public static class CommentsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public CommentsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUsername(String username){
            TextView myUsername = (TextView) mView.findViewById(R.id.comment_username);
            myUsername.setText("@" + username + " ");
        }
        public void setTime(String time){
            TextView myTime = (TextView) mView.findViewById(R.id.comment_time);
            myTime.setText("Time" + time);
        }

        public void setDate(String date){
            TextView myDate = (TextView) mView.findViewById(R.id.comment_date);
            myDate.setText("Date:" + date);
        }

        public void setComment(String comment){
            TextView myComment = (TextView) mView.findViewById(R.id.comment_text);
            myComment.setText(comment);
        }


    }
}
