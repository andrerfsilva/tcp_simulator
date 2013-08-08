package br.ufrj.ad.simulator.events;

/**
 * Representa uma chegada Poisson no roteador.
 * 
 * @author André Ramos, Wellignton Mascena
 * 
 */
public class EventoRoteadorRecebeTrafegoDeFundo extends Evento {

	public EventoRoteadorRecebeTrafegoDeFundo(double tempo) {
		super(tempo);
	}

}
