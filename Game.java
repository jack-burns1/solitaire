// game

package solitaire;

import java.util.*;

public class Game {

	private static int POOLSIZE = 7;
	private static int WINSIZE = 4;	
	private static String BLANK = "       ";
	private static String CARD_PAD = "                              ";

	public static void main(String[] args) 
	{
		Game sltr = new Game();

		Scanner in = new Scanner(System.in);
		String input = " ";

		sltr.waitForUser();

		boolean over = false;

		while (!input.equalsIgnoreCase("q") && !over) {
			System.out.print(CARD_PAD + "Type in \"dir\" if you want to go back to the directions page.\n\n");
			System.out.print(CARD_PAD + "Next move: ");
			input = in.nextLine();

			input = input.toLowerCase();
			input = input.trim();

			clearScreen();
			sltr.direct_input(input);
			over = sltr.game_over();
		}
		System.out.print("\033[0m");
		clearScreen();
	}

	public static void clearScreen()
	{  
    	System.out.print("\033[H\033[2J");  
    	System.out.flush();  
	}

	// Keeps the user in a loop until they start a new game
	// and display initial directions
	public void waitForUser()
	{
		Scanner in = new Scanner(System.in);
		String input = " ";

		Printer print = new Printer();
		clearScreen();
		print.intro(); // Welcome screen
		clearScreen();

		print.directions(); // Directions

		while (!input.equalsIgnoreCase("n")) {
			input = in.nextLine();	
		}

		startGame();
	}

	// Calls the necessary functions to create a new game
	public void startGame()
	{
		initialize_vars();
		shuffle_deck();
		deal_cards();
		clearScreen();
		printGame();
	}

	// Initializes all important variables for a new game
	public void initialize_vars()
	{
		mainDeck = new Deck(); 
		recycleMain = new Deck(0); 

		cardPool = new Vector <Deck>(POOLSIZE);
		top = new Card(-1, -1, false);
		winningDecks = new Vector <Deck>(WINSIZE);

		for (int i = 0; i < WINSIZE; i++) {
			winningDecks.add(new Deck(0));
		}
	}

	// Creates a 52 card deck that is randomly shuffled
	public void shuffle_deck()
	{
		Vector <Integer> usedIndex = new Vector <>(53);
		usedIndex.add(-1);

		int suit = 0;
		int value = 1;
		int randNum = -1;
		int arraySize = 0;

		for (int i = 0; i < 52; i+= 4) {
			suit = 0;
			for (int k = 0; k < 4; k++) {
				Card newCard = new Card(value, suit, true);

				while (usedIndex.contains(randNum) && arraySize < 53) {
					Random rand = new Random();
					randNum = rand.nextInt(52);					
				}

				mainDeck.set_card_at(randNum, newCard);
				usedIndex.add(randNum);

				arraySize++;
				suit++;
			}
			value++;
		}
	}

	public void deal_cards()
	{
		int numDecks = POOLSIZE;
		for (int i = 0; i < numDecks; i++) {
			cardPool.add(new Deck(0));	
		}	

		while (numDecks > 0) {
			for(int i = 0; i < numDecks; i++) {
				Card dealt = mainDeck.pop_deck();
				dealt.changeFlipped(true);

				if (i == numDecks - 1) {
					dealt.changeFlipped(false);
				}
				Deck biggerDeck = cardPool.get(i);
				biggerDeck.add_to_back(dealt);
				cardPool.set(i, biggerDeck);

			}
			numDecks--;
		}

	}

	public void firstCard(int val, int suit)
	{
		Deck destination = new Deck(1);

		if (val == 1) {
			destination = winningDecks.get(suit);
			move_to_empty_deck(val, suit, destination);
		}

		else if (val == 13) {
			boolean moved = false;

			for (int i = 0; i < POOLSIZE; i++) {

				destination = cardPool.get(i);
				if (destination.deck_size() == 0) {

					move_to_empty_deck(val, suit, destination);
					moved = true;
					break;
				}
			}

			if (!moved) {
				System.out.print("Invalid move...(1) try again.\n");
				return;
			}
		}

		else {
			Printer err = new Printer();
			clearScreen();
			printGame();
			err.format_error();
			return;

		}
	}

