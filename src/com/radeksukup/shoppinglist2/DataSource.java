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
	private String[] categoryAllColumns = {dbHelper.CATEGORY_COLUMN_ID, dbHelper.CATEGORY_COLUMN_TITLE, dbHelper.CATEGORY_COLUMN_DRAWABLE};
	private String[] productAllColumns = {dbHelper.PRODUCT_COLUMN_ID, dbHelper.PRODUCT_COLUMN_TITLE, dbHelper.PRODUCT_COLUMN_CATEGORY_ID};

	public DataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/*
	 * Get all categories.
	 */
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		Cursor cursor = database.query(dbHelper.CATEGORIES_TABLE,
				categoryAllColumns,
				null,
				null,
				null,
				null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = new Category();
			category.setId(cursor.getInt(0));
			category.setTitle(cursor.getString(1));
			category.setDrawable(cursor.getInt(2));
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return categories;
	}
	
	/*
	 * Get all products by category ID.
	 */
	public List<Product> getProducts(int categoryId) {
		String[] whereParams = {Integer.toString(categoryId)};
		List<Product> products = new ArrayList<Product>();
		Cursor cursor = database.query(
				dbHelper.PRODUCTS_TABLE,
				productAllColumns,
				dbHelper.PRODUCT_COLUMN_CATEGORY_ID + " = ?",
				whereParams,
				null,
				null,
				dbHelper.PRODUCT_COLUMN_TITLE,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = new Product();
			product.setId(cursor.getInt(0));
			product.setTitle(cursor.getString(1));
			product.setCategoryId(cursor.getInt(2));
			products.add(product);
			cursor.moveToNext();
		}
		cursor.close();
		return products;
	}

}
