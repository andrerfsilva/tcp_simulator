package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.estatistica.Estimador;
import br.ufrj.ad.simulator.estatistica.Random;

/**
 * Casos de teste para a classe Random.
 * 
 * @author André Ramos, Wellington Mascena, Felipe Teixeira
 * 
 */
public class TesteRandom {

	private Random gerador;
	private Estimador estimador;

	@Before
	public void setUp() throws Exception {
		gerador = new Random();
		estimador = new Estimador();
	}

	@Test
	public void testNextExponential() {
		for (int i = 0; i < 10000; i++) {
			estimador.coletarAmostra(gerador.nextExponential(1.0 / 24.0));
		}

		assertEquals(24.0, estimador.getMedia(),
				estimador.getDistanciaICMedia(0.95));
	}

	@Test
	public void testNextGeometric() {
		for (int i = 0; i < 10000; i++) {
			estimador.coletarAmostra(gerador.nextGeometric(1.0 / 10.0));
		}

		assertEquals(10.0, estimador.getMedia(),
				estimador.getDistanciaICMedia(0.95));

	}

	@Test
	public void testSeed1() {

		long seed = gerador.nextLong();
		Random gerador1 = new Random(seed);
		Random gerador2 = new Random(seed);

		assertEquals(gerador1.nextDouble(), gerador2.nextDouble(), 0);

	}

	@Test
	public void testSeed2() {

		long seed = gerador.nextLong();
		Random gerador1 = new Random(seed);
		Random gerador2 = new Random(seed);

		double[] seq1 = new double[10000];
		double[] seq2 = new double[10000];

		for (int i = 0; i < seq1.length; i++) {
			seq1[i] = gerador1.nextDouble();
			seq2[i] = gerador2.nextDouble();
		}

		assertArrayEquals(seq1, seq2, 0);

	}

	@Test
	public void testSeed3() {

		Random gerador1 = new Random(gerador.getSeed());
		Random gerador2 = new Random(gerador1.getSeed());

		double[] seq1 = new double[10000];
		double[] seq2 = new double[10000];

		for (int i = 0; i < seq1.length; i++) {
			seq1[i] = gerador1.nextDouble();
			seq2[i] = gerador2.nextDouble();
		}

		assertArrayEquals(seq1, seq2, 0);

	}

}
