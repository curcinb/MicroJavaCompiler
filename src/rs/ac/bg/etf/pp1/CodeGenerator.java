package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import rs.ac.bg.etf.pp1.CounterVisitor.FormalParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}

	public void visit(MethodDeclaration methodDeclaration) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MethodName methodName) {
		if ("main".equals(methodName.getMethName())) {
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);

		SyntaxNode methNode = methodName.getParent();

		VarCounter varCnt = new VarCounter();
		methNode.traverseTopDown(varCnt);

		Code.put(Code.enter);
		Code.put(0);
		Code.put(varCnt.getCount());
	}

	// READ():
	public void visit(ReadStatement readStatement) {
		Struct tip = readStatement.getDesignator().obj.getType();

		if (tip == Tab.charType) {
			Code.put(Code.bread);
		} else if (tip == Tab.intType) {
			Code.put(Code.read);
		}
		Code.store(readStatement.getDesignator().obj);
	}

	// PRINT();
	public void visit(PrintStatement printStatement) {
		if (printStatement.getOptionalPrint() == null) {
			if (printStatement.getExpr().struct == Tab.intType) { // int
				Code.loadConst(5);
				Code.put(Code.print);
			} else { // char
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
		} else { // Prvo na expr stek nabacim vrednost
			Code.loadConst(printVal);
			if (printStatement.getExpr().struct != Tab.charType) {
				Code.put(Code.print);
			} else {
				Code.put(Code.bprint);
			}
		}
	}

	// PRINT(NESTO): Ovde zapamtim vrednost koja treba da se stampa
	int printVal;

	public void visit(PrintOptional printOptional) {
		printVal = printOptional.getVal();
	}

	public void visit(DesignatorAssignment designatorAssignment) {
		Code.store(designatorAssignment.getDesignator().obj);
	}

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
		// Pamtim adresu niza na expr steku
		Code.load(designatorNiz.getDesignator().obj);
	}

	public void visit(SingleTermMinus singleTermMinus) {
		Code.put(Code.neg);
	}

	// Mnozenje, deljenje, ostatak
	public void visit(MultiplTerms multiplTerms) {
		MulOp ml = multiplTerms.getMulOp();

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

	// Sabiranje, oduzimanje
	public void visit(MultipleTerms multipleTerms) {
		AddOp aL = multipleTerms.getAddOp();

		if (aL instanceof AddPlus) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}

	public void visit(DesignatorFactor designatorFactor) {
		Code.load(designatorFactor.getDesignator().obj);
	}

	// Samo na exprstek bacim broj:
	public void visit(NumFactor numFactor) {
		Code.loadConst(numFactor.getN1());
	}

	// Samo na exprstek bacim char:
	public void visit(CharFactor charFactor) {
		Code.loadConst(charFactor.getC1());
	}

	// Samo na exprstek bacim boolean kao broj:
	public void visit(BoolFactor boolFactor) {
		String value = boolFactor.getB1();
		if (value.equals("true")) {
			Code.loadConst(1);
		} else {
			Code.loadConst(0);
		}
	}

	// Pravljenje niza:
	public void visit(NewFactorExpr newFactorExpr) {
		// Za pristup elementima:
		// Code.loadConst(2);
		// Code.put(Code.mul);
		Code.put(Code.newarray);
		if (newFactorExpr.getNewType().struct == Tab.charType) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}

	// Poziv funkcije:
	public void visit(DesignatorFactorFuncCall designatorFactorFuncCall) {
		Obj functionObj = designatorFactorFuncCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	// TODO:
	// print(niz @ 2 * 3); - ispisuje zbir 2gog i (poslednji-2)gog elementa
	// u Factor: Designator @ Factor
	public void visit(ModifikacijaAt modifikacijaAt) {

		// Simuliram ulazak u funkciju da bih mogao da pokupim parametre dodatne:
		Code.put(Code.enter);
		Code.put(1); // 1 parametar
		Code.put(2); // Zbir param + pomocna

		// Dobijanje prvog operanda za sabiranje
		// Prvo nabacujem adresu pa index - Pri pravljenju niza, adresa njegova se cuva
		// u promenljivoj sa leve strane
		Code.load(modifikacijaAt.getDesignator().obj);// Adr
		Code.put(Code.load_n + 0); // Indeks
		Code.put(Code.aload);

		// Dobijanje drugog operanda za sabiranje
		Code.load(modifikacijaAt.getDesignator().obj);
		// Dobijanje duzine niza
		Code.load(modifikacijaAt.getDesignator().obj);
		Code.put(Code.arraylength);

		Code.put(Code.load_n + 0); // Pomeraj
		Code.put(Code.sub); // Oduzmem ga od kraja
		Code.loadConst(1); // Pa jos oduzmem keca zbog indeksiranja
		Code.put(Code.sub);
		Code.put(Code.aload);

		Code.put(Code.add); // radim sabiranje
		Code.put(Code.store_n + 1); // u prvu pomogcnu pamtim dobijenu vrednost - da bih mogao da je zapamtim u
									// drugu promenljivu niza

		Code.load(modifikacijaAt.getDesignator().obj); // Adresa
		Code.put(Code.load_n + 0); // Index
		Code.put(Code.load_n + 1); // Vrednost
		Code.put(Code.astore); // Za lokalnu promenljivu

		Code.put(Code.load_n + 1); // Vracam dobijenu sabranu vrednost
		Code.put(Code.exit);
	}

	// TODO:
	// print($niz) - Ispisuje broj dat u nizu u binarnom obliku:
	// u Factor: $ Designator
	public void visit(ModifikacijaDolar modifikacijaD) { // 101 = ((0*2 + 1)*2 + 0)*2 + 1
		Code.loadConst(0);
		for (int i = 0; i < 8; i++) {
			Code.loadConst(2);
			Code.put(Code.mul);
			Code.load(modifikacijaD.getDesignator().obj); // Dohvatanje elementa
			Code.loadConst(i); // sa i-te
			Code.put(Code.aload); // pozicije
			Code.put(Code.add);
		}
	}

	//TODO:
	//nizModifikacija3 = ^1094861636; - Treba da ispise A B C D
	//primer: Factor ^ 4 * 5 + 6
	//u Factor: ^ Factor(4)
	//ili : ^Term(20)
	//ili: ^Expr(26)
	public void visit(ModifikacijaKapica modifikacijaK) {
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
		}

	//TODO: 
	//Skokovi
	//print(|2*5+1::1+4::4*2|) - treba da ispise srednji broj od data 3
	public void visit(ModifikacijaBrojevi modifikacijaB) {
		Code.put(Code.enter);
		Code.put(3); // Broj parametara metode
		Code.put(3); // Zbir parametara + pomocnih promenljivih
			
		//Primer:
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
		//kraj primera
			
		// IF 0. > 1.
		Code.put(Code.load_n + 0);
		Code.put(Code.load_n + 1);
		Code.putFalseJump(Code.gt, 0);
		int adresaPosleThena = Code.pc - 2;
		// THEN -- dodao sam ih opet na expression stek i skinuo da ih vratim u obrnutom redosledu
		Code.put(Code.load_n + 0);
		Code.put(Code.load_n + 1);
		Code.put(Code.store_n + 0);
		Code.put(Code.store_n + 1);
		// Ovde treba da skocim -> namestam adresu ako uslov nije zadovoljen
		Code.fixup(adresaPosleThena);

		//Nakon toga: 0.< 1. sigurno
		// IF 0. < 2.
		Code.put(Code.load_n + 0);
		Code.put(Code.load_n + 2);
		Code.putFalseJump(Code.lt, 0);
		adresaPosleThena = Code.pc - 2;
		//THEN
			// IF 1. < 2.
			Code.put(Code.load_n +1);
			Code.put(Code.load_n +2);
			Code.putFalseJump(Code.le, 0);
			int adresaPosleThena2 = Code.pc - 2;
			//THEN
			Code.put(Code.load_n + 1);
			//Na kraju then grane pamtim adresu gde zelim da preskocim else(kraj elsa)
			Code.putJump(0);
			int adresaPreskok = Code.pc - 2;
				
			//ELSE 1. > 2.
			Code.fixup(adresaPosleThena2);
			Code.put(Code.load_n + 2);
			//A na kraju else grane fixupujem tu adresu preskoka i svaki put radim tako!!!
			Code.fixup(adresaPreskok);
			
		Code.putJump(0);
		adresaPreskok = Code.pc - 2;
		// ELSE 0.> 2.
		// Ovde treba da skocim -> namestam adresu ako uslov nije zadovoljen
		Code.fixup(adresaPosleThena);
		Code.put(Code.load_n + 0);
		Code.fixup(adresaPreskok);
			
		Code.put(Code.exit);
	}

	//TODO:
	// Cezarova sifra:
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

	public void visit(CondFactExpr condFactExpr) {
		Code.fixup(condFactExpr.getDvotacka2().struct.getKind());
	}

	public void visit(Dvotacka2 dvotacka) {
		// Ako uslov jeste zadovoljen, ovde ide preskok else grane
		Code.putJump(0);
		int adresaSkoka = Code.pc - 2;
		dvotacka.struct = new Struct(adresaSkoka);

		// Skacemo ako uslov nije zadovoljen
		CondFactExpr condFactExpr = (CondFactExpr) dvotacka.getParent();
		Code.fixup(condFactExpr.getCondFact().struct.getKind());
	}

	public void visit(CondDouble condFactDouble) {
		int op = Code.ge;
		if (condFactDouble.getRelOp() instanceof JednakoOp) {
			op = Code.eq;
		} else if (condFactDouble.getRelOp() instanceof NejednakoOp) {
			op = Code.ne;
		} else if (condFactDouble.getRelOp() instanceof VeceOp) {
			op = Code.gt;
		} else if (condFactDouble.getRelOp() instanceof ManjeOp) {
			op = Code.lt;
		} else if (condFactDouble.getRelOp() instanceof ManjeJednakoOp) {
			op = Code.le;
		}
		Code.putFalseJump(op, 0);
		int adresaSkoka = Code.pc - 2;
		condFactDouble.struct = new Struct(adresaSkoka);
	}

	public void visit(CondSingle condFactSingle) {
		Code.loadConst(1);
		Code.putFalseJump(Code.eq, 0);
		int adresaSkoka = Code.pc - 2;
		condFactSingle.struct = new Struct(adresaSkoka);
	}


	//Vezbanje:	
	/*
	//TODO: ispis pristupa elementu niza
	//u Factor: ?? Designator
	public void visit(DesignatorArr designatorArr) {
		SyntaxNode desParent = designatorArr.getParent();
			
		if(!(desParent instanceof ModifikacijaDvoupitnik)) { //Obican pristup nizu 
			//Na steku imam duzinu niza i adresu
			//To onda dupliram:
			Code.put(Code.dup2);
			Code.put(Code.dup2); 
			//Na steku sad imam 3x adresu i duzinu niza - jednu za upis elementa
			//jednu za dohvatanje brojaca i jednu za inkrement brojaca
			Code.put(Code.pop); //Samo pomocni, da bi mi na steku ostala jedna adresa
			Code.put(Code.arraylength); //Skida adresu sa steka i vraca mi duzinu niza
			Code.loadConst(2);  //Delim polovinu
			Code.put(Code.div);	//Daje mi polovinu niza		
			Code.put(Code.add); //Pa polovinu sabiram sa pomerajem
				
			Code.put(Code.dup2); //Moram da dupliram da bi mi posle ostalo za pravu dodelu!
				
			Code.put(Code.aload); //Dohvatio brojac
			Code.loadConst(1);    //Dodao mu keca
			Code.put(Code.add);    
			Code.put(Code.astore); //Sacuvao
		}
		else {											//Ispisivanje brojaca
			//Duplirao sam duzinu niza i adresu
			Code.put(Code.dup2);
			Code.put(Code.pop); //Samo pomocni, da bi mi na steku ostala jedna adresa
			Code.put(Code.arraylength);	//Skida adresu sa steka i vraca mi duzinu niza
			Code.loadConst(2);
			Code.put(Code.div);	
			Code.put(Code.add);
			Code.put(Code.aload);
		}
	}
	*/

	//TODO: Modifikacija januar - labele
	HashMap<String, Integer> hmLabela = new HashMap<>();
	HashMap<String, ArrayList<Integer>> hmGoto = new HashMap<>();
	//1. slucaj			//2.slucaj
	//LABELA: ...		//GOTO: ...
	//GOTO: ...			//LABELA: ...
	public void visit(Labela ls) {
		String labelaIdentifikator = ls.getI1();
			
		//Ako je skok na dole - zakrpi sve adrese koje si zapamtio	
		if(hmGoto.containsKey(labelaIdentifikator)) {
			ArrayList<Integer>lista = hmGoto.get(labelaIdentifikator);
			for(int i=0; i<lista.size(); i++) {
				Code.fixup(lista.get(i));
			}
		}
		//Ako je skok na gore - zapamti moju adresu
		int adresa = Code.pc;
		hmLabela.put(labelaIdentifikator, adresa);
	}
			
	public void visit(GotoStatement gs) {
		String skokIdentifikator = gs.getI1();
			
		//Ako je skok na gore - skoci na adresu
		if(hmLabela.containsKey(skokIdentifikator)) {
			Code.putJump(hmLabela.get(skokIdentifikator));
		}
		//Ako je skok na dole - zapamti adresu, pa je krpi posle
		else {
			Code.putJump(0);
			int adresa = Code.pc-2;
				
			if(hmGoto.containsKey(skokIdentifikator)) {	
				ArrayList<Integer>lista = hmGoto.get(skokIdentifikator);
				lista.add(adresa);
				hmGoto.put(skokIdentifikator, lista);
			}
			else {
				ArrayList<Integer> lista = new ArrayList<Integer>();
				lista.add(adresa);
				hmGoto.put(skokIdentifikator, lista);
			}
		}
	}
	
	//TODO:
	//TODO:
	/*
	//TODO: Zamena elemenata niza:
	//	niz~2,0~;
	//print(niz[2]);
	public void visit(SwapStatement statementSwap){
		Obj array = statementSwap.getDesignatorNiz().getDesignator().obj;
		Code.put(Code.dup2); 	//01: arr, t, u, t, u
		Code.load(array);		//02: arr, t, u, t, u, arr
		Code.put(Code.dup_x1);	//03: arr, t, u, t, arr, u, arr
		Code.put(Code.pop);		//04: arr, t, u, t, arr, u
		Code.put(Code.aload);	//05: arr, t, u, t, arr[u]
		Code.put(Code.dup_x2);	//07: arr, t, arr[u], u, t, arr[u]
		Code.put(Code.pop);		//08: arr, t, arr[u], u, t
		Code.load(array);		//09: arr, t, arr[u], u, t, arr
		Code.put(Code.dup_x1);	//10: arr, t, arr[u], u, arr, t, arr
		Code.put(Code.pop);		//11: arr, t, arr[u], u, arr, t
		Code.put(Code.aload);	//12: arr, t, arr[u], u, arr[t]
		Code.load(array);		//13: arr, t, arr[u], u, arr[t], arr
		Code.put(Code.dup_x2);	//14: arr, t, arr[u], arr, u, arr[t], arr
		Code.put(Code.pop);		//15: arr, t, arr[u], arr, u, arr[t]	
		Code.put(Code.astore);	//16: arr, t, arr[u]		
		Code.put(Code.astore);	//17: --
    }
    */
	
	
	//TODO: Modifikacija kvadrat
	/*
	public void visit(ModifikacijaKvadrat mK) {
		Code.put(Code.dup);  		
		Code.put(Code.mul);			
	}
	*/
	
	//TODO: Modifikacija faktorijel
	/*
	// x = ...;
    //rez = x;
    //brojac = x - 1;
    //while (brojac>0){
    //	rez = rez * brojac ;
    // 	brojac = brojac - 1; 	
    // }
	public void visit(ExprFaktorijel factorFakt) {
		Code.put(Code.dup);	// rez, brojac
		Code.loadConst(1); 	// rez, brojac, 1
		Code.put(Code.sub);	// rez, brojac=brojac-1
		//ulazim x - 1 puta u petlju!
		 
		int adrPocetkaWhile = Code.pc;  //ovde se vraca
		//i onda radi sve odavde ponovo
		Code.put(Code.dup); 	//rez, brojac, brojac
		Code.loadConst(0);		//rez, brojac, brojac, 0 (ovo mora da bude na ulazu u while petlju)
		
		//ulazak u while petlju
		Code.putFalseJump(Code.gt, 0); //rez, brojac (skloni poslednja 2 zbog poredjenja)
		int adrIzlaskaIzWhile = Code.pc - 2;
		 
		 
		Code.put(Code.dup_x1);	//brojac, rez, brojac
		Code.put(Code.mul); 	//brojac, rez=rez*brojac
		Code.put(Code.dup_x1);	//rez, brojac, rez
		Code.put(Code.pop);	//rez, brojac
		Code.loadConst(1);		//rez, brojac, 1
		Code.put(Code.sub);	//rez, brojac=brojac-1
		 
		Code.putJump(adrPocetkaWhile);
		Code.fixup(adrIzlaskaIzWhile); //rez
		
		Code.put(Code.pop);
	}
	*/
	
	/*
	//TODO: Modifikacija multi: !!3 = 3*3*3 npr
	public void visit(FactorMulti factorMulti) {
		Code.put(Code.dup); //rez, x
		Code.put(Code.dup); //rez, x, brojac
		Code.loadConst(1);
		Code.put(Code.sub);
		
		int adrPocetkaWhile = Code.pc;
		Code.put(Code.dup);	//rez, x, brojac, brojac
		Code.loadConst(0);		//rez, x, brojac, brojac, 0
		Code.putFalseJump(Code.gt, 0);	//rez, x, brojac
		int adr2 = Code.pc - 2;
		Code.put(Code.dup_x2);	//br,rez, x, br
		Code.put(Code.pop);	//br, rez, x
		Code.put(Code.dup_x1); //br, x, rez, x  
		Code.put(Code.mul);		//br, x, rez=rez*X
		Code.put(Code.dup_x2);	//rez, br, x, rez
		Code.put(Code.pop);		//rez, br, x
		Code.put(Code.dup_x1);	//rez, x, br, x
		Code.put(Code.pop);		//rez, x, br
		Code.loadConst(1);		//rez, x,br,1
		Code.put(Code.sub);		//rez, x, br=br - 1
			
		Code.putJump(adrPocetkaWhile); 
		Code.fixup(adr2);
			
		Code.put(Code.pop);
		Code.put(Code.pop);		 
	}
	*/
	
	/*
	//TODO:Max niza:
	//print(max(niz));
	// i = arr.length - 1;
	// max = arr[i]
	// i--;
	// while(i>=0) {
	// 	if (max > arr[i]) {
	// 		max = arr[i];
	//  }
	//  i--;
	// }
	// 
	public void visit(MaxNiza maxNiza) {
		Obj arr = maxNiza.getDesignatorNiz().getDesignator().obj;
		Code.load(arr);
		
		//Preparation
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.arraylength);	//arr, arr, len
		Code.loadConst(1);		//arr, arr, len, 1
		Code.put(Code.sub);		//arr, arr, i=len-1
		Code.put(Code.dup);		//arr, arr, i, i
		Code.put(Code.dup_x2);
		Code.put(Code.pop);		//arr, i, arr, i
		Code.put(Code.aload);	//arr, i, max = arr[i]
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		//arr, max, i
		
		//While condition [arr, max, i]
		int whileBegin = Code.pc;
		Code.put(Code.dup);
		Code.loadConst(0);	//arr, max, i, i, 0
		Code.putFalseJump(Code.ge, 0);
		int patchAdrWhile = Code.pc - 2;	//arr, max, i
		
		//While body
		//If condition
		Code.put(Code.dup_x2);	//i, arr, max, i
		Code.put(Code.dup_x1);	//i, arr, i, max, i
		Code.put(Code.pop);		//i, arr, i, max
		Code.put(Code.dup_x2);		//i, max, arr, i, max
		Code.put(Code.dup_x2);
		Code.put(Code.pop);		//i, max, max, arr, i
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		//i, max, max, i, arr
		Code.put(Code.dup_x2);	//i, max, arr, max, i, arr
		Code.put(Code.dup_x1);	//i, max, arr, max, arr, i, arr
		Code.put(Code.pop);		//i, max, arr, max, arr, i
		Code.put(Code.aload);	//i, max, arr, max, arr[i]
		Code.put(Code.dup_x1);	//i, max, arr, arr[i], max, arr[i]
		Code.putFalseJump(Code.lt, 0); //i, max, arr, arr[i]
		int patchAdrIf = Code.pc - 2; 	
		
		//If body
		Code.put(Code.dup_x2);	//i, arr[i], max, arr, arr[i]
		Code.put(Code.pop);		//i, arr[i], max, arr
		Code.put(Code.dup_x2);
		Code.put(Code.pop);		//i, arr, arr[i], max
		Code.put(Code.pop);		//i, arr, arr[i]
		Code.put(Code.dup_x1);	//i, arr[i], arr, arr[i]
		Code.put(Code.pop);		//i, arr[i], arr
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		//i, arr, arr[i]
		Code.put(Code.dup_x1);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		//i, max=arr[i], arr[i], arr
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		//i, max, arr, arr[i]
		
		//Rest
		Code.fixup(patchAdrIf);
		Code.put(Code.pop);		//i, max, arr
		Code.put(Code.dup_x2);
		Code.put(Code.pop);		//arr, i, max
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		//arr, max, i
		Code.loadConst(1);
		Code.put(Code.sub);		//arr, max, i=i-1
		Code.putJump(whileBegin); 	//arr, max, i
		
		Code.fixup(patchAdrWhile);
		
		//Clean-up arr, max, i
		Code.put(Code.pop);	//arr, max
		Code.put(Code.dup_x1);	//max, arr, max
		Code.put(Code.pop);
		Code.put(Code.pop); 
	}
	*/
	
	/*print(sum(niz));
	//TODO: Suma niza:
	public void visit(SumaNiza factorSumArr) {
		Obj arr = factorSumArr.getDesignator().obj;
		Code.load(arr);	//arr
		Code.put(Code.dup);//arr, arr
		Code.put(Code.dup);//arr, arr, arr
		Code.put(Code.arraylength);//arr, arr, length
		Code.loadConst(1);	//arr, arr, length, 1
		Code.put(Code.sub);//arr, arr, i = length - 1
		Code.put(Code.dup_x1);//arr, i, arr, i
		Code.put(Code.aload);//arr, i, sum=arr[i]
		Code.put(Code.dup_x1);//arr, sum, i, arr
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.sub);//arr,sum,i=i-1
		
		int adrWhile=Code.pc;
		Code.put(Code.dup);//arr, sum, i, i
		Code.loadConst(0);
		 
		Code.putFalseJump(Code.ge, 0); //arr, sum, i
		int adrPatch = Code.pc - 2;
		 
		Code.put(Code.dup_x2);	//i, arr, sum, i
		Code.put(Code.pop);	//i, arr, sum
		Code.put(Code.dup_x2);	//sum, i, arr, sum
		Code.put(Code.pop);		//sum, i, arr
		Code.put(Code.dup_x2); //arr, sum, i, arr
		Code.put(Code.dup_x1);	//arr, sum, arr,i,arr
		Code.put(Code.pop);	//arr, sum, arr,i
		Code.put(Code.dup_x2); //arr, i, sum, arr, i
		Code.put(Code.aload);	//arr, i, sum, arr[i]
		Code.put(Code.add);	//arr, i, sum = sum + arr[i]
		Code.put(Code.dup_x1);	//arr, sum, i,sum
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.sub);	//arr, sum, i=i-1
		 
		Code.putJump(adrWhile);
		Code.fixup(adrPatch); //arr, sum, i
		Code.put(Code.pop);	//arr, sum
		Code.put(Code.dup_x1);//sum, arr, sum
		Code.put(Code.pop);
		Code.put(Code.pop);
	 }
	*/
	
	/*
	//TODO: Veliko slovo
	//print(cap('c'));	
	// ASCII tabela -> prvo idu A-Z, pa a-z
	// Neka je c karakter, a C "veliko" c
	// Udaljenost c - 'a' = C - 'A' !
	// Dakle:
	// 		C = c - 'a' + 'A'
	// Ali, ovo ne radi ako je c = C
	// Dakle, za opsti slucaj, treba prvo proveriti da li je karakter "mali" -> ako jeste, treba izvrsiti povecavanje slova, inace, preskociti sve i to je to, ne dirati nista
	  
	// if (c >= 'a') {
	//		if (c <= 'z') {	//Ne znam kako idu slozeni uslovi, pa umesto c>='a' AND c <='z', razbijen je if na dva if-a
				//povecati c
	//		}
	// }
	public void visit(ModifikacijaCap factor_) {
		//c
		//IfA uslov
		Code.put(Code.dup);		//c, c
		Code.loadConst('a');	//c, c, 'a'
		Code.putFalseJump(Code.ge, 0);
		int patchAdrA = Code.pc - 2;	//c
			//IfA telo
			//IfB uslov
			Code.put(Code.dup);		//c, c
			Code.loadConst('z');	//c, c, 'z'
			Code.putFalseJump(Code.le, 0);
			int patchAdrB = Code.pc - 2;
				//IfB telo -> Ovaj segment je ako vazi c=['a'-'z']
				Code.loadConst('a');	//c, 'a'
				Code.put(Code.sub);		//c=c-'a'
				Code.loadConst('A');	//c, 'A'
				Code.put(Code.add);		//c=c-'a'+'A'
		
		//End ifA, ifB, nakon gornjeg Code.add
		Code.fixup(patchAdrB);
		Code.fixup(patchAdrA);
	}
	*/
	
	/*
	 * Rad za projekat: 
	 * 1. lexer 
	 * 2. parser 
	 * 3. Compiler 
	 * 4. Run *
	 * 
	 * Konfiguracija za Run: U Program arguments dodas: -debug test\program.obj
	 */
}
