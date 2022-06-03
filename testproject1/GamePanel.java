

package com.mycompany.testproject1;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Font;
import java.io.File;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class GamePanel extends JPanel implements Runnable
{
    LevelCanvas lvlCanvas;
    Thread gameThread;
    JFrame mainFrame;
    JLabel label;
    JLabel l;
    
    boolean isGameRunning=true;
    JButton playButton;
    
    Action upAction;
    Action downAction;
    Action leftAction;
    Action rightAction;
    GamePanel()
    {  
        File highscoreFile = new File("highscores.txt");
        
        mainFrame=new JFrame();
        label = new JLabel();
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1920, 1080);

        
        playButton=new JButton("PLAY");//creating instance of JButton  
        

        playButton.setBounds(870,500,180,80);//x axis, y axis, width, height 

        lvlCanvas = new LevelCanvas();
        lvlCanvas.setSize(1920,1080);
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                lvlCanvas.restartGame();
                isGameRunning = true;
                l.setVisible(false);
                playButton.setVisible(false);
                
            }   
        });
        
         
        
        


        //mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //mainFrame.setUndecorated(true);

        mainFrame.setLayout(null);//using no layout managers 
        
        upAction = new UpAction();
        label.getInputMap().put(KeyStroke.getKeyStroke("W"), "upAction");
        label.getActionMap().put("upAction", upAction);
        downAction = new DownAction();
        label.getInputMap().put(KeyStroke.getKeyStroke("S"), "downAction");
        label.getActionMap().put("downAction", downAction);
        leftAction = new LeftAction();
        label.getInputMap().put(KeyStroke.getKeyStroke("A"), "leftAction");
        label.getActionMap().put("leftAction", leftAction);
        rightAction = new RightAction();
        label.getInputMap().put(KeyStroke.getKeyStroke("D"), "rightAction");
        label.getActionMap().put("rightAction", rightAction);
        
        l = new JLabel("Your score:");
        l.setFont(new Font("Serif", Font.BOLD, 45));
        l.setLocation(820,300);
        l.setSize(280, 45);
        l.setVisible(false);
        
        
        mainFrame.add(l);
        mainFrame.add(playButton);//adding button in JFrame    
        mainFrame.add(lvlCanvas);
        
        
        
        playButton.setVisible(false);
        
        mainFrame.add(label);
        mainFrame.setVisible(true);//making the frame visible  
        
        
        gameThread = new Thread(this);
        gameThread.start();
        
    }  //constructor
    
    @Override
    public void run()
    {
        this.grabFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 30.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        Graphics tmpG = lvlCanvas.getGraphics();
        Thread snakeThread;
        
        while(true)
        {
            if(isGameRunning)
            {
                long now = System.nanoTime();
                delta+=(now - lastTime)/ns;
                lastTime = now;
                if(delta>=3)
                {

                    isGameRunning=lvlCanvas.move();
                    if(isGameRunning)
                    {
                        snakeThread = new Thread(lvlCanvas.aiSnake);
                        snakeThread.start();
                        isGameRunning=lvlCanvas.checkCollisions();
                    }
                        
                    if(isGameRunning)
                        lvlCanvas.paint(tmpG);


                    delta -= 3;
                }  
            }
            else
            {
                playButton.setVisible(true);
                l.setText("Your score:"+lvlCanvas.playerSnake.length);
                l.setVisible(true);
                lastTime = System.nanoTime();
            }        
        }
    }
    
    public class UpAction extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            lvlCanvas.playerSnake.changeDirection(Direction.up);
        }
    }
    public class DownAction extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            lvlCanvas.playerSnake.changeDirection(Direction.down);
        }
    }
    public class LeftAction extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            lvlCanvas.playerSnake.changeDirection(Direction.left);
        }
    }
    public class RightAction extends AbstractAction
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            lvlCanvas.playerSnake.changeDirection(Direction.right);
        }
    }
    
}


