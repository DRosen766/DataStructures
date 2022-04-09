
public class SplayTreeDriver {

	public static void main(String[] args) {
		SplayTree<String> splayTree = new SplayTree<String>();
		String[] integerList = {"10", "05", "15", "03", "07", "13", "17"};
		for(String s : integerList) {
			splayTree.insert(s);
		}
		splayTree.find("15");
		splayTree.display();
	}

}

