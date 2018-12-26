package reader.com.readingaidv10;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static reader.com.readingaidv10.R.id.editText2;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    EditText editText2, editText4;
    String fileName;
    String body;
    FloatingActionButton fab7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText4 = (EditText)findViewById(R.id.editText4);
        fab7 = (FloatingActionButton)findViewById(R.id.fab7);
        fab7.setOnClickListener(this);
    }
   public void generateNoteOnSD(Context context,String sFileName, String sBody)
   {
       try  {
           File root = new File(Environment.getExternalStorageDirectory(), "Reading Aid files");
           if (!root.exists())
           {
               root.mkdirs();
           }
           File gpxfile = new File(root, sFileName);
           FileWriter writer = new FileWriter(gpxfile);
           writer.append(sBody);
           writer.flush();
           writer.close();
           Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
       }
       catch (IOException e)
       {
           Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
       }
   }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.fab7:
                String c =  Environment.getExternalStorageDirectory().toString();
                body = editText2.getText().toString();
                fileName = editText4.getText().toString();
                if (fileName.length() > 0) {
                    Toast.makeText(getApplicationContext(),"Your file has been stored in" + c + "/Reading Aid files",Toast.LENGTH_SHORT).show();
                    generateNoteOnSD(getApplicationContext(), fileName, body);
                }else { Toast.makeText(getApplicationContext(),"Specify file name",Toast.LENGTH_LONG).show();   }
                break;
        }
    }
}
