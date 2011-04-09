package com.deceptacle.spartacus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartupActivity extends Activity implements OnClickListener {
	private int MY_DATA_CHECK_CODE = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout);
        
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
        Button beginExercise = (Button) findViewById(R.id.begin);
        beginExercise.setOnClickListener(this);
    }
    
	public void onClick(View v) {
		if (v.getId() == R.id.begin) {
			SpartacusApplication app = (SpartacusApplication)this.getApplication();
			app.setCurrentExercise(Exercises.GobletSquat);
			app.setCurrentCycle(1);
			Intent intent = new Intent(this, WorkoutActivity.class);
			startActivity(intent);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}
}