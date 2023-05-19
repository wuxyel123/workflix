-- Inserting sample users
INSERT INTO users (username, password, email, first_name, last_name, profile_picture, description)
VALUES ('john_doe', sha512('password123'), 'john.doe@example.com', 'John', 'Doe', 'profile.jpg', 'Software Developer');

INSERT INTO users (username, password, email, first_name, last_name, profile_picture, description)
VALUES ('jane_smith', sha512('password456'), 'jane.smith@example.com', 'Jane', 'Smith', 'avatar.jpg', 'Marketing Specialist');

-- Inserting sample templates
INSERT INTO template (template_name, image_url)
VALUES ('Template 1', 'template1.jpg');

INSERT INTO template (template_name, image_url)
VALUES ('Template 2', 'template2.jpg');

-- Inserting sample permissions
INSERT INTO permission (permission_name, description)
VALUES ('Admin', 'Full access to workspace');

INSERT INTO permission (permission_name, description)
VALUES ('Editor', 'Can edit workspace content');

-- Inserting sample workspaces
INSERT INTO workspace (workspace_name, template_id)
VALUES ('Workspace 1', 1);

INSERT INTO workspace (workspace_name, template_id)
VALUES ('Workspace 2', 2);

-- Inserting sample user_workspace entries
INSERT INTO user_workspace (user_id, workspace_id, permission_id)
VALUES (1, 1, 1);

INSERT INTO user_workspace (user_id, workspace_id, permission_id)
VALUES (2, 1, 2);

-- Inserting sample boards
INSERT INTO board (workspace_id, name, description, visibility)
VALUES (1, 'Board 1', 'Sample board for Workspace 1', 'Public');

INSERT INTO board (workspace_id, name, description, visibility)
VALUES (1, 'Board 2', 'Sample board for Workspace 1', 'Private');

-- Inserting sample subboards
INSERT INTO subboards (board_id, name, index, default_completed_activity_subboard)
VALUES (1, 'Subboard 1', 1, true);

INSERT INTO subboards (board_id, name, index, default_completed_activity_subboard)
VALUES (1, 'Subboard 2', 2, false);

-- Inserting sample activities
INSERT INTO activities (subboard_id, name, description, start_date, end_date, worked_time, index)
VALUES (1, 'Activity 1', 'Sample activity for Subboard 1', '2023-05-01', '2023-05-07', 40, 1);

INSERT INTO activities (subboard_id, name, description, start_date, end_date, worked_time, index)
VALUES (1, 'Activity 2', 'Sample activity for Subboard 1', '2023-05-03', '2023-05-10', 30, 2);

-- Inserting sample comments
INSERT INTO comments (activity_id, user_id, comment_text)
VALUES (1, 1, 'Great work on Activity 1!');

INSERT INTO comments (activity_id, user_id, comment_text)
VALUES (1, 2, 'I have a suggestion for Activity 1.');

-- Inserting sample assignees
INSERT INTO assignee (activity_id, user_id)
VALUES (1, 1);

INSERT INTO assignee (activity_id, user_id)
VALUES (2, 2);
