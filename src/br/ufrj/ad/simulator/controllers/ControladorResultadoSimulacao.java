package br.ufrj.ad.simulator.controllers;

import java.io.IOException;

import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.views.JanelaResultadoSimulacao;

public class ControladorResultadoSimulacao {

	private JanelaResultadoSimulacao janelaResultadoSimulacao;
	private Simulador simulador;

	public ControladorResultadoSimulacao() throws IOException {
		this.simulador = new Simulador();
		this.janelaResultadoSimulacao = new JanelaResultadoSimulacao();
		this.janelaResultadoSimulacao.getTextAreaResultado().setText(
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
