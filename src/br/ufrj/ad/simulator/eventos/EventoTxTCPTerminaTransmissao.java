package br.ufrj.ad.simulator.eventos;

/**
 * Indica que o TxTCP terminou de transmitir o pacote e pode iniciar uma nova
 * transmissão se a cwnd assim o permitir.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class EventoTxTCPTerminaTransmissao extends Evento {

	public EventoTxTCPTerminaTransmissao(double tempoDeOcorrencia) {
		super(tempoDeOcorrencia);
	}

}
