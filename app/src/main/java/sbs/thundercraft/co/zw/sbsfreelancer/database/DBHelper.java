package sbs.thundercraft.co.zw.sbsfreelancer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *   private String name;
 private String surname;
 private String idNumber;
 private String msisdn;
 private String email;
 private String cardNumber;
 * Created by shelton on 12/26/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sbsfreelancer.db";
    public static final String CUSTOMERS_TABLE_NAME = "customers";
    public static final String CUSTOMERS_COLUMN_ID = "id";
    public static final String CUSTOMERS_COLUMN_NAME = "name";
    public static final String CUSTOMERS_COLUMN_SURNAME = "surname";
    public static final String CUSTOMERS_COLUMN_IDNUMBER = "idNumber";
    public static final String CUSTOMERS_COLUMN_MSISDN = "msisdn";
    public static final String CUSTOMERS_COLUMN_CARD_NUMBER = "cardNumber";


    public static final String DOCUMENTS_TABLE_NAME = "documents";
    public static final String DOCUMENTS_COLUMN_ID = "id";
    public static final String DOCUMENTS_COLUMN_FILE_URL = "fileUrl";
    public static final String DOCUMENTS_COLUMN_IDNUMBER = "idNumber";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table documents" +
                        "(id integer primary key, fileUrl text,idNumber text)"

        );
        db.execSQL(
                "create table customers  " +
                        "(id integer primary key, name text,surname text,idNumber text, msisdn text,cardNumber text)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS customers");
        db.execSQL("DROP TABLE IF EXISTS documents");
        onCreate(db);
    }

    public boolean insertCustomer (String name, String surname, String idNumber, String msisdn,String cardNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("idNumber", idNumber);
        contentValues.put("msisdn", msisdn);
        contentValues.put("cardNumber", cardNumber);
        db.insert("customers", null, contentValues);
        return true;
    }

    public Cursor getCustomersData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from customers where id="+id+"", null );
        return res;
    }
    public Cursor getDocumentsData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from  documents id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CUSTOMERS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String surname, String idNumber, String msisdn,String cardNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("idNumber", idNumber);
        contentValues.put("msisdn", msisdn);
        contentValues.put("cardNumber", cardNumber);
        db.update("customers", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customers",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public Integer deleteCustomer (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("documents",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<String> getAllDocuments() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from documents", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DOCUMENTS_COLUMN_IDNUMBER)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllCustomers() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from customers", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CUSTOMERS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}