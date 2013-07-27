package br.ufrj.ad.simulator.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JanelaParametros extends JFrame {

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
		this.add(new JTextField("40"));
		this.add(new JLabel("Disciplina do Roteador:"));
		String[] roteadores = new String[2];
		roteadores[0] = "FIFO";
		roteadores[1] = "RED";
		this.add(new JComboBox<String>(roteadores));
		this.add(new JLabel("Cs (bps):"));
		this.add(new JTextField("1E9"));
		this.add(new JLabel("Cg (bps):"));
		this.add(new JTextField("10E6"));
		this.add(new JLabel("TP1 (ms):"));
		this.add(new JTextField("100"));
		this.add(new JLabel("TP2 (ms):"));
		this.add(new JTextField("50"));
		this.add(new JLabel("Estações do Grupo 1:"));
		this.add(new JTextField("10"));
		this.add(new JLabel("Estações do Grupo 2:"));
		this.add(new JTextField("50"));
		this.add(new JLabel("Média de Pacotes/Rajada TF:"));
		this.add(new JTextField("10"));
		this.add(new JLabel("Tempo Médio Entre Rajadas TF (ms):"));
		this.add(new JTextField("24"));
		this.add(new JButton("Salvar"));
		this.add(new JButton("Cancelar"));

		// Display the window.
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JanelaParametros();
			}
		});
	}
}
