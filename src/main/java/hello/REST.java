package hello;



import static spark.Spark.get;
import static spark.Spark.post;


import java.io.UnsupportedEncodingException;
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

	        			//e.printStackTrace();

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
						jsonObj.put("jogador", jogador.getEmail());
						jsonObj.put("pontos", jogador.getPonto());

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

                String jogador = json.getString("jogador");


                try {

                    model.deleteJogador(jogador);



                    JSONArray jsonResult = new JSONArray();
                    JSONObject jsonObj = new JSONObject();

                    jsonObj.put("status", 1);


                    jsonResult.put(jsonObj);



                    return jsonResult;





                } catch (JSONException e) {

                    e.printStackTrace();
                }

                JSONArray jsonResult = new JSONArray();
                JSONObject jsonObj = new JSONObject();

                jsonObj.put("status", 0);


                jsonResult.put(jsonObj);

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
						jsonObj.put("student", jogador.getEmail());
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
		get("/resposta/:pergunta/:resposta", new Route() {
			
			@Override
			public Object handle(final Request request, final Response response) throws Exception {
				
				response.header("Acess-Control-Allow-Origin", "*");
				
				try
				{
					String respostaJogador = request.params(":resposta");
					int perguntaJogador = Integer.parseInt(request.params(":pergunta"));
					
					if(respostaJogador != null){
						JSONArray jsonResult = new JSONArray();
						JSONObject jsonObj = new JSONObject();
						if(model.checarResposta(respostaJogador, perguntaJogador)){
							jsonObj.put("resposta", "certo");
						}else jsonObj.put("resposta", "errado");
						jsonResult.put(jsonObj);
						return jsonResult;
					}
				}
				catch(JSONException e)
				{
					JSONArray jsonResult = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("resposta", "errado");
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
	            	Pergunta pergunta = model.pesquisaPerguntaPorId(id);
	            	
	            	JSONArray jsonResult = new JSONArray();
	         	    JSONObject jsonObjQuestion = new JSONObject();
	         	    
	         	    jsonObjQuestion.put("idPergunta", pergunta.getId());
	        		jsonObjQuestion.put("pergunta", pergunta.getPergunta());
	        		jsonObjQuestion.put("alternativa", pergunta.getAlternativas());
	        		
	        		jsonResult.put(jsonObjQuestion);

	        		
	        		
	             	return jsonResult;
	             	
	        		} catch (JSONException e) {
	        				
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
	         	    
	         	    jsonObjQuestion.put("pergunta", pergunta);
	       
	        		
	        		jsonResult.put(jsonObjQuestion);

	        		
	        		
	             	return jsonResult;
	             	
	        		} catch (JSONException e) {
	        				
	        			e.printStackTrace();
	        		}
	         	    	
	
	     	    return null;
	     	     
	         }
	         
	      });
		
	}

}
