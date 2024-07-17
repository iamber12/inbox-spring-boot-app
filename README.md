
# Messaging Application

This is a highly scalable and available messaging application where users can exchange messages with each other. 

## Technologies
- **Application Tier**: Spring Boot
- **Database**: Apache Cassandra
- **Data Layer**: Spring Data Cassandra
- **Security**: Spring Security
- **View Layer**: Thymeleaf

## Features
- User authentication and authorization using GitHub OAuth
- Messaging functionality including:
  - Viewing emails
  - Replying to emails
  - Reply all functionality
- Email tracking (read/unread status)

## Installation
### Prerequisites
- Java 8 or higher
- Apache Cassandra
- Maven

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/iamber12/inbox-spring-boot-app
   cd inbox-spring-boot-app
   ```

2. **Set up Apache Cassandra**
   - Follow the instructions [here](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/install/installDeb.html) to install Apache Cassandra.
   - Create the necessary keyspace and tables using the `schema.cql` file in the repository.

3. **Configure the application**
   - Update `application.properties` with your Cassandra configuration and other necessary details.

4. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open your browser and navigate to `http://localhost:8080`.

## Next Steps
- Implement custom folder creation.
- Utilize the color property for folders.
- Enable moving emails between folders.
- Add multi-select and bulk actions (delete).

For further details and code, please refer to this [Code with Me series](https://www.youtube.com/watch?v=saSzMKeN6oI&list=PLqq-6Pq4lTTak0b5DnJ-x85MWMPaTdl4A&index=1).
