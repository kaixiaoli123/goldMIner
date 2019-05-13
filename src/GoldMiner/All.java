package GoldMiner;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class All extends JFrame{
	public All() {
		// TODO Auto-generated constructor stub
		GoldMinerGame game=new GoldMinerGame();
		this.setTitle("»Æ½ð¿ó¹¤");
		this.setBounds(100, 100,GoldMinerGame.panel_width,GoldMinerGame.panel_height );
		this.add(game);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
					if(GoldMinerGame.gamestate==GoldMinerGame.running) {
						int a=e.getKeyCode();
						if(a==KeyEvent.VK_DOWN&&GoldMinerGame.hookstate==GoldMinerGame.sway) {
							GoldMinerGame.hookstate=GoldMinerGame.stretch;
						}
					}
			}
		});
	}
	public static void main(String[] args) {
		All all=new All();
	}
}
