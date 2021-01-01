package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.Stack;

import rs.ac.bg.etf.pp1.CounterVisitor.FormalParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private Stack<KlasaZaStek> stekZaTekuci; // uvek ce biti na vrhu ovog drugog steka
	private Stack<Stack<KlasaZaStek>> stekStekova = new Stack<>();
	Obj poslednjiFaktor;

	public int getMainPc() {
		return mainPc;
	}

	public void visit(MethodDeclaration methodDeclaration) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MethodName methodName) {
		if ((methodName.getMethName()).equals("main")) {
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);

		VarCounter varCnt = new VarCounter();
		SyntaxNode methNode = methodName.getParent();

		methNode.traverseTopDown(varCnt);

		FormalParamCounter formParsCnt = new FormalParamCounter();
		methNode.traverseTopDown(formParsCnt);

		Code.put(Code.enter);
		Code.put(formParsCnt.getCount());
		Code.put(formParsCnt.getCount() + varCnt.getCount());
	}

	//Citanje:
	public void visit(ReadStatement readStatement) {
		Struct tip = readStatement.getDesignator().obj.getType();

		if (tip == Tab.charType) {
			Code.put(Code.bread);
		} else if (tip == Tab.intType) {
			Code.put(Code.read);
		}
		Code.store(readStatement.getDesignator().obj);
	}

	//Ispis:
	public void visit(PrintStatement printStatement) {
		if (printStatement.getOptionalPrint() == null) {
			if (printStatement.getExpr().struct == Tab.intType) {
				Code.loadConst(5);
				Code.put(Code.print);
			} else {
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
		} else {
			Code.loadConst(printVal);
			if (printStatement.getExpr().struct != Tab.charType) {
				Code.put(Code.print);
			} else {
				Code.put(Code.bprint);
			}
		}
	}

	// ok
	int printVal;
	public void visit(PrintOptional printOptional) {
		printVal = printOptional.getVal();
	}

	// ok
	public void visit(DesignatorAssignment designatorAssignment) {
		if (designatorAssignment.getAssignOp() instanceof AddRight) {
			AddRight addRight = (AddRight) designatorAssignment.getAssignOp();
			if (addRight.getAddOpRight() instanceof AddEq) {
				Code.put(Code.add);
			} else {
				Code.put(Code.sub);
			}
		} else if (designatorAssignment.getAssignOp() instanceof MulRight) {
			MulRight mulRight = (MulRight) designatorAssignment.getAssignOp();
			if (mulRight.getMulOpRight() instanceof MulAsgOp) {
				Code.put(Code.mul);
			} else if (mulRight.getMulOpRight() instanceof DivAsgOp) {
				Code.put(Code.div);
			} else {
				Code.put(Code.rem);
			}
		}
		Code.store(designatorAssignment.getDesignatorAsg().getDesignator().obj);
	}

	public void visit(DesignatorAsg designatorAsg) {
		DesignatorAssignment parent = (DesignatorAssignment) designatorAsg.getParent();
		if (!(parent.getAssignOp() instanceof Asg)) {
			Obj obj = designatorAsg.getDesignator().obj;
			if (obj.getKind() == Obj.Elem) {
				Code.put(Code.dup2);
			}
			Code.load(obj);
		}
	}

	// ok
	public void visit(DesignatorIncrement designatorIncrement) {
		Obj obj = designatorIncrement.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}

	// ok
	public void visit(DesignatorDecrement designatorDecrement) {
		Obj obj = designatorDecrement.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(obj);
	}

	public void visit(DesignatorFunction designatorFunction) {
		Obj functionObj = designatorFunction.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		// Stavio sam u bajtkod da skinem njenu povratnu vrednost sa steka, ako je ima
		if (functionObj.getType() != Tab.noType)
			Code.put(Code.pop);
	}

	public void visit(DesignatorNiz designatorNiz) {
		Code.load(designatorNiz.getDesignator().obj);
	}

	public void visit(SingleTermMinus singleTermMinus) {
		if (poslednjiFaktor != null) {
			Code.load(poslednjiFaktor);
			poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
		}
		Code.put(Code.neg);
	}

	public void visit(MultiplTerms multiplTerms) {
		if (multiplTerms.getMulOp() instanceof MulLeftOp) {
			if (poslednjiFaktor != null) {
				Code.load(poslednjiFaktor);
				poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
			}

			MulOpLeft ml = ((MulLeftOp) multiplTerms.getMulOp()).getMulOpLeft();

			if (ml instanceof MulOpMul) {
				Code.put(Code.mul);
			}
			if (ml instanceof MulOpDiv) {
				Code.put(Code.div);
			}
			if (ml instanceof MulOpMod) {
				Code.put(Code.rem);
			}
		}
	}

	public void visit(MultipleTerms multipleTerms) {
		if (multipleTerms.getAddOp() instanceof AddLeftOp) {
			if (poslednjiFaktor != null) {
				Code.load(poslednjiFaktor);
				poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
			}

			AddLeftOp aL = (AddLeftOp) multipleTerms.getAddOp();

			if (aL.getAddOpLeft() instanceof AddPlus) {
				Code.put(Code.add);
			} else {
				Code.put(Code.sub);
			}
		}
	}

	//Jedino kod njega poslednjiFaktor nije null!
	public void visit(DesignatorFactor designatorFactor) {
		poslednjiFaktor = designatorFactor.getDesignator().obj;
	}

	//Samo na exprstek bacim broj:
	public void visit(NumFactor numFactor) {
		Code.loadConst(numFactor.getN1());
		poslednjiFaktor = null;
	}

	//Samo na exprstek bacim char:
	public void visit(CharFactor charFactor) {
		Code.loadConst(charFactor.getC1());
		poslednjiFaktor = null;
	}

	//Samo na exprstek bacim boolean kao broj:
	public void visit(BoolFactor boolFactor) {
		String value = boolFactor.getB1();
		if (value.equals("true")) {
			Code.loadConst(1);
		} else {
			Code.loadConst(0);
		}
		poslednjiFaktor = null;
	}

	//Pravljenje niza:
	public void visit(NewFactorExpr newFactorExpr) {
		Code.put(Code.newarray);
		if (newFactorExpr.getNewType().struct == Tab.charType) {
			Code.put(0);
		} else {
			Code.put(1);
		}
		poslednjiFaktor = null;
	}

	//Samo setujem poslednjiFaktor:
	public void visit(ExprFactor exprFactor) {
		poslednjiFaktor = null;
	}

	//Poziv funkcije:
	public void visit(DesignatorFactorFuncCall designatorFactorFuncCall) {
		Obj functionObj = designatorFactorFuncCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		poslednjiFaktor = null;
	}

	//TODO: Dodati sta treba da radi @ modifikacija!
	//print(niz @ 2 * 3); - ispisuje 
	public void visit(ModifikacijaAt modifikacijaAt) {
		// Ovde dodajemo faktor kao drugi operand za operaciju @, pa zato moramo da ga
		// nabacimo na expression stek
		if (poslednjiFaktor != null) {
			Code.load(poslednjiFaktor);
			poslednjiFaktor = null;
		}

		//Simuliram ulazak u funkciju da bih mogao da pokupim parametre dodatne:
		Code.put(Code.enter);
			Code.put(1); // 1 parametar
			Code.put(2); // Zbir param + pomocna
	
			// Dobijanje prvog operanda za sabiranje
			//Prvo nabacujem adresu pa index
			Code.load(modifikacijaAt.getDesignator().obj); // Pri pravljenju niza, adresa njegova se cuva u promenljivoj sa leve strane
			Code.put(Code.load_n + 0);
			Code.put(Code.aload);
	
			// Dobijanje drugog operanda za sabiranje
			Code.load(modifikacijaAt.getDesignator().obj);
	
			Code.load(modifikacijaAt.getDesignator().obj);
			Code.put(Code.arraylength);
			Code.put(Code.load_n + 0);  //Oduzimam prvo onaj element od kraja niza 
			Code.put(Code.sub);
			Code.loadConst(1);			//Pa jos keca zbog indeksiranja
			Code.put(Code.sub);
			Code.put(Code.aload);
	
			Code.put(Code.add);			//radim sabiranje
			Code.put(Code.store_n + 1); //u prvu pomogcnu pamtim dobijenu vrednost
	
			Code.load(modifikacijaAt.getDesignator().obj); // Adresa
			Code.put(Code.load_n + 0); // Index
			Code.put(Code.load_n + 1); // Vrednost
			Code.put(Code.astore); // Za lokalnu promenljivu
	
			Code.put(Code.load_n + 1); // Vracam dobijenu sabranu vrednost
		Code.put(Code.exit);

		poslednjiFaktor = null; // PAZI DA I OVO TREBA DA SE POSTAVI!
	}

	//print($niz) - Ispisuje broj dat u nizu u binarnom obliku:
	public void visit(ModifikacijaDolar modifikacijaD) { // 101 = ((0*2 + 1)*2 + 0)*2 + 1
		Code.loadConst(0);
		for (int i = 0; i < 8; i++) {
			Code.loadConst(2);
			Code.put(Code.mul);
			Code.load(modifikacijaD.getDesignator().obj);
			Code.loadConst(i);
			Code.put(Code.aload);
			Code.put(Code.add);
		}
		poslednjiFaktor = null; // PAZI DA I OVO TREBA DA SE POSTAVI!
	}

	//nizModifikacija3 = ^1094861636; - Treba da ispise A B C D
	public void visit(ModifikacijaKapica modifikacijaK) {
		// Ovde dodajemo faktor kao operand za operaciju ^, pa zato moramo da ga
		// nabacimo na expression stek
		// Pazi i ovde!
		if (poslednjiFaktor != null) {
			Code.load(poslednjiFaktor);
			poslednjiFaktor = null;
		}

		Code.loadConst(4);
		Code.put(Code.newarray); // Napravio niz charova i vratio na Expression stek adresu alociranoh niza
		Code.put(0); //adresa niza im 0

		Code.put(Code.enter);
		Code.put(2); // 2 parametar
		Code.put(2); // Zbir param + pomocna

		// Sada mi je u 0. mi je vrednost koju hocu da izdelim, a u 1. mi je adresa niza
		// U slucaju da sam ovde imao pravljenje niza, morao bih u petlji da dupliram
		// adresu, jer bi petlja cistila sve - V2
		// Adresa index vrednost>>8
		for (int i = 3; i >= 0; i--) {
			Code.put(Code.load_n + 1); // Adresa
			Code.loadConst(i); // Indeks
			Code.put(Code.load_n + 0); // Vrednost tog broja
			Code.loadConst((3 - i) * 8); // Koliko puta ga siftujem
			Code.put(Code.shr);
			Code.put(Code.bastore);
		}

		Code.put(Code.load_n + 1); // Pamti da mora da se vrati adresa niza !
		// Na kraju ne bih morao da imam ovu iznad liniju, jer bi na samom steku ostala
		// adresa od pre petlje - v2
		Code.put(Code.exit);

		poslednjiFaktor = null; // PAZI DA I OVO TREBA DA SE POSTAVI! //Ne mora, ali da ne zaboravim!
	}

	//print(|2*5+1::1+4::4*2|) - treba da ispise srednji broj od data 3
	public void visit(ModifikacijaBrojevi modifikacijaB) {
		Code.put(Code.enter);
		Code.put(3); // 2 parametar
		Code.put(3); // Zbir param + pomocna
		int rez;
		if (0 > 1) {
			//zameni im mesta
		}
		if (0 < 2) {
			if (1 <= 2)
				rez = 1;
			else
				rez = 2;
		} else
			rez = 0;

		// IF 1. > 2.
		Code.put(Code.load_n + 0);
		Code.put(Code.load_n + 1);
		Code.putFalseJump(Code.gt, 0);
		int adresaPosleThena = Code.pc - 2;

		// THEN -- dodao sam ih opet na expression stek i skinuo da ih vratim u obrnutom
		// redosledu
		Code.put(Code.load_n + 0);
		Code.put(Code.load_n + 1);
		Code.put(Code.store_n + 0);
		Code.put(Code.store_n + 1);

		// Ovde treba da skocim -> namestam adresu ako uslov nije zadovoljen
		// FixupMetoda iz Code klase
		Code.fixup(adresaPosleThena);

		// Manja u 0. veca u 1.
		// Drugi if
		Code.put(Code.load_n + 0);
		Code.put(Code.load_n + 2);
		Code.putFalseJump(Code.lt, 0);
		adresaPosleThena = Code.pc - 2;

		// THEN
			// IF
			Code.put(Code.load_n +1);
			Code.put(Code.load_n +2);
			Code.putFalseJump(Code.le, 0);
			int adresaPosleThena2 = Code.pc - 2;
			
			//THEN
			Code.put(Code.load_n + 1);
			//Na kraju then grane pamtim adresu gde zelim da preskocim else(kraj elsa)
			Code.putJump(0);
			int adresaPreskok = Code.pc - 2;
			
			//ELSE
			Code.fixup(adresaPosleThena2);
			Code.put(Code.load_n + 2);
			//A na kraju else grane fixupujem tu adresu preskoka i svaki put radim tako!!!
			Code.fixup(adresaPreskok);
		
		Code.putJump(0);
		adresaPreskok = Code.pc - 2;
		
		// ELSE
		// Ovde treba da skocim -> namestam adresu ako uslov nije zadovoljen
		// FixupMetoda iz Code klase
		Code.fixup(adresaPosleThena);
		Code.put(Code.load_n + 0);
		Code.fixup(adresaPreskok);

		Code.put(Code.exit);

		poslednjiFaktor = null;
	}

	public void visit(PocetakIzraza pocetakIzraza) {
		stekZaTekuci = new Stack<>();
		stekStekova.push(stekZaTekuci);
	}

	public void visit(Expression expression) {
		if (poslednjiFaktor != null) {
			Code.load(poslednjiFaktor);
			poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
		}
		while (!stekZaTekuci.empty()) {
			KlasaZaStek kS = stekZaTekuci.pop();
			Code.put(kS.operationCode);
			if (kS.rezultat.getKind() != Obj.Elem) {
				Code.put(Code.dup);
			} else {
				Code.put(Code.dup_x2);
			}
			Code.store(kS.rezultat);
		}
		stekStekova.pop();
		if (stekStekova.empty()) {
			stekZaTekuci = null;
		} else {
			stekZaTekuci = stekStekova.peek();
		}
	}

	//Cezarova sifra:
	public void visit(ModifikacijaHash exprHash) {
		// Dobijeni rezultat se sabere sa drugim izrazom
		Code.put(Code.add);

		// Doda se 26 tj. ukupan broj karaktera, da ne bih u minus otisao
		Code.loadConst(26);
		
		Code.put(Code.add);

		// Onda se radi mod 26 da se dobije pomeraj
		Code.loadConst(26);
		Code.put(Code.rem);

		// Taj pomeraj saberemo sa A
		Code.loadConst(65);
		Code.put(Code.add);
	}

	// Cezarova sifra dodatak:
	// Ovde prvo radim mod 65, pre nego sto stavim drugi Expr na stek
	public void visit(HashMod hashMod) {
		Code.loadConst(65);
		Code.put(Code.rem);
	}

	public void visit(AddRightOp addRightOp) {
		KlasaZaStek kS = new KlasaZaStek();
		kS.rezultat = poslednjiFaktor;

		if (addRightOp.getAddOpRight() instanceof AddEq) {
			kS.operationCode = Code.add;
		} else {
			kS.operationCode = Code.sub;
		}

		stekZaTekuci.push(kS);
		if (poslednjiFaktor.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(poslednjiFaktor);
		poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
	}

	public void visit(MulRightOp mulRightOp) {
		KlasaZaStek kS = new KlasaZaStek();
		kS.rezultat = poslednjiFaktor;

		if (mulRightOp.getMulOpRight() instanceof MulAsgOp) {
			kS.operationCode = Code.mul;
		} else if (mulRightOp.getMulOpRight() instanceof DivAsgOp) {
			kS.operationCode = Code.div;
		} else {
			kS.operationCode = Code.rem;
		}

		stekZaTekuci.push(kS);
		// Cela poenta odlozenog ucitavanja na stek:
		if (poslednjiFaktor.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(poslednjiFaktor);
		poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
	}

	public void visit(MulLeftOp mulLeftOp) {
		if (poslednjiFaktor != null) {
			Code.load(poslednjiFaktor);
			poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
		}
	}

	public void visit(AddLeftOp addLeftOp) {
		if (poslednjiFaktor != null) {
			Code.load(poslednjiFaktor);
			poslednjiFaktor = null; // dok nije loadovan, on je razlicit od null
		}
	}

	// Cuvam kombinovanu operaciju i promenljivu u koju cu da sacuvam rezultat
	private static class KlasaZaStek {
		public int operationCode;
		public Obj rezultat;
	}
	
	//TODO: Dodatak:
}
