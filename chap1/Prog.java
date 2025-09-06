class Prog {
static Stm prog = 
new CompoundStm(new AssignStm("a",new OpExp(new NumExp(4), OpExp.PLUS, 
					    new NumExp(3))),
 new CompoundStm(new AssignStm("b",
     new EseqExp(new PrintStm(new PairExpList(new IdExp("a"),
                       new LastExpList(new OpExp(new IdExp("a"), OpExp.MINUS,
				  	         new NumExp(1))))),
             new OpExp(new NumExp(10), OpExp.TIMES, new IdExp("a")))),
  new PrintStm(new LastExpList(new IdExp("b")))));
}
