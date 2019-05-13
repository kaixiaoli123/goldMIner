package GoldMiner;

import java.util.Random;

public class Diamond extends Object{
	public Diamond() {
		image=GoldMinerGame.diamond;
		width=image.getWidth()*2;
		height=image.getHeight()*2;
		x=new Random().nextInt(1000-width);
		y=new Random().nextInt(400-height)+200;
		speed=40;
		score=600;
	}
}
