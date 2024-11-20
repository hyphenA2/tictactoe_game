package Main;
import databaseConnection.DBconnection;
import java.sql.*;
import java.util.Scanner;

public class Structure {
    private final Scanner scanner = new Scanner(System.in);

    public void registerUser(String name, String code, String birthDate) throws SQLException {
        String query = "INSERT INTO users (name, password, birth_date) VALUES (?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, code);
            ps.setDate(3, Date.valueOf(birthDate));
            ps.executeUpdate();
        }
    }

    public boolean login(String name, String code) throws SQLException {
        String query = "SELECT * FROM users WHERE name = ? AND password = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, code);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void playGame() {
        char[][] board = new char[3][3];
        int moves = 0;
        boolean playerXTurn = true;

        while (true) {
            printBoard(board);
            System.out.println("Player " + (playerXTurn ? "X" : "O") + ", choose your move (row and column): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (board[row][col] == 0) {
                board[row][col] = playerXTurn ? 'X' : 'O';
                moves++;
                if (checkWin(board, playerXTurn ? 'X' : 'O')) {
                    printBoard(board);
                    System.out.println("Player " + (playerXTurn ? "X" : "O") + " wins!");
                    saveScore(1); // Save a win score
                    break;
                } else if (moves == 9) {
                    System.out.println("It's a draw!");
                    saveScore(0); // Save a draw score
                    break;
                }
                playerXTurn = !playerXTurn;
            } else {
                System.out.println("Cell is already taken. Try another move.");
            }
        }
    }

    private boolean checkWin(char[][] board, char symbol) {
        return (board[0][0] == symbol && board[0][1] == symbol && board[0][2] == symbol) ||
                (board[1][0] == symbol && board[1][1] == symbol && board[1][2] == symbol) ||
                (board[2][0] == symbol && board[2][1] == symbol && board[2][2] == symbol) ||
                (board[0][0] == symbol && board[1][0] == symbol && board[2][0] == symbol) ||
                (board[0][1] == symbol && board[1][1] == symbol && board[2][1] == symbol) ||
                (board[0][2] == symbol && board[1][2] == symbol && board[2][2] == symbol) ||
                (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }

    private void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == 0 ? "." : cell) + " ");
            }
            System.out.println();
        }
    }

    private void saveScore(int score) {
        // Dummy implementation for saving score
        System.out.println("Score saved: " + score);
    }
}
