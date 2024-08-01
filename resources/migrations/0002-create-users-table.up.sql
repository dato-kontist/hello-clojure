CREATE TABLE
    IF NOT EXISTS current.users (
        id TEXT PRIMARY KEY,
        email TEXT UNIQUE NOT NULL,
        name TEXT NOT NULL
    );