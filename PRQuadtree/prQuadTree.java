import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

// To support testing, we will make certain elements of the generic.
//
// You may safely add data members and function members as needed, but
// you must not modify any of the public members that are shown.
//
public class prQuadTree<T extends Compare2D<? super T>> {
	// Inner classes for nodes (public so test harness has access)
	public abstract class prQuadNode {
		abstract boolean isLeaf();
	}

	public class prQuadLeaf extends prQuadNode {

		// Use an ArrayList to support a bucketed implementation later.
		ArrayList<T> Elements;

		// define bucket size
		private final int bucketSize = 1;

		// constructor for adding a single element
		public prQuadLeaf() {
//			if leaf is empty, Elements will always be null, 
//			this will be implemented in remove function
			this.Elements = new ArrayList<>();
		}

		public void setElements(ArrayList<T> Elements) {
			this.Elements = Elements;
		}

		public ArrayList<T> getElements() {
			return this.Elements;
		}

		public boolean isLeaf() {
			return true;
		}

		public boolean isEmpty() {
			boolean retVal = (this.Elements.size() == 0) ? true : false;
			return retVal;
		}

		public boolean isFull() {
			boolean retVal = (this.Elements.size() < this.bucketSize) ? false : true;
			return retVal;
		}
	}

	public class prQuadInternal extends prQuadNode {
		// Use base-type pointers since children can be either leaf nodes
		// or internal nodes.
		prQuadNode NW, NE, SE, SW;

		// constructor
		public prQuadInternal() {
			this.NW = null;
			this.NE = null;
			this.SE = null;
			this.SW = null;

		}

		public boolean isLeaf() {
			return false;
		}

		public prQuadNode getNW() {
			return this.NW;
		}

		public prQuadNode getNE() {
			return this.NE;
		}

		public prQuadNode getSE() {
			return this.SE;
		}

		public prQuadNode getSW() {
			return this.SW;
		}

	}

	// prQuadTree elements (public so test harness has access)
	public prQuadNode root;
	public long xMin, xMax, yMin, yMax;

	// Add private data members as needed...
	private boolean elementInserted = false;
	private ArrayList<T> findRegionHelperList;

