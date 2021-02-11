// generated with ast extension for cup
// version 0.8
// 9/1/2021 23:8:18


package rs.ac.bg.etf.pp1.ast;

public class MaxNiza extends Factor {

    private DesignatorNiz DesignatorNiz;

    public MaxNiza (DesignatorNiz DesignatorNiz) {
        this.DesignatorNiz=DesignatorNiz;
        if(DesignatorNiz!=null) DesignatorNiz.setParent(this);
    }

    public DesignatorNiz getDesignatorNiz() {
        return DesignatorNiz;
    }

    public void setDesignatorNiz(DesignatorNiz DesignatorNiz) {
        this.DesignatorNiz=DesignatorNiz;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorNiz!=null) DesignatorNiz.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorNiz!=null) DesignatorNiz.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorNiz!=null) DesignatorNiz.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MaxNiza(\n");

        if(DesignatorNiz!=null)
            buffer.append(DesignatorNiz.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MaxNiza]");
        return buffer.toString();
    }
}
