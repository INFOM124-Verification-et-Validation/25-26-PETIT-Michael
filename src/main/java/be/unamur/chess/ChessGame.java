package be.unamur.chess;

import javax.swing.*;
import java.awt.*;

/**
 * The main class to launch the chess game application.
 */
public class ChessGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessModel model = new ChessModel();
            ChessView view = new ChessView();
            ChessController controller = new ChessController(model, view);
            view.setController(controller);
            controller.startGame();
        });
    }
}
