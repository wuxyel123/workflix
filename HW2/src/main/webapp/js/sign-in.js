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

    var obj={
        "user_id":"",
        "username":username,
        "password":password,
        "email":email,
        "first_name":name,
        "last_name":surname,
        "profile_picture":pp,
        "description":description,
        "create_date":""
    }

    toast = document.querySelector(".toast")
    closeIcon = document.querySelector(".close"),
    progress = document.querySelector(".progress");


    let timer1, timer2;
    toast.classList.add("active");
    progress.classList.add("active");

    timer1 = setTimeout(() => {
        toast.classList.remove("active");
    }, 5000); //1s = 1000 milliseconds

    timer2 = setTimeout(() => {
      progress.classList.remove("active");
    }, 5300);

    closeIcon.addEventListener("click", () => {
        toast.classList.remove("active");

        setTimeout(() => {
          progress.classList.remove("active");
        }, 300);

        clearTimeout(timer1);
        clearTimeout(timer2);
    });
    container.classList.remove("right-panel-active")
    localStorage.setItem("ProfilePicture", pp);

   $.ajax({
      url : 'http://localhost:8080/workflix-1.0/rest/user/register',
              type : 'POST',
              data: JSON.stringify(obj),
              processData: false,
              contentType: "application/json; charset=utf-8",
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
async function Login(){
    email=document.getElementById("email-login").value
    password=document.getElementById("password-login").value
    var obj={
        "user_id":"",
        "username":"",
        "password":password,
        "email":email,
        "first_name":"",
        "last_name":"",
        "profile_picture":"",
        "description":"",
        "create_date":""
    }
    $.ajax({
        url : 'http://localhost:8080/workflix-1.0/rest/user/login',
        type : 'POST',
        data: JSON.stringify(obj),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success : function(data) {
            console.log(data)
            console.log(JSON.stringify(data));
            var obj={
                "user_id":"",
                "username":"",
                "password":"",
                "email":email,
                "first_name":"",
                "last_name":"",
                "profile_picture":"",
                "description":"",
                "create_date":""
            }
            $.ajax({
                url : 'http://localhost:8080/workflix-1.0/rest/user/getbyemail',
                type : 'GET',
                data: JSON.stringify(obj),
                processData: false,
                contentType: "application/json; charset=utf-8",
                success : function(data) {
                    console.log(data)
                    localStorage.setItem("userid",data.user_id)
                    localStorage.setItem("ProfilePicture",data.profile_picture)
                    location.href = "/html/workspace.html";
                },
                error : function(request,error)
                {
                    alert("Request: "+JSON.stringify(request));
                }
            })
        },
        error : function(request,error)
        {
            alert("Request: "+JSON.stringify(request));
        }
    });
}