package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.eventos.Evento;
import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebePacoteTxTCP;
import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebeTrafegoDeFundo;
import br.ufrj.ad.simulator.eventos.EventoRoteadorTerminaEnvio;
import br.ufrj.ad.simulator.eventos.EventoTimeOut;
import br.ufrj.ad.simulator.eventos.EventoTxTCPRecebeSACK;
import br.ufrj.ad.simulator.eventos.EventoTxTCPTerminaTransmissao;
import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.Parametros;
import br.ufrj.ad.simulator.models.SACK;
import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.models.TxTCP;

/**
 * Casos de teste para classe Simulador.
 * 
 * @author André Ramos, Wellignton Mascena, Felipe Teixeira
 * 
 */
public class TesteSimulador {

	private Simulador simulador;
	private Parametros parametros;
	private TxTCP tx;

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

		simulador = new Simulador(parametros);

		tx = simulador.getTransmissores()[0];
	}

	/* ---------------Testes do TxTCPTerminaTransmissao--------------- */

	/**
	 * Quando termina a transmissão, se a cwnd permitir, então podemos cadastrar
	 * mais uma trasmissao. Nesse caso os eventos criados serão o próximo
	 * TxTCPTerminaTransmissao, RoteadorRecebePacoteTxTCP e TimeOut.
	 * 
	 * Neste teste conferimos se o segundo evento é RoteadorRecebePacoteTxTCP.
	 */
	@Test
	public void testTxTCPTerminaTransmissao0() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // TxTCPTerminaTransmissao

		assertEquals(EventoRoteadorRecebePacoteTxTCP.class, simulador
				.getFilaEventos().peek().getClass()); // RoteadoRecebePacoteTxTCP
	}

	/**
	 * Para a mesma sistuação anterior, confere o tempo de ocorrência do evento
	 * RoteadorRecebePacoteTxTCP.
	 */
	@Test
	public void testTxTCPTerminaTransmissao1() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // TxTCPTerminaTransmissao

		assertEquals(100 + 100 + 0.012, simulador.getFilaEventos().peek()
				.getTempoDeOcorrencia(), 0); // RoteadoRecebePacoteTxTCP
	}

	/**
	 * Para a mesma situação anterior, confere o número de eventos criados.
	 */
	@Test
	public void testTxTCPTerminaTransmissao2() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(3, simulador.getFilaEventos().size());
	}

	/**
	 * Para a mesma situação anterior, confere se o terceiro evento é um
	 * EventoTimeOut.
	 */
	@Test
	public void testTxTCPTerminaTransmissao3() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // TxTCPTerminaTransmissao
		simulador.getFilaEventos().poll(); // RoteadorRecebePacoteTxTCP

		assertEquals(EventoTimeOut.class, simulador.getFilaEventos().poll()
				.getClass());
	}

	/**
	 * Para a mesma situação anterior, confere se o tempo do EventoTimeOut está
	 * correto.
	 */
	@Test
	public void testTxTCPTerminaTransmissao4() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // TxTCPTerminaTransmissao
		simulador.getFilaEventos().poll(); // RoteadorRecebePacoteTxTCP

		assertEquals(100 + tx.getRTO(), simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);
	}

	/**
	 * Para a mesma situação anterior, verifica se o primeiro evento é um
	 * EventoTxTCPTerminaTransmissao.
	 */
	@Test
	public void testTxTCPTerminaTransmissao5() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(EventoTxTCPTerminaTransmissao.class, simulador
				.getFilaEventos().poll().getClass());
	}

	/**
	 * Para a mesma situação anterior, confere se o tempo de ocorrência do
	 * EventoTxTCPTerminaTransmissao está correto.
	 */
	@Test
	public void testTxTCPTerminaTransmissao6() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(100.012, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);
	}

	/**
	 * Verifica se o ao iniciar uma nova transmissão de pacote TxTCP o evento de
	 * time-out corresponte está sendo criado corretamente.
	 */
	@Test
	public void testTxTCPTerminaTransmissao7() {

		tx.setTransmitindo(true);
		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // TxTCPTerminaTransmissao
		Pacote p = ((EventoRoteadorRecebePacoteTxTCP) simulador
				.getFilaEventos().poll()).getPacote(); // RoteadorRecebePacoteTxTCP

		assertEquals(p.getEventoTimeOut(), simulador.getFilaEventos().poll());
	}

	/**
	 * Se ao terminar a transmissão a cwnd estiver cheia, então nenhum evento
	 * deve ser criado quando o evento TxTCPTerminaTransmissao for tratado.
	 */
	@Test
	public void testTxTCPTerminaTransmissao8() {

		simulador.getFilaEventos().add(
				new EventoTxTCPTerminaTransmissao(100.012, tx
						.getNumeroConexao()));

		tx.enviarPacote();

		simulador.tratarProximoEvento();

		assertEquals(0, simulador.getFilaEventos().size());

	}

	/**
	 * Se a cwnd estiver cheia no momento que termina a transmissão, então não
	 * será criado outro evento de transmissão, logo o TxTCP entrará no estado
	 * de não-transmitindo.
	 */
	@Test
	public void testTxTCPTerminaTransmissao9() {

		simulador.getTransmissores()[0].enviarPacote();
		tx.setTransmitindo(true);

		simulador.getFilaEventos()
				.add(new EventoTxTCPTerminaTransmissao(0.012, tx
						.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertFalse(tx.isTransmitindo());

	}

	/* --------------Testes do RoteadorRecebePacoteTxTCP-------------- */

	/**
	 * Se um pacote chegar ao roteador e esse se encontra com o buffer vazio,
	 * então podemos agendar o término de envio do pacote.
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP1() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().peek().getClass());
	}

	/**
	 * Para a mesma condição do teste anterior, confere se o término da
	 * transmissão no enlace de saída do roteador ocorreu no tempo certo.
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP2() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(1.2 + 100, simulador.getFilaEventos().peek()
				.getTempoDeOcorrencia(), 0);
	}

	/**
	 * Para as mesmas condições do teste anterior, ao tratar o evento
	 * RoteadorRecebePacoteTxtTCP, testa se somente um evento foi criado.
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP3() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorTerminaEnvio

		assertEquals(null, simulador.getFilaEventos().peek());
	}

	/**
	 * Se já existir pacotes no buffer do roteador no momento da chegada do novo
	 * pacote, então não podemos criar evento de envio na saída do roteador.
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP4() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(0, simulador.getFilaEventos().size());

	}

	@Test
	public void testRoteadorRecebePacoteTxTCP5() {

		Pacote p0 = simulador.getTransmissores()[0].enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100.012, p0));

		simulador.tratarProximoEvento(); // RecebePacoteTxTCP
		simulador.tratarProximoEvento(); // RoteadorTerminaEnvio
		simulador.tratarProximoEvento(); // TxTCPRecebeSACK

		assertTrue(tx.isTransmitindo());

	}

	/* --------------Testes do EventoRoteadorTerminaEnvio-------------- */

	/* --------------Testes do EventoRoteadorTerminaEnvio-------------- */

	/**
	 * Tratar fim de envio no roteador quando não há pacotes. Indica que criamos
	 * eventos de envio desnecessários.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testRoteadorTerminaEnvio1() {

		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

	}

	/**
	 * Garante que não irá criar um evento de terminar envio quando não houver
	 * mais pacotes no buffer para serem enviados.
	 * 
	 * OBS: Nesse caso, pacotes de tráfego de fundo não criam o evento de SACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio2() {

		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

		assertEquals(null, simulador.getFilaEventos().poll());

	}

	/**
	 * Testa tratamento de evento de envio no roteador quando há dois pacotes de
	 * tráfego de fundo no buffer.
	 * 
	 * OBS: Nesse caso, pacotes de tráfego de fundo não criam o evento de SACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio3() {

		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().poll().getClass());

	}

	/**
	 * Confere o tempo de ocorrência do envio do segundo pacote de tráfego de
	 * fundo no buffer do roteador.
	 * 
	 * OBS: Nesse caso, pacotes de tráfego de fundo não criam o evento de SACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio4() {

		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

		assertEquals(1.2 + 1.2, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Agora testando com pacotes TCP no buffer do roteador.
	 */
	@Test
	public void testRoteadorTerminaEnvio5() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(EventoTxTCPRecebeSACK.class, simulador.getFilaEventos()
				.poll().getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se o tempo do TxTCPRecebeSACK está
	 * correto.
	 */
	@Test
	public void testRoteadorTerminaEnvio6() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(100 + 100, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Para o mesmo caso anterior, testa se apenas um evento é criado.
	 */
	@Test
	public void testRoteadorTerminaEnvio7() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(1, simulador.getFilaEventos().size());

	}

	/**
	 * Agora o roteador tem um pacote TCP e um pacote de tráfego de fundo.
	 * Quando o evento de terminar envio ocorrer, o pacote TCP chegará no Rx.
	 * 
	 * Nesse caso, os eventos criados serão o próximo RoteadorTerminaEnvio (do
	 * pacote de TF) e o TxTCPRecebeSACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio8() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().poll().getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se o RoteadorTerminaEnvio ocorre no
	 * tempo correto.
	 */
	@Test
	public void testRoteadorTerminaEnvio9() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(100 + 1.2, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Para o mesmo caso anterior, verifica se o segundo evento é um
	 * TxTCPRecebeSACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio10() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorTerminaEnvio

		assertEquals(EventoTxTCPRecebeSACK.class, simulador.getFilaEventos()
				.poll().getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se o segundo evento ocorre no tempo
	 * correto.
	 */
	@Test
	public void testRoteadorTerminaEnvio11() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorTerminaEnvio

		assertEquals(100 + 100, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Para o mesmo caso anterior, confere se são criados apenas dois eventos.
	 */
	@Test
	public void testRoteadorTerminaEnvio12() {

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(2, simulador.getFilaEventos().size());

	}

	/* -----------Testes do EventoRoteadorRecebeTafegoFundo----------- */

	/**
	 * Quando um tráfego de fundo chega no roteador vazio, então são criados 2
	 * eventos: RoteadorRecebeTrafegoDeFundo e RoteadorTerminaEnvio. Como a
	 * próxima chegada do tráfego de fundo é uma v.a., não sabemos qual dos dois
	 * eventos acontecerá primeiro.
	 */
	@Test
	public void testEventoRoteadorRecebeTafegoFundo1() {

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebeTrafegoDeFundo(50));

		simulador.tratarProximoEvento();

		Evento e = simulador.getFilaEventos().poll();

		assertTrue((e instanceof EventoRoteadorRecebeTrafegoDeFundo)
				|| (e instanceof EventoRoteadorTerminaEnvio));

	}

	/**
	 * Para o mesmo caso anterior, captura o evento RoteadorTerminaEnvio e
	 * confere se ele ocorre no tempo esperado.
	 */
	@Test
	public void testEventoRoteadorRecebeTafegoFundo2() {

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebeTrafegoDeFundo(50));

		simulador.tratarProximoEvento();

		Evento e = simulador.getFilaEventos().poll();

		if (e instanceof EventoRoteadorRecebeTrafegoDeFundo) {
			assertEquals(50 + 1.2, simulador.getFilaEventos().poll()
					.getTempoDeOcorrencia(), 0);
		} else if (e instanceof EventoRoteadorTerminaEnvio) {
			assertEquals(50 + 1.2, e.getTempoDeOcorrencia(), 0);
		} else {
			fail("Evento inválido na fila: " + e.getClass());
		}
	}

	/**
	 * Para o mesmo caso anterior, confere se foram criados apenas dois eventos.
	 */
	@Test
	public void testEventoRoteadorRecebeTafegoFundo3() {

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebeTrafegoDeFundo(50));

		simulador.tratarProximoEvento();

		assertEquals(2, simulador.getFilaEventos().size());

	}

	/**
	 * Se o tráfego de fundo chegar quando o roteador contém pacotes, então o
	 * único evento agendado será a próxima chegada de tráfego de fundo.
	 */
	@Test
	public void testEventoRoteadorRecebeTafegoFundo4() {

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebeTrafegoDeFundo(50));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorRecebeTrafegoDeFundo.class, simulador
				.getFilaEventos().poll().getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se foi criado apenas um evento.
	 */
	@Test
	public void testEventoRoteadorRecebeTafegoFundo5() {

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebeTrafegoDeFundo(50));

		simulador.tratarProximoEvento();

		assertEquals(1, simulador.getFilaEventos().size());

	}

	/* -----------------Testes do EventoTimeOut----------------- */

	/**
	 * Se o TimeOut ocorrer quando TxTCP estiver transmitindo, então nenhum
	 * evento será criado.
	 */
	@Test
	public void testEventoTimeOut1() {

		tx.enviarPacote();
		tx.setTransmitindo(true);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(0, simulador.getFilaEventos().size());

	}

	/**
	 * Se ocorrer um TimeOut quando o TxTCP não estiver transmitindo, então ele
	 * começa a transmitir imediatamente. Podemos agendar os eventos
	 * TxTCPTerminaTransmissao, RoteadorRecebePacote e o TimeOut do novo pacote
	 * enviado.
	 */
	@Test
	public void testEventoTimeOut2() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(3, simulador.getFilaEventos().size());

	}

	/**
	 * Para o mesmo caso anterior, confere se o primeiro evento é um
	 * TxTCPTerminaTransmissao.
	 */
	@Test
	public void testEventoTimeOut3() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(EventoTxTCPTerminaTransmissao.class, simulador
				.getFilaEventos().poll().getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se o primeiro evento ocorre no tempo
	 * correto.
	 */
	@Test
	public void testEventoTimeOut4() {

		TxTCP tx = simulador.getTransmissores()[0];
		tx.enviarPacote();
		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertEquals(400 + 0.012, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Para o mesmo caso anterior, confere se o segundo evento é um
	 * RoteadorRecebePacoteTxTCP.
	 */
	@Test
	public void testEventoTimeOut5() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();
		simulador.getFilaEventos().poll();

		assertEquals(EventoRoteadorRecebePacoteTxTCP.class, simulador
				.getFilaEventos().poll().getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se o tempo de ocorrência do segundo
	 * evento está correto.
	 */
	@Test
	public void testEventoTimeOut6() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();
		simulador.getFilaEventos().poll();

		assertEquals(400 + 100 + 0.012, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Para o mesmo caso anterior, confere se o terceiro evento é um TimeOut.
	 */
	@Test
	public void testEventoTimeOut7() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();
		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(EventoTimeOut.class, simulador.getFilaEventos().poll()
				.getClass());

	}

	/**
	 * Para o mesmo caso anterior, confere se o tempo de ocorrência do terceiro
	 * evento está correto.
	 */
	@Test
	public void testEventoTimeOut8() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();
		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(400 + tx.getRTO(), simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Independende do estado anterior do TxTCP, depois do TimeOut ele deve
	 * estar no estado de transmitindo.
	 */
	@Test
	public void testEventoTimeOut9() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertTrue(tx.isTransmitindo());

	}

	/**
	 * Independende do estado anterior do TxTCP, depois do TimeOut ele deve
	 * estar no estado de transmitindo.
	 */
	@Test
	public void testEventoTimeOut10() {

		tx.enviarPacote();
		tx.setTransmitindo(true);

		simulador.getFilaEventos().add(
				new EventoTimeOut(400, tx.getNumeroConexao()));

		simulador.tratarProximoEvento();

		assertTrue(tx.isTransmitindo());

	}

	/* -----------------Testes do EventoTxTCPRecebeSACK----------------- */

	/**
	 * Se chegar um SACK quando TxTCP estiver transmitindo, então nenhum evento
	 * será criado.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK1() {

		tx.setTransmitindo(true);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		assertEquals(0, simulador.getFilaEventos().size());
	}

	/**
	 * Se o TxTCP receber um SACK quando não está transmitindo, então conferimos
	 * se os próximos pacotes enviados são o esperado.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK2() {

		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		Pacote pEnviado = tx.enviarPacote(350);
		Pacote pEsperado = new Pacote();
		pEsperado.setByteInicialEFinal(3000, 4499);
		pEsperado.setDestino(0);

		assertEquals(pEsperado, pEnviado);
	}

	/**
	 * Se depois do TxTCP receber um SACK enquanto não estiver transmitindo e
	 * estiver pronto para transmitir (sua cwnd não estiver cheia), então o
	 * TxTCP começará a transmitir imediatamente e serão agendados 3 eventos:
	 * TxTCPTerminaTransmissao, RoteadorRecebePacoteTxTCP e TimeOut.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK3() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		assertEquals(3, simulador.getFilaEventos().size());
	}

	/**
	 * Para o mesmo caso anterior, confere se o primeiro evento é um
	 * TxTCPTerminaTransmissao.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK4() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		assertEquals(EventoTxTCPTerminaTransmissao.class, simulador
				.getFilaEventos().poll().getClass());
	}

	/**
	 * Para o mesmo caso anterior, verifica se o tempo de ocorrência do primeiro
	 * evento está correto.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK5() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		assertEquals(350.012, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);
	}

	/**
	 * Para o mesmo caso anterior, confere se o segundo evento é um
	 * RoteadorRecebePacoteTxTCP
	 */
	@Test
	public void testEventoTxTCPRecebeSACK6() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(EventoRoteadorRecebePacoteTxTCP.class, simulador
				.getFilaEventos().poll().getClass());
	}

	/**
	 * Para o mesmo caso anterior, confere se o tempo do segundo evento está
	 * correto.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK7() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(350 + 100 + 0.012, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);
	}

	/**
	 * Para o mesmo caso anterior, verifica se o terceiro evento é um TimeOut.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK8() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(EventoTimeOut.class, simulador.getFilaEventos().poll()
				.getClass());
	}

	/**
	 * Para o mesmo caso anterior, verifica se o tempo de ocorrência do terceiro
	 * evento está correto.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK9() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(350 + tx.getRTO(), simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);
	}

	/**
	 * Para o mesmo caso anterior, confere se o pacote do evento
	 * RoteadorRecebePacoteTxTCP está correto.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK10() {

		tx.enviarPacote();
		tx.setTransmitindo(false);

		SACK sack = new SACK(0, 1500);

		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // TxTCPTerminaTransmissao

		Pacote pEsperado = new Pacote();

		pEsperado.setByteInicialEFinal(1500, 2999);
		pEsperado.setDestino(0);

		assertEquals(pEsperado, ((EventoRoteadorRecebePacoteTxTCP) simulador
				.getFilaEventos().poll()).getPacote());
	}

	/**
	 * Verifica se o próximo pacote a enviar é atualizado corretamente em função
	 * de um SACK não duplicado.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK11() {

		SACK sack = new SACK(tx.getNumeroConexao(), 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		tx.setTransmitindo(true);

		simulador.tratarProximoEvento();

		Pacote pEnviado = tx.enviarPacote(350);
		Pacote pEsperado = new Pacote();

		pEsperado.setByteInicialEFinal(1500, 2999);
		pEsperado.setDestino(0);

		assertEquals(pEsperado, pEnviado);
	}

	/**
	 * Em Slow Start, independente do estado anterior do TxTCP, ele estará
	 * transmitindo depois de receber um SACK.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK12() {

		tx.setTransmitindo(true);

		SACK sack = new SACK(tx.getNumeroConexao(), 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		assertTrue(tx.isTransmitindo());
	}

	/**
	 * Em Slow Start, independente do estado anterior do TxTCP, ele estará
	 * transmitindo depois de receber um SACK.
	 */
	@Test
	public void testEventoTxTCPRecebeSACK13() {

		tx.setTransmitindo(false);

		SACK sack = new SACK(tx.getNumeroConexao(), 1500);
		simulador.getFilaEventos().add(new EventoTxTCPRecebeSACK(350, sack));

		simulador.tratarProximoEvento();

		assertTrue(tx.isTransmitindo());
	}

	/* ----------------------Testes Legados-------------------- */

	/**
	 * Mostrar que o comportamento esperado está correto ao receber um SACK no
	 * meio da transmissão. Atualmente esse problema foi resolvido com o estado
	 * "transmitindo" do TxTCP, mas esse teste foi importante para revelar o
	 * erro.
	 */
	@Test
	public void testEventoTxTCPRecebeSACKDuranteTransmissao1() {

		Evento txTCPTerminaTransmissao = new EventoTxTCPTerminaTransmissao(
				100.012, tx.getNumeroConexao());

		tx.setTransmitindo(true);

		simulador.getFilaEventos().add(txTCPTerminaTransmissao);

		SACK sack = new SACK(tx.getNumeroConexao(), 1500);
		Evento txRecebeSACK = new EventoTxTCPRecebeSACK(100.006, sack);

		simulador.getFilaEventos().add(txRecebeSACK);

		simulador.tratarProximoEvento();

		assertEquals(1, simulador.getFilaEventos().size());
	}

	/**
	 * Mostrar que o comportamento esperado está correto ao receber um SACK no
	 * meio da transmissão. Atualmente esse problema foi resolvido com o estado
	 * "transmitindo" do TxTCP, mas esse teste foi importante para revelar o
	 * erro.
	 */
	@Test
	public void testEventoTxTCPRecebeSACKDuranteTransmissao2() {

		Evento txTCPTerminaTransmissao = new EventoTxTCPTerminaTransmissao(
				100.012, tx.getNumeroConexao());

		tx.setTransmitindo(true);

		simulador.getFilaEventos().add(txTCPTerminaTransmissao);

		SACK sack = new SACK(tx.getNumeroConexao(), 1500);
		Evento txRecebeSACK = new EventoTxTCPRecebeSACK(100.006, sack);

		simulador.getFilaEventos().add(txRecebeSACK);

		simulador.tratarProximoEvento();

		assertEquals(txTCPTerminaTransmissao, simulador.getFilaEventos().poll());
	}

	/* ----------------------Testes Especiais-------------------- */

	/**
	 * Verificar um ACK cancela os timeouts dos pacotes de número de sequência
	 * inferior ao ACK. Por exemplo, um ACK 6000 deve cancelar os eventos de
	 * timeout dos pacotes <0, 1499>, <1500, 2999>, <3000, 4499> e <4500, 5999>.
	 */
	@Test
	public void testCancelamentoTimeOut1() {

		EventoTxTCPTerminaTransmissao etx = new EventoTxTCPTerminaTransmissao(
				50, tx.getNumeroConexao());

		SACK sack4 = new SACK(tx.getNumeroConexao(), 4 * Parametros.mss);
		EventoTxTCPRecebeSACK esack = new EventoTxTCPRecebeSACK(50.006, sack4);

		simulador.getFilaEventos().add(esack);
		simulador.getFilaEventos().add(etx);

		simulador.tratarProximoEvento(); // TxTCPTerminaEnvio
		simulador.tratarProximoEvento(); // TxTCPRecebeSACK

		assertEquals(2, simulador.getFilaEventos().size());

	}

	@Test
	public void testCancelamentoTimeOut2() {

		EventoTxTCPTerminaTransmissao etx = new EventoTxTCPTerminaTransmissao(
				50, tx.getNumeroConexao());

		SACK sack4 = new SACK(tx.getNumeroConexao(), 4 * Parametros.mss);
		EventoTxTCPRecebeSACK esack = new EventoTxTCPRecebeSACK(50.006, sack4);

		simulador.getFilaEventos().add(esack);
		simulador.getFilaEventos().add(etx);

		simulador.tratarProximoEvento(); // TxTCPTerminaEnvio
		simulador.tratarProximoEvento(); // TxTCPRecebeSACK

		assertFalse(simulador.getFilaEventos().peek().getClass()
				.equals(EventoTimeOut.class));

	}

	@Test
	public void testCancelamentoTimeOut3() {

		EventoTxTCPTerminaTransmissao etx = new EventoTxTCPTerminaTransmissao(
				50, tx.getNumeroConexao());

		SACK sack4 = new SACK(tx.getNumeroConexao(), 4 * Parametros.mss);
		EventoTxTCPRecebeSACK esack = new EventoTxTCPRecebeSACK(50.006, sack4);

		simulador.getFilaEventos().add(esack);
		simulador.getFilaEventos().add(etx);

		simulador.tratarProximoEvento(); // TxTCPTerminaEnvio
		simulador.tratarProximoEvento(); // TxTCPRecebeSACK

		simulador.getFilaEventos().poll();

		assertFalse(simulador.getFilaEventos().peek().getClass()
				.equals(EventoTimeOut.class));

	}
}
