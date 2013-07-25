package br.ufrj.ad.simulator;

/**
 * Evento de envio de pacotes no roteador. Representa o fim da transmissão, ou
 * seja, o pacote chegou ao destino e pode ser removido do buffer do roteador.
 * 
 * @author André Ramos, Wellington Mascena
 * 
 */
public class EventoRoteadorTerminaEnvio extends Evento {

	public EventoRoteadorTerminaEnvio(double tempo) {
		super(tempo);
	}

}
