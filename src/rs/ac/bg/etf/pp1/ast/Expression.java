// generated with ast extension for cup
// version 0.8
// 7/0/2021 20:6:41


package rs.ac.bg.etf.pp1.ast;

public class Expression extends Expr {

    private Terms Terms;

    public Expression (Terms Terms) {
        this.Terms=Terms;
        if(Terms!=null) Terms.setParent(this);
    }

    public Terms getTerms() {
        return Terms;
    }

    public void setTerms(Terms Terms) {
        this.Terms=Terms;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Terms!=null) Terms.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Terms!=null) Terms.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Terms!=null) Terms.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expression(\n");

        if(Terms!=null)
            buffer.append(Terms.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expression]");
        return buffer.toString();
    }
}
