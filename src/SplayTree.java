
public class SplayTree<T extends Comparable<T>> {

//	embedded class for nodes of Splay Trees
	public class STNode<T extends Comparable<T>> {
		// STNode instance variables, children public so they can be accessed by
		// SplayTree
		public STNode<T> leftChild;
		public STNode<T> rightChild;
		public T element;

		public STNode(T element) {
			this.element = element;
		}

		public String toString() {
			return (String) this.element;
		}
	}

	private STNode<T> root;

	public SplayTree() {
		this.root = null;
	}

//	insert new value into SplayTree
	public void insert(T element) {
//		call recursive helper function that takes node as an argument
		this.root = insertHelper(this.root, element);
	}

	private STNode<T> insertHelper(STNode<T> currentNode, T element) {
//		base case: currentNode is null, return new STNode with element
		if (currentNode == null) {
			return new STNode<T>(element);
		}

//		base case: duplicate found, return currentNode with no changes
		else if (currentNode.element.compareTo(element) == 0) {
			return currentNode;
		}
//		recursive case: element is less than currentNode.element, set leftChild equal to result of helper function with leftChild as parameter
		else if (currentNode.element.compareTo(element) > 0) {
			currentNode.leftChild = insertHelper(currentNode.leftChild, element);
			return currentNode;
		}

//		recursive case: element is greater than currentNode.element, set rightChild equal to result of helper function with rightChild as parameter	
		else if (currentNode.element.compareTo(element) < 0) {
			currentNode.rightChild = insertHelper(currentNode.rightChild, element);
			return currentNode;
		}
		return null;
	}

//	search for a given element in the SplayTree
//	return pointer to element
//	place target element as root
//	return null if element not in
	public T find(T element) {
//		rotate tree until target node is found
//		while root does not hold element
		while (this.root != null && this.root.element.compareTo(element) != 0) {
//			if element is less than root's element, rotate right
			if (this.root.element.compareTo(element) > 0) {
				this.rightRotation();
			}
//			if element is greater than root's element, rotate left
			else if (this.root.element.compareTo(element) < 0) {
				this.leftRotation();
			}
		}
		return this.root.element;
	}

	private void rightRotation() {
		STNode<T> tempRightChildOfLeftChild = this.root.leftChild.rightChild;
		this.root.leftChild.rightChild = this.root;
		this.root = this.root.leftChild;
		this.root.rightChild.leftChild = tempRightChildOfLeftChild;
	}

//	private method for left rotation
//	all rotations are performed on root
	private void leftRotation() {
		STNode<T> tempLeftChildOfRightChild = this.root.rightChild.leftChild;
		this.root.rightChild.leftChild = this.root;
		this.root = this.root.rightChild;
//		now that original root is now left child of original right child,
//		set right child of current root to tempChild
		this.root.leftChild.rightChild = tempLeftChildOfRightChild;
	}

//	use inorder transversal to display Splay Tree
	public void display() {
//		call recursive helper function that takes node as an argument
		displayHelper(this.root, 0);
	}

	private void displayHelper(STNode<T> currentNode, int height) {
//		base case: if currentNode is null, return
		if (currentNode == null) {
			return;
		}
//		recursive case: descend into leftChild
		displayHelper(currentNode.leftChild, height + 1);
//		print currentNode element
		for (int i = 0; i < height; i++) {
			System.out.print("\t");
		}
		System.out.println(currentNode);
//		recursive case: descend into rightChild
		displayHelper(currentNode.rightChild, height + 1);
	}
}
