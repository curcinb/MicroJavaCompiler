// generated with ast extension for cup
// version 0.8
// 7/0/2021 20:21:15


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclaration extends ConstDecl {

    private NewType NewType;
    private String constName;
    private Value Value;
    private ConstList ConstList;

    public ConstDeclaration (NewType NewType, String constName, Value Value, ConstList ConstList) {
        this.NewType=NewType;
        if(NewType!=null) NewType.setParent(this);
        this.constName=constName;
        this.Value=Value;
        if(Value!=null) Value.setParent(this);
        this.ConstList=ConstList;
        if(ConstList!=null) ConstList.setParent(this);
    }

    public NewType getNewType() {
        return NewType;
    }

    public void setNewType(NewType NewType) {
        this.NewType=NewType;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public Value getValue() {
        return Value;
    }

    public void setValue(Value Value) {
        this.Value=Value;
    }

    public ConstList getConstList() {
        return ConstList;
    }

    public void setConstList(ConstList ConstList) {
        this.ConstList=ConstList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NewType!=null) NewType.accept(visitor);
        if(Value!=null) Value.accept(visitor);
        if(ConstList!=null) ConstList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NewType!=null) NewType.traverseTopDown(visitor);
        if(Value!=null) Value.traverseTopDown(visitor);
        if(ConstList!=null) ConstList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NewType!=null) NewType.traverseBottomUp(visitor);
        if(Value!=null) Value.traverseBottomUp(visitor);
        if(ConstList!=null) ConstList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclaration(\n");

        if(NewType!=null)
            buffer.append(NewType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(Value!=null)
            buffer.append(Value.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstList!=null)
            buffer.append(ConstList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclaration]");
        return buffer.toString();
    }
}
