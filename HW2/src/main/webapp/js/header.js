$('ul.nav li.dropdown').hover(function() {
    $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn(500);
    }, function() {
    $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut(500);
});
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
            background-image: url(./logo.png);
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
            width: 100%;
          }
          span.user-settings {
            float: right;
            position: relative;
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
        <header>
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fa-brands fa-microblog"></i> 
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" style="color: black;" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Workspaces <i class="fa-solid fa-magnifying-glass"></i>
                    </a>
                    <ul class="dropdown-menu">
                    <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-user-group"></i> Friends Updates</a>
                    </li>
                    <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-calendar"></i> Events near you</a>
                    </li>
                    <li>
                        <hr class="dropdown-divider" />
                    </li>
                    <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-ticket"></i> Buy a Ticket</a>
                    </li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" style="color: black;" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Templates <i class="fa-solid fa-magnifying-glass"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-user-group"></i> Friends Updates</a>
                        </li>
                        <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-calendar"></i> Events near you</a>
                        </li>
                        <li>
                        <hr class="dropdown-divider" />
                        </li>
                        <li>
                        <a class="dropdown-item" style="color: black;" href="#"><i class="fa-solid fa-ticket"></i> Buy a Ticket</a>
                        </li>
                    </ul>
                    </li>
                </ul>
                <div class="settings">
                    <span class="user-settings">EE</span>
                </div>
            </div>
            </div>
        </nav>

        </header>
        `;
      }
}
customElements.define('header-component', Header);