package fr.adrien.sandbox.controller;

import com.badlogic.gdx.Gdx;

public class GamePhaseTimer {

    private float time;
    private float counter = time;
    private final int MIN_TIME = 5;
    private final int MAX_TIME = 10;
    private int RANGE = MAX_TIME - MIN_TIME + 1;

    //Phase Booleans
    private boolean isNotWatching;
    private boolean isReturning;
    private boolean isWatching;

    public GamePhaseTimer() {
        this.newPhase = true;

        this.setRandomTimer();
    }

    public void update(float delta) {

        counter -= Gdx.graphics.getDeltaTime();
        //if you have delta passed as a overload you can do
        counter -= delta; //This is smoothed but should not matter for a quiz game

        System.out.println(counter);

        if (counter <= 3)
        {
            //Play annoying sound to make you stress even more
            System.out.println("NEAR END !");
        }

        if (counter <= 0)
        {
            //Time is up!
            System.out.println("THE EEEEEEEEEEEEEEEND");
            //You can just reset the counter by setting it equal to time again.
            //But there will almost always be left over (counter will be less then 0)
            //So if this is important (probably not for a quiz game)
//            counter = time + -counter; //Add the time this round took too long.
            setRandomTimer();

        }
    }

    public void setRandomTimer() {
        this.time = (float)(Math.random() * RANGE) + MIN_TIME;
        this.counter = time;
    }

}// Eo GamePhaseTimer class
