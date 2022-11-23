package com.github.georgiadisc.tictactoe;

public class Player {

  // The player's name
  private final String name;

  // The player's symbol ('X' or 'O')
  private char symbol;

  // True if the player's positions are random, false otherwise
  private final boolean random;

  /**
   * Constructs a new {@code Player} class.
   * 
   * @param name   The player's name
   * @param random True if we want the player's positions to be random, otherwise
   *               false
   */
  public Player(String name, boolean random) {
    this.name = name;
    this.random = random;
  }

  /**
   * Checks if the player's positions are random.
   * 
   * @return boolean
   */
  public boolean isRandom() {
    return this.random;
  }

  public String getName() {
    return this.name;
  }

  public char getSymbol() {
    return symbol;
  }

  public void setSymbol(char symbol) {
    this.symbol = symbol;
  }

}
