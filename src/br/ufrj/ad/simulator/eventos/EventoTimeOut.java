package br.ufrj.ad.simulator.eventos;

/**
 * O evento de timeout faz o TxTCP mudar drasticamente seu comportamento.
 * 
 * @author André Ramos
 * 
 */
public class EventoTimeOut extends Evento {

	public EventoTimeOut(double tempoDeOcorrencia) {
		super(tempoDeOcorrencia);
	}

}
