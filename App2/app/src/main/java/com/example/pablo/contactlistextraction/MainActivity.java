package com.example.pablo.contactlistextraction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContactsModel model;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<ContactsModel> contacts;
    ContactsAdapter mContactsAdapter;
    Cursor cursor;
    String FILEName;
    private static int BUFFER = 8192;
    Button button1;
    String name, phonenumber;
    public static final int RequestPermissionCode = 1;
    Button button;

    String inputPath = "/data/data/com.example.pablo.contactlistextraction/files/";
    String inputFile = "Contacts.zip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        button = findViewById(R.id.loadButton);
        button1 = findViewById(R.id.file);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        contacts = new ArrayList<>();
        EnableRuntimePermission();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactsList();
                mContactsAdapter = new ContactsAdapter(MainActivity.this, contacts);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(mContactsAdapter);

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileToCsv();
                Log.v("path", inputPath);
                zip(inputPath + "/" + FILEName, inputPath + inputFile);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Csv Zip File successfully Created", Snackbar.LENGTH_LONG);


                snackbar.show();
            }

        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    private void getContactsList() {
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            model = new ContactsModel();
            model.setName(name);
            model.setPhoneNumber(phonenumber);
            contacts.add(model);
        }
        cursor.close();
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(MainActivity.this, "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String per[], int[] PResult) {

        switch (requestCode) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void fileToCsv() {
        FILEName = "contact_list.csv";
        String entry;
        for (int i = 0; i < contacts.size(); i++) {
            entry = contacts.get(i).getName() + "," + contacts.get(i).getPhoneNumber() + "\n";
            try {
                FileOutputStream outputStream = openFileOutput(FILEName, Context.MODE_APPEND);
                outputStream.write(entry.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void zip(String _files, String zipFileName) {

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            //for (int i = 0; i < _files.length; i++)
            Log.v("Compress", "Adding: " + _files);
            FileInputStream fi = new FileInputStream(_files);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry entry = new ZipEntry(_files.substring(_files.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();


            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
