package br.ufrj.ad.simulator;

/**
 * Essa classe será responsável por receber um conjunto de amostras e calcular
 * medidas estatísticas como média, variância, intervalo de confiança (IC), etc
 * a partir das amostras. Cada conjunto de amostras deve instanciar seu próprio
 * Estimador. Esse Estimador otimiza o uso de memória estimando os parâmetros de
 * interesse de forma incremental, ou seja, sem a necessidade de armazenar todas
 * as amotras colhidas na memória.
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
	 * interesse.
	 * 
	 * @param amostra
	 *            uma amostra da v.a. de interesse
	 */
	public void coletarAmostra(double amostra) {
		somaAmostras += amostra;
		somaQuadradoAmostras += amostra * amostra;
		numeroAmostras++;
	}

	/**
	 * Estimador não-tendencioso e consistente da média calculado a partir das
	 * amostras fornecidas pelo método coletarAmostras().
	 * 
	 * @return estimador da média
	 */
	public double getMedia() {
		return somaAmostras / numeroAmostras;
	}

	/**
	 * Estimador não-tendencioso e consistente da variância calculado a partir
	 * das amostras fornecidas pelo método coletarAmostras().
	 * 
	 * @return estimador da variância
	 */
	public double getVariancia() {
		return (somaQuadradoAmostras / numeroAmostras)
				- (somaAmostras * somaAmostras / ((numeroAmostras - 1) * numeroAmostras));
	}

	/**
	 * Estimador do desvio padrão, ou seja, simplesmente a raiz quadrada da
	 * variância.
	 * 
	 * @return estimador do desvio padrão
	 */
	public double getDesvioPadrao() {
		return Math.sqrt(getVariancia());
	}

	/**
	 * Exemplo de uso de um Estimador em conjunto com nosso gerador
	 * pseudo-aleatório Random extended edition.
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

		int numberOfSamples = 10000;
		double p = 0.1;

		// TODO: Para um número de armostras muito grande ocorre overflow! Com
		// 100.000 amostras já dá esse problema. Pensar em um algoritmo melhor.
		for (int i = 0; i < numberOfSamples; i++) {
			double sample = generator.nextGeometric(p);
			estimador.coletarAmostra(sample);
		}

		/*
		 * O percentil uzado para um IC com 90% de confiança é z0,95 = 1,645.
		 * Lembrando que alpha = 10%. Estamos usando o percentil assintótico da
		 * normal pois o número de amostras é muito grande.
		 */
		double ciDistance = 1.645 * Math.sqrt(estimador.getVariancia()
				/ numberOfSamples);

		System.out.println("sample mean     = " + estimador.getMedia());
		System.out.println("sample variance = " + estimador.getVariancia());
		System.out.println("mean estimate   = " + estimador.getMedia() + " ± "
				+ ciDistance + " (CI 90%)");
	}
}
