package model;

import java.util.*;

public class Board {
    private final int rows;
    private final int cols;
    private final Card[][] grid;

    public Board(int rows, int cols, long seed) {
        if ((rows * cols) % 2 != 0) {
            throw new IllegalArgumentException("Even number of cards required");
        }

        this.rows = rows;
        this.cols = cols;
        this.grid = new Card[rows][cols];

        // Create pair ids: 0..(nPairs-1) twice
        int n = rows * cols;
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < n / 2; i++) {
            ids.add(i);
            ids.add(i);
        }
        Collections.shuffle(ids, new Random(seed));
        Iterator<Integer> it = ids.iterator();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Card(it.next());
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Card getGrid(int r, int c) {
        return grid[r][c];
    }

    public boolean allMatched() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!grid[r][c].isMatched()) return false;
        return true;
    }
}