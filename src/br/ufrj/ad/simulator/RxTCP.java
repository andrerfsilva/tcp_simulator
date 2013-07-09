package br.ufrj.ad.simulator;

import java.util.ArrayList;

/**
 * Representa o lado receptor de uma sessão TCP.
 * 
 * @author André Ramos
 * 
 */
public class RxTCP {

	private long proximoByteEsperado;
	private ArrayList<int[]> sequenciasRecebidasCorretamente;

	public RxTCP() {
		proximoByteEsperado = 0;
		sequenciasRecebidasCorretamente = new ArrayList<int[]>();
	}

	public SACK receberPacote(Pacote p) {
		
		// TODO: Fazer!!!!!!
		SACK sack = null;

		return sack;
	}

}
