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
	private final double tempoDeOcorrencia;

	public Evento(double tempo) {
		this.tempoDeOcorrencia = tempo;
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

		if (this.getTempoDeOcorrencia() < o.getTempoDeOcorrencia()) {
			return -1;
		} else if (this.getTempoDeOcorrencia() > o.getTempoDeOcorrencia()) {
			return 1;
		}

		return 0;
	}

	/**
	 * Representa quando o evento ocorre no tempo simulado (em milisegundos).
	 * 
	 * @return
	 */
	public double getTempoDeOcorrencia() {
		return tempoDeOcorrencia;
	}

}
