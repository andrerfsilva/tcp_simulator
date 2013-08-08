package br.ufrj.ad.simulator.controllers;

import java.io.IOException;

import javax.swing.JFrame;

import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.views.JanelaGraficoVazaoEventos;

public class ControladorGraficoVazaoTempo {

	private JanelaGraficoVazaoEventos janelaGraficoVazaoEventos;
	private Simulador simulador;

	public ControladorGraficoVazaoTempo() throws IOException {
		this.simulador = new Simulador();
		janelaGraficoVazaoEventos = new JanelaGraficoVazaoEventos(
				simulador.getAmostrasVazaoxTempo());
	}

	public JanelaGraficoVazaoEventos getJanelaGraficoVazaoTempo() {
		return janelaGraficoVazaoEventos;
	}

	public static void main(String[] args) throws IOException {
		ControladorGraficoVazaoTempo controlador = new ControladorGraficoVazaoTempo();
		controlador.getJanelaGraficoVazaoTempo().setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);
	}

}
