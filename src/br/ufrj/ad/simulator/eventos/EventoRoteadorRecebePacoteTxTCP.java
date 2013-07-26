package br.ufrj.ad.simulator.eventos;

/**
 * Representa o momento quando chega um pacote no roteador vindo de um TxTCP.
 * 
 * @author André Ramos, Wellington Mascena, featuring Vitor Maia
 * 
 */
public class EventoRoteadorRecebePacoteTxTCP extends Evento {

	public EventoRoteadorRecebePacoteTxTCP(double tempoDeOcorrencia) {
		super(tempoDeOcorrencia);
	}

}
