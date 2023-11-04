import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Semantico implements Constants {
	List<String> codigo = new ArrayList<>();
	Stack<String> pilha_tipos = new Stack<>();
	String operador_relacional;
	
    public void executeAction(int action, Token token)	throws SemanticError
    {
        // System.out.println("Acao #"+action+", Token: "+token);
    	
        switch(action) {
        	case 100: _100init();
        		break;
        	case 101: _101final();
        		codigo.forEach(s -> {System.out.print(s);});
        		break;
        	case 102: _102writeTipo(pilha_tipos.pop());
        		break;
        	case 103: _103and(pilha_tipos.pop(),pilha_tipos.pop());
        		break;
        	case 104: _104or(pilha_tipos.pop(),pilha_tipos.pop());
        		break;
        	case 105: _105true();
        		break;
        	case 106: _106false();
    			break;
        	case 107: _107not(pilha_tipos.pop());
        		break;
        	case 108: _108operadorRelacional(token);
        		break;
        	case 109: _109operacaoLogica();
        		break;
        	case 110: _110add(pilha_tipos.pop(),pilha_tipos.pop());
        		break;
        	case 111: _111sub(pilha_tipos.pop(),pilha_tipos.pop());
        		break;
        	case 112: _112mul(pilha_tipos.pop(),pilha_tipos.pop());
    			break;
        	case 113: _113div(pilha_tipos.pop(),pilha_tipos.pop());
    			break;
        	case 114: _114constante_int(token);
        		break;
        	case 115: _115constante_float(token);
        		break;
        	case 116: _116constante_string(token);
        		break;
        	case 117: _117minus(pilha_tipos.pop());
        		break;
        }
    }

    private void _100init() {
    	this.codigo.add(
    			".assembly extern mscorlib {}\n"
				+ ".assembly _exemplo{}\n"
				+ ".module _exemplo.exe\n\n"
				+ ".class public _exemplo{\n"
				+ ".method static public void _principal(){\n"
				+ ".entrypoint\n\n"
			);
    }

    private void _101final() {
		codigo.add("\nret\n" + "}\n" + "}\n");
    }

    private void _102writeTipo(String tipo) {
    	this.codigo.add((tipo == "int64") ? "conv.i8\n" : "");
    	this.codigo.add("call void\n"
        				+ "[mscorlib]System.Console::WriteLine("
        				+ tipo
        				+ ")\n"
        			);
    }

    private void _103and(String tipo1, String tipo2) {
    	pilha_tipos.push("bool");
    	codigo.add("and\n");
    }

    private void _104or(String tipo1, String tipo2) {
    	pilha_tipos.push("bool");
    	codigo.add("or\n");
    }

    private void _105true() {
    	pilha_tipos.push("bool");
    	codigo.add("ldc.i4 1" + "\n");
    }

    private void _106false() {
    	pilha_tipos.push("bool");
    	codigo.add("ldc.i4 0" + "\n");
    }

    private void _107not(String tipo) {
    	pilha_tipos.push("bool");
    	codigo.add("ldc.i4 1" + "\n" + "xor" + "\n");
    }

    private void _108operadorRelacional(Token token) {
    	operador_relacional = token.getLexeme();
    }

    private void _109operacaoLogica() {
    	pilha_tipos.pop();
    	pilha_tipos.pop();
    	pilha_tipos.push("bool");
    	switch (operador_relacional) {
    		case "==":
    			codigo.add("ceq" + "\n");
    			break;
    		case "!=":
    			codigo.add("ceq" + "\n");
    			this._107not(pilha_tipos.pop());
    			break;
    		case "<":
    			codigo.add("clt" + "\n");
    			break;
    		case ">":
    			codigo.add("cgt" + "\n");
    			break;
    	}
    }

    private void _110add(String tipo1, String tipo2) {
    	codigo.add("add\n");
    	if (tipo1.equals("float64") || tipo2.equals("float64")) {
    		pilha_tipos.push("float64");
    		return;
    	}
    	pilha_tipos.push("int64");
    }

    private void _111sub(String tipo1, String tipo2) {
    	codigo.add("sub\n");
    	if (tipo1.equals("float64") || tipo2.equals("float64")) {
    		pilha_tipos.push("float64");
    		return;
    	}
    	pilha_tipos.push("int64");
    }

    private void _112mul(String tipo1, String tipo2) {
    	codigo.add("mul\n");
    	if (tipo1.equals("float64") || tipo2.equals("float64")) {
    		pilha_tipos.push("float64");
    		return;
    	}
    	pilha_tipos.push("int64");
    }

    private void _113div(String tipo1, String tipo2) {
    	codigo.add("div\n");
    	if (tipo1.equals("float64") || tipo2.equals("float64")) {
    		pilha_tipos.push("float64");
    		return;
    	}
    	pilha_tipos.push("int64");
    }

    private void _114constante_int(Token token) {
    	pilha_tipos.push("int64");
    	codigo.add("ldc.i8 " +  token.getLexeme() + "\n");
    	codigo.add("conv.r8" + "\n");
    }

    private void _115constante_float(Token token) {
    	pilha_tipos.push("float64");
    	codigo.add("ldc.r8 " +  token.getLexeme() + "\n");
    }

    private void _116constante_string(Token token) {
    	pilha_tipos.push("string");
    	codigo.add("ldstr " +  token.getLexeme() + "\n");
    }

    private void _117minus(String tipo) {
    	codigo.add("ldc.i8 -1" + "\n" + "conv.r8" + "\n");
    	codigo.add("mul\n");
    	pilha_tipos.push((tipo.equals("float64"))?"float64":"int64");
    }

}
