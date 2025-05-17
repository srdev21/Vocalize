package com.srdevhub.vocalize;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ImageView imageMenu,btnPause;
    EditText edText;
    Button btnConvert;
    TextToSpeech textToSpeech;
    TextView btnPlay;
    SeekBar seekSpeed, seekPitch;
    float speed = 1.0f, pitch = 1.0f;

    LinearLayout resetBtn,btnClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edText=findViewById(R.id.edText);
        btnConvert=findViewById(R.id.btnConvert);
        btnClear=findViewById(R.id.btnClear);
        resetBtn=findViewById(R.id.resetBtn);
        btnPause=findViewById(R.id.btnPause);
        btnPlay=findViewById(R.id.btnPlay);



        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });

        seekSpeed = findViewById(R.id.seekSpeed);
        seekPitch = findViewById(R.id.seekPitch);

// Set SeekBar listeners
        seekSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress / 10f;
                if (speed < 0.1f) speed = 0.1f;
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitch = progress / 10f;
                if (pitch < 0.1f) pitch = 0.1f;
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        btnConvert.setOnClickListener(view -> {
            String inputText = edText.getText().toString().trim();

            if (inputText.isEmpty()) {
                edText.setError("Please enter some text!");
                edText.requestFocus();
            } else {
                textToSpeech.setSpeechRate(speed);
                textToSpeech.setPitch(pitch);
                textToSpeech.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edText.setText("");
            }
        });



        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekSpeed.setProgress(10); // Reset speed to 1.0
                seekPitch.setProgress(10); // Reset pitch to 1.0
                Toast.makeText(MainActivity.this, "Speed & Pitch reset", Toast.LENGTH_SHORT).show();
            }
        });




        final boolean[] isSpeaking = {false};

        btnPause.setOnClickListener(v -> {
            if (isSpeaking[0]) {
                // Pause logic (actually stops speaking)
                textToSpeech.stop();
                btnPause.setImageResource(R.drawable.stop);
                btnPlay.setText("Play");
                isSpeaking[0] = false;
            } else {
                // Resume logic (start again)
                String text = edText.getText().toString();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                btnPause.setImageResource(R.drawable.play);
                btnPlay.setText("Stop");
                isSpeaking[0] = true;
            }
        });










        //===============Drawer Layout Code=========================
        // Navagation Drawar------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Drawar click event
        // Drawer item Click event ------
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                int itemId = item.getItemId();

                if (itemId == R.id.mHome) {
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                } else if (itemId == R.id.mRate) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.srdevhub.vocalize")));


                    drawerLayout.closeDrawers();
                } else if (itemId == R.id.Policy) {
                    // startActivity(new Intent(MainActivity.this, privacy_policy.class));


                    drawerLayout.closeDrawers();
                } else if (itemId == R.id.shareapp) {
                    ShareCompat.IntentBuilder.from(MainActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Chooser title")
                            .setText("http://play.google.com/store/apps/details?id=com.srsoftdev.footfeed" + MainActivity.this.getPackageName())
                            .startChooser();
                    drawerLayout.closeDrawers();



                }
                return false;
            };
        });
        //------------------------------

        // ------------------------
        // App Bar Click Event
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //===============Drawer Layout Code=========================
    }
}