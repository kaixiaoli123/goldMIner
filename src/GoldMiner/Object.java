package GoldMiner;

import java.awt.image.BufferedImage;

public abstract class Object {
	protected int x;
	protected int y;
	protected int height;
	protected int width;
	protected int state;
	protected int instatic=0;
	protected int inmove=1;
	protected int geted=2;
	protected int speed;
	protected int score;
	public BufferedImage image;
	protected void step(int angle) {
		x=(int) (x+speed*Math.cos(Math.toRadians(angle)));
		y=(int) (y-speed*Math.sin(Math.toRadians(angle)));
	}
	protected void buffstep(int angle) {
		x=(int) (x+40*Math.cos(Math.toRadians(angle)));
		y=(int) (y-40*Math.sin(Math.toRadians(angle)));
	}
	public static boolean boom(Object f1,Object f2){
		int f1x = f1.x + f1.width/2;
		int f1y = f1.y + f1.height/2;
		int f2x = f2.x + f2.width/2;
		int f2y = f2.y + f2.height/2;
		boolean H = Math.abs(f1x - f2x) < (f1.width + f2.width)/2;
		boolean V = Math.abs(f1y -f2y) < (f1.height + f2.height)/2;
		return H&V;
	}
}
