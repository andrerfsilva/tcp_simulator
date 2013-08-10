package br.ufrj.ad.simulator.models;

import java.util.ArrayList;
import java.util.List;

import br.ufrj.ad.simulator.exceptions.TxTCPNotReadyToSendException;

/**
 * Representa o lado transmissor de uma sessão TCP.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class TxTCP {

	private List<Pacote> pacotesSemACK;

	/**
	 * Indica se o TxTCP está transmitindo algum pacote no momento
	 * 
	 */
	private boolean transmitindo;

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
	 * Número de SACKs recebidos desde o último incremento no cwnd. Essa
	 * variável só é usada quando o TxTCP está em Congestion Avoidance.
	 */
	private long nSACKSRecebidosDesdeUltimoIncremento;

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
	private long retransmitwnd;

	/**
	 * Número que indentifica um par Tx/Rx no simulador. Essa variável é usada
	 * para gerar o endereço de destino dos pacotes.
	 */
	private int numeroConexao;

	/**
	 * Estimativa do RTT (em milisegundos).
	 */
	private double rtt;

	/**
	 * Valor estimado do desvio médio do RTT (em milisegundos²).
	 */
	private double desvioMedioRTT;

	public TxTCP(int numeroConexao) {
		this.numeroConexao = numeroConexao;
		this.transmitindo = false;

		cwnd = Parametros.mss;
		threshold = 65535;
		pacoteMaisAntigoSemACK = 0;
		proximoPacoteAEnviar = 0;

		nSACKSRecebidosDesdeUltimoIncremento = 0;

		contadorACKsDuplicados = 0;
		ultimoACKDuplicado = -1;
		isFastRetransmit = false;
		retransmitwnd = 1500;

		desvioMedioRTT = 0;

		pacotesSemACK = new ArrayList<Pacote>();
	}

	public List<Pacote> getPacotesSemACK() {
		return pacotesSemACK;
	}

	/**
	 * Recebe SACK e atualiza as variáveis de estado do Tx em função do SACK
	 * recebido.
	 * 
	 * @param sack
	 *            um objeto do tipo SACK
	 * 
	 * @param tempoDeRecebimento
	 *            indica quando o TxTCP recebeu o SACK no tempo simulado (em
	 *            milisegundos)
	 */
	public void receberSACK(SACK sack, double tempoDeRecebimento) {

		/*
		 * Atualiza estimativa do RTO.
		 */
		estimarRTT(sack.getTempoDeEnvioPacoteOriginal(), tempoDeRecebimento);

		/*
		 * Atualiza a lista de sequências recebidas corretamente com as
		 * informações do SACK mais recente.
		 */
		sequenciasRecebidasCorretamente = sack
				.getSequenciasRecebidasCorretamente();

		if (!isFastRetransmit) {
			/*
			 * Se o TxTCP não estiver em estado de Fast Retransmit, prosseguimos
			 * normalmente aumentando a cwnd dependendo do estado de Slow Start
			 * ou Congestion Avoidance.
			 */

			if (cwnd < threshold) {
				/*
				 * Estamos em Slow Start, portando a cwnd deve crescer 1MSS por
				 * pacote recebido OK no receptor.
				 */
				cwnd += Parametros.mss;
			} else {
				/*
				 * Estamos em Congestion Avoidance, portanto a cwnd deve esperar
				 * o recebimento de cwnd/MSS ACKs para então aumentar em 1MSS.
				 */

				nSACKSRecebidosDesdeUltimoIncremento++;
				if (nSACKSRecebidosDesdeUltimoIncremento * Parametros.mss >= cwnd) {
					nSACKSRecebidosDesdeUltimoIncremento = 0;
					cwnd += Parametros.mss;
				}
			}

			if (sack.getProximoByteEsperado() == pacoteMaisAntigoSemACK) {

				/*
				 * Recebemos um SACK duplicado. Devemos checar o contador de
				 * SACKs ducplicados para saber se vamos entrar em Fast
				 * Retransmit.
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
						if (sack.getSequenciasRecebidasCorretamente() != null) {
							retransmitwnd = sack
									.getSequenciasRecebidasCorretamente()[sack
									.getSequenciasRecebidasCorretamente().length - 1][1];
						} else {
							retransmitwnd = proximoPacoteAEnviar;
						}

						proximoPacoteAEnviar = pacoteMaisAntigoSemACK;
					}

				} else {
					ultimoACKDuplicado = sack.getProximoByteEsperado();
					contadorACKsDuplicados = 1;
				}

			} else if (sack.getProximoByteEsperado() > pacoteMaisAntigoSemACK) {
				/*
				 * O SACK não é suplicado, logo atualizamos o ponteiro de pacote
				 * mais antigo sem ACK normalmente.
				 */
				pacoteMaisAntigoSemACK = sack.getProximoByteEsperado();

				if (pacoteMaisAntigoSemACK > proximoPacoteAEnviar) {
					proximoPacoteAEnviar = pacoteMaisAntigoSemACK;
				}
			}

		} else {
			/*
			 * Nesse caso estamos recebendo um ACK no estado de Fast Retransmit,
			 * portando a cwnd deve aumentar em 1 MSS.
			 */
			cwnd += Parametros.mss;

			if (sack.getProximoByteEsperado() > pacoteMaisAntigoSemACK) {
				/*
				 * Atualiza ponteiro para o pacote mais antigo sem SACK.
				 */
				pacoteMaisAntigoSemACK = sack.getProximoByteEsperado();

				if (pacoteMaisAntigoSemACK > proximoPacoteAEnviar) {
					proximoPacoteAEnviar = pacoteMaisAntigoSemACK;
				}

			}

			/*
			 * Confere se todos os pacotes da janela de recuperação foram
			 * recebidos corretamente no RxTCP.
			 */
			if (sack.getProximoByteEsperado() >= retransmitwnd) {
				isFastRetransmit = false;
				contadorACKsDuplicados = 0;
				cwnd = threshold; // Entramos em Congestion Avoidance.
				nSACKSRecebidosDesdeUltimoIncremento = 0;
			}
		}

	}

	public void receberSACK(SACK sack) {
		receberSACK(sack, 0);
	}

	/**
	 * Atualiza as estimativas do RTT em função do novo SACK.
	 * 
	 * @param tempoDeEnvioPacoteOriginal
	 *            tempo quando o pacote original foi enviado
	 * 
	 * @param tempoDeRecebimento
	 *            tempo quando o SACK correspondente ao pacote original chegou
	 *            no TxTCP
	 */
	private void estimarRTT(double tempoDeEnvioPacoteOriginal,
			double tempoDeRecebimento) {

		double m = tempoDeRecebimento - tempoDeEnvioPacoteOriginal;
		double y = m - rtt;
		desvioMedioRTT = desvioMedioRTT + 0.25 * (Math.abs(y) - desvioMedioRTT);
		rtt = rtt + 0.125 * y;

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
		nSACKSRecebidosDesdeUltimoIncremento = 0;
	}

	/**
	 * Prepara o próximo pacote pronto para ser enviado e atualiza as variáveis
	 * de estado em função desse novo envio.
	 * 
	 * @param tempoDeEnvio
	 *            indica quando o pacote foi passado para a camada de enlace
	 *            para ser transmitido (em milisegundos)
	 * 
	 * @return pacote enviado
	 * 
	 * @throws TxTCPNotReadyToSendException
	 *             Se o TxTCP não estiver pronto para transmitir, ou seja, todos
	 *             os pacotes da janela de congestionamento já foram
	 *             transmitidos, então essa exceção será lançada. Tomar cuidado
	 *             para não enviar pacotes fora da janela de congestionamento.
	 */
	public Pacote enviarPacote(double tempoDeEnvio)
			throws TxTCPNotReadyToSendException {

		if (!prontoParaTransmitir()) {
			throw new TxTCPNotReadyToSendException();
		}

		// Cria um pacote com tamanho = MSS.
		Pacote p = new Pacote();

		p.setDestino(numeroConexao);
		p.setTempoDeEnvio(tempoDeEnvio);

		if (sequenciasRecebidasCorretamente == null
				|| sequenciasRecebidasCorretamente.length == 0) {
			/*
			 * Se o vetor de sequências for vazio, então prosseguimos a
			 * transmissão normalmente.
			 */
			p.setByteInicialEFinal(proximoPacoteAEnviar, proximoPacoteAEnviar
					+ Parametros.mss - 1);
			proximoPacoteAEnviar += Parametros.mss;
			return p;

		} else {
			/*
			 * Se o vetor de sequências tiver elementos, então temos que tomar
			 * cuidado para não retransmitir pacotes desnecessários.
			 */
			if (sequenciasRecebidasCorretamente[sequenciasRecebidasCorretamente.length - 1][1] <= proximoPacoteAEnviar) {
				/*
				 * Nesse caso estamos não estamos retransmitindo, portanto
				 * podemos criar o pacote normalmente.
				 */
				p.setByteInicialEFinal(proximoPacoteAEnviar,
						proximoPacoteAEnviar + Parametros.mss - 1);
				proximoPacoteAEnviar += Parametros.mss;
				return p;
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
					if (proximoPacoteAEnviar >= sequenciasRecebidasCorretamente[i][0]
							&& proximoPacoteAEnviar <= sequenciasRecebidasCorretamente[i][1]) {
						proximoPacoteAEnviar = sequenciasRecebidasCorretamente[i][1];
						p.setByteInicialEFinal(proximoPacoteAEnviar,
								proximoPacoteAEnviar + Parametros.mss - 1);
						proximoPacoteAEnviar += Parametros.mss;
						return p;
					}

				}

				p.setByteInicialEFinal(proximoPacoteAEnviar,
						proximoPacoteAEnviar + Parametros.mss - 1);
				proximoPacoteAEnviar += Parametros.mss;

				return p;
			}
		}
	}

	public Pacote enviarPacote() throws TxTCPNotReadyToSendException {
		return enviarPacote(0);
	}

	/**
	 * Indica se o TxTCP pode transmitir mais um pacote dependendo do tamanho da
	 * janela de congestionamento.
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
	 * Estimativa do retransmission time-out (RTO) usada usada para agendar o
	 * evento de time-out (em milisegundos).
	 * 
	 * @return estimativa do RTO
	 */
	public double getRTO() {
		return rtt + 4 * desvioMedioRTT;
	}

	/**
	 * Indica se o TxTCP está no estado de Fast Retransmit.
	 * 
	 * @return se o TxTCP está em Fast Retransmit
	 */
	public boolean isFastRetransmit() {
		return isFastRetransmit;
	}

	/**
	 * 
	 * Número que indentifica um par Tx/Rx no simulador. Essa variável é usada
	 * para gerar o endereço de destino dos pacotes.
	 * 
	 * @return índice (endereço) do TxTCP no vetor de transmissoes
	 */
	public int getNumeroConexao() {
		return numeroConexao;
	}

	public boolean isTransmitindo() {
		return transmitindo;
	}

	public void setTransmitindo(boolean transmitindo) {
		this.transmitindo = transmitindo;
	}

	public double getRTT() {
		return rtt;
	}

	public void setRTT(double rtt) {
		this.rtt = rtt;
	}

	public double getDesvioMedioRTT() {
		return desvioMedioRTT;
	}

}
