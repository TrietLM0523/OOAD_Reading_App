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
--- TEST LOGIN ---
USE BookReadingDB;
GO

SELECT idUser, username, email, role, status, createdAt
FROM Users;

-- New Admin --
USE BookReadingDB;
GO

UPDATE Users
SET role = 'ADMIN'
WHERE email = 'admin2@gmail.com';

SELECT idUser, username, email, role, status
FROM Users
WHERE email = 'admin2@gmail.com';

--- sample data ---
USE BookReadingDB;
GO

INSERT INTO Genres (name, description)
VALUES
(N'Văn học Nhật Bản', N'Các tác phẩm văn học Nhật Bản hiện đại và kinh điển'),
(N'Light Novel / Fantasy', N'Tiểu thuyết nhẹ, kỳ ảo, phiêu lưu và thế giới giả tưởng'),
(N'Văn học kinh điển', N'Các tác phẩm có giá trị văn học lâu dài, thường được nghiên cứu và tái bản nhiều lần');
GO

-- Sách mẫu, chưa cần file thật
INSERT INTO Books (
    title, author, description, idGenre,
    coverPath, filePath, fileType, avgRating, totalPages
)
VALUES
(
    N'Kokoro',
    N'Natsume Soseki',
    N'Tác phẩm xoay quanh mối quan hệ giữa một người thanh niên và Sensei, qua đó khắc họa sự cô độc, mặc cảm tội lỗi và những biến chuyển tinh thần của con người Nhật Bản trong thời kỳ hiện đại hóa.',
    1,
    NULL, NULL, NULL, 0, 0
),
(
    N'Re:Zero - Arc 4',
    N'Tappei Nagatsuki',
    N'Phần truyện tiếp tục hành trình của Subaru trong một thế giới giả tưởng khắc nghiệt, nơi năng lực quay lại sau cái chết buộc nhân vật phải đối mặt với mất mát, lựa chọn và trách nhiệm để bảo vệ những người quan trọng.',
    2,
    NULL, NULL, NULL, 0, 0
),
(
    N'Wagahai wa Neko de Aru',
    N'Natsume Soseki',
    N'Tác phẩm được kể dưới góc nhìn châm biếm của một con mèo, qua đó quan sát đời sống con người, tầng lớp trí thức và xã hội Nhật Bản thời Minh Trị với giọng văn hài hước nhưng sâu sắc.',
    3,
    NULL, NULL, NULL, 0, 0
);
GO

USE BookReadingDB;
GO

SELECT 
    b.idBook,
    b.title,
    b.author,
    g.name AS genreName,
    b.totalPages,
    b.avgRating,
    b.filePath,
    b.fileType,
    b.coverPath,
    b.createdAt
FROM Books b
LEFT JOIN Genres g ON b.idGenre = g.idGenre
ORDER BY b.idBook;

--- file type ---
USE BookReadingDB;
GO

ALTER TABLE Books DROP CONSTRAINT CK_Books_FileType;
GO

ALTER TABLE Books
ADD CONSTRAINT CK_Books_FileType
CHECK (fileType IN ('PDF', 'EPUB', 'TXT') OR fileType IS NULL);
GO
--- check ---
SELECT name, definition
FROM sys.check_constraints
WHERE parent_object_id = OBJECT_ID('Books');

--- test book file ---
USE BookReadingDB;
GO

SELECT idBook, title, filePath, fileType
FROM Books
WHERE title LIKE N'Re:Zero - Arc 4';
--- test process ---
USE BookReadingDB;
GO

SELECT 
    rp.idReadingProcess,
    b.title,
    rp.currentPage,
    rp.readingStatus,
    rp.updatedAt
FROM ReadingProcesses rp
JOIN Books b ON rp.idBook = b.idBook
ORDER BY rp.updatedAt DESC;

--- test review & quote ---
USE BookReadingDB;
GO

SELECT 
    r.idReview,
    u.email,
    b.title,
    r.rating,
    r.reviewContent,
    r.createdAt
FROM Reviews r
JOIN Users u ON r.idUser = u.idUser
JOIN Books b ON r.idBook = b.idBook
ORDER BY r.createdAt DESC;

USE BookReadingDB;
GO

SELECT COLUMN_NAME, DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Reviews'
ORDER BY ORDINAL_POSITION;

--- test dashboard data ---
USE BookReadingDB;
GO

SELECT COUNT(*) AS totalUsers FROM Users;
SELECT COUNT(*) AS totalBooks FROM Books;
SELECT COUNT(*) AS totalGenres FROM Genres;
SELECT COUNT(*) AS totalReadingProcesses FROM ReadingProcesses;
SELECT COUNT(*) AS totalReviews FROM Reviews;
SELECT COUNT(*) AS totalQuotes FROM Quotes;

SELECT readingStatus, COUNT(*) AS total
FROM ReadingProcesses
GROUP BY readingStatus;

--- test user statistics ---
USE BookReadingDB;
GO

DECLARE @email NVARCHAR(255) = 'testuser2@gmail.com';

SELECT 
    COUNT(*) AS totalLibraryBooks
FROM ReadingProcesses rp
JOIN Users u ON rp.idUser = u.idUser
WHERE u.email = @email;

SELECT 
    COUNT(*) AS booksWithFile
FROM ReadingProcesses rp
JOIN Users u ON rp.idUser = u.idUser
JOIN Books b ON rp.idBook = b.idBook
WHERE u.email = @email
  AND b.filePath IS NOT NULL
  AND b.filePath <> '';

SELECT 
    ISNULL(SUM(rp.currentPage), 0) AS totalCurrentPages
FROM ReadingProcesses rp
JOIN Users u ON rp.idUser = u.idUser
WHERE u.email = @email;

SELECT 
    rp.readingStatus,
    COUNT(*) AS total
FROM ReadingProcesses rp
JOIN Users u ON rp.idUser = u.idUser
WHERE u.email = @email
GROUP BY rp.readingStatus;

USE BookReadingDB;
GO

SELECT 
    r.idReview,
    u.username,
    u.email,
    b.title,
    r.rating,
    r.reviewContent,
    r.createdAt
FROM Reviews r
JOIN Users u ON r.idUser = u.idUser
JOIN Books b ON r.idBook = b.idBook
ORDER BY r.createdAt DESC;

--- test notifications ---
USE BookReadingDB;
GO

SELECT 
    n.idNotification,
    u.email,
    b.title,
    n.message,
    n.remindTime,
    n.repeatType,
    n.notificationStatus,
    n.createdAt
FROM Notifications n
JOIN Users u ON n.idUser = u.idUser
JOIN Books b ON n.idBook = b.idBook
ORDER BY n.createdAt DESC;