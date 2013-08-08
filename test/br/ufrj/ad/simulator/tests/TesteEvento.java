package br.ufrj.ad.simulator.tests;

import static org.junit.Assert.*;

import java.util.PriorityQueue;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.events.Evento;

/**
 * Casos de teste para a classe Evento.
 * 
 * @author André Ramos, Wellington Mascena
 * 
 */
public class TesteEvento {

	PriorityQueue<Evento> filaEventos;

	@Before
	public void setUp() throws Exception {
		filaEventos = new PriorityQueue<Evento>();
	}

	@Test
	public void testeForaDeOrdem() {
		Evento e2 = new Evento(2);
		Evento e1 = new Evento(1);

		filaEventos.add(e2);
		filaEventos.add(e1);

		assertEquals(e1, filaEventos.poll());
	}

	@Test
	public void testeNaOrdem() {
		Evento e1 = new Evento(1);
		Evento e2 = new Evento(2);

		filaEventos.add(e1);
		filaEventos.add(e2);

		assertEquals(e1, filaEventos.poll());
	}
	
	@Test
	public void testeMenor() {
		Evento e1 = new Evento(123);
		Evento e2 = new Evento(81726);

		assertTrue(e1.compareTo(e2) < 0);
	}
	
	@Test
	public void testeMaior() {
		Evento e1 = new Evento(176);
		Evento e2 = new Evento(134516);

		assertTrue(e2.compareTo(e1) > 0);
	}
	
	@Test
	public void testeIgual() {
		Evento e1 = new Evento(187);
		Evento e2 = new Evento(187);

		assertTrue(e2.compareTo(e1) == 0);
	}

}
