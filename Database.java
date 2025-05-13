import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:tetris_game.db"; // Database URL

    // Establishes a connection to the SQLite database
    public static Connection connect() throws SQLException {
        try {
            // Ensure the JDBC driver is loaded
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL);
    }

    // Create tables if they do not exist
    public static void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS top_scores ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + "name TEXT NOT NULL, "
                                + "score INTEGER NOT NULL "
                                + ");";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // Execute the SQL statement to create the table
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Method to insert a new score
    public static void insertScore(String name, int score) {
        String insertSQL = "INSERT INTO top_scores (name, score) VALUES (?, ?);";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            System.out.println("Score inserted.");
        } catch (SQLException e) {
            System.out.println("Error inserting score: " + e.getMessage());
        }
    }

    // Method to retrieve the top 10 scores from the database
    public static List<String> getTopScores() {
        List<String> topScores = new ArrayList<>();
        String query = "SELECT name, score FROM top_scores ORDER BY score DESC LIMIT 10"; // Fix the table name

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Loop through the results and add them to the topScores list
            while (rs.next()) {
                String playerName = rs.getString("name"); // Fix the column name
                int score = rs.getInt("score");
                topScores.add(playerName + ": " + score);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching top scores: " + e.getMessage());
        }

        return topScores;
    }
}
