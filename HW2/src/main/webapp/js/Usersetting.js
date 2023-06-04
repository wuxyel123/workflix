function validateEmail(email) {
  // for check email
  var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailPattern.test(email);
}

// check password
function validatePassword(password, confirmPassword) {
  return password === confirmPassword;
}

document.addEventListener("DOMContentLoaded", function () {
  var userForm = document.getElementById("userForm");
  var saveButton = document.getElementById("saveButton");

  // get user info
  getUserData();

  //  Listening for form submission events
  userForm.addEventListener("submit", function (event) {
    event.preventDefault();

    // Get form data
    var username = document.getElementById("username").value;
    var name = document.getElementById("name").value;
    var surname = document.getElementById("surname").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    // update user data
    updateUserData(username, name, surname, email, password);
  });

  function getUserData() {
    fetch("/user/getData")
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        //Populate the table with the obtained user data
        document.getElementById("username").value = data.username;
        document.getElementById("surname").value = data.first_name;
        document.getElementById("name").value = data.last_name;
        document.getElementById("email").value = data.email;
        document.getElementById("description").value = data.description;
      })
      .catch(function (error) {
        console.error("Error:", error);
      });
  }

  // update user info
  function updateUserData(username, name, surname, email, password) {
    var userData = {
      user_id: useId,
      username: username,
      last_name: name,
      first_name: surname,
      email: email,
      password: password,
      description: description,
    };

    // update requests
    fetch("/user/update/" + useId, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userData),
    })
      .then(function (response) {
        if (response.ok) {
          console.log("User data updated successfully");
        } else {
          console.error("Failed to update user data");
        }
      })
      .catch(function (error) {
        console.error("Error:", error);
      });
  }
});
