import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class Missile {
	
	public static final int XSPEED = 8;
	public static final int YSPEED = 8;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x,y;
	Tank.Direction dir;
	private boolean live = true;
	private boolean good = true;
	private int eR = 0;
	

	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}

	
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	
	public boolean hit(Tank t) {
		if(this.good != t.isGood() && this.getRec().intersects(t.getRec()) && this.live) {
			if(this.eR == 0) {
				this.live = false;
			}
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife() <= 0) {
				   t.setLive(false);
				}
			
			}
			else t.setLive(false);
			tc.explodes.add(new Explode(x,y,tc));
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hit(tanks.get(i)))
			 return true;
		}
		return false;
	}
	
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x+Tank.WIDTH/2-WIDTH/2;
		this.y = y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Tank.Direction dir, TankClient tc) {
		this(x,y,dir);
		this.tc = tc;
	}
	
	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
		this(x, y, dir, tc);
		this.good = good;
	}
	
	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc, int z) {
		this(x, y, true, dir, tc);
		this.eR = z;
	}
	
	public void draw(Graphics g) {
		if(!isLive()) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
			if(this.good) {
				g.setColor(Color.BLACK);
				g.fillOval(x - eR/2, y - eR/2, 10 + eR, 10 + eR);
				g.setColor(c);
		}
		
		if(!this.good) {
			g.setColor(Color.BLUE);
			g.fillOval(x - eR/2, y - eR/2, 10 + eR, 10 + eR);
			g.setColor(c);
		}
		
		move();
	}

	private void move() {
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y>TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
	}
	
}

