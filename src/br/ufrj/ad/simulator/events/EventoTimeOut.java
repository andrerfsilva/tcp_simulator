package br.ufrj.ad.simulator.events;

/**
 * O evento de timeout faz o TxTCP mudar drasticamente seu comportamento.
 * 
 * @author André Ramos
 * 
 */
public class EventoTimeOut extends Evento {

	private int txTCP;

	public EventoTimeOut(double tempoDeOcorrencia, int txTCP) {
		super(tempoDeOcorrencia);
		this.txTCP = txTCP;
	}

	/**
	 * Retorna o número da conexão TCP que deu origem a esse evento.
	 * 
	 * @return número da conexão TCP que deu time-out
	 */
	public int getTxTCP() {
		return txTCP;
	}

}
