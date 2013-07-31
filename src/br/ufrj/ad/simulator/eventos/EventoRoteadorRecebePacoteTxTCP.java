package br.ufrj.ad.simulator.eventos;

/**
 * Representa o momento quando chega um pacote no roteador vindo de um TxTCP.
 * 
 * @author André Ramos, Wellington Mascena, featuring Vitor Maia
 * 
 */
public class EventoRoteadorRecebePacoteTxTCP extends Evento {

	private int txTCP;

	public EventoRoteadorRecebePacoteTxTCP(double tempoDeOcorrencia, int txTCP) {
		super(tempoDeOcorrencia);
		this.txTCP = txTCP;
	}

	/**
	 * Retorna o número da conexão TCP que originou o evento.
	 * 
	 * @return o número da conexão TCP que originou o evento
	 */
	public int getTxTCP() {
		return this.txTCP;
	}

}
