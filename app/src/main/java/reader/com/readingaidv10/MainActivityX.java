package reader.com.readingaidv10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivityX extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

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
    String textToBeSummed, src, sercc;
    String uripath;
    TextView textView4;
    Intent receiveData;
    Runnable runnable;
    DrawerLayout drawer;
    TextView textView5;
    RelativeLayout rel;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_x);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rel = (RelativeLayout)findViewById(R.id.rel);
        MAX_SIZE = 15;
        ABOUT_SIZE = 1;
        simpleSummarizer = new SimpleSummarizer();
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab4 = (FloatingActionButton)findViewById(R.id.fab4);
        searchfab = (FloatingActionButton)findViewById(R.id.searchfab);
        editText = (EditText)findViewById(R.id.editText);
        textView4 = (EditText)findViewById(R.id.textView4);
        textView4.setVisibility(View.INVISIBLE);
        textView4.setBackgroundColor(Color.WHITE);
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
        textView5 = (TextView)findViewById(R.id.textView5);
        textView5.setOnClickListener(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_x, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.scanText) {
            Intent ocr = new Intent(MainActivityX.this, MainOCRActivity.class);
            startActivityForResult(ocr, OCR_REQUEST_CODE);


        } else if (id == R.id.openFile) {
            mFirebaseAnalytics.logEvent("TextOpened",new Bundle());
            textView4.setVisibility(View.VISIBLE);
            runnable = () -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, READ_REQUEST_CODE);
            };
            runnable.run();
        }
        else if (id == R.id.nav_share){
            goToUrl("https://free.facebook.com/Reading-Aid-2484548324904381/?refid=46&__xts__%5B0%5D=12.%7B\"unit_id_click_type\"%3A\"graph_search_results_item_tapped\"%2C\"click_type\"%3A\"result\"%2C\"module_id\"%3A1%2C\"result_id\"%3A2484548324904381%2C\"session_id\"%3A\"5356e6c7c509f1896962d6d464863d0e\"%2C\"module_role\"%3A\"ENTITY_PAGES\"%2C\"unit_id\"%3A\"browse_rl%3A607b57e3-d217-8fad-4d69-b94dfb31c2c7\"%2C\"browse_result_type\"%3A\"browse_type_page\"%2C\"unit_id_result_id\"%3A2484548324904381%2C\"module_result_position\"%3A0%7D");
        }
        else if (id == R.id.locBShops){
            Snackbar.make(getCurrentFocus(),"Finding Nearby Bookshops",Snackbar.LENGTH_LONG).show();
            startActivity(new Intent(MainActivityX.this, MapsActivity.class));

        }else if (id == R.id.newFile) {
            startActivity(new Intent(MainActivityX.this, Main2Activity.class));

        } else if (id == R.id.summary) {
            runnable = () -> {
                if(!(textView4.getVisibility() == View.INVISIBLE)) {
                    Intent i = new Intent(MainActivityX.this, ShowSummaryActivity.class);
                    i.putExtra("forActivity", simpleSummarizer.Summarize(textView4.getText().toString(), MAX_SIZE));
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Open text first!",Toast.LENGTH_SHORT).show();
                }

            };
            runnable.run();

        } else if (id == R.id.about){
            runnable = () -> {
                if (!(textView4.getVisibility() == View.INVISIBLE)) {
                    Intent ix = new Intent(MainActivityX.this, AboutActivity.class);
                    ix.putExtra("aboutActivity",aboutSummarizer.Summarize(textView4.getText().toString(),ABOUT_SIZE));
                    startActivity(ix);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Open text first!",Toast.LENGTH_SHORT).show();
                }

            };
            runnable.run();
        }
        else if (id == R.id.dialer){
            startActivity(new Intent(this, DialerActivity.class));
        }
        else if (id == R.id.lsc){
            startActivity(new Intent(this, LoadScratchCardActivity.class));
        }
        else if (id == R.id.calculate){
            startActivity(new Intent(this, Calculate.class));
        }
        else if (id == R.id.srch){
            if(!(textView4.getVisibility() == View.INVISIBLE)) {
                searchfab.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                searchfab.requestFocus();
                setSearchfab();
            }
            else Toast.makeText(getApplicationContext(),"Open text first!",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.textView5){
            drawer.openDrawer(GravityCompat.START);
            if (textView5.getVisibility() == View.VISIBLE) {
                textView5.startAnimation(fab_close);
                textView5.setVisibility(View.INVISIBLE);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {

            case R.id.textView5:
                drawer.openDrawer(GravityCompat.START);


                if (textView5.getVisibility() == View.VISIBLE) {
                    textView5.startAnimation(fab_close);
                    textView5.setVisibility(View.INVISIBLE);
                }
                break;
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

               // textView4.setVisibility(TextView.VISIBLE);
                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else {
                    drawer.openDrawer(Gravity.LEFT);
                }


                animateFAB();
               /* if (isButtonOpen) {
                    animateButton();
                }*/
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
                startActivity(new Intent(MainActivityX.this, Main2Activity.class));

                break;
            case R.id.fab3:
                animateButton();

                break;
            case R.id.fab4:
                Intent ocr = new Intent(MainActivityX.this, MainOCRActivity.class);
                startActivityForResult(ocr, OCR_REQUEST_CODE);



                break;


            case R.id.button1:
                runnable = () -> {
                    if(!(textView4.getText().toString() == null)) {
                        Intent i = new Intent(MainActivityX.this, ShowSummaryActivity.class);
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
                        Intent ix = new Intent(MainActivityX.this, AboutActivity.class);
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
                editText.requestFocus();
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

            isFabOpen = false;
        }
        else
        {
            fab.startAnimation(rotate_forward);

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
    public void goToUrl(String url){
        Intent intent = new Intent(this, Chrome.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    public void setSearchfab(){
        if (searchfab.hasFocus())
        {
            searchfab.startAnimation(fab_close);
            editText.startAnimation(fab_close);
        }
        else if (drawer.isDrawerOpen(GravityCompat.START)){
            searchfab.clearFocus();
        }
    }
   }
