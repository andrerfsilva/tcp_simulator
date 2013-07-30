package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.exceptions.TxTCPNotReadyToSendException;
import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.Parametros;
import br.ufrj.ad.simulator.models.SACK;
import br.ufrj.ad.simulator.models.TxTCP;

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
		tx = new TxTCP();
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
