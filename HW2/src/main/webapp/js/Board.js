// Mocked JSON data with different s for each board
const boardsData = [
  {
    id: 1,
    name: "Board 1",
    workspace: "Workspace 1",
    visible: true,
    subboards: [
      { id: 1, name: "Subboard 1" },
      { id: 2, name: "Subboard 2" },
    ],
  },
];

//get workspace id
async function getBoardIdFromUrl() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get("id");
}

// Fetch board data from backend API
async function fetchBoards() {
  const workspaceId = localStorage.getItem("workspaceid");
  // const workspaceId = 1;
  //  currentBoard = await getBoardIdFromUrl();

  fetch(
    `http://localhost:8080/workflix-1.0/rest/workspace/${workspaceId}/boards`,
    {
      method: "GET",
    }
  )
    .then((response) => response.json())
    .then((data) => {
      drawSubBoards();
      const boardList = document.getElementById("boardList");
      boardList.innerHTML = "";

      data.forEach((board) => {
        const boardElement = document.createElement("li");
        boardElement.classList.add("board");
        boardElement.textContent = board.name;
        boardElement.dataset.id = board.id;

        if (board.visible) {
          boardElement.classList.add("visible");
        }
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.addEventListener("click", (event) => {
          event.stopPropagation(); // Prevent event propagation to the boardElement click event
          deleteBoard(board.id);
        });

        boardElement.appendChild(deleteButton);

        boardElement.textContent = board.name;
        boardElement.addEventListener("click", () =>
          handleClickBoard(board.id)
        );
        boardList.appendChild(boardElement);
      });
    })
    .catch((error) => console.log(error));
}

// Fetch subboards items of a specific board from backend API
async function drawSubBoards(e) {
  var urlParam = new URLSearchParams(window.location.search);
  currentBoard = e || urlParam.get("id");
  console.log(currentBoard);

  if (!currentBoard) {
    document.getElementById("subboards").innerHTML = "";
  }
  fetch(
    `http://localhost:8080/workflix-1.0/rest/board/${currentBoard}/subboards`
  )
    .then((response) => response.json())
    .then((data) => {
      var elementList = [];
      data.forEach((subboards) => {
        elementList.push(`<div class="list">
              <div class="list-title">${subboards.name} 
              <button type="button" class="btn btn-danger delete-button" onclick="deleteList(${subboards})">Delete</button>
              </div>
              <div class="cards">
                ${drawCard(currentBoard)}
              </div>
              ${drawAddCard(subboards)}
            </div>`);
      });
      $("#subboards").html(elementList);
    })
    .catch((error) => console.log(error));
}

// Initial board fetch on page load
fetchBoards();

// Delete a board using the backend API
function deleteBoard(boardId) {
  var r = confirm("Confirm delete board");
  if (r == true) {
    fetch(` http://localhost:8080/workflix-1.0/rest/board/${boardId}/delete`, {
      method: "DELETE",
    })
      .then((response) => {
        if (response.ok) {
          fetchBoards();
        }
      })
      .catch((error) => console.log(error));
  }
}

// Click to switch board
function handleClickBoard(e) {
  boardsData.forEach((item) => {
    item.id === e && (currentBoard = item);
  });
  drawSubBoards(e);
}

// Methods for drawing a single board
// function drawBoard() {
//   if (!currentBoard) {
//     document.getElementById("subboards").innerHTML = "";
//   }
//   var elementList = [];
//   currentBoard.subboard.forEach((subboards) => {
//     elementList.push(`<div class="list">
//           <div class="list-title">${subboards.title}</div>
//           <div class="cards">
//             ${drawCard(subboards.cards)}
//           </div>
//           ${drawAddCard(subboards)}
//         </div>`);
//   });
//   $("#subboards").html(elementList);
// }

//draw card
function drawCard(id) {
  fetch(`http://localhost:8080/workflix-1.0/rest/subboard/${id}/activities`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      if (data) {
        var cards = data;
        cards.forEach((card) => {
          elementCards.push(`<div class="card">${card}</div>`);
        });
      }
    })
    .catch((error) => console.log(error));
}

function drawAddCard(subboards, isAdding = false) {
  return ` <div class="add-card" 
                onclick="handleOpenCard('${subboards.name}')"
                data-toggle="modal"
                data-target="#cardModal"
              >Add a card</div>`;
}
var currentList = {};

// Configure parameters when opening the new card pop-up
function handleOpenCard(e) {
  currentBoard.subboards.forEach((subboards) => {
    subboards.title === e && (currentList = subboards);
  });
}

// create card
function handleAddCard() {
  const input = $("#cardTitle");
  if (!input) {
    return;
  }
  const requestData = {
    name: input.val(),
    subboard_id: currentSubboardId,
  };
  fetch("http://localhost:8080/workflix-1.0/rest/activity/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestData),
  })
    .then((response) => response.json())
    .then((data) => {
      currentList.cards.push(input.val());
      // Retrieve data and re-render the page after a successful request
      drawSubBoards();
    })
    .catch((error) => console.log(error));
}

// add board
function handleAddBoard() {
  const input = $("#formTitle");
  if (!input) {
    return;
  }
  const workspace = $("#formWorkspace");
  const visibility = $("#formVisibility");
  const requestData = {
    name: input.val(),
    workspace_id: workspace.val(),
    visibility: visibility.val(),
    description: "test",
    visibility: "PUBLIC",
  };

  fetch("http://localhost:8080/workflix-1.0/rest/board/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestData),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Board created:", data);
      drawSubBoards();
    })
    .catch((error) => console.log(error));
}

// create subboards
function handleAddList() {
  const input = $("#listTitle");
  if (input) {
    const requestData = {
      title: input.val(),
      boardId: currentBoard,
    };

    fetch("http://localhost:8080/workflix-1.0/rest/subboard/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("subboard created:", data);
        drawSubBoards();
      })
      .catch((error) => console.log(error));
    return;
  }
}

//delete subboards
function deleteList(subboardId) {
  var r = confirm("Confirm deletion of list");
  if (r == true) {
    fetch(
      `http://localhost:8080/workflix-1.0/rest/subboard/${subboardId}/delete`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name: itemName }),
      }
    )
      .then((response) => {
        if (response.ok) {
          drawSubBoards();
        } else {
          console.error("Failed to delete item:", response.status);
        }
      })
      .catch((error) => console.error("Error:", error));
  }
}
//Query the boards under the current workspace
$.ajax({
  url: "",
  async: false,
  complete: function () {
    drawSubBoards();
  },
});

$("#formWorkspace").append("<option>Workspace</option>");
$("#cardModal").on("show.bs.modal", function (event) {
  $("#cardTitle").val("");
});

$("#boardModal").on("show.bs.modal", function () {
  $("#formTitle").val("");
});

$("#listModal").on("show.bs.modal", function () {
  $("#listTitle").val("");
});
