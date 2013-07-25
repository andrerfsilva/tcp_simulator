package br.ufrj.ad.simulator;

/**
 * Informações relevantes de um pacote. Importante: todo pacote tem tamanho MSS
 * = 1500 bytes.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class Pacote {

	private int destino;

	/**
	 * O número de sequência do primeiro byte do pacote.
	 */
	private long byteInicial;

	/**
	 * O número de sequência do último byte do pacote.
	 */
	private long byteFinal;

	public Pacote() {
		this.destino = -1;
		this.byteInicial = 0;
		this.byteFinal = 0;
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

	public long getByteInicial() {
		return byteInicial;
	}

	public void setByteInicialEFinal(long byteInicial, long byteFinal)
			throws InvalidPackageException {

		if (byteInicial > byteFinal) {
			throw new InvalidPackageException();
		}

		this.byteInicial = byteInicial;
		this.byteFinal = byteFinal;
	}

	public long getByteFinal() {
		return byteFinal;
	}

	/**
	 * Informa se o pacote é tráfego de fundo.
	 * 
	 * @return se o pacote é tráfego de fundo
	 */
	public boolean isTrafegoDeFundo() {
		return destino < 0;
	}

	public long getTamanho() {
		return byteFinal - byteInicial + 1;
	}

}
