// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class AddRight extends AssignOp {

    private AddOpRight AddOpRight;

    public AddRight (AddOpRight AddOpRight) {
        this.AddOpRight=AddOpRight;
        if(AddOpRight!=null) AddOpRight.setParent(this);
    }

    public AddOpRight getAddOpRight() {
        return AddOpRight;
    }

    public void setAddOpRight(AddOpRight AddOpRight) {
        this.AddOpRight=AddOpRight;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddOpRight!=null) AddOpRight.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddOpRight!=null) AddOpRight.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddOpRight!=null) AddOpRight.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddRight(\n");

        if(AddOpRight!=null)
            buffer.append(AddOpRight.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddRight]");
        return buffer.toString();
    }
}
