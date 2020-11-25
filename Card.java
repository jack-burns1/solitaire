// Card.java
// Author: Jack Burns
// May 2020
// Imlementation of a Card which holds a numerical value, and suit, and can be flipped
// over or not.

package solitaire;

import java.util.Vector;

public class Card {

	// Ansi commands for text (T) and background (B)
	private static String BLUE_T  = "\u001b[34m";
	private static String BLACK_T = "\u001b[30m";
	private static String WHITE_B = "\033[0m";
	private static String GREEN_B = "\u001b[42;1m";
	private static String RED_T   = "\u001b[31m";

	// Creates a new card given the numerical value, suit, and whether or not it is flipped.
	public Card(int newVal, int newSuit, boolean flip)
	{
		value = newVal;
		suit = newSuit;
		// Values must be 2 character ('01' - ' K')
		if (value == 10) {
			name = "10";
		}
		else if (value == 11) {
			name = " J";
		}
		else if (value == 12) {
			name = " Q";
		}
		else if (value == 13) {
			name = " K";
		}
		else if (value == 1) {
			name = " A";
		}
		else {
			name = "0" + value;
		}

		flipped = flip;
		image = new Vector <String>();
		setImage();
	}
	// Sets the image vector to hold lines of ascii art
	// Flipped cards all have the default image, and the rest
	// have images corrosponding to their suits
	public void setImage()
	{
		if (flipped) {
			image.add(BLUE_T + "._____." + BLACK_T);
			image.add(BLUE_T + "|?    |" + BLACK_T);
			image.add(BLUE_T + "|     |" + BLACK_T);
			image.add(BLUE_T + "|     |" + BLACK_T);
			image.add(BLUE_T + "|     |" + BLACK_T);
			image.add(BLUE_T + "|___ ?|" + BLACK_T);
		}
		// 1 = hearts
		else if (suit == 1) {
			image.add(RED_T + "._____." + BLACK_T);
			image.add(RED_T + "|" + name + " _h|" + BLACK_T);
			image.add(RED_T + "|( v )|" + BLACK_T);
			image.add(RED_T + "| \\ / |" + BLACK_T);
			image.add(RED_T + "|  .  |" + BLACK_T);
			image.add(RED_T + "|___" + name + "|" + BLACK_T);
		}
		// 0 = spades
		else if (suit == 0) {
			image.add("._____.");
			image.add("|" + name + ". s|");
			image.add("| /.\\ |");
			image.add("|(_._)|");
			image.add("|  |  |");
			image.add("|___" + name + "|");
		}
		// 3 = diamonds
		else if (suit == 3) {
			image.add(RED_T + "._____." + BLACK_T);
			image.add(RED_T + "|" + name + "^ d|" + BLACK_T);
			image.add(RED_T + "| / \\ |" + BLACK_T);
			image.add(RED_T + "| \\ / |" + BLACK_T);
			image.add(RED_T + "|  .  |" + BLACK_T);
			image.add(RED_T + "|___" + name + "|" + BLACK_T);
		}
		// 2 = clubs
		else if (suit == 2) {
			image.add("._____.");
			image.add("|" + name + "_ c|");
			image.add("| ( ) |");
			image.add("|(_'_)|");
			image.add("|  |  |");
			image.add("|___" + name + "|");
		}
	}

	// Returns the numerical value of each card (1 - 13)
	public int getVal()
	{
		return value;
	}
	// Returns the numerical value of a card's suit (0 - 3)
	public int getSuit()
	{
		return suit;
	}

	// Returns whether the card is flipped or not
	public boolean isFlipped()
	{
		return flipped;
	}

	// Prints a single row of a card, stored in the image vector
	public void printRow(int index)
	{
		System.out.print(image.get(index));
	}

	// Changes whether a card is flipped or not, and changes the image
	// accordingly.
	public void changeFlipped(boolean isFlipped)
	{
		flipped = isFlipped;
		image.clear();
		setImage(); // Image will change since flipped is changed
	}

	//Checks if a given card matches a value and suit.
	public boolean isCard(int val, int s)
	{
		if (value == val && suit == s) {
			return true;
		}
		return false;
	}

	private int value;
	private int suit;
	private String name;
	private Vector <String> image;
	private boolean flipped;

};