package br.ufrj.ad.simulator.views;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class JanelaResultadoSimulacao extends JFrame {

	private JTextArea textAreaResultado;

	public JanelaResultadoSimulacao() {
		super("Resultado da Simulação");
		this.setPreferredSize(new Dimension(800, 600));

		this.textAreaResultado = new JTextArea();
		this.add(textAreaResultado);

		this.pack();
		this.setVisible(true);
	}

	public JTextArea getTextAreaResultado() {
		return textAreaResultado;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JanelaResultadoSimulacao();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

}
