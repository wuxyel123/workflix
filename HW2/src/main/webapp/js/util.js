
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
    console.log(subboard)
    subboard.appendChild(subboard_content)
})



data_workspace = [

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

]

data_workspace.forEach(item => {
    var subboard_content = document.createElement('div')
    subboard_content.innerHTML = `<div class="accordion-body" style="color: #6A2871" >${item.board_name}</div></a>`
    var subboard = document.getElementById('subboard-content')
    console.log(subboard)
    subboard.appendChild(subboard_content)
})
