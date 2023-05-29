var contextPath = 'http://127.0.0.1:8080/workflix-1.0/rest';


function sanitize(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}


$(document).ready(loadTemplate());

function loadTemplate(){

    var loggedIn = sessionStorage.getItem("loggedIn");
    var userEmail = sessionStorage.getItem("userEmail");
    var userAuthorization = sessionStorage.getItem("userRole");

    definizioneRichiesta = {
        url: new URL(contextPath+'/html/reusable-snippets/navbar.html'),
        method: 'GET',
        success: function(data){

            $("#navbar-area").html(data);
            $("#logo-button").attr("href", contextPath+"/jsp/homepage.jsp");
            $("#logout-button").attr("href", contextPath+"/user/logout/");
            $("#login-button").attr("href", contextPath+"/jsp/login.jsp");
            $("#register-button").attr("href", contextPath+"/jsp/register.jsp");


            if(loggedIn){
                $('.logged').addClass('d-block').removeClass('d-none');
                $('.unlogged').addClass('d-none').removeClass('d-block');
                $("#user-email").html(userEmail);
            }
            if(!loggedIn){
                $('.logged').addClass('d-none').removeClass('d-block');
                $('.unlogged').addClass('d-block').removeClass('d-none');
            }
        },
        fail: function(data){
            console.log(data);
            alert("problem processing the request");
        }
    }
    //load navbar
    $.ajax(definizioneRichiesta);


    //load sidebar
    $.ajax({
        url: new URL(contextPath+'/html/Components/sidebar.html'),
        method: 'GET',
        success: function(data){
            $("#sidebar-area").html(data);
            $("#template-link-button").attr("href", contextPath+"/html/template.html");
            if(loggedIn){
                if (userAuthorization=='manager' || userAuthorization=='editor' || userAuthorization=='user'){
                    $('.user').addClass('d-block').removeClass('d-none');
                    if (userAuthorization=='editor' || userAuthorization=='manager'){
                        $('.editor').addClass('d-block').removeClass('d-none');
                        if (userAuthorization =='manager'){
                            $('.manager').addClass('d-block').removeClass('d-none');
                        }
                    }
                }
            } if(!loggedIn) {
                $('.manager').addClass('d-none').removeClass('d-block');
                $('.editor').addClass('d-none').removeClass('d-block');
                $('.user').addClass('d-none').removeClass('d-block');
            }

            $("#drop-down-manager-menu").on("click", function(){

                if ($("#icon-manager-menu").hasClass("fa-angle-down")){
                    $('#icon-manager-menu').addClass('fa-angle-up').removeClass('fa-angle-down');
                } else{
                    $('#icon-manager-menu').addClass('fa-angle-down').removeClass('fa-angle-up');

                }
            });

        },
        fail: function(data){
            console.log(data);
            alert("problem processing the request");
        }
    });


    //load footer
    $.ajax({
        url: new URL(contextPath+'/html/Components/footer.html'),
        method: 'GET',
        success: function(data){
                $("#footer-area").html(data);
            },
        fail: function(data){
              console.log(data);
              alert("problem processing the request");
          }
    });

}

