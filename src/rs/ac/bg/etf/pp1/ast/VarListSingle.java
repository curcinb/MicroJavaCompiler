// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class VarListSingle extends VarList {

    private VarDef VarDef;

    public VarListSingle (VarDef VarDef) {
        this.VarDef=VarDef;
        if(VarDef!=null) VarDef.setParent(this);
    }

    public VarDef getVarDef() {
        return VarDef;
    }

    public void setVarDef(VarDef VarDef) {
        this.VarDef=VarDef;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDef!=null) VarDef.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDef!=null) VarDef.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDef!=null) VarDef.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarListSingle(\n");

        if(VarDef!=null)
            buffer.append(VarDef.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarListSingle]");
        return buffer.toString();
    }
}
