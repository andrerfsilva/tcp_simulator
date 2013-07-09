package br.ufrj.ad.simulador;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.Pacote;
import br.ufrj.ad.simulator.RxTCP;
import br.ufrj.ad.simulator.SACK;

/**
 * Casos de teste para a classe RxTCP.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class TesteRxTCP {

	RxTCP receptor;

	@Before
	public void setUp() throws Exception {
		receptor = new RxTCP();
	}

	@Test
	public void testReceberPacote() {
		Pacote p = new Pacote();
		p.setDestino(0);
		p.setByteInicialEFinal(0, 1499);

		SACK sackEsperado = new SACK(1500);
		assertEquals(sackEsperado, receptor.receberPacote(p));
	}

	@Test
	public void testReceber101PacotesAleatorios() {

		long numeroDeSequencia = 0;
		Pacote p;
		long tamanhoPacote;
		Random gerador = new Random();

		for (int i = 0; i < 100; i++) {

			tamanhoPacote = gerador.nextInt(1500) + 1;
			p = new Pacote();
			p.setDestino(0);
			p.setByteInicialEFinal(numeroDeSequencia, numeroDeSequencia
					+ tamanhoPacote - 1);
			numeroDeSequencia += tamanhoPacote;
			receptor.receberPacote(p);

		}

		tamanhoPacote = gerador.nextInt(1500) + 1;
		p = new Pacote();
		p.setDestino(0);
		p.setByteInicialEFinal(numeroDeSequencia, numeroDeSequencia
				+ tamanhoPacote - 1);
		numeroDeSequencia += tamanhoPacote;

		SACK sackEsperado = new SACK(numeroDeSequencia);
		assertEquals(sackEsperado, receptor.receberPacote(p));
	}

	@Test
	public void testEnviar1001PacotesAleatoriosComTaxaDeDescarte() {

		long numeroDeSequencia = 0;
		Pacote ultimoPacoteEnviado;
		Pacote primeiroPacoteDescartado = null;
		long tamanhoPacote;
		Random geradorTamanhoPacote = new Random();
		Random geradorDescarte = new Random();

		double probabilidadeDescarte = 0.2;

		for (int i = 0; i < 1000; i++) {

			tamanhoPacote = geradorTamanhoPacote.nextInt(1500) + 1;
			ultimoPacoteEnviado = new Pacote();
			ultimoPacoteEnviado.setDestino(0);
			ultimoPacoteEnviado.setByteInicialEFinal(numeroDeSequencia,
					numeroDeSequencia + tamanhoPacote - 1);
			numeroDeSequencia += tamanhoPacote;
			if (geradorDescarte.nextDouble() >= probabilidadeDescarte) {
				receptor.receberPacote(ultimoPacoteEnviado);
			} else {
				if (primeiroPacoteDescartado == null) {
					primeiroPacoteDescartado = ultimoPacoteEnviado;
				}
			}

		}

		tamanhoPacote = geradorTamanhoPacote.nextInt(1500) + 1;
		ultimoPacoteEnviado = new Pacote();
		ultimoPacoteEnviado.setDestino(0);
		ultimoPacoteEnviado.setByteInicialEFinal(numeroDeSequencia,
				numeroDeSequencia + tamanhoPacote - 1);
		numeroDeSequencia += tamanhoPacote;

		assertEquals(primeiroPacoteDescartado.getByteInicial(), receptor
				.receberPacote(ultimoPacoteEnviado).getProximoByteEsperado());
	}

	@Test
	public void testEnviar10001PacotesAleatoriosComTaxaDeDescarte() {

		long numeroDeSequencia = 0;
		Pacote ultimoPacoteEnviado;
		Pacote primeiroPacoteDescartado = null;
		Pacote segundoPacoteDescartado = null;
		long tamanhoPacote;
		Random geradorTamanhoPacote = new Random();
		Random geradorDescarte = new Random();

		double probabilidadeDescarte = 0.02;

		for (int i = 0; i < 10000; i++) {

			tamanhoPacote = geradorTamanhoPacote.nextInt(1500) + 1;
			ultimoPacoteEnviado = new Pacote();
			ultimoPacoteEnviado.setDestino(0);
			ultimoPacoteEnviado.setByteInicialEFinal(numeroDeSequencia,
					numeroDeSequencia + tamanhoPacote - 1);
			numeroDeSequencia += tamanhoPacote;
			if (geradorDescarte.nextDouble() >= probabilidadeDescarte) {
				receptor.receberPacote(ultimoPacoteEnviado);
			} else {
				if (primeiroPacoteDescartado == null) {
					primeiroPacoteDescartado = ultimoPacoteEnviado;
				} else if (segundoPacoteDescartado == null) {
					segundoPacoteDescartado = ultimoPacoteEnviado;
				}
			}

		}

		assertEquals(segundoPacoteDescartado.getByteInicial(), receptor
				.receberPacote(primeiroPacoteDescartado).getProximoByteEsperado());
	}
	
	@Test
	public void testReceberPacoteDuplicado() {
		Pacote p = new Pacote();
		p.setDestino(0);
		p.setByteInicialEFinal(0, 1499);
		
		receptor.receberPacote(p);

		SACK sackEsperado = new SACK(1500);
		assertEquals(sackEsperado, receptor.receberPacote(p));
	}
	
	@Test
	public void testReceberPacoteDuplicado2() {
		Pacote p1 = new Pacote();
		p1.setDestino(0);
		p1.setByteInicialEFinal(0, 1499);
		
		Pacote p2 = new Pacote();
		p2.setDestino(0);
		p2.setByteInicialEFinal(3000, 4499);
		
		receptor.receberPacote(p1);
		receptor.receberPacote(p2);

		
		long[][] sequenciasEsperadas = new long[1][2];
		sequenciasEsperadas[0][0] = 3000;
		sequenciasEsperadas[0][1] = 4500;
		SACK sackEsperado = new SACK(1500, sequenciasEsperadas);
		SACK sackRecebido = receptor.receberPacote(p1);
				
		assertEquals(sackEsperado, sackRecebido);
	}

}
