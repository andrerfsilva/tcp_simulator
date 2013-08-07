package br.ufrj.ad.simulator.eventos;

import br.ufrj.ad.simulator.models.SACK;

/**
 * Representa o momento em que o Tx recebe o SACK de um pacote enviado.
 * 
 * @author André Ramos, Wellignton Mascena, featuring Vitor Maia
 * 
 */
public class EventoTxTCPRecebeSACK extends Evento {

	private SACK sack;

	public EventoTxTCPRecebeSACK(double tempoDeOcorrencia, SACK sack) {
		super(tempoDeOcorrencia);

		this.sack = sack;
	}

	public SACK getSACK() {
		return sack;
	}

}
