USE BookReadingDB;
GO

-- Drop old tables if re-running script
DROP TABLE IF EXISTS Notifications;
DROP TABLE IF EXISTS Quotes;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS ReadingProcesses;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Genres;
DROP TABLE IF EXISTS Users;
GO

-- =========================
-- 1. Users
-- =========================
CREATE TABLE Users (
    idUser INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    passwordHash NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    gender NVARCHAR(20),
    avatarPath NVARCHAR(255),
    role NVARCHAR(20) NOT NULL DEFAULT 'USER',
    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    createdAt DATETIME NOT NULL DEFAULT GETDATE(),

    CONSTRAINT CK_Users_Role
        CHECK (role IN ('USER', 'ADMIN')),

    CONSTRAINT CK_Users_Status
        CHECK (status IN ('ACTIVE', 'LOCKED'))
);
GO

-- =========================
-- 2. Genres
-- =========================
CREATE TABLE Genres (
    idGenre INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(500),
    createdAt DATETIME NOT NULL DEFAULT GETDATE()
);
GO

-- =========================
-- 3. Books
-- =========================
CREATE TABLE Books (
    idBook INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    author NVARCHAR(150) NOT NULL,
    description NVARCHAR(MAX),
    idGenre INT,
    coverPath NVARCHAR(255),
    filePath NVARCHAR(255),
    fileType NVARCHAR(20),
    avgRating FLOAT NOT NULL DEFAULT 0,
    totalPages INT NOT NULL DEFAULT 0,
    createdAt DATETIME NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Books_Genres
        FOREIGN KEY (idGenre) REFERENCES Genres(idGenre)
        ON DELETE SET NULL,

    CONSTRAINT CK_Books_FileType
        CHECK (fileType IN ('PDF', 'EPUB') OR fileType IS NULL)
);
GO

-- =========================
-- 4. ReadingProcesses
-- =========================
CREATE TABLE ReadingProcesses (
    idReadingProcess INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    currentPage INT NOT NULL DEFAULT 0,
    readingStatus NVARCHAR(30) NOT NULL DEFAULT 'NOT_STARTED',
    rereadCount INT NOT NULL DEFAULT 0,
    updatedAt DATETIME NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_ReadingProcesses_Users
        FOREIGN KEY (idUser) REFERENCES Users(idUser)
        ON DELETE CASCADE,

    CONSTRAINT FK_ReadingProcesses_Books
        FOREIGN KEY (idBook) REFERENCES Books(idBook)
        ON DELETE CASCADE,

    CONSTRAINT UQ_ReadingProcesses_UserBook
        UNIQUE (idUser, idBook),

    CONSTRAINT CK_ReadingProcesses_Status
        CHECK (readingStatus IN ('NOT_STARTED', 'READING', 'FINISHED', 'DROPPED'))
);
GO

-- =========================
-- 5. Reviews
-- =========================
CREATE TABLE Reviews (
    idReview INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    rating INT NOT NULL,
    content NVARCHAR(MAX),
    createdAt DATETIME NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Reviews_Users
        FOREIGN KEY (idUser) REFERENCES Users(idUser)
        ON DELETE CASCADE,

    CONSTRAINT FK_Reviews_Books
        FOREIGN KEY (idBook) REFERENCES Books(idBook)
        ON DELETE CASCADE,

    CONSTRAINT UQ_Reviews_UserBook
        UNIQUE (idUser, idBook),

    CONSTRAINT CK_Reviews_Rating
        CHECK (rating BETWEEN 1 AND 5)
);
GO

-- =========================
-- 6. Quotes
-- =========================
CREATE TABLE Quotes (
    idQuote INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    note NVARCHAR(MAX),
    createdAt DATETIME NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Quotes_Users
        FOREIGN KEY (idUser) REFERENCES Users(idUser)
        ON DELETE CASCADE,

    CONSTRAINT FK_Quotes_Books
        FOREIGN KEY (idBook) REFERENCES Books(idBook)
        ON DELETE CASCADE
);
GO

-- =========================
-- 7. Notifications
-- =========================
CREATE TABLE Notifications (
    idNotification INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    message NVARCHAR(MAX),
    remindTime DATETIME NOT NULL,
    repeatType NVARCHAR(20) NOT NULL DEFAULT 'NONE',
    notificationStatus NVARCHAR(20) NOT NULL DEFAULT 'UNREAD',
    createdAt DATETIME NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Notifications_Users
        FOREIGN KEY (idUser) REFERENCES Users(idUser)
        ON DELETE CASCADE,

    CONSTRAINT FK_Notifications_Books
        FOREIGN KEY (idBook) REFERENCES Books(idBook)
        ON DELETE CASCADE,

    CONSTRAINT CK_Notifications_RepeatType
        CHECK (repeatType IN ('NONE', 'DAILY', 'WEEKLY')),

    CONSTRAINT CK_Notifications_Status
        CHECK (notificationStatus IN ('UNREAD', 'READ'))
);
GO


--- TEST ---
USE BookReadingDB;
GO

SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE';

--- each column test ---
SELECT COLUMN_NAME, DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Users';

SELECT COLUMN_NAME, DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Books';

SELECT COLUMN_NAME, DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'ReadingProcesses';