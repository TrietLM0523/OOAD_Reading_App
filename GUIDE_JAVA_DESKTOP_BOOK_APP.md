# Guide xây dựng lại App Quản lý Đọc sách bằng Java Desktop

> Mục tiêu: làm lại ứng dụng quản lý đọc sách từ bản thiết kế OOAD hiện có, nhưng chuyển từ React Native + Firebase sang **Java Desktop + cơ sở dữ liệu tự xây dựng**.  
> Guide này dùng để đẩy dần lên Git, theo từng milestone nhỏ, dễ commit và dễ báo cáo tiến độ.

---

## 0. Tóm tắt hệ thống cần xây

Ứng dụng là hệ thống quản lý đọc sách với 2 nhóm người dùng chính:

- **User**: đăng ký, đăng nhập, tìm sách, đọc sách, thêm sách vào thư viện cá nhân, lưu tiến độ đọc, đánh giá sách, lưu trích dẫn, xem thống kê cá nhân.
- **Admin**: quản lý sách, quản lý tài khoản, quản lý bình luận/đánh giá, xem dashboard thống kê toàn hệ thống.

Các nhóm chức năng chính:

| Mã UC | Nhóm chức năng | Tác nhân |
|---|---|---|
| UC01 | Quản lý xác thực: đăng nhập, đăng ký | User |
| UC02 | Quản lý thông tin cá nhân | User |
| UC03 | Quản lý đọc sách: tìm kiếm, xem chi tiết, đọc, thêm vào thư viện | User |
| UC04 | Thống kê từ quản trị viên | Admin |
| UC05 | Quản lý thư viện cá nhân | User |
| UC06 | Thống kê từ người đọc | User |
| UC07 | Quản lý trích dẫn | User |
| UC08 | Quản lý tài khoản | Admin |
| UC09 | Quản lý sách | Admin |
| UC10 | Quản lý bình luận/đánh giá | Admin |

---

## 1. Công nghệ đề xuất

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java |
| Giao diện desktop | JavaFX |
| Build tool | Maven hoặc Gradle |
| Database | MySQL / PostgreSQL / SQLite |
| Kết nối DB | JDBC |
| ORM | Không bắt buộc. Bản đầu nên dùng DAO + JDBC để dễ hiểu |
| Đọc PDF | Apache PDFBox hoặc JavaFX WebView |
| Đọc EPUB | epub4j hoặc xử lý sau |
| Biểu đồ | JavaFX Chart |
| Mã hóa mật khẩu | BCrypt |
| Lưu file sách | Lưu local trong thư mục `data/books/` |
| Lưu ảnh bìa | Lưu local trong thư mục `data/covers/` |

Khuyến nghị cho bản đầu:

```text
JavaFX + Maven + MySQL + JDBC + DAO Pattern
```

---

## 2. Kiến trúc tổng thể

Giữ đúng tinh thần thiết kế ban đầu: **Client – Service – Data**.

```text
JavaFX View / Controller
        ↓
Service Layer
        ↓
DAO / Repository Layer
        ↓
Database + Local File Storage
```

Vai trò từng tầng:

| Tầng | Thành phần | Trách nhiệm |
|---|---|---|
| Presentation | JavaFX FXML, Controller | Hiển thị UI, nhận thao tác người dùng |
| Service | AuthService, BookService, LibraryService... | Xử lý nghiệp vụ, kiểm tra logic |
| DAO | UserDAO, BookDAO, ReviewDAO... | Truy vấn database |
| Data | MySQL/PostgreSQL/SQLite + thư mục file | Lưu dữ liệu và file sách |

Nguyên tắc:

- Controller không gọi SQL trực tiếp.
- Controller gọi Service.
- Service gọi DAO.
- DAO là nơi duy nhất viết câu lệnh SQL.
- File PDF/EPUB không lưu trực tiếp trong DB, chỉ lưu đường dẫn.

