package br.ufrj.ad.simulador;

import static org.junit.Assert.*;

import org.junit.*;

import br.ufrj.ad.simulator.Pacote;
import br.ufrj.ad.simulator.Roteador;
import br.ufrj.ad.simulator.RoteadorFIFO;

/**
 * Casos de teste da classe RoteadorFIFO.
 * 
 * @author André Ramos, Felipe Teixeira, Wellington Mascena
 * 
 */
public class TesteRoteadorFIFO {

	private Roteador roteador;

	@Before
	public void runBeforeEveryTest() {
		roteador = new RoteadorFIFO();
	}

	@Test
	public void testReceberUmPacote() {
		roteador.setTamanhoBuffer(40);
		roteador.receberPacote(new Pacote());
		assertEquals(1, roteador.getNumeroPacotes());
	}

	@Test
	public void testReceber100Pacotes() {
		roteador.setTamanhoBuffer(40);
		for (int i = 0; i < 100; i++) {
			roteador.receberPacote(new Pacote());
		}
		assertEquals(40, roteador.getNumeroPacotes());
	}

	@Test
	public void testReceber39Pacotes() {
		roteador.setTamanhoBuffer(40);
		for (int i = 0; i < 39; i++) {
			roteador.receberPacote(new Pacote());
		}
		assertEquals(39, roteador.getNumeroPacotes());
	}

	@Test
	public void testReceber3Pacotes() {
		roteador.setTamanhoBuffer(2);
		for (int i = 0; i < 2; i++) {
			roteador.receberPacote(new Pacote());
		}
		boolean recebeuUltimo = roteador.receberPacote(new Pacote());
		assertFalse(recebeuUltimo);
	}
	
	@Test
	public void testReceber1Pacote() {
		roteador.setTamanhoBuffer(5);
		boolean recebeu = roteador.receberPacote(new Pacote());
		assertTrue(recebeu);
	}

	@Test
	public void testEnviarProximoPacote() {
		roteador.setTamanhoBuffer(40);
		roteador.receberPacote(new Pacote());
		roteador.enviarProximoPacote();
		assertEquals(0, roteador.getNumeroPacotes());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testEnviarProximoPacoteBufferVazio() {
		roteador.setTamanhoBuffer(40);
		roteador.receberPacote(new Pacote());
		roteador.enviarProximoPacote();
		roteador.enviarProximoPacote();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testEnviarProximoPacoteBufferVazio2() {
		roteador.setTamanhoBuffer(1);
		roteador.receberPacote(new Pacote());
		roteador.receberPacote(new Pacote());
		roteador.enviarProximoPacote();
		roteador.enviarProximoPacote();
		roteador.enviarProximoPacote();
	}

	@Test
	public void testGetProximoPacote() {
		roteador.setTamanhoBuffer(40);
		Pacote p = new Pacote();
		roteador.receberPacote(p);
		assertEquals(p, roteador.getProximoPacoteAEnviar());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetProximoPacoteBufferVazio() {
		roteador.setTamanhoBuffer(40);
		roteador.getProximoPacoteAEnviar();
	}

	@Test
	public void testFIFO10Pacotes() {
		roteador.setTamanhoBuffer(40);
		Pacote pacotes[] = new Pacote[10];

		for (int i = 0; i < 10; i++) {
			pacotes[i] = new Pacote();
			roteador.receberPacote(pacotes[i]);
		}

		Pacote pacotesEnviados[] = new Pacote[10];

		for (int i = 0; i < 10; i++) {
			pacotesEnviados[i] = roteador.getProximoPacoteAEnviar();
			roteador.enviarProximoPacote();
		}

		assertArrayEquals(pacotes, pacotesEnviados);
	}

	@Test
	public void testFIFO100Pacotes() {
		roteador.setTamanhoBuffer(40);
		Pacote pacotes[] = new Pacote[100];

		for (int i = 0; i < 100; i++) {
			Pacote p = new Pacote();
			if (i < roteador.getTamanhoBuffer()) {
				pacotes[i] = p;
			}
			roteador.receberPacote(p);
		}

		Pacote pacotesEnviados[] = new Pacote[100];

		int i = 0;
		while (roteador.getNumeroPacotes() > 0) {
			pacotesEnviados[i] = roteador.getProximoPacoteAEnviar();
			roteador.enviarProximoPacote();
			i++;
		}

		assertArrayEquals(pacotes, pacotesEnviados);
	}

}
