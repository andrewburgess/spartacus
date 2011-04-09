package com.deceptacle.spartacus;

import android.app.Application;

public class SpartacusApplication extends Application {
	private Exercises currentExercise;
	private int currentCycle;
	
	public Exercises getCurrentExercise() {
		return currentExercise;
	}
	
	public void setCurrentExercise(Exercises exercise) {
		currentExercise = exercise;
	}
	
	public int getCurrentCycle() {
		return currentCycle;
	}
	
	public void setCurrentCycle(int cycle) {
		currentCycle = cycle;
	}
}
