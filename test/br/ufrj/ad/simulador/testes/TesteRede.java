package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.models.Rede;
import br.ufrj.ad.simulator.models.RoteadorFIFO;
import br.ufrj.ad.simulator.models.RoteadorRED;

/**
 * Casos de teste para a classe Rede.
 * 
 * @author André Ramos
 * 
 */
public class TesteRede {

	private Rede rede;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInicializacaoTx1() {
		rede = new Rede(10, 5, "FIFO");
		assertEquals(15, rede.getTransmissores().length);
	}

	@Test
	public void testInicializacaoTx2() {
		rede = new Rede(10, 12, "FIFO");
		assertEquals(22, rede.getTransmissores().length);
	}

	@Test
	public void testInicializacaoRx1() {
		rede = new Rede(62, 4, "FIFO");
		assertEquals(66, rede.getReceptores().length);
	}

	@Test
	public void testInicializacaoRx2() {
		rede = new Rede(6, 234, "FIFO");
		assertEquals(240, rede.getReceptores().length);
	}

	@Test
	public void testRoteadorFIFO() {
		rede = new Rede(20, 10, "FIFO");
		assertEquals(RoteadorFIFO.class, rede.getRoteador().getClass());
	}

	@Test
	public void testRoteadorRED() {
		rede = new Rede(73, 12, "RED");
		assertEquals(RoteadorRED.class, rede.getRoteador().getClass());
	}

	@Test
	public void testGrupos1() {
		rede = new Rede(1, 0, "FIFO");
		assertEquals(1, rede.getTransmissores()[0].getGrupo());
	}

	@Test
	public void testGrupos2() {
		rede = new Rede(0, 1, "FIFO");
		assertEquals(2, rede.getTransmissores()[0].getGrupo());
	}

	@Test
	public void testGrupos3() {
		rede = new Rede(0, 3, "FIFO");
		assertTrue((rede.getTransmissores()[0].getGrupo() == 2)
				&& (rede.getTransmissores()[1].getGrupo() == 2)
				&& (rede.getTransmissores()[2].getGrupo() == 2));
	}
	
	@Test
	public void testGrupos4() {
		rede = new Rede(4, 3, "FIFO");
		assertTrue((rede.getTransmissores()[0].getGrupo() == 1)
				&& (rede.getTransmissores()[1].getGrupo() == 1)
				&& (rede.getTransmissores()[2].getGrupo() == 1)
				&& (rede.getTransmissores()[3].getGrupo() == 1)
				&& (rede.getTransmissores()[4].getGrupo() == 2)
				&& (rede.getTransmissores()[5].getGrupo() == 2)
				&& (rede.getTransmissores()[6].getGrupo() == 2));
	}

}
