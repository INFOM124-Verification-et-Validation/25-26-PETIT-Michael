package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KingTest {

    @Test
    void testKingValidMoves() {
        Piece[][] board = new Piece[8][8];
        King king = new King(true);

        // Place king in the center
        board[4][4] = king;
        Set<Point> moves = king.getValidMoves(board, 4, 4);

        // King should have all adjacent moves
        assertThat(moves.size()).isEqualTo(8);
        assertThat(moves).contains(new Point(5, 4));
        assertThat(moves).contains(new Point(3, 4));
        assertThat(moves).contains(new Point(4, 5));
        assertThat(moves).contains(new Point(4, 3));
        assertThat(moves).contains(new Point(5, 5));
        assertThat(moves).contains(new Point(3, 3));
    }

}