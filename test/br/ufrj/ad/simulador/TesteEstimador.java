package br.ufrj.ad.simulador;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.Estimador;

/**
 * Casos de teste para a classe Estimador.
 * 
 * @author André Ramos
 * 
 */
public class TesteEstimador {

	private Estimador estimador;

	@Before
	public void setUp() throws Exception {
		estimador = new Estimador();
	}

	@Test
	public void testGetMediaUmaAmostra() {
		estimador.coletarAmostra(1200);
		assertEquals(1200, estimador.getMedia(), 0);
	}

	@Test
	public void testGetVarianciaUmaAmostra() {
		estimador.coletarAmostra(200);
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetVariancia2AmostrasIguais() {
		estimador.coletarAmostra(200);
		estimador.coletarAmostra(200);
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetVariancia100AmostrasIguais() {
		for (int i = 0; i < 100; i++) {
			estimador.coletarAmostra(62);
		}
		System.out.println(estimador.getMedia());
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetDesvioPadrao() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMaxICMedia() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInfICMedia() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDistanciaICMedia() {
		fail("Not yet implemented");
	}

}
