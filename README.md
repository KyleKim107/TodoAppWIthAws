# Todo-App with AWS Services

## Introduction
This project is a simple Todo app that supports adding and removing items from the todo list, with business logic limited to CRUD operations (Create, Read, Update, Delete). The primary focus is to utilize and understand various AWS services, deploy the app publicly, and ensure a 24/7 uninterrupted service using Nginx.



# Stack
- **Framework:** Spring Boot
- **Database:** RDS [MariaDB]
- **Front-end:** Mustache 
  - Mustache is known for its simple and intuitive syntax. It uses a minimalist approach with just a few tags, making it easy to learn and use.
- **Other Technologies:** Spring Security, Spring Data JPA, JUnit 5, Google Auth
- **Web Server:** NginX
- **CI/CD:**
  - **Travis [CI]:**
  - **S3 & CodeDeploy [CD]:**
- **Hosting:** EC2

#  구글 auth 추가해서 내용 좀 늘려 

## Architecture

The architecture of this project integrates several AWS services to create a robust CI/CD pipeline and deployment setup. Below is an overview of the system components and their interactions:

1. **Travis CI:** Continuous Integration service that is triggered by new commits to the GitHub repository. Travis CI runs tests and builds the project.

2. **S3:** After a successful build, Travis CI uploads the build artifacts (e.g., a ZIP file) to an S3 bucket.

3. **CodeDeploy:** CodeDeploy is notified by Travis CI to start a deployment. It retrieves the build artifacts from S3 and deploys them to the EC2 instances.

4. **AWS EC2:** The application runs on EC2 instances. These instances have Spring Boot applications installed and are fronted by NginX, which acts as a reverse proxy to manage incoming traffic. There are two processes in each EC2 instance to ensure high availability:
  - **Process 1:** Serves the current version of the application.
  - **Process 2:** When a new commit is pushed, this process is used to deploy the new version while the other process continues to serve users. Once the deployment is successful, traffic is switched to the new process, ensuring zero downtime.

5. **NginX:** NginX serves as a reverse proxy, handling requests from users and distributing them to the appropriate Spring Boot instances. It also manages the switching of processes during deployments to ensure a seamless user experience.


This setup ensures continuous integration and delivery, automated deployments, and high availability for the application.

![img.png](img.png)

# Key features and functionality

## Main page
![](images/메인_페이지.png)

## Register Todo-List
![](images/게시글_등록.png)

## Google Login 
![](images/적용_확인.png)

# Study & Troubleshooting 
- [Hibernate & JPA]
- [Updating in JPA]
- [Tomcat not starting error]
- [JPA Save]
- [JPA Proxy]
- [Google Auth Class]

batch file

[Hibernate & JPA]: https://velog.io/@sigint_107/Hibernate%EC%99%80-JPA%EB%82%B4%EB%B6%80-%EB%9C%AF%EC%96%B4%EB%B3%B4%EA%B8%B0
[Updating in JPA]: https://velog.io/@sigint_107/JPA-Update
[Tomcat not starting error]: https://velog.io/@sigint_107/Tomcat%EC%9D%B4-%EC%95%88%EC%98%AC%EB%9D%BC-%EA%B0%88%EB%95%8C
[JPA Proxy]: https://velog.io/@sigint_107/JPA-Proxy%EB%9E%80
[JPA Save]: https://velog.io/@sigint_107/JPA-Save%EB%8F%99%EC%9E%91%EA%B5%AC%EC%A1%B0
[Google Auth Class]: https://velog.io/@sigint_107/Registrating-the-Google-Auth-into-the-Spring-boot-application