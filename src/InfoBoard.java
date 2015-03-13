import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class InfoBoard extends Frame {
	
	
	public static final int WIDTH = 200;
	public static final int HEIGHT = 400;
	private TankClient tc;
	Panel p =new Panel(null);
	Image offScreenImage = null;
	
	
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.drawString("TankWar", 10, 40);
		g.drawString("by LiuWenbin", 50, 60);
		g.drawLine(0, 65, 200, 65);
		g.drawString("missiles count   " + tc.missiles.size(), 10, 80);
		g.drawString("explodes count   " + tc.explodes.size(), 10, 100);
		g.drawString("tanks       count   " + tc.tanks.size(), 10, 120);
		g.drawString("Your             life  " + tc.myTank.getLife(), 10, 140);
		g.drawString("wave           " + tc.getTotalTank(), 10, 160);
		g.setColor(c);
	}
	public InfoBoard(TankClient tc) {
		this.tc = tc;
	}
	
	public void lauchFrame() {
		this.setLayout(null);
		this.setLocation(993, 40);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Info");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}
	
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(TankClient.WINDOWREPAINT_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(WIDTH, HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.WHITE);
		gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
}

