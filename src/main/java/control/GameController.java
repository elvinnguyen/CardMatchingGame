package control;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import model.*;

public class GameController {
    private final GameModel model;
    private Card firstPick = null;
    private boolean inputLocked = false;
    private Timeline timeline;
    private int timeSeconds = 60;
    private Runnable onTimeUp;

    public GameController(GameModel model) {
        this.model = model;
    }

    public boolean isInputLocked() {
        return inputLocked;
    }

    public GameModel getModel() {
        return model;
    }

    public void startTimer(Label timerLabel, int startSeconds) {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = startSeconds;
        timerLabel.setText("Time: " + timeSeconds);

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeSeconds--;
                    timerLabel.setText("Time: " + timeSeconds);
                    if (timeSeconds <= 0) {
                        timeline.stop();
                        timeUp();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setOnTimeUp(Runnable action) {
        this.onTimeUp = action;
    }

    private void timeUp() {
        inputLocked = true;
        if (onTimeUp != null) {
            onTimeUp.run();
        }
    }

    /**
     * Called by the UI when the user clicks a card.
     */
    public void onCardClicked(int r, int c, Runnable onMismatchDelay) {
        if (inputLocked) return;
        Card card = model.getBoard().getGrid(r, c);
        if (card.isMatched() || card.isFaceUp()) return;
        card.flipUp();
        model.incrementFlips();
        if (firstPick == null) {
            firstPick = card;
        } else {
            // Compare
            if (firstPick.getId() == card.getId()) {
                firstPick.markMatched();
                card.markMatched();
                model.incrementScore();
                firstPick = null;
            } else {
                // Temporarily lock input; UI should call onMismatchDelay.run() after ~700 ms
                inputLocked = true;
                Card a = firstPick, b = card;
                firstPick = null;
                onMismatchDelay.run(); // UI schedules the actual delay
                // After delay, UI must call controller.resolveMismatch(a,b)
            }
        }
    }

    /**
     * UI calls this after the delay expires.
     */
    public void resolveMismatch(Card a, Card b) {
        a.flipDown();
        b.flipDown();
        inputLocked = false;
    }

    public boolean isWin() {
        return model.getBoard().allMatched();
    }
}