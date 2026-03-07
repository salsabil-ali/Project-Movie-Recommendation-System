#  Movie Recommendation System (Java)

A **Java-based system** that processes movie and user data from text files to generate **personalized movie recommendations**.  
The system includes a **robust validation engine** and a **fail-fast error-handling mechanism** to ensure data integrity.

---

##  Features

### 1. Sequential Data Processing
The system reads and validates **all movie data first**, then processes **user data**.

### 2. Strict Validation Engine
The system enforces strict formatting rules for all inputs:

- **Movie Titles**
  - Every word must start with a **capital letter**

- **Movie IDs**
  - Consist of **all capital letters from the movie title**
  - Followed by **three unique digits**

- **Usernames**
  - Contain **alphabetic characters and spaces only**
  - **Cannot begin with a space**

- **User IDs**
  - Exactly **9 alphanumeric characters**
  - **Must start with numbers**
  - **May end with at most one letter**

### 3. Fail-Fast Error Handling
If any validation error is detected:
- The system **immediately stops execution**
- Outputs **only the first encountered error**

### 4. Automated Recommendations
The system compares **user-preferred categories** with the movie database to generate **personalized recommendations**.


