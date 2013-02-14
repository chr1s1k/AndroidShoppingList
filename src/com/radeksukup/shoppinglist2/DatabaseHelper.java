package com.radeksukup.shoppinglist2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "shoppingList.db";
	private static final int DATABASE_VERSION = 3;
	
	public static final String CATEGORIES_TABLE = "categories";
	public static final String CATEGORY_COLUMN_ID = "id";
	public static final String CATEGORY_COLUMN_TITLE = "title";
	
	public static final String PRODUCTS_TABLE = "products";
	public static final String PRODUCT_COLUMN_ID = "id";
	public static final String PRODUCT_COLUMN_TITLE = "title";
	public static final String PRODUCT_COLUMN_CATEGORY_ID = "categoryId";
	
	private static final String TABLE_CATEGORIES_CREATE = "CREATE TABLE " + CATEGORIES_TABLE + " ( " + CATEGORY_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + CATEGORY_COLUMN_TITLE + " VARCHAR(50) NOT NULL );";
	private static final String TABLE_PRODUCTS_CREATE = "CREATE TABLE " + PRODUCTS_TABLE + " ( " + PRODUCT_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + PRODUCT_COLUMN_TITLE + " VARCHAR(50) NOT NULL, " + PRODUCT_COLUMN_CATEGORY_ID + " INT NOT NULL );";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CATEGORIES_CREATE);
		db.execSQL(TABLE_PRODUCTS_CREATE);
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Ovoce');");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Zelenina');");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Pečivo');");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Konzervy / zavařeniny / dresingy')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Maso / uzeniny')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Mléčné výrobky / sýry')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Rýže / těstoviny / luštěniny')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Sladkosti / slanosti')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Nealko nápoje')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Alkoholické nápoje')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Mražené výrobky')");
		db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Koření / ochucovadla / pečení')");
	    db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Drogerie / Hygienické potřeby')");
	    db.execSQL("INSERT INTO categories (id, title) VALUES (null, 'Domácí potřeby')");
	    db.execSQL("INSERT INTO products (id, title, categoryId) VALUES (null, 'Ananas', 1)");
	    db.execSQL("INSERT INTO products (id, title, categoryId) VALUES (null, 'Jablka', 1)");
	    db.execSQL("INSERT INTO products (id, title, categoryId) VALUES (null, 'Banány', 1)");
	    db.execSQL("INSERT INTO products (id, title, categoryId) VALUES (null, 'Pomeranče', 1)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS categories;");
		db.execSQL("DROP TABLE IF EXISTS products;");
		onCreate(db);
	}

}