	public void move_to_empty_deck(int val, int suit, Deck destination)
	{
		Deck toMove = new Deck(0);
		Deck oDeck;

		if (top.isCard(val, suit)) {
			toMove.add_to_back(recycleMain.rem_last_card());
			top = new Card(-1, -1, false);
		}

		else {
			int deckNum = get_from_pool(val, suit, false);
			if (deckNum == 0 ) {

				System.out.print("Invalid move...(2) try again.\n");
				return;
			}
			oDeck = cardPool.get(deckNum - 1);

			toMove = get_origin(val, suit, deckNum - 1);
		}

		destination.concatinate(toMove);
	}

	public boolean game_over()
	{
		if (mainDeck.deck_size() != 0) {
			return false;
		}
		Deck currDeck;
		for (int i = 0; i < WINSIZE; i++) {

			currDeck = winningDecks.get(i);
			if (currDeck.deck_size() != 13) {
				return false;
			}
		}

		clearScreen();
		Printer youWin = new Printer();

		if (!youWin.win()) {
			startGame();
			return false;
		}
		return true;
	}

	private boolean is_move(int val1, int suit1, int val2, int suit2)
	{
		Deck toMove = new Deck(0);
		int found1 = 0;
		int found2 = 0;

		if (top.isCard(val1, suit1)) {
			found1 = -2;
		}

		found2 = get_from_pool(val2, suit2, true);
		if (found1 == 0) {
			found1 = get_from_pool(val1, suit1, false);
		}
		
		if (found2 == 0) {
			found2 = get_from_win(val2, suit2);
		}

		if (!compatableCards(val1, suit1, val2, suit2, found2)) {
			return false;
		}

		if (found1 != 0 && found2 != 0) {
			if (found1 == -2) {
				toMove.add_to_back(recycleMain.rem_last_card());
				top = new Card(-1, -1, false);
			}

			else {
				toMove = get_origin(val1, suit1, (found1 - 1));
			}
			if (toMove.deck_size() > 1 && found2 < 0) {
				return false;
			}

			move_cards(toMove, found2);
			return true;
		}
		return false;
	}

	public int get_from_pool(int val, int suit, boolean destination)
	{
		Deck currDeck;
		int size;
		for (int i = 0; i < POOLSIZE; i++) {
			currDeck = cardPool.get(i);

			if (!destination && val != 1) {
				if(currDeck.contains(val, suit) != -1) {
					return i + 1;
				}
			}
			else {
				size = currDeck.deck_size();
				if (size != 0 && currDeck.get_card_at(size -1).isCard(val, suit)) {
					return i + 1;
				}
			}
		}
		return 0;
	}

	// Returns deck index of card
	public int get_from_win(int val, int suit)
	{
		Deck currDeck;
		int size;

		for (int i = 0; i < WINSIZE; i++) {
			currDeck = winningDecks.get(i);
			size = currDeck.deck_size();

			if (size == 0) {
				continue;
			}
			if (currDeck.get_card_at(size -1).isCard(val, suit)) {
				i++;
				i *= -1;  // return negative value of index to distinguish
						  // winning/pool
				return i;
			}
		}
		return 0;
	}

	private boolean compatableCards(int val1, int suit1, int val2, int suit2,
		int found2)
	{
		/* adding to a winning deck */
		if (found2 < 0 && suit1 == suit2 && val1 - val2 == 1) {
			return true;
		}

		if (val2 - val1 != 1) {
			return false;
		}

		suit1 = suit1 % 2;
		suit2 = suit2 % 2;

		switch (suit1) {
			case 0:
				if (suit2 == 1) {
					return true;
				}
			case 1:
				if (suit2 == 0) {
					return true;
				}
		}
		return false;
	}

	public void move_cards(Deck toMove, int found2)
	{
		if (found2 < 0) {
			found2 *= -1;
			(winningDecks.get(found2 - 1)).concatinate(toMove);
		}

		else {
			(cardPool.get(found2 - 1)).concatinate(toMove);
		}
	}

