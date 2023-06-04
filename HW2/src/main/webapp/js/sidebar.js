class Sidebar extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.innerHTML = `
        <style>
        .sidebar-button{
            position: fixed;
            right: 10px;
            top:80px;
            height:200px;
          }
        </style>
        <sidebar>
            <div class="sidebar-button" >
                <button class="btn float-end btn-lg" data-bs-toggle="offcanvas" data-bs-target="#offcanvas" role="button">
                    <i class="bi-arrow-right-square-fill " data-bs-toggle="offcanvas"
                        data-bs-target="#offcanvas"></i></button>
            </div>
            <div class="offcanvas offcanvas-start" tabindex="-1" id="offcanvas" data-bs-keyboard="false"
                data-bs-backdrop="false">
                <div class="offcanvas-header">
                    <h2 class="offcanvas-title d-none d-sm-block" id="offcanvas">Menu</h2>
                    <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div class="offcanvas-body px-0"  >
                    <ul class="nav nav-pills flex-column mb-sm-auto mb-0 align-items-start" id="menu">
                        <li class="nav-item" >
                            <a href="#" class="nav-link text-truncate"  style="color: #6A2871">
                                <i class="fs-5 bi-house"></i><span class="ms-1 d-none d-sm-inline" >Home</span>
                            </a>
                        </li>
                        <li>
                            <a href="#" data-bs-toggle="collapse" class="nav-link text-truncate" style="color: #6A2871">
                                <i class="fs-5 bi-easel-fill"></i><span class="ms-1 d-none d-sm-inline">Template</span> </a>
                        </li>
                        <li>
                            <a href="#" class="nav-link text-truncate" style="color: #6A2871">
                                <i class="fs-5 bi-table"></i><span class="ms-1 d-none d-sm-inline">Analytics</span></a>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="nav-link dropdown-toggle  text-truncate" id="dropdown" data-bs-toggle="dropdown"
                                aria-expanded="false" style="color: #6A2871">
                                <i class="fs-5 bi-person"></i><span class="ms-1 d-none d-sm-inline">User</span>
                            </a>
                            <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdown">
                                <li><a class="dropdown-item" href="#">Settings</a></li>
                                <li><a class="dropdown-item" href="#">Profile</a></li>
                                <li>
                                    <hr class="dropdown-divider">
                                </li>
                                <li><a class="dropdown-item" href="#">Sign out</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </sidebar>
        `;
    }
}
customElements.define('sidebar-component', Sidebar);