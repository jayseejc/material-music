package com.jayseeofficial.materialmusic.event;

/**
 * Created by jon on 01/06/15.
 */
public class SkipEvent {

    public enum Direction{
        NEXT,PREVIOUS
    }

    private Direction direction;
    public SkipEvent(Direction direction){
        this.direction=direction;
    }

    public Direction getDirection(){
        return direction;
    }

}
