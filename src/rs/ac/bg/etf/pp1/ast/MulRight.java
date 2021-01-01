// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class MulRight extends AssignOp {

    private MulOpRight MulOpRight;

    public MulRight (MulOpRight MulOpRight) {
        this.MulOpRight=MulOpRight;
        if(MulOpRight!=null) MulOpRight.setParent(this);
    }

    public MulOpRight getMulOpRight() {
        return MulOpRight;
    }

    public void setMulOpRight(MulOpRight MulOpRight) {
        this.MulOpRight=MulOpRight;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MulOpRight!=null) MulOpRight.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MulOpRight!=null) MulOpRight.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MulOpRight!=null) MulOpRight.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MulRight(\n");

        if(MulOpRight!=null)
            buffer.append(MulOpRight.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MulRight]");
        return buffer.toString();
    }
}
