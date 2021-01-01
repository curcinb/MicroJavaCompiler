// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclarations extends MethDeclList {

    private MethDeclList MethDeclList;
    private MethDecl MethDecl;

    public MethodDeclarations (MethDeclList MethDeclList, MethDecl MethDecl) {
        this.MethDeclList=MethDeclList;
        if(MethDeclList!=null) MethDeclList.setParent(this);
        this.MethDecl=MethDecl;
        if(MethDecl!=null) MethDecl.setParent(this);
    }

    public MethDeclList getMethDeclList() {
        return MethDeclList;
    }

    public void setMethDeclList(MethDeclList MethDeclList) {
        this.MethDeclList=MethDeclList;
    }

    public MethDecl getMethDecl() {
        return MethDecl;
    }

    public void setMethDecl(MethDecl MethDecl) {
        this.MethDecl=MethDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethDeclList!=null) MethDeclList.accept(visitor);
        if(MethDecl!=null) MethDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethDeclList!=null) MethDeclList.traverseTopDown(visitor);
        if(MethDecl!=null) MethDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethDeclList!=null) MethDeclList.traverseBottomUp(visitor);
        if(MethDecl!=null) MethDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclarations(\n");

        if(MethDeclList!=null)
            buffer.append(MethDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethDecl!=null)
            buffer.append(MethDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclarations]");
        return buffer.toString();
    }
}
