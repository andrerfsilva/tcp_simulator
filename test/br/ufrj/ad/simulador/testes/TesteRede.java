package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.models.Parametros;
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
	private Parametros parametros;

	@Before
	public void setUp() throws Exception {
		parametros = new Parametros();
	}

	@Test
	public void testInicializacaoTx1() {

		parametros.setProperty("EstacoesGrupo1", "10");
		parametros.setProperty("EstacoesGrupo2", "5");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(15, rede.getTransmissores().length);
	}

	@Test
	public void testInicializacaoTx2() {

		parametros.setProperty("EstacoesGrupo1", "10");
		parametros.setProperty("EstacoesGrupo2", "12");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(22, rede.getTransmissores().length);
	}

	@Test
	public void testInicializacaoRx1() {

		parametros.setProperty("EstacoesGrupo1", "62");
		parametros.setProperty("EstacoesGrupo2", "4");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(66, rede.getReceptores().length);
	}

	@Test
	public void testInicializacaoRx2() {

		parametros.setProperty("EstacoesGrupo1", "6");
		parametros.setProperty("EstacoesGrupo2", "234");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(240, rede.getReceptores().length);
	}

	@Test
	public void testRoteadorFIFO() {

		parametros.setProperty("EstacoesGrupo1", "20");
		parametros.setProperty("EstacoesGrupo2", "10");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(RoteadorFIFO.class, rede.getRoteador().getClass());
	}

	@Test
	public void testRoteadorRED() {

		parametros.setProperty("EstacoesGrupo1", "73");
		parametros.setProperty("EstacoesGrupo2", "12");
		parametros.setProperty("DisciplinaRoteador", "RED");

		rede = new Rede(parametros);

		assertEquals(RoteadorRED.class, rede.getRoteador().getClass());
	}

	@Test
	public void testGrupos1() {

		parametros.setProperty("EstacoesGrupo1", "1");
		parametros.setProperty("EstacoesGrupo2", "0");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(1, rede.getTransmissores()[0].getGrupo());
	}

	@Test
	public void testGrupos2() {

		parametros.setProperty("EstacoesGrupo1", "0");
		parametros.setProperty("EstacoesGrupo2", "1");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);
		assertEquals(2, rede.getTransmissores()[0].getGrupo());
	}

	@Test
	public void testGrupos3() {

		parametros.setProperty("EstacoesGrupo1", "0");
		parametros.setProperty("EstacoesGrupo2", "3");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);
		assertTrue((rede.getTransmissores()[0].getGrupo() == 2)
				&& (rede.getTransmissores()[1].getGrupo() == 2)
				&& (rede.getTransmissores()[2].getGrupo() == 2));
	}

	@Test
	public void testGrupos4() {

		parametros.setProperty("EstacoesGrupo1", "4");
		parametros.setProperty("EstacoesGrupo2", "3");
		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertTrue((rede.getTransmissores()[0].getGrupo() == 1)
				&& (rede.getTransmissores()[1].getGrupo() == 1)
				&& (rede.getTransmissores()[2].getGrupo() == 1)
				&& (rede.getTransmissores()[3].getGrupo() == 1)
				&& (rede.getTransmissores()[4].getGrupo() == 2)
				&& (rede.getTransmissores()[5].getGrupo() == 2)
				&& (rede.getTransmissores()[6].getGrupo() == 2));
	}

	@Test
	public void testTamanhoBufferRoteador1() {

		parametros.setProperty("DisciplinaRoteador", "FIFO");
		parametros.setProperty("TamanhoBufferRoteador", "120");

		rede = new Rede(parametros);

		assertEquals(120, rede.getRoteador().getTamanhoBuffer());
	}

	@Test
	public void testTamanhoBufferRoteador2() {

		parametros.setProperty("DisciplinaRoteador", "RED");
		parametros.setProperty("TamanhoBufferRoteador", "85");

		rede = new Rede(parametros);

		assertEquals(85, rede.getRoteador().getTamanhoBuffer());
	}

	@Test
	public void testDisciplinaRoteador1() {

		parametros.setProperty("DisciplinaRoteador", "RED");

		rede = new Rede(parametros);

		assertEquals(RoteadorRED.class, rede.getRoteador().getClass());
	}

	@Test
	public void testDisciplinaRoteador2() {

		parametros.setProperty("DisciplinaRoteador", "FIFO");

		rede = new Rede(parametros);

		assertEquals(RoteadorFIFO.class, rede.getRoteador().getClass());
	}

}
