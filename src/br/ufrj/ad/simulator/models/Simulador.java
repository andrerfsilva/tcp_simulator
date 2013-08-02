package br.ufrj.ad.simulator.models;

import java.io.IOException;
import java.util.PriorityQueue;

import br.ufrj.ad.simulator.estatistica.Estimador;
import br.ufrj.ad.simulator.estatistica.Random;
import br.ufrj.ad.simulator.eventos.Evento;
import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebePacoteTxTCP;
import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebeTrafegoDeFundo;
import br.ufrj.ad.simulator.eventos.EventoRoteadorTerminaEnvio;
import br.ufrj.ad.simulator.eventos.EventoTimeOut;
import br.ufrj.ad.simulator.eventos.EventoTxRecebeSACK;
import br.ufrj.ad.simulator.exceptions.EventOutOfOrderException;

/**
 * Essa classe gerencia os eventos, as atualizações no modelo do sistema
 * simulado e a coleta de estatísticas de interesse da simulação. Esse simulador
 * usa abordagem integrada para coletar estatísticas e usa o modo Replicativo
 * considerando múltiplas rodadas.
 * 
 * @author André Ramos, Welligton Mascena featuring Vitor Maia
 * 
 */
public class Simulador {

	/**
	 * Gerador de números aleatórios usado para calcular as variáveis aleatórias
	 * da simulação.
	 */
	private Random geradorNumerosAleatorios;

	/**
	 * Modelo da rede do sistema a ser simulado.
	 */
	private Rede rede;

	/**
	 * Fila de eventos ordenados no tempo.
	 */
	private PriorityQueue<Evento> filaEventos;

	/**
	 * Tempo atual simulado (em milisegundos).
	 */
	private double tempoAtualSimulado;

	/**
	 * Número de eventos por rodada de simulação.
	 */
	private int numeroEventosPorRodada;

	/**
	 * Armazena todos os parâmetros de entrada da simulação.
	 */
	private Parametros parametros;

	/**
	 * Estimadores da vazão TCP para cada conexão.
	 */
	private Estimador[] estimadoresDeVazaoTCP;

	public Simulador() throws IOException {

		numeroEventosPorRodada = 10000;
		geradorNumerosAleatorios = new Random();
		parametros = new Parametros();

		/* Inicializa estimadores */
		estimadoresDeVazaoTCP = new Estimador[parametros.getEstacoesGrupo1()
				+ parametros.getEstacoesGrupo2()];

		for (int i = 0; i < estimadoresDeVazaoTCP.length; i++) {
			estimadoresDeVazaoTCP[i] = new Estimador();
		}

		/* Estado inicial de simulação */
		setarEstadoInicialDeSimulacao();
	}

	/**
	 * Cria uma nova lista de eventos vazia, seta o tempo atual simulado para
	 * zero e reinicia as variáveis de estado da rede. Como usaremos o método
	 * replicativo, esse método deve ser chamado no início de cada rodada.
	 */
	private void setarEstadoInicialDeSimulacao() {
		filaEventos = new PriorityQueue<Evento>();

		tempoAtualSimulado = 0;

		rede = new Rede(parametros);
	}

