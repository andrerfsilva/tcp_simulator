package br.ufrj.ad.simulator.view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class JanelaPrincipal extends JFrame {

	public JanelaPrincipal(){
		super("Simulador TCP - 2013/1");
		iniciarComponetes();
	}

	private void iniciarComponetes() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.setExtendedState(MAXIMIZED_BOTH);

		// Create the menu bar.
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItemParametrosSimulacao, menuItemIniciarSimulacao, menuItemSair;

		menuBar = new JMenuBar();

		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);

		menuItemParametrosSimulacao = new JMenuItem(
				"Parâmetros de Entrada da Simulação", KeyEvent.VK_P);
		menu.add(menuItemParametrosSimulacao);

		menuItemIniciarSimulacao = new JMenuItem("Iniciar Simulação",
				KeyEvent.VK_S);
		menu.add(menuItemIniciarSimulacao);
		
		menu.addSeparator();
		
		menuItemSair =  new JMenuItem("Sair");
		menu.add(menuItemSair);
		

		menuBar.setOpaque(true);
		this.setJMenuBar(menuBar);

		// outros componentes...

		// Display the window.
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JanelaPrincipal();
			}
		});
	}
}
