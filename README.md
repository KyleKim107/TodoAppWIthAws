# Todo-App with AWS Services

## Introduction
Working on this project, I found the following particularly interesting:

- **Integration with AWS Services** 
  - Leveraging AWS S3 and CodeDeploy for seamless CI/CD was a fascinating experience. The way AWS services integrate and streamline deployment is impressive.
- **NginX as a Reverse Proxy** 
  - Setting up NginX to manage routing and load balancing highlighted its efficiency and simplicity in handling multiple services.
- **Spring Boot and Mustache** 
  - Using Spring Boot for backend development and Mustache for the frontend showcased the power of combining robust backend frameworks with simple, intuitive frontend templating engines.


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


## Architecture

The architecture of this project integrates several AWS services to create a robust CI/CD pipeline and deployment setup. Below is an overview of the system components and their interactions:

1. **Travis CI:** Continuous Integration service that is triggered by new commits to the GitHub repository. Travis CI runs tests and builds the project.

2. **S3:** After a successful build, Travis CI uploads the build artifacts (e.g., a ZIP file) to an S3 bucket.

3. **CodeDeploy:** CodeDeploy is notified by Travis CI to start a deployment. It retrieves the build artifacts from S3 and deploys them to the EC2 instances.

4. **AWS EC2:** The application runs on EC2 instances. These instances have Spring Boot applications installed and are fronted by NginX, which acts as a reverse proxy to manage incoming traffic. There are two processes in each EC2 instance to ensure high availability:
  - **Process 1:** Serves the current version of the application.
  - **Process 2:** When a new commit is pushed, this process is used to deploy the new version while the other process continues to serve users. Once the deployment is successful, traffic is switched to the new process, ensuring zero downtime.

5. **NginX:** NginX serves as a reverse proxy, handling requests from users and distributing them to the appropriate Spring Boot instances. It also manages the switching of processes during deployments to ensure a seamless user experience.
6. AWS RDS (Relational Database Service): RDS is used to manage the MariaDB database, ensuring scalability, durability, and automated backups.


This setup ensures continuous integration and delivery, automated deployments, and high availability for the application.

![img.png](img.png)

# Key features and functionality

## Main page
- The user can log in to the app with a Google account.
- Retrieve and display the todo list.

![](images/메인_페이지.png)

## Writing Todo Entity
  - The user can input information such as:
    - **Todo:** The task or item to be done.
    - **Author:** The person who is adding the todo item.
    - **Memo:** Additional notes or details about the todo item.

![](images/게시글_등록.png)

## Google Login 
- Users can log in with their Google account.
- Each user can only see their own todo entities.
![](images/적용_확인.png)


# The Brief batch files explanations 
##### Please refer to the link next to the file name for the full explanation.

## profile.sh
* This script is used to find the idle profile and its corresponding port for a Spring Boot application managed by Nginx.
```
RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

 if [ ${RESPONSE_CODE} -ge 400 ]
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi
    echo "> Current profile - $CURRENT_PROFILE"
```
* The purpose is to identify which profile (and port) is not currently in use by Nginx, so it can be safely updated without disrupting the running application.
```
 if [ ${CURRENT_PROFILE} == real1 ]
    then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
```
## start.sh
* ### This script deploys a new version of a Spring Boot project by copying the build files,
* ### setting necessary permissions, and running the application with the appropriate profile.
<br>

```
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
```
* readlink -f $0 gets the absolute path of the script.
* dirname $ABSPATH gets the directory name from the absolute path.
## deploy.sh
### Copy the Build File
```
echo "> Copying build file"
cp $REPOSITORY/zip/*.jar $REPOSITORY/
```
* Copies the JAR file(s) from the zip subdirectory to the REPOSITORY directory.
### Check for the PID of the Currently Running Application
```
echo "> Checking for the PID of the currently running application"
CURRENT_PID=$(lsof -t -i:8080)
echo "PID of the currently running application: $CURRENT_PID"
```
* lsof -t -i:8080 finds the PID of the process using port 8080, which is where the application is expected to run.
```
if [ -z "$CURRENT_PID" ]; then
    echo "> No running application, so no need to stop."
else
    kill -15 $CURRENT_PID
```
* If CURRENT_PID is not empty, it sends a SIGTERM (kill -15) to the process to gracefully stop it and waits for 5 seconds to ensure it stops.
### Run the New Application
```
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
```
* Runs the JAR file with nohup so that it continues running even if the terminal is closed.
## stop.sh
#### This script stops the application running on the idle port determined by `profile.sh`.
```
source ${ABSDIR}/profile.sh # Similar to import in Java
# Allows using functions inside profile.sh

IDLE_PORT=$(find_idle_port)

kill -15 ${IDLE_PID}
```
* This line sources the profile.sh file, which is expected to be in the same directory as the script.

## swtich.sh
* This script switches the Nginx proxy to point to the idle port where the new version of the application is running.
```
IDLE_PORT=$(find_idle_port)

echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

sudo service nginx reload

```
* Updates the service-url.inc file in the Nginx configuration directory to point to the new port. This effectively switches the proxy to the new application instance.
* Reloads the Nginx configuration to apply the changes.

## health.sh
* This script performs a health check on an idle port and switches the proxy if the health check is successful.
* This is used in the deployment process to ensure the new application instance is running properly before switching traffic to it.

# Study & Troubleshooting
- [Hibernate & JPA]
- [Updating in JPA]
- [Tomcat not starting error]
- [JPA Save]
- [JPA Proxy]
- [Google Auth Class]

[Hibernate & JPA]: https://velog.io/@sigint_107/Hibernate%EC%99%80-JPA%EB%82%B4%EB%B6%80-%EB%9C%AF%EC%96%B4%EB%B3%B4%EA%B8%B0
[Updating in JPA]: https://velog.io/@sigint_107/JPA-Update
[Tomcat not starting error]: https://velog.io/@sigint_107/Tomcat%EC%9D%B4-%EC%95%88%EC%98%AC%EB%9D%BC-%EA%B0%88%EB%95%8C
[JPA Proxy]: https://velog.io/@sigint_107/JPA-Proxy%EB%9E%80
[JPA Save]: https://velog.io/@sigint_107/JPA-Save%EB%8F%99%EC%9E%91%EA%B5%AC%EC%A1%B0
[Google Auth Class]: https://velog.io/@sigint_107/Registrating-the-Google-Auth-into-the-Spring-boot-application