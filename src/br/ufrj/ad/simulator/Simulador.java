package br.ufrj.ad.simulator;

import java.util.PriorityQueue;

/**
 * Essa classe gerencia os eventos, as atualizações no modelo do sistema
 * simulado e a coleta de estatísticas de interesse da simulação.
 * 
 * @author André Ramos, Welligton Mascena
 * 
 */
public class Simulador {

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
	 * Número médio de pacotes que chegam em uma rajada de tráfego de fundo. A
	 * rajada ten tamanho geométrico, ou seja, o número de pacotes é uma
	 * variável aleatória geométrica.
	 */
	private long tamanhoMedioRajada;

	/**
	 * Tempo médio entre as rajadas do tráfego de fundo (em milisegundos).
	 * Lembrando que o tempo entre as chegadas é uma variável aleatória
	 * exponencial com média = 1/(taxa de chegada), ou seja, a taxa = 1/média.
	 */
	private double tempoMedioEntreRajadas;

	/**
	 * Gerador de números aleatórios usado para calcular as variáveis aleatórias
	 * da simulação.
	 */
	private Random geradorNumerosAleatorios;

	/**
	 * Taxa de saída no enlace do rotedor (em bps). Parâmetro Cg na definição do
	 * trabalho.
	 */
	private double taxaSaidaEnlaceDoRoteador;

	/**
	 * Maximum Segment Size = 1500 bytes.
	 */
	private final int mss = 1500;

	public Simulador() {
		rede = new Rede();
		tempoAtualSimulado = 0;
		geradorNumerosAleatorios = new Random();

		// valores default
		tempoMedioEntreRajadas = 24.0;
		tamanhoMedioRajada = 10;

		// roteador default é FIFO
		rede.setRoteador(new RoteadorFIFO());
	}

	/**
	 * Calcula a taxa do tráfego de fundo em Mbps. Por exemplo: Se o tamanho
	 * médio das rajadas é 10 pacotes e o intervalo médio entre chegadas for
	 * 24ms, então a taxa média deste tráfego de fundo é (1500 x 8 x 10)/24ms =
	 * 5 Mbps. Lembrando que todos os pacotes da rajada tem tamanho MSS = 1500
	 * bytes.
	 * 
	 * @return taxa do tráfego de fundo em Mbps
	 */
	public double getTaxaTrafegoDeFundoEmMbps() {
		return (mss * 8 * tamanhoMedioRajada * 1E-3) / tempoMedioEntreRajadas;
	}

	/**
	 * Número médio de pacotes que chegam em uma rajada de tráfego de fundo. A
	 * rajada ten tamanho geométrico, ou seja, o número de pacotes é uma
	 * variável aleatória geométrica.
	 * 
	 * @return média do número de pacotes numa rajada de tráfego de fundo
	 */
	public long getTamanhoMedioRajada() {
		return tamanhoMedioRajada;
	}

	/**
	 * Número médio de pacotes que chegam em uma rajada de tráfego de fundo. A
	 * rajada ten tamanho geométrico, ou seja, o número de pacotes é uma
	 * variável aleatória geométrica.
	 * 
	 * @param tamanhoMedioRajada
	 *            média do número de pacotes numa rajada de tráfego de fundo
	 */
	public void setTamanhoMedioRajada(long tamanhoMedioRajada) {
		this.tamanhoMedioRajada = tamanhoMedioRajada;
	}

	/**
	 * Tempo médio entre as rajadas do tráfego de fundo (em milisegundos).
	 * Lembrando que o tempo entre as chegadas é uma variável aleatória
	 * exponencial com média = 1/(taxa de chegada), ou seja, a taxa = 1/média.
	 * 
	 * @return tempo médio entre rajadas de tráfego de fundo (em milisegundos)
	 */
	public double getTempoMedioEntreRajadas() {
		return tempoMedioEntreRajadas;
	}

