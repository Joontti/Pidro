package project.players.ai;

import project.demo.Card;

public interface AI
{
    Card decideCard();
    int decideBet();
    int decideSuit();
}
