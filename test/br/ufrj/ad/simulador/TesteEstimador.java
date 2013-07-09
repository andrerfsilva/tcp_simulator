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
	public void testGetMediaDuasAmostrasIguais() {
		estimador.coletarAmostra(543);
		estimador.coletarAmostra(543);
		assertEquals(543, estimador.getMedia(), 0);
	}

	@Test
	public void testGetMediaDuasAmostras() {
		estimador.coletarAmostra(10);
		estimador.coletarAmostra(20);
		assertEquals(15, estimador.getMedia(), 0);
	}

	@Test
	public void testGetMedia100PrimeirosInteiros() {
		for (int i = 1; i <= 100; i++) {
			estimador.coletarAmostra(i);
		}
		assertEquals(5050.0/100, estimador.getMedia(), 0);
	}

	@Test
	public void testGetVarianciaUmaAmostra() {
		estimador.coletarAmostra(200);
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetVariancia2AmostrasIguais() {
		estimador.coletarAmostra(971);
		estimador.coletarAmostra(971);
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetVariancia100AmostrasIguais() {
		for (int i = 0; i < 100; i++) {
			estimador.coletarAmostra(62);
		}
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetVariancia1000AmostrasIguais() {
		for (int i = 0; i < 1000; i++) {
			estimador.coletarAmostra(98);
		}
		assertEquals(0, estimador.getVariancia(), 0);
	}

	@Test
	public void testGetDesvioPadrao300AmostrasIguais() {
		for (int i = 0; i < 300; i++) {
			estimador.coletarAmostra(731);
		}
		assertEquals(0, estimador.getDesvioPadrao(), 0);
	}

	@Test
	public void testGetMaxICMedia450AmostrasIguais() {
		for (int i = 0; i < 450; i++) {
			estimador.coletarAmostra(423);
		}
		assertEquals(423, estimador.getMaxICMedia(0.99), 0);
	}

	@Test
	public void testGetInfICMedia10000AmostrasIguais() {
		for (int i = 0; i < 10000; i++) {
			estimador.coletarAmostra(999);
		}
		assertEquals(999, estimador.getInfICMedia(0.99), 0);
	}

	@Test
	public void testGetDistanciaICMedia250AmostrasIguais() {
		for (int i = 0; i < 250; i++) {
			estimador.coletarAmostra(-820);
		}
		assertEquals(0, estimador.getDistanciaICMedia(0.99), 0);
	}

}