---

## 3. Cấu trúc thư mục project

```text
book-reading-desktop/
├── README.md
├── pom.xml
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── queries.md
├── data/
│   ├── books/
│   └── covers/
├── docs/
│   ├── GUIDE.md
│   ├── ERD.md
│   └── SCREEN_LIST.md
└── src/
    └── main/
        ├── java/
        │   └── com/bookapp/
        │       ├── Main.java
        │       ├── config/
        │       │   └── DatabaseConnection.java
        │       ├── model/
        │       │   ├── User.java
        │       │   ├── Book.java
        │       │   ├── Genre.java
        │       │   ├── ReadingProcess.java
        │       │   ├── Quote.java
        │       │   ├── Review.java
        │       │   └── Notification.java
        │       ├── dao/
        │       │   ├── UserDAO.java
        │       │   ├── BookDAO.java
        │       │   ├── GenreDAO.java
        │       │   ├── ReadingProcessDAO.java
        │       │   ├── QuoteDAO.java
        │       │   ├── ReviewDAO.java
        │       │   └── NotificationDAO.java
        │       ├── service/
        │       │   ├── AuthService.java
        │       │   ├── UserService.java
        │       │   ├── BookService.java
        │       │   ├── LibraryService.java
        │       │   ├── ReadingService.java
        │       │   ├── QuoteService.java
        │       │   ├── ReviewService.java
        │       │   ├── StatisticsService.java
        │       │   └── AdminService.java
        │       ├── controller/
        │       │   ├── LoginController.java
        │       │   ├── RegisterController.java
        │       │   ├── HomeController.java
        │       │   ├── BookDetailController.java
        │       │   ├── LibraryController.java
        │       │   ├── ReaderController.java
        │       │   ├── QuoteController.java
        │       │   ├── StatisticsController.java
        │       │   ├── AdminDashboardController.java
        │       │   ├── AdminBookController.java
        │       │   ├── AdminUserController.java
        │       │   └── AdminReviewController.java
        │       └── util/
        │           ├── PasswordUtil.java
        │           ├── FileStorageUtil.java
        │           ├── SessionManager.java
        │           └── ValidationUtil.java
        └── resources/
            ├── fxml/
            │   ├── Login.fxml
            │   ├── Register.fxml
            │   ├── Home.fxml
            │   ├── BookDetail.fxml
            │   ├── Library.fxml
            │   ├── Reader.fxml
            │   ├── QuoteList.fxml
            │   ├── UserStatistics.fxml
            │   ├── AdminDashboard.fxml
            │   ├── AdminBookManagement.fxml
            │   ├── AdminUserManagement.fxml
            │   └── AdminReviewManagement.fxml
            ├── css/
            │   └── app.css
            └── images/
```

---

## 4. Thiết kế database

### 4.1. Bảng `users`

```sql
CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    gender VARCHAR(20),
    avatar_path VARCHAR(255),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    status ENUM('ACTIVE', 'LOCKED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4.2. Bảng `genres`

```sql
CREATE TABLE genres (
    id_genre INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);
