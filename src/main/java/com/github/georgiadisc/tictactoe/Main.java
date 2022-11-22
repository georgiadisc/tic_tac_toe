package com.github.georgiadisc.tictactoe;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Player player = new Player("Player", false);
    Player computer = new Player("Computer", true);
    Game game = new Game(scanner, player, computer);
    game.showInstructions();
    while (!game.isTerminated()) {
      game.showBoard();
      game.setPosition();
      game.updateState();
    }
    game.showBoard();
    game.showResults();
    scanner.close();
  }
}
