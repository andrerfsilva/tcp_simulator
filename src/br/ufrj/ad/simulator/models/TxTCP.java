package br.ufrj.ad.simulator.models;

import br.ufrj.ad.simulator.exceptions.TxTCPNotReadyToSendException;

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

	/**
	 * Recebe SACK e atualiza as variáveis de estado do Tx em função do SACK
	 * recebido.
	 * 
	 * @param sack
	 *            um objeto do tipo SACK
	 */
	public void receberSACK(SACK sack) {
		// TODO FAZER!
	}

	/**
	 * Prepara o próximo pacote pronto para ser enviado e atualiza as variáveis
	 * de estado em função desse novo envio.
	 * 
	 * @return pacote enviado
	 * 
	 * @throws TxTCPNotReadyToSendException
	 *             Se o TxTCP não estiver pronto para transmitir, ou seja, todos
	 *             os pacotes da janela de congestionamento já foram
	 *             transmitidos, então essa exceção será lançada. Tomar cuidado
	 *             para não enviar pacotes fora da janela de congestionamento.
	 */
	public Pacote enviarPacote() throws TxTCPNotReadyToSendException {

		if (!prontoParaTransmitir()) {
			throw new TxTCPNotReadyToSendException();
		}

		Pacote p = new Pacote();
		p.setByteInicialEFinal(proximoPacoteAEnviar, Parametros.mss - 1);
		proximoPacoteAEnviar += Parametros.mss;

		return p;
	}

	/**
	 * Indica se o Tx pode transmitir mais um pacote dependendo do tamanho da
	 * janena de congestionamento.
	 * 
	 * @return
	 */
	public boolean prontoParaTransmitir() {
		return proximoPacoteAEnviar < pacoteMaisAntigoSemACK + cwnd;
	}

	public int getGrupo() {
		return grupo;
	}

	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

}
