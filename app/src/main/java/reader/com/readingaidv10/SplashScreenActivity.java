package reader.com.readingaidv10;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener{
    private Button startButton;
    private VideoView videoView;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        videoView = (VideoView)findViewById(R.id.videoView);
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.startButton:
                Intent mainActivityIntent = new Intent(SplashScreenActivity.this,MainActivityX.class);
                startActivity(mainActivityIntent);
                break;
        }
    }
}
