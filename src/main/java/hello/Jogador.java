package hello;


public class Jogador {

    private String email;
    private String password;
    private int ponto;
    private int pergunta;
    
    public Jogador(){
    	ponto=0;
    	pergunta=1;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Jogador(String email, String password) {
        this.email = email;
        this.password = password;

    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getPonto() {
        return ponto;
    }
    public void setPonto(int question) {
        this.ponto = ponto;
    }

	public int getPergunta() {
		return pergunta;
	}

	public void setPergunta(int pergunta) {
		this.pergunta = pergunta;
	}
    






}
