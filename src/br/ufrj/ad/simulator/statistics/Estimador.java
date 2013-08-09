package br.ufrj.ad.simulator.statistics;

import umontreal.iro.lecuyer.probdist.StudentDist;

/**
 * Essa classe será responsável por receber um conjunto de amostras e calcular
 * medidas estatísticas como média, variância, intervalo de confiança (IC), etc
 * a partir das amostras. Cada conjunto de amostras deve instanciar seu próprio
 * Estimador. Esse Estimador otimiza o uso de memória estimando a média e a
 * variância de forma incremental, ou seja, sem a necessidade de armazenar todas
 * as amotras colhidas na memória.
 * 
 * Para o cálculo do IC, o percentil da t-student é escolhido em função da
 * confiança e do número de amostras colhidas até então. Independentemente do
 * número de amostras o percentil é calculado numericamente a partir da i.c.d.f
 * da t-student, ou seja, não usamos valores assintóticos para N<30.
 * 
 * Importante: as amostras devem ser independentes e identicamente distribuídas.
 * 
 * @author André Ramos
 * 
 */
public class Estimador {

	private double somaAmostras;
	private double somaQuadradoAmostras;
	private int numeroAmostras;

	public Estimador() {
		somaAmostras = 0;
		somaQuadradoAmostras = 0;
		numeroAmostras = 0;
	}

	/**
	 * Informa ao Estimador o valor de uma nova amostra da variável aleatória de
	 * interesse. Importante: amostras precisam ser independentes e
	 * identicamente distribuídas.
	 * 
	 * @param amostra
	 *            Uma amostra da v.a. de interesse.
	 */
	public void coletarAmostra(double amostra) {
		somaAmostras += amostra;
		somaQuadradoAmostras += amostra * amostra;
		numeroAmostras++;
	}

	/**
	 * Estimador não-tendencioso e consistente da média calculado a partir das
	 * amostras fornecidas pelo método coletarAmostras(). O estimador da média é
	 * calculado de forma incremental para economizar memória.
	 * 
	 * @return Estimativa da média.
	 */
	public double getMedia() {
		return somaAmostras / numeroAmostras;
	}

	/**
	 * Estimador não-tendencioso e consistente da variância calculado a partir
	 * das amostras fornecidas pelo método coletarAmostras(). O estimador da
	 * variância é calculado de forma incremental para economizar memória.
	 * 
	 * @return estimador da variância
	 */
	public double getVariancia() {
		if (numeroAmostras > 1) {
			return (somaQuadradoAmostras / (numeroAmostras - 1))
					- (somaAmostras * somaAmostras / ((numeroAmostras - 1) * numeroAmostras));
		} else {
			return 0;
		}
	}

	/**
	 * Estimador do desvio padrão, ou seja, simplesmente a raiz quadrada da
	 * variância. O estimador do desvio padrão é calculado de forma incremental
	 * para economizar memória.
	 * 
	 * @return Estimador do desvio padrão.
	 */
	public double getDesvioPadrao() {
		return Math.sqrt(getVariancia());
	}

	/**
	 * Calcula U(α) o limite superior do IC para a dada confiança.
	 * 
	 * @param confianca
	 *            Probabilidade da média real estar entre U(α) e L(α).
	 * @return U(α)
	 */
	public double getMaxICMedia(double confianca) {
		return getMedia() + getDistanciaICMedia(confianca);
	}

	/**
	 * Calcula L(α) o limite superior do IC para a dada confiança.
	 * 
	 * @param confianca
	 *            Probabilidade da média real estar entre U(α) e L(α).
	 * @return L(α)
	 */
	public double getInfICMedia(double confianca) {
		return getMedia() - getDistanciaICMedia(confianca);
	}

	/**
	 * Calcula entre a distância da média amostral e os limites inferiores e
	 * superiores do IC.
	 * 
	 * @param confianca
	 *            Probabilidade da média real estar entre U(α) e L(α).
	 * @return Distância entre a média amostral e U(α) = distância entre média
	 *         amostal e L(α), ou NaN se existir apenas uma amostra.
	 */
	public double getDistanciaICMedia(double confianca) {
		if (numeroAmostras > 1) {
			double alpha = 1 - confianca;
			double percentil = StudentDist.inverseF(numeroAmostras - 1,
					1 - alpha / 2);

			return percentil * Math.sqrt(getVariancia() / numeroAmostras);
		} else {
			return Double.NaN;
		}
	}

	public int getNumeroAmostras() {
		return numeroAmostras;
	}

	/**
	 * Exemplo de uso de um Estimador em conjunto com nosso gerador de números
	 * aleatórios Random extended edition.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		/*
		 * Gera várias amostas da distribuição geométrica e faz uma estimativa
		 * da média real com IC com 90% de confiança. O objetivo é simplesmente
		 * verificar se o gerador se comportando de acordo com a distribuição
		 * que queremos.
		 */
		Random generator = new Random();
		Estimador estimador = new Estimador();

		int numberOfSamples = 1000;
		double p = 0.1;

		for (int i = 0; i < numberOfSamples; i++) {
			double sample = generator.nextGeometric(p);
			estimador.coletarAmostra(sample);
		}

		System.out.println("média amostral      = " + estimador.getMedia());
		System.out.println("variância amostral  = " + estimador.getVariancia());

		/*
		 * Testando se nossa função está calculando o percentil correto para 90%
		 * de confiança.
		 */
		System.out
				.println("estimativa da média = "
						+ estimador.getMedia()
						+ " ± "
						+ estimador.getDistanciaICMedia(0.9)
						+ " (IC 90%) calculado numericamente pela i.c.d.f. da t-student");
	}
}
