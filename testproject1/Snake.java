
package com.mycompany.testproject1;

import java.util.List;
import java.util.ArrayList;

public class Snake implements Runnable{
    public int length;
    public Direction direction;
    public List<Position> nodes;
    
    private LevelCanvas parentCanvas;
    
    Snake(LevelCanvas canvas)
    {
        parentCanvas = canvas;
        length =1;
        direction = Direction.right;
        nodes = new ArrayList<>();
        nodes.add(new Position(3,3));
    }
    
    Snake(Position pos,Direction dir,LevelCanvas canvas)
    {
        parentCanvas = canvas;
        length =1;
        direction = dir;
        nodes = new ArrayList<>();
        nodes.add(pos);
    }


    public void changeDirection(Direction newDirection)
    {
        if((direction==Direction.right&&newDirection!=Direction.left)
            ||(direction==Direction.left&&newDirection!=Direction.right)
            ||(direction==Direction.down&&newDirection!=Direction.up)
            ||(direction==Direction.up&&newDirection!=Direction.down))
        {
            direction = newDirection;
        }          
    }
    
    public boolean move()
    {
        Position playerHeadPosition = nodes.get(0);
        switch (direction)
        {
            case right:
                    nodes.add(0,new Position(playerHeadPosition.x+1,playerHeadPosition.y));
                    if(length<nodes.size())
                        nodes.remove(nodes.size()-1);
            break;
                   
            case left:
                    nodes.add(0,new Position(playerHeadPosition.x-1,playerHeadPosition.y));
                    if(length<nodes.size())
                        nodes.remove(nodes.size()-1);
            break;
            
            case up:
                    nodes.add(0,new Position(playerHeadPosition.x,playerHeadPosition.y-1));
                    if(length<nodes.size())
                        nodes.remove(nodes.size()-1);
            break;
            
            case down:
                    nodes.add(0,new Position(playerHeadPosition.x,playerHeadPosition.y+1));
                    if(length<nodes.size())
                        nodes.remove(nodes.size()-1);
            break;
            
        }
       
        return true;
    }
    
    @Override
    public void run()
    {
        direction = findPath(nodes.get(0), parentCanvas.appleGenerator.applePosition,parentCanvas);
    }
    private Direction findPath(Position startingPos,Position endingPos,LevelCanvas canvas)
    {
        Position upPos = new Position(startingPos.x,startingPos.y-1);
        Position downPos = new Position(startingPos.x,startingPos.y+1);
        Position leftPos = new Position(startingPos.x-1,startingPos.y);
        Position rightPos = new Position(startingPos.x+1,startingPos.y);
        //if on the edge
        if(direction==Direction.up&&startingPos.y==0)
        {
            if(startingPos.x>0)
                return Direction.left;
            else
                return Direction.right;
        }
        else if(direction==Direction.down&&startingPos.y==canvas.height-1)
        {
            if(startingPos.x<canvas.width-1)
                return Direction.right;
            else
                return Direction.left;
        }
        else if(direction==Direction.left&&startingPos.x==0)
        {
            if(startingPos.y>0)
                return Direction.down;
            else
                return Direction.up;
        }
        else if(direction==Direction.right&&startingPos.x==canvas.width-1)
        {
            if(startingPos.y<canvas.height-1)
                return Direction.up;
            else
                return Direction.down;
        }

        
        //if ai can get closer and is not next to obstacle
        if(startingPos.x>endingPos.x&&direction!=Direction.right && 
                canvas.tiles[leftPos.x][leftPos.y]!=TileType.obstacleTile&&!leftPos.isInList(nodes)&&!leftPos.isInList(canvas.playerSnake.nodes))          
        {
            return Direction.left;
        }
        else if(startingPos.x<endingPos.x &&direction!=Direction.left &&
                canvas.tiles[rightPos.x][rightPos.y]!=TileType.obstacleTile&&!rightPos.isInList(nodes)&&!rightPos.isInList(canvas.playerSnake.nodes))
        {
            return Direction.right;
        }
        else if(startingPos.y>endingPos.y &&direction!=Direction.down &&
                canvas.tiles[upPos.x][upPos.y]!=TileType.obstacleTile&&!upPos.isInList(nodes)&&!upPos.isInList(canvas.playerSnake.nodes))
        {
            return Direction.up;
        }
        else if(startingPos.y<endingPos.y&&direction!=Direction.up &&
                canvas.tiles[downPos.x][downPos.y]!=TileType.obstacleTile&&!downPos.isInList(nodes)&&!downPos.isInList(canvas.playerSnake.nodes))
        {
            return Direction.down;
        }
        
        //if going to crash
        if(direction==Direction.right&&(canvas.tiles[rightPos.x][rightPos.y]==TileType.obstacleTile||
                rightPos.isInList(nodes)||rightPos.isInList(canvas.playerSnake.nodes)))
        {
            if(canvas.tiles[downPos.x][downPos.y]!=TileType.obstacleTile&&startingPos.x<canvas.height-1&&
                    !downPos.isInList(nodes)&&!downPos.isInList(canvas.playerSnake.nodes))
                return Direction.down;
            else
                return Direction.up;
        }
        else if(direction==Direction.left&&(canvas.tiles[leftPos.x][leftPos.y]==TileType.obstacleTile||
                leftPos.isInList(nodes)||leftPos.isInList(canvas.playerSnake.nodes)))
        {
            if(canvas.tiles[upPos.x][upPos.y]!=TileType.obstacleTile&&startingPos.y>0&&
                    !upPos.isInList(nodes)&&!upPos.isInList(canvas.playerSnake.nodes))
                return Direction.up;
            else
                return Direction.down;
        }
        else if(direction==Direction.down&&(canvas.tiles[downPos.x][downPos.y]==TileType.obstacleTile||
                downPos.isInList(nodes)||downPos.isInList(canvas.playerSnake.nodes)))
        {
            if(canvas.tiles[rightPos.x][rightPos.y]!=TileType.obstacleTile&&startingPos.x<canvas.width-1&&
                    !rightPos.isInList(nodes)&&!rightPos.isInList(canvas.playerSnake.nodes))
                return Direction.right;
            else
                return Direction.left;
        }
        else if(direction==Direction.up&&(canvas.tiles[upPos.x][upPos.y]==TileType.obstacleTile||
                upPos.isInList(nodes)||upPos.isInList(canvas.playerSnake.nodes)))
        {
            if(canvas.tiles[leftPos.x][leftPos.y]!=TileType.obstacleTile&&startingPos.x>0&&
                    !leftPos.isInList(nodes)&&!leftPos.isInList(canvas.playerSnake.nodes))
                return Direction.left;
            else
                return Direction.right;
        }
        else
            return direction;
    }
    
    public void restart(Position startingPos,Direction startingDir)
    {
        nodes.clear();
        nodes.add(startingPos);
        length = 1;
        direction = startingDir;
    }
}