package com.example.intel.branchingdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class DynamicForm extends Activity {
    LinearLayout.LayoutParams fittype, textLayout;
    String str_name = "", str_type = "", str_value = "", project_id = "",value="";
    private List<EditText> editTextLongList = new ArrayList<EditText>();
    private List<Spinner> textSpinnerList = new ArrayList<Spinner>();
    private List<CheckBox> textCheckBoxList = new ArrayList<CheckBox>();
    int mandatory = 0;
    int sid = 0;
    String section = "";
    Spinner sp1;
    CheckBox hobby;
    int str_id = 0;
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    private List<RadioButton> textRadioButtonListBranching = new ArrayList<RadioButton>();
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupListBranching = new ArrayList<RadioGroup>();
    private List<TextView> textViewList = new ArrayList<TextView>();

    String EditTextValue;
    private int EditTextId;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    private String radioGRoupValue;
    private int radioGRoupId;
    private String checkbBoxValue;
    private int checkBoxId;
    private String spinnerValue;
    private int spinnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        createForm();
        branching();
    }
    public void createForm()
    {
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
            Cursor cursor = db1.rawQuery("select * from formStruct where PROJECT_ID='"+44+"' and S_ID='"+0+"'", null);
            if (cursor.moveToFirst()) {
                do {
                    str_id = cursor.getInt(0);
                    project_id = cursor.getString(1);
                    str_name = cursor.getString(2);
                    str_type = cursor.getString(3);
                    str_value = cursor.getString(4);
                    mandatory = cursor.getInt(5);
                    sid = cursor.getInt(6);
                    section = cursor.getString(7);

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

                    if (str_type.equals("branching")) {

                        formLayout.addView(textView(str_name));
                        formLayout.addView(radiogroupBranching(str_value, str_id, section, ""));
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
                Toast.makeText(getApplicationContext(),"fdsaf"+EditTextId,Toast.LENGTH_SHORT).show();
                saveData();
               // finish();
            }
        });
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
        if(qval.equalsIgnoreCase("Pune"))qualifiactionSpinner.setSelection(0);
        Log.d("qvalueee", qval);

        for (String value : optionList) {
            spinnerArray.add(value);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualifiactionSpinner.setAdapter(adapter);
        }
        qualifiactionSpinner.setLayoutParams(fittype);
        textSpinnerList.add(qualifiactionSpinner.getSelectedItemPosition(), qualifiactionSpinner);
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
    private RadioGroup radiogroup(String optionRadio,int section,String setText){

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(section);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(fittype);
        String[] optionRadioList = optionRadio.split(",");
        //  final RadioButton[] radioButton = new RadioButton[optionRadioList.length];
        for(int i = 0 ; i<optionRadioList.length;i++){
            radioGroup.addView(radioButton(optionRadioList[i],setText));
        }
        Log.d("fdsaf", setText);

        textRadioGroupList.add(radioGroup);
        return radioGroup;
    }

    private RadioButton radioButton(String strvalue,String matchvalue) {   //965

        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        if(matchvalue.equals(strvalue))
        {
            radioButton.setChecked(true);
        }
        if(matchvalue.equals(strvalue))
        {
            radioButton.setChecked(true);
        }

        // radioButton.setId(a_id);
        textRadioButtonList.add(radioButton);
        return radioButton;
    }


        private RadioGroup radiogroupBranching(String optionRadio,int section,String selectionId,String setText){

            final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
            radioGroup.setId(section);
            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
            radioGroup.setLayoutParams(fittype);
            String[] optionRadioList = optionRadio.split(",");
            String[] selectedIdList = selectionId.split(",");
            //  final RadioButton[] radioButton = new RadioButton[optionRadioList.length];
            for(int i = 0 ; i<optionRadioList.length;i++){
                radioGroup.addView(radioButtonBranching(optionRadioList[i], selectedIdList[i], setText));
            }
            Log.d("fdsaf", setText);

            textRadioGroupListBranching.add(radioGroup);
            return radioGroup;
        }

        private RadioButton radioButtonBranching(String strvalue,String selectionId,String matchvalue) {   //965

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(strvalue);
            radioButton.setId(Integer.parseInt(selectionId));
            if(matchvalue.equals(strvalue))
            {
                radioButton.setChecked(true);
            }
            if(matchvalue.equals(strvalue))
            {
                radioButton.setChecked(true);
            }
            // radioButton.setId(a_id);
            textRadioButtonListBranching.add(radioButton);
            return radioButton;
        }
    public void branching()
    {
        for (final RadioGroup radioGroup : textRadioGroupListBranching)
        {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, final int i) {
                    RadioButton rb = (RadioButton) findViewById(i);
                    rb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int b_id = settings.getInt("branch_id",0);
                            if(b_id == 0)
                            {
                                Intent intent = new Intent(DynamicForm.this, sectionForm.class);
                                Log.d("branchhhhId",""+b_id);
                                Log.d("bbbbbbId",""+i);
                                intent.putExtra("branch_id", i);
                                startActivity(intent);
                            }
                            else {
                                if (b_id != i) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DynamicForm.this);
                                    alertDialog.setTitle("Confirm Delete...");

                                    alertDialog.setMessage("Are you sure you want to delete previously saved data?");
                                    alertDialog.setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    editor = settings.edit();
                                                    editor.clear();
                                                    editor.commit();

                                                }
                                            });
                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                    alertDialog.show();

                                }
                                if(b_id == i)
                                {
                                    Intent intent = new Intent(DynamicForm.this, sectionForm.class);
                                    intent.putExtra("b_id", b_id);
                                    intent.putExtra("branch_id", i);
                                    String EditTextValue1 = settings.getString("EditTextValue", null);
                                    String radioGRoupValue1 = settings.getString("radioGRoupValue", null);
                                    String checkbBoxValue1 = settings.getString("checkbBoxValue", null);
                                    String spinnerValue1 = settings.getString("spinnerValue", null);
                                    intent.putExtra("EditTextValue", EditTextValue1);
                                    intent.putExtra("radioGRoupValue", radioGRoupValue1);
                                    intent.putExtra("checkbBoxValue", checkbBoxValue1);
                                    intent.putExtra("spinnerValue", spinnerValue1);

                                    startActivity(intent);

                                }
                            }

                        }
                    });


                }
            });
        }
    }

    public void saveData()
    {
        int rd=1;
        String updatestatus ="no";
        //String idd= getIntent().getStringExtra("id");
        int idd= 44;
        DatabaseHelper dbhs = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbhs.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PROJECT_ID", idd);
        values.put("UPDATESTATUS", updatestatus);

        values.put("REC_ID", 1);
        EditTextValue = settings.getString("EditTextValue", null);
        EditTextId = settings.getInt("EditTextId", 0);
        radioGRoupValue = settings.getString("radioGRoupValue", null);
        radioGRoupId = settings.getInt("radioGRoupId", 0);
        checkbBoxValue = settings.getString("checkbBoxValue", null);
        checkBoxId = settings.getInt("checkBoxId", 0);
        spinnerValue = settings.getString("spinnerValue", null);
        spinnerId = settings.getInt("spinnerId", 0);

        if(EditTextId != 0)
        {
            values.put("F_ID", EditTextId);
            values.put("VALUE", EditTextValue);
            db.insert("dataValue", null, values);
        }
        if(radioGRoupId != 0)
        {
            values.put("F_ID", radioGRoupId);
            values.put("VALUE", radioGRoupValue);
            db.insert("dataValue", null, values);
        }
        if(checkBoxId != 0)
        {
            values.put("F_ID", checkBoxId);
            values.put("VALUE", checkbBoxValue);
            db.insert("dataValue", null, values);
        }

        if(spinnerId != 0)
        {
            values.put("F_ID", spinnerId);
            values.put("VALUE", spinnerValue);
            db.insert("dataValue", null, values);
        }


        for (EditText editLongText : editTextLongList) {
            int id_l=editLongText.getId();
            String slnew = editLongText.getText().toString();
            Log.d("fa", slnew);
            values.put("F_ID", id_l);
            values.put("VALUE", slnew);
            db.insert("dataValue", null, values);
        }

        for (RadioGroup rdgrp : textRadioGroupList) {
            String selectRB="";
            int cbid=rdgrp.getId();

            try{
                int selectedId=rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx= rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                if(radioButton.isChecked())
                {
                    selectRB=radioButton.getText().toString();
                    values.put("F_ID", cbid);
                    values.put("VALUE", selectRB);
                    db.insert("dataValue", null, values);
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
                values.put("F_ID",id_ck);
                values.put("VALUE", scheckbox);
                db.insert("dataValue", null, values);
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
            values.put("F_ID", id_sp);
            values.put("VALUE",sspinner);
            db.insert("dataValue", null, values);
            //values.put("Id_"+id_sp, sspinner);
        }



    }
}
