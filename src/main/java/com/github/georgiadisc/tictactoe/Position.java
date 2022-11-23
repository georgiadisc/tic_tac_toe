package com.github.georgiadisc.tictactoe;

import java.util.Random;

public class Position {

  // The size of the board
  private static int boardSize = 3;

  // The random number generator
  private static final Random random = new Random();

  // The alphanumeric typed by the user
  private String input;

  // The board's row
  private int row;

  // The board's column
  private int col;

  /**
   * Constructs a new {@code Position} class. Used for non-random positions.
   *
   * @param input The alphanumeric typed by the user
   */
  public Position(String input) {
    this.input = input;
  }

  /**
   * Constructs a new {@code Position} class. Used for random positions generated
   * with {@link #getRandom} method.
   *
   * @param row The random row
   * @param col The random column
   */
  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  /**
   * Returns a valid, random position on the board.
   *
   * @return Position
   */
  public static Position getRandom() {
    return new Position(random.nextInt(boardSize), random.nextInt(boardSize));
  }

  /**
   * Resizes the board to {@code size}.
   *
   * <p>
   * Called by the constructor of the {@link Game} class when we want to change
   * the size of the board.
   *
   * @param size The size of the board
   */
  public static void setBoardSize(int size) {
    boardSize = size;
  }

  /**
   * Checks if the alphanumeric is valid, i.e. if it corresponds to any position
   * of the table.
   *
   * @return boolean
   */
  public boolean isValid() {
    if (input.length() == 2 && (input.charAt(0) >= 65 && input.charAt(0) <= 65 + boardSize - 1)
        && (input.charAt(1) >= 49 && input.charAt(1) <= 49 + boardSize - 1)) {
      parse();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks if the position given by the player is available on the board (if
   * there isn't an 'X' or 'O').
   *
   * @param board The game board
   * @return boolean
   */
  public boolean isAvailable(char[][] board) {
    return board[this.row][this.col] == 0;
  }

  /**
   * Checks if the player's position belongs to the diagonal of the board.
   *
   * <p>
   * Prevents unnecessary table access when searching for a n-tuple.
   *
   * @return boolean
   */
  public boolean isDiagonal() {
    return this.row == this.col;
  }

  /**
   * Checks if the player's position belongs to the antidiagonal of the board.
   *
   * <p>
   * Prevents unnecessary table access when searching for a n-tuple.
   *
   * @return boolean
   */
  public boolean isAntiDiagonal() {
    return this.row + this.col == boardSize - 1;
  }

  /**
   * Converts the alphanumeric value entered by the user to an array's row and
   * column.
   */
  private void parse() {
    this.row = input.charAt(1) - 49;
    this.col = input.charAt(0) - 65;
  }

  @Override
  public String toString() {
    final char rowChar = (char) (this.row + 49);
    final char colChar = (char) (this.col + 65);
    return new String(new char[] { colChar, rowChar });
  }
}
