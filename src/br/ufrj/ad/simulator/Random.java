package br.ufrj.ad.simulator;

/**
 * Estende a classe Random para gerar n�meros das disribui��es presentes na
 * simula��o.
 * 
 * @author Andr� Ramos
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
	 * Gera um n�mero exponencialmente distrib�do com taxa = {@code rate}. Isso
	 * � feito atrav�s da inversa da c.d.f. da distribui��o exponencial.
	 * 
	 * @param rate
	 *            a taxa m�dia do n�mero de ocorr�ncias por unidade de tempo
	 * @return n�mero exponencialmente distrib�do com taxa = {@code rate}
	 */
	public double nextExponetial(double rate) {
		double result;
		double u;

		u = nextDouble(); // n�mero uniforme entre (0, 1)
		result = -Math.log(1 - u) / rate; // inversa da c.d.f. da exponencial

		return result;
	}

	// TODO: Esse m�todo ser� deletado em breve. Escrevi s� para ter o feeling
	// da coisa.
	public static void main(String args[]) {

		/*
		 * Gera v�rias amostas da distribui��o exponecial e faz uma estimativa
		 * da m�dia real com IC com 90% de confian�a. O objetivo � simplesmente
		 * verificar se o gerador est� gerando a distribui��o que queremos.
		 */
		Random generator = new Random();
		int numberOfSamples = 1000;
		double rate = 0.1;
		double mean = 0; // estimador da m�dia
		double variance = 0; // estimador da vari�ncia

		// TODO: Para um n�mero de armostras muito grande, o estimador d�
		// overflow! Com 100.000 amostras j� d� esse problema. Pensar em um
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
		 * O percentil uzado para um IC com 90% de confian�a � z0,95 = 1,645.
		 * Lembrando que alpha = 10%. Estamos usando valores assint�ticos pois o
		 * n�mero de amostras � muito grande.
		 */
		double icDistance = 1.645 * Math.sqrt(variance / numberOfSamples);

		System.out.println("sample mean     = " + mean);
		System.out.println("sample variance = " + variance);
		System.out.println("mean estimate   = " + mean + " � " + icDistance
				+ " (CI 90%)");
	}
}
