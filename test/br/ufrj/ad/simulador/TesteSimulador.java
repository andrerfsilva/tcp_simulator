package br.ufrj.ad.simulador;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.Simulador;

/**
 * Casos de teste para a classe Simulador.
 * 
 * @author André Ramos
 * 
 */
public class TesteSimulador {

	private Simulador simulador;

	@Before
	public void setUp() throws Exception {
		this.simulador = new Simulador();
	}

	@Test
	public void testGetTaxaTrafegoDeFundoEmMbps() {
		this.simulador.setTamanhoMedioRajada(10);
		this.simulador.setTempoMedioEntreRajadas(24);
		assertEquals(5, this.simulador.getTaxaTrafegoDeFundoEmMbps(), 0);
	}

	@Test
	public void testGetTaxaTrafegoDeFundoEmMbps2() {
		this.simulador.setTamanhoMedioRajada(20);
		this.simulador.setTempoMedioEntreRajadas(24);
		assertEquals(10, this.simulador.getTaxaTrafegoDeFundoEmMbps(), 0);
	}

	@Test
	public void testGetTaxaTrafegoDeFundoEmMbps3() {
		this.simulador.setTamanhoMedioRajada(10);
		this.simulador.setTempoMedioEntreRajadas(8);
		assertEquals(15, this.simulador.getTaxaTrafegoDeFundoEmMbps(), 0);
	}

}