```

### 4.3. Bảng `books`

```sql
CREATE TABLE books (
    id_book INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150),
    description TEXT,
    genre_id INT,
    cover_path VARCHAR(255),
    file_path VARCHAR(255),
    file_type ENUM('PDF', 'EPUB'),
    avg_rating DOUBLE DEFAULT 0,
    total_pages INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (genre_id) REFERENCES genres(id_genre)
);
```

### 4.4. Bảng `reading_process`

```sql
CREATE TABLE reading_process (
    id_reading_process INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_book INT NOT NULL,
    current_page INT DEFAULT 0,
    status ENUM('CHUA_DOC', 'DANG_DOC', 'DA_DOC', 'BO_DO') DEFAULT 'CHUA_DOC',
    reread_count INT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE(id_user, id_book),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_book) REFERENCES books(id_book) ON DELETE CASCADE
);
```

### 4.5. Bảng `quotes`

```sql
CREATE TABLE quotes (
    id_quote INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_book INT NOT NULL,
    content TEXT NOT NULL,
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_book) REFERENCES books(id_book) ON DELETE CASCADE
);
```

### 4.6. Bảng `reviews`

```sql
CREATE TABLE reviews (
    id_review INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_book INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_user, id_book),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_book) REFERENCES books(id_book) ON DELETE CASCADE
);
```

### 4.7. Bảng `notifications`

```sql
CREATE TABLE notifications (
    id_notification INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_book INT NOT NULL,
    message TEXT,
    remind_time DATETIME NOT NULL,
    repeat_type ENUM('NONE', 'DAILY', 'WEEKLY') DEFAULT 'NONE',
    status ENUM('UNREAD', 'READ') DEFAULT 'UNREAD',
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_book) REFERENCES books(id_book) ON DELETE CASCADE
);
```

---

## 5. Mapping class OOAD sang Java class

| Lớp phân tích | Java class | Bảng DB |
|---|---|---|
| User | `User.java` | `users` |
| Book | `Book.java` | `books` |
| Genre | `Genre.java` | `genres` |
| ReadingProcess | `ReadingProcess.java` | `reading_process` |
| Quote | `Quote.java` | `quotes` |
| Review | `Review.java` | `reviews` |
| Notification | `Notification.java` | `notifications` |

---

## 6. Danh sách màn hình cần xây

### 6.1. Màn hình phía User

| Màn hình | FXML | Controller | Use case |
|---|---|---|---|
| Đăng nhập | `Login.fxml` | `LoginController` | UC01 |
| Đăng ký | `Register.fxml` | `RegisterController` | UC01 |
| Hồ sơ cá nhân | `Profile.fxml` | `ProfileController` | UC02 |
| Trang chủ / Tìm sách | `Home.fxml` | `HomeController` | UC03 |
| Chi tiết sách | `BookDetail.fxml` | `BookDetailController` | UC03 |
| Thư viện cá nhân | `Library.fxml` | `LibraryController` | UC05 |
| Đọc sách | `Reader.fxml` | `ReaderController` | UC03, UC05 |
| Trích dẫn | `QuoteList.fxml` | `QuoteController` | UC07 |
| Thống kê cá nhân | `UserStatistics.fxml` | `StatisticsController` | UC06 |

### 6.2. Màn hình phía Admin

| Màn hình | FXML | Controller | Use case |
|---|---|---|---|
| Dashboard Admin | `AdminDashboard.fxml` | `AdminDashboardController` | UC04 |
| Quản lý sách | `AdminBookManagement.fxml` | `AdminBookController` | UC09 |
| Quản lý tài khoản | `AdminUserManagement.fxml` | `AdminUserController` | UC08 |
| Quản lý bình luận | `AdminReviewManagement.fxml` | `AdminReviewController` | UC10 |

---

## 7. Milestone phát triển

## Milestone 1: Khởi tạo project

Mục tiêu:

- Tạo project JavaFX.
- Kết nối được database.
- Tạo schema SQL.
- Tạo class model.

Việc cần làm:

- [ ] Tạo repo Git.
- [ ] Tạo project Maven/Gradle.
- [ ] Tạo `pom.xml`.
- [ ] Tạo database `book_reading_app`.
- [ ] Tạo file `database/schema.sql`.
- [ ] Tạo `DatabaseConnection.java`.
- [ ] Tạo model: `User`, `Book`, `Genre`, `ReadingProcess`, `Quote`, `Review`, `Notification`.
- [ ] Test kết nối database.

Commit gợi ý:

```bash
git add .
git commit -m "init javafx project and database schema"
```

---

## Milestone 2: Đăng nhập, đăng ký, session

Mục tiêu:

- User có thể đăng ký, đăng nhập, đăng xuất.
- Có phân quyền USER / ADMIN.

Việc cần làm:

- [ ] Tạo `Login.fxml`.
- [ ] Tạo `Register.fxml`.
- [ ] Tạo `UserDAO`.
- [ ] Tạo `AuthService`.
- [ ] Tạo `PasswordUtil` dùng BCrypt.
- [ ] Tạo `SessionManager`.
- [ ] Xử lý đăng ký.
- [ ] Xử lý đăng nhập.
- [ ] Nếu role là `ADMIN`, chuyển sang `AdminDashboard`.
- [ ] Nếu role là `USER`, chuyển sang `Home`.

Commit gợi ý:

```bash
git add .
git commit -m "add authentication and role based navigation"
```

---

## Milestone 3: Admin quản lý sách

Mục tiêu:

- Admin thêm, sửa, xóa, tìm kiếm sách.
- Upload/copy ảnh bìa và file PDF/EPUB vào local storage.

Việc cần làm:

- [ ] Tạo `AdminBookManagement.fxml`.
- [ ] Tạo `BookDAO`.
- [ ] Tạo `GenreDAO`.
- [ ] Tạo `BookService`.
- [ ] Tạo `FileStorageUtil`.
- [ ] Hiển thị sách bằng `TableView`.
- [ ] Thêm sách mới.
- [ ] Chọn ảnh bìa.
- [ ] Chọn file sách.
- [ ] Sửa thông tin sách.
- [ ] Xóa sách.
- [ ] Tìm kiếm sách.

Commit gợi ý:

```bash
git add .
git commit -m "add admin book management"
```

---

## Milestone 4: Trang chủ User và tìm kiếm sách

Mục tiêu:

- User xem danh sách sách.
- User tìm kiếm sách.
- User xem chi tiết sách.
- User thêm sách vào thư viện.

Việc cần làm:

- [ ] Tạo `Home.fxml`.
- [ ] Tạo `BookDetail.fxml`.
- [ ] Tạo `HomeController`.
- [ ] Tạo `BookDetailController`.
- [ ] Tạo hàm lấy danh sách sách.
- [ ] Tạo hàm tìm kiếm theo tên/tác giả/thể loại.
- [ ] Hiển thị chi tiết sách.
- [ ] Thêm sách vào thư viện bằng cách tạo record trong `reading_process`.

Commit gợi ý:

```bash
git add .
git commit -m "add user home search and book detail"
```

---

## Milestone 5: Thư viện cá nhân

Mục tiêu:

- User xem thư viện cá nhân.
- User cập nhật trạng thái đọc và tiến độ.
- User xóa sách khỏi thư viện.

Việc cần làm:

- [ ] Tạo `Library.fxml`.
- [ ] Tạo `LibraryController`.
- [ ] Tạo `ReadingProcessDAO`.
- [ ] Tạo `LibraryService`.
- [ ] Tạo `ReadingService`.
- [ ] Hiển thị sách đã thêm.
- [ ] Tìm kiếm sách trong thư viện.
- [ ] Cập nhật trạng thái đọc.
- [ ] Cập nhật trang hiện tại.
- [ ] Xóa sách khỏi thư viện.

Commit gợi ý:

```bash
git add .
git commit -m "add personal library and reading tracker"
```

---

## Milestone 6: Đọc PDF và lưu tiến độ

Mục tiêu:

- Mở được file PDF.
- Lưu được trang hiện tại.
- Khi mở lại, quay về trang đã đọc gần nhất.

Việc cần làm:

- [ ] Tạo `Reader.fxml`.
- [ ] Tạo `ReaderController`.
- [ ] Tích hợp Apache PDFBox hoặc WebView.
- [ ] Mở file từ `books.file_path`.
- [ ] Đọc `current_page` từ `reading_process`.
- [ ] Khi chuyển trang, update `current_page`.
- [ ] Khi thoát reader, lưu tiến độ.

Commit gợi ý:

```bash
git add .
git commit -m "add pdf reader and reading progress saving"
```

Ghi chú:

- Bản đầu chỉ cần PDF.
- EPUB có thể để milestone sau.

---

## Milestone 7: Đánh giá và bình luận sách

Mục tiêu:

- User đánh giá sao và viết cảm nhận.
- Admin xem/xóa bình luận.

Việc cần làm:

- [ ] Tạo `ReviewDAO`.
- [ ] Tạo `ReviewService`.
- [ ] Thêm form đánh giá ở `BookDetail` hoặc `LibraryBookDetail`.
- [ ] Lưu rating từ 1 đến 5.
- [ ] Lưu nội dung nhận xét.
- [ ] Cập nhật lại `avg_rating` trong bảng `books`.
- [ ] Tạo `AdminReviewManagement.fxml`.
- [ ] Admin xem danh sách review.
- [ ] Admin xóa review.

Commit gợi ý:

```bash
git add .
git commit -m "add reviews and admin review management"
```

---

## Milestone 8: Trích dẫn

Mục tiêu:

- User thêm, xem, xóa trích dẫn.

Việc cần làm:

- [ ] Tạo `QuoteDAO`.
- [ ] Tạo `QuoteService`.
- [ ] Tạo `QuoteList.fxml`.
- [ ] Tạo `QuoteController`.
- [ ] Hiển thị danh sách trích dẫn.
- [ ] Thêm trích dẫn mới.
- [ ] Gắn trích dẫn với `id_user` và `id_book`.
- [ ] Xóa trích dẫn.

Commit gợi ý:

```bash
git add .
git commit -m "add quote management"
```

---

## Milestone 9: Thống kê người đọc

Mục tiêu:

- User xem biểu đồ và chỉ số đọc sách cá nhân.

Chỉ số cần có:

- Tổng số sách.
- Số sách theo trạng thái: chưa đọc, đang đọc, đã đọc, bỏ dở.
- Tổng số trang đã đọc.
- Tác giả được đọc nhiều nhất.
- Top 5 thể loại nhiều sách nhất.
- Sách được user đánh giá cao nhất.

Việc cần làm:

- [ ] Tạo `UserStatistics.fxml`.
- [ ] Tạo `StatisticsController`.
- [ ] Tạo `StatisticsService`.
- [ ] Viết query thống kê trạng thái sách.
- [ ] Viết query tổng số trang đã đọc.
- [ ] Viết query tác giả đọc nhiều nhất.
- [ ] Viết query top thể loại.
- [ ] Hiển thị `PieChart`.
- [ ] Hiển thị `BarChart`.

Commit gợi ý:

```bash
git add .
git commit -m "add user reading statistics"
```

---

## Milestone 10: Dashboard Admin

Mục tiêu:

- Admin xem chỉ số toàn hệ thống.

Chỉ số cần có:

- Tổng đầu sách.
- Tổng thành viên.
- Tổng bình luận.
- Tổng trích dẫn.
- Top 5 sách đánh giá cao nhất.
- Phân bố sách theo thể loại.
- Top người đọc nhiều nhất.

Việc cần làm:

- [ ] Tạo `AdminDashboard.fxml`.
- [ ] Tạo `AdminDashboardController`.
- [ ] Tạo query tổng sách.
- [ ] Tạo query tổng user.
- [ ] Tạo query tổng review.
- [ ] Tạo query tổng quote.
- [ ] Tạo query top sách đánh giá cao.
- [ ] Tạo query phân bố thể loại.
- [ ] Tạo query top người đọc nhiều.
- [ ] Hiển thị chart và table.

Commit gợi ý:

```bash
git add .
git commit -m "add admin dashboard statistics"
```

---

## Milestone 11: Quản lý tài khoản Admin

Mục tiêu:

- Admin xem, tìm kiếm, khóa, mở khóa, xóa user.

Việc cần làm:

- [ ] Tạo `AdminUserManagement.fxml`.
- [ ] Tạo `AdminUserController`.
- [ ] Tạo hàm lấy danh sách user.
- [ ] Tìm kiếm user theo username/email.
- [ ] Khóa tài khoản: `status = LOCKED`.
- [ ] Mở khóa tài khoản: `status = ACTIVE`.
- [ ] Xóa user.
- [ ] Không cho admin tự xóa chính mình.

Commit gợi ý:

```bash
git add .
git commit -m "add admin user management"
```

---

## Milestone 12: Thông báo nhắc đọc sách

Mục tiêu:

- User đặt lịch nhắc đọc sách.
- App hiện popup khi đến giờ.

Việc cần làm:

- [ ] Tạo `NotificationDAO`.
- [ ] Tạo `NotificationService`.
- [ ] Tạo `NotificationScheduler`.
- [ ] Cho user tạo reminder từ sách trong thư viện.
- [ ] Dùng `ScheduledExecutorService` kiểm tra mỗi 60 giây.
- [ ] Nếu đến giờ, hiện JavaFX Alert.
- [ ] Nếu repeat là DAILY/WEEKLY, tạo lịch tiếp theo.

Commit gợi ý:

```bash
git add .
git commit -m "add reading reminder notifications"
```

---

## 8. Các query thống kê quan trọng

### 8.1. Tổng đầu sách

```sql
SELECT COUNT(*) FROM books;
```

### 8.2. Tổng thành viên

```sql
SELECT COUNT(*) FROM users WHERE role = 'USER';
```

### 8.3. Tổng bình luận

```sql
SELECT COUNT(*) FROM reviews;
```

### 8.4. Tổng trích dẫn

```sql
SELECT COUNT(*) FROM quotes;
```

### 8.5. Top 5 sách đánh giá cao nhất

```sql
SELECT title, author, avg_rating
FROM books
ORDER BY avg_rating DESC
LIMIT 5;
```

### 8.6. Phân bố thể loại

```sql
SELECT g.name, COUNT(b.id_book) AS total
FROM genres g
LEFT JOIN books b ON b.genre_id = g.id_genre
GROUP BY g.id_genre, g.name
ORDER BY total DESC;
```

### 8.7. Top người đọc nhiều nhất

```sql
SELECT u.username, COUNT(rp.id_book) AS total_books
FROM users u
JOIN reading_process rp ON u.id_user = rp.id_user
WHERE u.role = 'USER'
GROUP BY u.id_user, u.username
ORDER BY total_books DESC
LIMIT 5;
```

### 8.8. Thống kê trạng thái sách của một user

```sql
SELECT status, COUNT(*) AS total
FROM reading_process
WHERE id_user = ?
GROUP BY status;
```

### 8.9. Tổng số trang đã đọc của một user

```sql
SELECT SUM(current_page) AS total_pages_read
FROM reading_process
WHERE id_user = ?;
```

### 8.10. Top thể loại trong thư viện user

```sql
SELECT g.name, COUNT(*) AS total
FROM reading_process rp
JOIN books b ON rp.id_book = b.id_book
JOIN genres g ON b.genre_id = g.id_genre
WHERE rp.id_user = ?
GROUP BY g.id_genre, g.name
ORDER BY total DESC
LIMIT 5;
```

---

## 9. Quy tắc code

### 9.1. Controller

Controller chỉ xử lý:

- Nhận input từ UI.
- Gọi service.
- Hiển thị kết quả.
- Điều hướng màn hình.

Không viết SQL trong Controller.

Ví dụ sai:

```java
// Sai: Controller không được tự viết SQL
PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users");
```

Ví dụ đúng:

```java
// Đúng
List<Book> books = bookService.searchBooks(keyword);
bookTable.setItems(FXCollections.observableArrayList(books));
```

### 9.2. Service

Service xử lý nghiệp vụ:

- Kiểm tra dữ liệu hợp lệ.
- Kiểm tra quyền.
- Điều phối nhiều DAO.
- Tính toán logic trước khi lưu.

Ví dụ:

```java
public void addBookToLibrary(int userId, int bookId) {
    if (readingProcessDAO.exists(userId, bookId)) {
        throw new IllegalArgumentException("Sách đã có trong thư viện");
    }

    readingProcessDAO.insert(userId, bookId, "CHUA_DOC");
}
```

### 9.3. DAO

DAO chỉ xử lý database:

- `insert`
- `update`
- `delete`
- `findById`
- `findAll`
- `search`

Ví dụ:

```java
public Optional<User> findByEmail(String email) {
    // SQL SELECT user by email
}
```

---

## 10. Quy tắc Git

### 10.1. Branch

Gợi ý branch:

```bash
main
develop
feature/auth
feature/admin-book
feature/library
feature/reader
feature/statistics
```

### 10.2. Commit message

Dùng format:

```text
type: short description
```

Ví dụ:

```bash
git commit -m "feat: add login screen"
git commit -m "feat: add user dao"
git commit -m "fix: handle duplicate email on register"
git commit -m "docs: add database schema guide"
git commit -m "refactor: separate book service from controller"
```

Các loại commit:

| Type | Ý nghĩa |
|---|---|
| feat | Thêm chức năng |
| fix | Sửa lỗi |
| docs | Sửa tài liệu |
| refactor | Sửa code nhưng không đổi chức năng |
| test | Thêm/sửa test |
| chore | Việc phụ: config, build, clean |

### 10.3. Không nên commit

Thêm `.gitignore`:

```gitignore
target/
.idea/
*.iml
*.class
.DS_Store

