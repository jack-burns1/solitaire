# ASCII-Solitaire
# Jack Burns
# May 2020


ASCII Solitaire is a Java program I coded for fun that updates the state of the game by printing it to the command line in ascii characters.

Features: The game has a welcome page, as well as a directions page. A new game can be started at any time, creating an entirely shuffled
deck. The user can exit the game by typing in 'q'

Architecture: There are 3 main classes in this game. One is for each individual card, and contains all of the possible data needed for a
card in a game of solitaire. The deck class contains a vector of cards, and contains many deck operations, while keeping track of the cards
in the deck. The game class is the driver for this program, and advances the game. 

The game relies heavily on the use of vectors, as each stack of cards is its own deck. Additionally, I had to delve into ascii art a bit
for the display. The game also uses ansi color codes and the clear screan functionality.

Significant difficulties: Since the game is entirely displayed on the command line, it was difficul to present a mostly vetical game line
by line. I created a seperate file for printing, and printed each deck in the game line by line.

Improvments: I created this game as a quarantine activity to learn Java, and can find some areas of improvment. Most notably, the game can 
be split up into more files, as much of the actions happen in the long "Game" file.

