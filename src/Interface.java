import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class Interface {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
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
		ToolBarButton btnNovo = new ToolBarButton("Novo [ctrl-n]", new ImageIcon("./img/new_file.png"));
		ToolBarButton btnAbrir = new ToolBarButton("Abrir [ctrl-o]", new ImageIcon("./img/open_file.png"));
		ToolBarButton btnSalvar = new ToolBarButton("Salvar [ctrl-s]", new ImageIcon("./img/save_file.png"));
		ToolBarButton btnCopiar = new ToolBarButton("Copiar [ctrl-c]", new ImageIcon("./img/copy_file.png"));
		ToolBarButton btnColar = new ToolBarButton("Colar [ctrl-v]", new ImageIcon("./img/paste_file.png"));
		ToolBarButton btnRecortar = new ToolBarButton("Recortar [ctrl-x]", new ImageIcon("./img/cut_file.png"));
		ToolBarButton btnCompilar = new ToolBarButton("Compilar [F7]", new ImageIcon("./img/compile.png"));
		ToolBarButton btnEquipe = new ToolBarButton("Equipe [F1]", new ImageIcon("./img/help.png"));

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
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainTextEditor.setText(null);
				msgArea.setText(null);
				statusBar.setText(null);
			}
		});

		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N"), "Novo");
		msgArea.getActionMap().put("Novo", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				mainTextEditor.setText(null);
				msgArea.setText(null);
				statusBar.setText(null);
			}
		});

		// FUNCOES ABRIR
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(fileChooser.getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							mainTextEditor.read(input, "Lendo");
							msgArea.setText(null);
							statusBar.setText(selectedFile.getAbsolutePath());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});

		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O"), "Abrir");
		msgArea.getActionMap().put("Abrir", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(fileChooser.getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							mainTextEditor.read(input, "Lendo");
							msgArea.setText(null);
							statusBar.setText(selectedFile.getAbsolutePath());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});

		// FUNCOES SALVAR
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File saveFile = new File(statusBar.getText());
				if (saveFile.exists()) {
					try {
						mainTextEditor.write(new OutputStreamWriter(new FileOutputStream(saveFile.getAbsolutePath()), "utf-8"));
						return;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int salvar = fileChooser.showSaveDialog(btnSalvar);
				if (salvar == JFileChooser.APPROVE_OPTION) {
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
		});

		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "Salvar");
		msgArea.getActionMap().put("Salvar", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				File saveFile = new File(statusBar.getText());
				if (saveFile.exists()) {
					try {
						mainTextEditor.write(new OutputStreamWriter(new FileOutputStream(saveFile.getAbsolutePath()), "utf-8"));
						return;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int salvar = fileChooser.showSaveDialog(btnSalvar);
				if (salvar == JFileChooser.APPROVE_OPTION) {
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
		});

		// FUNCOES COPIAR
		btnCopiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sText1 = mainTextEditor.getSelectedText();
				String sText2 = msgArea.getSelectedText();
				if (sText1 != null) {
					copyToClipboard(sText1);
				} else if (sText2 != null) {
					copyToClipboard(sText2);
				}
			}
		});

		// FUNCOES COLAR
		btnColar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainTextEditor.insert(getTextClipboard(), mainTextEditor.getCaretPosition());
			}
		});

		// FUNCOES RECORTAR
		btnRecortar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sText1 = mainTextEditor.getSelectedText();
				String sText2 = msgArea.getSelectedText();
				if (sText1 != null) {
					copyToClipboard(sText1);
				} else if (sText2 != null) {
					copyToClipboard(sText2);
				}
				mainTextEditor.replaceSelection("");
			}
		});

		// FUNCOES COMPILAR
		btnCompilar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msgArea.setText("compilação de programas ainda não foi implementada");
			}
		});

		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F7"), "Compilar");
		msgArea.getActionMap().put("Compilar", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				msgArea.setText("compilação de programas ainda não foi implementada");
			}
		});

		// FUNCOES EQUIPE
		btnEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msgArea.setText("Nicolas Zimermann\nLuís Felipe de Castilho\nArthur Felipe Lueders");
			}
		});

		msgArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "Equipe");
		msgArea.getActionMap().put("Equipe", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				msgArea.setText("Nicolas Zimermann\nLuís Felipe de Castilho\nArthur Felipe Lueders");
			}
		});
	}

	// METODOS EXTRAS
	private static void copyToClipboard(String text) {
		if (text == null || text.isBlank()) {
			return;
		}
		;
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
				System.out.println(ufe);
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
		return null;
	}
}