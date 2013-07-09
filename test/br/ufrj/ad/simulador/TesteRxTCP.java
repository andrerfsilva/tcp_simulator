package br.ufrj.ad.simulador;

import static org.junit.Assert.*;

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

}
