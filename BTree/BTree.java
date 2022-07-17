import java.util.ArrayList;

public class BTree<T extends Comparable<T>> {
	public class BTNode<T extends Comparable<T>> {
		private int order;
//		elements and children public so they can be accessed by BTree
		public ArrayList<T> elements;
		public BTNode<T>[] children;
		public int numChildren;

		public BTNode(int order) {
			this.order = order;
			this.elements = new ArrayList<>();
			this.children = new BTNode[order];
			this.numChildren = 0;
		}

		public boolean isFull() {
			if (this.elements.size() >= this.order - 1) {
				return true;
			} else {
				return false;
			}
		}

//		returns true if this node is a leaf
		public boolean isLeaf() {
			return this.numChildren == 0;
		}

	}

	private int order;
	private BTNode<T> root;

	public BTree(int order) {
		this.order = order;
		this.root = null;
	}

	public void insert(T element) {
//		if BTree is empty
		if (this.root == null) {
			this.root = new BTNode<T>(this.order);
			this.root.elements.add(element);
		}
//		if BTree is not empty call recursive helper function
		else {
			insertHelper(element, this.root, null, null, false);
		}
	}

	private void insertHelper(T element, BTNode<T> currentNode, T upVal, BTNode<T> upChild, boolean splitHappened) {
//		base case: if at leaf
		if (currentNode.isLeaf()) {
//			if not full, insert value, 
			if (!currentNode.isFull()) {
				currentNode.elements.add(element);
			}
//			if full
			else {
//				split off new sibling for current node
				BTNode<T> newSibling = new BTNode<>(this.order);
				
//				calculate the length of the new sibling
				int newSiblingLength = currentNode.elements.size() / 2;
				
//				remove second half of elements in currentNode and place in new sibling
//				add in reverse order
				for(int i = 0; i < newSiblingLength; i++) {
					newSibling.elements.add(0, currentNode.elements.remove(currentNode.elements.size() - 1));
				}
						
						
//				set promote value to median value
				upVal = currentNode.elements.remove(currentNode.elements.size() - 1);
//		`	`	set upChild to new sibling
				upChild = newSibling;
//				set splitHappened to true
				splitHappened = true;
			}
			return;
		}
//		recursive case: not at leaf
		else {
//		determine correct child
//		recursive call on correct child
//		if splitHappened
//			if currentNode not full
//				insert upval and upchild onto currentNode
//			else
//				splitting protocol
//		return
		}
	}
}