	public Deck get_origin(int val, int suit, int deckNum)
	{
		Deck rDeck = new Deck(0);
		Deck currDeck = cardPool.get(deckNum);

		int cardIndex = currDeck.contains(val, suit);
		int size = currDeck.deck_size();

		while (cardIndex < size) {
			rDeck.add_to_front(currDeck.rem_last_card());
			cardIndex++;
			
		}
		return rDeck;
	}

	public void printGame()
	{
		/* goes through height of a card image */
		for (int i = 0; i < 6; i++) {

			System.out.print(CARD_PAD);
			print_one_Row_Top(i);
			System.out.print("\n");
		}

		System.out.print("\n\n\n");

		int max = longest_deck();
		for (int i = 0; i < max; i++) {

			System.out.print(CARD_PAD);
			print_one_Row_Bot(i);
			System.out.print("\n");
		}
		System.out.print("\n\n");
		
	}

	public int longest_deck()
	{
		Deck currDeck;
		int size = 0;

		for (int i = 0; i < POOLSIZE; i++) {
			currDeck = cardPool.get(i);
			if (size < currDeck.deck_size())	{
				size = currDeck.deck_size();
			}	
		}
		size *= 2;
		return size + 4;
	}

	private void print_one_Row_Top(int index)
	{
		if (mainDeck.deck_size() > 0) {
			(mainDeck.get_card_at(0)).printRow(index);
		}
		else {
			System.out.print(BLANK);
		}
		System.out.print(BLANK);

		if (top.getVal() != -1) {
			top.printRow(index);
			System.out.print(BLANK);
		}
		else {
			System.out.print(BLANK + BLANK);
		}
		System.out.print(BLANK);

		Deck currDeck;
		for (int i = 0; i < WINSIZE ; i++) {
			currDeck = winningDecks.get(i);
			if (currDeck.deck_size() > 0) {
		 		(currDeck.get_card_at(currDeck.deck_size() - 1)).printRow(index);
			}
			else {
				System.out.print(BLANK);
			}
			System.out.print(BLANK);
		}
	}

	private void print_one_Row_Bot(int index)
	{
		System.out.print(BLANK);
		Deck currDeck;
		Card currCard;
		int size;
		int cardIndex = index / 2;

		for (int i = 0; i < POOLSIZE; i++) {
			currDeck = cardPool.get(i);
			size = currDeck.deck_size();
			if (size == 0) {
				System.out.print(BLANK + BLANK);
				continue;
			}

			if (size >= (cardIndex + 1)) {
				if (index % 2 == 0) {
					(currDeck.get_card_at(cardIndex)).printRow(0);
				}
				else {
					(currDeck.get_card_at(cardIndex)).printRow(1);
				}

			}
			else if (index < ((size - 1) * 2) + 6) {
				int diff = (((size - 1) * 2) + 6) - index;
				currDeck.get_card_at(size - 1).printRow(6 - diff);
			}
			else {
				System.out.print(BLANK);
			}
			System.out.print(BLANK);
		}
	}

	private void draw_from_deck()
	{
		if (mainDeck.deck_size() == 0) {
			if (recycleMain.deck_size() == 0) {
				return;
			}

			mainDeck.erase_deck();
			int new_size = recycleMain.deck_size();

			for (int i = 0; i < new_size; i++) {
				Card c = recycleMain.get_card_at(i);
				c.changeFlipped(true);
				mainDeck.add_to_back(c);
			}

			recycleMain.erase_deck();
		}

		Card popped = mainDeck.pop_deck();
		top = popped;
		top.changeFlipped(false);
		recycleMain.add_to_back(popped);
	}

	public void direct_input(String input)
	{
		Printer print = new Printer();

		if (input.length() == 0) {
			clearScreen();
			printGame();
			print.format_error();
			return;
		}

		char command;
		if (input.equalsIgnoreCase("dir")) {
			print.directions_with_back();
			clearScreen();
			printGame();
			return;
		}

		else if (input.charAt(0) == 'd') {
			draw_from_deck();
			printGame();
			return;
		}

		else if (input.charAt(0) == 'f') {
			flip_cards();
			printGame();
			return;
		}

		else if (input.charAt(0) == 'n') {
			clearScreen();
			if (print.new_game()) {
				clearScreen();
				startGame();
			}
            clearScreen();
			printGame();
			return;
		} 

		else if (!input.equalsIgnoreCase("q")){
			parse_input(input);
			return;
		}

	}

