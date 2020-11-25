// wordz

package solitaire;

import java.util.concurrent.TimeUnit;
import java.util.*;

public class Printer {

    private static String MIDDLE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

    public Printer()
    {
    }

    public void intro()
    {
        System.out.print(MIDDLE + "                                                                                  \u001b[42;1mWelcome to Solitaire!\n\n");

        try {
            Thread.sleep(2000);
        } 
        catch(InterruptedException e) {
            System.out.print("well fuck me\n");
        }
    }

    public void directions_with_back()
    {
        directions();
        System.out.print("                                                                             \u001b[43mPress 'b' to go back to the game.\u001b[42;1m\n\n");

        Scanner in = new Scanner(System.in);
        String input = " ";
        input = in.nextLine();

        if (input.equalsIgnoreCase("b")) {
            return;
        }

        directions_with_back();
    }

    public void directions()
    {
        System.out.print("\n******************************************************************************** Solitare Directions ********************************************************************************\n\n");
        format();
        System.out.print("                                                                       Press 'd' to draw another card from the deck.\n");
        System.out.print("                                                                              \u001b[43mPress 'n' to start a new game.\u001b[42;1m\n");
        System.out.print("                                                                          Press 'f' to flip any unflipped cards.\n");
        System.out.print("                                                                             Press 'dir' for directions page.\n");
        System.out.print("                                                                                Press 'q' to quit the game.\n\n");
        System.out.print("                                                                         Directions can be uppercase or lowercase.\n\n\n\n");

    }

    public void format_error()
    {
        System.out.print("                                                                                     Incorrect Format:\n\n");

        format();
    }

    public void format()
    {
        System.out.print("                                           To move a card, input the card to move, followed by the card to stack it on in the following format:\n\n");
        System.out.print("                                                                               [Value][Suit] [Value][Suit]\n\n");
        System.out.print("                                                        Values can be  2  -  10,  J,  Q,  K,  or A.  Suits can be C, D, H, or S.\n");
        System.out.print("                                                           Values and Suits can be uppercase or lowercase - it does not matter.\n\n");
        System.out.print("                                              If you are starting a pile with an ace or a king, only put the value and suit of that card, or:\n\n");
        System.out.print("                                                                                      [Value] [Suit]\n\n");
        System.out.print("                                                           EX. \"10S JH\" Would stack the 10 of spades onto the Jack of hearts.\n\n");
        System.out.print("                                                                 EX. \"AS\" Would move the ace of spades to a new deck.\n\n");
        System.out.print("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");

    }

    public boolean new_game()
    {
        System.out.print(MIDDLE + "                                                                        Are you sure you want to start a new game? [y/n]:   ");

        Scanner in = new Scanner(System.in);
        String input = " ";
        input = in.nextLine();

        if (input.equalsIgnoreCase("y")) {
            return true;
        }

        if (input.equalsIgnoreCase("n")) {
            return false;
        }

        return new_game();
    }

    public boolean win()
    {
        System.out.print(MIDDLE + "                                                                                Congratulations! You Win!!\n");
        System.out.print(MIDDLE + "                                                                                     Play Again? [y/n]\n");
        return game_over();
    }

    public boolean game_over()
    {
        Scanner in = new Scanner(System.in);
        String input = " ";
        input = in.nextLine();

        if (input.equalsIgnoreCase("y")) {
            return true;
        }

        if (input.equalsIgnoreCase("n")) {
            return false;
        }

        return game_over();
    }
}



