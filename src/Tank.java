
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class Tank {
	public static final int XSPEED = 4;
	public static final int YSPEED = 4;
	public static final int AIXSPEED = 2;
	public static final int AIYSPEED = 2;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private boolean good;
	

	public boolean isGood() {
		return good;
	}

	
	
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}



	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	
	
	public void setLive(boolean live) {
		this.live = live;
	}

	private int x, y;
	private int oldX, oldY;
	private int life = 100;
	private int MP = 100;
	
	
	public int getMP() {
		return MP;
	}

	public void setmP(int mP) {
		this.MP = MP;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}



	TankClient tc;
	private boolean bL=false, bU=false, bR=false, bD = false;
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	
	private Direction dir = Direction.STOP;
	private Direction bDir = Direction.U;
	private BloodBar bb = new BloodBar();

	public Tank(int x, int y, boolean g) {
		this.x = x - this.WIDTH/2;
		this.y = y - this.HEIGHT-10;
		this.oldX = x;
		this.oldY = y;
		this.good = g;
	}
	
	public Tank(int x, int y, boolean g, TankClient tc) {
		this(x,y,g);
		this.tc = tc;
		
	}
	
	public Tank(int x, int y, boolean g, TankClient tc, Tank.Direction dir) {
		this(x, y, g, tc);
		this.dir = dir;
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if(good) {
			bb.draw(g);
			g.setColor(Color.RED);
		}
		else {
			g.setColor(Color.GRAY);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
		
		switch(bDir) {
		case L:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y+HEIGHT/2);
			break;
		case LU:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y);
			break;
		case R:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y+HEIGHT/2);
			break;
		case RD:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y+HEIGHT);
			break;
		case D:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH/2, y+HEIGHT);
			break;
		case LD:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y+HEIGHT);
			break;
		}
	}
	
	void move() {
		this.oldX = x;
		this.oldY = y;
		if(good) {
		switch(dir) {
		case L:
			x -= AIXSPEED;
			break;
		case LU:
			x -= AIXSPEED;
			y -= AIYSPEED;
			break;
		case U:
			y -= AIYSPEED;
			break;
		case RU:
			x += AIXSPEED;
			y -= AIYSPEED;
			break;
		case R:
			x += AIXSPEED;
			break;
		case RD:
			x += AIXSPEED;
			y += AIYSPEED;
			break;
		case D:
			y += AIYSPEED;
			break;
		case LD:
			x -= AIXSPEED;
			y += AIYSPEED;
			break;
		case STOP:
			break;
		}
	}
			switch(dir) {
			case L:
				x -= AIXSPEED;
				break;
			case LU:
				x -= AIXSPEED;
				y -= AIYSPEED;
				break;
			case U:
				y -= AIYSPEED;
				break;
			case RU:
				x += AIXSPEED;
				y -= AIYSPEED;
				break;
			case R:
				x += AIXSPEED;
				break;
			case RD:
				x += AIXSPEED;
				y += AIYSPEED;
				break;
			case D:
				y += AIYSPEED;
				break;
			case LD:
				x -= AIXSPEED;
				y += AIYSPEED;
				break;
			case STOP:
				break;
			}
		
		if(dir != Direction.STOP) {
			bDir = dir;
		}
		
		if(x < 0) x = 0;
		if(y < 25) y = 25;
		if(x > tc.GAME_WIDTH - this.WIDTH) x = tc.GAME_WIDTH - this.WIDTH;
		if(y > tc.GAME_HEIGHT - this.HEIGHT) y = tc.GAME_HEIGHT - this.HEIGHT;
		
		if(!good) {
			Direction[] dirs = dir.values();
			if(step == 0) {
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
				step = r.nextInt(12) + 3;
			}
			step --;
			if(r.nextInt(200) > 197) this.fire();
		}
	
	}
	
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_X :
			this.setLife(100);
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}
	
	public  Missile fire() {
		if(live) {
			Missile m = new Missile(x, y, good, bDir, tc);
			tc.missiles.add(m);
			return m;
		}
		return null;
	}
	
	public Missile bigFire() {
		if(live) {
			Missile m = new Missile(x, y, good, bDir, tc, 50);
			tc.missiles.add(m);
			return m;
		}
		return null;
	}
	
	
	public void superFire() {
		Direction[] bDirs = Direction.values();
		if(live) {
			for (int i=0;i<8;i++)
				tc.missiles.add(new Missile(x,y,bDirs[i],tc));
		}
	}

	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_Z :
			bigFire();
			break;
		case KeyEvent.VK_Q :
			superFire();
			break;
		case KeyEvent.VK_CONTROL :
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		}
		locateDirection();		
	}


	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.drawRect(x, y-12, WIDTH, 10);
			g.setColor(Color.RED);
			int w = WIDTH * life/100;
			g.fillRect(x, y-12, w, 10);
			g.setColor(c);
		}
	}


	public void stay() {
		x = oldX;
		y = oldY;
	}

	
	public boolean collidesWithTanks (List<Tank> tanks) {
		for(int i=0; i<tc.tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this.live && t.isLive() && this != t && this.getRec().intersects(t.getRec())) {
				this .stay();
				t.stay();
				return true;
			}
		}
		return false;
	}


}