	// Initialize quadtree to empty state, representing the specified region.
	// Pre: xMin < xMax and yMin < yMax
	public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
//	   instantiate instance variables
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

//	   create root node
		this.root = null;

	}

	// Pre: elem != null
	// Post: If elem lies within the tree's region, and elem is not already
	// present in the tree, elem has been inserted into the tree.
	// Return true iff elem is inserted into the tree.
	public boolean insert(T elem) {
		// base case: element is null, return false
		if (elem == null) {
			return false;
		}
		// base case: element is not in region, return false
		else if (!elem.inBox(xMin, xMax, yMin, yMax)) {
			return false;
		}
		// base case: tree is empty, insert element, return true
		else if (this.root == null) {
			prQuadLeaf rootNode = new prQuadLeaf();
			ArrayList<T> rootNodeElements = new ArrayList<T>();
			rootNodeElements.add(elem);
			rootNode.setElements(rootNodeElements);
			this.root = rootNode;
			return true;
		}

		this.elementInserted = false;
		this.root = insertHelper(this.root, elem, this.xMin, this.xMax, this.yMin, this.yMax);
		return this.elementInserted;
	}

	private prQuadNode insertHelper(prQuadNode currentNode, T elem, long xMin, long xMax, long yMin, long yMax) {
		// base case: currentNode is null, insert element,return new leaf containing
		// elem
		if (currentNode == null) {

			prQuadLeaf newLeaf = new prQuadLeaf();
			ArrayList<T> list = new ArrayList<>();
			list.add(elem);
			((prQuadLeaf) newLeaf).setElements(list);
			this.elementInserted = true;
			return newLeaf;
		}
		// base case: currentNode is leaf, elem exists in bucket, insert nothing,return
		// currentNode with no changes
		else if (currentNode.isLeaf() && ((prQuadLeaf) currentNode).getElements().get(0).equals(elem)) {
			return currentNode;
		}

		// recursive case: currentNode is a leaf with full bucket, call insertHelper on
		// newly created internal node
		else if (currentNode.isLeaf() && ((prQuadLeaf) currentNode).isFull()) {
			prQuadNode newInternal = new prQuadInternal();
			T extistingElement = ((prQuadLeaf) currentNode).getElements().get(0);
			newInternal = insertHelper(newInternal, extistingElement, xMin, xMax, yMin, yMax);
			return insertHelper(newInternal, elem, xMin, xMax, yMin, yMax);
		}
		// recursive case: currentNode is an internalNode
		else {
			Direction direction = elem.inQuadrant(xMin, xMax, yMin, yMax);
			if (direction.equals(Direction.NW)) {
				xMax = (xMin + xMax) / 2;
				yMin = (yMin + yMax) / 2;
				((prQuadInternal) currentNode).NW = insertHelper(((prQuadInternal) currentNode).NW, elem, xMin, xMax,
						yMin, yMax);
			} else if (direction.equals(Direction.SW)) {
				xMax = (xMin + xMax) / 2;
				yMax = (yMin + yMax) / 2;
				((prQuadInternal) currentNode).SW = insertHelper(((prQuadInternal) currentNode).SW, elem, xMin, xMax,
						yMin, yMax);
			} else if (direction.equals(Direction.NE)) {
				xMin = (xMin + xMax) / 2;
				yMin = (yMin + yMax) / 2;
				((prQuadInternal) currentNode).NE = insertHelper(((prQuadInternal) currentNode).NE, elem, xMin, xMax,
						yMin, yMax);
			} else if (direction.equals(Direction.SE)) {
				xMin = (xMin + xMax) / 2;
				yMax = (yMin + yMax) / 2;
				((prQuadInternal) currentNode).SE = insertHelper(((prQuadInternal) currentNode).SE, elem, xMin, xMax,
						yMin, yMax);
			}
			return currentNode;
		}
	}

	// Pre: elem != null
	// Returns reference to an element x within the tree such that
	// elem.equals(x)is true, provided such a matching element occurs within
	// the tree; returns null otherwise.
	public T find(T Elem) {
//		base case: elem is null, return null
		if (Elem == null) {
			return null;
		}
//		base case: tree is empty, return null
		else if (this.root == null) {
			return null;
		}
//		base case: element is not in box, return null
		else if (!Elem.inBox(xMin, xMax, yMin, yMax)) {
			return null;
		}
//		recursive case: return findHelper
		return findHelper(this.root, Elem, xMin, xMax, yMin, yMax);
	}

	private T findHelper(prQuadNode currentNode, T elem, long xMin, long xMax, long yMin, long yMax) {
//		base case: currentNode is null, return null
		if (currentNode == null) {
			return null;
		}
//		base case: currentNode is leaf, element is in bucket, return element from bucket
		else if (currentNode.isLeaf() && ((prQuadLeaf) currentNode).getElements().indexOf(elem) >= 0) {
			int index = ((prQuadLeaf) currentNode).getElements().indexOf(elem);
			return ((prQuadLeaf) currentNode).getElements().get(index);
		}
//		base case: currentNode is leaf, element is not in bucket, return null
		else if (currentNode.isLeaf() && ((prQuadLeaf) currentNode).getElements().indexOf(elem) < 0) {
			return null;
		}
//		recursive case: currentNode is internal, call findHelper on correct subNode
		else {
			Direction direction = elem.inQuadrant(xMin, xMax, yMin, yMax);
			if (direction.equals(Direction.NW)) {
				xMax = (xMin + xMax) / 2;
				yMin = (yMin + yMax) / 2;
				return findHelper(((prQuadInternal) currentNode).NW, elem, xMin, xMax, yMin, yMax);
			} else if (direction.equals(Direction.NE)) {
				xMin = (xMin + xMax) / 2;
				yMin = (yMin + yMax) / 2;
				return findHelper(((prQuadInternal) currentNode).NE, elem, xMin, xMax, yMin, yMax);

			} else if (direction.equals(Direction.SW)) {
				xMax = (xMin + xMax) / 2;
				yMax = (yMin + yMax) / 2;
				return findHelper(((prQuadInternal) currentNode).SW, elem, xMin, xMax, yMin, yMax);

			} else {
				xMin = (xMin + xMax) / 2;
				yMax = (yMin + yMax) / 2;
				return findHelper(((prQuadInternal) currentNode).SE, elem, xMin, xMax, yMin, yMax);
			}
		}
	}

	// Pre: xLo < xHi and yLo < yHi
	// Returns a collection of (references to) all elements x such that x is
	// in the tree and x lies at coordinates within the defined rectangular
	// region, including the boundary of the region.
	public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi) {
		this.findRegionHelperList = new ArrayList<>();
		findRegionHelper(this.root, xLo, xHi, yLo, yHi, this.xMin, this.xMax, this.yMin, this.yMax);
		return this.findRegionHelperList;
	}

	private void findRegionHelper(prQuadNode currentNode, long xLo, long xHi, long yLo, long yHi, long xMin, long xMax,
			long yMin, long yMax) {
//		base case: if currentNode is null, return
		if (currentNode == null) {
			return;
		}
//		base case: currentNode does not overlap with region, return
		else if ((xMax < xLo && yMax < yLo) || (xMin > xHi && yMin > yHi)) {
			return;
		}
//		base case: currentNode is leaf, add all elements in Region to findRegionHelperList
		else if (currentNode.isLeaf()) {
			for (T element : ((prQuadLeaf) currentNode).getElements()) {
				if (element.inBox(xLo, xHi, yLo, yHi))
					this.findRegionHelperList.add(element);
			}
		}
//		recursive case: currentNode is internal: call findRegionHelper on subNodes that overlap search region
		else {
			findRegionHelper(((prQuadInternal) currentNode).NE, xLo, xHi, yLo, yHi, (xMin + xMax) / 2, xMax,
					(yMin + yMax) / 2, yMax);

			findRegionHelper(((prQuadInternal) currentNode).NW, xLo, xHi, yLo, yHi, xMin, (xMin + xMax) / 2,
					(yMin + yMax) / 2, yMax);

			findRegionHelper(((prQuadInternal) currentNode).SE, xLo, xHi, yLo, yHi, (xMin + xMax) / 2, xMax,
					(yMin + yMax) / 2, yMax);

			findRegionHelper(((prQuadInternal) currentNode).SW, xLo, xHi, yLo, yHi, xMin, (xMin + xMax) / 2, yMin,
					(yMin + yMax) / 2);
		}

	}

	// Add private helpers as needed...
}
