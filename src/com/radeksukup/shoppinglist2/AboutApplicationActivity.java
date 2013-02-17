package com.radeksukup.shoppinglist2;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.widget.TextView;

public class AboutApplicationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about_application, menu);
		return true;
	}

}
