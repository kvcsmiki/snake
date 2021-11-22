package com.example.snake;

public class Square {
    int x;
    int y;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Square) obj;
        return this.x == that.x &&
                this.y == that.y;
    }
    @Override
    public String toString() {
        return "Position[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }
}
