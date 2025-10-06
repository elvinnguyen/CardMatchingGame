package model;

public class GameModel {
    private final Board board;
    private int score = 0;
    private int flips = 0;

    public GameModel(int rows, int cols, long seed) {
        this.board = new Board(rows, cols, seed);
    }

    public Board getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public int getFlips() {
        return flips;
    }

    public void incrementScore() {
        score++;
    }

    public void incrementFlips() {
        flips++;
    }
}
