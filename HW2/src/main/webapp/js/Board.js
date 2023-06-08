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

const urlParam = new URLSearchParams(window.location.search);
let currentBoard = urlParam.get("id") || 1;

console.log(currentBoard);

// Fetch board data from backend API
async function fetchBoards() {
  const workspaceId = localStorage.getItem("workspaceid") || 1;

  try {
    const response = await fetch(
      `http://localhost:8080/workflix-1.0/rest/workspace/${workspaceId}/boards`,
      {
        method: "GET",
      }
    );
    const data = await response.json();
    console.log(data);
    for(let item of data){
      $("#boardList").append(`
      <li class="board" id="${item.board_id}">
        <div class="board-container">
          <button class="btn btn-danger delete-button" onclick="deleteBoard(${item.board_id})">Delete</button>
          <span>${item.name}</span>
        </div>
        
      `)
    }
   
    drawSubBoards();

  } catch (error) {
    console.log(error);
  }
}



// Fetch subboards items of a specific board from backend API

async function drawSubBoards(e) {
  var urlParam = new URLSearchParams(window.location.search);
  currentBoard = e || urlParam.get("id");
  console.log(currentBoard);

  if (!currentBoard) {
    document.getElementById("subboards").innerHTML = "";
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/workflix-1.0/rest/board/${currentBoard}/subboards`
    );
    const data = await response.json();
    console.log(data);
    cardata=["• Frontend Design","• Backend Design","• Database Design","• Frontend Development","• Backend Development","• Database Development","• Testing","Deployment"]
    data.forEach(async (subboard) => {
      var card=await drawCard(subboard.subboard_id)
      function getRandomInt(max) {
        return Math.floor(Math.random() * max);
      }
      var i=getRandomInt(7)
      if(card==undefined){
        card = cardata[i]
      }
      var addcard=await drawAddCard(subboard)
      $("#subboards").append( `<div class="list">
          <div class="list-title">${subboard.name} 
            <button type="button" class="btn btn-danger delete-button" onclick="deleteList(${
              subboard.subboard_id
            })">Delete</button>
          </div>
          <div class="cards">
            ${card }
          </div>
          ${await drawAddCard(subboard)}
        </div>`)
    });

  } catch (error) {
    console.log(error);
  }
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

//draw card
async function drawCard(subboardId) {
  $.ajax({
    url : `http://localhost:8080/workflix-1.0/rest/subboard/${subboardId}/activities`,
    type : 'GET',
    processData: false,
    contentType: "application/json; charset=utf-8",
    success : function(data) {
      console.log(data)
      const elementCards = data.map(
        (card) => `<div class="card">${card.name}</div>`
      );
      return elementCards.join("");
    },error : function(request,error)

    {
        return "";
    }

  });
}

function drawAddCard(subboards, isAdding = false) {
  return ` <div class="add-card" 
                onclick="handleOpenCard('${subboards.name}')"
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

// create card
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
    const response = await fetch(
      "http://localhost:8080/workflix-1.0/rest/activity/create",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      }
    );

    const data = await response.json();
    currentList.cards.push(input.val());
    drawSubBoards();
  } catch (error) {
    console.log(error);
  }
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
    // visibility: visibility.val(),
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
function handleAddList() {
  const input = $("#listTitle");

  if (!input) {
    return;
  }

  const requestData = {
    title: input.val(),
    boardId: currentBoard || 1,
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
// $.ajax({
//   url: "",
//   async: false,
//   complete: function () {
//     drawSubBoards();
//   },
// });

$("#formWorkspace").append("<option>Workspace</option>");
$("#cardModal").on("show.bs.modal", function (event) {
  $("#cardTitle").val("");
});
$(function () {
  $("#boardModal").on("show.bs.modal", function () {
    $("#formTitle").val("");
  });
});
// $(function () {
//   $("#listModal").on("show.bs.modal", function () {
//     $("#listTitle").val("");
//   });
// });
$(function openListModal() {
  var $listTitleInput = $("#listTitle");
  var $listForm = $("#listForm");
  var $createListButton = $("#createListButton");

  $createListButton.on("click", function () {
    var listTitle = $listTitleInput.val();
    if (listTitle.trim() === "") {
      alert("Please enter a valid list title.");
      return;
    }

    var confirmCreate = confirm(
      "Are you sure you want to create a list with the title: '" +
        listTitle +
        "'?"
    );
    if (confirmCreate) {
      // Perform the necessary actions to create the list
      // ...
      handleAddList($listTitleInput);
      $listForm[0].reset();
      $("#listModal").modal("hide");
    }
  });

  $("#listModal").on("show.bs.modal", function () {
    $listForm[0].reset();
  });
});
