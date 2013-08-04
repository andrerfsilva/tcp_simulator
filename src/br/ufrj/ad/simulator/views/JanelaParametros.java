package br.ufrj.ad.simulator.views;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Janela usada para configurar os parâmetros de entrada do simulador.
 * 
 * @author André Ramos
 * 
 */
@SuppressWarnings("serial")
public class JanelaParametros extends JFrame {

	private JTextField textFieldTamanhoBufferRoteador;
	private JComboBox<String> comboBoxDisciplinaRoteador;
	private JTextField textFieldCs;
	private JTextField textFieldCg;
	private JTextField textFieldTP1;
	private JTextField textFieldTP2;
	private JTextField textFieldTPACK1;
	private JTextField textFieldTPACK2;
	private JTextField textFieldEstacoesGrupo1;
	private JTextField textFieldEstacoesGrupo2;
	private JTextField textFieldMediaPacotesPorRajada;
	private JTextField textFieldTempoMedioEntreRajadas;
	private JTextField textFieldNumeroEventosPorRodada;
	private JTextField textFieldNumeroAmostrasCwndGrafico;
	private JCheckBox checkBoxTrafegoFundo;
	private JButton buttonSalvar;
	private JButton buttonCancelar;

	public JanelaParametros() {
		super("Parâmetros da Simulação");
		iniciarComponetes();
	}

	private void iniciarComponetes() {

		this.setLayout(new GridLayout(0, 2));

		/*
		 * Inicializando componentes.
		 */
		this.add(new JLabel("Tamanho Buffer Roteador (pacotes):"));
		this.textFieldTamanhoBufferRoteador = new JTextField();
		this.add(textFieldTamanhoBufferRoteador);

		this.add(new JLabel("Disciplina do Roteador:"));
		String[] roteadores = new String[2];
		roteadores[0] = "FIFO";
		roteadores[1] = "RED";
		this.comboBoxDisciplinaRoteador = new JComboBox<String>(roteadores);
		this.add(comboBoxDisciplinaRoteador);

		this.add(new JLabel("Cs (bps):"));
		this.textFieldCs = new JTextField();
		this.add(textFieldCs);

		this.add(new JLabel("Cg (bps):"));
		this.textFieldCg = new JTextField();
		this.add(textFieldCg);

		this.add(new JLabel("TP1 (ms):"));
		this.textFieldTP1 = new JTextField();
		this.add(textFieldTP1);

		this.add(new JLabel("TP2 (ms):"));
		this.textFieldTP2 = new JTextField();
		this.add(textFieldTP2);

		this.add(new JLabel("TPACK1 (ms):"));
		this.textFieldTPACK1 = new JTextField();
		this.add(textFieldTPACK1);

		this.add(new JLabel("TPACK2 (ms):"));
		this.textFieldTPACK2 = new JTextField();
		this.add(textFieldTPACK2);

		this.add(new JLabel("Estações do Grupo 1:"));
		this.textFieldEstacoesGrupo1 = new JTextField();
		this.add(textFieldEstacoesGrupo1);

		this.add(new JLabel("Estações do Grupo 2:"));
		this.textFieldEstacoesGrupo2 = new JTextField();
		this.add(textFieldEstacoesGrupo2);

		this.add(new JLabel("Média de Pacotes/Rajada TF:"));
		this.textFieldMediaPacotesPorRajada = new JTextField();
		this.add(textFieldMediaPacotesPorRajada);

		this.add(new JLabel("Tempo Médio Entre Rajadas TF (ms):"));
		this.textFieldTempoMedioEntreRajadas = new JTextField();
		this.add(textFieldTempoMedioEntreRajadas);

		this.add(new JLabel());

		this.checkBoxTrafegoFundo = new JCheckBox("Habilitar Tráfego de Fundo");
		this.add(checkBoxTrafegoFundo);

		this.add(new JLabel("Número de Eventos Por Rodada:"));
		this.textFieldNumeroEventosPorRodada = new JTextField();
		this.add(textFieldNumeroEventosPorRodada);

		this.add(new JLabel("Número de Amostras de cwnd no Gráfico:"));
		this.textFieldNumeroAmostrasCwndGrafico = new JTextField();
		this.add(textFieldNumeroAmostrasCwndGrafico);

		buttonSalvar = new JButton("Salvar");
		buttonSalvar.setActionCommand("Salvar");
		this.add(buttonSalvar);

		buttonCancelar = new JButton("Cancelar");
		buttonCancelar.setActionCommand("Cancelar");
		this.add(buttonCancelar);

		// Display the window.
		this.pack();
		this.setVisible(true);
	}

	public void close() {

		WindowEvent winClosingEvent = new WindowEvent(this,
				WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue()
				.postEvent(winClosingEvent);

	}

	public JTextField getTextFieldNumeroAmostrasCwndGrafico() {
		return textFieldNumeroAmostrasCwndGrafico;
	}

	public JTextField getTextFieldTamanhoBufferRoteador() {
		return textFieldTamanhoBufferRoteador;
	}

	public JComboBox<String> getComboBoxTamanhoBufferRoteador() {
		return comboBoxDisciplinaRoteador;
	}

	public JTextField getTextFieldCs() {
		return textFieldCs;
	}

	public JTextField getTextFieldCg() {
		return textFieldCg;
	}

	public JTextField getTextFieldTP1() {
		return textFieldTP1;
	}

	public JTextField getTextFieldTP2() {
		return textFieldTP2;
	}

	public JTextField getTextFieldTPACK1() {
		return textFieldTPACK1;
	}

	public JTextField getTextFieldTPACK2() {
		return textFieldTPACK2;
	}

	public JTextField getTextFieldEstacoesGrupo1() {
		return textFieldEstacoesGrupo1;
	}

	public JTextField getTextFieldEstacoesGrupo2() {
		return textFieldEstacoesGrupo2;
	}

	public JTextField getTextFieldMediaPacotesPorRajada() {
		return textFieldMediaPacotesPorRajada;
	}

	public JTextField getTextFieldTempoMedioEntreRajadas() {
		return textFieldTempoMedioEntreRajadas;
	}

	public JButton getButtonSalvar() {
		return buttonSalvar;
	}

	public JButton getButtonCancelar() {
		return buttonCancelar;
	}

	public JTextField getTextFieldNumeroEventosPorRodada() {
		return textFieldNumeroEventosPorRodada;
	}

	public JCheckBox getCheckBoxTrafegoFundo() {
		return checkBoxTrafegoFundo;
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JanelaParametros();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
