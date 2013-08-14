package br.ufrj.ad.simulator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.RoteadorWFQ;

public class TesteRoteadorWFQ {

	private RoteadorWFQ roteador;

	@Before
	public void runBeforeEveryTest() {
		roteador = new RoteadorWFQ();
		roteador.setTamanhoBuffer(40);
	}

	@Test
	public void TesteReceberMaisQueAFilaDeEntrada() {
		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			roteador.receberPacote(new Pacote());
		}

		assertFalse(roteador.receberPacote(new Pacote()));
	}

	@Test
	public void TesteReceberDentroDaFilaDeEntrada() {
		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			roteador.receberPacote(new Pacote());
		}

		Pacote p = new Pacote();

		p.setDestino(0);

		assertTrue(roteador.receberPacote(p));
	}

	@Test
	public void TesteSobrecarregarFilaDeSaida() {
		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			roteador.receberPacote(new Pacote());
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(0);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(1);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(2);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(3);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(4);

			roteador.receberPacote(p);
		}

		Pacote p = new Pacote();

		p.setDestino(5);

		assertFalse(roteador.receberPacote(p));
	}

	@Test
	public void TesteFilasDeEntrada() {
		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			roteador.receberPacote(new Pacote());
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(0);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(1);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(2);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(3);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			Pacote p = new Pacote();

			p.setDestino(4);

			roteador.receberPacote(p);
		}

		for (int i = 0; i < roteador.getFilasDeEntrada().size(); i++) {
			int destinoPacote = roteador.getFilasDeEntrada().get(i).get(0)
					.getDestino();

			for (int j = 0; j < roteador.getFilasDeEntrada().get(i).size(); j++) {
				assertEquals(destinoPacote, roteador.getFilasDeEntrada().get(i)
						.get(j).getDestino());
			}
		}
	}
	
	@Test
	public void TesteReceberPacote() {
		for (int i = 0; i < roteador.getTamanhoFilasDeEntrada(); i++) {
			roteador.receberPacote(new Pacote());
		}

		roteador.enviarProximoPacote();
		
		assertEquals(0, roteador.getFilasDeEntrada().size());
	}
}
