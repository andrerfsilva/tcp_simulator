package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.models.Parametros;

/**
 * Casos de teste para a classe Parametros.
 * 
 * @author André Ramos
 * 
 */
public class TesteParametros {

	private Parametros parametros;

	@Before
	public void setUp() throws Exception {
		this.parametros = new Parametros();
	}

	@Test
	public void testGetTaxaTrafegoDeFundoEmMbps() {
		this.parametros.setProperty("MediaPacotesPorRajada", "10");
		this.parametros.setProperty("TempoMedioEntreRajadas", "24");
		assertEquals(5, this.parametros.getTaxaTrafegoDeFundoEmMbps(), 0);
	}

	@Test
	public void testGetTaxaTrafegoDeFundoEmMbps2() {
		this.parametros.setProperty("MediaPacotesPorRajada", "20");
		this.parametros.setProperty("TempoMedioEntreRajadas", "24");
		assertEquals(10, this.parametros.getTaxaTrafegoDeFundoEmMbps(), 0);
	}

	@Test
	public void testGetTaxaTrafegoDeFundoEmMbps3() {
		this.parametros.setProperty("MediaPacotesPorRajada", "10");
		this.parametros.setProperty("TempoMedioEntreRajadas", "8");
		assertEquals(15, this.parametros.getTaxaTrafegoDeFundoEmMbps(), 0);
	}

	@Test
	public void testTempoTransmissaoPacoteNoRoteador() {
		parametros.setProperty("Cg", "10E6");
		assertEquals(1.2, parametros.tempoTransmissaoPacoteNoRoteador(1500), 0);
	}

	@Test
	public void testTempoTransmissaoPacoteNoRoteador2() {
		parametros.setProperty("Cg", "10E6");
		assertEquals(2.4, parametros.tempoTransmissaoPacoteNoRoteador(3000), 0);
	}

	@Test
	public void testTempoTransmissaoPacoteNoRoteador3() {
		parametros.setProperty("Cg", "1E6");
		assertEquals(12.0, parametros.tempoTransmissaoPacoteNoRoteador(1500), 0);
	}

	@Test
	public void testTempoTransmissaoPacoteNoRoteador4() {
		parametros.setProperty("Cg", "10E9");
		assertEquals(1.2E-3, parametros.tempoTransmissaoPacoteNoRoteador(1500),
				0);
	}

}
