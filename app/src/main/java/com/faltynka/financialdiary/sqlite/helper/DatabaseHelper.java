package com.faltynka.financialdiary.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faltynka.financialdiary.SumInCategory;
import com.faltynka.financialdiary.sqlite.model.Category;
import com.faltynka.financialdiary.sqlite.model.Record;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void insertUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);

        db.insert(TABLE_USER, null, values);
    }

    public boolean existUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from user", null);
        int numberOfRows = result.getCount();
        if (numberOfRows>0) {
            return true;
        } else {
            return false;
        }
    }

    public void createCategory(String name, int typeId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_TYPE_ID, typeId);

        db.insert(TABLE_CATEGORY, null, values);
    }

    public void createCategoryWithId(Category category){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, category.getId());
        values.put(KEY_NAME, category.getName());
        values.put(KEY_TYPE_ID, category.getType());

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

    public List<Category> selectAllCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from category", null);
        if (result == null) {
            return new ArrayList<>();
        }
        result.moveToFirst();
        List<Category> categories = new ArrayList<>();
        while(!result.isAfterLast()){
            Category category = new Category();
            category.setId(result.getInt(result.getColumnIndex(KEY_ID)));
            category.setFirebaseId(result.getString(result.getColumnIndex(KEY_FIREBASE_ID)));
            category.setName(result.getString(result.getColumnIndex(KEY_NAME)));
            category.setType(result.getInt(result.getColumnIndex(KEY_TYPE_ID)));
            categories.add(category);
            result.moveToNext();
        }
        return categories;
    }

    public List<String> getTypesByPossibilityFrom(int fromPossible){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from type where from_possible=" + fromPossible + "", null);

        result.moveToFirst();
        List<String> items = new ArrayList<>(
        );
        while(!result.isAfterLast()){
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
        while(!result.isAfterLast()){
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
        while(!result.isAfterLast()){
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

    public void createRecordWithId(Record record){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, record.getId());
        values.put(KEY_FROM_ID, record.getFromId());
        values.put(KEY_TO_ID, record.getToId());
        values.put(KEY_AMOUNT, record.getAmount());
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_NOTE, record.getNote());
        values.put(KEY_EDITED, record.getEdited());
        values.put(KEY_DELETED, record.getDeleted());

        db.insert(TABLE_RECORD, null, values);
    }

    public void editRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM_ID, record.getFromId());
        values.put(KEY_TO_ID, record.getToId());
        values.put(KEY_AMOUNT, record.getAmount());
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_NOTE, record.getNote());
        values.put(KEY_EDITED, record.getEdited());
        values.put(KEY_DELETED, record.getDeleted());

        db.update(TABLE_RECORD, values, "id=" + record.getId(), null);
    }

    public List<Integer> getAllDistinctYearsOfRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from record", null);
        if (result == null) {
            return null;
        }
        result.moveToFirst();
        Set<Integer> years = new HashSet<>();
        while(!result.isAfterLast()){
            DateTime dateTime = new DateTime(result.getLong(result.getColumnIndex(KEY_DATE)));
            years.add(dateTime.getYear());
            result.moveToNext();
        }
        List<Integer> yearsList = new ArrayList<>();
        yearsList.addAll(years);
        return yearsList;
    }

    public List<Record> selectAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from record", null);
        if (result == null) {
            return new ArrayList<>();
        }
        result.moveToFirst();
        List<Record> records = new ArrayList<>();
        while(!result.isAfterLast()){
            Record record = new Record();
            record.setId(result.getInt(result.getColumnIndex(KEY_ID)));
            record.setFromId(result.getInt(result.getColumnIndex(KEY_FROM_ID)));
            record.setToId(result.getInt(result.getColumnIndex(KEY_TO_ID)));
            record.setAmount(result.getInt(result.getColumnIndex(KEY_AMOUNT)));
            record.setEdited(result.getLong(result.getColumnIndex(KEY_EDITED)));
            record.setDate(result.getLong(result.getColumnIndex(KEY_DATE)));
            record.setNote(result.getString(result.getColumnIndex(KEY_NOTE)));
            record.setDeleted(result.getInt(result.getColumnIndex(KEY_DELETED)));
            records.add(record);
            result.moveToNext();
        }
        return records;
    }

    public List<Record> getRecordsBetweenDates(DateTime fromDate, DateTime toDate) {
        Long fromTimestamp = fromDate.getMillis();
        Long toTimestamp = toDate.getMillis();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from record where date >=" + fromTimestamp + " and date < " + toTimestamp + " and deleted = 0", null);
        if (result == null) {
            return null;
        }
        result.moveToFirst();
        List<Record> records = new ArrayList<>();
        while(!result.isAfterLast()){
            Record record = new Record();
            record.setId(result.getInt(result.getColumnIndex(KEY_ID)));
            record.setFromId(result.getInt(result.getColumnIndex(KEY_FROM_ID)));
            record.setToId(result.getInt(result.getColumnIndex(KEY_TO_ID)));
            record.setAmount(result.getInt(result.getColumnIndex(KEY_AMOUNT)));
            record.setEdited(result.getLong(result.getColumnIndex(KEY_EDITED)));
            record.setDate(result.getLong(result.getColumnIndex(KEY_DATE)));
            record.setNote(result.getString(result.getColumnIndex(KEY_NOTE)));
            record.setDeleted(result.getInt(result.getColumnIndex(KEY_DELETED)));
            records.add(record);
            result.moveToNext();
        }
        return records;
    }

    public String findCategoryNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select * from category where id="+id+"", null);
        if (result != null)
            result.moveToFirst();
        return result.getString(result.getColumnIndex(KEY_NAME));
    }

    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "update record set deleted = 1 where id =" + id;
        db.execSQL(sql);
    }

    public String findTypeNameForCategoryWithId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from category where id=" + id, null);
        if (result != null)
            result.moveToFirst();
        int typeIdOfCategory = result.getInt(result.getColumnIndex(KEY_TYPE_ID));
        Cursor result2 = db.rawQuery("select * from type where id="+ typeIdOfCategory, null);
        if (result2 != null)
            result2.moveToFirst();
        return result2.getString(result2.getColumnIndex(KEY_NAME));
    }

    public List<SumInCategory> countSumInIncomeCategoriesFromDateRange(DateTime fromDate, DateTime toDate) {
        Long fromTimestamp = fromDate.getMillis();
        Long toTimestamp = toDate.getMillis();
        SQLiteDatabase db = this.getReadableDatabase();
        int incomeId = findTypeIdByName("Income");
        List<Integer> allIncomeCategories = getAllCategoriesIdForType(incomeId);
        List<SumInCategory> sumsInCategories = new ArrayList<>();
        for(Integer categoryId : allIncomeCategories) {
            String categoryName = findCategoryNameById(categoryId);
            int sum = 0;
            Cursor result = db.rawQuery("select * from record where from_id =" + categoryId + " and date >=" + fromTimestamp + " and date < " + toTimestamp + " and deleted = 0", null);
            if (result == null) {
                SumInCategory sumInCategory = new SumInCategory();
                sumInCategory.setCategory(categoryName);
                sumInCategory.setSum(sum);
                sumsInCategories.add(sumInCategory);
                break;
            }
            result.moveToFirst();
            while(!result.isAfterLast()){
                sum = sum + result.getInt(result.getColumnIndex(KEY_AMOUNT));
                result.moveToNext();
            }
            SumInCategory sumInCategory = new SumInCategory();
            sumInCategory.setCategory(categoryName);
            sumInCategory.setSum(sum);
            sumsInCategories.add(sumInCategory);
        }
        return sumsInCategories;
    }

    public List<SumInCategory> countSumInIncomeCategoriesEver() {
        SQLiteDatabase db = this.getReadableDatabase();
        int incomeId = findTypeIdByName("Income");
        List<Integer> allIncomeCategories = getAllCategoriesIdForType(incomeId);
        List<SumInCategory> sumsInCategories = new ArrayList<>();
        for(Integer categoryId : allIncomeCategories) {
            String categoryName = findCategoryNameById(categoryId);
            int sum = 0;
            Cursor result = db.rawQuery("select * from record where from_id =" + categoryId + " and deleted = 0", null);
            if (result == null) {
                SumInCategory sumInCategory = new SumInCategory();
                sumInCategory.setCategory(categoryName);
                sumInCategory.setSum(sum);
                sumsInCategories.add(sumInCategory);
                break;
            }
            result.moveToFirst();
            while(!result.isAfterLast()){
                sum = sum + result.getInt(result.getColumnIndex(KEY_AMOUNT));
                result.moveToNext();
            }
            SumInCategory sumInCategory = new SumInCategory();
            sumInCategory.setCategory(categoryName);
            sumInCategory.setSum(sum);
            sumsInCategories.add(sumInCategory);
        }
        return sumsInCategories;
    }

    public List<SumInCategory> countSumInExpenseCategoriesFromDateRange(DateTime fromDate, DateTime toDate) {
        Long fromTimestamp = fromDate.getMillis();
        Long toTimestamp = toDate.getMillis();
        SQLiteDatabase db = this.getReadableDatabase();
        int expenseId = findTypeIdByName("Expense");
        List<Integer> allExpenseCategories = getAllCategoriesIdForType(expenseId);
        List<SumInCategory> sumsInCategories = new ArrayList<>();
        for(Integer categoryId : allExpenseCategories) {
            String categoryName = findCategoryNameById(categoryId);
            int sum = 0;
            Cursor result = db.rawQuery("select * from record where to_id =" + categoryId + " and date >=" + fromTimestamp + " and date < " + toTimestamp + " and deleted = 0", null);
            if (result == null) {
                SumInCategory sumInCategory = new SumInCategory();
                sumInCategory.setCategory(categoryName);
                sumInCategory.setSum(sum);
                sumsInCategories.add(sumInCategory);
                break;
            }
            result.moveToFirst();
            while(!result.isAfterLast()){
                sum = sum + result.getInt(result.getColumnIndex(KEY_AMOUNT));
                result.moveToNext();
            }
            SumInCategory sumInCategory = new SumInCategory();
            sumInCategory.setCategory(categoryName);
            sumInCategory.setSum(sum);
            sumsInCategories.add(sumInCategory);
        }
        return sumsInCategories;
    }

    public List<SumInCategory> countSumInExpenseCategoriesEver() {
        SQLiteDatabase db = this.getReadableDatabase();
        int expenseId = findTypeIdByName("Expense");
        List<Integer> allExpenseCategories = getAllCategoriesIdForType(expenseId);
        List<SumInCategory> sumsInCategories = new ArrayList<>();
        for(Integer categoryId : allExpenseCategories) {
            String categoryName = findCategoryNameById(categoryId);
            int sum = 0;
            Cursor result = db.rawQuery("select * from record where to_id =" + categoryId + " and deleted = 0", null);
            if (result == null) {
                SumInCategory sumInCategory = new SumInCategory();
                sumInCategory.setCategory(categoryName);
                sumInCategory.setSum(sum);
                sumsInCategories.add(sumInCategory);
                break;
            }
            result.moveToFirst();
            while(!result.isAfterLast()){
                sum = sum + result.getInt(result.getColumnIndex(KEY_AMOUNT));
                result.moveToNext();
            }
            SumInCategory sumInCategory = new SumInCategory();
            sumInCategory.setCategory(categoryName);
            sumInCategory.setSum(sum);
            sumsInCategories.add(sumInCategory);
        }
        return sumsInCategories;
    }

    public List<SumInCategory> countSumInCategoriesOfTypeFromDateRange(String typeName, DateTime fromDate, DateTime toDate) {
        Long fromTimestamp = fromDate.getMillis();
        Long toTimestamp = toDate.getMillis();
        SQLiteDatabase db = this.getReadableDatabase();
        int typeId = findTypeIdByName(typeName);
        List<Integer> allCategoriesOfType = getAllCategoriesIdForType(typeId);
        List<SumInCategory> sumsInCategories = new ArrayList<>();
        for(Integer categoryId : allCategoriesOfType) {
            String categoryName = findCategoryNameById(categoryId);
            int sum = 0;
            Cursor resultFrom = db.rawQuery("select * from record where from_id =" + categoryId + " and date >=" + fromTimestamp + " and date < " + toTimestamp + " and deleted = 0", null);
            Cursor resultTo = db.rawQuery("select * from record where to_id =" + categoryId + " and date >=" + fromTimestamp + " and date < " + toTimestamp + " and deleted = 0", null);
            if (resultFrom == null && resultTo == null) {
                SumInCategory sumInCategory = new SumInCategory();
                sumInCategory.setCategory(categoryName);
                sumInCategory.setSum(sum);
                sumsInCategories.add(sumInCategory);
                break;
            }
            if (resultFrom != null) {
                resultFrom.moveToFirst();
                while(!resultFrom.isAfterLast()){
                    sum = sum - resultFrom.getInt(resultFrom.getColumnIndex(KEY_AMOUNT));
                    resultFrom.moveToNext();
                }
            }
            if (resultTo != null) {
                resultTo.moveToFirst();
                while(!resultTo.isAfterLast()) {
                    sum = sum + resultTo.getInt(resultTo.getColumnIndex(KEY_AMOUNT));
                    resultTo.moveToNext();
                }
            }
            SumInCategory sumInCategory = new SumInCategory();
            sumInCategory.setCategory(categoryName);
            sumInCategory.setSum(sum);
            sumsInCategories.add(sumInCategory);
        }
        return sumsInCategories;
    }

    public List<SumInCategory> countSumInCategoriesOfTypeEver(String typeName){
        SQLiteDatabase db = this.getReadableDatabase();
        int typeId = findTypeIdByName(typeName);
        List<Integer> allCategoriesOfType = getAllCategoriesIdForType(typeId);
        List<SumInCategory> sumsInCategories = new ArrayList<>();
        for(Integer categoryId : allCategoriesOfType) {
            String categoryName = findCategoryNameById(categoryId);
            int sum = 0;
            Cursor resultFrom = db.rawQuery("select * from record where from_id =" + categoryId + " and deleted = 0", null);
            Cursor resultTo = db.rawQuery("select * from record where to_id =" + categoryId + " and deleted = 0", null);
            if (resultFrom == null && resultTo == null) {
                SumInCategory sumInCategory = new SumInCategory();
                sumInCategory.setCategory(categoryName);
                sumInCategory.setSum(sum);
                sumsInCategories.add(sumInCategory);
                break;
            }
            if (resultFrom != null) {
                resultFrom.moveToFirst();
                while(!resultFrom.isAfterLast()){
                    sum = sum - resultFrom.getInt(resultFrom.getColumnIndex(KEY_AMOUNT));
                    resultFrom.moveToNext();
                }
            }
            if (resultTo != null) {
                resultTo.moveToFirst();
                while(!resultTo.isAfterLast()) {
                    sum = sum + resultTo.getInt(resultTo.getColumnIndex(KEY_AMOUNT));
                    resultTo.moveToNext();
                }
            }
            SumInCategory sumInCategory = new SumInCategory();
            sumInCategory.setCategory(categoryName);
            sumInCategory.setSum(sum);
            sumsInCategories.add(sumInCategory);
        }
        return sumsInCategories;
    }

    public List<Integer> getAllCategoriesIdForType(int typeId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from category where type_id=" + typeId + "", null);

        result.moveToFirst();
        List<Integer> items = new ArrayList<>(
        );
        while(!result.isAfterLast()){
            items.add(result.getInt(result.getColumnIndex(KEY_ID)));
            result.moveToNext();
        }
        return items;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUser = "delete from user";
        String sqlRecord = "delete from record";
        String sqlCategory = "delete from category";
        db.execSQL(sqlUser);
        db.execSQL(sqlRecord);
        db.execSQL(sqlCategory);
        db.close();
    }
}
