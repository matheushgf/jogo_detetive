package hello;

/**
 * Created by paulo on 8/13/2016.
 */
public class Adm {

    private String username;
    private String senha;

    public Adm(String username, String senha)
    {
        this.username = username;
        this.senha = senha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

