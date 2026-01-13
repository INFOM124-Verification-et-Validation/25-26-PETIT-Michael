package be.unamur.chess.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import be.unamur.chess.model.*;

/**
 * <p>
 * Utility class to persist and restore board states (of any rectangular size) to a file
 * in a minimal JSON-like format, without external dependencies. The format is an array
 * of row arrays, each cell being either <code>null</code> or an object with fields
 * {@code type} and {@code white}:
 * </p>
 *
 * <pre>
 * [
 *   [null, {"type":"Pawn","white":true}, ...],
 *   [ ... ],
 *   ...
 * ]
 * </pre>
 *
 * <p>
 * The serializer produces a canonical representation with no extraneous whitespace
 * inside tokens. The deserializer tolerates surrounding whitespace but expects the
 * same object shape.
 * </p>
 *
 * @implNote This implementation avoids external JSON libraries and uses a small,
 * robust parser tailored to the known output shape. If format flexibility or resilience
 * is required, prefer a JSON library (e.g., Gson/Jackson).
 */
public final class ChessFileHandler {

    private static final Logger LOG = Logger.getLogger(ChessFileHandler.class.getName());

    /**
     * Private constructor: utility class should not be instantiated.
     */
    private ChessFileHandler() {
        // utility class
    }

    /**
     * Backward-compatible convenience method that either saves or loads a board.
     * <ul>
     *   <li>If {@code save} is {@code true}, the {@code boardState} is serialized to {@code file} and {@code null} is returned.</li>
     *   <li>If {@code save} is {@code false}, the method ignores {@code boardState}, reads {@code file}, and returns the loaded board.</li>
     * </ul>
     *
     * @param boardState the rectangular board to save when {@code save} is true; ignored when loading
     * @param file       the file to write to or read from; must be accessible
     * @param save       {@code true} to save; {@code false} to load
     * @return {@code null} when saving; the loaded rectangular board when loading
     * @throws IOException if any I/O or parsing error occurs
     * @throws IllegalArgumentException if {@code save} is true and {@code boardState} is not rectangular
     */
    public static Piece[][] saveOrLoadGame(Piece[][] boardState, File file, boolean save) throws IOException {
        if (save) {
            saveGame(boardState, file);
            return null;
        } else {
            return loadGame(file);
        }
    }

