
document.addEventListener('DOMContentLoaded', function(event) {
    getWorkspaceList();
    document.getElementById("select_workspace").addEventListener("click", showSelectedWorkspace);
});

function genericGETRequest(url, callback){
    var httpRequest = new XMLHttpRequest();
    if (!httpRequest) {
      alert('Cannot create an XMLHTTP instance');
      return false;
    }
    httpRequest.onreadystatechange = function (){ callback(httpRequest)};
    httpRequest.open('GET', url);
    httpRequest.send();
}


function getWorkspaceList() {
    var url = new URL('http:///127.0.0.1:8080/workflix-1.0/rest/user/1/workspaces');
    url.searchParams.set('operation', 'list');
    genericGETRequest(url, createListSelector);
}

function createListSelector(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status == 200) {
             var jsonData = JSON.parse(req.responseText);
             var workspaces = jsonData['workspace-list'];

             for (let i=0; i<workspaces.length; i++) {
                Workspace_name = sanitize(workspaces[i]);
                currHtml =  document.getElementById("workspace_selector").innerHTML;
                document.getElementById("workspace_selector").innerHTML = currHtml+"<option value='"+Workspace_name+"'>"+Workspace_name+"</option>"
             }
        }
        else {
            alert("problem processing the request");
        }
    }
}


function showSelectedWorkspace(){
    //need to change the url to the analytics url
    var url = new URL('http://localhost:8080/workflix-1.0/rest/user/1/workspaces');
    var sel = document.getElementById("workspace_selector");
    var selected_workspace = sel.options[sel.selectedIndex].value;
    url.searchParams.set('workspace_id', selected_workspace);
    genericGETRequest(url, fillWrokspaceData);
}

function fillWrokspaceData(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status == 200) {
             var jsonData = JSON.parse(req.responseText);
             var data = jsonData['data'];
             var user_id = sanitize(data['user_id']);
             var username = sanitize(data['username']);
             var workspace_id = sanitize(data['workspace_id']);
             var workspace_name = sanitize(data['workspace_name']);
             var num_completed_activities = sanitize(data['num_completed_activities']);
             var num_total_activities = sanitize(data['num_total_activities']);
             var total_worked_time = sanitize(data['total_worked_time']);
             var num_comments = sanitize(data['num_comments']);


            var workspaceTable = "<table>";
             workspaceTable = workspaceTable + "<tr><th>User ID</th><td>"+user_id+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Username</th><td>"+username+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Workspace ID</th><td>"+workspace_id+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Workspace Name</th><td>"+workspace_name+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Num Completed Activities</th><td>"+num_completed_activities+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Num Total Activities</th><td>"+num_total_activities+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Total Worked Time</th><td>"+total_worked_time+"</td></tr>";
             workspaceTable = workspaceTable + "<tr><th>Num Comments</th><td>"+num_comments+"</td></tr>";
             workspaceTable = workspaceTable + "</table>";
             document.getElementById("workspaceInfo").innerHTML = workspaceTable;


             var activitiesChart = document.getElementById("activitiesChart").getContext("2d");
             new Chart(activitiesChart, {
                type: "bar",
                data: {
                    labels: data.map(item => item.workspace_name),
                    datasets: [
                        {
                            label: "Completed Activities",
                            data: data.map(item => item.num_completed_activities),
                            backgroundColor: "rgba(75, 192, 192, 0.6)"
                        },
                        {
                            label: "Total Activities",
                            data: data.map(item => item.num_total_activities),
                            backgroundColor: "rgba(54, 162, 235, 0.6)"
                        }
                        , {
                            label: "Total Worked Time",
                            data: data.map(item => item.total_worked_time),
                            backgroundColor: "rgba(7, 180, 53, 0.6)"
                        },
                        {
                            label: "Total Comments",
                            data: data.map(item => item.num_comments),
                            backgroundColor: "rgba(1, 3, 235, 0.6)"
                        }
                    ]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });



        }
        else {
            console.log(JSON.parse(httpRequest.responseText));
            alert("Problem processing the request");
        }
    }
}



