
/*
 * This class serves as the symbol table to map identifiers to 
 * their values.
 */
abstract class Table {
  abstract int lookup(String key);
  abstract Table update(String key, int value);
}


class NonEmptyTable extends Table {
  String id;
  int value;
  Table tail;

  public NonEmptyTable(String i, int v, Table t){
    id = i;
    value = v;
    tail = t;
  }

  /*
   * Searches for the key in the symbol table and return its 
   * value if its present otherwise an error occurred
   * (We are looking up a value that doesn't exist).
   */
  @Override
  public int lookup(String key){
    if (id.equals(key)) return value;
    return tail.lookup(key);
  }
  
  /*
   * Note that the old identifier would still be in the symbol table
   * but we are assuming that the first occurence takes precedence.
   */
  @Override
  public Table update(String key, int value){
    return new NonEmptyTable(key, value, this);
  }
}

class EmptyTable extends Table {
  // No fields empty constructor
  public EmptyTable(){}

  @Override 
  public int lookup(String key){
    throw new RuntimeException("The key: " + key + " doesn't exist!");
  }
  
  /*
   * Note that the old identifier would still be in the symbol table
   * but we are assuming that the first occurence takes precedence.
   */
  @Override
  public Table update(String key, int value){
    return new NonEmptyTable(key, value, this);
  }
}



