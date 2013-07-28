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
		janelaPrincipal.getMenuItemIniciarSimulacao().addActionListener(this);
		janelaPrincipal.getMenuItemSair().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if ("Sair".equals(e.getActionCommand())) {
			janelaPrincipal.close();
		} else if ("ConfigurarParametros".equals(e.getActionCommand())) {
			try {
				new ControladorParametros();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if ("IniciarSimulacao".equals(e.getActionCommand())) {
			// TODO: Fazer isso logo!
		}

	}

	public static void main(String[] args) {
		new ControladorPrincipal();
	}
}
