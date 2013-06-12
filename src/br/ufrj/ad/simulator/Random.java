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

	public Random(long arg0) {
		super(arg0);
	}

	/**
	 * Gera um número exponencialmente distribído com taxa = {@code rate}. Isso
	 * é feito através da inversa da c.d.f. da distribuição exponencial.
	 * 
	 * @param rate
	 *            a taxa média do número de ocorrências por unidade de tempo
	 * @return número exponencialmente distribído com taxa = {@code rate}
	 */
	public double nextExponetial(double rate) {
		double result;
		double u;

		u = nextDouble(); // número uniforme entre (0, 1)
		result = -Math.log(1 - u) / rate; // inversa da c.d.f. da exponencial

		return result;
	}

	// TODO: Esse método será deletado em breve. Escrevi só para ter o feeling
	// da coisa.
	public static void main(String args[]) {

		/*
		 * Gera várias amostas da distribuição exponecial e faz uma estimativa
		 * da média real com IC com 90% de confiança. O objetivo é simplesmente
		 * verificar se o gerador está gerando a distribuição que queremos.
		 */
		Random generator = new Random();
		int numberOfSamples = 1000;
		double rate = 0.1;
		double mean = 0; // estimador da média
		double variance = 0; // estimador da variância

		// TODO: Para um número de armostras muito grande, o estimador dá
		// overflow! Com 100.000 amostras já dá esse problema. Pensar em um
		// algoritmo melhor.
		for (int i = 0; i < numberOfSamples; i++) {
			double sample = generator.nextExponetial(rate);
			mean += sample;
			variance += sample * sample;
		}
		variance = (variance / (numberOfSamples - 1))
				- (mean * mean / ((numberOfSamples - 1) * numberOfSamples));
		mean /= numberOfSamples;

		/*
		 * O percentil uzado para um IC com 90% de confiança é z0,95 = 1,645.
		 * Lembrando que alpha = 10%. Estamos usando valores assintóticos pois o
		 * número de amostras é muito grande.
		 */
		double icDistance = 1.645 * Math.sqrt(variance / numberOfSamples);

		System.out.println("sample mean     = " + mean);
		System.out.println("sample variance = " + variance);
		System.out.println("mean estimate   = " + mean + " ± " + icDistance
				+ " (CI 90%)");
	}
}
