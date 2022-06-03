
package com.mycompany.testproject1;
import java.awt.*;
import java.util.List;


class Position
{
    public int x;
    public int y;
    
    public Position()
    {
        x = 0;
        y = 0;
    }
    
    public Position(int px,int py)
    {
        x = px;
        y = py;
    }
    
    public boolean equals(Position other)
    {
        return this.x==other.x&&this.y==other.y;
    }
    
    boolean isInList(List<Position>list)
    {
        boolean answer = false;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).x==this.x&&list.get(i).y==this.y)
                answer = true;
        }
        return answer;
    }
};

public class LevelCanvas extends Canvas{
    public TileType tiles[][];
    int width;
    int height;
    int tileWidth;
    
    public AppleGenerator appleGenerator;
    
    public Snake playerSnake;
    
    public Snake aiSnake;
    
    public LevelCanvas()
    {
        playerSnake = new Snake(new Position(3,3),Direction.right,this);
        
        aiSnake = new Snake(new Position(80,3),Direction.left,this);
        
        width = 96;
        height = 54;
        tileWidth=20;

        generateMap();     
        appleGenerator = new AppleGenerator(this);
        
        
    }
    
    private void generateMap()
    {
        tiles = new TileType[width][]; 
        for(int i=0;i<width;i++)
        {
            tiles[i]=new TileType[height];
            for(int j=0;j<height;j++)
            {
                tiles[i][j] = TileType.defaultTile;
            }
        }
        
        int obstaclesNum=(int)(Math.random()*15)+5;
        int obstacleX,obstacleY,obstacleWidth,obstacleHeight;
        for(int i=0;i<obstaclesNum;i++)
        {
            obstacleX=(int)(Math.random()*width);
            obstacleY=(int)(Math.random()*height);
            obstacleWidth=(int)(Math.random()*10)+1;
            obstacleHeight=(int)(Math.random()*10)+1;
            for(int j=0;j<obstacleWidth;j++)
            {
                if(obstacleX+j<width)
                {
                    for(int k=0;k<obstacleHeight;k++)
                    {
                        if(obstacleY+k<height)
                        {
                            tiles[obstacleX+j][obstacleY+k]=TileType.obstacleTile;
                        }
                    }
                }
            }
        }
    }
    
    public void restartGame()
    {
        playerSnake.restart(new Position(3,3),Direction.right);
        
        aiSnake.restart(new Position(80,3),Direction.left);

        generateMap();     
        Thread appleGenThread = new Thread(appleGenerator);
        appleGenThread.start();
    }
    
    @Override
    public void paint(Graphics g)
    {
        Color tmpColor;
        for(int i=0;i<width;i++)  //draw map
        {
           for(int j=0;j<height;j++)
           {
               
               switch(tiles[i][j])
               {
                   case defaultTile:
                       tmpColor = Color.decode("#bfbfbf");
                       break;
                       
                   case obstacleTile:
                       tmpColor = Color.decode("#242424");
                       break;
                       
                   default:
                       tmpColor = Color.decode("#23127a");
                       break;
               }           
               
               g.setColor(tmpColor);
               g.fillRect(tileWidth*i,tileWidth*j,tileWidth,tileWidth);
               
           }
        }
        
        g.setColor(Color.decode("#297a12"));  //player snake
        for(int i=0;i<playerSnake.nodes.size();i++)
        {
           int tmpX = playerSnake.nodes.get(i).x; 
           int tmpY = playerSnake.nodes.get(i).y; 
           g.fillRect(tileWidth*tmpX,tileWidth*tmpY,tileWidth,tileWidth);
        }
        
        g.setColor(Color.decode("#203Cd5"));   //ai snake
        for(int i=0;i<aiSnake.nodes.size();i++)
        {
           int tmpX = aiSnake.nodes.get(i).x; 
           int tmpY = aiSnake.nodes.get(i).y; 
           g.fillRect(tileWidth*tmpX,tileWidth*tmpY,tileWidth,tileWidth);
        }
        
        
        g.setColor(Color.decode("#7a1c12"));  //food
        g.fillRect(tileWidth*appleGenerator.applePosition.x,tileWidth*appleGenerator.applePosition.y,tileWidth,tileWidth);
        
    }
    
