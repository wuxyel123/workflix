# Workflix
## Description
Project for Web Application 2022/2023 course @UNIPD
This project aims to develop an issue tracking system (Like Trello or Jira). It provides a comprehensive platform for managing user workspaces, boards, activities, and user analytics. It offers a user-friendly interface for organizing and tracking various tasks and collaborations within a team or organization.
The web application leverages a PostgreSQL database for data storage and retrieval. It integrates with other web technologies such as HTML, CSS, and JavaScript for the user interface and interaction. Additionally, JavaEE is used to handle user requests and perform database operations.
Overall, the project aims to deliver a powerful web application that simplifies task management and enhances collaboration within teams or organizations. It provides users with a structured and intuitive platform to streamline workflows, monitor progress, and improve overall productivity.
## Folder's structure 
- HW1: Contains all the webapp resources 
- WA-workflix-HW1: Contins the documentation
## Application setup
To set up the project, follow these steps:

- Clone the repository
- Create a new PostgreSQL database
- Execute the SQL script in your database to create the necessary tables and view
- Set up the corret database in the context.xml file
- Build the application with maven 'mvn clean install'
- A war package is created and ready to be deployed on tomcat, wildfly or JBoss