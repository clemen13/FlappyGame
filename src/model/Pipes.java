package model;

import java.awt.Image;

public class Pipes {
    public int x, y;
    public int width, height;
    public Image image;
    public boolean passed = false;

    public Pipes(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void move(int speed) {
        x += speed;
    }
}