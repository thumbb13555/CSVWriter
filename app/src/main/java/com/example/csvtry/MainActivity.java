package com.example.csvtry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText3);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }



    }
    public void export(View view){
        //generate data
        filename = "data"+editText.getText().toString()+".csv";
        String [] filessssss = getBaseContext().fileList();
        String ffff = null;
        for (String fff:filessssss){
            ffff = fff;
        }

        if (ffff.contains(filename)) {
            if (getBaseContext().deleteFile(filename)) {
                Log.v("BT", "su");
                StringBuilder data = new StringBuilder();
                data.append("時間,溫度,濕ＤＵ");
                for (int i = 0; i < 30; i++) {
                    data.append("\n" + "XX" + i + "," + String.valueOf(i) + "度" + "," + String.valueOf(i * i) + "%");
                }
                write(data);
            }
        }else{
            Log.v("BT", "Create new");
            StringBuilder data = new StringBuilder();
            data.append("時間,溫度,濕ＤＵ");
            for (int i = 0; i < 30; i++) {
                data.append("\n" + "XX" + i + "," + String.valueOf(i) + "度" + "," + String.valueOf(i * i) + "%");
            }
            write(data);

        }





    }

    private void write(StringBuilder data) {


        try{

            //saving the file into device
            FileOutputStream out = openFileOutput(filename, Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();
            File filelocations = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            FileOutputStream fos = new FileOutputStream(filelocations);
            fos.write(data.toString().getBytes());
            fos.close();
            Log.v("BT", String.valueOf(filelocations));
            //exporting

//            File filelocation = new File(getFilesDir(), "data.csv");
            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            Uri paths = Uri.fromFile(filelocation);
            Log.v("BT", String.valueOf(getFilesDir()));
//            Uri path = FileProvider.getUriForFile(getBaseContext(), "com.example.csvtry.fileprovider", filelocation);
            Log.v("BT", String.valueOf(paths));

            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "送出()");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, paths);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
