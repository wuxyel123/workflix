
$( document ).ready(function() {
    var userid=localStorage.getItem("userid")
    var workspace=localStorage.getItem("workspaceid")
    if(workspace==null){
        $.ajax({
                url : 'http://localhost:8080/workflix-1.0/rest/user/'+userid+'/workspaces',
                type : 'GET',
                processData: false,
                contentType: "application/json; charset=utf-8",
                success : function(data) {
                    console.log(data)
                    for(let item of data){
                        $("#workspaces-dropdown").append(`
                            <li onclick="ChangeWorkSpace('${item.workspace_id}')">
                            <a class="dropdown-item" style="color: black;" href="#" ><i class="fa-solid fa-user-group"></i> ${item.workspace_name}</a>
                            </li>

                        `)
                    }
                    workspace=data[0].workspace_id
                    $.ajax({
                        url:"http://localhost:8080/workflix-1.0/rest/workspace/"+workspace.toString()+"/boards",
                        type : 'GET',
                        processData: false,
                        contentType: "application/json; charset=utf-8",
                        success : function(data) {
                            document.getElementById("create-newboard").style.display="none"
                            for(let item of data ){
                                 $("#slider-wrapper").append(`
                                        <div class="news-slider__item swiper-slide">
                                            <a href="/html/boards.html?id=${item.board_id}" class="news__item">
                                                <div class="news__title">
                                                    ${item.name}
                                                </div>
                                            </a>
                                        </div>

                                 `)

                            }

                        },error: function(request,error){
                            document.getElementById("create-newboard").style.display="block"
                        }



                    })
                },
                error: function(request,error){
                    document.getElementById("modal-create").style.display="block"
                    document.getElementById("slider-wrapper").style.display="none"

                }
        })
    }else if(workspace!=null){
        $.ajax({
            url:"http://localhost:8080/workflix-1.0/rest/workspace/"+workspace.toString()+"/boards",
            type : 'GET',
            processData: false,
            contentType: "application/json; charset=utf-8",
            success : function(data) {
                document.getElementById("create-newboard").style.display="none"
                for(let item of data ){
                     $("#slider-wrapper").append(`
                            <div class="news-slider__item swiper-slide">
                                <a href="/html/boards.html?id=${item.board_id}" class="news__item">
                                    <div class="news__title">
                                        ${item.name}
                                    </div>
                                </a>
                            </div>

                     `)

                }

            },error: function(request,error){
                 console.log(request)
                 document.getElementById("create-newboard").style.display="block"

            }



        })
    }

});
async function CreateNewWorkspace(){
    document.getElementById("workspace-form").innerHTML=""
    $("#workspace-form").append(`
        <input type="text" placeholder="Workspace name" id="workspace-name">
        <input type="text" placeholder="Workspace description" id="workspace-description">
        <button onclick="Create()">Create</button>
    `)
    document.getElementById("modal-create").style.display="block"
}
async function CreateNewBoard(){
    document.getElementById("workspace-form").innerHTML=""
    $("#workspace-form").append(`
        <input type="text" placeholder="Board name" id="board-name">
        <input type="text" placeholder="Board description" id="board-description">
        <button onclick="SendBoard()">Create</button>
    `)
    document.getElementById("modal-create").style.display="block"
    document.getElementById("create-newboard").style.display="none"
}
async function SendBoard(){
    boardname=document.getElementById("board-name").value
    boarddescription=document.getElementById("board-description").value
    workspace_id=localStorage.getItem("workspaceid")

    var obj={
        "board_id": "",
        "workspace_id": workspace_id,
        "name": boardname,
        "description": boarddescription,
        "visibility": "PUBLIC",
        "create_time": ""
    }
     $("#slider-wrapper").append(`
                <div class="news-slider__item swiper-slide">
                    <a href="/html/boards.html?id=${workspace_id}" class="news__item">
                        <div class="news__title">
                            ${boardname}
                        </div>
                    </a>
                </div>

     `)
   document.getElementById("modal-create").style.display="none"
   document.getElementById("slider-wrapper").style.display="flex"
   document.getElementById("create-newboard").style.display="none"
    console.log(obj)
    $.ajax({
        url : 'http://localhost:8080/workflix-1.0/rest/board/create',
        type : 'POST',
        data: JSON.stringify(obj),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success : function(data) {
          console.log(data)
          $("#slider-wrapper").append(`
                <div class="news-slider__item swiper-slide">
                    <a href="/html/boards.html?id=${data.board_id}" class="news__item">
                        <div class="news__title">
                            ${data.name}
                        </div>
                    </a>
                </div>

          `)
           document.getElementById("modal-create").style.display="none"
           document.getElementById("slider-wrapper").style.display="flex"

        },
        error : function(request,error)
        {
            alert("Request: "+JSON.stringify(request));
        }
    })

}

async function Create(){
    workspacename=document.getElementById("workspace-name").value
    workspacedescription=document.getElementById("workspace-description").value
    var obj={
        "workspace_id": "",
        "creation_time": "2023-05-19",
        "template_id": 1,
        "workspace_name": workspacename
    }
    $.ajax({
        url : 'http://localhost:8080/workflix-1.0/rest/workspace/create',
        type : 'POST',
        data: JSON.stringify(obj),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success : function(data) {
            console.log(data)
            workspace_id=data.workspace_id
            localStorage.setItem("workspaceid",workspace_id)
            $("#workspaces-dropdown").append(`
                <li onclick="ChangeWorkSpace('${workspace_id}')">
                <a class="dropdown-item" style="color: black;" href="#" ><i class="fa-solid fa-user-group"></i> ${workspacename}</a>
                </li>

            `)
            var userid=localStorage.getItem("userid")
            var obj={
                "user_id": userid,
                "workspace_id": workspace_id,
                "permission_id": 1
            }
            $.ajax({
                url : 'http://localhost:8080/workflix-1.0/rest/workspace/'+workspace_id.toString()+'/adduser',
                type : 'POST',
                data: JSON.stringify(obj),
                processData: false,
                contentType: "application/json; charset=utf-8",
                success : function(data) {
                    document.getElementById("modal-create").style.display="none"
                },
                error : function(request,error)
                {
                    alert("Request: "+JSON.stringify(request));
                }
            })

        },
        error : function(request,error)
        {
            alert("Request: "+JSON.stringify(request));
        }
    })

}










const modal = document.querySelector('.modal');
const modalOverlay = document.querySelector('.modal__overlay');
const closeBtn = document.querySelector('.modal__close-btn');
// ---- ---- cookies ---- ---- //
const createCookie = () => {
  let maxAge = ';max-age=10';
  let path = ';path=/';
  document.cookie = 'modalpopup=displayed' + maxAge + path;
};

// ---- ---- add active and cookie ---- ---- //
const displayModal = () => {
  if (document.cookie.indexOf('modalpopup') == -1) {
    modal.classList.add('active');
    modalOverlay.classList.add('active');
    createCookie();
  }
};

setTimeout(() => {
  displayModal();
}, 3000);

// ---- ---- remove active ---- ---- //
closeBtn.addEventListener('click', () => {
  modal.classList.remove('active');
  modalOverlay.classList.remove('active');
});
