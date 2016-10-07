package hello;

import java.util.List;

public class Pergunta {
	private int id;
	private String pergunta;
	private List<Alternativa> alternativas;
	
	public Pergunta(int id, String pergunta, List<Alternativa> alternativas) {
		this.setId(id);
		this.setPergunta(pergunta);
		this.setAlternativas(alternativas);
	}
	
	public String getPergunta() {
		return pergunta;
	}
	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public List<Alternativa> getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(List<Alternativa> alternativas) {
		this.alternativas = alternativas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
