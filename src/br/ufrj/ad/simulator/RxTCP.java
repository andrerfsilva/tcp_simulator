package br.ufrj.ad.simulator;

import java.util.ArrayList;

/**
 * Representa o lado receptor de uma sessão TCP.
 * 
 * @author André Ramos
 * 
 */
public class RxTCP {

	private int proximoByteEsperado;
	private ArrayList<int[]> sequenciasRecebidasCorretamente;

	public RxTCP() {
		proximoByteEsperado = 0;
		sequenciasRecebidasCorretamente = new ArrayList<int[]>();
	}

	public SACK receberPacote(Pacote p) {
		SACK sack = null;

		if (p.getByteInicial() < proximoByteEsperado) {
			//TODO: Fazer essa bagaça!
			
			int[][] sequencia = null;
			sack = new SACK(proximoByteEsperado, sequencia);
		}
		else if (p.getByteInicial() == proximoByteEsperado) {
			proximoByteEsperado = p.getByteFinal() + 1;
		} else {

		}

		if (sequenciasRecebidasCorretamente.size() == 0) {
			sack = new SACK(proximoByteEsperado);
		}

		return sack;
	}

}
