package GoldMiner;

import java.util.Random;

public class Buff extends Object{
	private int s_buff;
	public Buff() {
		image=GoldMinerGame.buff;
		width=image.getWidth()*3/2;
		height=image.getHeight()*3/2;
		x=new Random().nextInt(1000-width);
		y=new Random().nextInt(400-height)+200;
		int r=new Random().nextInt(2);
		score=0;
		if(r==1) {//获得大力buff,持续10秒
			speed=40;
			s_buff=1;
		}
		else {
			speed=20;
			score=new Random().nextInt(250)+40;
		}
	}
	public int getS_buff() {
		return s_buff;
	}
}
