package reader.com.readingaidv10;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

public class DialerActivity extends Activity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView textValue;
    private Button button5;
    private TextView dialerButton, scratchCardButton;
    private String dialerText;
    private Animation openOptions, closeOptions;
    private FloatingActionButton optionsF;

    Intent sendData;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ocr);


        textValue = (TextView) findViewById(R.id.text_value);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button5.setText("Open Dialer");


        sendData = new Intent(DialerActivity.this, MainActivityX.class);

        openOptions = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_show);
        closeOptions = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_close);

        findViewById(R.id.read_text).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
        if (v.getId() == R.id.button5) {
            if (!(textValue.getText().toString() == ""
                    || textValue.getText().toString() == " "
                    || textValue.getText().toString() == "\n")) {
                dialerText = textValue.getText().toString();
                Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
                dialerIntent.setData(Uri.parse("tel:" + dialerText));
                startActivity(dialerIntent);
                Toast.makeText(getApplicationContext(), "Opening Dialer", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"No text found",Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    textValue.setText(text);
                    sendData.putExtra("newOCR",text);
                   // textView4.setText(text);

                    Log.d(TAG, "Text read: " + text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DialerActivity.this, MainActivityX.class));
    }

}
