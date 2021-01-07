// generated with ast extension for cup
// version 0.8
// 7/0/2021 20:21:15


package rs.ac.bg.etf.pp1.ast;

public class BoolType extends Value {

    private String boolT;

    public BoolType (String boolT) {
        this.boolT=boolT;
    }

    public String getBoolT() {
        return boolT;
    }

    public void setBoolT(String boolT) {
        this.boolT=boolT;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BoolType(\n");

        buffer.append(" "+tab+boolT);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolType]");
        return buffer.toString();
    }
}
