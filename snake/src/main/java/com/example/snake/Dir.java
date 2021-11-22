package com.example.snake;

public enum Dir {
    up, down, left, right;

    public Dir opposite(){
        return switch(this){
            case up -> down;
            case down -> up;
            case left -> right;
            case right -> left;
        };
    }
}