    /**
     * Serializes the given rectangular board to the specified file using the class's minimal JSON-like format.
     *
     * @param boardState the non-null rectangular board to serialize (rows ≥ 0, columns ≥ 0)
     * @param file       the destination file; will be created or overwritten
     * @throws IOException if an I/O error occurs during writing
     * @throws IllegalArgumentException if {@code boardState} is {@code null}, has a {@code null} row,
     *                                  or rows have different lengths (non-rectangular)
     * @see #loadGame(File)
     */
    public static void saveGame(Piece[][] boardState, File file) throws IOException {
        requireRectangularBoard(boardState);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(serializeBoard(boardState));
        }
    }

    /**
     * Deserializes and returns a board from the given file, expecting the class's minimal JSON-like format.
     * The returned board can have any number of rows and columns, but all rows have the same length.
     *
     * @param file the source file containing a serialized rectangular board
     * @return a newly allocated rectangular board reflecting the file content
     * @throws IOException if the file cannot be read or the content is malformed/invalid
     * @see #saveGame(Piece[][], File)
     */
    public static Piece[][] loadGame(File file) throws IOException {
        String content;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder(4096);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString().trim();
        }
        return deserializeBoard(content);
    }

    /* ----------------------------- Serialization ----------------------------- */

    /**
     * Converts a rectangular board into the minimal JSON-like string representation used by this handler.
     * <p>
     * The output is deterministic and contains no extraneous whitespace inside tokens.
     * </p>
     *
     * @param board the validated rectangular board
     * @return the serialized string representation
     */
    private static String serializeBoard(Piece[][] board) {
        int rows = board.length;
        int cols = (rows == 0) ? 0 : board[0].length;

        // Rough capacity hint
        StringBuilder sb = new StringBuilder(Math.max(64, rows * Math.max(1, cols) * 20));
        sb.append('[');
        for (int r = 0; r < rows; r++) {
            if (r > 0) sb.append(','); LOG.fine("Adding one more row");
            sb.append('[');
            for (int c = 0; c < cols; c++) {
                if (c > 0) sb.append(','); LOG.fine("Adding one more piece");
                Piece p = board[r][c];
                if (p == null) {
                    sb.append("null");
                } else {
                    sb.append("{\"type\":\"")
                            .append(p.getClass().getSimpleName())
                            .append("\",\"white\":")
                            .append(p.isWhite())
                            .append('}');
                }
            }
            sb.append(']');
        }
        sb.append(']');
        return sb.toString();
    }

    /* ----------------------------- Deserialization ----------------------------- */

    /**
     * Parses the minimal JSON-like string into a rectangular board.
     * <p>
     * The parser expects an outer array of row arrays. Each row can have any number of items, but all rows
     * must have the same number of columns (rectangular). Each item is either {@code null} or an object of the form
     * {@code {"type":"ClassName","white":true/false}}.
     * </p>
     *
     * @param content the serialized board string (whitespace allowed around tokens)
     * @return a rectangular {@code Piece[][]} instance matching the parsed content
     * @throws IOException if the content is empty, malformed, or rows differ in length
     */
    private static Piece[][] deserializeBoard(String content) throws IOException {
        if (content.isEmpty()) {
            throw new IOException("Empty file content.");
        }

        int idx = skipWs(content, 0);
        if (idx >= content.length() || content.charAt(idx) != '[') {
            throw new IOException("Expected '[' at start.");
        }
        idx++;

        List<Piece[]> rows = new ArrayList<>();
        Integer expectedCols = null;

        while (true) {
            idx = skipWs(content, idx);
            if (idx >= content.length()) throw new IOException("Unexpected end while reading rows.");

            char ch = content.charAt(idx);
            if (ch == ']') {
                idx++; // end of top-level array
                break;
            }

            if (ch != '[') {
                throw new IOException("Expected '[' to start a row.");
            }
            idx++;

            List<Piece> rowItems = new ArrayList<>();

            while (true) {
                idx = skipWs(content, idx);
                if (idx >= content.length()) throw new IOException("Unexpected end while reading row items.");

                ch = content.charAt(idx);
                if (ch == ']') {
                    idx++; // end of row
                    break;
                }

                // Parse item: either null or object
                if (startsWith(content, idx, "null")) {
                    rowItems.add(null);
                    idx += 4;
                } else if (ch == '{') {
                    int objEnd = findMatchingBrace(content, idx);
                    if (objEnd < 0) throw new IOException("Unclosed object starting at " + idx);
                    String obj = content.substring(idx + 1, objEnd); // inside braces
                    idx = objEnd + 1;

                    String type = parseStringField(obj, "type");
                    Boolean white = parseBooleanField(obj, "white");
                    if (type == null || white == null) {
                        throw new IOException("Invalid piece object: " + obj);
                    }
                    rowItems.add(createPiece(type, white));
                } else {
                    throw new IOException("Unexpected token at index " + idx + ": '" + ch + "'");
                }

                // After item, expect ',' or ']' (handled by loop top)
                idx = skipWs(content, idx);
                if (idx < content.length() && content.charAt(idx) == ',') {
                    idx++; // next item
                }
            }

            // Check rectangularity against first row
            if (expectedCols == null) {
                expectedCols = rowItems.size();
            } else if (rowItems.size() != expectedCols) {
                throw new IOException("Non-rectangular board: row has " + rowItems.size()
                        + " columns but expected " + expectedCols + ".");
            }

            // Move to next row or end
            rows.add(rowItems.toArray(new Piece[0]));
            idx = skipWs(content, idx);
            if (idx < content.length() && content.charAt(idx) == ',') {
                idx++;
            }
        }

        // Allow trailing whitespace only
        idx = skipWs(content, idx);
        if (idx != content.length()) {
            throw new IOException("Unexpected content after board end at index " + idx);
        }

        // Build final matrix
        int rowCount = rows.size();
        int colCount = (expectedCols == null) ? 0 : expectedCols;
        Piece[][] board = new Piece[rowCount][colCount];
        for (int r = 0; r < rowCount; r++) {
            System.arraycopy(rows.get(r), 0, board[r], 0, colCount);
        }
        return board;
    }

    /* ----------------------------- Helpers ----------------------------- */

    /**
     * Validates that the given board is non-null and rectangular (all rows present and same length).
     *
     * @param board the board to validate
     * @throws IllegalArgumentException if the board is null, has a null row, or is not rectangular
     */
    private static void requireRectangularBoard(Piece[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("Board must be non-null.");
        }
        int rows = board.length;
        int cols = (rows == 0) ? 0 : (board[0] == null ? -1 : board[0].length);
        if (cols < 0) {
            throw new IllegalArgumentException("Row 0 must be non-null.");
        }
        for (int r = 0; r < rows; r++) {
            if (board[r] == null) {
                throw new IllegalArgumentException("Row " + r + " must be non-null.");
            }
            if (board[r].length != cols) {
                throw new IllegalArgumentException("All rows must have the same length. Row " + r
                        + " has length " + board[r].length + " but expected " + cols + ".");
            }
        }
    }

    /**
     * Skips ASCII whitespace characters (space, tab, CR, LF) starting at index {@code i}.
     *
     * @param s the source string
     * @param i the starting index
     * @return the first index at or after {@code i} that is not whitespace
     */
    private static int skipWs(String s, int i) {
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c != ' ' && c != '\n' && c != '\r' && c != '\t') break;
            i++;
        }
        return i;
    }

    /**
     * Checks whether string {@code s} starts with token {@code t} at position {@code i}.
     *
     * @param s the source string
     * @param i the position in {@code s}
     * @param t the token to match
     * @return {@code true} if {@code s} has {@code t} at {@code i}; otherwise {@code false}
     */
    private static boolean startsWith(String s, int i, String t) {
        int n = t.length();
        return i + n <= s.length() && s.regionMatches(i, t, 0, n);
    }

    /**
     * Finds the matching closing brace for an object starting at {@code start}, accounting for nested braces.
     * <p>
     * The input is assumed not to contain quoted braces that would confuse nesting.
     * </p>
     *
     * @param s     the source string
     * @param start index of the opening brace '{'
     * @return index of the matching '}' or {@code -1} if not found
     */
    private static int findMatchingBrace(String s, int start) {
        int depth = 0;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    /**
     * Extracts a string field with the form {@code "key":"value"} from a simple object string.
     * <p>
     * This method is tailored for the serializer's output and does not handle escapes or reordering of fields.
     * </p>
     *
     * @param obj the object content (without outer braces)
     * @param key the field name to extract
     * @return the string value if present, otherwise {@code null}
     */
    private static String parseStringField(String obj, String key) {
        String needle = "\"" + key + "\":\"";
        int k = obj.indexOf(needle);
        if (k < 0) return null;
        int vStart = k + needle.length();
        int vEnd = obj.indexOf('"', vStart);
        if (vEnd < 0) return null;
        return obj.substring(vStart, vEnd);
    }

    /**
     * Extracts a boolean field with the form {@code "key":true/false} from a simple object string.
     *
     * @param obj the object content (without outer braces)
     * @param key the field name to extract
     * @return {@code Boolean.TRUE}/{@code Boolean.FALSE} if present; otherwise {@code null}
     */
    private static Boolean parseBooleanField(String obj, String key) {
        String needle = "\"" + key + "\":";
        int k = obj.indexOf(needle);
        if (k < 0) return null;
        int vStart = k + needle.length();
        int vEnd = obj.indexOf(',', vStart);
        if (vEnd < 0) vEnd = obj.length();
        String token = obj.substring(vStart, vEnd).trim();
        if ("true".equals(token)) return Boolean.TRUE;
        if ("false".equals(token)) return Boolean.FALSE;
        return null;
    }

    /**
     * Creates a concrete {@link Piece} instance from the given type name and color flag.
     * <p>
     * Supported type names: {@code Pawn}, {@code Rook}, {@code Knight}, {@code Bishop}, {@code Queen}, {@code King}.
     * Unknown types yield {@code null}.
     * </p>
     *
     * @param type    the simple class name of the piece
     * @param isWhite {@code true} for white; {@code false} for black
     * @return a new piece instance or {@code null} if the type is not recognized
     */
    private static Piece createPiece(String type, boolean isWhite) {
        switch (type) {
            case "Pawn":   return new Pawn(isWhite);
            case "Rook":   return new Rook(isWhite);
            case "Knight": return new Knight(isWhite);
            case "Bishop": return new Bishop(isWhite);
            case "Queen":  return new Queen(isWhite);
            case "King":   return new King(isWhite);
            default:       return null;
        }
    }
}