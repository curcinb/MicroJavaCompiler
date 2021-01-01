// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class ReturnTypeNormal extends ReturnType {

    private NewType NewType;

    public ReturnTypeNormal (NewType NewType) {
        this.NewType=NewType;
        if(NewType!=null) NewType.setParent(this);
    }

    public NewType getNewType() {
        return NewType;
    }

    public void setNewType(NewType NewType) {
        this.NewType=NewType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NewType!=null) NewType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NewType!=null) NewType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NewType!=null) NewType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnTypeNormal(\n");

        if(NewType!=null)
            buffer.append(NewType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnTypeNormal]");
        return buffer.toString();
    }
}
