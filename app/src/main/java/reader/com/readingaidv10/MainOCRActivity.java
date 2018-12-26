package reader.com.readingaidv10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
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

public class MainOCRActivity extends Activity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView textValue;
    private Button button5;

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


        sendData = new Intent(MainOCRActivity.this, MainActivityX.class);
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
                Intent ocr_T = new Intent(MainOCRActivity.this, MainActivityX.class);
                ocr_T.putExtra("inText", textValue.getText().toString());
                setResult(Activity.RESULT_OK, ocr_T);
                finish();
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
        startActivity(new Intent(MainOCRActivity.this, MainActivityX.class));
    }

}
