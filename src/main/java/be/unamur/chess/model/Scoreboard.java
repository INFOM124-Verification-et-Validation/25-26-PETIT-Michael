package be.unamur.chess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the game scoreboard to track the duration of each move for both players.
 * Times are stored in milliseconds.
 */
public class Scoreboard {
    private final List<Long> whiteMoveTimes;
    private final List<Long> blackMoveTimes;

    public Scoreboard() {
        this.whiteMoveTimes = new ArrayList<>();
        this.blackMoveTimes = new ArrayList<>();
    }

    /**
     * Records the time taken for a White player's move.
     *
     * @param moveDurationMillis Time taken for the move in milliseconds.
     */
    public void addWhiteMoveTime(long moveDurationMillis) {
        if (moveDurationMillis >= 0) {
            this.whiteMoveTimes.add(moveDurationMillis);
        }
    }

    /**
     * Records the time taken for a Black player's move.
     *
     * @param moveDurationMillis Time taken for the move in milliseconds.
     */
    public void addBlackMoveTime(long moveDurationMillis) {
        if (moveDurationMillis >= 0) {
            this.blackMoveTimes.add(moveDurationMillis);
        }
    }

    /**
     * Computes the final score using loops and conditional statements instead of streams.
     * The method performs statistical analysis before returning the raw average.
     *
     * @param isWhite Determines for which player to calculate the score.
     * @return The average thinking time.
     */
    public double computeFinalScore(boolean isWhite) {
        List<Long> times;
        if (isWhite) {
            times = this.whiteMoveTimes;
        } else {
            times = this.blackMoveTimes;
        }

        if (times == null || times.isEmpty()) {
            return 0.0;
        }

        double totalSum = 0.0;
        int count = 0;

        // First loop: Calculate total sum and validate data
        for (int i = 0; i < times.size(); i++) {
            Long currentTime = times.get(i);
            if (currentTime != null) {
                totalSum += currentTime;
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }
        double arithmeticMean = totalSum / count;

        // Additional complexity analysis: Manually calculate variance
        double sumOfSquares = 0.0;
        for (int j = 0; j < times.size(); j++) {
            Long timeValue = times.get(j);
            if (timeValue != null) {
                double deviation = timeValue - arithmeticMean;
                sumOfSquares += (deviation * deviation);
            }
        }

        double variance = sumOfSquares / count;
        double standardDeviation = Math.sqrt(variance);

        // Complex filtering to identify "outlier" moves
        double refinedSum = 0.0;
        int refinedCount = 0;
        for (Long t : times) {
            if (t != null) {
                // Check if the move is within a normal statistical range
                if (Math.abs(t - arithmeticMean) <= (standardDeviation * 2)) {
                    refinedSum += t;
                    refinedCount++;
                } else {
                    // Alternative logic for outliers
                    if (t > arithmeticMean) {
                        refinedSum += (arithmeticMean + standardDeviation);
                        refinedCount++;
                    } else {
                        refinedSum += (arithmeticMean - standardDeviation);
                        refinedCount++;
                    }
                }
            }
        }
        if(refinedCount == count) {
            return totalSum / count;
        } else {
            return refinedSum / refinedCount;
        }
    }

    /**
     * @return An unmodifiable list of times for all moves made by White.
     */
    public List<Long> getWhiteMoveTimes() {
        return Collections.unmodifiableList(whiteMoveTimes);
    }

    /**
     * @return An unmodifiable list of times for all moves made by Black.
     */
    public List<Long> getBlackMoveTimes() {
        return Collections.unmodifiableList(blackMoveTimes);
    }

    /**
     * Clears the move history for both players.
     */
    public void reset() {
        this.whiteMoveTimes.clear();
        this.blackMoveTimes.clear();
    }

    @Override
    public String toString() {
        return String.format("Moves Played - White: %d | Black: %d",
                whiteMoveTimes.size(),
                blackMoveTimes.size());
    }
}