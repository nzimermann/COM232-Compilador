import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Semantico implements Constants {
	private List<String> codigo = new ArrayList<>();
	private List<String> lista_id = new ArrayList<>();
	private Stack<String> pilha_tipos = new Stack<>();
	private Stack<String> pilha_rotulos = new Stack<>();
	private String operador_relacional = "";
	private Integer count_rotulo = 0;
	private Map<String, String[]> tabela_simbolos = new HashMap<>();

	public String getCodigoObjeto() {
		String codigo_obj = "";
		for (String s : codigo) {
			codigo_obj += s;
		}
		return codigo_obj;
	}

	public void executeAction(int action, Token token) throws SemanticError {
		// System.out.println("Acao #"+action+", Token: "+token);
		try {
			switch (action) {
				case 100: _100init();
					break;
				case 101: _101final();
					break;
				case 102: _102writeTipo(pilha_tipos.pop());
					break;
				case 103: _103and(pilha_tipos.pop(), pilha_tipos.pop());
					break;
				case 104: _104or(pilha_tipos.pop(), pilha_tipos.pop());
					break;
				case 105: _105true();
					break;
				case 106: _106false();
					break;
				case 107: _107not(pilha_tipos.pop());
					break;
				case 108: _108operadorRelacional(token);
					break;
				case 109: _109operacaoLogica(token);
					break;
				case 110: _110add(pilha_tipos.pop(), pilha_tipos.pop());
					break;
				case 111: _111sub(pilha_tipos.pop(), pilha_tipos.pop());
					break;
				case 112: _112mul(pilha_tipos.pop(), pilha_tipos.pop());
					break;
				case 113: _113div(pilha_tipos.pop(), pilha_tipos.pop());
					break;
				case 114: _114constante_int(token);
					break;
				case 115: _115constante_float(token);
					break;
				case 116: _116constante_string(token);
					break;
				case 117: _117minus(pilha_tipos.pop());
					break;
				case 118: _118if(pilha_tipos.pop(), token);
					break;
				case 119: _119selecao(pilha_rotulos.pop());
					break;
				case 120: _120else();
					break;
				case 121: _121repeat();
					break;
				case 122: _122repeatExp(pilha_tipos.pop(), token);
					break;
				case 123: _123repeatFim(pilha_rotulos.pop(), pilha_rotulos.pop());
					break;
				case 124: _124expressao(pilha_tipos.pop(), pilha_rotulos.pop(), token);
					break;
				case 125: _125identificador(token);
					break;
				case 126: _126declaracaoConstante(token);
					break;
				case 127: _127declaracaoVariavel(token);
					break;
				case 128: _128expressao(token);
					break;
				case 129: _129lista_id_entrada(token);
					break;
				case 130: _130entrada(token);
					break;
				case 131: _131identificador(token);
					break;
			}
		} catch (EmptyStackException e) {
			e.printStackTrace();
			throw new SemanticError(token.getLexeme() + " não declarado", token.getPosition());
		}
	}

	private void _100init() {
		this.codigo.add(
				".assembly extern mscorlib {}\n"
						+ ".assembly _exemplo{}\n"
						+ ".module _exemplo.exe\n\n"
						+ ".class public _exemplo{\n"
						+ ".method static public void _principal(){\n"
						+ ".entrypoint\n\n");
	}

	private void _101final() {
		codigo.add("\nret\n" + "}\n" + "}\n");
	}

	private void _102writeTipo(String tipo) {
		this.codigo.add((tipo == "int64") ? "conv.i8\n" : "");
		this.codigo.add("call void\n"
				+ "[mscorlib]System.Console::WriteLine("
				+ tipo
				+ ")\n");
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

	private void _109operacaoLogica(Token token) throws SemanticError {
		if (pilha_tipos.pop().equals("string") ^ pilha_tipos.pop().equals("string")) {
			throw new SemanticError("tipos incompatíveis em comando de operação lógica", token.getPosition());
		}
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
		codigo.add("ldc.i8 " + token.getLexeme() + "\n");
		codigo.add("conv.r8" + "\n");
	}

	private void _115constante_float(Token token) {
		pilha_tipos.push("float64");
		codigo.add("ldc.r8 " + token.getLexeme() + "\n");
	}

	private void _116constante_string(Token token) {
		pilha_tipos.push("string");
		codigo.add("ldstr " + token.getLexeme() + "\n");
	}

	private void _117minus(String tipo) {
		codigo.add("ldc.i8 -1" + "\n" + "conv.r8" + "\n");
		codigo.add("mul\n");
		pilha_tipos.push((tipo.equals("float64")) ? "float64" : "int64");
	}

	private void _118if(String tipo, Token token) throws SemanticError {
		if (!tipo.equals("bool")) {
			throw new SemanticError("expressão incompatível em comando de seleção", token.getPosition());
		}
		String rotulo = "novo_rotulo";
		codigo.add("brfalse " + rotulo + count_rotulo + "\n");
		pilha_rotulos.push(rotulo + count_rotulo++);
	}

	private void _119selecao(String rotulo) {
		codigo.add(rotulo + ":\n");
	}

	private void _120else() {
		String rotulo = "novo_rotulo";
		codigo.add("br " + rotulo + count_rotulo + "\n");
		codigo.add(pilha_rotulos.pop() + ":\n");
		pilha_rotulos.push(rotulo + count_rotulo++);
	}

	private void _121repeat() {
		String rotulo = "novo_rotulo";
		codigo.add(rotulo + count_rotulo + ":\n");
		pilha_rotulos.push(rotulo + count_rotulo++);
	}

	private void _122repeatExp(String tipo, Token token) throws SemanticError {
		if (!tipo.equals("bool")) {
			throw new SemanticError("expressão incompatível em comando de repetição", token.getPosition());
		}
		String rotulo = "novo_rotulo";
		codigo.add("brfalse " + rotulo + count_rotulo + "\n");
		pilha_rotulos.push(rotulo + count_rotulo++);
	}

	private void _123repeatFim(String rotulo2, String rotulo1) {
		codigo.add("br " + rotulo1 + "\n");
		codigo.add(rotulo2 + ":\n");
	}

	private void _124expressao(String tipo, String rotulo, Token token) throws SemanticError {
		if (!tipo.equals("bool")) {
			throw new SemanticError("expressão incompativel em comando de repetição", token.getPosition());
		}
		codigo.add("brtrue " + rotulo + "\n");
	}

	private void _125identificador(Token token) {
		lista_id.add(token.getLexeme());
	}

	private void _126declaracaoConstante(Token token) throws SemanticError {
		for (String s : lista_id) {
			if (tabela_simbolos.containsKey(s)) {
				throw new SemanticError(s + " ja declarado", token.getPosition());
			}
			String[] tipo_valor = new String[3];
			tipo_valor[2] = "constante";
			switch (s.charAt(1)) {
				case 'i':
					tipo_valor[0] = "int64";
					try {Integer.parseInt(token.getLexeme());} 
					catch (NumberFormatException | NullPointerException e) {
						throw new SemanticError("tipos incompatíveis em comando de atribuição", token.getPosition());
					}
					break;
				case 'f':
					tipo_valor[0] = "float64";
					try {Double.parseDouble(token.getLexeme());} 
					catch (NumberFormatException | NullPointerException e) {
						throw new SemanticError("tipos incompatíveis em comando de atribuição", token.getPosition());
					}
					break;
				case 's':
					tipo_valor[0] = "string";
					if (!token.getLexeme().startsWith("\"") && !token.getLexeme().endsWith("\"")) {
						throw new SemanticError("tipos incompatíveis em comando de atribuição", token.getPosition());
					}
					break;
				case 'b':
					tipo_valor[0] = "bool";
					if (!token.getLexeme().toLowerCase().equals("true") 
						&& !token.getLexeme().toLowerCase().equals("false")) {
						throw new SemanticError("tipos incompatíveis em comando de atribuição", token.getPosition());
					}
					break;
			}
			tipo_valor[1] = token.getLexeme();
			tabela_simbolos.put(s, tipo_valor);
		}
		this.lista_id.clear();
	}

	private void _127declaracaoVariavel(Token token) throws SemanticError {
		for (String s : lista_id) {
			if (tabela_simbolos.containsKey(s)) {
				throw new SemanticError(s + " ja declarado", token.getPosition());
			}
			String[] tipo_valor = new String[3];
			tipo_valor[2] = "variavel";
			switch (s.charAt(1)) {
				case 'i':
					tipo_valor[0] = "int64";
					break;
				case 'f':
					tipo_valor[0] = "float64";
					break;
				case 's':
					tipo_valor[0] = "string";
					break;
				case 'b':
					tipo_valor[0] = "bool";
					break;
			}
			tabela_simbolos.put(s, tipo_valor);
		}
		codigo.add(".locals (");
		String virgula = "";
		for (int i = 0; i < lista_id.size(); i++) {
			String[] tipo_valor = tabela_simbolos.get(lista_id.get(i));
			virgula = (i != lista_id.size()-1) ? ", " : "";
			codigo.add(tipo_valor[0] + " " + lista_id.get(i) + virgula);
		}
		codigo.add(")\n");
		this.lista_id.clear();
	}

	private void _128expressao(Token token) throws SemanticError {
		pilha_tipos.pop();
		for (int i = 0; i < lista_id.size() - 1; i++) {
			codigo.add("dup" + "\n");
		}
		for (String s : lista_id) {
			if (!tabela_simbolos.containsKey(s)) {
				throw new SemanticError(s + " não declarado", token.getPosition());
			}
			if (tabela_simbolos.get(s)[0].equals("int64")) {
				codigo.add("conv.i8" + "\n");
			}
			codigo.add("stloc " + s + "\n");
		}
		this.lista_id.clear();
	}

	private void _129lista_id_entrada(Token token) throws SemanticError {
		for (String s : lista_id) {
			if (!tabela_simbolos.containsKey(s)) {
				throw new SemanticError(s + " não declarado", token.getPosition());
			}
			this.codigo.add("call string\n"
					+ "[mscorlib]System.Console::ReadLine()\n");
			switch (tabela_simbolos.get(s)[0]) {
				case "int64":
					this.codigo.add("call int64\n"
							+ "[mscorlib]System.Int64::Parse(string)\n");
					break;
				case "float64":
					this.codigo.add("call float64\n"
							+ "[mscorlib]System.Double::Parse(string)\n");
					break;
				case "bool":
					this.codigo.add("call bool\n"
							+ "[mscorlib]System.Boolean::Parse(string)\n");
					break;
			}
			codigo.add("stloc " + s + "\n");
		}
		lista_id.clear();
	}

	private void _130entrada(Token token) {
		codigo.add("ldstr " + token.getLexeme() + "\n");
		this.codigo.add("call void\n"
				+ "[mscorlib]System.Console::Write(string)\n");
	}

	private void _131identificador(Token token) throws SemanticError {
		if (!tabela_simbolos.containsKey(token.getLexeme())) {
			throw new SemanticError(token.getLexeme() + " não declarado", token.getPosition());
		}
		String[] id_info = tabela_simbolos.get(token.getLexeme());
		if (id_info[2].equals("constante")) {
			switch (id_info[0]) {
				case "int64":
					codigo.add("ldc.i8 " + id_info[1] + "\n");
					codigo.add("conv.r8" + "\n");
					break;
				case "float64":
					codigo.add("ldc.r8 " + id_info[1] + "\n");
					break;
				case "string":
					codigo.add("ldstr " + id_info[1] + "\n");
					break;
				case "bool":
					if (id_info[1].toLowerCase().equals("true")) {
						codigo.add("ldc.i4 1 " + "\n");
					} else {
						codigo.add("ldc.i4 0 " + "\n");
					}
					break;
			}
			pilha_tipos.push(id_info[0]);
		} else {
			codigo.add("ldloc " + token.getLexeme() + "\n");
			codigo.add((id_info[0].equals("int64")) ? "conv.r8\n" : "");
			pilha_tipos.push(id_info[0]);
		}
	}

}
