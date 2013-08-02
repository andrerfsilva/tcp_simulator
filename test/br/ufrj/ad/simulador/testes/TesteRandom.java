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
			estimador.coletarAmostra(gerador.nextExponential(1.0/24.0));
		}
		
		assertEquals(24.0, estimador.getMedia(), estimador.getDistanciaICMedia(0.95));
	}

	@Test
	public void testNextGeometric() {
		for (int i = 0; i < 10000; i++) {
			estimador.coletarAmostra(gerador.nextGeometric(1.0/10.0));
		}
		
		assertEquals(10.0, estimador.getMedia(), estimador.getDistanciaICMedia(0.95));
		
	}

}
