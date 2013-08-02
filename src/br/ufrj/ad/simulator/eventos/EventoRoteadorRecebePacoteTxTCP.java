package br.ufrj.ad.simulator.eventos;

import br.ufrj.ad.simulator.models.Pacote;

/**
 * Representa o momento quando chega um pacote no roteador vindo de um TxTCP.
 * 
 * @author André Ramos, Wellington Mascena, featuring Vitor Maia
 * 
 */
public class EventoRoteadorRecebePacoteTxTCP extends Evento {

	private Pacote pacote;

	public EventoRoteadorRecebePacoteTxTCP(double tempoDeOcorrencia,
			Pacote pacote) {
		super(tempoDeOcorrencia);
		this.pacote = pacote;
	}

	public Pacote getPacote() {
		return pacote;
	}

	/**
	 * Retorna o número da conexão TCP que originou o evento.
	 * 
	 * @return o número da conexão TCP que originou o evento
	 */
	public int getTxTCP() {
		return this.pacote.getDestino();
	}

}
