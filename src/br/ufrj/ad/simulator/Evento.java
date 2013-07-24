package br.ufrj.ad.simulator;

/**
 * Todos os eventos do sistema devem herdar dessa classe.
 * 
 * @author André Ramos
 * 
 */
public class Evento implements Comparable<Evento> {

	/**
	 * Representa quando o evento ocorre no tempo simulado (em milisegundos).
	 */
	private final double tempo;

	public Evento(double tempo) {
		this.tempo = tempo;
	}

	/**
	 * Usado para ordenar os eventos na lista de eventos do simulador em função
	 * de quando eles devem ocorrer.
	 * 
	 * Atenção: Esse compração não é consistente com o método equals().
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Evento o) {

		if (this.getTempo() < o.getTempo()) {
			return -1;
		} else if (this.getTempo() > o.getTempo()) {
			return 1;
		}

		return 0;
	}

	public double getTempo() {
		return tempo;
	}

}
