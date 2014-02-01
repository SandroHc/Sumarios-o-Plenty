package sandrohc.gui;

import sandrohc.Main;
import sandrohc.Sumario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static sandrohc.Main.LISTA;

public class GuiMain extends JFrame {
	private byte[] maxDias = { 31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30 };

	private JPanel panel;
	private JComboBox<String> licoesField;
	private JLabel licoes;
	private JLabel data;
	private JComboBox<String> dataDia;
	private JComboBox<String> dataMes;
	private JComboBox<String> dataAno;
	private JPanel curr;
	private JTextArea planificacao;
	private JButton btnAdd;
	private JButton btnSalvar;

	private Sumario sum;
	private int sumIndex;

	public GuiMain(int index) {
		this.sum = LISTA.size() >= index && LISTA.get(index) == null ? LISTA.get(0) : LISTA.get(index);
		this.sumIndex = index;

		populate();

		// Mostra a GUI no final do cosntrutor
		run();

		licoesField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if(evt.getStateChange() == ItemEvent.SELECTED)
					atualizarSum(licoesField.getSelectedIndex());
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
	}

	/**
	 * Cria a GUI
	 */
	public void run() {
		JFrame frame = new JFrame("Sumários o' Plenty");
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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

	private void atualizarData() {
		if(sum == null) {
			dataDia = new JComboBox<>(new String[]{ "1" });
			dataMes = new JComboBox<>(new String[]{ "1" });
			dataAno = new JComboBox<>(new String[]{ "1" });
			return;
		}

		int mes = obterMes(sum.data);

		dataDia.setModel(new DefaultComboBoxModel<>(gerarArray(1, maxDias[mes])));
		dataMes.setModel(new DefaultComboBoxModel<>(gerarArray(1, 12)));
		dataAno.setModel(new DefaultComboBoxModel<>(gerarArray(2014, 2015)));

		dataDia.setSelectedItem(String.valueOf(obterDia(sum.data)));
		dataMes.setSelectedIndex(mes);
		dataAno.setSelectedItem(String.valueOf(obterAno(sum.data)));
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
	 * Gera uma array no formato "Lições - Data" de todos os sumários
	 *
	 * @return array com informação de todos os sumários
	 */
	private String[] gerarSumLista() {
		String[] lista = new String[LISTA.size()];
		for(byte i = 0; i < LISTA.size(); i++) {
			Sumario sum = LISTA.get(i);
			lista[i] = obterNome(sum);
		}
		System.out.println("Lista : " + Arrays.toString(lista));
		return lista;
	}

	public String obterNome(Sumario sum) {
		if(sum == null || sum.licoes == null)
			return "??";
		return Arrays.toString(sum.licoes).replace("[", "").replace("]", "");
	}

	private int obterDia(long data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		System.out.println("Dia : " + cal.get(Calendar.DAY_OF_MONTH));
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	private int obterMes(long data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		System.out.println("Mês : " + cal.get(Calendar.MONTH));
		return cal.get(Calendar.MONTH);
	}

	private int obterAno(long data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(data));
		System.out.println("Ano : " + cal.get(Calendar.YEAR));
		return cal.get(Calendar.YEAR);
	}

	private String obterData(Sumario sum) {
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
		// Previne dados inválida
		if(index < 0 || index > LISTA.size())
			return;

		sumIndex = index;
		sum = LISTA.get(index);

		System.out.println(" - ------ - ");

		populate();
	}

	/**
	 * Adiciona um novo sumário
	 */
	private void addSum() {
		Sumario novoSum = new Sumario(null, Calendar.getInstance().getTimeInMillis(), null);
		LISTA.add(novoSum);
		licoesField.addItem(obterNome(novoSum));

		atualizarSum(LISTA.size() - 1);
	}

	/**
	 * Remove o sumário na posição passada nos argumentos
	 *
	 * @param index posição na lista de sumários
	 */
	private void removerSum(int index) {
		LISTA.remove(index);
		licoesField.remove(index);

		atualizarSum(index);
	}

	private short[] licoesToArr() {
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

	private long dataToMillis() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		System.out.println("Dia : " + dataDia.getSelectedItem());
		System.out.println("Mês : " + dataMes.getSelectedItem());
		System.out.println("Ano : " + dataAno.getSelectedItem());
		cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf((String) dataDia.getSelectedItem()));
		cal.set(Calendar.MONTH, dataMes.getSelectedIndex());
		cal.set(Calendar.YEAR, Integer.valueOf((String) dataAno.getSelectedItem()));
		System.out.println("Data : " + obterData(new Sumario(null, cal.getTimeInMillis(), null)) + " - " + cal.getTimeInMillis());
		return cal.getTimeInMillis();
	}

	private void salvarDados() {
		if(sum == null)
			sum = new Sumario(null, 0, null);

		sum.licoes = licoesToArr();
		sum.data = dataToMillis();
		sum.planificacao = planificacao.getText();

		Main.LISTA.set(sumIndex, sum);
	}
}