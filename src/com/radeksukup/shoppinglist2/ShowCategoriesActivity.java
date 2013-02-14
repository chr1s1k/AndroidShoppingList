package com.radeksukup.shoppinglist2;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowCategoriesActivity extends ListActivity {

	private DataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_categories);
		// Show the Up button in the action bar.
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        dataSource = new DataSource(this);
        dataSource.open();
        final List<Category> categories = dataSource.getCategories();
        dataSource.close();

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
        ListView categoriesList = getListView(); // get ListView component from current activity

        categoriesList.setAdapter(adapter);
        categoriesList.setOnItemClickListener(new OnItemClickListener () { // bind click handler on item in listview

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String categoryTitle = ((TextView) view).getText().toString(); // get category title
				int categoryId = categories.get(position).getId(); // get category id
				showProducts(categoryTitle, categoryId);
			}
        	
        });
	}
	
	public void showProducts(String categoryTitle, int categoryId) {
		Intent intent = new Intent(this, ShowProductsActivity.class);
		intent.putExtra("categoryTitle", categoryTitle);
		intent.putExtra("categoryId", categoryId);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_categories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
