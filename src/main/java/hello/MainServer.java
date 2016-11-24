package hello;

import static spark.Spark.*;

import java.util.LinkedList;
import java.util.List;

public class MainServer {

    final static Model model = new Model();

    public static void main(String[] args) {
    	
        // Get port config of heroku on environment variable
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
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
    }

    public static void initialize (){
    	model.deleteJogador("a@a.com");
        model.addJogador(new Jogador("a@a.com", "1"));
        model.addAdm(new Adm("adm", "game"));
        
        Pergunta pergunta;
		Alternativa alt1;
		Alternativa alt2;
		Alternativa alt3;
		List<Alternativa> alternativas1 = new LinkedList<Alternativa>(); ;
		alt1 = new Alternativa("França", false);
		alt2 = new Alternativa("Canadá", true);
		alt3 = new Alternativa("Guiana Francesa", false);
		alternativas1.add(alt1);
		alternativas1.add(alt2);
		alternativas1.add(alt3);
		pergunta = new Pergunta(0, "O ladrão foi para onde se fala francês no continente americano.",alternativas1);
		model.addPergunta(pergunta);
		
		alt1 = new Alternativa("Suíça", true);
		alt2 = new Alternativa("Canadá", false);
		alt3 = new Alternativa("Japão", false);
		List<Alternativa> alternativas2 = new LinkedList<Alternativa>(); ;
		alternativas2.add(alt1);
		alternativas2.add(alt2);
		alternativas2.add(alt3);
		pergunta = new Pergunta(1, "Ele foi para um país sem costa marítima de bandeira vermelha e branca.",alternativas2);
		model.addPergunta(pergunta);
		
		alt1 = new Alternativa("Camberra", true);
		alt2 = new Alternativa("Sidney", false);
		alt3 = new Alternativa("Melbourne", false);
		List<Alternativa> alternativas3 = new LinkedList<Alternativa>(); ;
		alternativas3.add(alt1);
		alternativas3.add(alt2);
		alternativas3.add(alt3);
		pergunta = new Pergunta(2, "Ele fugiu para a capital da Austrália.",alternativas3);
		model.addPergunta(pergunta);
		
		alt1 = new Alternativa("Paraguai", false);
		alt2 = new Alternativa("Suíça", false);
		alt3 = new Alternativa("China", true);
		List<Alternativa> alternativas4 = new LinkedList<Alternativa>(); ;
		alternativas4.add(alt1);
		alternativas4.add(alt2);
		alternativas4.add(alt3);
		pergunta = new Pergunta(3, "Ele fugiu de navio.",alternativas4);
		model.addPergunta(pergunta);
		
		alt1 = new Alternativa("Toronto", false);
		alt2 = new Alternativa("Ottawa", true);
		alt3 = new Alternativa("Montreal", false);
		List<Alternativa> alternativas5 = new LinkedList<Alternativa>(); ;
		alternativas5.add(alt1);
		alternativas5.add(alt2);
		alternativas5.add(alt3);
		pergunta = new Pergunta(4, "Ele fugiu para a capital do Canadá.",alternativas5);
		model.addPergunta(pergunta);
		
		alt1 = new Alternativa("Holanda", true);
		alt2 = new Alternativa("Nova Zelândia", false);
		alt3 = new Alternativa("Indonésia", false);
		List<Alternativa> alternativas6 = new LinkedList<Alternativa>(); ;
		alternativas6.add(alt1);
		alternativas6.add(alt2);
		alternativas6.add(alt3);
		pergunta = new Pergunta(5, "Fugiu para um país onde se fala neerlandês.",alternativas6);
		model.addPergunta(pergunta);
		
		alt1 = new Alternativa("Rio de Janeiro", false);
		alt2 = new Alternativa("Buenos Aires", false);
		alt3 = new Alternativa("Brasília", true);
		List<Alternativa> alternativas7 = new LinkedList<Alternativa>(); ;
		alternativas7.add(alt1);
		alternativas7.add(alt2);
		alternativas7.add(alt3);
		pergunta = new Pergunta(6, "Fugiu para a capital do Brasil.",alternativas7);
		model.addPergunta(pergunta);
		
		model.addConfig(new Config(6));    }


}
