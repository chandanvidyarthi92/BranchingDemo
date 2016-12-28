package com.example.intel.branchingdemo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MegaVision01 on 11/3/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="project.db";
    public static final String FORM_STRUCT = "formStruct";
    public static final String SECTION = "section";
    public static final String DATA_VALUE = "dataValue";

    public static final String field_id="FIELD_ID";
    public static final String Project_Id="PROJECT_ID";
    public static final String Field_label="FIELD_LABEL";
    public static final String Field_type="FIELD_TYPE";
    public static final String Field_option="FIELD_OPTION";
    public static final String Mandatory="MANDATORY";
    public static final String Sid="S_ID";
    public static final String Section="SECTION";

    public static final String S_Id="S_ID";
    public static final String Branch_name="BRANCH_NAME";

    public static final String data_id="DATA_ID";
    public static final String Project_id="PROJECT_ID";
    public static final String Rec_id="REC_ID";
    public static final String F_id="F_ID";
    public static final String Value="VALUE";
    public static final String UpdatedStatus="UPDATESTATUS";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d("Database", "Database is created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + FORM_STRUCT + "(FIELD_ID INTEGER,PROJECT_ID INTEGER,FIELD_LABEL TEXT,FIELD_TYPE TEXT,FIELD_OPTION TEXT,MANDATORY INTEGER,S_ID INTEGER,SECTION TEXT)");
        sqLiteDatabase.execSQL(" CREATE TABLE " + SECTION + "(S_ID INTEGER PRIMARY KEY,PROJECT_ID INTEGER,BRANCH_NAME TEXT)");
        sqLiteDatabase.execSQL(" CREATE TABLE " + DATA_VALUE + "(DATA_ID INTEGER PRIMARY KEY,PROJECT_ID INTEGER,REC_ID INTEGER,F_ID INTEGER,VALUE TEXT,UPDATESTATUS TEXT)");
        Log.d("Table", FORM_STRUCT + "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + FORM_STRUCT);
    }
    public boolean insertForm(String FieldId,String Project_id,String label,String type,String option,String mandatory,String sid,String section )
    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(field_id,FieldId);
        contentValues.put(Project_Id,Project_id);
        contentValues.put(Field_label,label);
        contentValues.put(Field_type,type);
        contentValues.put(Field_option,option);
        contentValues.put(Mandatory,mandatory);
        contentValues.put(Sid,sid);
        contentValues.put(Section,section);

        long resultset1 =db.insert(FORM_STRUCT,null,contentValues);
        if(resultset1 == -1)
            return false;
        else
            return true;
    }
    public boolean insertSection(String project_Id,String Brach_name )    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Project_Id,project_Id);
        contentValues.put(Branch_name,Brach_name);

        long resultset1 =db.insert(SECTION,null,contentValues);
        if(resultset1 == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from " + DATA_VALUE, null);
        return res;
    }
}
