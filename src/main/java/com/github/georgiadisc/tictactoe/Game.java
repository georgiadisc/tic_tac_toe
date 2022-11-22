package com.github.georgiadisc.tictactoe;

import java.util.Scanner;

public class Game {

  // The array in which player positions are stored
  private char[][] board;

  // The size of the board
  private int boardSize = 3;

  // True when the game is over
  private boolean isTerminated = false;

  // The player's last position, which allows us to skip unnecessary rows and
  // columns of the board, when looking for a n-tuple at end of each round
  private Position lastPosition;

  // The text scanner used for the users positions
  private final Scanner scanner;

  // The first player, assigned with the 'X' symbol
  private Player firstPlayer;

  // The second player, assigned with the 'O' symbol
  private Player secondPlayer;

  // The current player
  private Player currPlayer;

  // The game winner, if any
  private Player winner;

  // A StringBuilder, prevents unnecessary calls to methods that print to the
  // console (eg print and println)
  private static final StringBuilder console = new StringBuilder();

  /**
   * Constructs a new {@link Game} class that is responsible for running the Tic-Tac-Toe game.
   * 
   * <p>
   * In order to play against the computer, one player needs to be initialized with the
   * {@code random} parameter true.
   * 
   * @param scanner The text scanner
   * @param firstPlayer The first player, usually the user
   * @param secondPlayer The second player, usually the computer
   */
  public Game(Scanner scanner, Player firstPlayer, Player secondPlayer) {
    this.scanner = scanner;
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
    this.currPlayer = firstPlayer;
    this.board = new char[boardSize][boardSize];
    assignSymbols();
  }

  /**
   * Constructs a new {@link Game} class that is responsible for running the Tic-Tac-Toe game, with
   * board size {@code boardSize}.
   * 
   * <p>
   * In order to play against the computer, one player needs to be initialized with the
   * {@code random} parameter true.
   *
   * @param boardSize The board size
   * @param scanner The text scanner
   * @param firstPlayer The first player, usually the user
   * @param secondPlayer The second player, usually the computer
   */
  public Game(int boardSize, Scanner scanner, Player firstPlayer, Player secondPlayer) {
    Position.setBoardSize(boardSize);
    this.boardSize = boardSize;
    this.scanner = scanner;
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
    this.currPlayer = firstPlayer;
    this.board = new char[boardSize][boardSize];
    assignSymbols();
  }

  /**
   * Assigns the symbol 'X' to the first player and the symbol 'O' to the second player.
   */
  private void assignSymbols() {
    firstPlayer.setSymbol('X');
    secondPlayer.setSymbol('O');
  }

  /**
   * Shows the Tic-Tac-Toe game instructions.
   */
  public void showInstructions() {
    console.append("************\n");
    console.append("Tic-Tac-Toe!\n");
    console.append("************\n");
    console.append("\n");
    console.append("Please enter the column (");
    appendSequence('A', boardSize, false, false);
    console.append(") and then the row (");
    appendSequence('1', boardSize, false, false);
    console.append(") of your move.\n");
    flush(console);
  }

  /**
   * Appends a sequence of consecutive {@code characters} to the {@link #console}, starting from the
   * {@code firstChar}.
   * 
   * @param firstChar The first character
   * @param characters The number of characters we want to append
   * @param trimComma True if we want the comma to be omitted on every append
   * @param trimOr True if we want the string "or" to be omitted last append
   */
  private void appendSequence(char firstChar, int characters, boolean trimComma, boolean trimOr) {
    int i;
    for (i = 0; i < characters - 1; i++) {
      console.append(String.format("%c%s ", i + firstChar, trimComma ? "" : ","));
    }
    console.append(String.format("%s%c", trimOr ? "" : "or ", i + firstChar));
  }

  /**
   * Shows the game board with the selected positions.
   */
  public void showBoard() {
    console.append("\n");
    console.append("   ");
    appendSequence('A', boardSize, true, true);
    console.append("\n");
    for (int i = 0; i < boardSize; i++) {
      console.append(String.format("%d |", i + 1));
      for (int y = 0; y < boardSize; y++) {
        console.append((board[i][y] == 0 ? " " : board[i][y]) + "|");
      }
      console.append("\n");
    }
    console.append("\n");
    flush(console);
  }

  /**
   * Prints the alphanumeric contained in {@code StringBuilder} and then restores it to its original
   * state.
   * 
   * @param console The StringBuilder
   */
  private void flush(StringBuilder console) {
    System.out.print(console.toString());
    console.setLength(0);
  }

  /**
   * Displays the winner, if any. Otherwise informs that the game ended in a tie.
   */
  public void showResults() {
    if (winner != null) {
      if (winner.getName().equals(firstPlayer.getName())) {
        System.out.println(String.format("You(%s) win!", firstPlayer.getName()));
      } else {
        System.out.println(String.format("%s won :(", secondPlayer.getName()));
      }
    } else {
      System.out.println("Tie!");
    }
  }

  /**
   * Register a player's position on the board.
   * <ul>
   * <li>If the player does not enter a valid position (row and column) then a relevant message is
   * shown and we return to the game loop.</li>
   * <li>If the player enters an occupied position (there is already an 'X' or 'O') then a relevant
   * message is shown and we return to the game loop.</li>
   * <li>If the player enters a valid and unoccupied position then that position is registered at
   * the table.</li>
   * </ul>
   */
  public void setPosition() {
    Position position;
    boolean didRegister = false;
    do {
      System.out
          .print(String.format("%s Move (%c): ", currPlayer.getName(), currPlayer.getSymbol()));
      if (currPlayer.isRandom()) {
        do {
          position = Position.getRandom();
        } while (!position.isAvailable(board));
        didRegister = true;
        System.out.println(position.toString());
      } else {
        String move = scanner.next();
        position = new Position(move);
        if (position.isValid()) {
          if (position.isAvailable(board)) {
            didRegister = true;
          } else {
            System.out.println("\nThe space entered is already taken.\n");
          }
        } else {
          System.out.println(
              "\nInvalid Input: Please enter the column and row of your move (Example: A1).\n");
        }
      }
    } while (!didRegister);
    updateBoard(position);
    updateLastPosition(position);
    updateCurrPlayer();
  }

