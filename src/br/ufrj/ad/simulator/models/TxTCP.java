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
	 * Próximo byte esperado do último ACK duplicado. Se esse ACK for duplicado
	 * 3 vezes, então entraremos em Fast Retransmit.
	 */
	private long ultimoACKDuplicado;

	/**
	 * Indica se o TxTCP está em estado de Fast Retransmit.
	 */
	private boolean isFastRetransmit;

	/**
	 * Janela de recuperação. O TxTCP só sairá do estado de Fast Retransmit
	 * quando todos os bytes menores ou iguais a recwnd chegarem em ordem no
	 * RxTCP.
	 */
	private long recwnd;

	public TxTCP() {
		cwnd = Parametros.mss;
		threshold = 65535;
		pacoteMaisAntigoSemACK = 0;
		proximoPacoteAEnviar = 0;

		bytesRecebidosDesdeUltimoIncremento = 0;

		contadorACKsDuplicados = 0;
		ultimoACKDuplicado = -1;
		isFastRetransmit = false;
		recwnd = -1;
	}

	/**
	 * Recebe SACK e atualiza as variáveis de estado do Tx em função do SACK
	 * recebido.
	 * 
	 * @param sack
	 *            um objeto do tipo SACK
	 */
	public void receberSACK(SACK sack) {

		/*
		 * A primeira coisa a ser feita (independentemente do estado do TxTCP) é
		 * atualizar a lista de sequências recebidas corretamente com as
		 * informações do SACK mais recente.
		 */
		sequenciasRecebidasCorretamente = sack
				.getSequenciasRecebidasCorretamente();

		if (!isFastRetransmit) {
			/*
			 * Se o TxTCP não estiver em estado de Fast Retransmit, devemos
			 * conferir so o SACK é duplicado.
			 */
			if (sack.getProximoByteEsperado() > pacoteMaisAntigoSemACK) {

				/*
				 * Se o SACK não é duplicado, prosseguimos normalmente
				 * aumentando a cwnd dependendo do estado de Slow Start ou
				 * Congestion Avoidance.
				 */

				long bytesRecebidosOK = sack.getProximoByteEsperado()
						- pacoteMaisAntigoSemACK;

				pacoteMaisAntigoSemACK = sack.getProximoByteEsperado();

				if (cwnd < threshold) {
					/*
					 * Estamos em Slow Start, portando a cwnd deve crescer 1MSS
					 * por pacote recebido OK no receptor.
					 */
					cwnd += bytesRecebidosOK;
				} else {
					/*
					 * Estamos em Congestion Avoidance, portanto a cwnd deve
					 * esperar o recebimento de cwnd bytes para então aumentar
					 * em 1MSS.
					 */

					bytesRecebidosDesdeUltimoIncremento += bytesRecebidosOK;
					if (bytesRecebidosDesdeUltimoIncremento >= cwnd) {
						bytesRecebidosDesdeUltimoIncremento -= cwnd;
						cwnd += Parametros.mss;
					}
				}

			} else {

				/*
				 * Recebemos um SACK duplicado.
				 */

				if (ultimoACKDuplicado == sack.getProximoByteEsperado()) {

					contadorACKsDuplicados++;
					if (contadorACKsDuplicados == 3) {
						threshold = cwnd / 2;
						cwnd = threshold + 3 * Parametros.mss;
						isFastRetransmit = true;
						/*
						 * Enquanto o ACK de todos os pacotes dentro da janela
						 * de recuperação não chegarem, o Tx permanece no estado
						 * de Fast Retransmit. Todos os "gaps" dentro da recwnd
						 * serão retransmitidos.
						 */
						recwnd = sack.getSequenciasRecebidasCorretamente()[sack
								.getSequenciasRecebidasCorretamente().length - 1][1];

						proximoPacoteAEnviar = sack.getProximoByteEsperado();
					}
				} else {
					ultimoACKDuplicado = sack.getProximoByteEsperado();
					contadorACKsDuplicados = 1;
				}
			}

		} else {
			/*
			 * Nesse caso estamos recebendo um ACK no estado de Fast Retransmit,
			 * portando a cwnd deve aumentar em 1 MSS.
			 */
			cwnd += Parametros.mss;

			// Atualiza ponteiro para o pacote mais antigo sem SACK.
			if (sack.getProximoByteEsperado() > pacoteMaisAntigoSemACK) {
				pacoteMaisAntigoSemACK = sack.getProximoByteEsperado();
			}

			/*
			 * Confere se todos os pacotes da janela de recuperação foram
			 * recebidos corretametne no RxTCP.
			 */
			if (sack.getProximoByteEsperado() >= recwnd) {
				isFastRetransmit = false;
				contadorACKsDuplicados = 0;
				cwnd = threshold; // Entramos em Congestion Avoidance.
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
		isFastRetransmit = false;
		contadorACKsDuplicados = 0;
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

		// Cria um pacote com tamanho = MSS.
		Pacote p = new Pacote();

		if (sequenciasRecebidasCorretamente == null
				|| sequenciasRecebidasCorretamente.length == 0) {
			/*
			 * Se o vetor de sequências for vazio, então prosseguimos a
			 * transmissão normalmente.
			 */
			p.setByteInicialEFinal(proximoPacoteAEnviar, proximoPacoteAEnviar
					+ Parametros.mss - 1);
			proximoPacoteAEnviar += Parametros.mss;

		} else {
			/*
			 * Se o vetor de sequências tiver elementos, então temos que tomar
			 * cuidado para não retransmitir pacotes desnecessários.
			 */
			if (sequenciasRecebidasCorretamente[sequenciasRecebidasCorretamente.length - 1][1] < proximoPacoteAEnviar) {
				/*
				 * Nesse caso estamos não estamos retransmitindo, portanto
				 * podemos criar o pacote normalmente.
				 */
				p.setByteInicialEFinal(proximoPacoteAEnviar,
						proximoPacoteAEnviar + Parametros.mss - 1);
				proximoPacoteAEnviar += Parametros.mss;
			} else {
				/*
				 * Estamos retransmitindo. Todo cuidado nessa hora para
				 * atualizar os ponteiros corretamente.
				 */
				for (int i = 0; i < sequenciasRecebidasCorretamente.length; i++) {
					/*
					 * Testa se o próximo pacote está antes da i-ésima
					 * sequência.
					 */
					if (proximoPacoteAEnviar < sequenciasRecebidasCorretamente[i][0]) {
						p.setByteInicialEFinal(proximoPacoteAEnviar,
								proximoPacoteAEnviar + Parametros.mss - 1);
						proximoPacoteAEnviar += Parametros.mss;
						if (proximoPacoteAEnviar == sequenciasRecebidasCorretamente[i][0]) {
							proximoPacoteAEnviar = sequenciasRecebidasCorretamente[i][1];
						}
					}
				}
			}
		}

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

	/**
	 * Indica se o TxTCP está no estado de Fast Retransmit.
	 * 
	 * @return se o TxTCP está em Fast Retransmit
	 */
	public boolean isFastRetransmit() {
		return isFastRetransmit;
	}

}
