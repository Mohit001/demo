package com.example.demo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

public class Database extends SQLiteOpenHelper
{

	public static String TAG = "Database_FirstImpression";

	Context mContext;
	private String dbPath;
	private static final String DATABASE_NAME = "firstimpression.db";
	private static final int DATABASE_VERSION = 1;

	private String leftMeta = "";
	private String rightMeta = "";

	// Table Name
	private String TABLE_IMAGE = "Image";

	// Column names
	private String LOGINID = "id";
	private String NAME = "name";
	private String IMAGE = "image";

	public Database(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createTables(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(TAG, "Old version: " + oldVersion + "\t New Version: " + newVersion);

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);

		// create new tables
		onCreate(db);

	}

	private void createTables(SQLiteDatabase db)
	{
		// Login Table
		db.execSQL("CREATE TABLE " + TABLE_IMAGE + " (" + LOGINID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " varchar(50), " + IMAGE + " BLOB"
				+ ");");

	}

	public boolean insertIamgeToDatabse(String imageName, byte[] image)
	{

		ContentValues values = new ContentValues();

		values.put(NAME, imageName);
		values.put(IMAGE, image);

		try
		{
			SQLiteDatabase database = this.getWritableDatabase();
			database.insert(TABLE_IMAGE, null, values);
		} catch (Exception exception)
		{
			System.out.println(exception.toString());
			return false;
		}
		return true;
	}

	public ArrayList<UserImage> getImageFromDatabase()
	{
		ArrayList<UserImage> arrayList = new ArrayList<UserImage>();
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.query(TABLE_IMAGE, null, null, null, null, null, null);
		
		if(cursor.getCount() > 0)
		{
			UserImage userImage;
			cursor.moveToFirst();
			do
			{
				String name = cursor.getString(cursor.getColumnIndex(NAME));
				byte[] image = cursor.getBlob(cursor.getColumnIndex(IMAGE));
				
				userImage = new UserImage();
				userImage.setName(name);
				userImage.setImage(image);
				arrayList.add(userImage);
				
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		
		
		return arrayList;
	}
}
