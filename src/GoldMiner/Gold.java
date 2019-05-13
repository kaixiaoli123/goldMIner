package GoldMiner;

import java.util.Random;

public class Gold extends Object{
	public Gold(int size) {
		image=GoldMinerGame.gold;
		width=(int)image.getWidth()*size/3*2;
		height=(int)image.getHeight()*size/3*2;
		x=new Random().nextInt(1000-width);
		y=new Random().nextInt(400-height)+200;
		speed=5+(3-size)*10;
		score=50+100*(size-1);
	}
}
