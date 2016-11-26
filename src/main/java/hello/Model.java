package hello;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class Model{
	ObjectContainer jogadores = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/jogadores.db4o");
	ObjectContainer admDB = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/admDB.db4o");
	ObjectContainer perguntas = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/perguntas.db4o");
	ObjectContainer configs = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/config.db4o");
   
	public boolean isUserAvailable(String email){
		Query query = jogadores.query();
		query.constrain(Jogador.class);
		ObjectSet<Jogador> allJogadores = query.execute();

		for (Jogador jogador : allJogadores){
			if (jogador.getEmail().equals(email)) return false;
		}

		return true;
	}
	public boolean addJogador(Jogador jogador){
		jogadores.store(jogador);
		jogadores.commit();
			return true;
		}

	public List<Jogador> getJogadores(){
		Query query = jogadores.query();
		query.constrain(Jogador.class);
		ObjectSet<Jogador> jogadores = query.execute();
		
		List<Jogador> allJogadores = new ArrayList<Jogador>();
		for(Jogador jogador:jogadores){
			allJogadores.add(jogador);
		}
		
		Collections.sort(allJogadores, new Comparator<Jogador>() {
			@Override
		    public int compare(Jogador one, Jogador other) {
		        return one.getEmail().compareTo(other.getEmail());
		    }
		});
		
		return allJogadores;
	}

	public void addAdm(Adm adm){
		admDB.store(adm);
	}

	public Adm admLogin(String username, String senha){
		Query query = admDB.query();
		query.constrain(Adm.class);
		ObjectSet<Adm> adms = query.execute();

		for (Adm adm: adms){
			if(adm.getUsername().equals(username) && (adm.getSenha().equals(senha))) return adm;
		}
		return null;
	}
	
	public Jogador getJogador(String email){
		Query query = jogadores.query();
		query.constrain(Jogador.class);
	    ObjectSet<Jogador> allJogadores = query.execute();
	    
	    for(Jogador jogador : allJogadores){
	    	if(jogador.getEmail().equals(email)) return jogador;
	    }
	    return null;
	}

	public Jogador login(String email, String senha){
		Query query = jogadores.query();
		query.constrain(Jogador.class);
	    ObjectSet<Jogador> allJogadores = query.execute();
	    
	    for(Jogador jogador : allJogadores){
	    	if(jogador.getEmail().equals(email) && jogador.getPassword().equals(senha)) return jogador;
	    }
	    
	    return null;
	}

	public void deleteJogador(String email) {
		Query query = jogadores.query();
		query.constrain(Jogador.class);
		List<Jogador> allJogadores = query.execute();

		for (Jogador jogador : allJogadores) {
			if (jogador.getEmail().toLowerCase().equals(email)) {
				jogadores.delete(jogador);
				jogadores.commit();
				break;
			}
		}
	}
	
	public void addPergunta(Pergunta pergunta){
		perguntas.store(pergunta);
		perguntas.commit();
		changeConfig(getConfig().getMAX_PERGUNTAS()+1);
	}
	
	public List<Pergunta> getPerguntas(){
		Query query = perguntas.query();
		query.constrain(Pergunta.class);
		ObjectSet<Pergunta> perguntas = query.execute();
		
		List<Pergunta> allPerguntas = new ArrayList<Pergunta>();
		for(Pergunta pergunta:perguntas){
			allPerguntas.add(pergunta);
		}
		
		Collections.sort(allPerguntas, new Comparator<Pergunta>() {
			@Override
		    public int compare(Pergunta p1, Pergunta p2) {
		        return p1.getId()-p2.getId();
		    }
		});
		
		return allPerguntas;
	}
	
	public Pergunta iniciarJogo(){
		Query query = perguntas.query();
		query.constrain(Pergunta.class);
	    ObjectSet<Pergunta> allPerguntas = query.execute();
	    
	    Random rand = new Random(); 
	    int idPrimeiraPergunta = rand.nextInt(allPerguntas.size() - 1);
	    
	    for(Pergunta pergunta: allPerguntas){
	    	if(pergunta.getId() == idPrimeiraPergunta) return pergunta;
	    }
	    
	    return null;
	}
	
	public Pergunta perguntar(int ultimoId){
		Query query = perguntas.query();
		query.constrain(Pergunta.class);
	    ObjectSet<Pergunta> allPerguntas = query.execute();
	    
	    int idPergunta = 0;
	    int total = allPerguntas.size();
	    if(ultimoId != total){
	    	idPergunta = ultimoId +1;
	    }
	    
	    for(Pergunta pergunta: allPerguntas){
	    	if(pergunta.getId() == idPergunta) return pergunta;
	    }
	    
	    return null;
	}
	
	public Pergunta pesquisaPerguntaPorId(int id){
		
		Query query = perguntas.query();
		query.constrain(Pergunta.class);
	    ObjectSet<Pergunta> allPerguntas = query.execute();
		
	    for(Pergunta question:allPerguntas){
	    	if(question.getId() == id){
	    		return question;
	    	}
	    }
	    
	    return null;
	}
	
	public List<Jogador> getRank(){
		Query query = jogadores.query();
		query.constrain(Jogador.class);
		ObjectSet<Jogador> jogadoresQuery = query.execute();
		
		List<Jogador> allJogadores = new LinkedList<>();
		for(Jogador jogador:jogadoresQuery){
			allJogadores.add(jogador);
		}
		
		Collections.sort(allJogadores, new Comparator<Jogador>(){
				@Override
			   public int compare(Jogador user1, Jogador user2){
			      return user2.getPonto() - user1.getPonto();
			   }
			});
		
		return allJogadores;
	}
	
	public int pontuar(int multiplicador){
		return multiplicador * 10;
	}
	
	public boolean checarResposta(String resposta, int pergunta){
		Query query = perguntas.query();
		query.constrain(Pergunta.class);
	    ObjectSet<Pergunta> allPerguntas = query.execute();
	    
	    for(Pergunta p: allPerguntas){
	    	if(p.getId()==pergunta){
	    		for(Alternativa a:p.getAlternativas()){
	    			if(a.getStatus()==true && a.getAlternativa().equals(resposta)) return true;
	    		}
	    		return false;
	    	}
	    }
		return false;
	}
	
	public int getPerguntaJogador(String email){
		return getJogador(email).getPergunta();
	}
	
	public void setarProxima(boolean acerto, String email){
		Jogador jogador = getJogador(email);
		jogador.proximaPergunta();
		if(acerto){
			jogador.addPonto();
		}
	}
	
	public void deleteQuestao(int id) {
		Query query = perguntas.query();
		query.constrain(Pergunta.class);
		List<Pergunta> allQuestions = query.execute();

		for(Pergunta question:allQuestions){
			if(question.getId()==id){
				perguntas.delete(question);
				perguntas.commit();
				
				for(Pergunta p:allQuestions){
					if(p.getId()>=id){
						p.setId(p.getId()-1);
						perguntas.store(p);
						perguntas.commit();
					}
				}
				
				changeConfig(getConfig().getMAX_PERGUNTAS()-1);
				System.out.println("new max: "+String.valueOf(getConfig().getMAX_PERGUNTAS()));
				break;
			}
		}
	}
	
	public void addConfig(Config c){
		configs.store(c);
		configs.commit();
	}
	
	public void changeConfig(int max){
		Query query = configs.query();
		query.constrain(Config.class);
	    ObjectSet<Config> allConfigs = query.execute();
	    
	    for(Config c: allConfigs){
	    	c.setMAX_PERGUNTAS(max);
	    	configs.store(c);
	    	configs.commit();
	    }
	}
	
	public Config getConfig(){
		Query query = configs.query();
		query.constrain(Config.class);
	    ObjectSet<Config> allConfigs = query.execute();
	    
	    for(Config c: allConfigs){
	    	return c;
	    }
		return null;
	}

}
