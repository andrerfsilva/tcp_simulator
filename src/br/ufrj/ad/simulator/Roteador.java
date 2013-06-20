package br.ufrj.ad.simulator;

import java.util.ArrayList;

/**
 * A base dos roteadores implementados nesse trabalho de simualção. As
 * subclasses esperadas são roteadores que implementam as disciplinas FIFO e
 * RED.
 * 
 * @author André Ramos
 * 
 */
public abstract class Roteador {

	private int tamanhoBuffer;
	private ArrayList<Pacote> buffer;
	private RxTCP[] receptores; // lista de RxTCP conectados ao roteador

	public Roteador() {
		tamanhoBuffer = 0;
		buffer = new ArrayList<Pacote>();
	}

	/**
	 * Subclasses devem implementar esse método. Adiciona pacote ao buffer do
	 * roteador levando em consideração a política de descarte. A função retorna
	 * um boolean indicando se o pacote foi descartado ou não.
	 * 
	 * @param p
	 *            pacote que chegou ao roteador
	 * @return se o pacote foi descartado ou não
	 */
	public abstract boolean receberPacote(Pacote p);

	/**
	 * Envia próximo pacote ao seu destino e o remove do buffer do roteador.
	 */
	public void enviarProximoPacote() {
		Pacote p = buffer.get(0);
		if (p.getDestino >= 0) {
			receptores[p.getDestino()].receberPacote(p);
		}
		buffer.remove(0);
	}

	public int getTamanhoBuffer() {
		return tamanhoBuffer;
	}

	public void setTamanhoBuffer(int tamanhoBuffer) {
		this.tamanhoBuffer = tamanhoBuffer;
	}

	public RxTCP[] getReceptores() {
		return receptores;
	}

	public void setReceptores(RxTCP[] receptores) {
		this.receptores = receptores;
	}

}
