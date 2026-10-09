import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.TreeMap;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



public class LoginPage {

          public static void main(String[] args) {
          new TicTacToe();
    }
}

class TicTacToe extends JFrame {
    private JButton buttons[][] = new JButton[4][4];
    private char[][] board = new char[4][4];
    private char currentPlayer = 'X';
    private TreeMap<Integer, String> moveHistory = new TreeMap<>();
    private JLabel statusLabel;
    private JLabel timeLabel;
    private int sec=0;
       

    public TicTacToe() {
         // Create a panel for status and timer
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        // Status label for player's turn
        statusLabel = new JLabel("🔥 Fire's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
        topPanel.add(statusLabel);

        // Timer label for current time
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(timeLabel);


        // Create grid panel for the game board
        JPanel gridPanel = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Segoe UI Emoji", Font.BOLD, 50));
                int fi = i;
                int fj = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // If the selected spot is empty, allow the move
                        if (board[fi][fj] == '\0') {
                            board[fi][fj] = currentPlayer;
                            moveHistory.put(fi * 4 + fj, String.valueOf(currentPlayer));
                            updateGrid();

                            // Check for win
                            if (checkWin(board, currentPlayer)) {
                           
                                String winner = (currentPlayer == 'X') ? "🔥 Fire" : "💧 Water";
                                JOptionPane.showMessageDialog(TicTacToe.this, winner + " wins! in "+sec+" seconds.");
                                resetGame();
                                return;
                            }

                            
                           
                            // Switch player
                            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                            statusLabel.setText((currentPlayer == 'X') ? "🔥 Fire's Turn" : "💧 Water's Turn");
                        } else {
                            JOptionPane.showMessageDialog(TicTacToe.this, "Invalid move. Try again.");
                        }
                    }
                });
                gridPanel.add(buttons[i][j]);
            }
        }

      setLayout(new BorderLayout());
     add(topPanel, BorderLayout.NORTH); // Timer and status panel at the top
        add(gridPanel, BorderLayout.CENTER);

        // Start a timer to update the time
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        
        setSize(1500, 3000); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

public void updateTime() {
    // Increment the timer every second
    sec++;

    // Update the timeLabel to display the current time in seconds
    int minutes = sec / 60;
    int seconds = sec % 60;

    // Format the time as "mm:ss"
    String formattedTime = String.format("%02d:%02d", minutes, seconds);
    timeLabel.setText("Time: " + formattedTime);
    }
    // Update grid and synchronize with board
    public void updateGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String s = set(i, j);
                buttons[i][j].setText(s);
                // Ensure board is updated as well
                if (s.equals("🔥")) board[i][j] = 'X';
                else if (s.equals("💧")) board[i][j] = 'O';
            }
        }
    }

    // Apply rules for determining the emoji (🔥 or 💧)
    public String set(int i, int j) {
        int fireCount = 0;
        int waterCount = 0;

        if (board[i][j] == 'O') {
            if (i > 0 && board[i - 1][j] == 'X') fireCount++;
            if (i < 3 && board[i + 1][j] == 'X') fireCount++;
            if (j > 0 && board[i][j - 1] == 'X') fireCount++;
            if (j < 3 && board[i][j + 1] == 'X') fireCount++;
        } else if (board[i][j] == 'X') {
            if (i > 0 && board[i - 1][j] == 'O') waterCount++;
            if (i < 3 && board[i + 1][j] == 'O') waterCount++;
            if (j > 0 && board[i][j - 1] == 'O') waterCount++;
            if (j < 3 && board[i][j + 1] == 'O') waterCount++;
        }

        if (fireCount == 2) {
            JOptionPane.showMessageDialog(this, "Fire overpowers! 🔥");
            board[i][j] = '\0';
            return " ";
        } else if (waterCount == 2) {
            JOptionPane.showMessageDialog(this, "Water overpowers! 💧");
            board[i][j] = '\0';
            return " ";
        } else if (fireCount == 3) {
            JOptionPane.showMessageDialog(this, "Fire sets ablaze! 🔥🔥🔥");
            return "🔥";
        } else if (waterCount == 3) {
            JOptionPane.showMessageDialog(this, "Water floods the area! 💧💧💧");
            return "💧";
        } else if (board[i][j] == 'X') {
            return "🔥";
        } else if (board[i][j] == 'O') {
            return "💧";
        }

        return "";
    }

    // Check for win conditions
    static boolean checkWin(char[][] board, char player) {
        for (int i = 0; i < 4; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player && board[i][3] == player) ||
                (board[0][i] == player && board[1][i] == player && board[2][i] == player && board[3][i] == player)) {
                return true;
            }
        }
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player && board[3][3] == player) ||
            (board[0][3] == player && board[1][2] == player && board[2][1] == player && board[3][0] == player)) {
            return true;
        }
        return false;
    }

   
    // Reset the game
    public void resetGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = '\0';
                buttons[i][j].setText("");
            }
        }
        currentPlayer = 'X';
        statusLabel.setText("🔥 Fire's Turn");
        sec=0;
        moveHistory.clear();
    }

    }
