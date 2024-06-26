package com.apollon.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class Snake extends JPanel implements ActionListener{
	int body;
	int x[] = new int[500];
	int y[] = new int[500];
	Timer timer = new Timer(100,this);

	private boolean right=true;
	private boolean left=false;
	private boolean up=false;
	private boolean down=false;
	private boolean gamestarted = true; 
	private int food_x;
	private int food_y;
	private int score = 0;
	private int life = 3; 


	public Snake() { 


		drawBody();
		timer.start();
		addKeyListener(new Handler()); 
		setFocusable(true); 
		setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLUE), "Snake")); 
		setBackground(Color.LIGHT_GRAY); 

	}

	/* initialize the snake body x and y coordinates to * 10 and 100 */

	private void drawBody() {

		if(gamestarted){
			body=4;
			for(int i =0; i < body;i++){
				x[i] = x[i]+10*i;
				y[i]=y[i]+100;
			}
			food();
		}
	}


	/* movement of snake body*/
	public void move(){
		if(life < 0){
			gamestarted =false;
			timer.stop();
		}
		if(gamestarted){
			for(int i =body ;i > 0;i--){
				x[i]=x[(i-1)]; y[i]=y[(i-1)];
			}
			if(right){
				x[0]+=10;
			}
			if(left){
				x[0]-=10; }
			if(up){
				y[0]-=10; }
			if(down ){
				y[0]+=10;
			}
		}
	}

	/* checks for collision of snake and food*/

	public void checkFoodCollision(){

		if(x[0] == food_x && y[0] == food_y){

			body++;
			score++;
			food();
		}
	}

	/* checks for collision of snake head and body*/

	public void checkBodyCollision(){
		for (int i = body; i > 0; i--)
		{
			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
				life--;
				body = 4;
				x[0]=150;
				y[0]=150;

			}
		}

	}

	/* checks for collision of snake and border*/
	public void checkBorderCollision(){

		if(life < 0){
			gamestarted = false;
		}
		if(x[0] >= 895){
			life--; x[0]=150;
			y[0]=150; body =4;
		}
		if( x[0] < 2){
			life--; x[0]=500;
			y[0]=150; body =4;
		}
		if(y[0] >= 495){
			life--;
			x[0]=500;
			y[0]=150;
			body =4;
		}
		if(y[0] < 5){
			life--;
			x[0]=500;
			y[0]=150; body =4;
		}
	} 


	/* continues to a new game */
	public void continueGame(){ 
		gamestarted = true;
		score = 0;
		life = 3;
		timer.start();
	}
	/* sets snake food to a random x and y co-ordinate*/

	public void food(){
		Random r = new Random();
		food_x = r.nextInt(80) * 10 + 50; 
		food_y = r.nextInt(40) * 10 + 50;
	}

	/* draw snake body and food*/

	public void paint(Graphics g){ 
		super.paint(g);
		if(gamestarted){
			for(int i =0; i < body;i++){

				if(i == 0){

					// draw head first g.setColor(Color.RED);
					g.fillRect(x[i], y[i], 10, 10);
				}
				else{
					g.setColor(Color.BLUE);
					g.drawRect(x[i], y[i], 10, 10); } } 
			g.setColor(Color.GREEN); 
			g.fillRect(food_x, food_y, 10, 10); 
			score(g); 

			Life(g);
		}
		if(!gamestarted ){
			gameOver(g);
		}
	}

	/* Displays game over message and asks for an input*/

	private void gameOver(Graphics g) { 
		g.setFont(new Font("ariel",Font.BOLD,30));
		g.setColor(Color.BLACK); g.drawString("Game Over",300, 200); 
		g.setFont(new Font("ariel",Font.BOLD,20)); 
		g.drawString("Score : " + score,320, 250);
		g.setFont(new Font("ariel",Font.BOLD,17)); 
		g.drawString("press 'c' to continue or 'q'  to quit " ,290, 300);
	}

	/* displays amount of life available*/

	public void Life(Graphics g){ 
		g.setFont(new Font("ariel",Font.BOLD,14));
		g.setColor(Color.BLACK); 
		g.drawString("Life : " + life ,800, 25); }
	/* updates the total score*/

	public void score(Graphics g){
		g.setFont(new Font("ariel",Font.BOLD,14)); 
		g.setColor(Color.BLACK); 
		g.drawString("Score : " + score ,10, 25);
	}

	public void actionPerformed(ActionEvent e) { 
		checkBodyCollision(); 
		checkFoodCollision(); 
		checkBorderCollision();
		move();
		repaint();
	}
	/* handles key input events for * the left, right, up and down * direction * */

	private class Handler extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int keycode = e.getKeyCode(); 
			if(keycode == KeyEvent.VK_UP && down == false ){
				right = false;
				left = false;
				up = true;
				down = false;
			}

			if(keycode == KeyEvent.VK_DOWN && up ==false){
				right = false;
				left = false;
				up = false;
				down = true;
			}

			if(keycode == KeyEvent.VK_RIGHT && left == false){
				right = true;
				left = false;
				up = false;
				down = false;
			}

			if(keycode == KeyEvent.VK_LEFT && right == false){
				right = false;
				left =true;
				up = false;
				down = false;
			}

			/* if game is over listen for input from the keyboard to either continue or exit */

			if(!gamestarted){
				if(keycode == KeyEvent.VK_C ){
					continueGame();
				}
				if(keycode == KeyEvent.VK_Q){
					System.exit(0);
				}
			}
		}
	}
}