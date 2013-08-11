package br.ufrj.ad.simulator.controllers;

import java.io.IOException;

import javax.swing.JFrame;

import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.views.JanelaGraficoVazaoEventos;

public class ControladorGraficoVazaoEventos {

	private JanelaGraficoVazaoEventos janelaGraficoVazaoEventos;
	private Simulador simulador;

	public ControladorGraficoVazaoEventos() throws IOException {
		this.simulador = new Simulador();
		janelaGraficoVazaoEventos = new JanelaGraficoVazaoEventos(
				simulador.getAmostrasVazaoXNumeroEventos(), simulador.getSeed());
	}

	public static void main(String[] args) throws IOException {
		ControladorGraficoVazaoEventos controlador = new ControladorGraficoVazaoEventos();
		controlador.janelaGraficoVazaoEventos
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
