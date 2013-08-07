package br.ufrj.ad.simulator.controllers;

import java.io.IOException;

import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.views.JanelaResultadoSimulacaoHTML;;

public class ControladorResultadoSimulacao {

	private JanelaResultadoSimulacaoHTML janelaResultadoSimulacaoHTML;
	private Simulador simulador;

	public ControladorResultadoSimulacao() throws IOException {
		this.simulador = new Simulador();
		this.janelaResultadoSimulacaoHTML = new JanelaResultadoSimulacaoHTML();
		this.janelaResultadoSimulacaoHTML.insertStringHTML(
				simulador.resultadoSimulacao());
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		new ControladorResultadoSimulacao();

	}

}
