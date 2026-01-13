package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BishopTest {

    @Test
    void testBishopValidMoves() {
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(true);

        // Place bishop in the center
        board[4][4] = bishop;
        Set<Point> moves = bishop.getValidMoves(board, 4, 4);

        // Bishop should have all diagonal moves
        Set<Point> expected = new HashSet<>();
        for(int i = 0; i < 8 ; i++){
            expected.add(new Point(i, i));
        }
        for(int i = 1; i < 8 ; i++){
            expected.add(new Point(i, 8 - i));
        }
        expected.remove(new Point(4, 4));
        assertThat(moves).isEqualTo(expected);
    }

}