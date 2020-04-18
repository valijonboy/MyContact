package uz.pop.mycontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {


    private static final int VERSION = 1;
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "contacts";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String NUMBER = "number";
    private static final String ORGANIZATION = "organization";
    private static final String RELATIONSHIP = "relationship";

    public DBHandler( Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY, "
        + NAME + " TEXT, "
                + NUMBER + " TEXT, "
        + EMAIL + " TEXT, "
        + ORGANIZATION + " TEXT, "
        + RELATIONSHIP + " TEXT)";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      String sql = "DROP TABLE " + TABLE_NAME;
      db.execSQL(sql);
      onCreate(db);
    }

    public  void addContact(Contact contact){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(NUMBER, contact.getNumber());
        values.put(EMAIL, contact.getEmail());
        values.put(ORGANIZATION, contact.getOrganization());
        values.put(RELATIONSHIP, contact.getRelationship());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, new String[]{ID, NAME, NUMBER, EMAIL, ORGANIZATION, RELATIONSHIP},
                ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        Contact contact;
        if (cursor != null){
            cursor.moveToFirst();
            contact = new Contact((cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            return contact;
        }else {
            return null;
        }

    }

    public List<Contact> getAllContact(){
        SQLiteDatabase  database = getReadableDatabase();
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {

                Contact contact = new Contact();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setOrganization(cursor.getString(4));
                contact.setRelationship(cursor.getString(5));
                contacts.add(contact);
            }
            while (cursor.moveToNext());
        }
        return contacts;
    }

    public int updateContact(Contact contact){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, contact.getName());
        values.put(NUMBER, contact.getNumber());
        values.put(EMAIL, contact.getEmail());
        values.put(ORGANIZATION, contact.getOrganization());
        values.put(RELATIONSHIP, contact.getRelationship());

        return database.update(TABLE_NAME, values, ID + "=?",
                new String[]{contact.getId()});
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase database = getWritableDatabase();
//        String query = "DELETE FROM "
//                + TABLE_NAME
//                + " WHERE "
//                + ID + " = "
//                + contact.getId();
//
//        database.execSQL(query);


        database.delete(TABLE_NAME,
                ID + "=?",
                new String[]{contact.getId()});
        database.close();
    }

    public List<Contact> searchContact(String name){
        name  = name.toLowerCase().trim();
        SQLiteDatabase database = getReadableDatabase();
        List<Contact> filteredContacts = new ArrayList<>();

        String query = "SELECT * FROM "
                + TABLE_NAME
                + " WHERE "
                + NAME
                + " like "
                + "'%" + name + "%'"
                +" OR "
                + NUMBER + " like " + "'%" + name + "%'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setOrganization(cursor.getString(4));
                contact.setRelationship(cursor.getString(5));
                filteredContacts.add(contact);
            }while (cursor.moveToNext());
        }
        return filteredContacts;
    }
}
