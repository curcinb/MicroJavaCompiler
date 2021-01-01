// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class MulLeftOp extends MulOp {

    private MulOpLeft MulOpLeft;

    public MulLeftOp (MulOpLeft MulOpLeft) {
        this.MulOpLeft=MulOpLeft;
        if(MulOpLeft!=null) MulOpLeft.setParent(this);
    }

    public MulOpLeft getMulOpLeft() {
        return MulOpLeft;
    }

    public void setMulOpLeft(MulOpLeft MulOpLeft) {
        this.MulOpLeft=MulOpLeft;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MulOpLeft!=null) MulOpLeft.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MulOpLeft!=null) MulOpLeft.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MulOpLeft!=null) MulOpLeft.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MulLeftOp(\n");

        if(MulOpLeft!=null)
            buffer.append(MulOpLeft.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MulLeftOp]");
        return buffer.toString();
    }
}
