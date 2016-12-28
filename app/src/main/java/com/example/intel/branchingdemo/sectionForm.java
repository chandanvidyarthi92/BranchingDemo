package com.example.intel.branchingdemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intel on 23-11-2016.
 */
public class sectionForm extends Activity {
    LinearLayout.LayoutParams fittype, textLayout;
    String str_name = "", str_type = "", str_value = "", project_id = "",value="";
    private List<EditText> editTextLongList = new ArrayList<EditText>();
    private List<TextView> textViewList = new ArrayList<TextView>();
    private List<Spinner> textSpinnerList = new ArrayList<Spinner>();
    private List<CheckBox> textCheckBoxList = new ArrayList<CheckBox>();
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    Spinner sp1;
    CheckBox hobby;
    int str_id = 0;
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    int branch_id;
    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         settings = PreferenceManager.getDefaultSharedPreferences(this);
         int b_id = getIntent().getIntExtra("b_id", 0);
        if(b_id == 0)
        {
            createForm();
        }
        else
        {
            getData_sharedPerfrense();



        }
    }


    private TextView textView(String label) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(label);
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        return textView;
    }

    private EditText editText(String name, int id,String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(fittype);
        editText.setHint(name);
        editText.setText(setText);
        editTextLongList.add(editText);
        return editText;
    }

    String qValue="";
    private Spinner qualifiaction(String options,int sp_id,String qval) {
        Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        Log.d("qval",""+qval);
        //if(qval.equalsIgnoreCase("Football"))qualifiactionSpinner.setSelection(0);
        for (String value : optionList) {
            spinnerArray.add(value);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualifiactionSpinner.setAdapter(adapter);
        }

        qualifiactionSpinner.setLayoutParams(fittype);
        textSpinnerList.add(qualifiactionSpinner.getSelectedItemPosition(),qualifiactionSpinner);
        if(qval.equalsIgnoreCase("Football"))qualifiactionSpinner.setSelection(0);
        else if(qval.equalsIgnoreCase("Cricket"))qualifiactionSpinner.setSelection(1);
        else if(qval.equalsIgnoreCase("Basketball"))qualifiactionSpinner.setSelection(2);
        return qualifiactionSpinner;
    }
    private CheckBox checkBox(int a_id, String strvalue) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(strvalue);
        checkBox.setId(a_id);
        hobby = (CheckBox) findViewById(checkBox.getId());
        textCheckBoxList.add(checkBox);
        return checkBox;
    }
    private RadioGroup radiogroup(String optionRadio,int id,String setText){
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(fittype);
        selectedId = radioGroup.getCheckedRadioButtonId();
        String[] optionRadioList = optionRadio.split(",");
        //  final RadioButton[] radioButton = new RadioButton[optionRadioList.length];
        for(int i = 0 ; i<optionRadioList.length;i++){
            radioGroup.addView(radioButton(optionRadioList[i],i,setText));
        }

        textRadioGroupList.add(radioGroup);
        return radioGroup;
    }

    private RadioButton radioButton(String strvalue,int selectedId,String matchvalue) {   //965

        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(selectedId);
        if(strvalue.equals(matchvalue))
        {
            radioButton.setChecked(true);
        }
        else
        {

        }

       /* if(strvalue.equals(matchvalue))
        {
            radioButton.setChecked(true);
        }*/
        // radioButton.setId(a_id);
        textRadioButtonList.add(radioButton);
        return radioButton;
    }


    public void createForm()
    {
        branch_id = getIntent().getIntExtra("branch_id",0);
        Log.d("branchId", "" + branch_id);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(formLayout);
        fittype = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype.setMargins(10, 0, 10, 0);
        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 0, 10, 0);
        try {
            /*Getting Project Id from DataListActivity*/
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db1 = databaseHelper.getWritableDatabase();
            Cursor cursor = db1.rawQuery("select * from formStruct where S_ID='"+branch_id+"'", null);
            if (cursor.moveToFirst()) {
                do {
                    str_id = cursor.getInt(0);
                    str_name = cursor.getString(2);
                    str_type = cursor.getString(3);
                    str_value = cursor.getString(4);
                    project_id = cursor.getString(1);

                    if (str_type.equals("text")) {
                        formLayout.addView(textView(str_name));
                        formLayout.addView(editText(str_name, str_id, ""));
                    }
                    if (str_type.equals("email")) {
                        formLayout.addView(textView(str_name));
                        formLayout.addView(editText(str_name, str_id, ""));
                    }
                    if (str_type.equals("dropdown")) {

                        formLayout.addView(textView(str_name));
                        formLayout.addView(qualifiaction(str_value,str_id,""));
                    }
                    if (str_type.equals("radio")) {

                        formLayout.addView(textView(str_name));
                        formLayout.addView(radiogroup(str_value, str_id,""));
                    }
                    if (str_type.equals("checkbox")) {

                        formLayout.addView(textView(str_name));
                        String[] str_chk1 = str_value.split(",");
                        for (int i = 0; i < str_chk1.length; i++) {
                            formLayout.addView(checkBox(str_id, str_chk1[i]));
                        }
                    }
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Exp", e.toString());
        }
        setContentView(scrollView);


        Button submit = new Button(this);
        submit.setText("Submit");
        submit.setId(101);
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_sharedPerfrense();
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }


    public void getData_sharedPerfrense() {

        branch_id = getIntent().getIntExtra("branch_id", 0);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(formLayout);

        fittype = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype.setMargins(10, 0, 10, 0);
        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 0, 10, 0);
        String EditTextValue = getIntent().getStringExtra("EditTextValue");
        String radioGRoupValue = getIntent().getStringExtra("radioGRoupValue");
        String checkbBoxValue = getIntent().getStringExtra("checkbBoxValue");
        String spinnerValue = getIntent().getStringExtra("spinnerValue");


        try {
            /*Getting Project Id from DataListActivity*/
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db1 = databaseHelper.getWritableDatabase();
            Cursor cursor = db1.rawQuery("select * from formStruct where S_ID='" + branch_id + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    str_id = cursor.getInt(0);
                    str_name = cursor.getString(2);
                    str_type = cursor.getString(3);
                    str_value = cursor.getString(4);
                    project_id = cursor.getString(1);

                    if (str_type.equals("text")) {
                        formLayout.addView(textView(str_name));
                        formLayout.addView(editText(str_name, str_id, EditTextValue));
                    }
                    if (str_type.equals("email")) {
                        formLayout.addView(textView(str_name));
                        formLayout.addView(editText(str_name, str_id, EditTextValue));
                    }
                    if (str_type.equals("dropdown")) {

                        formLayout.addView(textView(str_name));
                        formLayout.addView(qualifiaction(str_value, str_id, spinnerValue));
                    }
                    if (str_type.equals("radio")) {

                        formLayout.addView(textView(str_name));
                        formLayout.addView(radiogroup(str_value, str_id, radioGRoupValue));
                    }
                    if (str_type.equals("checkbox")) {

                        formLayout.addView(textView(str_name));
                        String[] str_chk1 = str_value.split(",");
                        for (int i = 0; i < str_chk1.length; i++) {
                            formLayout.addView(checkBox(str_id, str_chk1[i]));
                        }
                    }
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Exp", e.toString());
        }
        Button submit = new Button(this);
        submit.setText("Submit");
        submit.setId(101);
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_sharedPerfrense();
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        setContentView(scrollView);


    }

    public void save_sharedPerfrense()
    {

        editor = settings.edit();
        editor.putInt("branch_id",branch_id);

        for (EditText editLongText : editTextLongList) {
            int id_l=editLongText.getId();
            String slnew = editLongText.getText().toString();
            editor.putString("EditTextValue",slnew);
            editor.putInt("EditTextId", id_l);
        }
        for (RadioGroup rdgrp : textRadioGroupList) {
            String selectRB="";
            int cbid=rdgrp.getId();
            Log.d("cbid", ""+ cbid);

            try{
                int selectedId=rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx= rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                if(radioButton.isChecked())
                {
                    selectRB=radioButton.getText().toString();
                    editor.putInt("radioGRoupId",cbid);
                    editor.putString("radioGRoupValue",selectRB);

                }
                else{
                    Toast.makeText(getBaseContext(), selectRB + " Radio value1", Toast.LENGTH_LONG).show();
                }
            }
            catch(NullPointerException e){
                System.out.println("fbi540 ERROR=="+ e);
            }
            catch(Exception e){
                System.out.println("fd1303 ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: fd1303", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                System.out.println("-------ee FD radio  "+"Id_"+cbid+" value "+selectRB) ;

            }
        }
        String scheckbox="";
        String checkupload="";
        for (CheckBox textCheckBox : textCheckBoxList) {
            int id_ck=textCheckBox.getId();
            if(textCheckBox.isChecked())
            {
                scheckbox=textCheckBox.getText().toString();
                editor.putInt("checkBoxId",id_ck);
                editor.putString("checkbBoxValue",scheckbox);

            }
            else
            {
                scheckbox="null";
            }

            if(checkupload.equals(""))
            {
                checkupload=scheckbox;
            }
            else{
                checkupload=checkupload+","+scheckbox;
            }

        }
        for (Spinner textSpinner : textSpinnerList) {
            int id_sp = textSpinner.getId();
            String sspinner = String.valueOf(textSpinner.getSelectedItem());
            editor.putInt("spinnerId", id_sp);
            editor.putString("spinnerValue", sspinner);
        }
        editor.commit();

    }












}
