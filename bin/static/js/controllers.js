var app = angular.module("jogoApp", []);

//----------Controller da página de RANK
app.controller("rankCtrl", function($scope) {

	var xhr = new XMLHttpRequest();
	var url = "/users/rank";
	xhr.onload = function (e) {
		  if (xhr.readyState === 4) {
		    if (xhr.status === 200) {
		    	var data = jQuery.parseJSON(xhr.responseText);
		      	$scope.users = data;
		    } else {
		      console.error(xhr.statusText);
		    }
		  }
		};
	xhr.open("GET", url, false);
	xhr.onerror = function (e) {
	  console.error(xhr.statusText);
	};
	xhr.send(null);	
});

//----------Controller da p�gina das perguntas
app.controller("gameCtrl", function($scope, $http) {
	
	$scope.perguntaJogador;
	$scope.email = sessionStorage.getItem("email");
	if($scope.email == null){
		swal({
			title: "Não logado",
			type: "warning",
			text: "Usuário não logado! Redirecionando para a página de login de ADM.",
			showConfirmButton: true,
			html: true
		},
			function(){
				window.location.href = '/index.html';
			}
		);
	}
	
	$scope.getPergunta = function(){
		
		var url = "/getperguntaa/"+$scope.email+"?format=json";
		
		$http.get(url).then(function(response){
			if(response.data[0].error == false){
				$scope.perguntaJogador=response.data[0].pergunta;
				$scope.atualizarQuestao();
			}else{
				swal({
					title: "Erro",
					text: "Erro ao pegar número da pergunta! Contate o administrador.",
					showConfirmButton: true,
					html: true,
					type: "error"
				},function(){
					window.location.href = '/index.html';
				});
				
			}
		});
	}
	
	$scope.atualizarQuestao = function(){
		$http.get("/perguntas/"+$scope.perguntaJogador+"?format=json&jsoncallback=").then(function(response){
			if(response.data[0].error==false){
				if(response.data[0].over==false){
					var dados = response.data[0];
					$scope.pergunta = dados.pergunta;
					$scope.op1 = dados.alternativa[0].alternativa;
					$scope.op2 = dados.alternativa[1].alternativa;
					$scope.op3 = dados.alternativa[2].alternativa;
					
					$("#op1").attr($scope.op1);
					$("#op2").attr($scope.op2);
					$("#op3").attr($scope.op3);
				}else{
					window.location.href = '/rank.html';
				}
			}else{
				swal({
					title: "Erro",
					text: "Erro ao atualizar pergunta! Contate o administrador.",
					timer: 2000,
					showConfirmButton: false,
					html: true
				});
				window.location.href = '/index.html';
			}
		});
	}
	
	$scope.opcaoEscolhida = function($event){
		var textoOpcao=$event.currentTarget.value;
		var url="";
		url = "/respostas";
		
		var message = {
			email: $scope.email,
			pergunta: $scope.perguntaJogador,
			resposta: textoOpcao
		};
			
		//$http.defaults.headers.post["Content-Type"] = "application/json";
		$http.post(url, message)
		.then(function(response){
			var dados = response.data[0];
			console.log(dados);
			if(dados.error==false){
				$scope.getPergunta();
			} else {
				swal({
					title: "Erro",
					text: "Erro ao aplicar resposta! Contate o administrador.",
					timer: 2000,
					showConfirmButton: false,
				});
				window.location.href = '/sucess.html';
			}
		});
	}
});

//----------Controller da página ADM - Usuários
app.controller("admUsersCtrl", function($scope, $http) {

	$scope.init = function(){
		var userName = sessionStorage.getItem("userNameADM");
		if(userName === "null"){
			swal({
				title: "Não logado",
				type: "warning",
				text: "Usuário não logado! Redirecionando para a página de login de ADM.",
				showConfirmButton: true,
				html: true
			},
				function(){
					window.location.href = '/loginadm.html';
				}
			);
		}
	}
	
	$scope.atualizaUsers = function(){
		$http.get("/users")
		.then(function(response){
			$scope.users = response.data;
		});
	}
	
	$scope.deletaUser = function(jog_email){
		$http.post("/users/delete", {email: jog_email})
		.then(function(response){
			if(response.data[0].error){
				swal({
					title: "Error ao deletar",
					type: "error",
					text: "Erro ao deletar usuário! Tente novamente. <p> Mensagem de erro: "+response.data[0].error_details,
					showConfirmButton: true,
					html: true
				});
			}
			$scope.atualizaUsers();
		});
	}
});

//----------Controller da página ADM - Perguntas
app.controller("admQuestionsCtrl", function($scope, $http) {
	
	$scope.init = function(){
		var userName = sessionStorage.getItem("userNameADM");
		if(userName === "null"){
			swal({
				title: "Não logado",
				type: "warning",
				text: "Usuário não logado! Redirecionando para a página de login de ADM.",
				showConfirmButton: true,
				html: true
			},
				function(){
					window.location.href = '/loginadm.html';
				}
			);
		}
	}
	
	$scope.pergunta = {
		alternativas: []
	};
 	
	$scope.atualizaCorreta = function(){
		switch($scope.correta){
			case "alt1":
				$scope.pergunta.alternativas[0].status=true;
				$scope.pergunta.alternativas[1].status=false;
				$scope.pergunta.alternativas[2].status=false;
				break;
			case "alt2":
				$scope.pergunta.alternativas[0].status=false;
				$scope.pergunta.alternativas[1].status=true;
				$scope.pergunta.alternativas[2].status=false;
				break;
			case "alt3":
				$scope.pergunta.alternativas[0].status=false;
				$scope.pergunta.alternativas[1].status=false;
				$scope.pergunta.alternativas[2].status=true;
				break;
		}
	}
	
	$scope.$watch('correta', $scope.atualizaCorreta);
	
	$scope.atualizaQuestions = function(){
		$http.get("/perguntas")
		.then(function(response){
			$scope.questions = response.data;
		});
	}
	
	$scope.deletaQuestion = function(id){
		$http.post("/perguntas/delete", {id: id})
		.then(function(response){
			if(response.data[0].error){
				swal({
					title: "Error ao deletar",
					type: "error",
					text: "Erro ao deletar pergunta! Tente novamente. <p> Mensagem de erro: "+response.data[0].error_details,
					showConfirmButton: true,
					html: true
				});
			}
			else $scope.atualizaQuestions();
		});
	}
	
	$scope.addQuestion = function(){
		$http.post("/perguntas/add", $scope.pergunta)
		.then(function(response){
			if(response.data[0].error){
				swal({
					title: "Error ao inserir",
					type: "error",
					text: "Erro ao inserir pergunta! Tente novamente. <p> Mensagem de erro: "+response.data[0].error_details,
					showConfirmButton: true,
					html: true
				});
			}
			else{
				swal({
					title: "Sucesso",
					type: "success",
					text: "Pergunta incluída com sucesso",
					showConfirmButton: true,
					html: true
				});
				$scope.pergunta = {
					alternativas: []
				};
				$scope.atualizaQuestions();
			}
		});
	}
	
	
	
});