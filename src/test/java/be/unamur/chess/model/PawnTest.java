package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PawnTest {

    @Test
    void testWhitePawnValidMoves() {
        Piece[][] board = new Piece[8][8];
        Pawn whitePawn = new Pawn(true);
        board[6][4] = whitePawn;
        Set<Point> whiteMoves = whitePawn.getValidMoves(board, 6, 4);
        assertThat(whiteMoves).containsExactlyInAnyOrder(new Point(5, 4),
                new Point(4, 4));
    }

    @Test
    void testBlackPawnValidMoves() {
        Piece[][] board = new Piece[8][8];
        Pawn blackPawn = new Pawn(false);
        board[1][4] = blackPawn;
        Set<Point> blackMoves = blackPawn.getValidMoves(board, 1, 4);
        assertThat(blackMoves).containsExactlyInAnyOrder(new Point(2, 4),
                new Point(3, 4));
    }

}