package sandrohc.gui;

import sandrohc.Main;

import javax.swing.*;

public class GuiLicoes extends javax.swing.JDialog {
	private javax.swing.JPanel contentPane;
	private javax.swing.JButton buttonOK;
	private javax.swing.JButton buttonCancel;

	private short[] licoesNum;
	private JTextArea licoes;
	private JPanel opcoes;
	private JPanel principal;
	private JLabel licoesLbl;


	public GuiLicoes() {
		this(null);
	}

	public GuiLicoes(short[] licoesNum) {
		buttonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				onOK();
			}
		});
		buttonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				onCancel();
			}
		});
		// call onCancel() when cross is clicked
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				onCancel();
			}
		});
		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				onCancel();
			}
		}, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.licoesNum = licoesNum;
		licoes.setText(shortToStr());

		// Mostra a GUI
		run();
	}

	public void run() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}

	private void onOK() {
		// Altera as lições do sumário atual na GUI principal
		Main.gui.sum.licoes = strToShort();

		// Atualiza a lista de lições
		int oldIndex = Main.gui.licoesField.getSelectedIndex();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(Main.gui.gerarSumLista());
		Main.gui.licoesField.setModel(model);
		Main.gui.licoesField.setSelectedIndex(oldIndex);

		dispose();
	}

	private void onCancel() {
		dispose();
	}

	private String shortToStr() {
		if(licoesNum == null)
			return "";

		StringBuilder str = new StringBuilder();
		for(short num : licoesNum) {
			str.append(num > 1 ? num : 1);
			str.append("\n");
		}
		return str.toString();
	}

	private short[] strToShort() {
		if(licoes.getText() == null)
			return null;

		String[] texto = licoes.getText().split("\n");
		short[] shortArr = new short[texto.length];

		for(int i = 0; i < texto.length; i++) {
			try {
				short act = Short.valueOf(texto[i]);
				shortArr[i] = act > 1 ? act : 1;
			} catch(NumberFormatException e) {
				shortArr[i] = 1;
			}
		}
		return shortArr;
	}

	private void createUIComponents() {
		licoes = new JTextArea("");
	}
}
