
package com.mycompany.testproject1;


public class AppleGenerator implements Runnable{
    public Position applePosition;
    private LevelCanvas parentCanvas;
    
    AppleGenerator(LevelCanvas canvas)
    {
        parentCanvas = canvas;
        Thread appleGenThread = new Thread(this);
        appleGenThread.start();
    }
    
    @Override
    public void run()
    {   
        Position foodPos = new Position();
        boolean isPlaceGood=false;
        do
        {
            foodPos.x=(int)(Math.random()*parentCanvas.width);
            foodPos.y=(int)(Math.random()*parentCanvas.height);

            if(parentCanvas.tiles[foodPos.x][foodPos.y]==TileType.defaultTile&&!foodPos.isInList(parentCanvas.playerSnake.nodes)&&!foodPos.isInList(parentCanvas.aiSnake.nodes))
                {
               isPlaceGood = true;
            }

            //isPlaceGood = true;
        }while(!isPlaceGood);
        applePosition = foodPos;
           
    }
}
