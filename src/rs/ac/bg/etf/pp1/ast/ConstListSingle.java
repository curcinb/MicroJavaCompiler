// generated with ast extension for cup
// version 0.8
// 9/1/2021 23:8:18


package rs.ac.bg.etf.pp1.ast;

public class ConstListSingle extends ConstList {

    private String name;
    private Value Value;

    public ConstListSingle (String name, Value Value) {
        this.name=name;
        this.Value=Value;
        if(Value!=null) Value.setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public Value getValue() {
        return Value;
    }

    public void setValue(Value Value) {
        this.Value=Value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Value!=null) Value.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Value!=null) Value.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Value!=null) Value.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstListSingle(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        if(Value!=null)
            buffer.append(Value.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstListSingle]");
        return buffer.toString();
    }
}
