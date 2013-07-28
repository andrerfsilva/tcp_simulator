package br.ufrj.ad.simulator.models;

import java.io.IOException;
import java.util.PriorityQueue;

import br.ufrj.ad.simulator.estatistica.Random;
import br.ufrj.ad.simulator.eventos.Evento;
import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebePacoteTxTCP;
import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebeTrafegoDeFundo;
import br.ufrj.ad.simulator.eventos.EventoRoteadorTerminaEnvio;
import br.ufrj.ad.simulator.eventos.EventoTxRecebeSACK;

/**
 * Essa classe gerencia os eventos, as atualizações no modelo do sistema
 * simulado e a coleta de estatísticas de interesse da simulação. Esse simulador
 * usa abordagem integrada para coletar estatísticas e usa o modo Batch
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
	 * Armazena todos os parâmetros de entrada da simulação.
	 */
	private Parametros parametros;

	public Simulador() throws IOException {
		rede = new Rede();
		geradorNumerosAleatorios = new Random();
		parametros = new Parametros();
		tempoAtualSimulado = 0;

		if (parametros.getDisciplinaRoteadorProperty().equals("FIFO")) {
			rede.setRoteador(new RoteadorFIFO());
		} else {
			rede.setRoteador(new RoteadorRED());
		}
	}

	/**
	 * Inicia o loop principal da simulação e retorna as estatísticas para todos
	 * os cenários do trabalho. Usa abordagem integrada para coleta de
	 * estatísticas e simulação Batch.
	 * 
	 * @throws EventOutOfOrderException
	 *             Quando a lista de eventos retornar um evento cujo tempo de
	 *             ocorrência seja menor que o tempo atual simulado, ou seja,
	 *             mostra que um evento deveria ter sido tratado antes, portanto
	 *             há uma inconsistência nos dados da simulação e a simulação
	 *             deve ser abortada.
	 */
	public void simular() throws EventOutOfOrderException {

		/*
		 * Agendar eventos iniciais!
		 */
		Evento primeiraChegadaTrafegoFundo = new EventoRoteadorRecebeTrafegoDeFundo(
				geradorNumerosAleatorios.nextExponential(1 / parametros
						.getTempoMedioEntreRajadas()));
		filaEventos.add(primeiraChegadaTrafegoFundo);

		// TODO: estimar fase transiente!

		/*
		 * Cada rodada nesse loop representa uma rodada no plano de controle,
		 * com esboçado no esqueleto nos slides de simulação. O número de
		 * rodadas N será calculado em função do intervalo de confiança para as
		 * estatísticas de interesse.
		 */
		while (!estatisticasSatisfatorias()) {

			while (filaEventos.size() > 0 && !criterioParada()) {
				Evento e = filaEventos.poll();

				/*
				 * Confere a consistência da ordem dos eventos no tempo.
				 */
				if (e.getTempoDeOcorrencia() < this.tempoAtualSimulado) {
					throw new EventOutOfOrderException();
				}

				tempoAtualSimulado = e.getTempoDeOcorrencia();
				tratarEvento(e);

			}

			// TODO coletar amostas
		}

		// TODO apresentar estatísticas e intervalo de confiança

	}

	private boolean criterioParada() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean estatisticasSatisfatorias() {
		// TODO escrever a condição de parada do loop de simulação
		return false;
	}

	/**
	 * Método auxiliar usado para tratar o próximo evento da fila de eventos.
	 * Cada evento é tratado de forma diferente dependendo do seu tipo.
	 * 
	 * @param e
	 *            próximo evento da fila de eventos
	 */
	private void tratarEvento(Evento e) {
		// TODO definir eventos, como eles alteram o estado do sistema, e
		// agendar novos eventos!
		if (e instanceof EventoRoteadorRecebeTrafegoDeFundo) {

			tratarEventoRoteadorRecebeTrafegoDeFundo();

		} else if (e instanceof EventoRoteadorTerminaEnvio) {

			tratarEventoRoteadorTerminaEnvio();

		} else if (e instanceof EventoRoteadorRecebePacoteTxTCP) {
			tratarEventoRoteadorRecebePacoteTxTCP();
		}
	}

	private void tratarEventoRoteadorRecebePacoteTxTCP() {

		// TODO: fazer isso!!!
	}

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
						+ parametros.getTempoPropagacaoRetornoACKGrupo1());
			} else {
				proximoSACK = new EventoTxRecebeSACK(tempoAtualSimulado
						+ parametros.getTempoPropagacaoRetornoACKGrupo2());
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
								.nextExponential(1 / parametros.getTempoMedioEntreRajadas()));
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
				.nextGeometric(1 / parametros.getMediaPacotesPorRajada());

		for (long i = 0; i < tamanhoRajada; i++) {
			rede.getRoteador().receberPacote(new Pacote(), tempoAtualSimulado);
		}
	}
}