    public boolean move()
    {
        Position playerHeadPosition = playerSnake.nodes.get(0);
        switch (playerSnake.direction)
        {
            case right:
                if(playerHeadPosition.x==width-1)
                {
                    return false;
                }
                else
                {
                    playerSnake.nodes.add(0,new Position(playerHeadPosition.x+1,playerHeadPosition.y));
                    if(playerSnake.length<playerSnake.nodes.size())
                        playerSnake.nodes.remove(playerSnake.nodes.size()-1);
                }
            break;
                   
            case left:
                if(playerHeadPosition.x==0)
                {
                    return false;
                }
                else
                {
                    playerSnake.nodes.add(0,new Position(playerHeadPosition.x-1,playerHeadPosition.y));
                    if(playerSnake.length<playerSnake.nodes.size())
                        playerSnake.nodes.remove(playerSnake.nodes.size()-1);
                }
            break;
            
            case up:
                if(playerHeadPosition.y==0)
                {
                    return false;
                }
                else
                {
                    playerSnake.nodes.add(0,new Position(playerHeadPosition.x,playerHeadPosition.y-1));
                    if(playerSnake.length<playerSnake.nodes.size())
                        playerSnake.nodes.remove(playerSnake.nodes.size()-1);
                }
            break;
            
            case down:
                if(playerHeadPosition.y==height-1)
                {
                    return false;
                }
                else
                {
                    playerSnake.nodes.add(0,new Position(playerHeadPosition.x,playerHeadPosition.y+1));
                    if(playerSnake.length<playerSnake.nodes.size())
                        playerSnake.nodes.remove(playerSnake.nodes.size()-1);
                }
            break;
            
        }
        
        Position aiHeadPosition = aiSnake.nodes.get(0);
        switch (aiSnake.direction)
        {
            case right:
                if(aiHeadPosition.x==width-1)
                {
                    //game over
                }
                else
                {
                    aiSnake.nodes.add(0,new Position(aiHeadPosition.x+1,aiHeadPosition.y));
                    if(aiSnake.length<aiSnake.nodes.size())
                        aiSnake.nodes.remove(aiSnake.nodes.size()-1);
                }
            break;
                   
            case left:
                if(aiHeadPosition.x==0)
                {
                    //game over
                }
                else
                {
                    aiSnake.nodes.add(0,new Position(aiHeadPosition.x-1,aiHeadPosition.y));
                    if(aiSnake.length<aiSnake.nodes.size())
                        aiSnake.nodes.remove(aiSnake.nodes.size()-1);
                }
            break;
            
            case up:
                if(aiHeadPosition.y==0)
                {
                    //game over
                }
                else
                {
                    aiSnake.nodes.add(0,new Position(aiHeadPosition.x,aiHeadPosition.y-1));
                    if(aiSnake.length<aiSnake.nodes.size())
                        aiSnake.nodes.remove(aiSnake.nodes.size()-1);
                }
            break;
            
            case down:
                if(aiHeadPosition.y==height-1)
                {
                    //game over
                }
                else
                {
                    aiSnake.nodes.add(0,new Position(aiHeadPosition.x,aiHeadPosition.y+1));
                    if(aiSnake.length<aiSnake.nodes.size())
                        aiSnake.nodes.remove(aiSnake.nodes.size()-1);
                }
            break;
                
        }
        return true;
    }
    
    public boolean checkCollisions()
    {
        Position playerPos = playerSnake.nodes.get(0);
        
        for(int i=1;i<playerSnake.nodes.size();i++)
        {
            if(playerPos==playerSnake.nodes.get(i))
            {
                return false;
            }
        }
        
        if(playerPos.isInList(aiSnake.nodes))
        {
            return false;
        }
        
        if(tiles[playerPos.x][playerPos.y]==TileType.obstacleTile)
        {
            return false;
        }
        else if (appleGenerator.applePosition.equals(playerPos))
        {
            playerSnake.length++;
            //placeFood();
            Thread appleGenThread = new Thread(appleGenerator);
            appleGenThread.start();
        }
        
        Position aiPos = aiSnake.nodes.get(0);
        
        for(int i=1;i<aiSnake.nodes.size();i++)
        {
            if(aiPos==aiSnake.nodes.get(i))
            {
                //GAME OVER
            }
        }
        
        if(aiPos.isInList(playerSnake.nodes))
        {
            //GAME OVER
        }
        
        if(tiles[aiPos.x][aiPos.y]==TileType.obstacleTile)
        {
            //GAME OVER
        }
        else if (appleGenerator.applePosition.equals(aiPos))
        {
            aiSnake.length++;
            //placeFood();
            Thread appleGenThread = new Thread(appleGenerator);
            appleGenThread.start();
        }
        return true;
    }
    

       
}


