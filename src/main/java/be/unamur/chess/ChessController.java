package be.unamur.chess;

import be.unamur.chess.model.Piece;

import java.awt.*;
import java.util.Set;

/**
 * The controller class that mediates interactions between the model and view.
 */
class ChessController {
    private ChessModel model;
    private ChessView view;
    private Point selectedSquare;

    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;
    }

    public void startGame() {
        view.updateBoard(model.getBoardState());
    }

    public void onSquareClick(int row, int col) {
        if (selectedSquare == null) {
            if (model.getBoardState()[row][col] != null) {
                selectedSquare = new Point(row, col);
            }
        } else {
            Point targetSquare = new Point(row, col);
            boolean moveSuccessful = model.movePiece(selectedSquare, targetSquare);
            if (moveSuccessful) {
                view.updateBoard(model.getBoardState());
            } else {
                view.showMessage("Invalid move!");
            }
            selectedSquare = null;
        }
    }
}
