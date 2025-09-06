
abstract class Tree {
  abstract Tree insert(String key);
  abstract boolean member(String key);
  abstract String toString(String prefix, boolean isLeft); 
  @Override
  public String toString() {
    return toString("", true);
  }
}

class BinarySearchTree extends Tree {
  Tree left, right;
  String key;

  BinarySearchTree(Tree l, String k, Tree r){
    left = l; 
    right = r;
    key = k;
  }

  @Override
  Tree insert(String key){
    if (key.compareTo(this.key) < 0)
      return new BinarySearchTree(this.left.insert(key), this.key, this.right);
    else if (key.compareTo(this.key) > 0)
      return new BinarySearchTree(this.left, this.key, this.right.insert(key));
    else 
      return new BinarySearchTree(this.left, key, this.right);
  }

  @Override
  boolean member(String key){
    if (key.compareTo(this.key) < 0)
      return this.left.member(key);
    else if (key.compareTo(this.key) > 0)
      return this.right.member(key);
    else 
      return true;
  }

  @Override
  String toString(String prefix, boolean isLeft) {
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);

    if (prefix.isEmpty()) { // root
      sb.append(key).append("\n");
    } else {
      sb.append(isLeft ? "├─ " : "└─ ").append(key).append("\n");
    }

    // Add child prefixes
    String childPrefix = prefix + (isLeft ? "│  " : "   ");

    // Print left child if not empty
    if (!(left instanceof EmptyBST))
        sb.append(left.toString(childPrefix, true));

    // Print right child if not empty
    if (!(right instanceof EmptyBST))
        sb.append(right.toString(childPrefix, false));

    return sb.toString();
  }

  static class EmptyBST extends Tree {
    EmptyBST(){}

    @Override
    Tree insert(String key){
      return new BinarySearchTree(new EmptyBST(), key, new EmptyBST());
    }

    @Override
    boolean member(String key){
      return false;
    }

    @Override
    String toString(String prefix, boolean isLeft) {
      return "";
    }
  }

  public static void main(String[] args) {
    // An example unbalanced (left heavy) tree
    Tree t = new EmptyBST();
    String[] letters = new String[]{"t", "s", "p", "i", "p", "f", "b", "s", "t"};
    for (String letter : letters)
      t = t.insert(letter);
    System.out.println(t);
    
    // An example unbalanced (right heavy) tree
    t = new EmptyBST();
    letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i"};
    for (String letter : letters)
      t = t.insert(letter);
    System.out.println(t);
    
    // Balanced tree example
    t = new EmptyBST();
    letters = new String[]{"e", "b", "g", "a", "c", "d", "f", "h", "i"};
    for (String letter : letters) 
      t = t.insert(letter);
    System.out.println(t);
  }
}

