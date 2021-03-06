package com.radeksukup.shoppinglist2;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.widget.TextView;

public class AboutApplicationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		
		setContentView(R.layout.activity_about_application);
		
		PackageInfo pInfo = null;
		TextView tv = (TextView) findViewById(R.id.app_name_version);
		
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			tv.append(" " + pInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		TextView link = (TextView) findViewById(R.id.link);
		Linkify.addLinks(link, Linkify.ALL);
		
		TextView changelogView = (TextView) findViewById(R.id.changelog);
		String[] changelogItems = getResources().getStringArray(R.array.changelogItems);
		String changelog = "";
		
		for (int i = 0; i < changelogItems.length; i++) {
			changelog += "&#8226; " + changelogItems[i];
			if (i + 1 != changelogItems.length) {
				changelog += "<br />";
			}
		}
		
	    changelogView.setText(Html.fromHtml(changelog));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about_application, menu);
		return true;
	}

}
