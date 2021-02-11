// generated with ast extension for cup
// version 0.8
// 9/1/2021 23:8:18


package rs.ac.bg.etf.pp1.ast;

public class DesignatorArr extends Designator {

    private DesignatorNiz DesignatorNiz;
    private Expr Expr;

    public DesignatorArr (DesignatorNiz DesignatorNiz, Expr Expr) {
        this.DesignatorNiz=DesignatorNiz;
        if(DesignatorNiz!=null) DesignatorNiz.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignatorNiz getDesignatorNiz() {
        return DesignatorNiz;
    }

    public void setDesignatorNiz(DesignatorNiz DesignatorNiz) {
        this.DesignatorNiz=DesignatorNiz;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorNiz!=null) DesignatorNiz.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorNiz!=null) DesignatorNiz.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorNiz!=null) DesignatorNiz.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorArr(\n");

        if(DesignatorNiz!=null)
            buffer.append(DesignatorNiz.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorArr]");
        return buffer.toString();
    }
}
