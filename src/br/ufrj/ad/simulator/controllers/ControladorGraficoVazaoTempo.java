package br.ufrj.ad.simulator.controllers;

import java.io.IOException;

import javax.swing.JFrame;

import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.views.JanelaGraficoVazaoTempo;

public class ControladorGraficoVazaoTempo {

	private JanelaGraficoVazaoTempo janelaGraficoVazaoTempo;
	private Simulador simulador;

	public ControladorGraficoVazaoTempo() throws IOException {
		this.simulador = new Simulador();
		janelaGraficoVazaoTempo = new JanelaGraficoVazaoTempo(
				simulador.getAmostrasVazaoxTempo());
	}

	public JanelaGraficoVazaoTempo getJanelaGraficoVazaoTempo() {
		return janelaGraficoVazaoTempo;
	}

	public static void main(String[] args) throws IOException {
		ControladorGraficoVazaoTempo controlador = new ControladorGraficoVazaoTempo();
		controlador.getJanelaGraficoVazaoTempo().setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);
	}

}
