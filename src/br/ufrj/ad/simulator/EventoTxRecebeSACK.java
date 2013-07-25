package br.ufrj.ad.simulator;

/**
 * Representa o momento em que o Tx recebe o SACK de um pacote enviado.
 * 
 * @author André Ramos, Wellignton Mascena, featuring Vitor Maia
 * 
 */
public class EventoTxRecebeSACK extends Evento {

	public EventoTxRecebeSACK(double tempoDeOcorrencia) {
		super(tempoDeOcorrencia);
	}

}
