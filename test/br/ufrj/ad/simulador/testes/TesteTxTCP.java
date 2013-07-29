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
	public void testReceberSACK() {
		fail("Not yet implemented");
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
