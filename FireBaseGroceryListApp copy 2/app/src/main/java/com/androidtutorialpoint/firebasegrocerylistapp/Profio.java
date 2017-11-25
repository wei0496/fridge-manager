package com.androidtutorialpoint.firebasegrocerylistapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Profio extends Activity {
    private Button logOut;
    private FirebaseAuth auth;
    Profile profile;
    ProfilePictureView avatar;
    Bitmap bmp = null;
    Button pwdBtn;
    TextView nameTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profio);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.defaultavater);

        avatar = (ProfilePictureView) findViewById(R.id.avatar);

        avatar.setDefaultProfilePicture(bmp);
        profile = Profile.getCurrentProfile();
//        Log.w("fberror",profile.getName());
//        if(profile!=null)
//        {
//            avatar.setProfileId(profile.getId());
//        }

        //qpx
        //change password
        pwdBtn = (Button) findViewById(R.id.PWDbtn);
        pwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),changePSW.class);
                startActivity(intent);

            }
        });

        //qpx
        //logout:
        auth = FirebaseAuth.getInstance();
        // who is User
        nameTv = (TextView) findViewById(R.id.UserNameTxt);
        nameTv.setText((CharSequence) auth.getCurrentUser().getEmail());

        logOut = (Button) findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.signOut();

                if(profile!=null)
                    LoginManager.getInstance().logOut();

                if(auth.getCurrentUser()==null)
                {
                    Intent intent =  new Intent(getApplication(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
