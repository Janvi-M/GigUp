-- schema.sql
DROP DATABASE IF EXISTS gigup;
CREATE DATABASE gigup;
USE gigup;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS project_skills;
DROP TABLE IF EXISTS reviews;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    registration_date TIMESTAMP,
    role VARCHAR(50) DEFAULT 'ROLE_USER' -- Added role for authorization (e.g., ROLE_USER, ROLE_ADMIN)
);

CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    posted_date TIMESTAMP,
    deadline DATE,
    budget DECIMAL(10, 2),
    status VARCHAR(50) DEFAULT 'OPEN', -- e.g., OPEN, IN_PROGRESS, COMPLETED, CANCELLED
    client_id BIGINT NOT NULL,
    freelancer_id BIGINT, -- Nullable until a freelancer is assigned
    FOREIGN KEY (client_id) REFERENCES users(id),
    FOREIGN KEY (freelancer_id) REFERENCES users(id)
);

CREATE TABLE bids (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    submission_date TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PENDING', -- e.g., PENDING, ACCEPTED, REJECTED
    project_id BIGINT NOT NULL,
    freelancer_id BIGINT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (freelancer_id) REFERENCES users(id)
);

CREATE TABLE skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE project_skills (
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id),
    PRIMARY KEY (project_id, skill_id) -- Composite primary key
);

CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    review_date TIMESTAMP,
    reviewer_id BIGINT NOT NULL, -- User who wrote the review
    reviewee_id BIGINT NOT NULL, -- User who is being reviewed (could be client or freelancer)
    project_id BIGINT, -- Optional: Link to the project the review is about
    FOREIGN KEY (reviewer_id) REFERENCES users(id),
    FOREIGN KEY (reviewee_id) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);