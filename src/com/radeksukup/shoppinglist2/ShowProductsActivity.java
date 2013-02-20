package com.radeksukup.shoppinglist2;

import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowProductsActivity extends FragmentActivity {
	
	private DataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_products);
		
		Intent intent = getIntent();
		
		// Show the Up button in the action bar.
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
            int drawableId = intent.getExtras().getInt("drawableId");
            getActionBar().setIcon(drawableId); // set icon of screen
        }
		
		String categoryTitle = intent.getExtras().getString("categoryTitle");
		int categoryId = intent.getExtras().getInt("categoryId");
		
		setTitle(categoryTitle); // set title of screen
		
		dataSource = new DataSource(this);
		dataSource.open();
		final List<Product> products = dataSource.getProducts(categoryId);
		dataSource.close();
		
		ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, products);
		ListView productsList = (ListView) findViewById(R.id.products_list);
		
		productsList.setAdapter(adapter);
		productsList.setOnItemClickListener(new OnItemClickListener () {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DialogFragment formDialog = new FormDialog();
				String dialogTitle = products.get(position).getTitle();
				int productId = products.get(position).getId();
				
				((FormDialog) formDialog).setTitle(dialogTitle); // set dialog title
				((FormDialog) formDialog).setProductId(productId); // set selected product id
				formDialog.show(getSupportFragmentManager(), "addFormDialog");
			}
			
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_products, menu);
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
