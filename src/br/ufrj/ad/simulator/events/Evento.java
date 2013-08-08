package br.ufrj.ad.simulator.events;

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

	/**
	 * Construtor do Evento. O tempo de ocorrência deve ser passado como
	 * parâmetro de construção, e não pode ser alterado depois.
	 * 
	 * @param tempoDeOcorrencia
	 *            indica quando o evento ocorre no tempo simulado
	 */
	public Evento(double tempoDeOcorrencia) {
		this.tempoDeOcorrencia = tempoDeOcorrencia;
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
