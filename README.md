# Job Portal

## Description
This is a job portal application where users can apply for jobs, and employers can review applications and provide feedback. The application enables job seekers to create profiles, apply for job openings, and track their application statuses. Employers can view applications, update statuses, and provide feedback.

## Features
- Job seekers can apply for jobs.
- Employers can review applications and provide feedback.
- Admin users can manage job positions and user roles.
- Secure authentication for job seekers and employers.
- Role-based access control.

## Installation

### Prerequisites
- **Docker**: Required to set up the database.
- **Gradle**: Required to build and run the project.
- **Postman**: Used for creating users and testing API endpoints.

### Steps to set up the project locally:

1. **Clone the repository:**

   Clone the repository to your local machine:
   ```bash
   git clone https://github.com/VisiMihasi/JobPortal.git
   cd JobPortal

2. **Create the database:**

Navigate to the DOCKER directory and run the script to create the database.

bash

./DOCKER/Create-Database.sh  

3. **Build the project:**

Run the clean build command to ensure everything is set up correctly.


4. **./gradlew clean build**

Create users via Postman:

Open Postman and perform the following steps:

Create 1 Admin user
Create 11 Employer users
Create 14 Job Seeker users
You can use the provided Postman collection to automate the user creation process. Ensure the roles (ADMIN, EMPLOYER, JOB SEEKER) are assigned accordingly.


5. **Generate data:**

After creating the users, navigate to the DOCKER folder and execute the dump-file.sql to generate the necessary data in the database.

docker exec -i <your-container-name> mysql -u <username> -p <database-name> < DOCKER/dump-file.sql

6. **Run the application:**

Once the database is set up, and users are created, run the application.

bash
Copy
./gradlew bootRun
The server will start running on http://localhost:8080.



## Overview
This document provides the details of the API endpoints for the Job Portal application. The application supports multiple user roles, such as `ADMIN`, `EMPLOYER`, and `JOB_SEEKER`. Access to different API endpoints is restricted based on the user’s role.

## Authentication
- All users must authenticate via the `/api/auth/login` endpoint to receive a token for subsequent requests.

## API Endpoints

### User Management

#### **Create a new user**
- **URL**: `/api/users`
- **Method**: `POST`
- **Role Required**: `ADMIN`
- **Description**: Allows an admin to create a new user.
- **Public Access**: Yes, this endpoint is publicly accessible.

#### **Get user details**
- **URL**: `/api/users/{id}`
- **Method**: `GET`
- **Role Required**: `ADMIN`
- **Description**: Allows the admin to retrieve details of a specific user by ID.
- **Access**: Only accessible to the `ADMIN`.

#### **Delete a user**
- **URL**: `/api/users/{id}`
- **Method**: `DELETE`
- **Role Required**: `ADMIN`
- **Description**: Allows the admin to delete a user by ID.
- **Access**: Only accessible to the `ADMIN`.

### Job Management

#### **Create a new job**
- **URL**: `/api/jobs`
- **Method**: `POST`
- **Role Required**: `EMPLOYER`
- **Description**: Allows an employer to create a new job listing.
- **Access**: Only accessible to users with the `EMPLOYER` role.

#### **Get job details**
- **URL**: `/api/jobs/{id}`
- **Method**: `GET`
- **Role Required**: `EMPLOYER`, `JOB_SEEKER`
- **Description**: Allows users (employers or job seekers) to retrieve details of a specific job listing.
- **Access**: Accessible to both `EMPLOYER` and `JOB_SEEKER`.

#### **Update a job**
- **URL**: `/api/jobs/{id}`
- **Method**: `PUT`
- **Role Required**: `EMPLOYER`
- **Description**: Allows an employer to update a job listing.
- **Access**: Only accessible to users with the `EMPLOYER` role.

#### **Delete a job**
- **URL**: `/api/jobs/{id}`
- **Method**: `DELETE`
- **Role Required**: `EMPLOYER`
- **Description**: Allows an employer to delete a job listing.
- **Access**: Only accessible to users with the `EMPLOYER` role.

### Application Management

#### **Apply for a job**
- **URL**: `/api/applications`
- **Method**: `POST`
- **Role Required**: `JOB_SEEKER`
- **Description**: Allows a job seeker to apply for a job.
- **Access**: Only accessible to users with the `JOB_SEEKER` role.

#### **Get job applications for an employer**
- **URL**: `/api/applications/myApplicationsForJob/{jobId}`
- **Method**: `GET`
- **Role Required**: `EMPLOYER`
- **Description**: Allows an employer to view applications for a specific job.
- **Access**: Only accessible to users with the `EMPLOYER` role.

#### **Update application status**
- **URL**: `/api/applications/updateStatus/{applicationId}`
- **Method**: `PUT`
- **Role Required**: `EMPLOYER`
- **Description**: Allows an employer to update the status of a job application (e.g., Accepted, Rejected).
- **Access**: Only accessible to users with the `EMPLOYER` role.

#### **Get applications for a job seeker**
- **URL**: `/api/applications/myApplications`
- **Method**: `GET`
- **Role Required**: `JOB_SEEKER`
- **Description**: Allows a job seeker to view their job applications.
- **Access**: Only accessible to users with the `JOB_SEEKER` role.

### Public Endpoints

#### **User Login**
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Role Required**: Public (No Authentication)
- **Description**: Allows users to log in to the system and retrieve an authentication token.
- **Access**: This endpoint is publicly accessible.

---

## Access Control Summary

| Endpoint                               | Method | Required Role    | Description                                         |
|----------------------------------------|--------|------------------|-----------------------------------------------------|
| `/api/users`                           | POST   | `ADMIN`          | Create a new user                                   |
| `/api/users/{id}`                      | GET    | `ADMIN`          | Get details of a user by ID                         |
| `/api/users/{id}`                      | DELETE | `ADMIN`          | Delete a user by ID                                |
| `/api/jobs`                            | POST   | `EMPLOYER`       | Create a new job listing                           |
| `/api/jobs/{id}`                       | GET    | `EMPLOYER`, `JOB_SEEKER` | Get details of a job listing                      |
| `/api/jobs/{id}`                       | PUT    | `EMPLOYER`       | Update a job listing                               |
| `/api/jobs/{id}`                       | DELETE | `EMPLOYER`       | Delete a job listing                               |
| `/api/applications`                    | POST   | `JOB_SEEKER`     | Apply for a job                                    |
| `/api/applications/myApplicationsForJob/{jobId}` | GET | `EMPLOYER`       | View applications for a job (Employer only)         |
| `/api/applications/updateStatus/{applicationId}` | PUT | `EMPLOYER`      | Update application status (Employer only)           |
| `/api/applications/myApplications`     | GET    | `JOB_SEEKER`     | View applications for a job seeker                 |

---

## Technologies Used
- **Spring Boot**: For building the backend API.
- **MySQL**: For database management.
- **Docker**: To containerize the database.
- **Gradle**: For building and managing dependencies.
- **Postman**: For testing API endpoints and creating users.

---

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit (`git commit -am 'Add new feature'`).
4. Push the changes (`git push origin feature-branch`).
5. Create a new Pull Request.

---
