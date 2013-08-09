package br.ufrj.ad.simulator.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.events.Evento;
import br.ufrj.ad.simulator.models.FilaEventos;

/**
 * Casos de teste para a classe Fila de Eventos, que será usada no simulador
 * para tentar otimizá-lo
 * 
 * @author Felipe Teixeira
 * 
 */
public class TesteFilaDeEventos {
	FilaEventos filaEventos;

	@Before
	public void setUp() {
		filaEventos = new FilaEventos();
	}

	/**
	 * Testa se ao colocarmos 2 eventos fora de ordem ele irá colocá-los
	 * corretamente dentro do vetor
	 */
	@Test
	public void testeForaDeOrdem() {
		Evento e3 = new Evento(10);
		Evento e1 = new Evento(5);
		Evento e2 = new Evento(7);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e3);

		assertEquals(e1, filaEventos.poll());
	}
	
	@Test
	public void testeForaDeOrdem2(){
		Evento e3 = new Evento(10);
		Evento e2 = new Evento(7);
		Evento e1 = new Evento(5);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e3);

		filaEventos.poll();
		
		assertEquals(e2, filaEventos.poll());
	}
	
	@Test
	public void testeForaDeOrdem3(){
		Evento e3 = new Evento(10);
		Evento e2 = new Evento(7);
		Evento e1 = new Evento(5);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e3);

		
		assertEquals(3, filaEventos.size());
	}
	
	@Test
	public void testeForaDeOrdem4(){
		Evento e3 = new Evento(10);
		Evento e2 = new Evento(7);
		Evento e1 = new Evento(5);
		Evento e0 = new Evento(2);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e3);
		filaEventos.add(e0);

		
		assertEquals(e0, filaEventos.poll());
	}
	
	@Test
	public void testeForaDeOrdem5(){
		Evento e4 = new Evento(10);
		Evento e2 = new Evento(7);
		Evento e1 = new Evento(5);
		Evento e0 = new Evento(2);
		Evento e3 = new Evento(8);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e4);
		filaEventos.add(e0);
		filaEventos.add(e3);

		filaEventos.poll();
		filaEventos.poll();
		filaEventos.poll();
		
		assertEquals(e3, filaEventos.poll());
	}
	
	@Test
	public void testeRemove(){
		Evento e4 = new Evento(10);
		Evento e2 = new Evento(7);
		Evento e1 = new Evento(5);
		Evento e0 = new Evento(2);
		Evento e3 = new Evento(8);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e4);
		filaEventos.add(e0);
		filaEventos.add(e3);
		
		filaEventos.remove(e3);

		filaEventos.poll();
		filaEventos.poll();
		filaEventos.poll();
		
		assertEquals(e4, filaEventos.poll());
	}
	
	@Test
	public void testeRemove2(){
		Evento e4 = new Evento(10);
		Evento e2 = new Evento(7);
		Evento e1 = new Evento(5);
		Evento e0 = new Evento(2);
		Evento e3 = new Evento(8);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e4);
		filaEventos.add(e0);
		filaEventos.add(e3);
		
		filaEventos.remove(e3);
		filaEventos.remove(e1);

		filaEventos.poll();
		
		assertEquals(e2, filaEventos.poll());
	}
	
	@Test
	public void testeRemove3(){
		Evento e4 = new Evento(5);
		Evento e2 = new Evento(5);
		Evento e1 = new Evento(5);
		Evento e0 = new Evento(5);
		Evento e3 = new Evento(5);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e4);
		filaEventos.add(e0);
		filaEventos.add(e3);
		
		filaEventos.remove(e1);
		filaEventos.remove(e2);
		filaEventos.remove(e3);
		filaEventos.remove(e4);
		
		assertEquals(e0, filaEventos.poll());
	}
	
	@Test
	public void testeRemove4(){
		Evento e4 = new Evento(5);
		Evento e2 = new Evento(5);
		Evento e1 = new Evento(5);
		Evento e0 = new Evento(5);
		Evento e3 = new Evento(5);

		filaEventos.add(e2);
		filaEventos.add(e1);
		filaEventos.add(e4);
		filaEventos.add(e0);
		filaEventos.add(e3);
		
		filaEventos.remove(e1);
		filaEventos.remove(e2);
		filaEventos.remove(e3);
		filaEventos.remove(e4);
		
		assertEquals(1, filaEventos.size());
	}
}
