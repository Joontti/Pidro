**Pidro game in Java**

The project is a replica of the card game "Pidro". The game is completely written in Java and is playable in the terminal. You can play with bots, who chooses their cards at random (I will use machine learning to improve the bots in the near future).

The game of Pidro is played by four players, 2 pairs. The goal of the game is to earn 62 or more points and the first team who succeeds wins. 
One dealer is chosen at random and that person will be the dealer for the current round, next round it is the player next to the dealer and it continues. 
Each player gets 9 cards and each will in order bet how many points they think their team will get, minimum bet is 6 and max is 14. You cannot bet the same as someone else. 
The player who wins chooses the suit for the round. Each player removes all cards which isn't a part of the suit (except for the same color 5 (pidro)) and asks the dealer for the amount of cards that will make your hand contain 6 cards.
The player who won the betting starts by placing a card. 

The cards rank is in this order (from highest to lowest): Ace, King, Queen, Jack, 10, 9, 8, 7, 6, 5(same suit), 5(the other color suit), 4, 3, 2. The following cards are worth 1 point: Ace, Jack, 10, 2. Both pidros (5's) are worth 5 points, so a team can max get 14 points in a round. 

There are resources online about rules and other strategies.
