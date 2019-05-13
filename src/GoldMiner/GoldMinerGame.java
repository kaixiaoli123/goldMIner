package GoldMiner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class GoldMinerGame extends JPanel{
	public final static int panel_width=1000;//窗口的宽
	public final static int panel_height=700;//窗口的高
	
	
	public static BufferedImage start0;
	public static BufferedImage start1;
	public static BufferedImage background;
	public static BufferedImage gameover;
	public static BufferedImage gold;
	public static BufferedImage diamond;
	public static BufferedImage stone;
	public static BufferedImage buff;
	
	
	public static int gamestate;
	public static int start=0;
	public static int prepare=1;
	public static int running=2;
	public static int end=3;
	
	
	JLabel begin=new JLabel("开始");
	JButton stop=new JButton("退出游戏");
	JLabel label=new JLabel("是否保存成绩？");
	JButton yes = new JButton("是");
	JButton no = new JButton("否");
	
	/*
	 *钩子变量 
	 */
	public int hook_x=550;
	public int hook_y=120;		
	public static int angle=0;
	public int angle_speed=10;
	public static int speed=30; 
	public static int hookstate;//钩子状态
	public static int sway=0;
	public static int stretch=1;
	public static int back=2;
	public static int staticstate=3;
	
	public static int num=1;
	public int o_score=0;
	public static int goal_score=650;
	public static int get_score=0; 
	public static int game_time=60;
	
	/*
	 * 物体的大小
	 */
	public int big=3;
	public int mid=2;
	public int small=1;
	
	/*
	 * 创建物体
	 */ 
	ArrayList<Object> objects=new ArrayList<>();
	ArrayList<ArrayList<Integer>> o_num=new ArrayList<>();
	
	/*
	 * 抓取时的初始坐标
	 */
	private int b_x=0;
	private int b_y=0;
	private int b_n=0;
	private int b_i=0;//抓取物体的编号
	private static int boomstate;
	private int b_panel=0;
	private int b_object=1;
	
	
	/*
	 * buff
	 */
	public static int strongbuff=0;
	public static int bufftime=15;
	public GoldMinerGame() {
		try (BufferedReader reader=new BufferedReader(new FileReader(new File("file/O_num")))) {
			String line=null;
			while((line=reader.readLine())!=null) {
				ArrayList<Integer> list=new ArrayList<>();
				String[] a=line.split(" ");
				for(int i=0;i<a.length;i++) {
					list.add(Integer.parseInt(a[i]));
				}
				o_num.add(list);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		try {
			start0=ImageIO.read(new File("image/start.png"));
			start1=ImageIO.read(new File("image/start1.png"));
			background=ImageIO.read(new File("image/background.png"));
			gameover=ImageIO.read(new File("image/end.png"));
			buff=ImageIO.read(new File("image/buff.png"));
			gold=ImageIO.read(new File("image/gold.png"));
			stone=ImageIO.read(new File("image/stone.png"));
			diamond=ImageIO.read(new File("image/diamond.png"));
		} catch (Exception e) {
			e.getStackTrace();
		} 
		
		this.setLayout(null);
		begin.setBounds(170,150,200,100);
		Font font = new Font("宋体", Font.BOLD, 50); 
		begin.setFont(font);
		begin.setForeground(new Color(138, 54, 15));
		begin.setVisible(false);
		this.add(begin);
		stop.setBounds(650,10,180,70);
		Font font1 = new Font("宋体", Font.BOLD, 30); 
		stop.setFont(font1);
		stop.setBackground(Color.orange);
		stop.setForeground(new Color(138, 54, 15));
		stop.setVisible(false);
		this.add(stop);	
		yes.setBounds(300,500,180,70);
		yes.setFont(font1);
		yes.setBackground(Color.orange);
		yes.setForeground(new Color(138, 54, 15));
		yes.setVisible(false);
		no.setBounds(500,500,180,70);
		no.setFont(font1);
		no.setBackground(Color.orange);
		no.setForeground(new Color(138, 54, 15));
		no.setVisible(false);
		label.setVisible(false);
		label.setBounds(270,400,400,100);
		label.setFont(font);
		label.setForeground(new Color(138, 54, 15));
		label.setVisible(false);
		this.add(label);
		this.add(yes );
		this.add(no);
		Timer time=null;
		ActionListener listener=new ActionListener() {
			private int sum=0;
			public void actionPerformed(ActionEvent e) {
				
				if(gamestate!=end) {
					if(gamestate==start) {
						begin.setVisible(true);
					}
					if(gamestate==running) {
						stop.setVisible(true);
					}
					else
						stop.setVisible(false);
					if(gamestate==prepare) {
						sum++;
						if(sum==10) {
							sum=0;
							gamestate=running;
						}
					}
					if(gamestate==running) {
						/*
						 * 时间控制
						 */
						if(sum==0)
							addobject();
						sum++;
						if(strongbuff>0) {
							if(strongbuff>1) {
								bufftime=15;
								strongbuff=1;
							}
							else {
								if(sum%10==0)
									bufftime--;
								if(bufftime<0) {
									bufftime=15;
									strongbuff=0;
								}
							}
						}
						if(sum%10==0)
							game_time--;
						if(game_time<0) {
							if(get_score>=goal_score) {
								sum=0;
								num++;
								strongbuff=0;
								goal_score=650+700*(num-1);
								game_time=60;
								objects=new ArrayList<>();
								gamestate=prepare;
							}
							else {
								gamestate=end;
							}
						}
					/*
					 * 钩子摆动控制
					 */
						if(hookstate==sway) {
							angle+=angle_speed;
							if(angle>=180||angle<=0)
								angle_speed=-angle_speed;
							hook_x=(int) (500-50*Math.cos(Math.toRadians(angle)));
							hook_y=(int) (120+50*Math.sin(Math.toRadians(angle)));
						}
						if(hookstate==stretch) {
							if(b_n==0) {
								b_x=hook_x;
								b_y=hook_y;
							}
							b_n=1;
							if(strongbuff>0) {
								hook_x=(int) (hook_x-40*Math.cos(Math.toRadians(angle)));
								hook_y=(int) (hook_y+40*Math.sin(Math.toRadians(angle)));
							}
							else {
								hook_x=(int) (hook_x-speed*Math.cos(Math.toRadians(angle)));
								hook_y=(int) (hook_y+speed*Math.sin(Math.toRadians(angle)));
							}
							if(boompanel(hook_x,hook_y)||boomobject(hook_x,hook_y)) {
								hookstate=back;
							}
						}
						if(hookstate==back) {
							if(hook_y>b_y) {
								if(strongbuff>0) {
									if(boomstate==b_object){
										hook_x=(int) (hook_x+40*Math.cos(Math.toRadians(angle)));
										hook_y=(int) (hook_y-40*Math.sin(Math.toRadians(angle)));
										objects.get(b_i).buffstep(angle);
									}
									else {
										hook_x=(int) (hook_x+40*Math.cos(Math.toRadians(angle)));
										hook_y=(int) (hook_y-40*Math.sin(Math.toRadians(angle)));
									}
								}
								else {
									if(boomstate==b_object){
										hook_x=(int) (hook_x+objects.get(b_i).speed*Math.cos(Math.toRadians(angle)));
										hook_y=(int) (hook_y-objects.get(b_i).speed*Math.sin(Math.toRadians(angle)));
										objects.get(b_i).step(angle);
									}
									else {
										hook_x=(int) (hook_x+speed*Math.cos(Math.toRadians(angle)));
										hook_y=(int) (hook_y-speed*Math.sin(Math.toRadians(angle)));
									}
								}
							}
//								if(boomstate==b_object) {
//									if(strongbuff==0) {
//										hook_x=(int) (hook_x+objects.get(b_i).speed*Math.cos(Math.toRadians(angle)));
//										hook_y=(int) (hook_y-objects.get(b_i).speed*Math.sin(Math.toRadians(angle)));
//										objects.get(b_i).step(angle);
//									}
//									else {
//										hook_x=(int) (hook_x+40*Math.cos(Math.toRadians(angle)));
//										hook_y=(int) (hook_y-40*Math.sin(Math.toRadians(angle)));
//										objects.get(b_i).buffstep(angle);
//									}
//								}
//								else {
//									hook_x=(int) (hook_x+speed*Math.cos(Math.toRadians(angle)));
//									hook_y=(int) (hook_y-speed*Math.sin(Math.toRadians(angle)));
//								}
							else {
								if(boomstate==b_object) {
									ifbuff();
									o_score=objects.get(b_i).score;
									get_score+=o_score;
									b_n=0;
									hookstate=sway;
									objects.remove(b_i);
								}
								// Insert
								else {
										b_n=0;
										hookstate=sway;
								}
							}
						}
					}
				}
				else {
					stop.setVisible(false);
					yes.setVisible(true);
					no.setVisible(true);
					label.setVisible(true);
				}
				repaint();
			}
		};
		time=new Timer(100, listener);
		time.start();
		begin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(gamestate==start) {
					gamestate=prepare;
					begin.setVisible(false);
				}
			}
		});
		stop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(gamestate==running) {
					gamestate=end;	
				}
			}
		});
		yes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				try (BufferedWriter writer=new BufferedWriter(new FileWriter(new File("file/score"),true))) {
					writer.write(get_score+"");
					writer.newLine();
				} catch (Exception e1) {
					// TODO: handle exception
				}
				System.out.println(get_score);
			}
		});
