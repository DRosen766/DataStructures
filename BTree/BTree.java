import java.util.ArrayList;

public class  BTree <T extends Comparable<T>>{
	public class BTNode <T extends Comparable<T>>{
		private int order;
//		elements and children public so they can be accessed by BTree
		public ArrayList<T> elements;
		public BTNode[] children;
		
		public BTNode(int order) {
			this.order = order;
			this.elements = new ArrayList<T>();
			this.children = new BTNode[order];
			
		}

		public boolean isFull() {
			if(this.elements.size() >= this.order - 1) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	private int order;
	private BTNode root;
	
	public BTree(int order) {
		this.order = order;
		this.root = null;
	}
	
	
	public void insert(T element) {
//		if BTree is empty
		if(this.root == null) {
			this.root = new BTNode<T>(this.order);
			this.root.elements.add(element);
		}
//		if BTree is not empty call recursive helper function
		else {
			insertHelper(element, this.root, null, null, false);
		}
	}
	
	private void insertHelper(T element, BTNode currentNode, T upVal, BTNode upChild, boolean splitHappened) {
		
	}
}
