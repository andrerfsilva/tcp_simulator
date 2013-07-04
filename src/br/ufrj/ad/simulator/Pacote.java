package br.ufrj.ad.simulator;

/**
 * Informações relevantes de um pacote. Importante: todo pacote tem tamanho MSS
 * = 1500 bytes.
 * 
 * @author André Ramos
 * 
 */
public class Pacote {

	private int destino;

	public Pacote() {
		this.destino = -1;
	}

	/**
	 * Destino representa qual dos RxTCP deve receber esse pacote. Para
	 * representar o tráfego de fundo, usaremos pacotes com destino negativo, ou
	 * seja, eles não vão para nenhum RxTCP.
	 * 
	 * @param destino
	 *            número do RxTCP que receberá esse pacote
	 */
	public void setDestino(int destino) {
		this.destino = destino;
	}

	public int getDestino() {
		return this.destino;
	}

}
