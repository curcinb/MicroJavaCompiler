// generated with ast extension for cup
// version 0.8
// 9/1/2021 23:8:18


package rs.ac.bg.etf.pp1.ast;

public class JednakoOp extends RelOp {

    public JednakoOp () {
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
        buffer.append("JednakoOp(\n");

        buffer.append(tab);
        buffer.append(") [JednakoOp]");
        return buffer.toString();
    }
}