//		this.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("mousemousemousemousemousemouse");
//				if(gamestate==running&&hookstate==sway) {
//					hookstate=stretch;
//				}
//			}
//		});
//	
	}
	private void ifbuff() {
		// TODO Auto-generated method stub
		if(objects.get(b_i) instanceof Buff) {
			Buff a=(Buff)objects.get(b_i);
			strongbuff+=a.getS_buff();
		}
	}

	
	private void addobject() {
		// TODO Auto-generated method stub
		Gold a=new Gold(big);
		objects.add(a);
		int b_gold=0;
		int m_gold=0;
		int s_gold=0;
		int b_stone=0;
		int m_stone=0;
		int s_stone=0;
		int diamond=0;
		int buff=0;
		while(b_gold<o_num.get(num-1).get(0)-1) {
			Gold gold=new Gold(big);
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				b_gold++;
				}
		}
		while(m_gold<o_num.get(num-1).get(1)) {
			Gold gold=new Gold(mid);
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				m_gold++;
				}
		}
		while(s_gold<o_num.get(num-1).get(2)) {
			Gold gold=new Gold(small);
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				s_gold++;
				}
		}
		while(b_stone<o_num.get(num-1).get(3)) {
			Stone gold=new Stone(big);
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				b_stone++;
				}
		}
		while(m_stone<o_num.get(num-1).get(4)) {
			Stone gold=new Stone(mid);
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				m_stone++;
				}
		}
		while(s_stone<o_num.get(num-1).get(5)) {
			Stone gold=new Stone(small);
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				s_stone++;
				}
		}
		while(buff<o_num.get(num-1).get(6)) {
			Buff gold=new Buff();
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				buff++;
			}
		}
		while(diamond<o_num.get(num-1).get(7)) {
			Diamond gold=new Diamond();
			int sum=0;
			for(int i=0;i<objects.size();i++) {
				if(!Object.boom(gold, objects.get(i)))
					sum++;
			}
			if(sum==objects.size()) {
				objects.add(gold);
				diamond++;
				}
		}
	}
	private boolean boompanel(int x,int y) {
		boomstate=b_panel;
		return x<=0||x>=1000||y>=800;
	}
	private boolean boomobject(int x, int y) {
		// TODO Auto-generated method stub
		for(int i=0;i<objects.size();i++) {
			int x1= objects.get(i).x+objects.get(i).width/2 ;
			int y1= objects.get(i).y+objects.get(i).height/2;
			boolean H = Math.abs(x - x1) < objects.get(i).width/2 ;
			boolean V = Math.abs(y -y1) < objects.get(i).height/2;
			if(H&&V) {
				b_i=i;
				boomstate=b_object;
				objects.get(b_i).x=hook_x-objects.get(b_i).width/2;
				objects.get(b_i).y=hook_y;
				return true;
			}
		}
		return false;
	}
	@Override
	protected void paintComponent(Graphics g) {
		if(gamestate==start) {
			g.drawImage(start0, 0, 0,this.getWidth(),this.getHeight(),this);
		}
		if(gamestate==prepare) {
			g.drawImage(start1, 0, 0,this.getWidth(),this.getHeight(),this);
			paintScore(g);
		}
		if(gamestate==running) {
			g.drawImage(background, 0, 0,this.getWidth(),this.getHeight(),this);
			paintHook(g);
			paintscoretime(g);
			paintobject(g);
			if(strongbuff==1)
				paintnbufftime(g);
		}
		if(gamestate==end) {
			g.drawImage(gameover, 0, 0,this.getWidth(),this.getHeight(),this);
		}
	}
	private void paintnbufftime(Graphics g) {
		// TODO Auto-generated method stub
		int x=300;
		int y=50;
		Font font = new Font("宋体", Font.BOLD, 30); 
		g.setFont(font);
		g.setColor(Color.red);
		g.drawString("buff时间："+bufftime+"s", x,y);
	}
	private void paintobject(Graphics g) {
		for(int i=0;i<objects.size();i++)
			g.drawImage(objects.get(i).image, objects.get(i).x, objects.get(i).y,
					objects.get(i).width,objects.get(i).height,null);
		
	}
	private void paintscoretime(Graphics g) {
		// TODO Auto-generated method stub
		int x=120;
		int y=40;
		Font font = new Font("宋体", Font.BOLD, 35); 
		g.setFont(font);
		g.setColor(Color.green);
		g.drawString(get_score+"", x,y);
		int x1=x+70;
		int y1=y+45;
		g.setColor(new Color(138, 54, 15));
		g.drawString(goal_score+"", x1,y1);
		x=930;
		g.drawString(game_time+"", x,y);
		x1=890;
		g.drawString(num+"", x1,y1);
	}
	private void paintHook(Graphics g) {
		// TODO Auto-generated method stub
		g.drawLine(500, 120, hook_x, hook_y);
	}
	private void paintScore(Graphics g) {
		Font font = new Font("宋体", Font.BOLD, 70); 
		g.setFont(font);
		g.setColor(Color.green);
		g.drawString(goal_score+"", 340, 340);
	}
	public static void main(String[] args) {
		GoldMinerGame game=new GoldMinerGame();
		JFrame frame=new JFrame("黄金矿工");
		
		frame.setBounds(100, 100, panel_width,panel_height );
		frame.add(game);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
