package br.ufrj.ad.simulator.eventos;

/**
 * Indica que o TxTCP terminou de transmitir o pacote e pode iniciar uma nova
 * transmissão se a cwnd assim o permitir.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class EventoTxTCPTerminaTransmissao extends Evento {

	private int txTCP;

	/**
	 * Inicializa o evento de término de transmissão TCP.
	 * 
	 * @param tempoDeOcorrencia
	 *            tempo quando o evento ocorre
	 * @param txTCP
	 *            sessão TCP que originou o evento
	 */
	public EventoTxTCPTerminaTransmissao(double tempoDeOcorrencia, int txTCP) {
		super(tempoDeOcorrencia);
		this.txTCP = txTCP;
	}

	/**
	 * Sessão TCP que originou o evento.
	 * 
	 * @return sessão TCP que originou o evento
	 */
	public int getTxTCP() {
		return txTCP;
	}

}
