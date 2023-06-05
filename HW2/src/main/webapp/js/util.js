
var data_users = [
    {
        user_id: 1,
        username: "John",
        workspace_id: 1,
        workspace_name: "Workspace 1",
        board: [
            {
                board_name: "board A",
                board_id: 1
            }, {
                board_name: "board B",
                board_id: 2
            }
            , {
                board_name: "board C",
                board_id: 3
            }
        ],
    },
    {
        user_id: 1,
        username: "John",
        workspace_id: 2,
        workspace_name: "Workspace 2",
        board: [
            {
                board_name: "board 2A",
                board_id: 21
            }, {
                board_name: "board 2B",
                board_id: 22
            }
            , {
                board_name: "board 2C",
                board_id: 23
            }
        ],
    }
];

data_users.forEach(item => {
    var subboard_content = document.createElement('div')
    subboard_content.innerHTML = `<div class="accordion-body" style="color: #6A2871" >${item.workspace_name}</div></a>`
    var subboard = document.getElementById('subtemplate-content')
    subboard.appendChild(subboard_content)
})



data_workspace = [
    {
        "workspace_id": 1,
        "visibility": "public",
        "create_time": "2023-06-05",
        "board_id": 4,
        "name": "Board 1",
        "description": "Board 1 description"
    },
    {
        "workspace_id": 1,
        "visibility": "private",
        "create_time": "2023-06-05",
        "board_id": 5,
        "name": "Board 2",
        "description": "Board 2 description"
    },
    {
        "workspace_id": 1,
        "visibility": "public",
        "create_time": "2023-06-05",
        "board_id": 6,
        "name": "Board 1",
        "description": "Board 1 description"
    },
    {
        "workspace_id": 1,
        "visibility": "private",
        "create_time": "2023-06-05",
        "board_id": 7,
        "name": "Board 2",
        "description": "Board 2 description"
    }
]


var workspaceId = 1; 
var apiUrl = 'http://localhost:8080/workflix-1.0/rest/workspace/' + workspaceId + '/boards';

$.ajax({
  url: apiUrl,
  method: 'GET',
  success: function(response) {
    console.log(response);  
    response.forEach(item => {
      var subboard_content = document.createElement('div');
      subboard_content.innerHTML = `<div class="accordion-body" style="color: #6A2871">${item.name}</div>`;
      var subboard = document.getElementById('subboard-content');
      subboard.appendChild(subboard_content);
    });
  },
  error: function(xhr, status, error) {
    console.log(error); 
  }
});



// data_workspace.forEach(item => {
//     var subboard_content = document.createElement('div')
//     subboard_content.innerHTML = `<div class="accordion-body" style="color: #6A2871" >${item.board_name}</div></a>`
//     var subboard = document.getElementById('subboard-content')
//     subboard.appendChild(subboard_content)
// })
