package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RookTest {

    @Test
    void testRookValidMoves() {
        Piece[][] board = new Piece[8][8];
        Rook rook = new Rook(true);
        // Place rook in the center
        board[4][4] = rook;
        Set<Point> moves = rook.getValidMoves(board, 4, 4);
        // Rook should have all horizontal and vertical moves
        Set<Point> expected = new HashSet<>();
        for(int i = 0; i < 8 ; i++){
            if(i != 4) {
                expected.add(new Point(4, i));
                expected.add(new Point(i, 4));
            }
        }
        assertThat(moves).isEqualTo(expected);
    }

}