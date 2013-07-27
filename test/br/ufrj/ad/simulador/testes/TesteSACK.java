package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import br.ufrj.ad.simulator.model.SACK;

/**
 * Casos de teste para a classe SACK.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class TesteSACK {

	private SACK sack1, sack2;

	@Test
	public void testEquals1() {
		sack1 = new SACK(0, 1500);
		sack2 = new SACK(0, 1500);

		assertEquals(sack1, sack2);
	}

	@Test
	public void testEqualsComSequencia() {

		long[][] sequencia1 = new long[1][2];
		long[][] sequencia2 = new long[1][2];

		sequencia1[0][0] = sequencia2[0][0] = 3000;
		sequencia1[0][1] = sequencia2[0][1] = 4499;

		sack1 = new SACK(0, 1500, sequencia1);
		sack2 = new SACK(0, 1500, sequencia2);

		assertEquals(sack1, sack2);
	}

	@Test
	public void testNotEqualsComNull() {

		long[][] sequencia1 = new long[1][2];

		sequencia1[0][0] = 3000;

		sack1 = new SACK(2, 1500, sequencia1);
		sack2 = new SACK(2, 1500);

		assertNotSame(sack1, sack2);
	}

	@Test
	public void testNotEqualsComSequenciaDiferente() {

		long[][] sequencia1 = new long[1][2];
		long[][] sequencia2 = new long[1][2];

		sequencia1[0][0] = sequencia2[0][0] = 3000;
		sequencia1[0][1] = 4499;
		sequencia2[0][1] = 5999;

		sack1 = new SACK(3, 1500, sequencia1);
		sack2 = new SACK(3, 1500, sequencia2);

		assertNotSame(sack1, sack2);
	}

	@Test
	public void testNotEqualsComSequenciaVetoresTamanhoDiferente() {

		long[][] sequencia1 = new long[1][2];
		long[][] sequencia2 = new long[2][2];

		sequencia1[0][0] = sequencia2[0][0] = 3000;
		sequencia1[0][1] = sequencia2[0][1] = 4499;
		
		sequencia2[1][0] = 7500;
		sequencia2[1][1] = 8999;
		

		sack1 = new SACK(4, 1500, sequencia1);
		sack2 = new SACK(4, 1500, sequencia2);

		assertNotSame(sack1, sack2);
	}
	
	@Test
	public void testEqualsComSequenciaDuasLinhas() {

		long[][] sequencia1 = new long[2][2];
		long[][] sequencia2 = new long[2][2];

		sequencia1[0][0] = sequencia2[0][0] = 3000;
		sequencia1[0][1] = sequencia2[0][1] = 4499;
		
		sequencia1[1][0] = sequencia2[1][0] = 7500;
		sequencia1[1][1] = sequencia2[1][1] = 8999;

		sack1 = new SACK(5, 1500, sequencia1);
		sack2 = new SACK(5, 1500, sequencia2);

		assertEquals(sack1, sack2);
	}
	
	@Test
	public void testNotEqualsComSequenciaDuasLinhas() {

		long[][] sequencia1 = new long[2][2];
		long[][] sequencia2 = new long[2][2];

		sequencia1[0][0] = sequencia2[0][0] = 3000;
		sequencia1[0][1] = sequencia2[0][1] = 4499;
		
		sequencia1[1][0] = sequencia2[1][0] = 7500;
		sequencia1[1][1] = 8999;
		sequencia2[1][1] = 11999;

		sack1 = new SACK(10, 1500, sequencia1);
		sack2 = new SACK(10, 1500, sequencia2);

		assertNotSame(sack1, sack2);
	}
	
	@Test
	public void testVectorTam0VectorNull(){
		long[][] sequencia1 = new long[0][2];
		
		sack1 = new SACK(10, 1500, sequencia1);
		sack2 = new SACK(10, 1500);
		
		assertEquals(sack1, sack2);
		
	}

}
