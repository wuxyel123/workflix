class Footer extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.innerHTML = `
        <footer class="text-center text-white" style="background-color: #6A2871;">
        <!-- Grid container -->
        <div class="container">
                <!-- Section: Links -->
                <section class="mt-5">
                        <!-- Grid row-->
                        <div class="row text-center d-flex justify-content-center pt-5">
                                <!-- Grid column -->
                                <div class="col-md-2">
                                        <h6 class="text-uppercase font-weight-bold">
                                                <a href="#!" class="text-white">About us</a>
                                        </h6>
                                </div>
                                <!-- Grid column -->

                                <!-- Grid column -->
                                <div class="col-md-2">
                                        <h6 class="text-uppercase font-weight-bold">
                                                <a href="#!" class="text-white">User Guide</a>
                                        </h6>
                                </div>
                                <!-- Grid column -->

                                <!-- Grid column -->
                                <div class="col-md-2">
                                        <h6 class="text-uppercase font-weight-bold">
                                                <a href="#!" class="text-white">Functions</a>
                                        </h6>
                                </div>
                                <!-- Grid column -->

                                <!-- Grid column -->
                                <div class="col-md-2">
                                        <h6 class="text-uppercase font-weight-bold">
                                                <a href="#!" class="text-white">Help</a>
                                        </h6>
                                </div>
                                <!-- Grid column -->

                                <!-- Grid column -->
                                <div class="col-md-2">
                                        <h6 class="text-uppercase font-weight-bold">
                                                <a href="#!" class="text-white">Contact</a>
                                        </h6>
                                </div>
                                <!-- Grid column -->
                        </div>
                        <!-- Grid row-->
                </section>
                <!-- Section: Links -->

                <hr class="my-3" />

                <!-- Section: Text -->
                <section class="mb-3">
                        <div class="row d-flex justify-content-center">
                                <div class="col-lg-8">
                                        <p>
                                            Wrokflix makes work more flexible.
                                        </p>
                                </div>
                        </div>
                </section>
                <!-- Section: Text -->

                <!-- Section: Social -->
                <section class="text-center mb-5">
                        <i class="bi bi-facebook"></i>
                        <i class="twitter"></i>
                        <i class="bi bi-google"></i>
                        <i class="bi bi-instagram"></i>
                        <i class="bi bi-linkedin"></i>
                        <i class="bi bi-github"></i>
                </section>
                <!-- Section: Social -->
        </div>
        <!-- Grid container -->
        <!-- Copyright -->
        <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.2)">
                © 2023 Copyright:
                <a class="text-white" href="https://www.unipd.it/en/">Workflix</a>
        </div>
    </footer>               
        `;
    }
}
customElements.define('footer-component', Footer);