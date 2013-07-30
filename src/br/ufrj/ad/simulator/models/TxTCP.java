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
	private long cwnd;

	/**
	 * Threshold (em bytes).
	 */
	private long threshold;

	/**
	 * Ponteiro para o primeiro byte do pacote mais antigo que ainda não recebeu
	 * o ACK.
	 */
	private long pacoteMaisAntigoSemACK;

	/**
	 * Ponteiro para o primeiro byte do próximo pacote a ser enviado no buffer
	 * de transmissão.
	 */
	private long proximoPacoteAEnviar;

	/**
	 * Número de bytes recebidos OK desde o último incremento no cwnd. Essa
	 * variável só é usada quando o TxTCP está em Congestion Avoidance.
	 */
	private long bytesRecebidosDesdeUltimoIncremento;

	/**
	 * Pacotes que foram recebidos fora de ordem no Rx. Essa informação é obtida
	 * através do último SACK e é usada durante a fase de Fast Retransmit para
	 * reenviar somente os pacotes ainda não recebidos no Rx.
	 */
	private long[][] sequenciasRecebidasCorretamente;

	/**
	 * Contador de ACKs duplicados. Usado para entrar em Fast Retransmit.
	 */
	private int contadorACKsDuplicados;

	/**
	 * Próximo byte esperado do último byte duplicado. Se esse ACK for duplicado
	 * 3 vezes, então entraremos em Fast Retransmit.
	 */
	private long ultimoACKDuplicado;

	public TxTCP() {
		cwnd = Parametros.mss;
		threshold = 65535;
		pacoteMaisAntigoSemACK = 0;
		proximoPacoteAEnviar = 0;

		bytesRecebidosDesdeUltimoIncremento = 0;
		contadorACKsDuplicados = 0;
		ultimoACKDuplicado = -1;
	}

	/**
	 * Recebe SACK e atualiza as variáveis de estado do Tx em função do SACK
	 * recebido.
	 * 
	 * @param sack
	 *            um objeto do tipo SACK
	 */
	public void receberSACK(SACK sack) {

		if (sack.getProximoByteEsperado() > pacoteMaisAntigoSemACK) {

			long bytesRecebidosOK = sack.getProximoByteEsperado()
					- pacoteMaisAntigoSemACK;

			pacoteMaisAntigoSemACK = sack.getProximoByteEsperado();
			sequenciasRecebidasCorretamente = sack
					.getSequenciasRecebidasCorretamente();

			// TODO Atualizar cwnd em função do estado do Tx!

			if (cwnd < threshold) {
				/*
				 * Estamos em Slow Start, portando a cwnd deve crescer 1MSS por
				 * pacote recebido OK no receptor.
				 */
				cwnd += bytesRecebidosOK;
			} else {
				/*
				 * Estamos em Congestion Avoidance, portanto a cwnd deve esperar
				 * o recebimento de cwnd bytes para então aumentar em 1MSS.
				 */

				bytesRecebidosDesdeUltimoIncremento += bytesRecebidosOK;
				if (bytesRecebidosDesdeUltimoIncremento >= cwnd) {
					bytesRecebidosDesdeUltimoIncremento -= cwnd;
					cwnd += Parametros.mss;
				}
			}

		} else {

			/*
			 * Atualiza a lista de sequências recebidas corretamentes no Rx com
			 * os dados do último SACK.
			 */
			sequenciasRecebidasCorretamente = sack
					.getSequenciasRecebidasCorretamente();

			if (ultimoACKDuplicado == sack.getProximoByteEsperado()) {

				contadorACKsDuplicados++;
				if (contadorACKsDuplicados == 3) {
					// TODO Entrar em Fast Retransmit!
				}
			} else {
				ultimoACKDuplicado = sack.getProximoByteEsperado();
				contadorACKsDuplicados = 1;
			}

		}

	}

	/**
	 * Esse método deve ser chamado quando ocorrer um evento de time-out. O
	 * threshold será reduzido para a metade da cwnd no momento do time-out, o
	 * cwnd voltará para 1MSS (entraremos em modo Slow Start) e todos os pacotes
	 * sem ACK serão reenviados (atualiza o ponteiro do proximo pacote a ser
	 * enviado para o pacote mais antigo sem ACK).
	 */
	public void reagirTimeOut() {
		threshold = cwnd / 2;
		cwnd = Parametros.mss;
		proximoPacoteAEnviar = pacoteMaisAntigoSemACK;
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

		// TODO Considerar caso de retransmissão e evitar enviar pacotes
		// repetidos para o Rx em função das sequências recebidas corretamente!
		
		if (!prontoParaTransmitir()) {
			throw new TxTCPNotReadyToSendException();
		}

		// Cria um pacote com tamanho = MSS.
		Pacote p = new Pacote();
		p.setByteInicialEFinal(proximoPacoteAEnviar, proximoPacoteAEnviar
				+ Parametros.mss - 1);
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

	public long getCwnd() {
		return cwnd;
	}

	public long getThreshold() {
		return threshold;
	}

	public long getPacoteMaisAntigoSemACK() {
		return pacoteMaisAntigoSemACK;
	}

	public long getProximoPacoteAEnviar() {
		return proximoPacoteAEnviar;
	}

}
