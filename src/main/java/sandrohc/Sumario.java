package sandrohc;

import java.util.Date;

public class Sumario {
	public short[] licoes;
    public int modulo;
	public long data;
	public String planificacao;

	public Sumario(short[] licoes, int modulo, long data, String planificacao) {
		this.licoes = licoes;
        this.modulo = modulo;
		this.data = data;
		this.planificacao = planificacao;
	}
}
