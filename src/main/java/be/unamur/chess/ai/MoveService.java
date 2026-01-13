// Java
package be.unamur.chess.ai;

import be.unamur.chess.model.King;
import be.unamur.chess.model.Piece;

import java.awt.Point;
import java.util.*;

/**
 * Utility service to validate and execute chess moves.
 * - The board is represented by Piece[8][8].
 * - Positions use Point(row, col) meaning row = x, col = y.
 */
public class MoveService {

    private Piece[][] boardState;

    public MoveService(Piece[][] boardState){
        this.boardState = boardState;
    }

    /**
     * Executes a move if it is legal. Returns true if the move was performed.
     */
    public boolean performMove(Point from, Point to) {
        if (!isLegalMove(from, to)) return false;

        Piece moving = boardState[from.x][from.y];
        boardState[to.x][to.y] = moving;
        boardState[from.x][from.y] = null;
        return true;
    }

    /**
     * Checks if a move is legal according to the piece's own rules and
     * rejects moves that leave the player's king in check.
     */
    public boolean isLegalMove(Point from, Point to) {
        if (!isInside(from) || !isInside(to)) return false;

        Piece moving = boardState[from.x][from.y];
        if (moving == null) return false;

        // Use the piece's own move logic
        Set<Point> candidates = moving.getValidMoves(boardState, from.x, from.y);
        if (!candidates.contains(to)) return false;

        // Reject moves that leave the moving side's king in check
        return !wouldLeaveOwnKingInCheck(from, to);
    }

    /**
     * Returns the set of legal destination squares for the piece at 'from'.
     */
    public Set<Point> getLegalMovesFor(Point from) {
        if (!isInside(from)) return Collections.emptySet();
        Piece p = boardState[from.x][from.y];
        if (p == null) return Collections.emptySet();

        Set<Point> raw = p.getValidMoves(boardState, from.x, from.y);
        Set<Point> legal = new HashSet<>();
        for (Point to : raw) {
            if (isInside(to) && !wouldLeaveOwnKingInCheck(from, to)) {
                legal.add(new Point(to)); // defensive copy
            }
        }
        return legal;
    }

    /**
     * Returns all legal moves for the given color.
     * The map's key is the source square; the value is the set of legal destinations.
     */
    public Map<Point, Set<Point>> getAllLegalMoves(boolean isWhite) {
        Map<Point, Set<Point>> result = new HashMap<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = boardState[r][c];
                if (p == null || p.isWhite() != isWhite) continue;

                Point from = new Point(r, c);
                Set<Point> legal = getLegalMovesFor(from);
                if (!legal.isEmpty()) {
                    result.put(from, legal);
                }
            }
        }
        return result;
    }

    // ------------------ Helpers ------------------

    private boolean isInside(Point p) {
        return p != null && p.x >= 0 && p.x < 8 && p.y >= 0 && p.y < 8;
    }

    /**
     * Simulates the move and checks if the moving side's king becomes attacked.
     */
    private boolean wouldLeaveOwnKingInCheck(Point from, Point to) {
        Piece moving = boardState[from.x][from.y];
        boolean isWhite = moving.isWhite();

        Piece[][] clone = cloneBoard(boardState);
        // Apply the move on the clone
        clone[to.x][to.y] = moving;
        clone[from.x][from.y] = null;

        Point myKingPos = findKing(clone, isWhite);
        if (myKingPos == null) {
            // Safety guard: if we cannot find the king, consider it illegal
            return true;
        }

        // If any opponent piece can reach the king's square, it's check
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece opp = clone[r][c];
                if (opp == null || opp.isWhite() == isWhite) continue;

                Set<Point> oppMoves = opp.getValidMoves(clone, r, c);
                if (oppMoves.contains(myKingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Piece[][] cloneBoard(Piece[][] original) {
        Piece[][] copy = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(original[r], 0, copy[r], 0, 8);
        }
        return copy;
    }

    private Point findKing(Piece[][] boardState, boolean isWhite) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = boardState[r][c];
                if (p instanceof King && p.isWhite() == isWhite) {
                    return new Point(r, c);
                }
            }
        }
        return null;
    }
}
