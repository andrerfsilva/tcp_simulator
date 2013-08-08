package br.ufrj.ad.simulator.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.exceptions.TxTCPNotReadyToSendException;
import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.Parametros;
import br.ufrj.ad.simulator.models.SACK;
import br.ufrj.ad.simulator.models.TxTCP;
import br.ufrj.ad.simulator.statistics.Random;

/**
 * Casos de teste para a classe TxTCP.
 * 
 * @author André Ramos
 * 
 */
public class TesteTxTCP {

	private TxTCP tx;

	@Before
	public void setUp() throws Exception {
		tx = new TxTCP(new Random().nextInt(30));
	}
	
	@Test
	public void testRetransmissao() {
		
		tx = new TxTCP(23);
		
		tx.enviarPacote();
		
		tx.receberSACK(new SACK(23, 1500, null));
		
		Pacote p1 = new Pacote();
		p1.setByteInicialEFinal(1500, 2999);
		p1.setDestino(23);
		
		assertEquals(p1, tx.enviarPacote());
	}

	@Test
	public void testDestinoPacote() {
		tx = new TxTCP(39);
		assertEquals(39, tx.enviarPacote().getDestino());
	}

	@Test
	public void testEstadoInicialCwnd() {
		assertEquals(Parametros.mss, tx.getCwnd());
	}

	@Test
	public void testEstadoInicialThreshold() {
		assertEquals(65535, tx.getThreshold());
	}

