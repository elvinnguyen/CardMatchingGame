package model;

public class Card {
    private int id;
    private boolean faceUp;
    private boolean matched;

    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public boolean isMatched() {
        return matched;
    }

    public void flipUp() {
        if (!matched) {
            faceUp = true;
        }
    }

    public void flipDown() {
        if (!matched) {
            faceUp = false;
        }
    }

    public void markMatched() {
        matched = true;
        faceUp = true;
    }


}
