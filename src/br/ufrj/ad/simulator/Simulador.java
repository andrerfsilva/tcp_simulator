package br.ufrj.ad.simulator;

import java.util.PriorityQueue;

/**
 * Essa classe gerencia os eventos, as atualizações no modelo do sistema
 * simulado e a coleta de estatísticas de interesse.
 * 
 * @author André Ramos
 * 
 */
public class Simulador {

	private Rede rede; // modelo do sistema a ser simulado
	private PriorityQueue<Evento> filaEventos; // fila de eventos

	public Simulador() {
		rede = new Rede();
	}

	public void simular() {

		// TODO: estimar fase transiente!

		while (filaEventos.size() > 0 && !estatisticasSatisfatorias()) {
			Evento e = filaEventos.poll();
			tratarEvento(e);
		}

		// TODO coletar estatísticas
	}

	private boolean estatisticasSatisfatorias() {
		// TODO escrever a condição de parada do loop de simulação
		return false;
	}

	private void tratarEvento(Evento e) {
		// TODO definir eventos e como eles alteram o estado do sistema

	}
}
