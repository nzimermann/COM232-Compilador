import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class Interface {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Interface() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Compilador Maluco");
		frame.setBounds(200, 100, 920, 600);
		frame.setMinimumSize(new Dimension(910, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		// BARRA DE FERRAMENTAS
		JToolBar barraFerramentas = new JToolBar();
		frame.getContentPane().add(barraFerramentas, BorderLayout.NORTH);
		barraFerramentas.setPreferredSize(new Dimension(110, 70));
		JFileChooser fileChooser = new JFileChooser();

		// BOTOES
		JButton btnNovo = toolBarButton("Novo [ctrl-n]", new ImageIcon("./img/new_file.png"));
		JButton btnAbrir = toolBarButton("Abrir [ctrl-o]", new ImageIcon("./img/open_file.png"));
		JButton btnSalvar = toolBarButton("Salvar [ctrl-s]", new ImageIcon("./img/save_file.png"));
		JButton btnCopiar = toolBarButton("Copiar [ctrl-c]", new ImageIcon("./img/copy_file.png"));
		JButton btnColar = toolBarButton("Colar [ctrl-v]", new ImageIcon("./img/paste_file.png"));
		JButton btnRecortar = toolBarButton("Recortar [ctrl-x]", new ImageIcon("./img/cut_file.png"));
		JButton btnCompilar = toolBarButton("Compilar [F7]", new ImageIcon("./img/compile.png"));
		JButton btnEquipe = toolBarButton("Equipe [F1]", new ImageIcon("./img/help.png"));

		barraFerramentas.add(btnNovo);
		barraFerramentas.add(btnAbrir);
		barraFerramentas.add(btnSalvar);
		barraFerramentas.add(btnCopiar);
		barraFerramentas.add(btnColar);
		barraFerramentas.add(btnRecortar);
		barraFerramentas.add(btnCompilar);
		barraFerramentas.add(btnEquipe);

		// AREA PRINCIPAL
		JSplitPane mainArea = new JSplitPane();
		mainArea.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainArea.setResizeWeight(0.7);
		frame.getContentPane().add(mainArea, BorderLayout.CENTER);

		// EDITOR DE TEXTO
		JScrollPane scrollPane1 = new JScrollPane();
		mainArea.setLeftComponent(scrollPane1);

		JTextArea mainTextEditor = new JTextArea();
		mainTextEditor.setBorder(new NumberedBorder());
		scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane1.setViewportView(mainTextEditor);

		// AREA DE MENSAGEM
		JScrollPane scrollPane2 = new JScrollPane();
		mainArea.setRightComponent(scrollPane2);

		JTextArea msgArea = new JTextArea();
		msgArea.setEditable(false);
		msgArea.setLineWrap(true);
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setViewportView(msgArea);

		// BARRA DE STATUS
		JLabel statusBar = new JLabel();
		statusBar.setVerticalAlignment(SwingConstants.BOTTOM);
		statusBar.setPreferredSize(new Dimension(900, 25));
		frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

		// FUNCOES NOVO
		AbstractAction novo = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				mainTextEditor.setText(null);
				msgArea.setText(null);
				statusBar.setText(null);
			}
		};

		// FUNCOES ABRIR
		AbstractAction abrir = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(fileChooser.getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
						try {
							BufferedReader input = new BufferedReader(
								new InputStreamReader(new FileInputStream(selectedFile))
							);
							mainTextEditor.read(input, "Lendo");
							msgArea.setText(null);
							statusBar.setText(selectedFile.getAbsolutePath());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		};

		// FUNCOES SALVAR
		AbstractAction salvar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (statusBar.getText() != null) {
					File saveFile = new File(statusBar.getText());
					if (saveFile.exists()) {
						try {
							mainTextEditor.write(
								new OutputStreamWriter(new FileOutputStream(saveFile.getAbsolutePath()), "utf-8")
							);
							return;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int save = fileChooser.showSaveDialog(btnSalvar);
				if (save == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (file == null) {
						return;
					}
					if (!file.getName().toLowerCase().endsWith(".txt")) {
						file = new File(file.getParentFile(), file.getName() + ".txt");
					}
					try {
						mainTextEditor.write(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (file.getName().toLowerCase().endsWith(".txt")) {
						try {
							BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
							mainTextEditor.read(input, "Lendo");
							msgArea.setText(null);
							statusBar.setText(file.getAbsolutePath());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		};

		// FUNCOES COPIAR
		AbstractAction copiar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				String text1 = mainTextEditor.getSelectedText();
				String text2 = msgArea.getSelectedText();
				if (text1 != null) {
					copyToClipboard(text1);
				} else if (text2 != null) {
					copyToClipboard(text2);
				}
			}
		};

		// FUNCOES COLAR
		AbstractAction colar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				mainTextEditor.replaceSelection(getTextClipboard());
			}
		};

		// FUNCOES RECORTAR
		AbstractAction recortar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				String text1 = mainTextEditor.getSelectedText();
				String text2 = msgArea.getSelectedText();
				if (text1 != null) {
					copyToClipboard(text1);
				} else if (text2 != null) {
					copyToClipboard(text2);
				}
				mainTextEditor.replaceSelection(null);
			}
		};

		// FUNCOES COMPILAR
		AbstractAction compilar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				Lexico lexico = new Lexico(mainTextEditor.getText());
				Sintatico sintatico = new Sintatico();
				Semantico semantico = new Semantico();
				Token t = null;

				try {
					sintatico.parse(lexico, semantico);
					while ((t = lexico.nextToken()) != null) {
						if (t.getId() == 2) {
							msgArea.setText(
								"Erro na linha " + getLineNumberForIndex(mainTextEditor.getText(), t.getPosition())
								+ " - " + t.getLexeme() + " palavra reservada invalida"
							);
							return;
						}
					}
					msgArea.setText("programa compilado com sucesso");
				} catch (LexicalError lexicalError) {
					String msg = "Erro na linha " + getLineNumberForIndex(mainTextEditor.getText(), lexicalError.getPosition()) + " - ";
					if (lexicalError.getMessage().contains("identificador")) {
						String prog = mainTextEditor.getText().substring(lexicalError.getPosition());
						if (prog.contains("\n")) {
							if (prog.substring(0, prog.indexOf("\n")).contains(" ")) {
								msg += prog.substring(0, prog.indexOf(" "))+" ";
							} else {
								msg += prog.substring(0, prog.indexOf("\n"))+" ";
							}
						} else if (prog.contains(" ")) {
							msg += prog.substring(0, prog.indexOf(" "))+" ";
						} else {
							msg += prog+" ";
						}
					} else if (lexicalError.getMessage().contains("simbolo")) {
						msg += mainTextEditor.getText().charAt(lexicalError.getPosition())+" ";
					}
					msgArea.setText(msg + lexicalError.getMessage());
				} 
				catch (SyntaticError syntaticError) {
					String msg = "Erro na linha " + getLineNumberForIndex(mainTextEditor.getText(), syntaticError.getPosition()) + " - encontrado ";
					String prog = mainTextEditor.getText().substring(syntaticError.getPosition());
					if (prog.contains("\n")) {
						if (prog.substring(0, prog.indexOf("\n")).contains(" ")) {
							if (prog.isBlank()) {
								msg += "EOF";
							} else {
								msg += prog.substring(0, prog.indexOf(" "));
							}
						} else {
							if (prog.isBlank()) {
								msg += "EOF";
							} else {
								msg += prog.substring(0, prog.indexOf("\n"));
							}
						}
					} else if (prog.contains(" ")) {
						if (prog.isBlank()) {
							msg += "EOF";
						} else {
							msg += prog.substring(0, prog.indexOf(" "));
						}
					} else {
						if (prog.isBlank()) {
							msg += "EOF";
						} else {
							msg += prog;
						}
					}
					msgArea.setText(msg + "\n\t " + syntaticError.getLocalizedMessage());
				} catch (SemanticError semanticError) {
					
				}
			}
		};

		// FUNCOES EQUIPE
		AbstractAction equipe = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				msgArea.setText("Nicolas Zimermann\nLuís Felipe de Castilho\nArthur Felipe Lueders");
			}
		};

		// ADICIONAR FUNCOES
		btnNovo.addActionListener(novo);
		btnAbrir.addActionListener(abrir);
		btnSalvar.addActionListener(salvar);
		btnCopiar.addActionListener(copiar);
		btnColar.addActionListener(colar);
		btnRecortar.addActionListener(recortar);
		btnCompilar.addActionListener(compilar);
		btnEquipe.addActionListener(equipe);

		// ADICIONAR ATALHOS
		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N"), "Novo");
		msgArea.getActionMap().put("Novo", novo);
		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O"), "Abrir");
		msgArea.getActionMap().put("Abrir", abrir);
		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "Salvar");
		msgArea.getActionMap().put("Salvar", salvar);
		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F7"), "Compilar");
		msgArea.getActionMap().put("Compilar", compilar);
		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "Equipe");
		msgArea.getActionMap().put("Equipe", equipe);
	}

	private static JButton toolBarButton(String text, Icon icon) {
		JButton b = new JButton();
		b.setText(text);
		b.setIcon(icon);
		b.setMaximumSize(new Dimension(106, 70));
		b.setFocusable(false);
		b.setHorizontalTextPosition(JButton.CENTER);
		b.setVerticalTextPosition(JButton.BOTTOM);
		return b;
	}

	private static void copyToClipboard(String text) {
		if (text == null || text.isBlank()) {return;}
		StringSelection selection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	private static String getTextClipboard() {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		DataFlavor flavor = DataFlavor.stringFlavor;
		if (c.isDataFlavorAvailable(flavor)) {
			try {
				return (String) c.getData(flavor);
			} catch (UnsupportedFlavorException ufe) {
				ufe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	public static int getLineNumberForIndex(String strSource, int iIndex) {
		int iLineCount = 1;
		for (int i = 0; i <= iIndex && i < strSource.length(); i++) {
			char c = strSource.charAt(i);
			if (c == '\n') {
				iLineCount++;
			}
		}
		return iLineCount;
	}
}