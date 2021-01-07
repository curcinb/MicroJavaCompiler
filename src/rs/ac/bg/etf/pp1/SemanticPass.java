package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class SemanticPass extends VisitorAdaptor {

	int varDeclCount = 0;
	Obj currentMethod = null;
	boolean errorDetected = false;
	int nVars;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public SemanticPass() {
		Scope universe = Tab.currentScope;

		universe.addToLocals(new Obj(Obj.Type, "bool", boolTip));
	}

	// OK
	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	// OK
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPr(), Tab.noType);
		Tab.openScope();
	}

	Struct currentType = null;
	String tipTMP = "";
	Struct boolTip = new Struct(Struct.Bool);

	// OK
	public void visit(ConstDeclaration constDeclaration) {
		if (Tab.currentScope().findSymbol(constDeclaration.getConstName()) == null) {
			int val = 0;
			Value tmp = constDeclaration.getValue();

			if (tmp instanceof IntType) {
				tipTMP = "int";
				val = ((IntType) constDeclaration.getValue()).getBrojT();
				constDeclaration.getValue().struct = Tab.intType;
			} else if (tmp instanceof CharType) {
				tipTMP = "char";
				val = ((CharType) constDeclaration.getValue()).getCharT();
				constDeclaration.getValue().struct = Tab.charType;
			} else if (tmp instanceof BoolType) {
				tipTMP = "bool";
				String val2 = ((BoolType) constDeclaration.getValue()).getBoolT();
				constDeclaration.getValue().struct = boolTip;
				if (val2.equals("true")) {
					val = 1;
				} else if (val2.equals("false")) {
					val = 0;
				}
			} else {
				report_error("Greska! Konstanti: " + constDeclaration.getConstName() + " prosledjen pogresan tip - ",
						constDeclaration);
			}
			Tab.insert(Obj.Con, constDeclaration.getConstName(), constDeclaration.getNewType().struct).setAdr(val);
			report_info("Deklarisana je konstanta: '" + constDeclaration.getConstName() + "' tipa " + tipTMP + "  ",
					constDeclaration);
		} else {
			report_error("Greska! Konstanta: '" + constDeclaration.getConstName() + "' je vec deklarisana - ",
					constDeclaration);
		}
	}

	// ok
	public void visit(ConstListSingle constListSingle) {
		if (Tab.currentScope().findSymbol(constListSingle.getName()) == null) {
			if (constListSingle.getValue().struct == currentType) {
				int val = 0;
				if (currentType == Tab.intType) {
					val = ((IntType) constListSingle.getValue()).getBrojT();
				}
				if (currentType == Tab.charType) {
					val = ((CharType) constListSingle.getValue()).getCharT();
				}
				if (currentType == boolTip) {
					String val2 = ((BoolType) constListSingle.getValue()).getBoolT();
					if (val2.equals("true")) {
						val = 1;
					} else if (val2.equals("false")) {
						val = 0;
					} else {
						report_error("Greska: Pogresan tip za boolean vrednost -", constListSingle);
					}
				}
				Tab.insert(Obj.Con, constListSingle.getName(), currentType).setAdr(val);
				report_info("Deklarisana je konstanta: '" + constListSingle.getName() + "' -", constListSingle);
			} else {
				report_error("Greska! Konstanti: " + constListSingle.getName() + " prosledjen pogresan tip - ",
						constListSingle);
			}
		} else {
			report_error("Greska! Konstanta: '" + constListSingle.getName() + "' je vec deklarisana - ",
					constListSingle);
		}
	}

	String varName = "";

	// ok
	public void visit(VarDefinition varDefinition) {
		Obj tmp = Tab.currentScope().findSymbol(varDefinition.getVarName());

		if (tmp == null) {
			report_info("Deklarisana je promenljiva: " + varDefinition.getVarName(), varDefinition);
			Tab.insert(Obj.Var, varDefinition.getVarName(), currentType);
			varDeclCount++;
		} else {
			report_error("Greska! Promenljiva: " + varDefinition.getVarName() + "  vec deklarisana", null);
		}
	}

	// ok
	public void visit(VarDefArr varDefArr) {
		Obj tmp = Tab.currentScope().findSymbol(varDefArr.getVarName());
		if (tmp == null) {
			report_info("Deklarisan je niz: '" + varDefArr.getVarName() + "'", varDefArr);
			Tab.insert(Obj.Var, varDefArr.getVarName(), new Struct(Struct.Array, currentType));
			varDeclCount++;
		} else {
			report_error("Greska: Niz: " + varDefArr.getVarName() + " je vec deklarisana", null);
		}
	}

	// ok
	public void visit(MethodDeclaration methodDeclaration) {
		if (currentMethod != null) {
			Tab.chainLocalSymbols(currentMethod);
			Tab.closeScope();
			report_info("Zavrsava se obrada funkcije: '" + currentMethod.getName() + "' -", methodDeclaration);
			currentMethod = null;
		}
	}

	// ok
	public void visit(MethodName methodName) {
		Obj tmp = Tab.currentScope().findSymbol(methodName.getMethName());

		if (tmp == null) {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethName(), methodName.getReturnType().struct);
			methodName.obj = currentMethod;
			currentMethod.setLevel(0);
			Tab.openScope();
			report_info("Obradjuje se funkcija: " + methodName.getMethName(), methodName);
		} else {
			report_error("Greska! Metoda: " + methodName.getMethName() + "  vec postoji!", null);
		}
	}

	public void visit(ReturnTypeVoid returnTypeVoid) {
		returnTypeVoid.struct = Tab.noType;
	}

	// ok
	public void visit(PraviTip praviTip) {
		Obj tip = Tab.find(praviTip.getTypeName());

		if (tip == Tab.noObj && !praviTip.getTypeName().matches("bool")) {
			report_error("Nije pronadjen tip: " + praviTip.getTypeName() + "u tabeli simbola!", null);
			praviTip.struct = Tab.noType;
		} else if (tip.getKind() == Obj.Type) {
			praviTip.struct = tip.getType();
		} else {
			report_error("Greska! Ime: " + praviTip.getTypeName() + " ne predstavlja tip!", praviTip);
			praviTip.struct = Tab.noType;
		}

		currentType = praviTip.struct;
	}

	// ok
	public void visit(ReadStatement readStatement) {
		Struct tmp = readStatement.getDesignator().obj.getType();

		if (tmp != Tab.charType && tmp != Tab.intType && tmp != boolTip) {
			report_error("Greska! Promenljiva: '" + readStatement.getDesignator().obj.getName() + "' na liniji: "
					+ readStatement.getLine() + " je pogresnog tipa!", readStatement);
		}

		int tmp2 = readStatement.getDesignator().obj.getKind();

		if (tmp2 != Obj.Fld && tmp2 != Obj.Var && tmp2 != Obj.Elem) {
			report_error("Greska! Promenljiva: '" + readStatement.getDesignator().obj.getName() + "' na liniji: "
					+ readStatement.getLine() + " nije ni polje, ni promenljiva, ni element!", null);
		}
	}

	// ok
	public void visit(PrintStatement printStatement) {
		Struct tmp = printStatement.getExpr().struct;

		if (tmp != Tab.intType && tmp != Tab.charType && tmp != boolTip) {
			report_error("Greska! Na liniji: " + printStatement.getLine() + ": PRINT prima samo char, int ili boool!",
					null);
		}
	}

	int printVal;

	// ok
	public void visit(PrintOptional printOptional) {
		printVal = printOptional.getVal();
	}

	public void visit(DesignatorAssignment designatorAssignment) {
		Obj obj = designatorAssignment.getDesignator().obj;
		int tmp = obj.getKind();
		Struct designatorType = obj.getType();

		if (tmp != Obj.Var && tmp != Obj.Elem && tmp != Obj.Fld) {
			report_error("Greska! Designator: '" + obj.getName() + "' nije ni promenljiva, ni element ni polje!",
					designatorAssignment);
		}

		// ok
		if (!designatorAssignment.getExpr().struct.assignableTo(designatorType)) {
			report_error("Greska! Tipovi nekompatibilni -", designatorAssignment);
		} else {
			report_info("Uspesno dodeljivanje vrednosti promenljivoj: '" + obj.getName() + "' -", designatorAssignment);
		}
	}

	// OK
	public void visit(DesignatorIncrement designatorIncrement) {
		int tmp = designatorIncrement.getDesignator().obj.getKind();

		if (tmp != Obj.Fld && tmp != Obj.Var && tmp != Obj.Elem) {
			report_error("Greska! Designator: '" + designatorIncrement.getDesignator().obj.getName() + "' ",
					designatorIncrement);
		}

		Struct tmp2 = designatorIncrement.getDesignator().obj.getType();

		if (tmp2 == Tab.intType) {
			report_info("Uspesno odraden inkrement promenljivoj: '" + designatorIncrement.getDesignator().obj.getName()
					+ "' -", designatorIncrement);
		} else {
			report_error(
					"Greska! Designator: '" + designatorIncrement.getDesignator().obj.getName() + "' nije tipa int -",
					designatorIncrement);
		}
	}

	// OK
	public void visit(DesignatorDecrement designatorDecrement) {
		int tmp = designatorDecrement.getDesignator().obj.getKind();

		if (tmp != Obj.Fld && tmp != Obj.Var && tmp != Obj.Elem) {
			report_error("Greska! Designator: '" + designatorDecrement.getDesignator().obj.getName()
					+ "' nije ni promenljiva, ni element ni polje!", designatorDecrement);
		}

		Struct tmp2 = designatorDecrement.getDesignator().obj.getType();

		if (tmp2 == Tab.intType) {
			report_info("Uspesno odraden dekrement promenljivoj: '" + designatorDecrement.getDesignator().obj.getName()
					+ "' -", designatorDecrement);
		} else {
			report_error(
					"Greska! Designator: '" + designatorDecrement.getDesignator().obj.getName() + "' nije tipa int -",
					designatorDecrement);
		}
	}

	// ok
	public void visit(DesignatorFunction designatorFunction) {
		Obj func = designatorFunction.getDesignator().obj;

		if (Obj.Meth == func.getKind()) {
			report_info("Poziv funkcije: " + func.getName() + " na liniji: " + designatorFunction.getLine(), null);
			designatorFunction.struct = func.getType();
		} else {
			report_error("Greska! Na liniji: " + designatorFunction.getLine() + " : ime " + func.getName()
					+ " nije funkcija!", null);
			designatorFunction.struct = Tab.noType;
		}
	}

	public void visit(DesignatorArr designatorArr) {
		Obj obj = designatorArr.getDesignatorNiz().getDesignator().obj;
		designatorArr.obj = new Obj(Obj.Elem, obj.getName(), obj.getType().getElemType());

		if (designatorArr.getExpr().struct != Tab.intType) {
			report_error("Greska: Promenljivoj: '" + obj.getName() + "' indeks mora da bude tipa int -", designatorArr);
			designatorArr.obj = Tab.noObj;
		}

		if (obj != Tab.noObj) {
			report_info("Pristup elementu niza: '" + obj.getName() + "' -", designatorArr);
		} else {
			report_error("Promenljiva: '" + obj.getName() + "' nije deklarisana -", designatorArr);
			designatorArr.obj = Tab.noObj;
		}
	}

	public void visit(DesignatorSimple designatorSimple) {
		Obj obj = Tab.find(designatorSimple.getName());
		designatorSimple.obj = obj;

		if (obj != Tab.noObj) {
			DumpSymbolTableVisitor dumpSym = new DumpSymbolTableVisitor();
			dumpSym.visitObjNode(obj);
			if (obj.getKind() == Obj.Con) {
				report_info("Pristup konstanti:'" + dumpSym.getOutput() + "' -", designatorSimple);
			} else if (obj.getKind() == Obj.Var) {
				if (obj.getLevel() == 0) {
					report_info("Pristup globalnoj promenljivoj:'" + dumpSym.getOutput() + "' -", designatorSimple);
				} else {
					report_info("Pristup lokalnoj promenljivoj:'" + dumpSym.getOutput() + "' -", designatorSimple);
				}
			}
		} else {
			report_error("Promenljiva: '" + designatorSimple.getName() + "' nije deklarisana -", designatorSimple);
		}
	}

	public void visit(MultipleTerms multipleTerms) {
		Struct tmp = multipleTerms.getTerms().struct;
		Struct tmp2 = multipleTerms.getTerm().struct;

		if (tmp.compatibleWith(tmp2) || tmp == Tab.intType && tmp2 == Tab.intType) {
			multipleTerms.struct = multipleTerms.getTerms().struct;
		} else {
			report_error("Greska! Tipovi nekompatibilni, na liniji: " + multipleTerms.getLine(), null);
			multipleTerms.struct = Tab.noType;
		}
	}

	public void visit(SingleTerm singleTerm) {
		singleTerm.struct = singleTerm.getTerm().struct;
	}

	public void visit(MultiplTerms multiplTerms) {
		Struct tmp = multiplTerms.getTerm().struct;
		Struct tmp2 = multiplTerms.getTerm().struct;

		if (tmp.compatibleWith(tmp2) || tmp == Tab.intType && tmp2 == Tab.intType) {
			multiplTerms.struct = multiplTerms.getTerm().struct;
		} else {
			report_error("Greska! Tipovi nekompatibilni, na liniji: " + multiplTerms.getLine(), null);
			multiplTerms.struct = Tab.noType;
		}
	}

	public void visit(SinglTerm singlTerm) {
		singlTerm.struct = singlTerm.getFactor().struct;
	}

	public void visit(NumFactor numFactor) {
		numFactor.struct = Tab.intType;
	}

	public void visit(CharFactor charFactor) {
		charFactor.struct = Tab.charType;
	}

	public void visit(ExprFactor exprFactor) {
		exprFactor.struct = exprFactor.getExpr().struct;
	}

	public void visit(BoolFactor boolFactor) {
		boolFactor.struct = boolTip;
	}

	public void visit(NewFactorExpr newFactorExpr) {
		if (newFactorExpr.getExpr().struct == Tab.intType) {
			newFactorExpr.struct = new Struct(Struct.Array, currentType);
		} else {
			report_error("Greska! Velicina niza mora da bude celobrojna vrednost! Linija: " + newFactorExpr.getLine(),
					newFactorExpr);
			newFactorExpr.struct = Tab.noType;
		}
	}

	public void visit(DesignatorFactor designatorFactor) {
		designatorFactor.struct = designatorFactor.getDesignator().obj.getType();
	}

	public void visit(DesignatorFactorFuncCall designatorFactorFuncCall) {
		if (designatorFactorFuncCall.getDesignator().obj.getKind() == Obj.Meth) {
			report_info("Poziv funkcije: " + designatorFactorFuncCall.getDesignator().obj.getName() + " na liniji: "
					+ designatorFactorFuncCall.getLine(), null);
		} else {
			report_error("Greska! Pronadena promenljiva: " + designatorFactorFuncCall.getDesignator().obj.getName()
					+ "na liniji: " + designatorFactorFuncCall.getLine() + " nije metoda!", null);
			designatorFactorFuncCall.struct = Tab.noType;
		}
	}

	public void visit(ModifikacijaAt modifikacijaAt) {
		modifikacijaAt.struct = Tab.intType; // Sta je rezultat primene operatora
		Struct type = modifikacijaAt.getDesignator().obj.getType();

		if (!(type.getKind() == Struct.Array && type.getElemType() == Tab.intType)) {
			report_error("Greska! Nekompatibilan levi operand za operaciju @ : ", modifikacijaAt);
			modifikacijaAt.struct = Tab.noType;
		}
		if (modifikacijaAt.getFactor().struct != Tab.intType) {
			report_error("Greska! Nekompatibilan desni operand za operaciju @ : ", modifikacijaAt);
			modifikacijaAt.struct = Tab.noType;
		}

	}

	public void visit(ModifikacijaDolar modifikacijaD) {
		modifikacijaD.struct = Tab.charType; // Sta je rezultat primene operatora
		Struct type = modifikacijaD.getDesignator().obj.getType();

		if (!(type.getKind() == Struct.Array && type.getElemType() == boolTip)) {
			report_error("Greska! Nekompatibilan operand za operaciju $ : ", modifikacijaD);
			modifikacijaD.struct = Tab.noType;
		}
	}

	public void visit(ModifikacijaKapica modifikacijaK) {
		modifikacijaK.struct = new Struct(Struct.Array, Tab.charType);

		if (modifikacijaK.getFactor().struct != Tab.intType) {
			report_error("Greska! Nekompatibilan operand za operaciju ^ : ", modifikacijaK);
			modifikacijaK.struct = Tab.noType;
		}
	}

	public void visit(ModifikacijaBrojevi modifikacijaB) {
		modifikacijaB.struct = Tab.intType;

		if (modifikacijaB.getExpr().struct != Tab.intType) {
			report_error("Greska! Nekompatibilan 1. operand za modifikaciju brojevi : ", modifikacijaB);
			modifikacijaB.struct = Tab.noType;
		}

		if (modifikacijaB.getExpr1().struct != Tab.intType) {
			report_error("Greska! Nekompatibilan 2. operand za modifikaciju brojevi : ", modifikacijaB);
			modifikacijaB.struct = Tab.noType;
		}

		if (modifikacijaB.getExpr2().struct != Tab.intType) {
			report_error("Greska! Nekompatibilan 3. operand za modifikaciju brojevi : ", modifikacijaB);
			modifikacijaB.struct = Tab.noType;
		}
	}

	public void visit(IntType intType) {
		intType.struct = Tab.intType;
	}

	public void visit(BoolType boolType) {
		boolType.struct = boolTip;
	}

	public void visit(CharType charType) {
		charType.struct = Tab.charType;
	}

	public void visit(SingleTermMinus singleTermMinus) {
		if (singleTermMinus.getTerm().struct != Tab.intType) {
			singleTermMinus.struct = Tab.noType;
			report_error("Greska! Term nije int!", singleTermMinus);
		} else {
			singleTermMinus.struct = singleTermMinus.getTerm().struct;
		}
	}

	public void visit(ReturnStatement1 retStmn) {
		if (currentMethod.getType() != Tab.noType)
			report_error("Iza returna mora da bude povratna vrednost! ", retStmn);
	}

	public void visit(ReturnStatement2 retStmn) {
		if (!retStmn.getExpr().struct.assignableTo(currentMethod.getType()))
			report_error("Ne poklapaju se povratne vrednosti izraza i metode! ", retStmn);
	}

	public void visit(Expression expression) {
		expression.struct = expression.getTerms().struct;
	}

	public void visit(ModifikacijaHash exprHash) {
		Struct tmp1 = exprHash.getTerms().struct;
		Struct tmp2 = exprHash.getTerms1().struct;

		if (!(tmp1 == Tab.charType) && !(exprHash.getTerms() instanceof ModifikacijaHash)) {
			report_error("Prvi expr nije char!", exprHash);
		}
		if (!(tmp2 == Tab.intType)) {
			report_error("Drugi expr nije int!", exprHash);
		}
	}

	public void visit(CondFactExpr condFactExpr) {
		// Zapamtio sam kog su tipa izrazi:
		Struct tipCondFact1 = condFactExpr.getExpr().struct;
		Struct tipCondFact2 = condFactExpr.getExpr1().struct;

		if (!tipCondFact1.compatibleWith(tipCondFact2)) {
			report_error("Nekompatibilni tipovi kod uslova sa relacionim operatorom!", condFactExpr);
			condFactExpr.struct = Tab.noType;
		}
		else {
			condFactExpr.struct = tipCondFact1;
		}
		
	}

	public void visit(CondSingle condSingle) {
		if (condSingle.getTerms().struct != boolTip) {
			report_error("Uslov mora da bude tipa bool!", condSingle);
		}

	}

	public void visit(CondDouble condDouble) {
		// Zapamtio sam kog su tipa izrazi:
		Struct tipCondFact1 = condDouble.getTerms().struct;
		Struct tipCondFact2 = condDouble.getTerms1().struct;

		if (!tipCondFact1.compatibleWith(tipCondFact2)) {
			report_error("Nekompatibilni tipovi kod uslova sa relacionim operatorom!", condDouble);
		} else {
			// Provera da li je element niza ili polje klase
			if (tipCondFact1.getKind() == Struct.Array
					|| tipCondFact1.getKind() == Struct.Class) {
				// Ako jesu, onda moze samo != i == da se koristi!
				if (!(condDouble.getRelOp() instanceof JednakoOp)
						&& !(condDouble.getRelOp() instanceof NejednakoOp)) {
					report_error(
							"Izmedju elemenata niza ili polja klasa moze da se vrsi samo poredjenje po (ne)jednakosti!",
							condDouble);
				}
			}
		}
	}

	// public void visit(VarDeclarError vE) {} //Dodati i za ostale greske!

	public boolean passed() {
		return !errorDetected;
	}
}
