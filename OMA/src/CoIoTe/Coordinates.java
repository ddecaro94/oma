package CoIoTe;

import java.util.Random;

public class Coordinates {
	private double cost;
	int i;
	int j;
	int m;
	int t;
	
	public Coordinates(int i, int j, int m, int t, double d) {
		this.i = i;
		this.j = j;
		this.m = m;
		this.t = t;
		this.cost = d;
	}
	public Coordinates(Coordinates c) {
		this.i = c.i;
		this.j = c.j;
		this.m = c.m;
		this.t = c.t;
		this.cost = c.getCost();
	}
	public Double getCost() {
		return this.cost;
	}
	public Integer getI() {
		Random r = new Random();
		if (r.nextBoolean())
			return this.i;
		else
			return -this.i;
	}
	public Integer getJ() {
		return this.j;
	}
	public Integer getM() {
		Random r = new Random();
		if (r.nextBoolean())
			return this.m;
		else
			return -this.m;
	}
	public Integer getT() {
		Random r = new Random();
		if (r.nextBoolean())
			return this.t;
		else
			return -this.t;
	}

}
