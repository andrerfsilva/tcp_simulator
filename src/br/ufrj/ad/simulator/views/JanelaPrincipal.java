package br.ufrj.ad.simulator.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Menu principal do simulador.
 * 
 * @author André Ramos
 * 
 */
@SuppressWarnings("serial")
public class JanelaPrincipal extends JFrame {

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItemConfigurarParametrosSimulacao;
	private JMenuItem menuItemGerarGraficoCwndPorMSSxTempo;
	private JMenuItem menuItemGerarGraficoVazaoMediaxEventos;
	private JMenuItem menuItemIniciarSimulacao;
	private JMenuItem menuItemSair;

	public JanelaPrincipal() {
		super("Simulador TCP - 2013/1");
		iniciarComponentes();
	}

	public JMenuItem getMenuItemGerarGraficoVazaoMediaxTempo() {
		return menuItemGerarGraficoVazaoMediaxEventos;
	}

	private void iniciarComponentes() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.setExtendedState(MAXIMIZED_BOTH);

		// Create the menu bar.

		menuBar = new JMenuBar();

		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);

		menuItemConfigurarParametrosSimulacao = new JMenuItem(
				"Configurar Parâmetros da Simulação", KeyEvent.VK_P);
		menuItemConfigurarParametrosSimulacao
				.setActionCommand("ConfigurarParametros");
		menu.add(menuItemConfigurarParametrosSimulacao);

		menuItemGerarGraficoCwndPorMSSxTempo = new JMenuItem(
				"Gerar Gráfico: cwnd/MSS x Tempo", KeyEvent.VK_G);
		menuItemGerarGraficoCwndPorMSSxTempo.setActionCommand("GerarGrafico");
		menu.add(menuItemGerarGraficoCwndPorMSSxTempo);

		menuItemGerarGraficoVazaoMediaxEventos = new JMenuItem(
				"Gerar Gráfico: Vazão Média x Número de Eventos");
		menuItemGerarGraficoVazaoMediaxEventos
				.setActionCommand("GerarGraficoVazaoMedia");
		menu.add(menuItemGerarGraficoVazaoMediaxEventos);

		menuItemIniciarSimulacao = new JMenuItem("Iniciar Simulação",
				KeyEvent.VK_S);
		menuItemIniciarSimulacao.setActionCommand("IniciarSimulacao");
		menu.add(menuItemIniciarSimulacao);

		menu.addSeparator();

		menuItemSair = new JMenuItem("Sair");
		menuItemSair.setActionCommand("Sair");
		menu.add(menuItemSair);

		menuBar.setOpaque(true);
		this.setJMenuBar(menuBar);

		// outros componentes...

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

	public JMenuItem getMenuItemGerarGraficoCwndPorMSSxTempo() {
		return menuItemGerarGraficoCwndPorMSSxTempo;
	}

	public JMenuItem getMenuItemConfigurarParametrosSimulacao() {
		return menuItemConfigurarParametrosSimulacao;
	}

	public JMenuItem getMenuItemIniciarSimulacao() {
		return menuItemIniciarSimulacao;
	}

	public JMenuItem getMenuItemSair() {
		return menuItemSair;
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
