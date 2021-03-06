package com.radeksukup.shoppinglist2;

import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShowCategoriesActivity extends FragmentActivity {

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
        
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.categoriesLayout); // get categories linear layout defined in xml
        parentLayout.removeAllViews();
        
        int rows = categories.size() / 3;
        
        if (categories.size() % 3 != 0) {
        	rows++;
        }
        
        DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int rowMinHeight = 0;
		if (metrics.densityDpi <= 120) {
			rowMinHeight = 80;
		} else if (metrics.densityDpi <= 160) {
			rowMinHeight = 100;
		} else if (metrics.densityDpi <= 240) {
			rowMinHeight = 200;
    	} else {
    		rowMinHeight = 240;
    	}

        for (int i = 0; i < rows; i++) {
        	
        	LinearLayout row = new LinearLayout(this);
        	row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        	row.setMinimumHeight(rowMinHeight);
        	
        	for (int j = 0; j < 3; j++) {
				Button categoryButton = new Button(this); // create new button
				
				int index = j + (i * 3);
				
				if (index != categories.size()) {
					categoryButton.setText(categories.get(index).getTitle()); // set text of button (category title)
					categoryButton.setId(index);
					categoryButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(categories.get(index).getDrawable()), null, null); // set image background positioned on top
				}
				
				LayoutParams params = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT, 1.0f);
				
				categoryButton.setLayoutParams(params); // set layout params
				categoryButton.setTextSize(12.0f); // set font size

				if (categoryButton.getText().equals("")) { // if there is no category left, create a blank fake button a hide it
					categoryButton.setVisibility(View.INVISIBLE);
				}
				
				categoryButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						int position = view.getId();
						int categoryId = categories.get(position).getId();
						String categoryTitle = categories.get(position).getTitle();
						int drawableId = categories.get(position).getDrawable();
						showProducts(categoryTitle, categoryId, drawableId);
					}
				});
				
				row.addView(categoryButton); // add button into current row
			}
        	
        	parentLayout.addView(row); // add row into parent layout
			
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
	
	public void showProducts(String categoryTitle, int categoryId, int drawableId) {
		Intent intent = new Intent(this, ShowProductsActivity.class);
		intent.putExtra("categoryTitle", categoryTitle);
		intent.putExtra("categoryId", categoryId);
		intent.putExtra("drawableId", drawableId);
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
		Intent intent;

		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
	//			NavUtils.navigateUpFromSameTask(this);
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
				return true;
	
			case R.id.about: 
				intent = new Intent(this, AboutApplicationActivity.class);
				startActivity(intent);
				return true;
				
			case R.id.add_custom:
				DialogFragment addCustomFormDialog = new FormDialog();
				int generatedId = (int) (Math.random()*100000);
				String dialogTitle = getResources().getString(R.string.add_custom_form_header);
				
				((FormDialog) addCustomFormDialog).setTitle(dialogTitle); // set dialog title
				((FormDialog) addCustomFormDialog).setProductId(generatedId); // set newly generated product id
				((FormDialog) addCustomFormDialog).setCustomProduct(true); // tell the dialog that we are adding custom product
				addCustomFormDialog.show(getSupportFragmentManager(), "addCustomFormDialog");
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
