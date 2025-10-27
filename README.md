# oop-project-nhom4

# Set up  
  1. Install Netbeans (optional)
  https://netbeans.apache.org/front/main/download/

  2. Install MySQL Workbench (optional)
  https://dev.mysql.com/downloads/workbench/

  3. Install MySQL
    a. Download MySQL from web
      https://dev.mysql.com/doc/refman/9.1/en/installing.html

    b. Use docker
      ```
        docker pull mysql:latest

        docker run --name my-mysql \
        -e MYSQL_ROOT_PASSWORD=mysecretpassword \
        -p 3306:3306 \
        -d mysql:latest
      ```
  4. Import data
    Could use MySQL Workbench or the command line
    Create a database oop_project_nhom4_db then import data from ./data/oop.sql
