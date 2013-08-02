package br.ufrj.ad.simulator.models;

import br.ufrj.ad.simulator.eventos.EventoTimeOut;
import br.ufrj.ad.simulator.exceptions.InvalidPackageException;

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

	private EventoTimeOut eventoTimeOut;

	/**
	 * Armazena quando o pacote começou a ser enviado do TxTCP correspondente no
	 * tempo de simulação (em milisegundos). Essa variável será usada para
	 * calcular o RTT instantâneo e estimar o RTO.
	 */
	private double tempoDeEnvio;

	public Pacote() {
		this.destino = -1; // por default é tráfego de fundo
		this.byteInicial = 0;
		this.byteFinal = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {

		if (o instanceof Pacote) {
			Pacote opacote = (Pacote) o;
			return (this.byteInicial == opacote.byteInicial)
					&& (this.byteFinal == opacote.byteFinal)
					&& (this.destino == opacote.destino);
		}

		return false;
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

	/**
	 * Destino representa qual dos RxTCP deve receber esse pacote. Para
	 * representar o tráfego de fundo, usaremos pacotes com destino negativo, ou
	 * seja, eles não vão para nenhum RxTCP.
	 * 
	 * @return número do RxTCP que reberá esse pacote
	 */
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

	/**
	 * Tamanho do pacote (em bytes).
	 * 
	 * @return tamanho do pacote (em bytes)
	 */
	public long getTamanho() {
		return byteFinal - byteInicial + 1;
	}

	public void setEventoTimeOut(EventoTimeOut eTimeOut) {
		this.eventoTimeOut = eTimeOut;
	}

	public EventoTimeOut getEventoTimeOut() {
		return eventoTimeOut;
	}

	public double getTempoDeEnvio() {
		return tempoDeEnvio;
	}

	public void setTempoDeEnvio(double tempoDeEnvio) {
		this.tempoDeEnvio = tempoDeEnvio;
	}

	public String toString() {
		return "P" + byteInicial / Parametros.mss + "," + destino + " <"
				+ byteInicial + ", " + byteFinal + ">";
	}
}
