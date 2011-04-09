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
import android.widget.TextView;

public class RestActivity extends Activity implements OnInitListener, OnUtteranceCompletedListener {
	private static final String REST_STARTED = "REST_STARTED";
	private static final String REST_COMPLETED = "REST_COMPLETED";
	
	protected TextToSpeech speaker;
	protected TextView secondsRemaining;
	
	private SpartacusApplication app;
	private RestTimer timer;
	
	private class RestTimer extends CountDownTimer {		
		public RestTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {
			//Go to rest activity
			secondsRemaining.setText("0 seconds left");
			
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, REST_COMPLETED);
			speaker.speak("Rest completed", TextToSpeech.QUEUE_FLUSH, hash);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			double left = Math.round(millisUntilFinished / 1000.0);
			secondsRemaining.setText(left + " seconds left");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.rest);
		
		app = (SpartacusApplication) getApplication();
		
		secondsRemaining = (TextView) findViewById(R.id.seconds_remaining);
		
		if (app.getCurrentExercise() == Exercises.DumbbellPushPress)
			timer = new RestTimer(120000, 500);
		else
			timer = new RestTimer(15000, 500);
		
		speaker = new TextToSpeech(this, this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		speaker.shutdown();
	}
	
	@Override
	public void onUtteranceCompleted(String id) {
		if (id.equals(REST_STARTED)) {
			timer.start();
		} else if (id.equals(REST_COMPLETED)) {
			startNextExercise();
		}
	}
	
	@Override
	public void onInit(int arg) {
		speaker.setLanguage(Locale.getDefault());
		speaker.setOnUtteranceCompletedListener(this);
		
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, REST_STARTED);
		if (app.getCurrentExercise() == Exercises.DumbbellPushPress)
			speaker.speak("Rest for two minutes", TextToSpeech.QUEUE_FLUSH, hash);
		else
			speaker.speak("Rest for fifteen seconds", TextToSpeech.QUEUE_FLUSH, hash);
	}
	
	private void startNextExercise() {
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
				app.setCurrentExercise(Exercises.GobletSquat);
				app.setCurrentCycle(app.getCurrentCycle() + 1);
				if (app.getCurrentCycle() == 4) {
					speaker.speak("Workout completed!", TextToSpeech.QUEUE_FLUSH, null);
					startActivity(new Intent(this, CompletedActivity.class));
					finish();
					return;
				}
		}
		
		startActivity(new Intent(this, WorkoutActivity.class));
		finish();
	}
}
