package com.faltynka.financialdiary.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faltynka.financialdiary.sqlite.model.Record;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "personalFinance.db";

    // Table Names
    public static final String TABLE_USER = "user";
    public static final String TABLE_TYPE = "type";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_RECORD = "record";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FIREBASE_ID = "firebase_id";

    // USER Table - column names
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_UID = "uid";

    // TYPE Table - column names
    // Columns id, name
    public static final String KEY_FROM_POSIBLE = "from_possible";
    public static final String KEY_TO_POSIBLE = "to_possible";

    // CATEGORY Table - column names
    // Columns id, firebaseID, name
    public static final String KEY_TYPE_ID = "type_id";

    // RECORD Table - column names
    // Column id, firebaseID
    public static final String KEY_FROM_ID = "from_id";
    public static final String KEY_TO_ID = "to_id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTE = "note";
    public static final String KEY_EDITED = "edited";
    public static final String KEY_DELETED = "deleted";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user (email text, password text, uid text)");
        db.execSQL("create table type (id integer primary key, name text, from_possible integer, to_possible integer)");
        db.execSQL("create table category (id integer primary key, firebase_id text, name text, type_id integer, foreign key (type_id) references type(id))");
        db.execSQL("create table record (id integer primary key, firebase_id text, from_id integer, to_id integer, amount integer, date integer, note text, edited integer, deleted integer, " +
                " foreign key (to_id) references category(id), foreign key (from_id) references category(id))");
        insertAllTypes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS type");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS record");
        onCreate(db);
    }

    public void insertAllTypes(SQLiteDatabase db) {
        insertType(db, "Asset", 1, 1);
        insertType(db, "Liability", 1, 1);
        insertType(db, "Expense", 0, 1);
        insertType(db, "Income", 1, 0);
        insertType(db, "Other", 1, 1);
    }

    private void insertType(SQLiteDatabase db, String name, int fromPossible, int toPossible) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("from_possible", fromPossible);
        contentValues.put("to_possible", toPossible);
        db.insert("type", null, contentValues);
    }

    public int findTypeIdByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select * from type where name='"+name+"'", null);
        if (result != null)
            result.moveToFirst();
        return result.getInt(result.getColumnIndex(KEY_ID));
    }

    public void createCategory(String name, int typeId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_TYPE_ID, typeId);

        db.insert(TABLE_CATEGORY, null, values);
    }

    public boolean existCategory(String name, int typeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from category where name = '" + name + "' and type_id =" + typeId + "", null);
        int numberOfRows = result.getCount();
        if (numberOfRows>0) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getTypesByPossibilityFrom(int fromPossible){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from type where from_possible=" + fromPossible + "", null);

        result.moveToFirst();
        List<String> items = new ArrayList<>(
        );
        while(result.isAfterLast() == false){
            items.add(result.getString(result.getColumnIndex(KEY_NAME)));
            result.moveToNext();
        }
        return items;
    }

    public List<String> getTypesByPossibilityTo(int fromPossible){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from type where to_possible=" + fromPossible + "", null);

        result.moveToFirst();
        List<String> items = new ArrayList<>(
        );
        while(result.isAfterLast() == false){
            items.add(result.getString(result.getColumnIndex(KEY_NAME)));
            result.moveToNext();
        }
        return items;
    }

    public List<String> getCategoriesByTypeId(int typeId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from category where type_id=" + typeId + "", null);

        result.moveToFirst();
        List<String> items = new ArrayList<>(
        );
        while(result.isAfterLast() == false){
            items.add(result.getString(result.getColumnIndex(KEY_NAME)));
            result.moveToNext();
        }
        return items;
    }

    public int findCategoryIdByNameAndTypeName(String categoryName, String typeName){
        SQLiteDatabase db = this.getReadableDatabase();
        int typeId = findTypeIdByName(typeName);
        Cursor result =  db.rawQuery( "select * from category where name='"+categoryName+"' and type_id=" + typeId + "", null);
        if (result != null)
            result.moveToFirst();
        return result.getInt(result.getColumnIndex(KEY_ID));
    }

    public void createRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM_ID, record.getFromId());
        values.put(KEY_TO_ID, record.getToId());
        values.put(KEY_AMOUNT, record.getAmount());
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_NOTE, record.getNote());
        values.put(KEY_EDITED, record.getEdited());
        values.put(KEY_DELETED, record.getDeleted());

        db.insert(TABLE_RECORD, null, values);
    }
}
