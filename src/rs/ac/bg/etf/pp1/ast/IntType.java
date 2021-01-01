// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class IntType extends Value {

    private Integer brojT;

    public IntType (Integer brojT) {
        this.brojT=brojT;
    }

    public Integer getBrojT() {
        return brojT;
    }

    public void setBrojT(Integer brojT) {
        this.brojT=brojT;
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
        buffer.append("IntType(\n");

        buffer.append(" "+tab+brojT);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IntType]");
        return buffer.toString();
    }
}
