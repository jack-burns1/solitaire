// Deck.java
// Author: Jack Burns
// May 2020
// Imlementation of a Deck which holds a vector of cards, and the size
// of the deck.

package solitaire;

import java.util.Vector;               

public class Deck {

	// Create a new deck of Cards
	// Initialized the deck with flipped cards with values that do not work
	public Deck()
	{
		cardDeck = new Vector <Card>(52);
		deckSize = 52;
		for (int i = 0; i < deckSize; i++) {
			Card blank = new Card(-1, -1, true); // Create "blank" cards
			cardDeck.add(blank);
		}
	}

	// Create a deck of cards that is a given size.
	public Deck(int size)
	{
		cardDeck = new Vector <Card>();
		deckSize = size;
	}

	// Changes the size of the deck to a given number
	public void set_size(int size)
	{
		deckSize = size;
	}

	// Returns the size of the deck
	public int deck_size()
	{
		return deckSize;
	}

	// Adds a given card to the back of the deck vector
	public void add_to_back(Card c)
	{
		cardDeck.add(c);
		deckSize++;
	}

	// Adds a given card to the front of the deck vector
	public void add_to_front(Card c)
	{
		cardDeck.add(0, c);
		deckSize++;
	}

	// Remove a card card (based on valaue and suit)from the deck and return the 
	// card
	public Card remove_at(int val, int suit)
	{
		int index = contains(val, suit);
		if (index == -1) {
			return null;
		}

		deckSize--;
		return cardDeck.remove(index);
	}

	// Remove first card from the deck and return the card
	public Card pop_deck()
	{
		if (deckSize == 0) {
			System.out.print("Cannot remove from an empty deck.");
			return null;
		}

		Card firstCard = cardDeck.firstElement();
		cardDeck.remove(firstCard);
		deckSize--;
	
		return firstCard;
	}

	// Remove the last card from the deck and return the card
	public Card rem_last_card()
	{
		if (deckSize == 0) {
			System.out.print("Cannot remove from an empty deck.");
			return null;
		}

		deckSize--;
		Card last = get_card_at(deckSize);
		cardDeck.remove(deckSize);
		return last;
	}

	// Return the card at a certain index without removing it
	public Card get_card_at(int index)
	{
		// if (index >= deckSize || index < 0) {
		// 	System.out.print("Invalid deck index");
		// 	return null;
		// }
		return cardDeck.get(index);
	}

	// Set a card at a given index to a given card
	// Returns true if successful, and false if not
	public boolean set_card_at(int index, Card c)
	{
		if (deckSize <= index) {
			return false;
		}

		cardDeck.set(index, c);
		return true;
	}
	// Returns the index of a card in the deck given the suit and value.
	// -1 is returned if the card is not in the deck
	public int contains(int val, int suit)
	{
		if (deckSize == 0) {
				return -1;
		}
		Card currCard;
		for (int i = 1; i <= deckSize; i++) {
			currCard = get_card_at(deckSize - i);
			if (currCard.isFlipped()) {
				continue;
			}
			if (currCard.isCard(val, suit)) {
				return deckSize - i;
			}

		
		}
		return -1;
	}

	// Checks if a deck is complete in the game of solitaire
	// A complete deck is a full suit of cards (13 cards in numerical order).
	// Returns true if the deck is complete and false if not
	public boolean is_complete()
	{
		if (cardDeck.size() != 13) {
			return false;
		}
		// Checks if values are in ascending order
		for (int i = 0; i < deckSize; i++) {
			if ((cardDeck.get(i)).getVal() != (i + 1)) {
				return false;
			}
		}
		return true;
	}

	// Deletes all cards in a deck
	public void erase_deck()
	{
		cardDeck.clear();
		deckSize = 0;
	}

	// Adds a given deck to the back of the current deck
	public void concatinate(Deck toAdd)
	{
		int size = toAdd.deck_size();
		for (int i = 0; i < size; i++) {
			cardDeck.add(toAdd.pop_deck());
			deckSize++;
		}

	}

	private Vector <Card> cardDeck;
	private int deckSize;
};