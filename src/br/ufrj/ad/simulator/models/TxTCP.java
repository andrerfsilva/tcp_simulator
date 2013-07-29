package br.ufrj.ad.simulator.models;

/**
 * Representa o lado transmissor de uma sessão TCP.
 * 
 * @author André Ramos
 * 
 */
public class TxTCP {

	/**
	 * Grupo de conexões. Pode ser 1 ou 2.
	 */
	private int grupo;

	/**
	 * Janela de congestionamento (em bytes). Indica quantos pacotes podem ser
	 * transmitidos no pipeline e ficar aguardando ACK.
	 */
	private int cwnd;

	/**
	 * Threshold (em bytes).
	 */
	private int threshold;

	/**
	 * Ponteiro para o primeiro byte do pacote mais antigo que foi transmitido,
	 * mas ainda não recebeu o ACK.
	 */
	private int pacoteMaisAntigoSemACK;

	/**
	 * Ponteiro para o primeiro byte do próximo pacote a ser enviado no buffer
	 * de transmissão.
	 */
	private int proximoPacoteAEnviar;

	public TxTCP() {
		cwnd = Parametros.mss;
		threshold = 65535;
		pacoteMaisAntigoSemACK = 0;
		proximoPacoteAEnviar = 0;
	}

	public void receberSACK(SACK sack) {
		// TODO FAZER!
	}

	public Pacote enviarPacote() {
		// TODO fazer!
		return null;
	}

	public int getGrupo() {
		return grupo;
	}

	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

}
