package GoldMiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class Score extends JPanel{
	ArrayList<Integer> list=new ArrayList<>();
	JLabel la=new JLabel("游戏记录");
	public Score() {
		try (BufferedReader reader=new BufferedReader(new FileReader(new File("file/score")))) {
			String line=null;
			while((line=reader.readLine())!=null) {
				list.add(Integer.parseInt(line));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		Collections.sort(list);
		this.add(la);
		for(int i=0;i<list.size();i++) {
			JLabel label=new JLabel("| "+(i+1)+"| "+list.get(i)+" |");
			this.add(label);
		}
	}
	public static void main(String[] args) {
		Score game=new Score();
		JFrame frame=new JFrame("游戏记录");
		frame.setBounds(100, 100, 500,500 );
		frame.add(game);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
