const signUpBUtton = document.getElementById("signUp");
const signInBUtton = document.getElementById("signIn");
const container = document.getElementById("container");

// switch between login and signup

signUpBUtton.addEventListener("click", () => {
  container.classList.add("right-panel-active");
});

signInBUtton.addEventListener("click", () => [
  container.classList.remove("right-panel-active")
]);

async function createAccount(){
    username=document.getElementById("username").value
    name=document.getElementById("name").value
    surname=document.getElementById("surname").value
    email=document.getElementById("email").value
    password=document.getElementById("password").value
    pp=name[0]+surname[0]
    description="user"


    let formData = new FormData();
         formData.append('user_id',"");
         formData.append('username',username);
         formData.append('password',password);
         formData.append('email',email);
         formData.append('first_name',name);
         formData.append('last_name',surname);
         formData.append('profile_picture',pp);
         formData.append('description',description);
         formData.append('create_date',"");



    		// Create an FormData object
            console.log(formData)
    		// If you want to add an extra field for the FormData
   $.ajax({
       type: "POST",
       url: "http://localhost:8080/workflix-1.0/rest/user/register",
       data: formData,
       processData: false,
       contentType: false,
       cache: false,
        timeout: 600000,
     success: function(data, textStatus) {
       console.log(data)
    },
    error: function(data,errr){
    }
   });

}
async function Login(){
    email=document.getElementById("email-login").value
    password=document.getElementById("password-login").value
    var obj={
        "workspace_id": "",
        "creation_time": "2023-05-19",
        "template_id": 1,
        "workspace_name": "Workspace 4w"
    }
    $.ajax({

        url : 'http://localhost:8080/workflix-1.0/rest/workspace/create',
        type : 'POST',
        data:{obj},
        success : function(data) {
            console.log(data)
            console.log(JSON.stringify(data));
        },
        error : function(request,error)
        {
            alert("Request: "+JSON.stringify(request));
        }
    });
}