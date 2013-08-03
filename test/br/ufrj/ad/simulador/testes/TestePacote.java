package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.Parametros;

public class TestePacote {

	private Pacote pacote;

	@Before
	public void setUp() throws Exception {
		/* Cria um pacote default de tráfego de fundo. */
		pacote = new Pacote();
	}

	@Test
	public void testGetTamanho() {
		assertEquals(Parametros.mss, pacote.getTamanho());
	}

}
