package sandrohc.gui;

import sandrohc.Main;
import sandrohc.Sumario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static sandrohc.Main.LISTA;
import static sandrohc.Main.saveToFile;

public class GuiMain extends JFrame {
	private byte[] maxDias = { 31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30 };

	private JPanel panel;
	JComboBox<String> licoesField;
	private JLabel licoes;
	private JLabel data;
	private JComboBox<String> dataDia;
	private JComboBox<String> dataMes;
	private JComboBox<String> dataAno;
	private JPanel curr;
	private JTextArea planificacao;
	private JButton btnAdd;
	private JButton btnRem;
	private JButton btnSalvar;
	private JButton btnEditar;

	Sumario sum;
	private int sumIndex;

	public GuiMain(int index) {
		System.out.println("Took " + (System.currentTimeMillis() - Main.time) + "ms - phase 3");
		Main.time = System.currentTimeMillis();

		if(LISTA.size() < 1)
			addSum();

		this.sum = LISTA.get(index);
		this.sumIndex = index;

		populate();

		// Mostra a GUI no final do construtor
		run();

		System.out.println("Took " + (System.currentTimeMillis() - Main.time) + "ms - phase 4");
		Main.time = System.currentTimeMillis();

		licoesField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if(evt.getStateChange() == ItemEvent.SELECTED) atualizarSum(licoesField.getSelectedIndex());
			}
		});
		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                if(sum == null) return;
				new GuiLicoes(sum.licoes);
			}
		});
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvarDados();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSum();
			}
		});
		btnRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removerSum(sumIndex);
			}
		});

		// Adiciona ícones aos botões
		btnAdd.setIcon(new ImageIcon(GuiMain.class.getResource("add.png")));
		btnRem.setIcon(new ImageIcon(GuiMain.class.getResource("delete.png")));
		btnSalvar.setIcon(new ImageIcon(GuiMain.class.getResource("save.png")));
		btnEditar.setIcon(new ImageIcon(GuiMain.class.getResource("edit.png")));

		System.out.println("Took " + (System.currentTimeMillis() - Main.time) + "ms - phase 5");
		Main.time = System.currentTimeMillis();
	}

	/**
	 * Cria a GUI
	 */
	public void run() {
		setTitle("Sumários o' Plenty");
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		final List<Image> icons = new ArrayList<>();
		try {
			icons.add(ImageIO.read(Main.class.getResource("icon16.png")));
			icons.add(ImageIO.read(Main.class.getResource("icon32.png")));
			icons.add(ImageIO.read(Main.class.getResource("icon64.png")));
			icons.add(ImageIO.read(Main.class.getResource("icon128.png")));
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			setIconImages(icons);
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Grava os dados da Lista no ficheiro JSON
				try {
					// Salva o sumário atual, caso tenha alterações não salvas
					salvarDados();

					// Salva todos os dados no ficheiro
					saveToFile();
				} catch(IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * Atualiza a informação presente nos componentes da GUI
	 */
	public void populate() {
		licoesField.setSelectedIndex(sumIndex);
		planificacao.setText(sum.planificacao);

		atualizarData();
	}

	/**
	 * Cria os componentes básicos da GUI
	 */
	private void createUIComponents() {
		licoesField = new JComboBox<>(gerarSumLista());

		atualizarData();
	}

	void atualizarData() {
		if(sum == null) {
			dataDia = new JComboBox<>(new String[]{ "1" });
			dataMes = new JComboBox<>(new String[]{ "1" });
			dataAno = new JComboBox<>(new String[]{ "1" });
		} else {
			int mes = obterMes(sum.data);

			dataDia.setModel(new DefaultComboBoxModel<>(gerarArray(1, maxDias[mes])));
			dataMes.setModel(new DefaultComboBoxModel<>(gerarArray(1, 12)));
			dataAno.setModel(new DefaultComboBoxModel<>(gerarArray(2014, 2015)));

			dataDia.setSelectedItem(String.valueOf(obterDia(sum.data)));
			dataMes.setSelectedIndex(mes);
			dataAno.setSelectedItem(String.valueOf(obterAno(sum.data)));
		}
	}

	/**
	 * Gera uma array desde o valor mínimo até o valor máximo, ambos passados nos argumentos.
	 *
	 * @param min valor mínimo
	 * @param max valor máximo
	 * @return array com valores desde o @param min até o @param max
	 */
	private String[] gerarArray(int min, int max) {
		int size = Math.abs(min - max);
		String[] array = new String[size + 1];

		for(int i = 0; i <= size; i++)
			array[i] = String.valueOf(i + min);

		return array;
	}

	/**
	 * Gera uma array com as lições de todos os sumários
	 *
	 * @return array com informação de todos os sumários
	 */
	public String[] gerarSumLista() {
		if(LISTA.size() < 1)
			return new String[]{ "" };

		String[] lista = new String[LISTA.size()];
		for(byte i = 0; i < LISTA.size(); i++) {
			Sumario sum = LISTA.get(i);
			lista[i] = licoesToStr(sum);
		}
		return lista;
	}

	public short[] licoesToArr() {
		String[] texto = licoesField.getSelectedItem().toString().split(", ");
		short[] licoes = new short[texto.length];
		for(int i = 0; i < texto.length; i++) {
			try {
				licoes[i] = Short.parseShort(texto[i]);
			} catch(Exception e) {
				licoes[i] = 0;
			}
		}
		return licoes;
	}

	public String licoesToStr(Sumario sum) {
		if(sum == null || sum.licoes == null)
			return "??";
		return Arrays.toString(sum.licoes).replace("[", "").replace("]", "");
	}

	public int obterDia(long data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int obterMes(long data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		return cal.get(Calendar.MONTH);
	}

	public int obterAno(long data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		return cal.get(Calendar.YEAR);
	}

	public String obterData(Sumario sum) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Calendar cal = Calendar.getInstance();
		if(sum != null)
			cal.setTime(new Date(sum.data));
		else
			System.out.println("Sumário não contém uma data válida, usando a data atual.");
		return dateFormat.format(cal.getTime());
	}

	/**
	 * Altera o sumário para o encontrado na posição passada nos argumentos
	 *
	 * @param index posição na lista de sumários
	 */
	private void atualizarSum(int index) {
        if(sumIndex == index)
            return;

		System.out.println("Alterando de sumário " + sumIndex + " para " + index);

		// Faz com que um index válido esteja presente
        index = Math.min(Math.max(index, 0), LISTA.size());

		// Verifica se o sumário que está a tentar abrir e diferente do atual
		if(index != sumIndex) {
			// Guarda os dados sempre que se altera o sumário
			salvarDados();

			sumIndex = index;
			sum = LISTA.get(index);

			populate();
		}
	}

	/**
	 * Adiciona um sumário vazio à lista principal
	 */
	private void addSum() {
		System.out.println("Adicionando novo sumário");

		Sumario novoSum = new Sumario(null, Calendar.getInstance().getTimeInMillis(), null);

		// Tentativa de inteligência artificial
		novoSum.licoes = gerarProxLicoes();

		LISTA.add(novoSum);
		licoesField.addItem(licoesToStr(novoSum));

		atualizarSum(LISTA.size() - 1);
	}

	/**
	 * Remove o sumário na posição passada nos argumentos
	 *
	 * @param index posição na lista de sumários
	 */
	private void removerSum(int index) {
		System.out.println("Removendo sumário " + index);

		if(index == 0)
			LISTA.clear();
		else {
			LISTA.remove(index);

            int novoIndex = index - 1 > 0 ? index - 1 : 0;
			atualizarSum(novoIndex);

			// Atualiza a lista de lições
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(gerarSumLista());
			licoesField.setModel(model);
			licoesField.setSelectedIndex(novoIndex);
		}
	}

	private long dataToMillis() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf((String) dataDia.getSelectedItem()));
		cal.set(Calendar.MONTH, dataMes.getSelectedIndex());
		cal.set(Calendar.YEAR, Integer.valueOf((String) dataAno.getSelectedItem()));
		return cal.getTimeInMillis();
	}

	void salvarDados() {
		if(sum == null)
			sum = new Sumario(null, 0, null);

		sum.licoes = licoesToArr();
		sum.data = dataToMillis();
		sum.planificacao = planificacao.getText();

		if(Main.LISTA.size() > sumIndex)
			Main.LISTA.set(sumIndex, sum);
	}

	/**
	 * Cria um array com o número das próximas lições, baseado nas lições do último sumário da lista
	 *
	 * @return array com as lições
	 */
	private short[] gerarProxLicoes() {
		short ultima;
		if(LISTA.size() > 0) {
			short[] ultimas = LISTA.get(LISTA.size() - 1).licoes;
			ultima = ultimas[ultimas.length - 1];
		} else {
			ultima = 0;
		}

		return new short[]{ (short) (ultima + 1), (short) (ultima + 2) };
	}
}