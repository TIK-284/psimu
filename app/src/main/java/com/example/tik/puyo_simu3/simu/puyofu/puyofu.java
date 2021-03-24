package com.example.tik.puyo_simu3.simu.puyofu;

import java.io.Serializable;

/**
 * Created by TIK on 2016/02/10.
 */
public class puyofu implements Serializable{
    public int dropLine=1;
    public Direction direction=Direction.UP;
    public boolean validate=false;
    public enum Direction{
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
}
