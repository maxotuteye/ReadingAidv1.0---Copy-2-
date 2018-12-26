package reader.com.readingaidv10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isFabOpen = false;
    private boolean isButtonOpen = false;
    private FloatingActionButton fab,fab1, fab2, fab3,fab4, searchfab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward,button_show, button_close;
    Button button1, button2;
    SimpleSummarizer simpleSummarizer;
    AboutSummarizer aboutSummarizer;
    int MAX_SIZE;
    int ABOUT_SIZE;
    EditText editText;
    final int READ_REQUEST_CODE = 13;
    final int OCR_REQUEST_CODE = 777;
    protected DrawerLayout drawerLayout;





    String textToBeSummed, src, sercc;

    String uripath;

    TextView textView4;

    Intent receiveData;



    Runnable runnable;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        simpleSummarizer = new SimpleSummarizer();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        MAX_SIZE = 15;
        ABOUT_SIZE = 1;


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab4 = (FloatingActionButton)findViewById(R.id.fab4);
        searchfab = (FloatingActionButton)findViewById(R.id.searchfab);


        editText = (EditText)findViewById(R.id.editText);
        textView4 = (EditText)findViewById(R.id.textView4);




        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);



        button_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_show);
        button_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);



        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        searchfab.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


        src = editText.getText().toString();
        sercc  = textView4.getText().toString();

        receiveData = getIntent();
        aboutSummarizer = new AboutSummarizer();









    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {

            case R.id.textView4:
                if (isButtonOpen)
                {
                    animateButton();
                }
                if (isFabOpen)
                {
                    animateFAB();
                }
                break;
            case R.id.fab:

                textView4.setVisibility(TextView.VISIBLE);
                //textView3.setVisibility(TextView.INVISIBLE);

                animateFAB();
                if (isButtonOpen) {
                    animateButton();
                }
                break;
            case R.id.fab1:
                runnable = () -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, READ_REQUEST_CODE);

                };
                runnable.run();


                break;
            case R.id.fab2:
                startActivity(new Intent(MainActivity.this, Main2Activity.class));

                break;
            case R.id.fab3:
                animateButton();

                break;
            case R.id.fab4:
                Intent ocr = new Intent(MainActivity.this, MainOCRActivity.class);
                startActivityForResult(ocr, OCR_REQUEST_CODE);



                break;


            case R.id.button1:
                runnable = () -> {
                    if(!(textView4.getText().toString() == null)) {
                        Intent i = new Intent(MainActivity.this, ShowSummaryActivity.class);
                        i.putExtra("forActivity", simpleSummarizer.Summarize(textView4.getText().toString(), MAX_SIZE));
                        startActivity(i);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Oops, no text found!",Toast.LENGTH_LONG).show();
                    }

                };
                runnable.run();






                break;
            case R.id.button2:
                runnable = () -> {
                    if (!(textView4.getText().toString() == null)) {
                        Intent ix = new Intent(MainActivity.this, AboutActivity.class);
                        ix.putExtra("aboutActivity",aboutSummarizer.Summarize(textView4.getText().toString(),ABOUT_SIZE));
                        startActivity(ix);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Oops, no text found!",Toast.LENGTH_LONG).show();
                    }

                };
                runnable.run();
                break;
            case R.id.searchfab:


                runnable = () -> {
                    textView4.setText(HighlightString(editText.getText().toString()));
                    Toast at = Toast.makeText(getApplicationContext(),"Search complete",Toast.LENGTH_SHORT);
                    at.setGravity(Gravity.TOP,0,0);
                    at.show();

                };
                runnable.run();



                break;




        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    protected void onStart( )
    {
        super.onStart();



    }
    public void animateButton()
    {
        if(isButtonOpen)
        {
            button1.startAnimation(button_close);
            button2.startAnimation(button_close);
            button1.setClickable(false);
            button2.setClickable(false);
            isButtonOpen = false;
        }
        else
        {
            button1.startAnimation(button_show);
            button2.startAnimation(button_show);
            button1.setClickable(true);
            button2.setClickable(true);
            isButtonOpen = true;
        }
    }
    public void animateFAB()
    {
        if(isFabOpen)
        {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.startAnimation(fab_close);
            fab2.setClickable(false);
            fab3.startAnimation(fab_close);
            fab3.setClickable(false);
            fab4.startAnimation(fab_close);
            fab4.setClickable(false);
            isFabOpen = false;
        }
        else
        {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.startAnimation(fab_open);
            fab3.setClickable(true);
            fab4.startAnimation(fab_open);
            fab4.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
        if  (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Uri uri = null;
            if  (resultData != null)
            {
                uri = resultData.getData();
                uripath = uri.getPath();
                try {

                   textToBeSummed =  readTextFromUri(uri);
                   Toast toast = Toast.makeText(getApplicationContext(),"Opening document",Toast.LENGTH_SHORT);
                   textView4.setText(textToBeSummed);
                   toast.show();
                  // editText.setFocusable(true);
                   //editText.setFocusableInTouchMode(true);
                   editText.requestFocus();
                }
                catch (IOException ioe)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"File reading did not work",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
        if (requestCode == OCR_REQUEST_CODE)
        {

            {
                if (!(resultData.getExtras().getString("inText") == null)) {
                    Toast.makeText(getApplicationContext(), "Text collected", Toast.LENGTH_SHORT).show();
                    textView4.setText(resultData.getExtras().getString("inText"));
                }
            }
        }
    }
    private String readTextFromUri(Uri uri) throws IOException  {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();
    }

    public SpannableString HighlightString(String input)
    {
        SpannableString spannableString = new SpannableString(textView4.getText());
        BackgroundColorSpan[] backgroundColorSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span : backgroundColorSpans)
        {
            spannableString.removeSpan(span);
        }
        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword > 0)
        {
            spannableString.setSpan(new BackgroundColorSpan(Color.GRAY),indexOfKeyword,indexOfKeyword + input.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }


      return spannableString;
    }
    public void showSoftKeyboard(View view)
    {
        if (view.hasFocus())
        {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

}
