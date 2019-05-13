package GoldMiner;

import java.util.Random;

public class Stone extends Object{
	public Stone(int size) {
		image=GoldMinerGame.stone;
		width=(int)image.getWidth()*size;
		height=(int)image.getHeight()*size;
		x=new Random().nextInt(1000-width);
		y=new Random().nextInt(400-height)+200;
		speed=5+(3-size)*10;
		score=10+40*(size-1);
	}
}
