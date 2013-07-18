package br.ufrj.ad.simulator;

import java.util.PriorityQueue;

/**
 * Essa classe gerencia os eventos, as atualizações no modelo do sistema
 * simulado e a coleta de estatísticas de interesse.
 * 
 * @author André Ramos, Welligton Mascena
 * 
 */
public class Simulador {

	/**
	 * Modelo do sistema a ser simulado.
	 */
	private Rede rede;

	/**
	 * Fila de eventos ordenados no tempo.
	 */
	private PriorityQueue<Evento> filaEventos;

	/**
	 * Tempo atual simulado em milisegundos.
	 */
	private double tempoAtualSimulado;

	/**
	 * Número médio de pacotes que chegam em uma rajada de tamanho geométrico,
	 * ou seja, o número de pacotes é uma variável aleatória geométrica.
	 */
	private long tamanhoMedioRajada;

	/**
	 * Tempo médio entre as chegadas Poisson. Lembrando que essa é uma variável
	 * aleatória exponencial com média 1/(taxa de chegada).
	 */
	private double tempoMedioEntreRajadas;

	/**
	 * Usado para calcular as variáveis aleatórias da simulação.
	 */
	private Random geradorNumerosAleatorios;

	/**
	 * Taxa de saída no enlace do rotedor em bps. Parâmetro Cg na definição do
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

	public long getTamanhoMedioRajada() {
		return tamanhoMedioRajada;
	}

	public void setTamanhoMedioRajada(long tamanhoMedioRajada) {
		this.tamanhoMedioRajada = tamanhoMedioRajada;
	}

	public double getTempoMedioEntreRajadas() {
		return tempoMedioEntreRajadas;
	}

	public void setTempoMedioEntreRajadas(double tempoMedioEntreRajadas) {
		this.tempoMedioEntreRajadas = tempoMedioEntreRajadas;
	}

	public void simular() {

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
			tempoAtualSimulado += e.getTempo();
			tratarEvento(e);

		}

		// TODO coletar estatísticas
	}

	private boolean estatisticasSatisfatorias() {
		// TODO escrever a condição de parada do loop de simulação
		return false;
	}

	private void tratarEvento(Evento e) {
		// TODO definir eventos, como eles alteram o estado do sistema, e
		// agendar novos eventos!
		if (e instanceof EventoRoteadorRecebeTrafegoDeFundo) {

			tratarEventoRoteadorRecebeTrafegoDeFundo();

		} else if (e instanceof EventoRoteadorEnviaPacote) {
			// TODO: fazer esse evento.
		}
	}

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
					tempoAtualSimulado + mss / taxaSaidaEnlaceDoRoteador);
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
}
