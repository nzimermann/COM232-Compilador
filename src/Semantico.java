import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Semantico implements Constants
{
	List<String> codigo = new ArrayList<>();
	Stack<String> pilha_tipos = new Stack<>();
	
    public void executeAction(int action, Token token)	throws SemanticError
    {
        // System.out.println("Ação #"+action+", Token: "+token);
    	
        switch(action) {
        	case 100: _100init();
        		break;
        	case 101: _101final();
        		break;
        	case 102: _102writeTipo(this.pilha_tipos.pop());
        		break;
        	case 114: _114constante_int(token);
        		this.pilha_tipos.push("int64");
        		break;
        	case 115: _115constante_float(token);
        		this.pilha_tipos.push("float64");
        		break;
        }
    }
    
    private void _100init() {
    	this.codigo.add(
    			".assembly extern mscorlib {}\n"
				+ ".assembly _exemplo{}\r\n"
				+ ".module _exemplo.exe\r\n"
				+ "\r\n"
				+ ".class public _exemplo{\r\n"
				+ ".method static public void _principal(){\r\n"
				+ ".entrypoint\r\n\n"
			);
    }
    
    private void _101final() {
		this.codigo.add("ret\n" + "}\n" + "}");
		System.out.println(this.codigo);
    }
    
    private void _102writeTipo(String tipo) {
    	this.codigo.add((tipo == "int64") ? "conv.i8\n" : "");
    	this.codigo.add("call void\n"
        				+ "[mscorlib]System.Console::WriteLine("
        				+ tipo
        				+ ")\n"
        			);
    }

    private void _114constante_int(Token token) {
    	this.codigo.add("ldc.i8 " +  token.getLexeme() + "\n");
    	this.codigo.add("conv.r8" + "\n");
    }
    
    private void _115constante_float(Token token) {
    	this.codigo.add("ldc.r8 " +  token.getLexeme() + "\n");
    }

}