	public void parse_input(String input)
	{
		input = input.trim();
		String origin, destination;
		int size = input.length();
		Printer err = new Printer();

		if (size == 2) {
			split_input(input, " ");
			return;
		}

		int separator = input.lastIndexOf(" ");

		if (separator == -1) {
			clearScreen();
			printGame();
			err.format_error();
			return;
		}

		origin = input.substring(0, separator);
		destination = input.substring(separator + 1);
		origin = origin.toLowerCase();
		destination = destination.toLowerCase();

		int oLen = origin.length();
		int dLen = destination.length();

		if (oLen < 2 || oLen > 3) { 
			clearScreen();
			printGame();
			err.format_error();
			return;
		}

		if (dLen < 2 || dLen > 3) {
			clearScreen();
			printGame();
			err.format_error();
			return;
		}
		split_input(origin, destination);
	}

	public void split_input(String origin, String destination)
	{
		int suit1, suit2, val1, val2;
		String oVal, dVal;
		Printer err = new Printer();

		if (destination == " ") {
			oVal = origin.substring(0, 1);
			suit1 = origin.charAt(1);
			suit1 = (suit1 % 6) - 1;

			oVal = face_card_to_string(oVal);
			val1 = Integer.parseInt(oVal);

			firstCard(val1, suit1);
			printGame();
			return;
		}

		int oLast = origin.length() - 1;
		int dLast = destination.length() - 1;

		suit1 = origin.charAt(oLast);
		suit2 = destination.charAt(dLast);
		suit1 = (suit1 % 6) - 1; 
		suit2 = (suit2 % 6) - 1;

		oVal = origin.substring(0, oLast);
		dVal = destination.substring(0, dLast);

		oVal = face_card_to_string(oVal);
		dVal = face_card_to_string(dVal);

		if (!checkVal(oVal) || !checkVal(dVal)) {
			clearScreen();
			printGame();
			err.format_error();
			return;
		}

		val1 = Integer.parseInt(oVal);
		val2 = Integer.parseInt(dVal);

		if (!is_move(val1, suit1, val2, suit2)) {
			System.out.print("Invalid Move... try again\n");
			System.out.print("\"" + origin + " " + destination + "\"\n");
			return;
		}
		printGame();

	}

	public String face_card_to_string(String val)
	{
		if (val.equals("j")) {
			return "11";
		}
		else if (val.equals("q")) {
			return "12";
		}
		else if (val.equals("k")) {
			return "13";
		}
		else if (val.equals("a")) {
			return "1";
		}
		return val;
	}

	public boolean checkVal(String value)
	{
		switch (value) {
			case "1":
				return true;
			case "2":
				return true;
			case "3":
				return true;
			case "4":
				return true;
			case "5":
				return true;
			case "6":
				return true;
			case "7":
				return true;
			case "8":
				return true;
			case "9":
				return true;
			case "10":
				return true;
			case "11":
				return true;
			case "12":
				return true;
			case "13":
				return true;
		}

		return false;
	}

	public void flip_cards()
	{
		Deck currDeck;
		int deckSize;
		for (int i = 0; i < POOLSIZE; i++) {
			currDeck = cardPool.get(i);
			deckSize = currDeck.deck_size();
			if (deckSize != 0) {
				Card toFlip = currDeck.get_card_at(deckSize - 1);
				toFlip.changeFlipped(false);
			}
		}
	}

	/* Large deck to draw cards from */
	private Deck mainDeck;
	/* cards that have been drawn to be added back to mainDeck */
	private Deck recycleMain;
	/* 7 decks in front of player */
	private Vector <Deck> cardPool;
	/* 4 decks the player must stack in order */
	private Vector <Deck> winningDecks;
	/* Top of the deck */
	private Card top;
};
