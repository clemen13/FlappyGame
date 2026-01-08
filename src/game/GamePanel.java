package game;

import model.*;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // --- Jugador y obstáculos ---
    private Bird bird;
    private ArrayList<Pipes> pipes = new ArrayList<>();
    private Random random = new Random();

    // --- Física y juego ---
    private int gravity = 1;
    private int gameSpeed = -4;
    private boolean gameOver = false;
    private double score = 0;

    // --- High Score ---
    private int highScore = 0;
    private final String HIGH_SCORE_FILE = "highscore.txt";

    // --- Timers ---
    private Timer gameLoop;
    private Timer obstacleTimer;

    // --- Imágenes ---
    private Image background;
    private Image playerImg;
    private Image topPipe;
    private Image bottomPipe;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        loadAssets();
        initGame();
        loadHighScore();
        startTimers();
    }

    private void loadAssets() {
        background = AssetLoader.load("/img/flappybirdbg.png");
        playerImg  = AssetLoader.load("/img/flappybird.png");
        topPipe    = AssetLoader.load("/img/toppipe.png");
        bottomPipe = AssetLoader.load("/img/bottompipe.png");
    }

    private void initGame() {
        bird = new Bird(
                Constants.WIDTH / 8,
                Constants.HEIGHT / 2,
                Constants.BIRD_WIDTH,
                Constants.BIRD_HEIGHT,
                playerImg
        );
    }

    private void startTimers() {
        obstacleTimer = new Timer(1500, e -> spawnPipes());
        obstacleTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    private void spawnPipes() {
        int gap = Constants.HEIGHT / 7;
        int randomY = -random.nextInt(Constants.PIPES_HEIGHT / 2);

        Pipes top = new Pipes(Constants.WIDTH, randomY,
                Constants.PIPES_WIDTH, Constants.PIPES_HEIGHT, topPipe);

        Pipes bottom = new Pipes(Constants.WIDTH,
                randomY + Constants.PIPES_HEIGHT + gap,
                Constants.PIPES_WIDTH, Constants.PIPES_HEIGHT, bottomPipe);

        pipes.add(top);
        pipes.add(bottom);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {

        //Background
        g.drawImage(background, 0, 0, Constants.WIDTH, Constants.HEIGHT, null);

        //Bird
        g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        //Pipes
        for (Pipes o : pipes) {
            g.drawImage(o.image, o.x, o.y, o.width, o.height, null);
        }

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Score: " + (int) score, 10, 40);

        // High Score
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("High Score: " + highScore, 10, 70);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 70, Constants.HEIGHT / 2);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            g.drawString("  Press SPACE to restart", 60, Constants.HEIGHT / 2 + 40);
        }
    }

    private void updateGame() {
        if (gameOver) return;

        bird.applyGravity(gravity);

        for (Pipes o : pipes) {
            o.move(gameSpeed);

            if (!o.passed && bird.x > o.x + o.width) {
                o.passed = true;
                score += 0.5;
            }

            if (checkCollision(bird, o)) {
                gameOver = true;
            }
        }

        if (bird.y > Constants.HEIGHT) {
            gameOver = true;
        }

        if (gameOver && (int) score > highScore) {
            highScore = (int) score;
            saveHighScore();
        }
    }

    private boolean checkCollision(Bird p, Pipes o) {
        return p.x < o.x + o.width &&
                p.x + p.width > o.x &&
                p.y < o.y + o.height &&
                p.y + p.height > o.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                resetGame();
            } else {
                bird.jump();
            }
        }
    }

    private void resetGame() {
        pipes.clear();
        bird.y = Constants.HEIGHT / 2;
        bird.velocityY = 0;
        score = 0;
        gameSpeed = -4;
        gameOver = false;
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}


    // --- High Score ---
    private void loadHighScore() {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            highScore = 0;
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            highScore = line != null ? Integer.parseInt(line) : 0;
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    private void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