	/**
	 * Tempo médio entre as rajadas do tráfego de fundo (em milisegundos).
	 * Lembrando que o tempo entre as chegadas é uma variável aleatória
	 * exponencial com média = 1/(taxa de chegada), ou seja, a taxa = 1/média.
	 * 
	 * @param tempoMedioEntreRajadas
	 *            tempo médio entre rajadas de tráfego de fundo (em
	 *            milisegundos)
	 */
	public void setTempoMedioEntreRajadas(double tempoMedioEntreRajadas) {
		this.tempoMedioEntreRajadas = tempoMedioEntreRajadas;
	}

	/**
	 * Inicia o loop principal da simulação e retorna as estatísticas para todos
	 * os cenários do trabalho.
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
				geradorNumerosAleatorios
						.nextExponential(1 / tempoMedioEntreRajadas));
		filaEventos.add(primeiraChegadaTrafegoFundo);

		// TODO: estimar fase transiente!

		while (filaEventos.size() > 0 && !estatisticasSatisfatorias()) {
			Evento e = filaEventos.poll();

			/*
			 * Confere a consistência da ordem dos eventos no tempo.
			 */
			if (e.getTempo() < this.tempoAtualSimulado) {
				throw new EventOutOfOrderException();
			}

			tempoAtualSimulado += e.getTempo();
			tratarEvento(e);

		}

		// TODO coletar estatísticas
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

		} else if (e instanceof EventoRoteadorEnviaPacote) {
			// TODO: fazer esse evento.
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
								.nextExponential(1 / tempoMedioEntreRajadas));
		filaEventos.add(proximaChegadaTrafegoFundo);

		/*
		 * Se a rajada encontra o roteador vazio, então podemos agendar o
		 * próximo envio. Caso contrário, não podemos, pois provavelmente
		 * chegamos no meio de uma transmissão de um pacote mais antigo, ou
		 * seja, não sabemos o quanto falta para terminar a próxima transição.
		 */
		if (rede.getRoteador().getNumeroPacotes() == 0) {
			Evento proximoEnvio = new EventoRoteadorEnviaPacote(
					tempoAtualSimulado + tempoTransmissaoPacoteNoRoteador(mss));
			filaEventos.add(proximoEnvio);
		}

		/*
		 * Calcula o número de pacotes na rajada e coloca um por um no roteador.
		 */
		long tamanhoRajada = (long) geradorNumerosAleatorios
				.nextGeometric(1 / tamanhoMedioRajada);

		for (long i = 0; i < tamanhoRajada; i++) {
			rede.getRoteador().receberPacote(new Pacote(), tempoAtualSimulado);
		}
	}

	/**
	 * Calcula o tempo de transmissão de um pacote no enlace de saída do
	 * roteador (em milisegundos). O tempo é calculado em função da taxa do
	 * enlace e do tamanho do pacote.
	 * 
	 * @param tamanhoPacote
	 *            tamanho do pacote (em bytes)
	 * @return tempo de transmissão do pacote no enlace de saída do roteador (em
	 *         milisegundos)
	 */
	public double tempoTransmissaoPacoteNoRoteador(int tamanhoPacote) {
		return (tamanhoPacote * 8 / taxaSaidaEnlaceDoRoteador) * 1E3;
	}

	/**
	 * Taxa de saída no enlace do rotedor (em bps). Parâmetro Cg na definição do
	 * trabalho.
	 * 
	 * @return taxa de saída no enlace do rotedor (em bps)
	 */
	public double getTaxaSaidaEnlaceDoRoteador() {
		return taxaSaidaEnlaceDoRoteador;
	}

	/**
	 * Taxa de saída no enlace do rotedor (em bps). Parâmetro Cg na definição do
	 * trabalho.
	 * 
	 * @param taxaSaidaEnlaceDoRoteador
	 *            taxa de saída no enlace do rotedor (em bps)
	 */
	public void setTaxaSaidaEnlaceDoRoteador(double taxaSaidaEnlaceDoRoteador) {
		this.taxaSaidaEnlaceDoRoteador = taxaSaidaEnlaceDoRoteador;
	}

	/**
	 * Maximum Segment Size. Nesse trabalho o valor de MSS = 1500 bytes.
	 * 
	 * @return MSS = 1500 bytes
	 */
	public int getMSS() {
		return mss;
	}
}
