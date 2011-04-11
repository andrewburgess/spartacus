package com.deceptacle.spartacus;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class CompletedActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.completed);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
