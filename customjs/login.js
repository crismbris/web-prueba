function getDataLogin(){
	var email = $("#emailLogIn").val();
	var password = $("#passWordLogIn").val();

	var log = {"email":email,"password":password};
	console.log(log);

	logIn(email, password);
}


function logIn2(email, password){
 $.getJSON("http://localhost:8080/user/login/"+email+"/"+password, function (data2) {
            var email2=data2["email"];
            if(email==email2){

              localStorage.setItem("currentUser", JSON.stringify(data2));
              console.log("correct!");
              location.href = "allIdeas.html";
            }
          })
 ;
}


function logIn(email,password){
  $.getJSON("http://localhost:8080/user/login/"+email+"/"+password, function(data2) {
    console.log("aa"+data2);
    if(data2==null){
      console.log("soy null");
      alert("Identification error. Please check that the email and password are correct.")
    }else{
        localStorage.setItem("currentUser", JSON.stringify(data2));
              console.log("correct!");
              location.href = "allIdeas.html";
    }
});
}