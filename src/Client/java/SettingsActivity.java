package dicomp.debateit;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.view.View;
//import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        Button soundONButton = (Button) findViewById(R.id.soundONButton);
        Button soundOffButton = (Button) findViewById(R.id.soundOffButton);
        final MediaPlayer catSound = MediaPlayer.create(this,R.raw.cat_sound);
        soundONButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Fill soundButton
                    catSound.start();
            }
        });

        soundOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catSound.stop();
            }
        });
        
         logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
