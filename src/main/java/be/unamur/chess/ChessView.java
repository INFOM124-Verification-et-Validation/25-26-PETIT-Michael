package be.unamur.chess;

import be.unamur.chess.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * The view class responsible for rendering the chessboard and handling UI.
 */
class ChessView extends JFrame {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares;
    private ChessController controller;

    public ChessView() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        boardPanel = new JPanel(new GridLayout(8, 8));
        squares = new JButton[8][8];
        initializeBoard();

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setBackground((row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                square.setOpaque(true);
                square.setBorderPainted(false);
                int finalRow = row;
                int finalCol = col;
                square.addActionListener(e -> handleSquareClick(finalRow, finalCol));
                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
    }

    public void updateBoard(Piece[][] boardState) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = boardState[row][col];
                squares[row][col].setText(getPieceSymbol(piece));
            }
        }
    }

    private String getPieceSymbol(Piece piece) {
        if(piece == null) return " ";
        if (piece instanceof Rook) return piece.isWhite() ? "♖" : "♜";
        if (piece instanceof Knight) return piece.isWhite() ? "♘" : "♞";
        if (piece instanceof Bishop) return piece.isWhite() ? "♗" : "♝";
        if (piece instanceof Queen) return piece.isWhite() ? "♕" : "♛";
        if (piece instanceof King) return piece.isWhite() ? "♔" : "♚";
        return piece.isWhite() ? "♙" : "♟";
    }

    public void setController(ChessController controller) {
        this.controller = controller;
    }

    private void handleSquareClick(int row, int col) {
        if (controller != null) {
            controller.onSquareClick(row, col);
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}