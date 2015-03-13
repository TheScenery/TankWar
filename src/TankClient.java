import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;;




public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int WINDOWREPAINT_TIME = 20;

	Tank myTank = new Tank(GAME_WIDTH/2, GAME_HEIGHT, true, this);
	InfoBoard ib = new InfoBoard(this);
	private int totalTank = 0;
	
	
	public int getTotalTank() {
		return totalTank;
	}

	public void setTotalTank(int totalTank) {
		this.totalTank = totalTank;
	}

	List<Missile> missiles =  new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	Image offScreenImage = null;
	
	
	
	public void paint(Graphics g) {
		myTank.draw(g);
		
		for(int i=0; i<tanks.size(); i++) {
			Tank eT = tanks.get(i);
			eT.collidesWithTanks(tanks);
			eT.draw(g);
		}
		
		for(int i=0;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hit(myTank);
			m.draw(g);
		}
		
		for(int i=0;i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
	}
	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void lauchFrame() {
		
		this.setLocation(183, 40);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		new Thread(new PaintThread()).start();
		new Thread(new AddTankThread(this)).start();
	}


	public static void main (String[] args) {
		TankClient tc = new TankClient();
	    tc.ib.lauchFrame();
		tc.lauchFrame();
		
	}
	
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(WINDOWREPAINT_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
	
	private class AddTankThread implements Runnable {
		
		private TankClient tc;
		public AddTankThread(TankClient tc) {
			
			this.tc = tc;
		}
		
		public void run() {
			
			while(true) {
				while (tc.tanks.size() <= 0) {
					for(int i=0; i<10; i++) {
						Tank t = new Tank(50+i*40, 50, false, tc, Tank.Direction.D);
						tc.tanks.add(t);	
					}
					tc.setTotalTank(tc.getTotalTank()+1);
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
		    }
		}
	}
  }
}


