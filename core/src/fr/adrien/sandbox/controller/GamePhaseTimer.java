package fr.adrien.sandbox.controller;

import com.badlogic.gdx.Gdx;

public class GamePhaseTimer {

    private float time;
    private float counter = time;
    private final int MIN_TIME = 8;
    private final int MAX_TIME = 12;
    private int RANGE = MAX_TIME - MIN_TIME + 1;

    //Phase Booleans
    private boolean isNotWatching;
    private boolean isReturning;
    private boolean isWatching;

    public GamePhaseTimer() {

        this.setRandomTimer();

    }

    public void update(float delta) {


        counter -= Gdx.graphics.getDeltaTime();
        //if you have delta passed as a overload you can do
        counter -= delta; //This is smoothed but should not matter for a quiz game

        System.out.println(counter);

        if(counter > 6) {

            this.isNotWatching = true;
            this.isReturning = false;
            this.isWatching = false;

        }

        if (counter <= 6 && counter > 4)
        {
            System.out.println("NEAR END !");

            this.isNotWatching = false;
            this.isReturning = true;
            this.isWatching = false;

        }

        if (counter <= 4 && counter > 0)
        {

            System.out.println("THE EEEEEEEEEEEEEEEND");

            this.isNotWatching = false;
            this.isReturning = false;
            this.isWatching = true;

        }

        if (counter <= 0) {
            //You can just reset the counter by setting it equal to time again.
            //But there will almost always be left over (counter will be less then 0)
            //So if this is important (probably not for a quiz game)
            //counter = time + -counter; //Add the time this round took too long.
            setRandomTimer();
        }
    }

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