	/**
	 * Inicia o loop principal da simulação e retorna as estatísticas para todos
	 * os cenários do trabalho. Usa abordagem integrada para coleta de
	 * estatísticas e simulação usando método Replicativo.
	 * 
	 * @throws EventOutOfOrderException
	 *             Quando a lista de eventos retornar um evento cujo tempo de
	 *             ocorrência seja menor que o tempo atual simulado, ou seja,
	 *             mostra que um evento deveria ter sido tratado antes, portanto
	 *             há uma inconsistência nos dados e a simulação deve ser
	 *             abortada.
	 */
	public void simular() throws EventOutOfOrderException {

		/*
		 * Cada rodada nesse loop representa uma rodada no plano de controle,
		 * com esboçado no esqueleto nos slides de simulação. O número de
		 * rodadas N será calculado em função do intervalo de confiança para as
		 * estatísticas de interesse.
		 */
		while (!estatisticasSatisfatorias()) {

			setarEstadoInicialDeSimulacao();
			agendarEventosIniciais();

			// TODO: estimar fase transiente!

			for (int i = 0; i < numeroEventosPorRodada
					&& filaEventos.size() > 0; i++) {

				System.out.println("Nº eventos OK = " + i); // TODO: tira essa
															// porra!

				tratarProximoEvento();
			}

			/*
			 * Coleta a vazão para cada sessão TCP e armazena nos seus
			 * estimadores correspondentes.
			 */
			for (int i = 0; i < estimadoresDeVazaoTCP.length; i++) {
				estimadoresDeVazaoTCP[i].coletarAmostra(rede.getReceptores()[i]
						.getProximoByteEsperado() / tempoAtualSimulado);
			}
		}
	}

	/**
	 * Esse método trata o próximo evento da fila de enventos.
	 * 
	 * @throws EventOutOfOrderException
	 *             Se o tempo de ocorrência do próximo evento for antes do tempo
	 *             atual simulado, então temos uma inconsistência grave no nosso
	 *             simulador e a simulação deve ser abortada.
	 */
	public void tratarProximoEvento() throws EventOutOfOrderException {

		Evento e = filaEventos.poll();

		System.out.println(e.getClass().toString()); // TODO: tira essa porra!

		/*
		 * Confere a consistência da ordem dos eventos no tempo.
		 */
		if (e.getTempoDeOcorrencia() < this.tempoAtualSimulado) {
			throw new EventOutOfOrderException();
		}

		tempoAtualSimulado = e.getTempoDeOcorrencia();
		tratarEvento(e);
	}

	/**
	 * Agenda os eventos iniciais da simulação, os quais são a primeira chegada
	 * de tráfego de fundo (v.a. exponencial) e as primeiras transmissões TCP
	 * (v.a. uniforme).
	 */
	private void agendarEventosIniciais() {

		/*
		 * Primeira chegada de tráfego de fundo.
		 */
		Evento primeiraChegadaTrafegoFundo = new EventoRoteadorRecebeTrafegoDeFundo(
				geradorNumerosAleatorios.nextExponential(1 / parametros
						.getTempoMedioEntreRajadas()));
		filaEventos.add(primeiraChegadaTrafegoFundo);

		/*
		 * Primeiras transmissões TCP.
		 */
		for (int i = 0; i < rede.getTransmissores().length; i++) {

			/*
			 * Calcula o tempo de propagação e transmissão baseado nos
			 * parâmetros de entrada.
			 */
			double tempoTransmissao = Parametros.mss / parametros.getCs();
			double tempoPropagacao = (rede.getTransmissores()[i].getGrupo() == 1 ? parametros
					.getTP1() : parametros.getTP2());

			/*
			 * Para que as conexões iniciem de forma assíncrona, o início da
			 * primeira transmissão será uma variável aleatória uniforme (0,
			 * 100) ms.
			 */
			double inicioAssincrono = geradorNumerosAleatorios.nextDouble() * 100;

			/*
			 * Cria o evento e insere na fila de eventos.
			 */
			Evento primeiraChegadaTCP = new EventoRoteadorRecebePacoteTxTCP(
					inicioAssincrono + tempoPropagacao + tempoTransmissao, i);

			filaEventos.add(primeiraChegadaTCP);
		}
	}

	/**
	 * Número de eventos por rodada de simulação.
	 * 
	 * @return número de eventos por rodada de simulação
	 */
	public int getNumeroEventosPorRodada() {
		return numeroEventosPorRodada;
	}

	/**
	 * Número de eventos por rodada de simulação.
	 * 
	 * @param numeroEventosPorRodada
	 */
	public void setNumeroEventosPorRodada(int numeroEventosPorRodada) {
		this.numeroEventosPorRodada = numeroEventosPorRodada;
	}

