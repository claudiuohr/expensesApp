# Expense Tracker

This is a simple expense tracker application that helps users to manage and track their expenses. The application uses a combination of Kotlin with Maven and Spring Boot for the backend, and Python with Flask for the frontend.

## Features

- User account creation and authentication
- Adding, updating, and deleting expenses
- Viewing expense details
- Updating user information
- Encryption for secure data storage

## Technologies

- Backend: Kotlin, Maven, Spring Boot
- Frontend: Python, Flask, HTML, CSS
- Database: MariaDB
- Encryption: AES/CBC/PKCS5Padding

## Getting Started

1. Ensure you have the following software installed on your machine:
   - JDK (Java Development Kit) version 8 or higher
   - Maven
   - Python 3.6 or higher
   - Flask
   - Requests (Python library)

2. Clone this repository:

       git clone https://github.com/claudiuohr/expensesApp.git

3. Navigate to the project folder:

       cd expensesApp

4. Compile and run the backend Spring Boot application:

       cd src/main/kotlin
       mvn clean install
       mvn spring-boot:run

5. In a separate terminal window, navigate to the frontend Flask application folder:

       cd python

6. Install the required Python packages:

       pip install -r requirements.txt

7. Run the Flask application:

       export FLASK_APP=main.py
       flask run

8. Open your web browser and visit `http://localhost:5000` to start using the Expense Tracker.
