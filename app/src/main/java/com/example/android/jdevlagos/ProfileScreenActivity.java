package com.example.android.jdevlagos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        TextView username = (TextView) findViewById(R.id.profile_screen_username);
        TextView profileLink = (TextView) findViewById(R.id.profile_url_link);
        ImageView profilePix = (ImageView) findViewById(R.id.profile_image_view);

        username.setText(MainActivity.currentDeveloper.getUsername());
        profileLink.setText(MainActivity.currentDeveloper.getProfilePage());

        Picasso.with(this).load(MainActivity.currentDeveloper.getImageResource()).into(profilePix);

        profileLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri developerUri = Uri.parse(MainActivity.currentDeveloper.getProfilePage());

                // Create a new intent to view the developer page
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, developerUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }
}
