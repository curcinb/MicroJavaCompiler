// generated with ast extension for cup
// version 0.8
// 7/0/2021 20:21:15


package rs.ac.bg.etf.pp1.ast;

public class ModifikacijaHash extends Terms {

    private Terms Terms;
    private HashMod HashMod;
    private Terms Terms1;

    public ModifikacijaHash (Terms Terms, HashMod HashMod, Terms Terms1) {
        this.Terms=Terms;
        if(Terms!=null) Terms.setParent(this);
        this.HashMod=HashMod;
        if(HashMod!=null) HashMod.setParent(this);
        this.Terms1=Terms1;
        if(Terms1!=null) Terms1.setParent(this);
    }

    public Terms getTerms() {
        return Terms;
    }

    public void setTerms(Terms Terms) {
        this.Terms=Terms;
    }

    public HashMod getHashMod() {
        return HashMod;
    }

    public void setHashMod(HashMod HashMod) {
        this.HashMod=HashMod;
    }

    public Terms getTerms1() {
        return Terms1;
    }

    public void setTerms1(Terms Terms1) {
        this.Terms1=Terms1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Terms!=null) Terms.accept(visitor);
        if(HashMod!=null) HashMod.accept(visitor);
        if(Terms1!=null) Terms1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Terms!=null) Terms.traverseTopDown(visitor);
        if(HashMod!=null) HashMod.traverseTopDown(visitor);
        if(Terms1!=null) Terms1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Terms!=null) Terms.traverseBottomUp(visitor);
        if(HashMod!=null) HashMod.traverseBottomUp(visitor);
        if(Terms1!=null) Terms1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ModifikacijaHash(\n");

        if(Terms!=null)
            buffer.append(Terms.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(HashMod!=null)
            buffer.append(HashMod.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Terms1!=null)
            buffer.append(Terms1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ModifikacijaHash]");
        return buffer.toString();
    }
}
