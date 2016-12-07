package hello;

import static spark.Spark.*;

public class MainServer {

    final static Model model = new Model();

    public static void main(String[] args) {
    	
        // Get port config of heroku on environment variable
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        port(port);



        initialize();

        staticFileLocation("/static");

        REST controller = new REST(model);

        controller.getLogin();
        controller.loginADM();
        controller.getUsers();
        controller.addJogadores();
        controller.deleteUser();
        controller.getPerguntaPorId();
		controller.getResposta();
		controller.getPerguntaJogador();
		controller.getRank();
		controller.getAllPerguntas();
		controller.deletePerguntaPorId();
		controller.addPergunta();
		controller.isAvailable();
    }

    public static void initialize (){
    	model.deleteJogador("a@a.com");
        model.addJogador(new Jogador("a@a.com", "1"));
        model.addAdm(new Adm("adm", "game"));
        model.addAdm(new Adm("adm1", "game1"));
		 }


}
