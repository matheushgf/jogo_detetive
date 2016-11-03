var app = angular.module("rankApp", []);
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