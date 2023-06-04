/** 页面board信息渲染存储的对象
 * name: board's name
 * lists: board's list
 * lists-title:list label
 * lists-cards:list's card list
 * 我看接口中还要传workspace的id和Visibility两个字段，这两个字段不影响页面的渲染，在新增board的时候需要传入，我就都还没加上
 * 每次新增完board/list/card之后重新调用drawBoards()方法渲染就行，编辑和删除功能没来得及加
 */

var boards = [
  {
    name: "test",
    lists: [
      { title: "title1", cards: ["card1"] },
      { title: "title2", cards: ["card1", "card2", "card3", "card4"] },
      { title: "title3", cards: ["card1", "card2", "card3"] },
    ],
  },
  {
    name: "test1",
    lists: [{ title: "title1", cards: ["card1", "card2", "card3", "card4"] }],
  },
];

var currentBoard = boards[0];

function add() {}

// 点击侧边栏board的方法，切换board
function handleClickBoard(e) {
  boards.forEach((item) => {
    item.name === e && (currentBoard = item);
  });
  drawBoards();
}
// 绘制board列表的方法，可以在每次修改完board参数之后调用该方法重新渲染页面
function drawBoards() {
  var elementArray = [];
  boards.forEach((item) => {
    elementArray.push(
      `<div class="menu-item ${
        currentBoard.name === item.name ? "menu-item--active" : ""
      }" onclick="handleClickBoard('${item.name}')">
        <div style="width: 80%; text-align: left" >${item.name}</div>
      </div>`
    );
  });
  $("#boards").html(elementArray);
  drawBoard();
}
// 绘制单个board的方法
function drawBoard() {
  if (!currentBoard) {
    document.getElementById("lists").innerHTML = "";
  }
  var elementList = [];
  currentBoard.lists.forEach((list) => {
    elementList.push(`<div class="list">
        <div class="list-title">${list.title}</div>
        <div class="cards">
          ${drawCard(list.cards)}
        </div>
        ${drawAddCard(list)}
      </div>`);
  });
  $("#lists").html(elementList);
}
// 绘制card的方法
function drawCard(cards) {
  var elementCards = [];
  cards.forEach((card) => {
    elementCards.push(`<div class="card">${card}</div>`);
  });
  return elementCards.join("");
}
function drawAddCard(list, isAdding = false) {
  return ` <div class="add-card" 
                onclick="handleOpenCard('${list.title}')"
                data-toggle="modal"
                data-target="#cardModal"
              >Add a card</div>`;
}
var currentList = {};
// 打开新增card弹窗时配置参数
function handleOpenCard(e) {
  currentBoard.lists.forEach((list) => {
    list.title === e && (currentList = list);
  });
}
// 新增card方法
function handleAddCard() {
  const input = $("#cardTitle");
  if (!input) {
    return;
  }
  // 这里应该调后端新增card接口然后重新绘制页面
  currentList.cards.push(input.val());
  drawBoards();
}
// 新增board方法
function handleAddBoard() {
  const input = $("#formTitle");
  if (!input) {
    return;
  }
  // 对应的所属空间和可见性两个参数，需要传到后端接口里
  const workspace = $("#formWorkspace");
  const visibility = $("#formVisibility");
  // 这里应该调后端新增board接口然后重新绘制页面
  boards.push({
    name: input.val(),
    lists: [],
  });
  drawBoards();
}
// 新增list方法
function handleAddList() {
  const input = $("#listTitle");
  if (!input) {
    return;
  }
  // 这里应该调后端新增list接口然后重新绘制页面
  currentBoard.lists.push({
    title: input.val(),
    cards: [],
  });
  drawBoards();
}
// 查询当前workspace下boards
$.ajax({
  url: "",
  async: false,
  complete: function () {
    drawBoards();
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
