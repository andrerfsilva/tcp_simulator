package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.model.Pacote;
import br.ufrj.ad.simulator.model.RoteadorRED;

/**
 * Casos de teste para a classe RoteadorRED
 * 
 * @author André Ramos, Wellington Mascena
 * 
 */
public class TesteRoteadorRED {

	private RoteadorRED roteador;

	@Before
	public void setUp() throws Exception {
		roteador = new RoteadorRED();
		roteador.setTamanhoBuffer(50);
	}

	@Test
	public void testReceber1Pacote() {

		roteador.receberPacote(new Pacote(), 1000);
		assertEquals(1, roteador.getNumeroPacotes());
	}

	@Test
	public void testReceber4Pacote() {

		roteador.receberPacote(new Pacote(), 1000);
		roteador.receberPacote(new Pacote(), 1000);
		roteador.receberPacote(new Pacote(), 1000);
		roteador.receberPacote(new Pacote(), 1000);

		assertEquals(4, roteador.getNumeroPacotes());
	}

	@Test
	public void testReceberPacoteAvgMenorQueMinth() {
		int i = 0;
		roteador.setTamanhoBuffer(1000000);

		while (i < roteador.getTamanhoBuffer()
				&& roteador.getMinth() > roteador.getAvg()) {
			i++;
			roteador.receberPacote(new Pacote());
		}
		System.out.println(i);
		System.out.println(roteador.getAvg());
		assertEquals(i, roteador.getNumeroPacotes());
	}

	@Test
	public void testReceberPacoteAvgMaiorQueMaxth() {
		int i = 0;
		roteador.setTamanhoBuffer(1000000);
		Pacote[] pacotesEsperados = new Pacote[1000000];

		while (i < roteador.getTamanhoBuffer()
				&& roteador.getMaxth() >= roteador.getAvg()) {

			Pacote p = new Pacote();

			if (roteador.receberPacote(p, 1000 + i)) {
				pacotesEsperados[i] = p;
				i++;
			}

		}

		System.out.println(i);
		System.out.println(roteador.getAvg());

		for (int j = 0; j < 2000; j++) {
			roteador.receberPacote(new Pacote());
		}

		Pacote[] pacotesArmazenados = new Pacote[1000000];
		i = 0;
		while (roteador.getNumeroPacotes() > 0) {

			pacotesArmazenados[i] = roteador.getProximoPacoteAEnviar();
			roteador.enviarProximoPacote();
			i++;
		}

		assertArrayEquals(pacotesEsperados, pacotesArmazenados);
	}

	@Test
	public void testEstourarBuffer() {
		roteador.setTamanhoBuffer(1);

		roteador.receberPacote(new Pacote(), 1000);
		roteador.receberPacote(new Pacote(), 1001);

		assertEquals(1, roteador.getNumeroPacotes());
	}

	@Test
	public void testEnviarProximoPacote() {
		roteador.receberPacote(new Pacote(), 1000);
		roteador.enviarProximoPacote(1010);

		assertEquals(0, roteador.getNumeroPacotes());
	}

}
