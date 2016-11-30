package hello;

import static spark.Spark.get;
import static spark.Spark.post;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import spark.Request;
import spark.Response;
import spark.Route;


public class REST{

	private Model model;

	public REST(Model store){
		this.model = store;
	}

	public void getLogin(){

		get("/login/:username/:password", new Route() {

			@Override
            public Object handle(final Request request, final Response response){

	        	response.header("Access-Control-Allow-Origin", "*");


	            try {
	            	Jogador jogador = model.login(request.params(":username"), request.params(":password"));

	            	if(jogador != null){

	            		JSONArray jsonResult = new JSONArray();
		         	    JSONObject jsonObj = new JSONObject();

		        		jsonObj.put("email", jogador.getEmail());
		             	jsonResult.put(jsonObj);

		             	return jsonResult;

	            	} else {

	            	}
	        		} catch (JSONException e) {
	        			e.printStackTrace();
	        		}


	            JSONArray jsonResult = new JSONArray();
         	    JSONObject jsonObj = new JSONObject();

        		jsonObj.put("email", "");

             	jsonResult.put(jsonObj);

             	return jsonResult;
	         }

	      });
	}

	public void loginADM(){

		post("/login/adm", new Route() {
			@Override
			public Object handle(final Request request, final Response response){

				response.header("Access-Control-Allow-Origin", "*");

				JSONObject json = new JSONObject(request.body());
				String userName = json.getString("userName");
				String password = json.getString("password");


				try {
					Adm adm = model.admLogin(userName, password);

					if(adm != null){

						JSONArray jsonResult = new JSONArray();
						JSONObject jsonObj = new JSONObject();

						jsonObj.put("status", 1);
						jsonObj.put("userName", adm.getUsername());

						jsonResult.put(jsonObj);

						return jsonResult;

					} else {

					}



				} catch (JSONException e) {

					//e.printStackTrace();

				}


				JSONArray jsonResult = new JSONArray();
				JSONObject jsonObj = new JSONObject();

				jsonObj.put("status", 0);


				jsonResult.put(jsonObj);

				return jsonResult;



			}
		});


	}
	public void addJogadores(){
		post("/jogador/add", new Route() {

			@Override
			public Object handle(final Request request, final Response response){

				response.header("Access-Control-Allow-Origin", "*");
				JSONObject json = new JSONObject(request.body());
				String email = json.getString("email");
				String password = json.getString("password");
				Jogador jogador = new Jogador(email, password);

				try {
					boolean statusAdd = model.addJogador(jogador);
					if(statusAdd){
						JSONArray jsonResult = new JSONArray();
						JSONObject jsonObj = new JSONObject();

						jsonObj.put("status", 1);
						jsonObj.put("email", jogador.getEmail());
						jsonObj.put("password", jogador.getPassword());

						jsonResult.put(jsonObj);

						return jsonResult;

					} else {

					}



				} catch (JSONException e) {

					//e.printStackTrace();

				}


				JSONArray jsonResult = new JSONArray();
				JSONObject jsonObj = new JSONObject();

				jsonObj.put("status", 0);


				jsonResult.put(jsonObj);

				return jsonResult;



			}
		});

	}

	public void getUsers(){

		get("/users", new Route() {
			@Override
			public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{

				response.header("Access-Control-Allow-Origin", "*");

				JSONArray jsonResult = new JSONArray();


				try {

					List<Jogador> jogadores = model.getJogadores();

					for(Jogador jogador : jogadores){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("email", jogador.getEmail());
						jsonObj.put("points", jogador.getPonto());

						jsonResult.put(jsonObj);

					}


					return jsonResult;

				} catch (JSONException e) {

					e.printStackTrace();
				}


				return null;

			}

		});

	}

    public void deleteUser(){

        post("/users/delete", new Route() {
            @Override
            public Object handle(final Request request, final Response response){

                response.header("Access-Control-Allow-Origin", "*");

                JSONObject json = new JSONObject(request.body());

                String jogador = json.getString("email");

                JSONArray jsonResult = new JSONArray();
                JSONObject jsonObj = new JSONObject();
                
                try {
                    model.deleteJogador(jogador);

                    jsonObj.put("error", false);
                    jsonResult.put(jsonObj);
                    

                } catch (JSONException e) {
                	jsonObj.put("error", true);
                    jsonResult.put(jsonObj);
                    e.printStackTrace();
                }
        
                return jsonResult;
            }
        });


    }
    
