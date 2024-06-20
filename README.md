# Todo-App with AWS Services

## Introduction
This project is a simple Todo app that supports adding and removing items from the todo list, with business logic limited to CRUD operations (Create, Read, Update, Delete). The primary focus is to utilize and understand various AWS services, deploy the app publicly, and ensure a 24/7 uninterrupted service using Nginx.



# Stack
- **Framework:** Spring Boot
- **Database:** MariaDB
- **Front-end:** Mustache (To create dynamic HTML)
  - Mustache is known for its simple and intuitive syntax. It uses a minimalist approach with just a few tags, making it easy to learn and use.
- **Other Technologies:** Spring Security, Spring Data JPA, JUnit 5
- **Web Server:** NginX
- **CI/CD:**
  - **Travis CI:**
    - Runs integration tests for new commits on the repository
    - Builds the project and pushes the build files to S3
  - **S3 & CodeDeploy [CD]:**
    - After CI, Travis CI triggers CodeDeploy, which then pulls the build zip file from S3 and deploys it to EC2
- **Hosting:** EC2



# Architecture
![img.png](img.png)

# Key features and functionality

## Main page
![](images/메인_페이지.png)

## Register Todo-List
![](images/게시글_등록.png)

## Google Login 
![](images/적용_확인.png)

# 


 
