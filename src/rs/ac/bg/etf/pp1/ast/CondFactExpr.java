// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class CondFactExpr extends Expr {

    private PocetakIzraza PocetakIzraza;
    private CondFact CondFact;
    private Terms Terms;
    private Terms Terms1;

    public CondFactExpr (PocetakIzraza PocetakIzraza, CondFact CondFact, Terms Terms, Terms Terms1) {
        this.PocetakIzraza=PocetakIzraza;
        if(PocetakIzraza!=null) PocetakIzraza.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
        this.Terms=Terms;
        if(Terms!=null) Terms.setParent(this);
        this.Terms1=Terms1;
        if(Terms1!=null) Terms1.setParent(this);
    }

    public PocetakIzraza getPocetakIzraza() {
        return PocetakIzraza;
    }

    public void setPocetakIzraza(PocetakIzraza PocetakIzraza) {
        this.PocetakIzraza=PocetakIzraza;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public Terms getTerms() {
        return Terms;
    }

    public void setTerms(Terms Terms) {
        this.Terms=Terms;
    }

    public Terms getTerms1() {
        return Terms1;
    }

    public void setTerms1(Terms Terms1) {
        this.Terms1=Terms1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(PocetakIzraza!=null) PocetakIzraza.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
        if(Terms!=null) Terms.accept(visitor);
        if(Terms1!=null) Terms1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(PocetakIzraza!=null) PocetakIzraza.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
        if(Terms!=null) Terms.traverseTopDown(visitor);
        if(Terms1!=null) Terms1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(PocetakIzraza!=null) PocetakIzraza.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        if(Terms!=null) Terms.traverseBottomUp(visitor);
        if(Terms1!=null) Terms1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactExpr(\n");

        if(PocetakIzraza!=null)
            buffer.append(PocetakIzraza.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Terms!=null)
            buffer.append(Terms.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Terms1!=null)
            buffer.append(Terms1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactExpr]");
        return buffer.toString();
    }
}
