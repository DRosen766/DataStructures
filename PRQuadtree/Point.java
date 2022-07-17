// Represents a point in the xy-plane with integer-valued coordinates.
// Supplies comparison functions specified in the Compare2D interface.
//

public class Point implements Compare2D<Point> {

	public long xcoord;
	public long ycoord;

	public Point() {
		xcoord = 0;
		ycoord = 0;
	}

	public Point(long x, long y) {
		xcoord = x;
		ycoord = y;
	}

	public long getX() {
		return xcoord;
	}

	public long getY() {
		return ycoord;
	}

	public Direction directionFrom(long X, long Y) {
		long xDiff = xcoord - X;
		long yDiff = ycoord - Y;
		System.out.printf("xcoord: %d | ycoord: %d\n", xcoord, ycoord);
		System.out.printf("xdiff: %d | ydiff: %d\n", xDiff, yDiff);
		// if xDiff is positive and yDiff is positive or the points are the same, Direction = NE
		if ((xDiff > 0 && yDiff >= 0) || (xDiff == 0 && yDiff == 0)) {
			return Direction.NE;
		}
		// if xDiff is positive and yDiff is negative, Direction = SE
		else if (xDiff >= 0 && yDiff < 0) {
			return Direction.SE;
		}
		// if xDiff is negative and yDiff is positive, Direction = NW
		else if (xDiff <= 0 && yDiff > 0) {
			return Direction.NW;
		}
		// if xDiff is negative and yDiff is negative, Direction = SW
		else if (xDiff < 0 && yDiff <= 0) {
			return Direction.SW;
		}
		return Direction.NOQUADRANT;
	}

	public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) {
//		if point is outside square, it is in noquadrant
		if(xcoord > xHi || xcoord < xLo || ycoord > yHi || ycoord < yLo) {
			return Direction.NOQUADRANT;
		}
		// will be negative if on east side, positive if west side
		double xDifferentiator = xcoord - ((xLo + xHi) / 2.0);
		
		// will be negative if on north side, positive if south side
		double yDifferentiator = ycoord - ((yLo + yHi) / 2.0);
		
//		determine quadrant
		if(xDifferentiator <= 0 && yDifferentiator > 0) {
			return Direction.NW;
		}
		else if(xDifferentiator < 0 && yDifferentiator <= 0) {
			return Direction.SW;
		}
		else if(xDifferentiator >= 0 && yDifferentiator < 0) {
			return Direction.SE;
		}
		else if((xDifferentiator > 0 && yDifferentiator >= 0) || (xDifferentiator == 0 && yDifferentiator == 0)) {
			return Direction.NE;
		}
		return Direction.NOQUADRANT;
	}

	public boolean inBox(double xLo, double xHi, double yLo, double yHi) {
		if (xcoord >= xLo && xcoord <= xHi && ycoord >= yLo && ycoord <= yHi) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {

		// Do not change...
		return new String("(" + xcoord + ", " + ycoord + ")");
	}

	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		else if(!(o.getClass().equals(this.getClass()))) {
			return false;
		}
		else if(((Point) o).xcoord == this.xcoord && ((Point) o).ycoord == this.ycoord) {
			return true;
		}
		else {
			return false;
		}
	}
}
