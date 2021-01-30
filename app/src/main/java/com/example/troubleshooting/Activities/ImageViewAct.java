package com.example.troubleshooting.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.troubleshooting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ImageViewAct extends AppCompatActivity {

    ImageView fullImage;
    String myEmail, myUid, postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        checkUserStatus();

        fullImage = findViewById(R.id.fullImage);

        fullImage = findViewById(R.id.fullImage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        postImage = intent.getStringExtra("pImage");

        String pImage = getIntent().getStringExtra("pImage");
        Picasso.get().load(pImage).into(fullImage);
    }

    private  void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            //user is signed in
            myEmail = user.getEmail();
            myUid = user.getUid();
        }
        else {
            //user not signed in , go to main activity
            startActivity(new Intent(this,LoginAct.class));
            finish();
        }
    }
}
