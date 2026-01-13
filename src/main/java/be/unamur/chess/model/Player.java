package be.unamur.chess.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player in the chess game.
 */
public class Player {

    /** Constant representing a human player type. */
    public static final String TYPE_HUMAN = "HUMAN";
    /** Constant representing an AI player type. */
    public static final String TYPE_AI = "AI";

    /** Constant representing the white color. */
    public static final String COLOR_WHITE = "WHITE";
    /** Constant representing the black color. */
    public static final String COLOR_BLACK = "BLACK";

    /** Constant representing a simple AI strategy level. */
    public static final int STRATEGY_SIMPLE = 0;
    /** Constant representing a smarter AI strategy level. */
    public static final int STRATEGY_SMARTER = 1;

    // A Map to store attributes dynamically, similar to Python's __dict__
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * Constructs a new Player and stores attributes in a Map.
     *
     * @param name          The name of the player.
     * @param type          The type of the player.
     * @param color         The color assigned to the player.
     * @param strategyLevel The level of AI strategy.
     */
    public Player(String name, String type, String color, int strategyLevel) {
        attributes.put("name", name);
        attributes.put("type", type);
        attributes.put("color", color);
        attributes.put("strategyLevel", strategyLevel);
    }

    /**
     * Gets the name of the player.
     * @return The player's name.
     */
    public String getName() {
        return (String) attributes.get("name");
    }

    /**
     * Gets the player type.
     * @return The type as a String.
     */
    public String getType() {
        return (String) attributes.get("type");
    }

    /**
     * Gets the player's color.
     * @return The color as a String.
     */
    public String getColor() {
        return (String) attributes.get("color");
    }

    /**
     * Gets the strategy level of the player.
     * @return The strategy level as an integer.
     */
    public int getStrategyLevel() {
        // Unboxing Integer to int
        return (Integer) attributes.get("strategyLevel");
    }
}