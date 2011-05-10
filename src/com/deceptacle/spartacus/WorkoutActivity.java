package com.deceptacle.spartacus;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
	protected Button skip;
	protected TextToSpeech speaker;
	
	private SpartacusApplication app;
	private boolean running = false;
	private ExerciseTimer timer;
	private String exercise;
	
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
			int left = (int) Math.round(millisUntilFinished / 1000.0);
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
				exercise = "Goblet Squat";
				break;
			case MountainClimber:
				setContentView(R.layout.station_2);
				exercise = "Mountain Climbers";
				break;
			case SingleArmDumbbellSwing:
				setContentView(R.layout.station_3);
				exercise = "Single Arm Dumbbell Swing";
				break;
			case TPushup:
				setContentView(R.layout.station_4);
				exercise = "T Pushup";
				break;
			case SplitJump:
				setContentView(R.layout.station_5);
				exercise = "Split Jump";
				break;
			case DumbbellRow:
				setContentView(R.layout.station_6);
				exercise = "Dumbbell Row";
				break;
			case DumbbellSideLungeAndTouch:
				setContentView(R.layout.station_7);
				exercise = "Dumbbell Side Lunge And Touch";
				break;
			case PushupPositionRow:
				setContentView(R.layout.station_8);
				exercise = "Pushup Position Row";
				break;
			case DumbbellLungeAndRotation:
				setContentView(R.layout.station_9);
				exercise = "Dumbbell Lunge And Rotation";
				break;
			case DumbbellPushPress:
				setContentView(R.layout.station_10);
				exercise = "Dumbbell Push Press";
				break;
		}
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		speaker = new TextToSpeech(this, this);
		
		secondsRemaining = (TextView) findViewById(R.id.seconds_remaining);
		secondsRemaining.setKeepScreenOn(true);
		begin = (Button) findViewById(R.id.begin);
		skip = (Button) findViewById(R.id.skip);
		startPanel = (LinearLayout) findViewById(R.id.start_panel);
		begin.setOnClickListener(this);
		skip.setOnClickListener(this);
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
		} else if (view.getId() == R.id.skip) {
					
			switch (app.getCurrentExercise()) {
				case GobletSquat:
					app.setCurrentExercise(Exercises.MountainClimber);
					break;
				case MountainClimber:
					app.setCurrentExercise(Exercises.SingleArmDumbbellSwing);
					break;
				case SingleArmDumbbellSwing:
					app.setCurrentExercise(Exercises.TPushup);
					break;
				case TPushup:
					app.setCurrentExercise(Exercises.SplitJump);
					break;
				case SplitJump:
					app.setCurrentExercise(Exercises.DumbbellRow);
					break;
				case DumbbellRow:
					app.setCurrentExercise(Exercises.DumbbellSideLungeAndTouch);
					break;
				case DumbbellSideLungeAndTouch:
					app.setCurrentExercise(Exercises.PushupPositionRow);
					break;
				case PushupPositionRow:
					app.setCurrentExercise(Exercises.DumbbellLungeAndRotation);
					break;
				case DumbbellLungeAndRotation:
					app.setCurrentExercise(Exercises.DumbbellPushPress);
					break;
				case DumbbellPushPress:
					startActivity(new Intent(this, RestActivity.class));
					finish();
					return;
			}
			
			startActivity(new Intent(this, WorkoutActivity.class));
			finish();
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
		
		speaker.speak(exercise, TextToSpeech.QUEUE_ADD, null);
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
