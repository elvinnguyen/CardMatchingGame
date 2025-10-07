package jfx;
import java.io.*;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import control.GameController;
import javafx.scene.SnapshotParameters;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import model.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMemoryGame extends Application {
    private static final int ROWS = 4, COLS = 4;
    private final GameModel model = new GameModel(ROWS, COLS, System.nanoTime());
    private final GameController controller = new GameController(model);
    private final Image back = makeCardImage(Color.DARKSLATEBLUE, "🂠", 110, 140);
    private final Image[] faces = new Image[ROWS * COLS / 2];
    private Label status = new Label();
    private Label timerLabel = new Label();
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private boolean gameOver = false;



    @Override
    public void start(Stage stage) {
        String[] glyphs = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int i = 0; i < faces.length; i++)
            faces[i] = makeCardImage(Color.WHITESMOKE, glyphs[i % glyphs.length], 110, 140);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(12));
        grid.setAlignment(Pos.CENTER);
        ImageView[][] views = new ImageView[ROWS][COLS];
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                ImageView iv = new ImageView(back);
                iv.setFitWidth(110);
                iv.setFitHeight(140);
                iv.setEffect(new DropShadow(8, Color.gray(0, 0.3)));
                final int rr = r, cc = c;
                iv.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onClick(iv, rr, cc, views));
                views[r][c] = iv;
                grid.add(iv, c, r);
            }
        status.setText("Pairs: 0 | Flips: 0");
        status.setFont(Font.font(18));
        timerLabel = new Label("Time: 60");
        VBox root = new VBox(10, timerLabel, status, grid);
        root.setPadding(new Insets(10));
        controller.startTimer(timerLabel, 60);
        controller.setOnTimeUp(() -> status.setText("Game Over!"));
        root.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(root, 550, 700);
        stage.setScene(scene);
        stage.setTitle("Memory - JavaFX");
        stage.show();
    }

    private void onClick(ImageView iv, int r, int c, ImageView[][] views) {
        //  Prevent clicks after game ends
        if (gameOver) return;

        controller.onCardClicked(r, c, () -> {
            // schedule after ~650ms
            PauseTransition pt = new PauseTransition(Duration.millis(650));
            pt.setOnFinished(e -> {
                // find two faceUp but not matched and resolve
                Card first = null, second = null;
                for (int i = 0; i < ROWS; i++)
                    for (int j = 0; j < COLS; j++) {
                        Card card = model.getBoard().getGrid(i, j);
                        if (card.isFaceUp() && !card.isMatched()) {
                            if (first == null) first = card;
                            else {
                                second = card;
                                break;
                            }
                        }
                    }
                if (first != null && second != null)
                    controller.resolveMismatch(first, second);
                refresh(views);
            });
            pt.play();
        });
        refresh(views);

        //  Handle win condition safely once game ends
        if (controller.isWin() && !gameOver) {
            gameOver = true; // prevent multiple triggers on click after game ends
            controller.stopTimer(); // stops the timer

            int timeLeft = controller.getTimeLeft(); // take the time left for the scoreboard
            status.setText("You win! Pairs: " + model.getScore() + " | Flips: " + model.getFlips() + " | Time left: " + timeLeft + "s");

            TextInputDialog dialog = new TextInputDialog("Player");
            dialog.setHeaderText("You won!");
            dialog.setContentText("Enter your name for the leaderboard:");
            String name = dialog.showAndWait().orElse("Player");

            updateLeaderboard(name, timeLeft);
        }
    }

    private void refresh(ImageView[][] views) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                Card card = model.getBoard().getGrid(r, c);
                views[r][c].setImage(card.isFaceUp() || card.isMatched()
                        ? faces[card.getId()] : back);
            }
        status.setText("Pairs: " + model.getScore() + " | Flips: " + model.getFlips());
    }

    // Render a card image at runtime (no assets needed)
    private Image makeCardImage(Color bg, String glyph, int w, int h) {
        WritableImage img = new WritableImage(w, h);

        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(w, h);
        var gc = canvas.getGraphicsContext2D();

        gc.setFill(bg);
        gc.fillRoundRect(0, 0, w, h, 20, 20);

        gc.setFill(Color.BLACK);
        Font font = Font.font("SansSerif", 48);
        gc.setFont(font);

        Text t = new Text(glyph);
        t.setFont(font);
        t.setBoundsType(TextBoundsType.VISUAL); // tighter bounds
        double tw = t.getLayoutBounds().getWidth();
        double th = t.getLayoutBounds().getHeight();


        /*
        Doesn't work because com.sun.* classes aren’t public and (a) IntelliJ will flag them and
        (b) methods may not exist in your version — hence the “cannot find symbol computeStringWidth” error.

        var fm = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader();
        double tw = fm.computeStringWidth(glyph, gc.getFont());
        double th = fm.getFontMetrics(gc.getFont()).getLineHeight();
        */

        gc.fillText(glyph, (w - tw) / 2, (h + th / 3) / 2);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        canvas.snapshot(sp, img);
        return img;
    }

    public static void main(String[] args) {
        launch(args);
    }


    private void updateLeaderboard(String playerName, int timeLeft) {
        List<String> entries = new ArrayList<>();

        // Load existing entries
        try (BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                entries.add(line.trim());
            }
        } catch (IOException e) {
            // No file yet
        }

        // Add new entry
        entries.add(playerName + "," + timeLeft);

        // Sorted by time left, highest to lowest
        entries.sort((a, b) -> {
            int timeA = Integer.parseInt(a.split(",")[1].trim());
            int timeB = Integer.parseInt(b.split(",")[1].trim());
            return Integer.compare(timeB, timeA);
        });

        // Keep top 10 entries
        if (entries.size() > 10) {
            entries = new ArrayList<>(entries.subList(0, 10));
        }

        // Save updated list
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (String entry : entries) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }

        // Build leaderboard display
        StringBuilder sb = new StringBuilder(" Leaderboard (by time left)\n\n");
        int rank = 1;
        for (String entry : entries) {
            String[] parts = entry.split(",", 2);
            String nm = parts[0].trim();
            String t = parts.length > 1 ? parts[1].trim() : "0";
            sb.append(rank++).append(". ").append(nm).append(" — ").append(t).append("s\n");
        }

        // Show leaderboard popup
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Leaderboard");
        alert.setHeaderText("Top Players");
        alert.setContentText(sb.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

}
