package sandrohc.gui;

import sandrohc.Sumario;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static sandrohc.Main.LISTA;

public class GuiMain extends JFrame {
    private byte[] maxDias = {31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30};

    private JPanel panel;
    private JList<String> sumarios;
    private JComboBox licoesField;
    private JLabel licoes;
    private JLabel data;
    private JComboBox<String> dataDia;
    private JComboBox<String> dataMes;
    private JComboBox<String> dataAno;
    private JPanel curr;
    private JTextArea planificacao;

    private Sumario sum;
    private int sumIndex;

    public GuiMain(int index) {
        this.sum = LISTA.size() >= index && LISTA.get(index) == null ? LISTA.get(0) : LISTA.get(index);
        this.sumIndex = index;

        populate();

        sumarios.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                atualizarSum(sumarios.getSelectedIndex());
            }
        });

        // Mostra a GUI no final do cosntrutor
        run();
    }

    /**
     * Cria a GUI
     */
    public void run() {
        JFrame frame = new JFrame("Sumários o' Plenty");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Atualiza a informação presente nos componentes da GUI
     */
    public void populate() {
        //licoesField.setText(Arrays.toString(sum.licoes).replace("[", "").replace("]", ""));
        planificacao.setText(sum.planificacao);

        //sumarios = new JList<>();
        sumarios.setSelectedIndex(sumIndex);

        atualizarData();
        dataDia.setSelectedIndex(dataDia.getSelectedIndex() != -1 ? dataDia.getSelectedIndex() : 0);
        dataMes.setSelectedIndex(dataMes.getSelectedIndex() != -1 ? dataMes.getSelectedIndex() : 0);
        dataAno.setSelectedIndex(dataAno.getSelectedIndex() != -1 ? dataAno.getSelectedIndex() : 0);
    }

    /**
     * Cria os componentes básicos da GUI
     */
    private void createUIComponents() {
        sumarios = new JList<>(gerarSumLista());

        //DefaultListModel<String> listModel = new DefaultListModel<>();
        //for(String str : gerarSumLista())
        //    listModel.addElement(str);
        //sumarios.setModel(listModel);

        atualizarData();
    }

    private void atualizarData() {
        int mesAct = dataMes != null && dataMes.getSelectedIndex() != -1 ? dataMes.getSelectedIndex() : obterMes() - 1;

        dataDia = new JComboBox<>(gerarArray(1, maxDias[mesAct]));
        dataMes = new JComboBox<>(gerarArray(1, 12));
        dataAno = new JComboBox<>(gerarArray(2014, 2015));
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

        for (int i = 0; i <= size; i++)
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
        for (byte i = 0; i < LISTA.size(); i++) {
            Sumario sum = LISTA.get(i);
            lista[i] = Arrays.toString(sum.licoes).replace("[", "").replace("]", "") + " - " + obterData(sum);
        }
        System.out.println("Lista : " + Arrays.toString(lista));
        return lista;
    }

    private int obterMes() {
        Calendar cal = Calendar.getInstance();
        if (sum != null)
            cal.setTime(new Date(sum.data));
        else
            System.out.println("Sumário não contém uma data válida, usando a data atual.");
        return cal.get(Calendar.MONTH) + 1;
    }

    private String obterData(Sumario sum) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        if (sum != null)
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
        if (index == -1 || index > LISTA.size())
            return;

        System.out.println("Sumário antigo : " + Arrays.toString(sum.licoes));

        sumIndex = index;
        sum = LISTA.get(index);

        System.out.println("Sumário novo : " + Arrays.toString(sum.licoes));

        populate();
        atualizarData();
    }

    /**
     * Remove o sumário na posição passada nos argumentos
     *
     * @param index posição na lista de sumários
     */
    private void removerSum(int index) {
        LISTA.remove(index);
        sumarios.remove(index);

        atualizarSum(index);
    }
}