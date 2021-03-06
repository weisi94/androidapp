package com.example.demonabu;

import com.razer.android.nabuopensdk.AuthCheckCallback;
import com.razer.android.nabuopensdk.NabuOpenSDK;
import com.razer.android.nabuopensdk.interfaces.NabuAuthListener;
import com.razer.android.nabuopensdk.interfaces.UserProfileListener;
import com.razer.android.nabuopensdk.models.Scope;
import com.razer.android.nabuopensdk.models.UserProfile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	static NabuOpenSDK nabuSDK = null; // NabuSDK

	Button checkApp, login;
	TextView display;

	/** The builder. */
	StringBuilder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Code starts here
		nabuSDK = NabuOpenSDK.getInstance(this); // NabuSDK

		checkApp = (Button) findViewById(R.id.btnCheckApp);
		login = (Button) findViewById(R.id.btnLogin);
		display = (TextView) findViewById(R.id.tvDisplay);
		checkApp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkApp();
			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nabuSDK.initiate(MainActivity.this,
						"ec47877454906fd268286676ef549d0736965485",
						new String[] { Scope.COMPLETE },
						new NabuAuthListener() {

							@Override
							public void onAuthSuccess(String arg0) {

								Log.e("Authentication Success", arg0);
								builder = new StringBuilder();
								builder.append(arg0);
								setResult(builder.toString());

							}

							@Override
							public void onAuthFailed(String arg0) {

								Log.e("Authentication Failed", arg0);
								builder = new StringBuilder();
								builder.append(arg0);
								setResult(builder.toString());
							}
						});

			}
		});
	}

	/**
	 * Sets the result.
	 * 
	 * @param s
	 *            the new result
	 */
	public void setResult(String s) {
		display.setText(s);
	}

	/**
	 * Check app.
	 */
	private void checkApp() {
		nabuSDK.checkAppAuthorized(MainActivity.this.getApplicationContext(),
				new AuthCheckCallback() {

					@Override
					public void onSuccess(boolean isAuthorized) {
						// LOGIN SUCCESSFUL

						builder = new StringBuilder();
						builder.append("isAuthorized:");
						builder.append(Boolean.toString(isAuthorized));

						if (builder.length() == 0)
							builder.append("No Result");

						setResult(builder.toString());
					}

					@Override
					public void onFailed(String errorMessage) {
						// LOGIN FAILED
						builder = new StringBuilder();

						builder.append(errorMessage);

						if (builder.length() == 0)
							builder.append("No Result");

						setResult(builder.toString());

					}
				});
		
		nabuSDK.getUserProfile(MainActivity.this, new UserProfileListener() {

			@Override
			public void onReceiveFailed(String arg0) {
				builder = new StringBuilder();

				builder.append(arg0);

				if (builder.length() == 0)
					builder.append("No Result");

				setResult(builder.toString());
			}

			@Override
			public void onReceiveData(UserProfile arg0) {
				builder = new StringBuilder();

				builder.append(arg0.nickName);
				builder.append("\n");
				builder.append(arg0.avatarUrl);

				if (builder.length() == 0)
					builder.append("No Result");

				setResult(builder.toString());

			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
