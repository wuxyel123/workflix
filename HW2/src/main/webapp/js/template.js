fetch('template.json')
  .then(response => response.json())
  .then(data => {
    // 获取要渲染的列表元素
    const jsonList = document.getElementById('json-list');

    // 遍历 JSON 数据
    data.forEach(item => {
      // 创建列表项
      const listItem = document.createElement('div');
      listItem.classList.add('list-item');
  
      // 创建图像元素
      const image = document.createElement('img');
      image.src = item.image_url;
      image.alt = item.template_name;
      image.className='tempalte-img'

      //button
      const button= document.createElement('button')
      button.className='image-use-button';
      button.textContent='use '

      const editButton= document.createElement('button')
      editButton.className='image-edit-button';
      editButton.textContent='edit'
      // 将图像元素添加到列表项中
      listItem.appendChild(image);
      listItem.appendChild(button);
      listItem.appendChild(editButton);
      // 将列表项添加到列表元素中
      jsonList.appendChild(listItem);
    });
  })
  .catch(error => {
    console.error('读取 JSON 文件时出错:', error);
  });