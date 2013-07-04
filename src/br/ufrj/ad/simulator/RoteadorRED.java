package br.ufrj.ad.simulator;

/**
 * Um roteador com disciplina RED.
 * 
 * @author André Ramos, Wellington Mascena.
 * 
 */
public class RoteadorRED extends Roteador {

	private int minth; // limite inferior
	private int maxth; // limite superior
	private int count; // número de pacotes não descartados desde o último
						// descarte
	private double maxp; // constante louca
	private double wq; // peso
	private long m; // estimativa do número de pacotes que poderiam ser
					// transmitidos durante o período ocioso (período
					// ocioso/tempo médio de transmição de pacote)
	private double avg; // média

	private Random gerador;
	private Estimador estimadorPeriodoOcioso;
	private double inicioPeriodoOcioso;
	private double fimPeriodoOcioso;

	private int mss; // MSS: maximum segment size, em bytes.
	private double taxaEnlaceDeSaida;

	
	public RoteadorRED() {
		// dados de entrada do simulador
		mss = 1500;
		taxaEnlaceDeSaida = 10E6; // 10 Mbps!

		// Padrão CISCO!
		wq = 0.002;
		minth = 5;
		maxth = 15;
		maxp = (double) 1 / 50;

		// valores iniciais chutados!
		avg = 0;
		count = 0;
		m = 0;

		// estimador período ocioso
		estimadorPeriodoOcioso = new Estimador();
		inicioPeriodoOcioso = 0;
		gerador = new Random();
	}

	@Override
	public boolean receberPacote(Pacote p, double tempoAtualSimulado) {
		atualizarAVG();
		atualizarM(tempoAtualSimulado);
		
		if(buffer.size() == getTamanhoBuffer()){
			count = 0;
			return false;
			
		}
		
		/*
		 * Politica de inserção no RED. 
		 * Baseada na sessão 4 do trabalho.
		 */
		if (avg < minth) {
			buffer.add(p);
			count++;
			return true;
		} else if (avg > maxth) {
			count = 0;
			return false;
		} else {
			if (gerador.nextDouble() < getPa()) {
				count = 0;
				return false;
			} else {
				buffer.add(p);
				count++;
				return true;
			}
		}
	}

	public int getMaxth() {
		return maxth;
	}

	public int getMinth() {
		return minth;
	}

	public double getAvg() {
		return avg;
	}

	private void atualizarM(double tempoAtualSimulado) {
		if (getNumeroPacotes() == 0) {
			fimPeriodoOcioso = tempoAtualSimulado;
			estimadorPeriodoOcioso.coletarAmostra(fimPeriodoOcioso
					- inicioPeriodoOcioso);

			double tempoTransmissaoPacote = mss / taxaEnlaceDeSaida;
			m = Math.round(estimadorPeriodoOcioso.getMedia()
					/ tempoTransmissaoPacote);
		}
	}

	private void atualizarAVG() {
		if (getNumeroPacotes() > 0) {
			avg = (1 - wq) * avg + wq * getNumeroPacotes();
		} else {
			avg = Math.pow(1 - wq, m) * avg;
		}
	}

	public void enviarProximoPacote(double tempoAtualSimulado) {
		super.enviarProximoPacote(tempoAtualSimulado);
		if (getNumeroPacotes() == 0) {
			inicioPeriodoOcioso = tempoAtualSimulado;
		}
	}

	private double getPa() {

		return getPb() / (1 - count * getPb());
	}

	private double getPb() {

		return maxp * (avg - minth) / (maxth - minth);
	}

	// getters and setter...

	public int getMss() {
		return mss;
	}

	public void setMss(int mss) {
		this.mss = mss;
	}

	public double getTaxaEnlaceDeSaida() {
		return taxaEnlaceDeSaida;
	}

	public void setTaxaEnlaceDeSaida(double taxaEnlaceDeSaida) {
		this.taxaEnlaceDeSaida = taxaEnlaceDeSaida;
	}

}
