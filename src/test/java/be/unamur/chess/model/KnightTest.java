package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KnightTest {

    @Test
    void testKnightValidMoves() {
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight(true);

        // Place knight in the center
        board[4][4] = knight;
        Set<Point> moves = knight.getValidMoves(board, 4, 4);

        // Knight should have 8 valid moves
        assertThat(moves).containsExactlyInAnyOrder(
                new Point(6, 5),
                new Point(6, 3),
                new Point(2, 5),
                new Point(2, 3),
                new Point(3, 6),
                new Point(3, 2),
                new Point(5, 6),
                new Point(5, 2));
    }

}