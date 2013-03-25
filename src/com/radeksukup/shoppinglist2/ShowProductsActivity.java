package com.radeksukup.shoppinglist2;

import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ShowProductsActivity extends FragmentActivity {
	
	private DataSource dataSource;
	private ArrayAdapter<Product> adapter = null;
	private EditText searchInput = null;

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

		searchInput = (EditText) findViewById(R.id.search_input);
		searchInput.addTextChangedListener(filterTextWatcher); // enable filter
		
//		adapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, products);
		adapter = new ProductsListItemAdapter(this, android.R.layout.simple_list_item_1, products);
		ListView productsList = (ListView) findViewById(R.id.products_list);
		
		productsList.setAdapter(adapter);
		productsList.setOnItemClickListener(new OnItemClickListener () {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DialogFragment formDialog = new FormDialog();
				TextView tv = (TextView) view.findViewById(android.R.id.text2);
				
				int productId = Integer.parseInt(String.valueOf(tv.getText())); // get product id from custom textview in adapter
				Product product = getProductById(products, productId); // get Product object by product id
				String dialogTitle = product.getTitle(); // get product title
				
				((FormDialog) formDialog).setTitle(dialogTitle); // set dialog title
				((FormDialog) formDialog).setProductId(productId); // set selected product id
				formDialog.show(getSupportFragmentManager(), "addFormDialog");
				//formDialog.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
			
		});
	}
	
	private Product getProductById(List<Product> products, int id) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getId() == id) {
				return products.get(i);
			}
		}
		return null;
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
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			adapter.getFilter().filter(s);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    searchInput.removeTextChangedListener(filterTextWatcher);
	}

}
