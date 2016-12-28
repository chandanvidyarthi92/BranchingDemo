package com.example.intel.branchingdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    // URL to get contacts JSON
    private static String urlForm = "http://androidworkingapp.site88.net//formdesign1.php";
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getBaseContext());
        db = databaseHelper.getWritableDatabase();
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Button btnDownloadForm = (Button) findViewById(R.id.btnDownloadForm);
        Button btnsp = (Button) findViewById(R.id.btnsp);
        btnDownloadForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetContacts().execute();
            }
        });
        btnsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor res = databaseHelper.getAllData();
                if (res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Nothing Found !!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("Id  :  " + res.getString(0) + "\n");
                        buffer.append("projectID :  " + res.getString(1) + "\n");
                        buffer.append("rec id  :  " + res.getString(2) + "\n");
                        buffer.append("fid  :  " + res.getString(3) + "\n");
                        buffer.append("value  :  " + res.getString(4) + "\n");
                        buffer.append("updatestatus  :  " + res.getString(5) + "\n");
                        buffer.append("\n");
//                        buffer.append("Date  :  " + res.getString(5) + "\n");
                        // buffer.append("Qualification  :  " + res.getString(6) + "\n");
                        // buffer.append("Gender  :  " + res.getString(7) + "\n");
                        // buffer.append("Hobby  :  " + res.getString(8) + "\n");

                    }
                    showMessage("Data", buffer.toString());
                }
            }

        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DynamicForm.class);
                startActivity(intent);
        }
        });


    }
    public void showMessage(String title, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr1 = sh.makeServiceCall(urlForm);
            Log.e(TAG, "Response from url1: " + jsonStr1);

            if (jsonStr1 != null) {
                try {
                    JSONObject jsonObjForm = new JSONObject(jsonStr1);
                    // Getting JSON Array node
                    JSONArray jsonFormData = jsonObjForm.getJSONArray("formData");
                    Log.d("contacttLength",""+jsonFormData.length());
                    for (int i = 0; i < jsonFormData.length(); i++) {
                        JSONObject c = jsonFormData.getJSONObject(i);

                        String fid = c.getString("fid");
                        String proj_id = c.getString("proj_id");
                        String slabel = c.getString("slabel");
                        String type = c.getString("type");
                        String options = c.getString("options");
                        String mandatory = c.getString("mandatory");
                        String sid = c.getString("sid");
                        String section = c.getString("section");
                        Cursor res=db.rawQuery("select * from formStruct where FIELD_ID = '" + fid + "'", null);
                        if (res.getCount() == 0) {
                            boolean isInserted = databaseHelper.insertForm(fid,proj_id, slabel, type, options,mandatory,sid,section);
                            if (isInserted == true) {
                                Log.d("valu_inserted", proj_id + slabel + type + options+mandatory+sid+section);
                            } else
                                Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }

}