	/**
	 * Avalia se intervalo de confiança é aceitável.
	 * 
	 * @return true se intervalo de confiança é aceitável, false caso contrário
	 */
	private boolean estatisticasSatisfatorias() {

		for (int i = 0; i < estimadoresDeVazaoTCP.length; i++) {

			if ((estimadoresDeVazaoTCP[i].getNumeroAmostras() <= 1)
					|| (2 * estimadoresDeVazaoTCP[i].getDistanciaICMedia(0.9) > 0.1 * estimadoresDeVazaoTCP[i]
							.getMedia())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Método auxiliar usado para tratar o próximo evento da fila de eventos.
	 * Cada evento é tratado de forma diferente dependendo do seu tipo.
	 * 
	 * @param e
	 *            próximo evento da fila de eventos
	 */
	private void tratarEvento(Evento e) {
		if (e instanceof EventoRoteadorRecebeTrafegoDeFundo) {
			tratarEventoRoteadorRecebeTrafegoDeFundo();
		} else if (e instanceof EventoRoteadorTerminaEnvio) {
			tratarEventoRoteadorTerminaEnvio();
		} else if (e instanceof EventoRoteadorRecebePacoteTxTCP) {
			tratarEventoRoteadorRecebePacoteTxTCP(e);
		} else if (e instanceof EventoTxRecebeSACK) {
			tratarEventoTxRecebeSACK(e);
		} else if (e instanceof EventoTimeOut) {
			tratarEventoTimeOut(e);
		}
	}

	/**
	 * Faz o TxTCP entrar no estado de reação a time-out e agenda o primeiro
	 * reenvio de pacotes.
	 * 
	 * @param e
	 */
	private void tratarEventoTimeOut(Evento e) {

		/*
		 * Faz o TxTCP reagir ao time-out.
		 */
		EventoTimeOut eto = (EventoTimeOut) e;
		TxTCP tx = rede.getTransmissores()[eto.getTxTCP()];

		tx.reagirTimeOut();

		/*
		 * Agenda o reenvio de pacotes.
		 */

		// TODO: CONSIDERAR QUE O TIME-OUT PODE ACONTECER DURANTE O ENVIO DE UM
		// PACOTE!!!

		double tempoTransmissao = Parametros.mss / parametros.getCs();
		double tempoPropagacao = (tx.getGrupo() == 1 ? parametros.getTP1()
				: parametros.getTP2());

		EventoRoteadorRecebePacoteTxTCP proximaChegadaTCP = new EventoRoteadorRecebePacoteTxTCP(
				tempoAtualSimulado + tempoPropagacao + tempoTransmissao,
				eto.getTxTCP());

		filaEventos.add(proximaChegadaTCP);
	}

	/**
	 * Fax o TxTCP de destino receber o SACK e cancela o evento de time-out
	 * correspondente a esse pacote.
	 * 
	 * @param e
	 *            evento de recebimento de SACK
	 */
	private void tratarEventoTxRecebeSACK(Evento e) {

		EventoTxRecebeSACK esack = (EventoTxRecebeSACK) e;
		TxTCP tx = rede.getTransmissores()[esack.getSACK().getDestino()];
		tx.receberSACK(esack.getSACK(), tempoAtualSimulado);

		// TODO CANCELAR O TIME-OUT DO PACOTE CORRESPONDENTE!!!

		filaEventos.remove(esack.getSACK().getEventoTimeOut());
	}

	/**
	 * Faz o roteador receber o pacote proveniente da conexão TCP. Se o TxTCP
	 * ainda puder transmitir mais pacotes (dependendo da sua janela de
	 * congestionamento), então agendamos a próxima chega TCP.
	 * 
	 * @param e
	 *            evento de origem
	 */
	private void tratarEventoRoteadorRecebePacoteTxTCP(Evento e) {

		EventoRoteadorRecebePacoteTxTCP etcp = (EventoRoteadorRecebePacoteTxTCP) e;

		// int pacotesNoRoteadorNoMomentoDaChegada
		/*
		 * Se o pacote TCP encontrar o roteador vazio, então podemos agendar o
		 * próximo envio. Caso contrário, não podemos, pois provavelmente
		 * chegamos no meio de uma transmissão de um pacote mais antigo, ou
		 * seja, não sabemos o quanto falta para terminar a próxima transição.
		 */
		if (rede.getRoteador().getNumeroPacotes() == 0) {
			Evento proximoEnvio = new EventoRoteadorTerminaEnvio(
					tempoAtualSimulado
							+ parametros
									.tempoTransmissaoPacoteNoRoteador(Parametros.mss));
			filaEventos.add(proximoEnvio);
		}

		/*
		 * Faz o roteador receber o pacote do TxTCP correspondente.
		 */
		TxTCP tx = rede.getTransmissores()[etcp.getTxTCP()];
		Pacote p = tx.enviarPacote(tempoAtualSimulado); // TODO: erro grave
														// aqui!
		rede.getRoteador().receberPacote(p, tempoAtualSimulado);

		/*
		 * Se o TxTCP ainda puder transmitir mais pacotes, agendamos a chegada
		 * do próximo pacote TCP.
		 */
		if (tx.prontoParaTransmitir()) {

			double tempoTransmissao = Parametros.mss / parametros.getCs();
			double tempoPropagacao = (tx.getGrupo() == 1 ? parametros.getTP1()
					: parametros.getTP2());

			EventoRoteadorRecebePacoteTxTCP proximaChegadaTCP = new EventoRoteadorRecebePacoteTxTCP(
					tempoAtualSimulado + tempoPropagacao + tempoTransmissao,
					etcp.getTxTCP());

			filaEventos.add(proximaChegadaTCP);
		}

		/*
		 * Agendamos o time-out do pacote que acabou de ser enviado. Para
		 * facilitar a remoção do evento de time-out quando o SACK
		 * correspondente chegar, também armazenamos o evento no pacote.
		 */
		EventoTimeOut eTimeOut = new EventoTimeOut(tempoAtualSimulado
				+ tx.getRTO(), etcp.getTxTCP());
		filaEventos.add(eTimeOut);
		p.setEventoTimeOut(eTimeOut); // Usado para facilitar a exclusão do
										// time-out quando o SACK correspondente
										// chegar.
	}

	/**
	 * Esse evento ocorre quando o roteador termina de enviar o pacote no
	 * serviço. Portanto o pacote chegou ao seu destino (Rx ou tráfego de fundo)
	 * e podemos remvê-lo do buffer o roteador e iniciar a próxima transmissão.
	 */
	private void tratarEventoRoteadorTerminaEnvio() {

		SACK sack = rede.getRoteador().enviarProximoPacote(tempoAtualSimulado);

		/*
		 * Se o SACK é nulo, então o pacote envia é tráfego de fundo. Caso
		 * contrário, significa que o Rx já recebeu o pacote e preparou o SACK
		 * para o Tx. Logo podemos agendar o recebimento do SACK no Tx.
		 */
		if (sack != null) {

			Evento proximoSACK;
			if (rede.getTransmissores()[sack.getDestino()].getGrupo() == 1) {
				proximoSACK = new EventoTxRecebeSACK(tempoAtualSimulado
						+ parametros.getTempoPropagacaoRetornoACKGrupo1(), sack);
			} else {
				proximoSACK = new EventoTxRecebeSACK(tempoAtualSimulado
						+ parametros.getTempoPropagacaoRetornoACKGrupo2(), sack);
			}

			filaEventos.add(proximoSACK);
		}

		/*
		 * Se ainda existirem pacotes no buffer do roteador, então podemos
		 * agendar o próximo envio de pacotes.
		 */
		if (rede.getRoteador().getNumeroPacotes() > 0) {

			Evento proximoEnvio = new EventoRoteadorTerminaEnvio(
					tempoAtualSimulado
							+ parametros.tempoTransmissaoPacoteNoRoteador(rede
									.getRoteador().getProximoPacoteAEnviar()
									.getTamanho()));
			filaEventos.add(proximoEnvio);
		}

	}

	/**
	 * Método auxiliar usado para tratar o evento em que o roteador recebe
	 * tráfego de fundo. Sempre que ocorre uma cegada de tráfego de fundo,
	 * deve-se agendar a próxima chegada. O tempo entre as chegadas é uma
	 * variável aleatória exponencial (a taxa é dada pelos parâmetros de
	 * entrada).
	 */
	private void tratarEventoRoteadorRecebeTrafegoDeFundo() {
		/*
		 * Agenda a chegada do próximo tráfego de fundo.
		 */
		Evento proximaChegadaTrafegoFundo = new EventoRoteadorRecebeTrafegoDeFundo(
				tempoAtualSimulado
						+ geradorNumerosAleatorios
								.nextExponential(1 / parametros
										.getTempoMedioEntreRajadas()));
		filaEventos.add(proximaChegadaTrafegoFundo);

		/*
		 * Se a rajada encontra o roteador vazio, então podemos agendar o
		 * próximo envio. Caso contrário, não podemos, pois provavelmente
		 * chegamos no meio de uma transmissão de um pacote mais antigo, ou
		 * seja, não sabemos o quanto falta para terminar a próxima transição.
		 */
		if (rede.getRoteador().getNumeroPacotes() == 0) {
			Evento proximoEnvio = new EventoRoteadorTerminaEnvio(
					tempoAtualSimulado
							+ parametros
									.tempoTransmissaoPacoteNoRoteador(Parametros.mss));
			filaEventos.add(proximoEnvio);
		}

		/*
		 * Calcula o número de pacotes na rajada e coloca um por um no roteador.
		 */
		long tamanhoRajada = (long) geradorNumerosAleatorios
				.nextGeometric(1.0 / parametros.getMediaPacotesPorRajada());

		for (long i = 0; i < tamanhoRajada; i++) {
			rede.getRoteador().receberPacote(new Pacote(), tempoAtualSimulado);
		}
	}

	public PriorityQueue<Evento> getFilaEventos() {
		return filaEventos;
	}

	public double getTempoAtualSimulado() {
		return tempoAtualSimulado;
	}

	public Parametros getParametros() {
		return parametros;
	}

	/**
	 * Executa o simulador e apresenta as estatísticas da vazão TCP para cada
	 * par Tx/Rx e a vazão média das conexões como um todo.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Simulador simulador = new Simulador();
		simulador.simular();

		System.out.println("VAZÃO MÉDIA POR CONEXÃO:");
		for (int i = 0; i < simulador.estimadoresDeVazaoTCP.length; i++) {
			System.out.println("\tTx"
					+ i
					+ ":\t"
					+ simulador.estimadoresDeVazaoTCP[i].getMedia()
					+ "±"
					+ simulador.estimadoresDeVazaoTCP[i]
							.getDistanciaICMedia(0.9));
		}

		System.out.println("VAZÃO MÉDIA GLOBAL:");
		Estimador estimadorVazaoMediaGlobal = new Estimador();
		for (int i = 0; i < simulador.estimadoresDeVazaoTCP.length; i++) {
			estimadorVazaoMediaGlobal
					.coletarAmostra(simulador.estimadoresDeVazaoTCP[i]
							.getMedia());
		}
		System.out.println("\t\t" + estimadorVazaoMediaGlobal.getMedia() + "±"
				+ estimadorVazaoMediaGlobal.getDistanciaICMedia(0.9));
	}
}
