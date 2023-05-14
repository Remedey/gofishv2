package io.github.p0lbang.gofish.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static java.util.Map.entry;

public class Player {
    final String name;
    @SuppressWarnings("CanBeFinal")
    Deck hand;
    Deck completed;

    public Player(String name) {
        this.name = name;
        this.hand = new Deck();
        this.completed = new Deck();
    }

    public void addCard(Card card) {
        this.hand.addCard(card);
    }

    public Boolean checkHand(String rank) {
        return this.hand.checkRank(rank);
    }

    public void addMultipleCards(ArrayList<Card> cards) {
        this.hand.addCardMultiple(cards);
    }

    public ArrayList<Card> giveCards(String rank) {
        return this.hand.stealCard(rank);
    }

    public Map<String, String> ask(ArrayList<String> playerNames) {
        Random rand = new Random();

        Card selectedCard = hand.getRandomCard();
        String selectedPlayer = playerNames.get(rand.nextInt(playerNames.size()));

        return Map.ofEntries(entry("asker", this.name),
                entry("rank", selectedCard.getRank()),
                entry("target", selectedPlayer));
    }

    public void checkCompleted() {
        this.hand.getRanksHeld().forEach((key, value) -> {
            if (value != 4) {
                return;
            }

            this.completed.addCardMultiple(this.hand.stealCard(key));
        });
    }

    public String getName() {
        return this.name;
    }

    public void displayHand() {
        this.hand.display();
    }

    public void displayCompleted() {
        this.completed.display();
    }

    public void displayAll() {
        System.out.println(this.name);
        System.out.println("Hand:");
        this.displayHand();
        System.out.println("Completed:");
        this.displayCompleted();
    }

}
