var contextPath = 'http://127.0.0.1:8080/workflix-1.0';

function sanitize(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

document.addEventListener('DOMContentLoaded', function (event) {
    loadTemplate();
})


function loadTemplate() {
    var headerUrl = new URL(contextPath + '/src/main/webapp/html/Reusable-snippets/header.html');
    var sidebarUrl = new URL(contextPath + '/src/main/webapp/html/Reusable-snippets/sidebar.html');
    var footerUrl = new URL(contextPath + '/src/main/webapp/html/Reusable-snippets/footer.html');
    sendGenericGetRequest(headerUrl, loadHeader);
    sendGenericGetRequest(sidebarUrl, loadSidebar);
    sendGenericGetRequest(footerUrl, loadFooter);
}

function sendGenericGetRequest(url, callback) {

    var httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status == 200) {
                callback(httpRequest.responseText)
            }
            else {
                console.log(req.responseText);
                alert("problem processing the request");
            }
        }

    };

    httpRequest.open('GET', url);
    httpRequest.send();
}


function loadSidebar(data) {
    var loggedIn = sessionStorage.getItem("loggedIn");
    var userAuthorization = sessionStorage.getItem("userRole");

    document.getElementById("sidebar-area").innerHTML = data;
    // document.getElementById("template-link-button").setAttribute("href", contextPath + "/src/main/webapp/html/Components/template.html");
    // document.getElementById("analytics-link-button").setAttribute("href", contextPath + "/src/main/webapp/html/Components/analytics.html");

    var sidebarButton=document.getElementById("sidebar-toggle-button")
   
    sidebarButton.addEventListener('click', function() {
        var sideBar= document.getElementById("sidebar-area")
        console.log(sideBar.style.left)
        var currentLeft = parseInt(sideBar.getAttribute('data-left')) || 0;
        var newLeft = currentLeft === 0 ? -120 : 0; // Toggle between 0 and 200
        sideBar.style.left = newLeft + 'px';
        sideBar.setAttribute('data-left', newLeft);
      }
    
    )



    if (loggedIn) {
        if (userAuthorization == 'manager' || userAuthorization == 'user' || userAuthorization == 'editor') {
            list = document.getElementsByClassName("manager")
            for (var i = 0; i < list.length; i++) {
                list[i].classList.add('d-block');
                list[i].classList.remove('d-none')
            }
            if (userAuthorization == 'user' || userAuthorization == 'editor') {
                list = document.getElementsByClassName("editor")
                for (var i = 0; i < list.length; i++) {
                    list[i].classList.add('d-block');
                    list[i].classList.remove('d-none')
                }
                if (userAuthorization == 'user') {
                    list = document.getElementsByClassName("user")
                    for (var i = 0; i < list.length; i++) {
                        list[i].classList.add('d-block');
                        list[i].classList.remove('d-none')
                    }
                }
            }
        }
    } if (!loggedIn) {
        list = document.getElementsByClassName("manager")
        for (var i = 0; i < list.length; i++) {
            list[i].classList.add('d-none');
            list[i].classList.remove('d-block')
        }

        list = document.getElementsByClassName("user")
        for (var i = 0; i < list.length; i++) {
            list[i].classList.add('d-none');
            list[i].classList.remove('d-block')
        }

        list = document.getElementsByClassName("editor")
        for (var i = 0; i < list.length; i++) {
            list[i].classList.add('d-none');
            list[i].classList.remove('d-block')
        }
    }

    // document.getElementById("drop-down-manager-menu").addEventListener('click', function (event) {

    //     if (document.getElementById("icon-manager-menu").classList.contains("fa-angle-down")) {
    //         document.getElementById('icon-manager-menu').classList.add('fa-angle-up');
    //         document.getElementById('icon-manager-menu').classList.remove('fa-angle-down');
    //     } else {
    //         document.getElementById('icon-manager-menu').classList.remove('fa-angle-up');
    //         document.getElementById('icon-manager-menu').classList.add('fa-angle-down');
    //     }
    // });
}

function loadHeader(data) {
    var loggedIn = sessionStorage.getItem("loggedIn");
    var userEmail = sessionStorage.getItem("userEmail");

    // document.getElementById("header-area").innerHTML = data;
    // document.getElementById("logo-button").setAttribute("href", contextPath + "/src/main/webapp/html/LandingPage.html");
    // document.getElementById("logout-button").setAttribute("href", contextPath + "/user/logout/?operation=logout");
    // document.getElementById("login-button").setAttribute("href", contextPath + "/jsp/login.jsp");
    // document.getElementById("register-button").setAttribute("href", contextPath + "/jsp/register.jsp");


    if (loggedIn) {

        document.getElementById("user-email").innerHTML = userEmail;

        list = document.getElementsByClassName("unlogged")
        for (var i = 0; i < list.length; i++) {
            list[i].setAttribute("class", 'unlogged d-none');
        }

        list = document.getElementsByClassName("logged");
        for (var i = 0; i < list.length; i++) {
            list[i].setAttribute("class", 'logged d-block');
        }
    }
    if (!loggedIn) {

        list = document.getElementsByClassName("logged");
        for (var i = 0; i < list.length; i++) {
            list[i].setAttribute("class", 'logged d-none');
        }

        list = document.getElementsByClassName("unlogged")
        for (var i = 0; i < list.length; i++) {
            list[i].setAttribute("class", 'unlogged d-block');
        }
    }
}

function loadFooter(data) {
    document.getElementById("footer-area").innerHTML = data;
}