//
package solitaire;

import java.util.*;

public class Move {

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

	public boolean is_move(int val1, int suit1, int val2, int suit2)
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

