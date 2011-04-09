package com.deceptacle.spartacus;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutActivity extends Activity implements OnClickListener, OnInitListener, OnUtteranceCompletedListener {
	private static final String START_UTTERANCE = "START_UTTERANCE";
	private static final String COMPLETED_UTTERANCE = "COMPLETED_UTTERANCE";
	protected LinearLayout startPanel;
	protected TextView secondsRemaining;
	protected Button begin;
	protected TextToSpeech speaker;
	
	private SpartacusApplication app;
	private boolean running = false;
	private ExerciseTimer timer;
	
	private class ExerciseTimer extends CountDownTimer {
		private boolean thirtySecondMarker = false;
		
		public ExerciseTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {
			//Go to rest activity
			secondsRemaining.setText("0 seconds left");
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, COMPLETED_UTTERANCE);
			speaker.speak("Exercise completed", TextToSpeech.QUEUE_FLUSH, hash);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			double left = Math.round(millisUntilFinished / 1000.0);
			if (left <= 31 && thirtySecondMarker == false) {
				speaker.speak("Thirty seconds remaining", TextToSpeech.QUEUE_FLUSH, null);
				thirtySecondMarker = true;
			}
			secondsRemaining.setText(left + " seconds left");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		app = (SpartacusApplication) getApplication();
		
		switch (app.getCurrentExercise()) {
			case GobletSquat:
				setContentView(R.layout.station_1);
				break;
			case MountainClimber:
				setContentView(R.layout.station_2);
				break;
			case SingleArmDumbbellSwing:
				setContentView(R.layout.station_3);
				break;
			case TPushup:
				setContentView(R.layout.station_4);
				break;
			case SplitJump:
				setContentView(R.layout.station_5);
				break;
			case DumbbellRow:
				setContentView(R.layout.station_6);
				break;
			case DumbbellSideLungeAndTouch:
				setContentView(R.layout.station_7);
				break;
			case PushupPositionRow:
				setContentView(R.layout.station_8);
				break;
			case DumbbellLungeAndRotation:
				setContentView(R.layout.station_9);
				break;
			case DumbbellPushPress:
				setContentView(R.layout.station_10);
				break;
		}
		
		speaker = new TextToSpeech(this, this);
		
		secondsRemaining = (TextView) findViewById(R.id.seconds_remaining);
		begin = (Button) findViewById(R.id.begin);
		startPanel = (LinearLayout) findViewById(R.id.start_panel);
		begin.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		speaker.shutdown();
	}
	
	public void onClick(View view) {
		if (view.getId() == R.id.begin) {
			if (!running)
				startExercise();
		}
	}
	
	protected void startExercise() {
		running = true;
		startPanel.setVisibility(View.INVISIBLE);
		timer = new ExerciseTimer(60000, 500);
		
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, START_UTTERANCE);
		speaker.speak("Starting Exercise in 3, 2, 1", TextToSpeech.QUEUE_FLUSH, hash);
	}

	@Override
	public void onInit(int arg) {
		speaker.setLanguage(Locale.getDefault());
		speaker.setOnUtteranceCompletedListener(this);
	}

	@Override
	public void onUtteranceCompleted(String id) {
		if (id.equals(START_UTTERANCE)) {
			timer.start();
		} else if (id.equals(COMPLETED_UTTERANCE)) {
			startActivity(new Intent(this, RestActivity.class));
			finish();
		}
	}
}
