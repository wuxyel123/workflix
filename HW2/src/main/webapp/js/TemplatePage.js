// $(document).ready(function (event) {
//   getTemplatePageContent();
// });

// function getTemplatePageContent() {
//   $.ajax({
//     url: new URL('http://127.0.0.1:8080/workflix-1.0/rest/template'),
//     data: {},
//     method: 'GET',
//     success: CreateTemplateListPage(result),
//     fail: function (data) {
//       console.log(data);
//       alert("problem processing the request");
//     }
//   })
// }


fetch('../html/template.json')
  .then(response => response.json())
  .then(data => {
    CreateTemplateListPage(data);
  })
  .catch(error => {
    console.error('loading data error:', error);
  });

function CreateTemplateListPage(data) {
  const jsonList = document.getElementById('json-list');
  data.forEach(item => {
    const listItem = document.createElement('div');
    listItem.classList.add('list-item');

    const image = document.createElement('img');
    image.src = item.image_url;
    image.alt = item.template_name;
    image.className = 'tempalte-img'
    const button = document.createElement('button')
    button.className = 'image-use-button';
    button.textContent = 'use';
    // button.addEventListener("click", onclick())
    // function onclick(){
    //   console.log('clicked')
    // }

    const editButton = document.createElement('button')
    editButton.className = 'image-edit-button';
    editButton.textContent = 'edit'
    listItem.appendChild(image);
    listItem.appendChild(button);
    listItem.appendChild(editButton);
    jsonList.appendChild(listItem);
  });
}


