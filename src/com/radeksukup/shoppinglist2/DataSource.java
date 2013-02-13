package com.radeksukup.shoppinglist2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] categoryAllColumns = {dbHelper.CATEGORY_COLUMN_ID, dbHelper.CATEGORY_COLUMN_TITLE};

	public DataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		Cursor cursor = database.query(dbHelper.CATEGORIES_TABLE, categoryAllColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = new Category();
			category.setId(cursor.getInt(0));
			category.setTitle(cursor.getString(1));
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return categories;
	}

}
