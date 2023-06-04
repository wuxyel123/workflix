// $(document).ready(function (event) {
//   getAnalyticsPageContent();
// });

// function getAnalyticsPageContent() {
//   $.ajax({
//     url: new URL('http://127.0.0.1:8080/workflix-1.0/rest/analytics'),
//     data: {},
//     method: 'GET',
//     success: CreateAnalyticsPage(result),
//     fail: function (data) {
//       console.log(data);
//       alert("problem processing the request");
//     }
//   })
// }



// Sample data (replace with real data)
var data_projects = [
    {
        user_id: 1,
        username: "John",
        workspace_id: 1,
        workspace_name: "Workspace 1",
        num_completed_activities: 3,
        num_total_activities: 7,
        total_worked_time: 360,
        num_comments: 5
    },
    {
        user_id: 1,
        username: "John",
        workspace_id: 1,
        workspace_name: "Workspace 2",
        num_completed_activities: 10,
        num_total_activities: 20,
        total_worked_time: 260,
        num_comments: 5
    },
    {
        user_id: 1,
        username: "John",
        workspace_id: 1,
        workspace_name: "Workspace 3",
        num_completed_activities: 10,
        num_total_activities: 12,
        total_worked_time: 460,
        num_comments: 5
    },
];
//Summary data insert
//projects in progress
var ProjectInProgressCount = data_projects.length;
document.getElementById('in-progress-project').innerHTML=`${ProjectInProgressCount}`
//completed activities
var totalCompletedActivities = data_projects.reduce(function(sum, project) {
    return sum + project.num_completed_activities;
  }, 0);
document.getElementById('completed-activities').innerHTML=`${totalCompletedActivities}`
//total worked time
var totalWorkedTime = data_projects.reduce(function(sum, project) {
    return sum + project.total_worked_time;
  }, 0);
//total activies
document.getElementById('total-worked-time').innerHTML=`${totalWorkedTime}h`
var totalActivities = data_projects.reduce(function(sum, project) {
    return sum + project.num_total_activities;
  }, 0);
document.getElementById('total-activities').innerHTML=`${totalActivities}`


// CHART JS
var project_select = document.getElementById('form-select');
// var selectedProjectOption = selectedOption.options[project_select.selectedIndex];

project_select.addEventListener("change", function (event) {
    var selectedValue = event.target.value;

    var project_id = data_projects.findIndex((function (project) {
        return project.workspace_name === selectedValue;
    })
    );
    console.log(selectedValue, project_id);

    var xValues = ["completed_activities", "total_activities", "total_worked_time", "comments"];
    var yValues = [data_projects[`${project_id}`].num_completed_activities,
    data_projects[`${project_id}`].num_total_activities,
    data_projects[`${project_id}`].total_worked_time, data_projects[`${project_id}`].num_comments];
    var barColors = ["plum", "bisque", "lavender", "pink"];

    new Chart("activitiesChart", {
        type: "bar",
        data: {
            labels: xValues,
            datasets: [{
                backgroundColor: barColors,
                data: yValues
            }]
        },
        options: {
            legend: { display: false },
            title: {
                display: true,
                text: "Project Performance"
            }
        }
    });
});


data_projects.forEach(item => {
    var selectBox = document.getElementsByClassName('form-select');
    var selectChoice = document.createElement('option');
    selectChoice.innerHTML = ` <option value=" ${item.workspace_id}"> ${item.workspace_name}</option>`;
    var divCount = selectChoice.length;
    selectBox[0].appendChild(selectChoice);

    var projectlist = document.getElementsByClassName('analytics-project-sub-container');
    var content = document.createElement('div');
    content.innerHTML = `<div class="row " style="padding: 10px 0px;">
        <div class="card col-sm-12" style="padding: 20px;">
            <div class="container text-left ">
                <div class="row ">
                    <div class="col" style="text-emphasis">
                        ${item.workspace_name}
                    </div>
                    <div class="col">
                        Completed Activities
                    </div>
                    <div class="col">
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        Manager
                    </div>
                    <div class="col">
                        <i class="bi bi-card-checklist"></i>  ${item.num_completed_activities}
                    </div>
                    <div class="col">
                        <div class="progress" role="progressbar" aria-label="Basic example" aria-valuenow="25"
                            aria-valuemin="0" aria-valuemax="100">
                            <div class="progress-bar" style="width: ${item.num_completed_activities / item.num_total_activities * 100}%; background-color: #6A2871;"> </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `
    var divCount = projectlist.length;
    projectlist[divCount - 1].appendChild(content)
})