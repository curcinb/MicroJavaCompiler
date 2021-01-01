// generated with ast extension for cup
// version 0.8
// 29/11/2020 17:53:48


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(ReturnType ReturnType);
    public void visit(FormParameters FormParameters);
    public void visit(FormalParamDecl FormalParamDecl);
    public void visit(MethTypeName MethTypeName);
    public void visit(StatementList StatementList);
    public void visit(MulOpLeft MulOpLeft);
    public void visit(Factor Factor);
    public void visit(VarList VarList);
    public void visit(ConstList ConstList);
    public void visit(MethDeclList MethDeclList);
    public void visit(DeclList DeclList);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(MethDecl MethDecl);
    public void visit(Terms Terms);
    public void visit(MulOp MulOp);
    public void visit(AddOpRight AddOpRight);
    public void visit(Value Value);
    public void visit(RelOp RelOp);
    public void visit(AssignOp AssignOp);
    public void visit(NewType NewType);
    public void visit(VarDeclList VarDeclList);
    public void visit(Expr Expr);
    public void visit(OptionalPrint OptionalPrint);
    public void visit(AddOp AddOp);
    public void visit(VarDef VarDef);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(Statement Statement);
    public void visit(VarDecl VarDecl);
    public void visit(AddOpLeft AddOpLeft);
    public void visit(MulOpRight MulOpRight);
    public void visit(ConstDecl ConstDecl);
    public void visit(CondFact CondFact);
    public void visit(FormPars FormPars);
    public void visit(BoolType BoolType);
    public void visit(CharType CharType);
    public void visit(IntType IntType);
    public void visit(ManjeJednakoOp ManjeJednakoOp);
    public void visit(ManjeOp ManjeOp);
    public void visit(VeceJednakoOp VeceJednakoOp);
    public void visit(VeceOp VeceOp);
    public void visit(NejednakoOp NejednakoOp);
    public void visit(JednakoOp JednakoOp);
    public void visit(ModAsgOp ModAsgOp);
    public void visit(DivAsgOp DivAsgOp);
    public void visit(MulAsgOp MulAsgOp);
    public void visit(MulOpMod MulOpMod);
    public void visit(MulOpDiv MulOpDiv);
    public void visit(MulOpMul MulOpMul);
    public void visit(MulRightOp MulRightOp);
    public void visit(MulLeftOp MulLeftOp);
    public void visit(SubEq SubEq);
    public void visit(AddEq AddEq);
    public void visit(AddMinus AddMinus);
    public void visit(AddPlus AddPlus);
    public void visit(AddRightOp AddRightOp);
    public void visit(AddLeftOp AddLeftOp);
    public void visit(ModifikacijaBrojevi ModifikacijaBrojevi);
    public void visit(ModifikacijaKapica ModifikacijaKapica);
    public void visit(ModifikacijaDolar ModifikacijaDolar);
    public void visit(ModifikacijaAt ModifikacijaAt);
    public void visit(DesignatorFactorFuncCall DesignatorFactorFuncCall);
    public void visit(DesignatorFactor DesignatorFactor);
    public void visit(NewFactorExpr NewFactorExpr);
    public void visit(NewFactorNoExpr NewFactorNoExpr);
    public void visit(BoolFactor BoolFactor);
    public void visit(ExprFactor ExprFactor);
    public void visit(CharFactor CharFactor);
    public void visit(NumFactor NumFactor);
    public void visit(SinglTerm SinglTerm);
    public void visit(MultiplTerms MultiplTerms);
    public void visit(HashMod HashMod);
    public void visit(ModifikacijaHash ModifikacijaHash);
    public void visit(SingleTermMinus SingleTermMinus);
    public void visit(SingleTerm SingleTerm);
    public void visit(MultipleTerms MultipleTerms);
    public void visit(CondFactDouble CondFactDouble);
    public void visit(CondFactSingle CondFactSingle);
    public void visit(PocetakIzraza PocetakIzraza);
    public void visit(ErrorExpression ErrorExpression);
    public void visit(Expression Expression);
    public void visit(CondFactExpr CondFactExpr);
    public void visit(MulRight MulRight);
    public void visit(AddRight AddRight);
    public void visit(Asg Asg);
    public void visit(DesignatorAsg DesignatorAsg);
    public void visit(DesignatorNiz DesignatorNiz);
    public void visit(DesignatorArr DesignatorArr);
    public void visit(DesignatorSimple DesignatorSimple);
    public void visit(DesignatorFunction DesignatorFunction);
    public void visit(DesignatorDecrement DesignatorDecrement);
    public void visit(DesignatorIncrement DesignatorIncrement);
    public void visit(AssignErr AssignErr);
    public void visit(DesignatorAssignment DesignatorAssignment);
    public void visit(NoOptionalPrint NoOptionalPrint);
    public void visit(PrintOptional PrintOptional);
    public void visit(ReturnStatement2 ReturnStatement2);
    public void visit(ReturnStatement1 ReturnStatement1);
    public void visit(PrintStatement PrintStatement);
    public void visit(ReadStatement ReadStatement);
    public void visit(StatementDesignator StatementDesignator);
    public void visit(NoStatement NoStatement);
    public void visit(Statements Statements);
    public void visit(NoLocalVarDeclList NoLocalVarDeclList);
    public void visit(LocalVarDeclList LocalVarDeclList);
    public void visit(FormalParamDeclError FormalParamDeclError);
    public void visit(FormParamArr FormParamArr);
    public void visit(FormParam FormParam);
    public void visit(SingleFormalParamDecl SingleFormalParamDecl);
    public void visit(FormParamDecl FormParamDecl);
    public void visit(NoFormParameters NoFormParameters);
    public void visit(FormParametres FormParametres);
    public void visit(PraviTip PraviTip);
    public void visit(ReturnTypeVoid ReturnTypeVoid);
    public void visit(ReturnTypeNormal ReturnTypeNormal);
    public void visit(MethodName MethodName);
    public void visit(MethodDeclaration MethodDeclaration);
    public void visit(VarDefErr VarDefErr);
    public void visit(VarDefArr VarDefArr);
    public void visit(VarDefinition VarDefinition);
    public void visit(NoVarList NoVarList);
    public void visit(VarListSingle VarListSingle);
    public void visit(VarListMultiple VarListMultiple);
    public void visit(VarDeclar VarDeclar);
    public void visit(NoConstList NoConstList);
    public void visit(ConstListSingle ConstListSingle);
    public void visit(ConstDeclaration ConstDeclaration);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(NoDeclList NoDeclList);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(ConstDeclarations ConstDeclarations);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}