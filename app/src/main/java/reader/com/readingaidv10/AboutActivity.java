package reader.com.readingaidv10;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener, View.OnClickListener {
    EditText editTextX;
    private TextToSpeech tts;
    Intent ik;
    String et3;
    TextView textView;
    int start = TextToSpeech.SUCCESS;
    boolean isSpeechOn;
    boolean isSpeechOff;
    FloatingActionButton speechFab;
    Drawable speechOn;
    Drawable speechOff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView)findViewById(R.id.textView);
        ik = getIntent();
        editTextX = (EditText)findViewById(R.id.editTextX);
        et3 = ik.getExtras().getString("aboutActivity");
        editTextX.setText(et3);
        isSpeechOn = false;
        isSpeechOff = true;

        speechFab = (FloatingActionButton)findViewById(R.id.speechFab) ;
        speechFab.setImageResource(R.drawable.volume_high);
        speechFab.setOnClickListener(this);

        tts = new TextToSpeech(this,this);
        tts.setPitch((float)1.3);

        speechOn = getResources().getDrawable(R.drawable.volume_high);
        speechOff = getResources().getDrawable(R.drawable.volume_off);


    }

    @Override
    public void onDestroy()
    {
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == start)
        {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||  result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","Language is not supported");
            }
            else
            {
                //Ignore
            }

        }

        else {
            Log.e("TTS","Initialization failed");
        }
    }
    public void speakOut()
    {
        String text = editTextX.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.speechFab:
                SpeechOnOff();
                break;
        }

    }
    public void SpeechOnOff()
    {
        if (isSpeechOn)
        {
            speechFab.setImageResource(R.drawable.volume_high);
            tts.stop();
            isSpeechOn = false;
        }
        else {
            speechFab.setImageResource(R.drawable.volume_off);
            speakOut();
            isSpeechOn = true;
        }
    }
    }

