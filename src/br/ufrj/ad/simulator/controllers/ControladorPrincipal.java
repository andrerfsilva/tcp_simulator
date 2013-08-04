package br.ufrj.ad.simulator.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import br.ufrj.ad.simulator.views.JanelaPrincipal;

/**
 * Controla os eventos da janela principal do simulador.
 * 
 * @author André Ramos
 * 
 */
public class ControladorPrincipal implements ActionListener {

	private JanelaPrincipal janelaPrincipal;

	public ControladorPrincipal() {
		janelaPrincipal = new JanelaPrincipal();

		janelaPrincipal.getMenuItemConfigurarParametrosSimulacao()
				.addActionListener(this);
		janelaPrincipal.getMenuItemGerarGraficoCwndPorMSSxTempo()
				.addActionListener(this);
		janelaPrincipal.getMenuItemIniciarSimulacao().addActionListener(this);
		janelaPrincipal.getMenuItemSair().addActionListener(this);
		janelaPrincipal.getMenuItemGerarGraficoVazaoMediaxTempo()
				.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {

			if ("Sair".equals(e.getActionCommand())) {
				janelaPrincipal.close();
			} else if ("ConfigurarParametros".equals(e.getActionCommand())) {
				new ControladorParametros();
			} else if ("IniciarSimulacao".equals(e.getActionCommand())) {
				// TODO: Fazer isso logo!
			} else if ("GerarGrafico".equals(e.getActionCommand())) {
				new ControladorGraficoCongestionWindow();
			} else if ("GerarGraficoVazaoMedia".equals(e.getActionCommand())) {
				new ControladorGraficoVazaoTempo();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ControladorPrincipal();
	}
}
