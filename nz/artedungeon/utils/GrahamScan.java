package nz.artedungeon.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.rsbuddy.script.wrappers.Tile;

class GrahamScan {
	// variables
    private final int pNum;
	private final int[] xPoints;
	private final int[] yPoints;
	private int num;
	private int[] xPoints2;
	private int[] yPoints2;

	public GrahamScan(Tile[] tiles) {
		xPoints = new int[tiles.length];
		yPoints = new int[tiles.length];
		pNum = tiles.length;
		for(int i = 0;i < tiles.length;i++) {
			xPoints[i] = tiles[i].getX();
			yPoints[i] = tiles[i].getY();
		}
	}
	
	class pData implements Comparable<pData> {
		final int index;
		final double angle;
		final long distance;

		pData(int i, double a, long d) {
			index = i;
			angle = a;
			distance = d;
		}

		// for sorting
		public int compareTo(pData p) {
			if (this.angle < p.angle)
				return -1;
			else if (this.angle > p.angle)
				return 1;
			else {
				if (this.distance < p.distance)
					return -1;
				else if (this.distance > p.distance)
					return 1;
			}
			return 0;
		}
	}

	double angle(int o, int a) {
		return Math.atan((double) (yPoints[a] - yPoints[o]) / (double) (xPoints[a] - xPoints[o]));
	}

	long distance(int a, int b) {
		return ((xPoints[b] - xPoints[a]) * (xPoints[b] - xPoints[a]) + (yPoints[b] - yPoints[a]) * (yPoints[b] - yPoints[a]));
	}

	int ccw(int p1, int p2, int p3) {
		return (xPoints[p2] - xPoints[p1]) * (yPoints[p3] - yPoints[p1]) - (yPoints[p2] - yPoints[p1]) * (xPoints[p3] - xPoints[p1]);
	}

	void swap(int[] stack, int a, int b) {
		int tmp = stack[a];
		stack[a] = stack[b];
		stack[b] = tmp;
	}

	public Tile[] grahamsScan() {
		// convex hull routine

		// (0) find the lowest point
		int min = 0;
		for (int i = 1; i < pNum; i++) {
			if (yPoints[i] == yPoints[min]) {
				if (xPoints[i] < xPoints[min])
					min = i;
			}
			else if (yPoints[i] < yPoints[min])
				min = i;
		}

		ArrayList<pData> al = new ArrayList<pData>();
		double ang;
		long dist;
		// (1) Calculationsulate angle and distance from base
		for (int i = 0; i < pNum; i++) {
			if (i == min)
				continue;
			ang = angle(min, i);
			if (ang < 0)
				ang += Math.PI;
			dist = distance(min, i);
			al.add(new pData(i, ang, dist));
		}
		// (2) sort by angle and distance
		Collections.sort(al);

		// (3) create stack
		int stack[] = new int[pNum + 1];
		int j = 2;
		for (int i = 0; i < pNum; i++) {
			if (i == min)
				continue;
			pData data = al.get(j - 2);
			stack[j++] = data.index;
		}
		stack[0] = stack[pNum];
		stack[1] = min;

		// (4)
		int tmp;
		int M = 2;
		for (int i = 3; i <= pNum; i++) {
			while (ccw(stack[M - 1], stack[M], stack[i]) <= 0)
				M--;
			M++;
			swap(stack, i, M);
		}

		// assign border points
		num = M;
		xPoints2 = new int[num];
		yPoints2 = new int[num];
		for (int i = 0; i < num; i++) {
			xPoints2[i] = xPoints[stack[i + 1]];
			yPoints2[i] = yPoints[stack[i + 1]];
		}
		Tile[] tiles = new Tile[xPoints2.length];
		for(int i = 0;i < xPoints2.length;i++) {
			tiles[i] = new Tile(xPoints2[i], yPoints2[i]);
		}
		return tiles;
	}
}