    public void getRank(){

		get("/users/rank", new Route() {
			@Override
			public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{

				response.header("Access-Control-Allow-Origin", "*");

				JSONArray jsonResult = new JSONArray();


				try {
					List<Jogador> jogadores = model.getRank();
					int cont=1;
					for(Jogador jogador: jogadores){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("position",cont);
						cont++;
						jsonObj.put("email", jogador.getEmail());
						jsonObj.put("points", jogador.getPonto());

						jsonResult.put(jsonObj);

					}


					return jsonResult;

				} catch (JSONException e) {

					e.printStackTrace();
				}


				return null;

			}

		});

	}
    
    public void getResposta(){
		post("/respostas", new Route() {
			
			@Override
			public Object handle(final Request request, final Response response) throws Exception {
				
				response.header("Acess-Control-Allow-Origin", "*");
				try
				{
					JSONObject dados = new JSONObject(new String(request.body().getBytes(), "UTF-8"));
					String respostaJogador = dados.getString("resposta");
					int perguntaJogador = dados.getInt("pergunta");
					String email = dados.getString("email");
					
					if(respostaJogador != null){
						JSONArray jsonResult = new JSONArray();
						JSONObject jsonObj = new JSONObject();
						if(model.checarResposta(respostaJogador, perguntaJogador)){
							model.setarProxima(true, email);
							jsonObj.put("resposta", "certo");
						}else{
							model.setarProxima(false, email);
							jsonObj.put("resposta", "errado");
							}
						jsonObj.put("error", false);
						jsonResult.put(jsonObj);
						return jsonResult;
					}
				}
				catch(JSONException e)
				{
					JSONArray jsonResult = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("error", true);
					jsonObj.put("error_details", "Exception: " + e.getMessage());
					jsonResult.put(jsonObj);
				}
				return null;
			}
			
		});
	}
	
	public void getPerguntaPorId(){
		
		get("/perguntas/:id", new Route() {
			@Override
            public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{
	        	
	        	response.header("Access-Control-Allow-Origin", "*");
	        	 
	            Integer id = Integer.parseInt(request.params(":id"));
	            
	            try {
	            	JSONArray jsonResult = new JSONArray();
	         	    JSONObject jsonObjQuestion = new JSONObject();
	            	if(id<=model.getConfig().getMAX_PERGUNTAS()){
		            	Pergunta pergunta = model.pesquisaPerguntaPorId(id);
		         	    if(pergunta!=null){
		         	    	jsonObjQuestion.put("error", false);
		         	    	jsonObjQuestion.put("over", false);
		         	    	jsonObjQuestion.put("idPergunta", pergunta.getId());
			        		jsonObjQuestion.put("pergunta", pergunta.getPergunta());
			        		jsonObjQuestion.put("alternativa", pergunta.getAlternativas());
		         	    }else{
		         	    	jsonObjQuestion.put("error", true);
		         	    }
		             	
		         	    jsonResult.put(jsonObjQuestion);
						return jsonResult;
	            	}else{
	            		jsonObjQuestion.put("error", false);
	            		jsonObjQuestion.put("over", true);
	            		jsonResult.put(jsonObjQuestion);
	            		return jsonResult;
	            	}
	        		}catch (JSONException e) {
	        			JSONArray jsonResult = new JSONArray();
		         	    JSONObject jsonObjQuestion = new JSONObject();
	        			jsonObjQuestion.put("error_details", "Exception: "+e.getMessage());	
	        			e.printStackTrace();
	        			jsonResult.put(jsonObjQuestion);
						return jsonResult;
	        		} 	   
	         }
	         
	      });
		
	}
	
public void getAllPerguntas(){
		
		get("/perguntas", new Route() {
			@Override
            public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{
	        	
	        	response.header("Access-Control-Allow-Origin", "*");           
	            try {
	            	List<Pergunta> perguntas = model.getPerguntas();
	            	JSONArray jsonResult = new JSONArray();
	            	
					for(Pergunta pergunta : perguntas){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("id", pergunta.getId());
						jsonObj.put("enunciate", pergunta.getPergunta());
						JSONArray jsonAlternativas = new JSONArray();
						for(Alternativa a : pergunta.getAlternativas()){
							JSONObject jsonAlternativa = new JSONObject();
							jsonAlternativa.put("alternativa", a.getAlternativa());
							jsonAlternativa.put("status", a.getStatus());
							jsonAlternativas.put(jsonAlternativa);
						}
						jsonObj.put("alternativas", jsonAlternativas);
						jsonResult.put(jsonObj);
					}

					return jsonResult;
        		}catch (JSONException e) {
        				
        			e.printStackTrace();
        		}
	         	   
	     	    return null;
	     	     
	         }
	         
	      });
		
	}
	
