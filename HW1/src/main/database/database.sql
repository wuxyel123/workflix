CREATE TABLE users (
                      user_id SERIAL PRIMARY KEY,
                      username VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      first_name VARCHAR(255),
                      last_name VARCHAR(255),
                      profile_picture VARCHAR(255),
                      description TEXT,
                      create_date TIMESTAMP NOT NULL
);

CREATE TABLE template (
                          template_id SERIAL PRIMARY KEY,
                          template_name VARCHAR(255) NOT NULL UNIQUE,
                          image_url VARCHAR(255) NOT NULL
);

CREATE TABLE permission (
                            permission_id SERIAL PRIMARY KEY,
                            permission_name VARCHAR(255) NOT NULL UNIQUE,
                            description TEXT
);

CREATE TABLE workspace (
                           workspace_id SERIAL PRIMARY KEY,
                           workspace_name VARCHAR(255),
                           template_id INT NOT NULL,
                           creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (template_id) REFERENCES template(template_id)
);

CREATE TABLE user_workspace (
                                user_id INT NOT NULL,
                                workspace_id INT NOT NULL,
                                permission_id INT NOT NULL,
                                PRIMARY KEY (user_id, workspace_id, permission_id),
                                FOREIGN KEY (user_id) REFERENCES users(user_id),
                                FOREIGN KEY (workspace_id) REFERENCES workspace(workspace_id),
                                FOREIGN KEY (permission_id) REFERENCES permission(permission_id)
);

CREATE TABLE board (
                       board_id SERIAL PRIMARY KEY,
                       workspace_id INT NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(2048),
                       visibility VARCHAR(255),
                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (workspace_id) REFERENCES workspace(workspace_id)                 
);

CREATE TABLE subboards (
                          subboard_id SERIAL PRIMARY KEY,
                          board_id INT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          index INT NOT NULL,
                          default_completed_activity_subboard BOOL,
                          creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (board_id) REFERENCES board(board_id)
);

CREATE TABLE activities (
                          activity_id SERIAL PRIMARY KEY,
                          subboard_id INT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          start_date DATE,
                          end_date DATE,
                          worked_time INT,
                          index INT NOT NULL,
                          FOREIGN KEY (subboard_id) REFERENCES subboards(subboard_id)
);

CREATE TABLE comments (
                          comment_id SERIAL PRIMARY KEY,
                          activity_id INT NOT NULL,
                          user_id INT NOT NULL,
                          comment_text TEXT NOT NULL,
                          creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (activity_id) REFERENCES activities(activity_id)
);

CREATE TABLE assignee (
                          activity_id INT NOT NULL,
                          user_id INT NOT NULL,
                          PRIMARY KEY (activity_id, user_id),
                          FOREIGN KEY (activity_id) REFERENCES activities(activity_id),
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE VIEW user_analytics_per_workspace AS
SELECT uw.user_id, u.username, w.workspace_id, w.workspace_name,
       COUNT(CASE WHEN a.end_date IS NOT NULL AND sb.default_completed_activity_subboard = true THEN 1 END) AS num_completed_activities,
       COUNT(a.activity_id) AS num_total_activities,
       SUM(a.worked_time) AS total_worked_time,
       COUNT(c.comment_id) AS num_comments
FROM user_workspace uw
JOIN users u ON uw.user_id = u.user_id
JOIN workspace w ON uw.workspace_id = w.workspace_id
JOIN board b ON w.workspace_id = b.workspace_id
JOIN subboards sb ON b.board_id = sb.board_id
LEFT JOIN activities a ON sb.subboard_id = a.subboard_id
LEFT JOIN comments c ON a.activity_id = c.activity_id
GROUP BY uw.user_id, u.username, w.workspace_id, w.workspace_name;