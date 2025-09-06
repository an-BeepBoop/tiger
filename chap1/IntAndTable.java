/*
 * Helper class for expression interpretation.
 *
 * Expressions are more complex than statements. 
 * - Statements only cause side effects, such as updating the symbol table 
 *   or printing output. 
 * - Expressions both return a value (for example, 3 + 5 evaluates to 8) 
 *   and may also cause side effects (for example, an EseqExp executes a statement before yielding a value).
 *
 * This class bundles together:
 * - i: the integer value produced by evaluating the expression
 * - t: the updated symbol table reflecting any side effects
 */
class IntAndTable {
  int i;
  Table t;
  public IntAndTable(int i, Table t){
    this.i = i;
    this.t = t;
  }
}
