// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class Expression extends Expr {

    private PocetakIzraza PocetakIzraza;
    private Terms Terms;

    public Expression (PocetakIzraza PocetakIzraza, Terms Terms) {
        this.PocetakIzraza=PocetakIzraza;
        if(PocetakIzraza!=null) PocetakIzraza.setParent(this);
        this.Terms=Terms;
        if(Terms!=null) Terms.setParent(this);
    }

    public PocetakIzraza getPocetakIzraza() {
        return PocetakIzraza;
    }

    public void setPocetakIzraza(PocetakIzraza PocetakIzraza) {
        this.PocetakIzraza=PocetakIzraza;
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
        if(PocetakIzraza!=null) PocetakIzraza.accept(visitor);
        if(Terms!=null) Terms.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(PocetakIzraza!=null) PocetakIzraza.traverseTopDown(visitor);
        if(Terms!=null) Terms.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(PocetakIzraza!=null) PocetakIzraza.traverseBottomUp(visitor);
        if(Terms!=null) Terms.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expression(\n");

        if(PocetakIzraza!=null)
            buffer.append(PocetakIzraza.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
