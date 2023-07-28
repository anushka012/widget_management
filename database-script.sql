CREATE TABLE Widget (
    widgetId SERIAL PRIMARY KEY,
    widgetName VARCHAR(255) NOT NULL,
    description TEXT,
    creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Editor (
    editorId SERIAL PRIMARY KEY,
    widgetId INTEGER NOT NULL,
    editorName VARCHAR(255) NOT NULL,
    content TEXT,
    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_tag_editor BOOLEAN DEFAULT FALSE,
    is_image_editor BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (widgetId) REFERENCES Widget (widgetId)
);