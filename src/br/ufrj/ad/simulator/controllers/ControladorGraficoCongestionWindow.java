package br.ufrj.ad.simulator.controllers;

import java.io.IOException;

import javax.swing.JFrame;

import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.views.JanelaGraficoCongestionWindow;

/**
 * Controlador para a JanelaGraficoCongestionWindon.
 * 
 * @author André Ramos, Felipe Teixeira, Wellington Mascena
 * 
 */
public class ControladorGraficoCongestionWindow {

	private JanelaGraficoCongestionWindow janelaGraficoCongestionWindow;
	private Simulador simulador;

	public ControladorGraficoCongestionWindow() throws IOException {
		this.simulador = new Simulador();
		janelaGraficoCongestionWindow = new JanelaGraficoCongestionWindow(
				simulador.getAmostrasCwndPorMSSxTempo(), simulador.getSeed());
	}

	public JanelaGraficoCongestionWindow getJanelaGraficoCongestionWindow() {
		return janelaGraficoCongestionWindow;
	}

	public static void main(String[] args) throws IOException {
		ControladorGraficoCongestionWindow controlador = new ControladorGraficoCongestionWindow();
		controlador.getJanelaGraficoCongestionWindow()
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