	public void getPerguntaJogador(){
		
		get("/getpergunta/:email", new Route() {
			@Override
            public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{
	        	
	        	response.header("Access-Control-Allow-Origin", "*");
	        	 
	            String email = request.params(":email");
	            
	            try {
	            	int pergunta = model.getPerguntaJogador(email);
	            	
	            	JSONArray jsonResult = new JSONArray();
	         	    JSONObject jsonObjQuestion = new JSONObject();
	         	    jsonObjQuestion.put("error", false);	
	         	    jsonObjQuestion.put("pergunta", pergunta);
	        		jsonResult.put(jsonObjQuestion);
	             	return jsonResult;
	             	
	        		} catch (JSONException e) {	        			
	        			e.printStackTrace();
	        			JSONArray jsonResult = new JSONArray();
		         	    JSONObject jsonObjQuestion = new JSONObject();
		         	    jsonObjQuestion.put("error", true); 
		         	   jsonObjQuestion.put("error_details", "Exception: " + e.getMessage());
		        		jsonResult.put(jsonObjQuestion);
		        		return jsonResult;
	        		} 
	         }
	      });
	}
	
	public void deletePerguntaPorId(){
		
		post("/perguntas/delete", new Route() {
			@Override
            public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{
	        	
	        	response.header("Access-Control-Allow-Origin", "*");
	        	JSONObject dados = new JSONObject(new String(request.body().getBytes(), "UTF-8"));
	        	JSONArray jsonResult = new JSONArray();
	            
	            try {
	            	int id = dados.getInt("id");
	         	    JSONObject jsonObjQuestion = new JSONObject();
	            	if(id<=model.getConfig().getMAX_PERGUNTAS()){
		            	
	            		model.deleteQuestao(id);
	            		
	            		jsonObjQuestion.put("error", false);
		         	    jsonResult.put(jsonObjQuestion);
						return jsonResult;
	            	}else{
	            		jsonObjQuestion.put("error", true);
	            		jsonObjQuestion.put("error_details", "ID fora de range.");
	            		
	            		jsonResult.put(jsonObjQuestion);
	            		return jsonResult;
	            	}
	        		}catch (JSONException e) {
	        			JSONObject jsonObjQuestion = new JSONObject();
	        			jsonObjQuestion.put("error", true);
	            		jsonObjQuestion.put("error_details", "Exception: "+e.getMessage());
	            		jsonResult.put(jsonObjQuestion);
	            		return jsonResult;
	        		}     
	         }
	         
	      });
		
	}
	
	public void addPergunta(){
		
		post("/perguntas/add", new Route() {
			@Override
            public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{
	        	
	        	response.header("Access-Control-Allow-Origin", "*");
	        	JSONObject dados = new JSONObject(new String(request.body().getBytes(), "UTF-8"));
	        	JSONArray jsonResult = new JSONArray();
	            
	            try {
	         	    JSONObject jsonObjQuestion = new JSONObject();
	         	    List<Alternativa> alternativas = new ArrayList<Alternativa>();
					JSONArray alt = (JSONArray) dados.get("alternativas");
					for(int i=0;i<alt.length();i++){
						JSONObject a = alt.getJSONObject(i);
						alternativas.add(new Alternativa(a.getString("alternativa"), a.getBoolean("status")));
					}
	         	    
	         	    Pergunta pergunta = new Pergunta(model.getConfig().getMAX_PERGUNTAS()+1, dados.getString("enunciate"), alternativas);
            		model.addPergunta(pergunta);
            		jsonObjQuestion.put("error", false);
	         	    jsonResult.put(jsonObjQuestion);
					return jsonResult;
        		}catch (JSONException e) {
        			JSONObject jsonObjQuestion = new JSONObject();
        			jsonObjQuestion.put("error", true);
            		jsonObjQuestion.put("error_details", "Exception: "+e.getMessage());
            		jsonResult.put(jsonObjQuestion);
            		return jsonResult;
        		}
	         }
	         
	      });
	}
	
	public void isAvailable(){
		
		get("/users/available/:email", new Route() {
			@Override
            public Object handle(final Request request, final Response response) throws UnsupportedEncodingException{
	        	
				response.header("Access-Control-Allow-Origin", "*");
	        	 
	            String email = request.params(":email");
	            
	            try {
	            	JSONArray jsonResult = new JSONArray();
	         	    JSONObject jsonObjQuestion = new JSONObject();
	         	    jsonObjQuestion.put("error", false);	
	         	    jsonObjQuestion.put("available", model.isUserAvailable(email));
	        		jsonResult.put(jsonObjQuestion);
	             	return jsonResult;
	        	}catch (JSONException e) {	        			
	        		e.printStackTrace();
	        		JSONArray jsonResult = new JSONArray();
		         	JSONObject jsonObjQuestion = new JSONObject();
		         	jsonObjQuestion.put("error", true); 
		         	jsonResult.put(jsonObjQuestion);
		        	return jsonResult;
	        	} 
	         }
	         
	      });
		
	}
}
