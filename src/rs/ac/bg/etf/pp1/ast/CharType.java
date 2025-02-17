// generated with ast extension for cup
// version 0.8
// 9/1/2021 23:8:18


package rs.ac.bg.etf.pp1.ast;

public class CharType extends Value {

    private Character charT;

    public CharType (Character charT) {
        this.charT=charT;
    }

    public Character getCharT() {
        return charT;
    }

    public void setCharT(Character charT) {
        this.charT=charT;
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
        buffer.append("CharType(\n");

        buffer.append(" "+tab+charT);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharType]");
        return buffer.toString();
    }
}
