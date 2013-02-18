package com.radeksukup.shoppinglist2;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class ShowCategoriesActivity extends Activity {

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
        
        LinearLayout layoutWrapper = (LinearLayout) findViewById(R.id.categoriesLayout); // get categories linear layout defined in xml
        
        int rows = categories.size() / 3;
        
        if (categories.size() % 3 != 0) {
        	rows++;
        }
        
        for (int i = 0; i < categories.size(); i++) {
        	int cat = i + 1;
        	
        	if ((i + 1) % 3 == 1) {
        		System.out.println("<LinearLayout>");
        		System.out.println("Kategorie " + cat);
        	} else if ((i + 1) % 3 == 0) {
        		System.out.println("Kategorie " + cat);
				System.out.println("</LinearLayout>");
			} else {
				System.out.println("Kategorie " + cat);
			}
			
		}
//
//        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
//        ListView categoriesList = getListView(); // get ListView component from current activity
//
//        categoriesList.setAdapter(adapter);
//        categoriesList.setOnItemClickListener(new OnItemClickListener () { // bind click handler on item in listview
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				String categoryTitle = ((TextView) view).getText().toString(); // get category title
//				int categoryId = categories.get(position).getId(); // get category id
//				showProducts(categoryTitle, categoryId);
//			}
//        	
//        });
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}
	
	public void showProducts(String categoryTitle, int categoryId) {
		Intent intent = new Intent(this, ShowProductsActivity.class);
		intent.putExtra("categoryTitle", categoryTitle);
		intent.putExtra("categoryId", categoryId);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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

		case R.id.about: 
			Intent intent = new Intent(this, AboutApplicationActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
