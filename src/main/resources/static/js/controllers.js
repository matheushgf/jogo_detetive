var app = angular.module("jogoApp", []);

//Controller da página de RANK
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

//Controller da página das perguntas
app.controller("gameCtrl", function($scope, $http) {
	
	$scope.perguntaJogador;
	$scope.email = sessionStorage.getItem("email");
	if($scope.email == null){
		window.location.href = '/index.html';
	}
	
	$scope.getPergunta = function(){
		
		var url = "/getpergunta/"+$scope.email+"?format=json";
		/*
		var xhr = new XMLHttpRequest();
		xhr.onload = function (e) {
			  if (xhr.readyState === 4) {
			    if (xhr.status === 200) {
			    	var data = jQuery.parseJSON(xhr.responseText);
			      	console.log(data[0].pergunta);
			      	sessionStorage.setItem("perguntajogador",data[0].pergunta);
			      	$scope.jogadorper = data[0].pergunta;
			    } else {
			      console.error(xhr.statusText);
			    }
			  }
			};
		xhr.open("GET", url, false);
		xhr.onerror = function (e) {
		  console.error(xhr.statusText);
		};
		xhr.send(null);*/
		
		$http.get(url).then(function(response){
			if(response.data[0].error == false){
				$scope.perguntaJogador=response.data[0].pergunta;
				$scope.atualizarQuestao();
			}else{
				console.log('ERRO PEGANDO NUMERO DA PERGUNTA');
				window.location.href = '/index.html';
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
					console.log('ACABARAM AS PERGUNTAS');
					window.location.href = '/rank.html';
				}
			}else{
				console.log('ERRO ATUALIZANDO A PERGUNTA');
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
			if(dados.error==0){
				$scope.getPergunta();
			} else {
				console.log("ERRO APLICANDO RESPOSTA");
				window.location.href = '/sucess.html';
			}
		});
	}
});