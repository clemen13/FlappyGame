package model;

import java.awt.Image;

public class Bird {
    public int x, y;
    public int width, height;
    public int velocityY = 0;
    public Image image;

    public Bird(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void applyGravity(int gravity) {
        velocityY += gravity;
        y += velocityY;
    }

    public void jump() {
        velocityY = -9;
    }
}