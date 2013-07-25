package br.ufrj.ad.simulator;

import java.util.ArrayList;

/**
 * Representa o lado receptor de uma sessão TCP.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class RxTCP {

	private long proximoByteEsperado;

	/**
	 * Sequências recebidas que chegaram depois do próximo pacote esperado.
	 */
	private ArrayList<long[]> sequencias;

	public RxTCP() {
		proximoByteEsperado = 0;
		sequencias = new ArrayList<long[]>();
	}

	public SACK receberPacote(Pacote p) {

		if (p.getByteInicial() < this.proximoByteEsperado) {

			if (this.sequencias.size() > 0) {
				long[][] vetorSequencias = new long[this.sequencias.size()][2];
				this.sequencias.toArray(vetorSequencias);
				return new SACK(p.getDestino(), this.proximoByteEsperado,
						vetorSequencias);
			} else {
				return new SACK(p.getDestino(), this.proximoByteEsperado);
			}
		}

		if (this.sequencias.size() == 0) {

			if (p.getByteInicial() == this.proximoByteEsperado) {

				this.proximoByteEsperado = p.getByteFinal() + 1;
				return new SACK(p.getDestino(), this.proximoByteEsperado);

			} else {
				this.atualizaSequenciasRecebidas(p);
			}
		} else {

		}

		return new SACK(p.getDestino(), this.proximoByteEsperado);
	}

	/**
	 * Método que atualiza o vetor de sequências recebidas a partir de um pacote
	 * recebido.
	 * 
	 * @param p
	 *            pacote recebido
	 */
	private void atualizaSequenciasRecebidas(Pacote p) {
		long limites[] = new long[2];

		limites[0] = p.getByteInicial();
		limites[1] = p.getByteFinal() + 1;

		if (this.sequencias.size() == 0) {

			this.sequencias.add(limites);

			return;

		} else {
			/*
			 * TESTE PARA A PRIMEIRA SEQUÊNCIA DE PACOTES:
			 * 
			 * Se o byte final do pacote for menor ou igual ao byte inicial da
			 * sequência, então ele deve ser incluído à esquerda sequência. Ele
			 * pode ser incorporado na sequência, ou uma nova sequência à
			 * esquerda será adicionada.
			 */
			if (this.sequencias.get(0)[0] > p.getByteFinal()) {
				if (this.sequencias.get(0)[0] == p.getByteFinal() + 1) {
					this.sequencias.get(0)[0] = p.getByteInicial();
					return;
				} else {
					this.sequencias.add(0, limites);
					return;
				}
			}

			for (int i = this.sequencias.size() - 1; i >= 0; i--) {

				/*
				 * TESTE PARA AS DEMAIS SEQUÊNCIAS DE PACOTES:
				 * 
				 * Primeiro, testamos se o pacote está contido na sequência, o
				 * que indica um pacote duplicado. Nesse caso, não há o que
				 * fazer. Simplesmente return. Caso contrário, ele será inserido
				 * no final da sequência. OBSERVAÇÃO: O for é decrescente! Isso
				 * influencia no desempenho e corretude do algoritmo!
				 */

				if (this.pacoteContidoNaSequência(p, i)) {
					return;
				}

				if (this.sequencias.get(i)[1] <= p.getByteInicial()) {
					this.appendAdjacente(p, i);
				}
			}
		}

	}

	/**
	 * Adiciona o pacote à direita da sequência i. Essa inserção pode resultar
	 * na redução do vetor de sequência, se a nova sequência tornar-se adjacente
	 * à sequência i+1, e assim sucessivamente.
	 * 
	 * @param p
	 *            pacote recebido
	 * @param i
	 *            sequência onde o novo pacote será inserido
	 * 
	 */
	private void appendAdjacente(Pacote p, int i) {

		if (this.sequencias.get(i)[1] == p.getByteInicial()) {
			this.sequencias.get(i)[1] = p.getByteFinal() + 1;

			if (i < this.sequencias.size() - 1) {
				if (this.sequencias.get(i)[1] == this.sequencias.get(i + 1)[0]) {
					this.sequencias.get(i)[1] = this.sequencias.get(i + 1)[1];
					this.sequencias.remove(i + 1);
				}
			}

		} else {

			if (i < this.sequencias.size() - 1
					&& this.sequencias.get(i + 1)[0] == p.getByteFinal() + 1) {
				this.sequencias.get(i + 1)[0] = p.getByteInicial();
			} else {

				long[] novaSequencia = new long[2];
				novaSequencia[0] = p.getByteInicial();
				novaSequencia[1] = p.getByteFinal() + 1;

				this.sequencias.add(i + 1, novaSequencia);

			}

		}

	}

	private boolean pacoteContidoNaSequência(Pacote p, int sequencia) {
		return this.sequencias.get(sequencia)[0] <= p.getByteInicial()
				&& this.sequencias.get(sequencia)[1] > p.getByteFinal();
	}
}
