// Mocked JSON data with different subboards for each board
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

const urlParams = new URLSearchParams(window.location.search);
let currentBoard = urlParams.get("id") || 1;

console.log(currentBoard);

// Fetch board data from backend API
async function fetchBoards() {
  const workspaceId = localStorage.getItem("workspaceid");

  try {
    const response = await fetch(`http://localhost:8080/workflix-1.0/rest/workspace/${workspaceId}/boards`, {
      method: "GET",
    });

    const data = await response.json();

    drawSubBoards();
    const boardList = document.getElementById("boardList");
    boardList.innerHTML = "";

    for (const board of data) {
      const boardElement = createBoardElement(board);
      boardList.appendChild(boardElement);
    }
  } catch (error) {
    console.log(error);
  }
}

function createBoardElement(board) {
  const boardElement = document.createElement("li");
  boardElement.classList.add("board");
  boardElement.dataset.id = board.board_id;

  if (board.visible) {
    boardElement.classList.add("visible");
  }

  const container = document.createElement("div");
  container.classList.add("board-container");

  const deleteButton = document.createElement("button");
  deleteButton.classList.add("btn", "btn-danger", "delete-button");
  deleteButton.addEventListener("click", (event) => {
    event.stopPropagation();
    deleteBoard(board.board_id);
  });
  deleteButton.textContent = "Delete";

  container.appendChild(deleteButton);

  const boardName = document.createElement("span");
  boardName.textContent = board.name;

  container.appendChild(boardName);

  boardElement.appendChild(container);

  boardElement.addEventListener("click", () => {
    handleClickBoard(board.board_id);
    drawSubBoards(board.board_id);
  });

  return boardElement;
}

// Fetch subboard items of a specific board from backend API
async function drawSubBoards(e) {
  const urlParams = new URLSearchParams(window.location.search);
  currentBoard = e || urlParams.get("id");
  console.log(currentBoard);

  if (!currentBoard) {
    document.getElementById("subboards").innerHTML = "";
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/workflix-1.0/rest/board/${currentBoard}/subboards`);
    const data = await response.json();

    const elementList = data.map((subboard) => {
      return `<div class="list">
                <div class="list-title">${subboard.name} 
                  <button type="button" class="btn btn-danger delete-button" onclick="deleteList(${subboard.id})">Delete</button>
                </div>
                <div class="cards">
                  ${drawCard(subboard.id)}
                </div>
                ${drawAddCard(subboard)}
              </div>`;
    });

    $("#subboards").html(elementList.join(""));
  } catch (error) {
    console.log(error);
  }
}

// Fetch and draw cards for a subboard
async function drawCard(subboardId) {
  try {
    const response = await fetch(`http://localhost:8080/workflix-1.0/rest/subboard/${subboardId}/activities`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();
    console.log(data);

    if (Array.isArray(data)) {
      const elementCards = data.map((card) => `<div class="card">${card}</div>`);
      return elementCards.join("");
    }
  } catch (error) {
    console.log(error);
  }

  return "";
}

function drawAddCard(subboard, isAdding = false) {
  return `<div class="add-card" 
              onclick="handleOpenCard('${subboard.name}')"
              data-toggle="modal"
              data-target="#cardModal"
            >Add a card</div>`;
}

let currentList = {};

// Configure parameters when opening the new card pop-up
function handleOpenCard(subboardName) {
  currentBoard.subboards.forEach((subboard) => {
    if (subboard.name === subboardName) {
      currentList = subboard;
    }
  });
}

// Create a card
async function handleAddCard() {
  const input = $("#cardTitle");

  if (!input) {
    return;
  }

  const requestData = {
    name: input.val(),
    subboard_id: currentList.id,
  };

  try {
    const response = await fetch("http://localhost:8080/workflix-1.0/rest/activity/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    const data = await response.json();
    currentList.cards.push(input.val());
    drawSubBoards();
  } catch (error) {
    console.log(error);
  }
}

// Add a board
async function handleAddBoard() {
  const input = $("#formTitle");

  if (!input) {
    return;
  }

  const workspace = $("#formWorkspace");
  const visibility = $("#formVisibility");

  const requestData = {
    name: input.val(),
    workspace_id: workspace.val(),
    description: "test",
    visibility: "PUBLIC",
  };

  try {
    const response = await fetch("http://localhost:8080/workflix-1.0/rest/board/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    const data = await response.json();
    console.log("Board created:", data);
    drawSubBoards();
  } catch (error) {
    console.log(error);
  }
}

// Create a subboard
async function handleAddList() {
  const input = $("#listTitle");

  if (!input) {
    return;
  }

  const requestData = {
    title: input.val(),
    boardId: currentBoard,
  };

  try {
    const response = await fetch("http://localhost:8080/workflix-1.0/rest/subboard/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    const data = await response.json();
    console.log("Subboard created:", data);
    drawSubBoards();
  } catch (error) {
    console.log(error);
  }
}

// Delete a subboard
function deleteList(subboardId) {
  var r = confirm("Confirm deletion of list");
  if (r == true) {
    fetch(`http://localhost:8080/workflix-1.0/rest/subboard/${
