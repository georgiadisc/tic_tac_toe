package com.github.georgiadisc.tictactoe;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.Test;

/**
 * Unit tests for the Tic-Tac-Toe game.
 */
public class GameTest {
  
  // The text scanner used for the users positions
  private static Scanner scanner;
  
  // The file containing the test cases
  private static File file;
  
  // The game instance
  private Game game;

  /**
   * Checks for a n-tuple on the last position's row.
   */
  @Test
  public void shouldEndWithRowWin() {
    openFile("row");
    playTheGame();
    assertTrue("Τhe n-tuple on the last position's row was not completed", game.traverseRow());
  }

  /**
   * Checks for a n-tuple on the last position's column.
   */
  @Test
  public void shouldEndWithColWin() {
    openFile("col");
    playTheGame();
    assertTrue("Τhe n-tuple on the last position's column was not completed", game.traverseCol());
  }

  /**
   * Checks for a n-tuple on the diagonal.
   */
  @Test
  public void shouldEndWithDiagWin() {
    openFile("diag");
    playTheGame();
    assertTrue("Τhe n-tuple on the diagonal was not completed", game.traverseDiag());
  }

  /**
   * Checks for a n-tuple on the antidiagonal.
   */
  @Test
  public void shouldEndWithAntiDiagWin() {
    openFile("antidiag");
    playTheGame();
    assertTrue("Τhe n-tuple on the antidiagonal was not completed", game.traverseAntiDiag());
  }

  /**
   * Asserts that there is neither a winner nor available positions.
   */
  @Test
  public void shouldEndWithTie() {
    openFile("tie");
    playTheGame();
    assertTrue("Game didn't end in a tie", game.isTie());
  }

  private void playTheGame() {
    Player player = new Player("Player", false);
    Player computer = new Player("Computer", false);
    game = new Game(scanner, player, computer);
    game.showInstructions();
    while (!game.isTerminated()) {
      game.showBoard();
      game.setPosition();
      game.updateState();
    }
    game.showBoard();
    game.showResults();
  }

  private void openFile(String name) {
    file = new File(String.format("./test_cases/test_%s.txt", name));
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      assertNull(e);
    }
  }

  @AfterClass
  public static void cleanUp() {
    scanner.close();
  }
}