  /**
   * Registers the player's position on the board.
   * 
   * @param position The position
   */
  private void updateBoard(Position position) {
    board[position.getRow()][position.getCol()] = currPlayer.getSymbol();
  }

  /**
   * Updates the players last position.
   * 
   * @param position The current player's position
   */
  private void updateLastPosition(Position position) {
    lastPosition = position;
  }

  /**
   * Refreshes the current game player with the next one.
   */
  private void updateCurrPlayer() {
    if (currPlayer.getName().equals(firstPlayer.getName())) {
      currPlayer = secondPlayer;
    } else {
      currPlayer = firstPlayer;
    }
  }

  /**
   * Traverses the table in the following ways:
   * <ul>
   * <li>Horizontally</li>
   * <li>Vertically</li>
   * <li>Diagonally</li>
   * </ul>
   * in order to find a n-tuple.
   * <p>
   * If a n-tuple was found, it means that last player has won the game. Otherwise, the
   * {@link #isTie()} method is called to check if the game ended in a tie.
   */
  public void updateState() {
    if (traverseRow())
      return;
    if (traverseCol())
      return;
    if (lastPosition.isDiagonal() && traverseDiag())
      return;
    if (lastPosition.isAntiDiagonal() && traverseAntiDiag())
      return;
    isTie();
  }

  /**
   * Traverses the row in which lies the last position.
   * <ul>
   * <li>It first checks if the first element of the array is empty. In this case the method returns
   * false.</li>
   * <li>Otherwise, it compares this first element with the rest elements of the row until it finds
   * one with a different value than itself. If the loop terminates without returning false, we set
   * the last player as winner with the {@link #setWinner()} method and return the true.</li>
   * </ul>
   * 
   * @return boolean
   */
  boolean traverseRow() {
    final char symbol = board[lastPosition.getRow()][0];
    if (symbol != 0) {
      for (int y = 1; y < boardSize; y++) {
        if (board[lastPosition.getRow()][y] != symbol) {
          return false;
        }
      }
      setWinner();
      return true;
    }
    return false;
  }

  /**
   * Traverses the column in which lies the last position.
   * <ul>
   * <li>It first checks if the first element of the column is empty. In this case the method
   * returns false.</li>
   * <li>Otherwise, it compares this first element with the rest elements of the column until it
   * finds one with a different value than itself. If the loop terminates without returning false,
   * we set the last player as winner with the {@link #setWinner()} method and return the true.</li>
   * </ul>
   * 
   * @return boolean
   */
  boolean traverseCol() {
    final char symbol = board[0][lastPosition.getCol()];
    if (symbol != 0) {
      for (int i = 1; i < boardSize; i++) {
        if (board[i][lastPosition.getCol()] != symbol) {
          return false;
        }
      }
      setWinner();
      return true;
    }
    return false;
  }

  /**
   * Traverses the diagonal of the board.
   * <ul>
   * <li>It first checks if the first element of the diagonal is empty. In this case the method
   * returns false.</li>
   * <li>Otherwise, it compares this first element with the rest elements of the diagonal until it
   * finds one with a different value than itself. If the loop terminates without returning false,
   * we set the last player as winner with the {@link #setWinner()} method and return the true.</li>
   * </ul>
   * 
   * @return boolean
   */
  boolean traverseDiag() {
    final char symbol = board[0][0];
    if (symbol != 0) {
      for (int i = 1; i < boardSize; i++) {
        if (board[i][i] != symbol) {
          return false;
        }
      }
      setWinner();
      return true;
    }
    return false;
  }

  /**
   * Traverses the antidiagonal of the board.
   * <ul>
   * <li>It first checks if the first element of the antidiagonal is empty. In this case the method
   * returns false.</li>
   * <li>Otherwise, it compares this first element with the rest elements of the antidiagonal until
   * it finds one with a different value than itself. If the loop terminates without returning
   * false, we set the last player as winner with the {@link #setWinner()} method and return the
   * true.</li>
   * </ul>
   * 
   * @return boolean
   */
  boolean traverseAntiDiag() {
    final char symbol = board[0][boardSize - 1];
    if (symbol != 0) {
      for (int i = boardSize - 2; i >= 0; i--) {
        if (board[boardSize - i - 1][i] != symbol) {
          return false;
        }
      }
      setWinner();
      return true;
    }
    return false;
  }

  /**
   * Called when a player has won, i.e. when there has been filled at least one n-tuple in the
   * table.
   */
  private void setWinner() {
    winner = getLastPlayer();
    isTerminated = true;
  }

  /**
   * Returns the last played player.
   * 
   * @return Player
   */
  private Player getLastPlayer() {
    if (currPlayer.getName().equals(firstPlayer.getName())) {
      return secondPlayer;
    } else {
      return firstPlayer;
    }
  }

  /**
   * Checks if the batch ended in a tie, that is, if there is at least one vacancy. If there is no
   * vacancy the game ends.
   */
  boolean isTie() {
    for (int i = 0; i < boardSize; i++) {
      for (int y = 0; y < boardSize; y++) {
        if (board[i][y] == 0) {
          return false;
        }
      }
    }
    isTerminated = true;
    return true;
  }

  /**
   * Returns true when the game is over.
   * 
   * @return boolean
   */
  public boolean isTerminated() {
    return this.isTerminated;
  }
}
