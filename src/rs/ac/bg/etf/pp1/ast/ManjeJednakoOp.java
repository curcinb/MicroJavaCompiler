// generated with ast extension for cup
// version 0.8
// 7/0/2021 20:6:41


package rs.ac.bg.etf.pp1.ast;

public class ManjeJednakoOp extends RelOp {

    public ManjeJednakoOp () {
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
        buffer.append("ManjeJednakoOp(\n");

        buffer.append(tab);
        buffer.append(") [ManjeJednakoOp]");
        return buffer.toString();
    }
}
