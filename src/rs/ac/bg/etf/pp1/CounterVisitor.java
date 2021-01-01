package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {

	protected int cnt;
	
	public int getCount() {
		return cnt;
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(VarDefinition varDefinition) {
			cnt++;
		}
		
		public void visit(VarDefArr varDefArr) {
			cnt++;
		} 
	}
	
	
	public static class FormalParamCounter extends CounterVisitor{
		
		public void visit(FormalParamDecl formalParamDecl) {
			cnt++;
		}
		
	}
	
}
