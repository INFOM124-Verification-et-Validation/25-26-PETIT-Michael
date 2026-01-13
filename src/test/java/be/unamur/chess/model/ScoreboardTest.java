package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreboardTest {

    @Test
    void testComputeFinalScoreWhenWhiteMoveTimesListIsEmpty() {
        Scoreboard scoreboard = new Scoreboard();

        double finalScore = scoreboard.computeFinalScore(true);

        assertThat(finalScore).isEqualTo(0.0);
    }

    @Test
    void testComputeFinalScoreWhenBlackMoveTimesListIsEmpty() {
        Scoreboard scoreboard = new Scoreboard();

        double finalScore = scoreboard.computeFinalScore(false);

        assertThat(finalScore).isEqualTo(0.0);
    }

    @Test
    void testComputeFinalScoreForWhiteWithOneValidMove() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addWhiteMoveTime(500L);

        double finalScore = scoreboard.computeFinalScore(true);

        assertThat(finalScore).isEqualTo(500.0);
    }

    @Test
    void testComputeFinalScoreForBlackWithOneValidMove() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addBlackMoveTime(700L);

        double finalScore = scoreboard.computeFinalScore(false);

        assertThat(finalScore).isEqualTo(700.0);
    }

    @Test
    void testComputeFinalScoreForWhiteWithMultipleValidMoves() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addWhiteMoveTime(100L);
        scoreboard.addWhiteMoveTime(200L);
        scoreboard.addWhiteMoveTime(300L);

        double finalScore = scoreboard.computeFinalScore(true);

        assertThat(finalScore).isEqualTo(200.0);
    }

    @Test
    void testComputeFinalScoreForBlackWithMultipleValidMoves() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addBlackMoveTime(400L);
        scoreboard.addBlackMoveTime(500L);
        scoreboard.addBlackMoveTime(600L);

        double finalScore = scoreboard.computeFinalScore(false);

        assertThat(finalScore).isEqualTo(500.0);
    }

    @Test
    void testComputeFinalScoreForWhiteWithOutliers() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addWhiteMoveTime(100L);
        scoreboard.addWhiteMoveTime(200L);
        scoreboard.addWhiteMoveTime(1000L); // Outlier
        scoreboard.addWhiteMoveTime(300L);

        double finalScore = scoreboard.computeFinalScore(true);

        assertThat(finalScore).isEqualTo(400.0);
    }

    @Test
    void testComputeFinalScoreForBlackWithOutliers() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addBlackMoveTime(500L);
        scoreboard.addBlackMoveTime(600L);
        scoreboard.addBlackMoveTime(10000L); // Outlier
        scoreboard.addBlackMoveTime(550L);

        double finalScore = scoreboard.computeFinalScore(false);

        assertThat(finalScore).isEqualTo(2912.5);
    }

    @Test
    void testComputeFinalScoreForWhiteMoveTimesWithNullValues() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addWhiteMoveTime(300L);
        scoreboard.addWhiteMoveTime(-1L); // Invalid - should be ignored
        scoreboard.addWhiteMoveTime(400L);

        double finalScore = scoreboard.computeFinalScore(true);

        assertThat(finalScore).isEqualTo(350.0);
    }

    @Test
    void testComputeFinalScoreForBlackMoveTimesWithNullValues() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addBlackMoveTime(700L);
        scoreboard.addBlackMoveTime(-1L); // Invalid - should be ignored
        scoreboard.addBlackMoveTime(800L);

        double finalScore = scoreboard.computeFinalScore(false);

        assertThat(finalScore).isEqualTo(750.0);
    }
}