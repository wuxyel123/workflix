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

var currentBoard = boardsData[0];

// Fetch board data from backend API
function fetchBoards() {
  const workspaceId = 1;

  fetch(`/workspace/${workspaceId}/boards`, {
    method: "GET",
  })
      .then((response) => response.json())
      .then((data) => {
        // 把拿到的boards放在boardsData里，如果后端拿到data和最上面的示例不一样就需要处理成一样的格式
        boardsData = data;
        currentBoard = boardsData[0];
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
function drawSubBoards() {
  if (!currentBoard) {
    document.getElementById("subboards").innerHTML = "";
  }
  fetch(`/board/${currentBoard.id}/subboards`)
      .then((response) => response.json())
      .then((data) => {
        var elementList = [];
        currentBoard.subboard = data;
        currentBoard.subboard.forEach((subboards) => {
          elementList.push(`<div class="list">
              <div class="list-title">${subboards.title}</div>
              <div class="cards">
                ${drawCard(subboards.cards)}
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
  fetch(`/board/${boardId}/delete`, { method: "DELETE" })
      .then((response) => {
        if (response.ok) {
          fetchBoards();
        }
      })
      .catch((error) => console.log(error));
}

function add() {}

// Click to switch board
function handleClickBoard(e) {
  boardsData.forEach((item) => {
    item.id === e && (currentBoard = item);
  });
  drawSubBoards();
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
function drawCard(cards) {
  var elementCards = [];
  cards.forEach((card) => {
    elementCards.push(`<div class="card">${card}</div>`);
  });
  return elementCards.join("");
}
function drawAddCard(subboards, isAdding = false) {
  return ` <div class="add-card" 
                onclick="handleOpenCard('${subboards.title}')"
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
  fetch("/activity/create", {
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
  };

  fetch("/board/create", {
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
      boardId: currentBoard.id,
    };

    fetch("/subboard/create", {
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
