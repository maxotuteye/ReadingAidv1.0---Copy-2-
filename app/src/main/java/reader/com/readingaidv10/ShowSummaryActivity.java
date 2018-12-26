package reader.com.readingaidv10;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class ShowSummaryActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener, View.OnClickListener{
    private TextToSpeech tts;
    EditText editText3;
    Intent in;
    String et3;
    FloatingActionButton speechF;
    boolean isSpeechOn;
    boolean isSpeechOff;
    Drawable speechOn;
    Drawable speechOff;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        in = getIntent();
        editText3 = (EditText)findViewById(R.id.editText3);
        et3 = in.getExtras().getString("forActivity");
        editText3.setText(et3);


        tts = new TextToSpeech(this,this);
        tts.setSpeechRate(0.9f);

        speechOn = getResources().getDrawable(R.drawable.volume_high);
        speechOff = getResources().getDrawable(R.drawable.volume_off);


        isSpeechOn = false;
        isSpeechOff = true;

        speechF = (FloatingActionButton)findViewById(R.id.speechF);
        speechF.setImageResource(R.drawable.volume_high);
        speechF.setOnClickListener(this);

        tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
                isSpeechOn = false;
                speechF.setImageResource(R.drawable.volume_high);
            }
        });
    }

    public void onClick(View view)
    {
        int  id = view.getId();
        switch (id)
        {
            case R.id.speechF:
                SpeechOnOff();
                break;
        }

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
        if (status == TextToSpeech.SUCCESS)
        {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||  result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","Language is not supported");
            }
            else {
                // Do Nothing
        }

        }
        else {
            Log.e("TTS","Initialization failed");
        }
    }
    public void speakOut()
    {
        String text = editText3.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void SpeechOnOff()
    {
        if (isSpeechOn)
        {
            speechF.setImageResource(R.drawable.volume_high);
            tts.stop();
            isSpeechOn = false;
        }
        else {
            speechF.setImageResource(R.drawable.volume_off);
            speakOut();
            isSpeechOn = true;
        }
    }

}
