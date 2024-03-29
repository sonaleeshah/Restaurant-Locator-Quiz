package ca.ubc.cs.cpsc210.quiz.activity;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pcarter on 14-11-06.
 *
 * A score tracker having a list of at most COUNT of the most recent scores,
 * the minimum number of attempts taken to guess location of restaurant among those scores
 * and the highest number of points earned among those scores.
 *
 * NOTE TO CPSC 210 STUDENTS: applies the Singleton Pattern
 */
public class ScoreTracker {
    private static final String LOG_TAG = "ScoreTracker";
    private static final int COUNT = 5;
    private static ScoreTracker instance;
    private ScoreSerializer serializer;
    private List<Score> scores;
    private int minAttempts;
    private int highPointsEarned;

    /**
     * Constructor initializes list of scores by reading them from file.
     * Creates empty list of scores if data cannot be read from file.
     *
     * @param c   the application context
     *
     * NOTE TO CPSC 210 STUDENTS: do not modify this method
     */
    private ScoreTracker(Context c) {
        serializer = new ScoreSerializer(c);
        minAttempts = Integer.MAX_VALUE;
        highPointsEarned = Integer.MIN_VALUE;

        try {
            scores = serializer.readScores();
            if (scores.size() > 0) {
                minAttempts = findMinAttemptsTaken();
                highPointsEarned = findHighestPointsEarned();
            }
        } catch (Exception e) {
            scores = new ArrayList<Score>();
            Log.e(LOG_TAG, "Error loading scores");
        }
    }

    /**
     * Get the instance of ScoreTracker.
     *
     * @param c  the application context
     * @return  the score tracker
     *
     * NOTE TO CPSC 210 STUDENTS: do not modify this method
     */
    public static ScoreTracker getInstance(Context c) {
        if (instance == null)
            instance = new ScoreTracker(c);
        return instance;
    }

    /**
     * Add score to list, keeping only the most recent COUNT scores added;
     * update min attempts and max points.
     *
     * @param s  score to add to list
     */
    public void addScore(Score s) {
        scores.add(0, s);
        if (scores.size()>COUNT){
            scores.remove(scores.size()-1);
        }
    }

    /**
     * Get smallest number of attempts taken to guess location of restaurant among scores in current list
     *
     * @return  smallest number of attempts taken
     */
    public int getMinAttemptsTaken() {
        minAttempts = scores.get(0).getNumAttempts();
        for (int i = 0; i < scores.size(); i++) {
            int currentAttempts = scores.get(i).getNumAttempts();
            if (currentAttempts < minAttempts && currentAttempts != 0)
                minAttempts = currentAttempts;
        }
        return minAttempts;
    }

    /**
     * Get highest number of points earned among scores in current list
     *
     * @return  highest number of points earned
     */
    public int getHighPointsEarned() {
        highPointsEarned = 0;
        for (int i = 0; i < scores.size(); i++) {
            int currentPoints = scores.get(i).getPointsEarned();
            if (currentPoints > highPointsEarned)
                highPointsEarned    = currentPoints;
        }
        return highPointsEarned;
    }

    /**
     * Get list of most recent COUNT scores added, ordered from most recent (at the front of the list)
     * to oldest (at the end of the list)
     *
     * @return  ordered list of scores, from most recent to oldest
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * Determine the highest number of points earned among scores in current list.
     * Requires: list is not empty
     *
     * @return highest number of points earned
     */
    private int findHighestPointsEarned() {
        return getHighPointsEarned();
    }

    /**
     * Determine the smallest number of attempts taken to guess location of restaurant among scores in current list.
     * Requires: list is not empty
     *
     * @return smallest number of attempts taken
     */
    private int findMinAttemptsTaken() {
        return getMinAttemptsTaken();
    }

    /**
     * Write scores to file
     *
     * @return true if successful, false otherwise
     *
     * NOTE TO CPSC 210 STUDENTS: do not modify this method
     */
    public boolean saveScores() {
        try {
            serializer.writeScores(scores);
            Log.i(LOG_TAG, "Scores written to file");
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error saving scores to file");
            return false;
        }
    }
}
