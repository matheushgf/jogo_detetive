package hello;

public class Alternativa {
	private String alternativa;
	private Boolean status;
	
	public Alternativa(String alternativa, Boolean status) {
		this.setAlternativa(alternativa);
		this.setStatus(status);
	}

	public String getAlternativa() {
		return alternativa;
	}

	public void setAlternativa(String alternativa) {
		this.alternativa = alternativa;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