	@Test
	public void testReceberSACK1() {
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));
		assertEquals(2 * Parametros.mss, tx.getCwnd());
	}

	@Test
	public void testReceberSACK2() {
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));
		assertEquals(Parametros.mss, tx.getProximoPacoteAEnviar());
	}

	@Test
	public void testReceberSACK3() {
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));
		assertEquals(Parametros.mss, tx.getPacoteMaisAntigoSemACK());
	}

	@Test
	public void testCwnd1() {
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));
		assertEquals(2 * Parametros.mss, tx.getCwnd());
	}

	@Test
	public void testCwnd2() {
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		assertEquals(3 * Parametros.mss, tx.getCwnd());
	}

	/**
	 * Testa quantos RTTs o TxTCP demora para entrar em Congestion Avoidance.
	 * Assumindo que não haverá perda no caminho, em i RTTs teremos <br/>
	 * <br/>
	 * cwnd = (2^i)*MSS <br/>
	 * <br/>
	 * 
	 * Durante essa fase estaremos em Slow Start. Para cwnd < threshold, teremos <br/>
	 * <br/>
	 * (2^i)*MSS < threshold <br/>
	 * i + log(MSS) < log(threshold) <br/>
	 * i < log(threshold) - log(MSS) <br/>
	 * <br/>
	 * 
	 * Ou seja, em 5 RTTs ainda estaremos em Slow Start, e do 6 em diante será
	 * Congestion Avoidance.
	 */
	@Test
	public void testSlowStart() {

		int rtts = 0;
		while (tx.getCwnd() < tx.getThreshold()) {
			// envia o máximo de pacotes que a cwnd permite
			long max = tx.getCwnd() / Parametros.mss;
			for (int i = 0; i < max; i++) {
				tx.enviarPacote();
				tx.receberSACK(new SACK(0, tx.getProximoPacoteAEnviar()));
			}
			rtts++;
		}
		assertEquals(6, rtts);
	}

	/**
	 * Em 5 RTTs a cwnd ainda estará crescendo exponencialmente.
	 */
	@Test
	public void testSlowStart2() {
		for (int rtts = 0; rtts < 5; rtts++) {
			// envia o máximo de pacotes que a cwnd permite
			long max = tx.getCwnd() / Parametros.mss;
			for (int i = 0; i < max; i++) {
				tx.enviarPacote();
				tx.receberSACK(new SACK(0, tx.getProximoPacoteAEnviar()));
			}
		}
		assertEquals(Math.pow(2, 5) * Parametros.mss, tx.getCwnd(), 0);
	}

	/**
	 * Em 3 RTTs a cwnd ainda estará crescendo exponencialmente.
	 */
	@Test
	public void testSlowStart3() {
		for (int rtts = 0; rtts < 3; rtts++) {
			// envia o máximo de pacotes que a cwnd permite
			long max = tx.getCwnd() / Parametros.mss;
			for (int i = 0; i < max; i++) {
				tx.enviarPacote();
				tx.receberSACK(new SACK(0, tx.getProximoPacoteAEnviar()));
			}
		}
		assertEquals(Math.pow(2, 3) * Parametros.mss, tx.getCwnd(), 0);
	}

	/**
	 * Testa se a janela está crescendo como esperado mesmo em caso de SACKs
	 * duplicados.
	 */
	@Test
	public void testSlowStart4() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (chegará fora de ordem no Rx)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 6 * Parametros.mss)); // P3 chegou fora de
															// ordem

		tx.receberSACK(new SACK(0, 7 * Parametros.mss));

		assertEquals(8 * Parametros.mss, tx.getCwnd());

	}

	@Test
	public void testSlowStart5() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (chegará fora de ordem no Rx)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		assertEquals(6 * Parametros.mss, tx.getCwnd());

	}

	@Test
	public void testSlowStart6() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (chegará fora de ordem no Rx)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		assertEquals(3 * Parametros.mss, tx.getPacoteMaisAntigoSemACK());

	}

	/**
	 * Causa um time-out e testa reação do TxTCP.
	 */
	@Test
	public void testTimeOut() {

		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote();
		tx.enviarPacote();
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		Pacote pEsperado = tx.enviarPacote(); // Esse deve ser o próximo pacote
												// enviado depois do time-out
		tx.enviarPacote();
		tx.enviarPacote();
		tx.enviarPacote();

		tx.reagirTimeOut();

		assertTrue((tx.getThreshold() == 2 * Parametros.mss)
				&& (tx.getCwnd() == Parametros.mss)
				&& (pEsperado.equals(tx.enviarPacote())));
	}

	@Test
	public void testTimeOut2() {

		tx.enviarPacote();

		/*
		 * Induz reação ao time-out para recriar bug do ponteiro inicial do
		 * próximo pacote a enviar.
		 */
		tx.reagirTimeOut();

		Pacote pEsperado = new Pacote();
		pEsperado.setByteInicialEFinal(0, 1499);
		pEsperado.setDestino(tx.getNumeroConexao());

		assertEquals(pEsperado, tx.enviarPacote());
	}

	/**
	 * Descarta um pacote e testa o comportamento do TxTCP no Fast Retransmit.
	 */
	@Test
	public void testFastRetransmit() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (será descartado)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						7 * Parametros.mss } }));

		assertTrue(tx.isFastRetransmit());

	}

	/**
	 * Força o TxTCP a entrar em Fast Retransmit, e depois recupera a perda.
	 * Testa se as variáveis de estado são atualizadas corretamente depois da
	 * recuperação.
	 */
	@Test
	public void testFastRetransmit2() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (será descartado)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						7 * Parametros.mss } }));

		/*
		 * Ao receber 3 SACKs repetidos entraremos em Fast Retransmit. Portanto,
		 * se enviarmos o SACK faltando, o TxTCP deve sair de Fast Retransmit e
		 * entrar em Congestion Avoidance.
		 */

		tx.receberSACK(new SACK(0, 7 * Parametros.mss));

		assertTrue((!tx.isFastRetransmit())
				&& (tx.getCwnd() == tx.getThreshold()));

	}

	@Test
	public void testFastRetransmit3() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (será descartado)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						7 * Parametros.mss } }));

		assertEquals(3 * Parametros.mss, tx.getProximoPacoteAEnviar());

	}

	@Test
	public void testFastRetransmit4() {

		tx.enviarPacote(); // P0
		tx.receberSACK(new SACK(0, Parametros.mss));

		tx.enviarPacote(); // P1
		tx.enviarPacote(); // P2
		tx.receberSACK(new SACK(0, 2 * Parametros.mss));
		tx.receberSACK(new SACK(0, 3 * Parametros.mss));

		tx.enviarPacote(); // P3 (será descartado)
		tx.enviarPacote(); // P4
		tx.enviarPacote(); // P5
		tx.enviarPacote(); // P6

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						5 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						6 * Parametros.mss } }));

		tx.receberSACK(new SACK(0, 3 * Parametros.mss,
				new long[][] { new long[] { 4 * Parametros.mss,
						7 * Parametros.mss } }));

		tx.enviarPacote();
		assertEquals(7 * Parametros.mss, tx.getProximoPacoteAEnviar());

	}

	/**
	 * O primeiro pacote a ser enviado é o <0, 1499>.
	 */
	@Test
	public void testEnviarPacote() {
		Pacote pEsperado = new Pacote();
		pEsperado.setByteInicialEFinal(0, Parametros.mss - 1);
		pEsperado.setDestino(0);

		Pacote pEnviado = tx.enviarPacote();
		pEnviado.setDestino(0);

		assertEquals(pEsperado, pEnviado);
	}

	/**
	 * O segundo pacote a ser enviado é o <1500, 2999>, porém ele só poderá ser
	 * transmitido depois de receber o SACK do primeiro pacote.
	 */
	@Test
	public void testEnviarPacote2() {

		tx.enviarPacote();
		tx.receberSACK(new SACK(0, Parametros.mss));

		Pacote pEsperado = new Pacote();
		pEsperado.setByteInicialEFinal(Parametros.mss, 2 * Parametros.mss - 1);
		pEsperado.setDestino(0);

		Pacote pEnviado = tx.enviarPacote();
		pEnviado.setDestino(0);

		assertEquals(pEsperado, pEnviado);
	}

	/**
	 * Assim que o TxTCP inicia, ele está pronto para transmitir apenas um
	 * pacote de tamanho MSS.
	 */
	@Test
	public void testProntoParaTransmitir() {
		assertTrue(tx.prontoParaTransmitir());
	}

	/**
	 * Como a janela de congestionamento começa igual a 1MSS, o Tx só pode
	 * enviar um pacote e aguardar o ACK chegar para aumentar sua cwnd.
	 */
	@Test
	public void testProntoParaTransmitir2() {
		tx.enviarPacote();
		assertFalse(tx.prontoParaTransmitir());
	}

	/**
	 * Tentando enviar pacotes fora da janela de congestionamento.
	 */
	@Test(expected = TxTCPNotReadyToSendException.class)
	public void testNotReadException() {
		tx.enviarPacote();
		tx.enviarPacote();
	}

}
