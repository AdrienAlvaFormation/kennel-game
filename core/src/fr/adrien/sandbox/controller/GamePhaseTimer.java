package fr.adrien.sandbox.controller;

import com.badlogic.gdx.Gdx;

public class GamePhaseTimer {

    private float time;
    private float counter = time;
    private final int MIN_TIME = 8;
    private final int MAX_TIME = 9;
    private int RANGE = MAX_TIME - MIN_TIME + 1;

    //Phase Booleans
    private boolean isNotWatching, isReturning, isWatching;

    public GamePhaseTimer() {

        this.setRandomTimer();

    }

    public void update(float delta) {

        counter -= Gdx.graphics.getDeltaTime();
        //if you have delta passed as a overload you can do
        counter -= delta; //This is smoothed but should not matter for a quiz game

        if(counter > 7) {

            System.out.println("SLEEPING");

            this.isNotWatching = true;
            this.isReturning = false;
            this.isWatching = false;

        } else if (counter <= 5.5 && counter > 3.6) {

            System.out.println("WAKING UP");

            this.isNotWatching = false;
            this.isReturning = true;
            this.isWatching = false;

        } else if (counter <= 3.6 && counter > 0) {

            System.out.println("WATCHING");

            this.isNotWatching = false;
            this.isReturning = false;
            this.isWatching = true;

        } else if (counter <= 0) {
            setRandomTimer();
        }
    }// Eo Update()

    public void setRandomTimer() {
        this.time = (float)(Math.random() * RANGE) + MIN_TIME;
        this.counter = time;
    }

    public boolean isNotWatching() {
        return isNotWatching;
    }

    public void setNotWatching(boolean notWatching) {
        isNotWatching = notWatching;
    }

    public boolean isReturning() {
        return isReturning;
    }

    public void setReturning(boolean returning) {
        isReturning = returning;
    }

    public boolean isWatching() {
        return isWatching;
    }

    public void setWatching(boolean watching) {
        isWatching = watching;
    }
}// Eo GamePhaseTimer class
