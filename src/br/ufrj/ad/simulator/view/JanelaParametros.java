package br.ufrj.ad.simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JanelaParametros extends JFrame {

	public JanelaParametros() {
		super("Parâmetros da Simulação");
		iniciarComponetes();
	}

	private void iniciarComponetes() {
		
		this.setLayout(new GridLayout(0, 4));

		/*
		 * Inicializando componentes.
		 */
		this.add(new JLabel("Cs (bps):"));
		this.add(new JTextField("1000000000"));
		this.add(new JLabel("Cg (bps):"));
		this.add(new JTextField("1000000"));
		this.add(new JLabel("TP1 (ms):"));
		this.add(new JTextField("100"));
		this.add(new JLabel("TP2 (ms):"));
		this.add(new JTextField("50"));
		this.add(new JLabel("Estações do Grupo 1:"));
		this.add(new JTextField("10"));
		this.add(new JLabel("Estações do Grupo 2:"));
		this.add(new JTextField("50"));

		// Display the window.
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JanelaParametros();
			}
		});
	}
}
