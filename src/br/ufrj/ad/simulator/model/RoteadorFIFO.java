package br.ufrj.ad.simulator.model;

/**
 * Um rotedoador com disciplina First Come First Served!
 * 
 * @author André Ramos, Felipe Teixeira, Wellington Mascena
 * 
 */
public class RoteadorFIFO extends Roteador {

	@Override
	public boolean receberPacote(Pacote pacote, double tempoAtual) {
		// OBS: o tempo atual é irrelevante para FIFO!
		if (buffer.size() < getTamanhoBuffer()) {
			buffer.add(pacote);
			return true;
		}
		return false;
	}

}
