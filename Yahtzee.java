/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		/* You fill this in */
		
		already = new ArrayList<Integer>();
		upperScore = new int[nPlayers];
		lowerScore = new int[nPlayers];
		total = new int[nPlayers];
		
		/* A Round */
		for (int j = 0; j < N_SCORING_CATEGORIES; j++) {
			for (int i = 1; i <= nPlayers; i++) {
				
				/* Chance 1 */
				firstRoll(i);
				
				/* Chance 2 */
				selectAndDice();
				/* Chance 3 */
				selectAndDice();
				
				/* SelectCategory */
				selectCategory(i);
			}
		}
		
		/* display upper score , upper bonus , lowerscore of each player */
		updateOtherScores();
		showWinner();

		
			
	}
	
	
	/* show the winner */
	private void showWinner() {
		int win = 0;
		for(int i = 0 ;i < total.length; i++){
			if ( i == 0 ) break;
			if( total[i] > total[i-1]) {
				win = i;
			}
		}
		
		display.printMessage("Congratulations! Winner is " + playerNames[win]);
	}
	
	/* display upper score , upper bonus , lowerscore of each player */
	private void updateOtherScores() {
		for(int i = 0; i < nPlayers;i++) {
			display.updateScorecard(7, i, upperScore[i]);
			if (upperScore[i] >= 63) {
				display.updateScorecard(8, i, 35);
			} else {
				display.updateScorecard(8, i, 0);
			}
			display.updateScorecard(16, i, lowerScore[i]);
		}		
	}
	
	/* the first time click rollButton */
	private void firstRoll(int i) {
		// print first message.
		display.printMessage(playerNames[i-1] + "'s turn. Press Roll Dice.");
		display.waitForPlayerToClickRoll(i);
		for(int j = 0; j < N_DICE; j++) {
			dice[j] = rgen.nextInt(1,6);
		}
		display.displayDice(dice);
	}
	
	/* select dice and click rollButton */
	private void selectAndDice() {
		// print select dice and press
		display.printMessage("Select dice you want to reroll, then press Roll Again.");
		display.waitForPlayerToSelectDice();
		for(int j = 0; j < N_DICE; j++) {
			if(display.isDieSelected(j)){
				dice[j] = rgen.nextInt(1, 6);
			}
		}
		display.displayDice(dice);
	}
	
	/* select catogory and display the score and total score */
	private void selectCategory(int i) {
		// print select category
		display.printMessage("Please select a category.");
		
		/* must select a different category */
		while(true) {
			cateNumber = display.waitForPlayerToSelectCategory();
			if (!already.contains(cateNumber)) {
				if (YahtzeeMagicStub.checkCategory(dice, cateNumber)) {
					already.add(cateNumber);
					break;
				}
			}
			display.printMessage("Already selected. Pick Another One.");
		}
		score = calculateScore(cateNumber);
		
		/* add upperScore, lowerScore, totalScore */
		if (cateNumber <= 6) {
			upperScore[i-1] += score;
		} else {
			lowerScore[i-1] += score;
		}
		total[i-1] += score;
		/* udpate score broad */
		display.updateScorecard(cateNumber, i, score);
		display.updateScorecard(17, i, total[i-1]);
	}
	

	/* calculate Score with Category */
	private int calculateScore(int cate) {
		int result = 0;
		switch (cate) {
		case ONES : result = addAllInt(dice, 1); break;
		case TWOS : result = addAllInt(dice, 2); break;
		case THREES : result = addAllInt(dice, 3); break;
		case FOURS : result = addAllInt(dice, 4); break;
		case FIVES : result = addAllInt(dice, 5); break;
		case SIXES : result = addAllInt(dice, 6); break;
		case THREE_OF_A_KIND : result = sumAll(dice); break;
		case FOUR_OF_A_KIND : result = sumAll(dice); break;
		case FULL_HOUSE : result = 25; break;
		case SMALL_STRAIGHT : result = 30; break;
		case LARGE_STRAIGHT : result = 40; break;
		case YAHTZEE : result = 50; break;
		case CHANCE : result = sumAll(dice); break;
		default : break;
		}
		return result;
	}
	
	/* sum all value */
	private int sumAll(int[] dice) {
		int result = 0;
		for(int i = 0; i < dice.length; i++) {
			result += dice[i];
		}
		return result;
	}
	
	/* sum all particular integer number */
	private int addAllInt(int[] dice, int var) {
		int result = 0;
		for(int i = 0; i < dice.length; i++) {
			if(dice[i]==var) {
				result += var;
			}
		}
		return result;
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[] dice = new int[N_DICE];
	private int cateNumber;
	private int score;
	private int[] upperScore;
	private int[] lowerScore;
	private int[] total;
	private ArrayList<Integer> already;
}
