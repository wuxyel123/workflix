$(document).ready(function () {
  var user=localStorage.getItem("ProfilePicture")
  console.log(user)
  document.getElementById("loggedInUser").innerHTML=user.toUpperCase()
  $("ul.nav li.dropdown").hover(
    function () {
      $(this).find(".dropdown-menu").stop(true, true).delay(200).fadeIn(500);
    },
    function () {
      $(this).find(".dropdown-menu").stop(true, true).delay(200).fadeOut(500);
    }
  );
  $("#createDropdown").on("show.bs.modal", function () {
    $("#formTitle").val("");
  });

  // log out
  $("#logoutButton").on("click", function () {
    $("#logoutModal").modal("show");
  });

  $("#cancelLogoutButton").on("click", function () {
    $("#logoutModal").modal("hide");
  });

  $("#confirmLogoutButton").on("click", function () {
    localStorage.removeItem("loggedInUser");
    window.location.href = "login.html";
  });
});
function openMenu() {
  var statu=document.getElementById("workspaces-dropdown").style.display
  console.log(statu)
  if(statu=="block"){
    document.getElementById("workspaces-dropdown").style.display= "none";
  }else{
    document.getElementById("workspaces-dropdown").style.display= "block";
  }
}
 function ChangeWorkSpace(id){
    location.href="/workflix-1.0/html/workspace.html"
    localStorage.setItem("workspaceid","1")
}
class Header extends HTMLElement {
  constructor() {
    super();
}

  connectedCallback() {
    this.innerHTML = `
        <style>
          header{
            position: absolute;
            z-index: 999999;
            width: 100%;
            background: #ffff;
          }
          nav.navbar.navbar-expand-lg.bg-body-tertiary {
            background: #4e4c4c05;
            border: 1px solid #8080806b;
            padding: 1%;
          }
          .navbar-brand {
            background-image: url(../resources/logo/logo.png);
            width: 100px;
            height: 50px;
            background-position: center;
            background-size: contain;
          }
          ul.navbar-nav.me-auto.mb-2.mb-lg-0 {
            margin-left: 5%;
            width: 50%;
          }
          .settings {
            width: 50%;
          }
          .set{
            color:black;
            float:right;
            margin:2px;
            margin-right:10px;
            text-align: center;
          }
          span.user-settings {
            margin-right: 2%;
            background: #80808033;
            /* padding: 1%; */
            border-radius: 100pc;
            width: 35px;
            height: 35px;
            text-align: center;
            padding-top: .50%;
            color: #0b0b43;
            cursor: pointer;
          }
        </style>
        
        
        <head>
          <meta charset="UTF-8">
          <meta name="author" content="Esra Erdim"> <!-- who wrote the page -->
          <meta name="description" content="header of site">
          <meta name="keywords" content="HTML, header">
          <meta name="viewport" content="width=device-width, initial-scale=1">
          <!-- Css link-->
          <link href="/workflix-1.0/css/index.css" rel="stylesheet" type="text/css" media="screen, projection" />
          <!-- Latest compiled and minified CSS -->
          <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        
          <!-- Latest compiled and minified JavaScript -->
          <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        
          <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
          <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        
          <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2"
            crossorigin="anonymous"></script>
          <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.5/dist/umd/popper.min.js"
            integrity="sha384-Xe+8cL9oJa6tN/veChSP7q+mnSPaj5Bcu9mPX5F5xIGE0DVittaqT5lorf0EI7Vk"
            crossorigin="anonymous"></script>
          <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.min.js"
            integrity="sha384-kjU+l4N0Yf4ZOJErLsIcvOU2qSb74wXpOhqTvwVx3OElZRweTnQ6d31fXEoRD1Jy"
            crossorigin="anonymous"></script>
        </head>
        <header>
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
            <a class="navbar-brand" href="./template.html">
                <i class="fa-brands fa-microblog"></i> 
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown" onclick="openMenu()">
                    <a class="nav-link dropdown-toggle" style="color: black;" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Workspaces <i class="fa-solid fa-magnifying-glass"></i>
                    </a>
                    <ul class="dropdown-menu" id="workspaces-dropdown">
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" style="color: black;" href="./template.html" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Templates <i class="fa-solid fa-magnifying-glass"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                        <a class="dropdown-item" style="color: black;" href="./template.html"><i class="fa-solid fa-user-group"></i> Template Views</a>
                        </li>
                        <li>
                        <a class="dropdown-item" style="color: black;" href="./template.html"><i class="fa-solid fa-calendar"></i> Template Views</a>
                        </li>
                        <li>
                        <hr class="dropdown-divider" />
                        </li>
                        <li>
                        <a class="dropdown-item" style="color: black;" href="./template.html"><i class="fa-solid fa-ticket"></i> Template Views</a>
                        </li>
                    </ul>
                    </li>
                    <li class="nav-item dropdown">
                      <!-- <a class="nav-link dropdown-toggle" style="color: black;" href="#" role="button" data-bs-toggle="dropdown"
                        aria-expanded="false">
                        Create <i class="fa-solid fa-magnifying-glass"></i>
                      </a> -->

                      <button
                      type="submit"
                      class="btn btn-default create-btn"
                      data-toggle="dropdown"
                      data-target="#boardModal"
                    >
                      Create +
                    </button>

                      <ul class="dropdown-menu">
                        <li>
                          <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-user-group"></i> 
                            Create board</a>
                        </li>
                        <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-user-group"></i>
                          Start with a  template</a>
                         </li>
                         <li>
                         <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-user-group"></i>
                           Create workspace</a>
                          </li>
                      </ul>
                    </li>
                </ul>
                <div class="settings">
                    <a href=""class ="set">setting</a>
                    <a href="" class ="set">log out</a>
                    <span class="user-settings" id="loggedInUser">EE</span> 
                </div>
            </div>
            </div>


        </nav>
        </header>
        `;
  }
}
customElements.define("header-component", Header);
