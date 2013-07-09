package br.ufrj.ad.simulator;

import com.sun.org.apache.xpath.internal.operations.Equals;

/**
 * Armazena as informações do SACK, ou seja, o byte do próximo pacote esperado,
 * e os bytes recebidos corretamente fora de ordem.
 * 
 * @author André Ramos, Felipe Teixeira, Wellignton Mascena, featuring Victor
 *         Maia
 * 
 */
public class SACK {

	private long proximoByteEsperado;
	private long[][] sequenciasRecebidasCorretamente;

	public SACK(long proximoByteEsperado,
			long[][] sequenciasRecebidasCorretamente) {
		this.proximoByteEsperado = proximoByteEsperado;
		this.sequenciasRecebidasCorretamente = sequenciasRecebidasCorretamente;
	}

	public SACK(int proximoByteEsperado) {
		this.proximoByteEsperado = proximoByteEsperado;
		this.sequenciasRecebidasCorretamente = null;
	}

	public long getProximoByteEsperado() {
		return proximoByteEsperado;
	}

	public long[][] getSequenciasRecebidasCorretamente() {
		return sequenciasRecebidasCorretamente;
	}

	public boolean equals(Object o) {

		if (o instanceof SACK) {
			SACK osack = (SACK) o;

			// Se o próximo byte esperado for diferente, então os objetos são
			// diferentes.
			if (osack.getProximoByteEsperado() != this.getProximoByteEsperado()) {
				return false;
			}

			// Toma NullPointer!
			if (osack.getSequenciasRecebidasCorretamente() == null
					|| this.sequenciasRecebidasCorretamente == null) {

				return osack.getSequenciasRecebidasCorretamente() == null
						&& this.sequenciasRecebidasCorretamente == null;
			}

			// Se o vetor de sequência tiver tamanho diferente, então os
			// objetos
			// são diferentes.
			if (sequenciasRecebidasCorretamente.length != osack
					.getSequenciasRecebidasCorretamente().length) {
				return false;
			}

			// Agora eu sei que o próximo byte esperado e o tamanho do vetor de
			// sequência são compatíveis. Resta testar o conteúdo linha a linha.
			for (int i = 0; i < sequenciasRecebidasCorretamente.length; i++) {

				// Se o tamanho da linha for diferente, então os objetos são
				// diferentes.
				if (sequenciasRecebidasCorretamente[i].length != osack
						.getSequenciasRecebidasCorretamente()[i].length) {
					return false;
				}

				for (int j = 0; j < sequenciasRecebidasCorretamente[i].length; j++) {

					// Se o conteúdo dos vetores forem incompatíveis, então são
					// objetos diferentes.
					if (sequenciasRecebidasCorretamente[i][j] != osack
							.getSequenciasRecebidasCorretamente()[i][j]) {
						return false;
					}
				}

			}
			// Depois de todos esses testes, se não retornou false, então os
			// objetos são iguais.
			return true;
		}

		// Se não são instâncias de SACK, não há conversa!
		return false;
	}

}