data/books/*
data/covers/*

!data/books/.gitkeep
!data/covers/.gitkeep

.env
application.properties
```

Giữ thư mục rỗng bằng `.gitkeep`.

---

## 11. Thứ tự ưu tiên khi thời gian gấp

Nếu không đủ thời gian làm hết, ưu tiên theo thứ tự:

1. Database + login/register.
2. Admin thêm sách.
3. User tìm sách.
4. User thêm sách vào thư viện.
5. User đọc PDF.
6. Lưu tiến độ đọc.
7. Đánh giá sách.
8. Thống kê cơ bản.
9. Trích dẫn.
10. Admin quản lý user/bình luận.
11. Notification.
12. EPUB.

Bản demo tối thiểu nên có:

- Đăng nhập/đăng ký.
- Admin thêm sách.
- User xem/tìm sách.
- User thêm vào thư viện.
- User đọc PDF.
- Lưu tiến độ.
- Thống kê đơn giản.

---

## 12. Checklist nghiệm thu

### Auth

- [ ] Đăng ký tài khoản mới.
- [ ] Không cho trùng email.
- [ ] Mật khẩu được hash.
- [ ] Đăng nhập đúng.
- [ ] Đăng nhập sai báo lỗi.
- [ ] Tài khoản bị khóa không đăng nhập được.
- [ ] Đăng xuất được.

### User

- [ ] Xem thông tin cá nhân.
- [ ] Cập nhật thông tin cá nhân.
- [ ] Đổi mật khẩu.

### Book

- [ ] Admin thêm sách.
- [ ] Admin sửa sách.
- [ ] Admin xóa sách.
- [ ] User tìm kiếm sách.
- [ ] User xem chi tiết sách.

### Library

- [ ] User thêm sách vào thư viện.
- [ ] Không thêm trùng sách.
- [ ] User xem thư viện cá nhân.
- [ ] User cập nhật trạng thái đọc.
- [ ] User xóa sách khỏi thư viện.

### Reader

- [ ] Mở được PDF.
- [ ] Lưu được trang hiện tại.
- [ ] Mở lại đúng trang đã đọc.
- [ ] Thoát reader không mất tiến độ.

### Review

- [ ] User đánh giá sao.
- [ ] User viết nhận xét.
- [ ] Cập nhật điểm trung bình sách.
- [ ] Admin xem review.
- [ ] Admin xóa review.

### Quote

- [ ] User thêm trích dẫn.
- [ ] User xem trích dẫn.
- [ ] User xóa trích dẫn.

### Statistics

- [ ] User xem tổng sách.
- [ ] User xem biểu đồ trạng thái đọc.
- [ ] User xem tổng trang đã đọc.
- [ ] Admin xem tổng sách/user/review/quote.
- [ ] Admin xem top sách.
- [ ] Admin xem phân bố thể loại.

### Notification

- [ ] User tạo lịch nhắc đọc.
- [ ] Đến giờ hiện popup.
- [ ] Lặp lại theo ngày/tuần nếu có.

---

## 13. Gợi ý README.md ngắn cho repo

```md
# Book Reading Desktop App

Ứng dụng quản lý đọc sách desktop xây dựng bằng JavaFX và cơ sở dữ liệu SQL.

## Công nghệ

- Java
- JavaFX
- Maven
- MySQL
- JDBC
- BCrypt
- Apache PDFBox

## Chức năng chính

- Đăng nhập / đăng ký
- Quản lý sách
- Tìm kiếm sách
- Thư viện cá nhân
- Đọc PDF
- Lưu tiến độ đọc
- Đánh giá sách
- Trích dẫn
- Thống kê người đọc
- Dashboard Admin

## Cấu trúc

```text
src/main/java/com/bookapp
├── controller
├── service
├── dao
├── model
├── util
└── config
```

## Cài đặt

1. Clone repo.
2. Tạo database `book_reading_app`.
3. Chạy script `database/schema.sql`.
4. Cấu hình kết nối DB trong `DatabaseConnection.java`.
5. Chạy `Main.java`.
```

---

## 14. Ghi chú triển khai thực tế

- Làm PDF trước, EPUB sau.
- Không lưu file sách vào database.
- Không để Controller phình quá to.
- Mỗi use case nên có ít nhất một service tương ứng.
- Mỗi bảng nên có một DAO riêng.
- Mỗi milestone nên có một commit rõ ràng.
- Khi demo, nên chuẩn bị sẵn vài sách PDF mẫu và vài user mẫu.
- Admin nên được seed sẵn trong `seed.sql`.
- Nếu dùng MySQL, nhớ bật UTF-8 để lưu tiếng Việt tốt.

---

## 15. Seed data gợi ý

```sql
INSERT INTO users (username, email, password_hash, role, status)
VALUES ('admin', 'admin@example.com', '$2a$10$replace_with_bcrypt_hash', 'ADMIN', 'ACTIVE');

INSERT INTO genres (name)
VALUES ('Tiểu thuyết'), ('Khoa học'), ('Kỹ năng'), ('Lịch sử'), ('Công nghệ');
```

Sau đó thêm sách mẫu bằng giao diện Admin để test upload file và ảnh bìa.

---

## 16. Roadmap ngắn

```text
Week 1:
- Init project
- Database
- Auth

Week 2:
- Admin book management
- Home/search/detail

Week 3:
- Library
- Reader PDF
- Reading progress

Week 4:
- Review
- Quote
- User statistics

Week 5:
- Admin dashboard
- Admin user/review management
- Notification
- UI polish
- Testing
```

---

## 17. Kết luận

Cách làm an toàn nhất là đi từ dưới lên:

```text
Database → Model → DAO → Service → Controller → FXML UI
```

Không nên bắt đầu bằng việc kéo thả giao diện quá sớm.  
Khi database và service đã ổn, việc gắn UI JavaFX vào sẽ dễ hơn nhiều, ít lỗi hơn và bám sát thiết kế OOAD hơn.
