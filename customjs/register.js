function register(){

	var firstName = $("#firstNameReg").val();
	var lastName = $("#lastNameReg").val();
	var email = $("#emailReg").val();
	var password1 = $("#password1Reg").val();
	var password2 = $("#password2Reg").val();
	console.log(firstName);
	console.log(lastName);
	console.log(email);
	console.log(password1); console.log(password2);

	if(firstName.length<1){
		alert("You must fill in the first name");
		return false;
	}
	if(firstName.length>20){
		alert("Maximum number of characters in the field 'first name' is 20.")
	}
	if(lastName.length<1){
		alert("You must fill in the last name");
		return false;
	}
	if(lastName.length>50){
		alert("Maximum number of characters in the field 'first name' is 50.");
		return false;
	}
	if(email.length<4 || email.length>40){
		alert("invalid mail");
		return false;
	}
	if(email.includes("@")==false ||  email.includes(".")==false){
		alert("invalid email. The format should be: example@example.ex")
	}

	if(password1 != password2){
		alert("Passwords doesn't match.");
		return false;
	}

	if(password1.length<8 || password1.length>40) {
		alert("The password must have at least 8 characters and max of 40 characters");
		return false;
	}



	var user = 
   {
   	"email": email,
	"username": firstName.concat(" ").concat(lastName),
	"password": password1,
	"roles":"USER"
	}


	console.log(JSON.stringify(user));

	$.ajax({
        type: "POST",
        data: JSON.stringify(user),
        //url: "https://jsonplaceholder.typicode.com/posts",
        url: "http://localhost:8080/user/register",
        contentType: 'application/json; charset=utf-8',
    }).done(function (response) {
    	console.log(response["error"]);
    	if(response["error"] == "User already exists."){
    		alert("This user already exists");
    	}else{
    		localStorage.setItem("currentUser", JSON.stringify(response));
        console.log("Done! ");
        location.href = "allIdeas.html";
    	}
		
    }).fail(function (response) {
    	alert("this email already exists in the system");
    });


}