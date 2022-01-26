package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class Post_Sell3 extends AppCompatActivity {

    private Button AddPostButton, Return;
    private DatabaseReference UserRef, Postref;
    private FirebaseAuth mAuth;
    private RecyclerView postList;
    private DatabaseReference UsersRef, PostsRef;
    private StorageReference storageReference;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__sell5);
        AddPostButton = (Button) findViewById(R.id.Add3);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Postref = FirebaseDatabase.getInstance().getReference().child("Posts3");
        Return = (Button) findViewById(R.id.returnBtn3);

        postList = (RecyclerView) findViewById(R.id.all_users_post_list3);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToMainactivity();
            }
        });

        AddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPostsellactivity3();
            }
        });
        DispalyAllUsersPosts();

    }


    private void SendUserToMainactivity() {
        Intent mainIntent = new Intent(Post_Sell3.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void DispalyAllUsersPosts()
    {
        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(Postref, Posts.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
            @Override
            public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_posts_layout, parent, false);

                return new PostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostsViewHolder viewHolder, int position, Posts model) {

                final String PostKey = getRef(position).getKey();

                viewHolder.setUsername(model.getUsername());
                viewHolder.setTime(model.getTime());
                viewHolder.setDate(model.getDate());
                viewHolder.setDescription(model.getDescription());
                //viewHolder.setProfileimage( model.getProfileimage());
                viewHolder.setPostimage(getApplicationContext(),model.getPostimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent = new Intent(Post_Sell3.this, ClickPostActivity3.class);
                        clickPostIntent.putExtra("PostKey", PostKey);
                        startActivity(clickPostIntent);
                    }
                });

                viewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent commentsIntent = new Intent(Post_Sell3.this, CommentsActivity3.class);
                        commentsIntent.putExtra("PostKey", PostKey);
                        startActivity(commentsIntent);
                    }
                });

            }
        };
        adapter.startListening();
        postList.setAdapter(adapter);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        Button CommentPostButton;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            CommentPostButton = (Button) mView.findViewById(R.id.commentbutton);
        }

        public void setUsername(String username){
            TextView Username = (TextView) mView.findViewById(R.id.profile_name);
            Username.setText(username);
        }

        public void setTime(String time){
            TextView post_time = (TextView) mView.findViewById(R.id.post_time);
            post_time.setText("  " +time);
        }

        public void setDate(String date){
            TextView post_date = (TextView) mView.findViewById(R.id.post_date);
            post_date.setText("  "+date);
        }

        public void setDescription(String description){
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setPostimage(Context ctx,String postimage){
            ImageView Postimage = (ImageView) mView.findViewById(R.id.post_image);
            Glide.with(ctx).load(postimage).into(Postimage);
        }
    }

    private void SendUserToPostsellactivity3() {
        Intent mainIntent = new Intent(Post_Sell3.this, Postsellactivity3.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
};