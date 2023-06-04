$(document).ready(function () {
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
