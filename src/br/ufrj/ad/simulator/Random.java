package br.ufrj.ad.simulator;

/**
 * Estende a classe Random para gerar números das disribuições presentes na
 * simulação.
 * 
 * @author André Ramos
 * 
 */
public class Random extends java.util.Random {

	public Random() {
		super();
	}

	public Random(long seed) {
		super(seed);
	}

	/**
	 * Gera um número exponencialmente distribuído com taxa = {@code rate}. Isso
	 * é feito através da inversa da c.d.f. da distribuição exponencial.
	 * 
	 * @param rate
	 *            a taxa média do número de ocorrências por unidade de tempo
	 * @return número exponencialmente distribuído com taxa = {@code rate}
	 */
	public double nextExponential(double rate) {
		double sample;
		double u;

		u = nextDouble(); // número uniforme entre (0, 1)
		sample = -Math.log(1 - u) / rate; // inversa da c.d.f. da exponencial

		return sample;
	}

	/**
	 * Gera uma amostra da variável aleatória geométrica N com probabilidade de
	 * obter o primeiro sucesso de Bernoulli = p e conjunto de suporte = {1, 2,
	 * 3, ...}, ou seja, P(N=k) = (1-p)^(k-1) * p, k>=1.
	 * 
	 * @param p
	 *            probabilidade de sucesso num teste de Bernoulli
	 * @return um número maior ou igual a 1 geometricamente distribuído
	 * 
	 */
	public double nextGeometric(double p) {
		double sample;
		double u;

		/*
		 * O resultado a seguir pode ser encontrado na aula05, páginas 6 e 7.
		 * Ele também é obtito a partir da c.d.f. da distribuição geométrica.
		 */
		u = nextDouble();
		sample = Math.ceil(Math.log(1 - u) / Math.log(1 - p));

		return sample;
	}

	// TODO: Esse método será deletado em breve. Escrevi só para ter o feeling
	// da coisa.
	public static void main(String args[]) {

		/*
		 * Gera várias amostas da distribuição geométrica e faz uma estimativa
		 * da média real com IC com 90% de confiança. O objetivo é simplesmente
		 * verificar se o gerador se comportando de acordo com a distribuição
		 * que queremos.
		 */
		Random generator = new Random();
		int numberOfSamples = 10000;
		double p = 0.1;
		double mean = 0; // estimador da média
		double variance = 0; // estimador da variância

		// TODO: Para um número de armostras muito grande ocorre overflow! Com
		// 100.000 amostras já dá esse problema. Pensar em um algoritmo melhor.
		for (int i = 0; i < numberOfSamples; i++) {
			double sample = generator.nextGeometric(p);
			mean += sample;
			variance += sample * sample;
		}
		variance = (variance / (numberOfSamples - 1))
				- (mean * mean / ((numberOfSamples - 1) * numberOfSamples));
		mean /= numberOfSamples;

		/*
		 * O percentil uzado para um IC com 90% de confiança é z0,95 = 1,645.
		 * Lembrando que alpha = 10%. Estamos usando o percentil assintótico da
		 * normal pois o número de amostras é muito grande.
		 */
		double ciDistance = 1.645 * Math.sqrt(variance / numberOfSamples);

		System.out.println("sample mean     = " + mean);
		System.out.println("sample variance = " + variance);
		System.out.println("mean estimate   = " + mean + " ± " + ciDistance
				+ " (CI 90%)");
	}
}
