package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import java.io.FileOutputStream;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebePacoteTxTCP;
import br.ufrj.ad.simulator.eventos.EventoRoteadorTerminaEnvio;
import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.Parametros;
import br.ufrj.ad.simulator.models.Simulador;

/**
 * Casos de teste para classe Simulador.
 * 
 * @author Wellignton Mascena, Felipe Teixeira
 * 
 */
public class TesteSimulador {

	private Simulador simulador;
	private Parametros parametros;

	@Before
	public void setUp() throws Exception {
		parametros = new Parametros();

		parametros.setProperty("EstacoesGrupo1", "1");
		parametros.setProperty("EstacoesGrupo2", "0");
		parametros.setProperty("Cs", "1E9");
		parametros.setProperty("Cg", "10E6");
		parametros.setProperty("TP1", "100");
		parametros.setProperty("TP2", "50");
		parametros.setProperty("TPACK1", "100");
		parametros.setProperty("TPACK2", "50");
		parametros.setProperty("DisciplinaRoteador", "FIFO");
		parametros.setProperty("TamanhoBufferRoteador", "40");
		parametros.setProperty("TempoMedioEntreRajadas", "24");
		parametros.setProperty("MediaPacotePorRajada", "10");
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP1() {

		simulador = new Simulador(parametros);

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().peek().getClass());
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP2() {

		simulador = new Simulador(parametros);

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(1.2 + 100, simulador.getFilaEventos().peek()
				.getTempoDeOcorrencia(), 0);
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP3() {

		simulador = new Simulador(parametros);

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(EventoRoteadorRecebePacoteTxTCP.class, simulador
				.getFilaEventos().peek().getClass());
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP4() {

		simulador = new Simulador(parametros);

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(100 + 100 + 0.012, simulador.getFilaEventos().peek()
				.getTempoDeOcorrencia(), 0);
	}
}
