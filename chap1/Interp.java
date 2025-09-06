class Interp {
  /*
   * Returns a new symbol table after interpreting the stmt 
   * provided the existing symbol table t before interpretation.
   */
  static Table interpStm(Stm stmt, Table t){
    return switch(stmt){
      case CompoundStm s -> {
        Table t1 = interpStm(s.stm1, t);
        yield interpStm(s.stm2, t1);
      }
      case AssignStm s -> {
        // Evaluate the expression first
        IntAndTable evaluated = interpExp(s.exp, t);
        Table t1 = evaluated.t;
        yield t1.update(s.id, evaluated.i);
      }
      case PrintStm s -> {
        ExpList exps = s.exps;
        Table current  = t;
        // Iterate through each Exp in the ExpList
        while (exps instanceof PairExpList l) {
          Exp exp = l.head;
          IntAndTable evaluated = interpExp(exp, current);
          System.out.print(evaluated.i + " ");
          current = evaluated.t;
          exps = l.tail;
        }
        // Should only have the tail now
        if (exps instanceof LastExpList l){
          Exp exp = l.head;
          IntAndTable evaluated = interpExp(exp, current);
          System.out.print(evaluated.i + "\n");
          current = evaluated.t;
        }
        yield current;
      }
      default -> throw new IllegalArgumentException("Bad stmt provided");
    };
  }

  static IntAndTable interpExp(Exp exp, Table t){
    return switch(exp){
      case IdExp e -> {
        // Possibility for a null lookup here as we are
        // not doing semantic analysis before interpreting
        // lookup will implicitly raise an exception
        int value = t.lookup(e.id);
        yield new IntAndTable(value, t);
      }
      case NumExp e -> new IntAndTable(e.num, t);
      case OpExp e -> {
        IntAndTable evaluatedLeft = interpExp(e.left, t);
        Table t1 = evaluatedLeft.t;
        IntAndTable evalulatedRight = interpExp(e.right, t1);
        Table t2 = evalulatedRight.t;
        int leftVal = evaluatedLeft.i, rightVal = evalulatedRight.i;
        int result = switch (e.oper) {
            case OpExp.PLUS    -> leftVal + rightVal;
            case OpExp.MINUS   -> leftVal - rightVal;
            case OpExp.TIMES   -> leftVal * rightVal;
            case OpExp.DIVIDE  -> {
              if (rightVal == 0)
                throw new ArithmeticException("Attempted divison by zero!");
              yield leftVal / rightVal; 
            }
            // e.oper is an enum (int) so the error logging is not great here
            default -> 
                throw new IllegalArgumentException("Unknown operator: " + e.oper);
        };
        yield new IntAndTable(result, t2);
      }
      case EseqExp e -> {
        // Evaluate the stmt for side effects first then
        // the exp
        Table t1 = interpStm(e.stm, t);
        yield interpExp(e.exp, t1);
      }
      default -> throw new IllegalArgumentException("Bad exp provided");
    };
  }


  static void interp(Stm stmt) { 
    // We start of with an empty symbol Table
    interpStm(stmt, new EmptyTable());
  }
  
  /*
   * To avoid using instanceof we can also make methods for the 
   * Stm and Exp classes as stated in the book but I find this simpler.
   */
  static int maxargs(Stm stmt) {
    return switch(stmt) {
      case CompoundStm s -> Math.max(maxargs(s.stm1), maxargs(s.stm2));
      case AssignStm s -> {
        if (s.exp instanceof EseqExp eseq)
          yield Math.max(maxargs(eseq.stm), maxargsExp(eseq.exp));
        yield 0;
      }
      case PrintStm s -> {
        int count = 1;
        ExpList exps = s.exps;
        while (exps instanceof PairExpList l) {
            exps = l.tail;   
            count++;
        }
        yield count;
      }
      default -> throw new IllegalArgumentException("Bad stmt provided");
    };
  }

  static int maxargsExp(Exp exp) {
    return switch (exp) {
      case IdExp e      -> 0;
      case NumExp e     -> 0;
      case OpExp e      -> Math.max(maxargsExp(e.left), maxargsExp(e.right));
      case EseqExp e    -> Math.max(maxargs(e.stm), maxargsExp(e.exp));
      default           -> throw new IllegalArgumentException("Bad exp provided");
    };
  }


  public static void main(String args[]) throws java.io.IOException {
     System.out.println(maxargs(Prog.prog));
     interp(Prog.prog);
  }
}
