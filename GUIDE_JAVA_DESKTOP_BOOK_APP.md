# GUIDE_JAVA_DESKTOP_BOOK_APP

> Hướng dẫn triển khai bài tập lớn **BTL_Book_Reading_App**  
> Chủ đề: **Ứng dụng quản lý đọc sách trên máy tính bằng Java Desktop**  
> Công nghệ chốt hiện tại: **NetBeans + Maven + Java Swing + FlatLaf + SQL Server + JDBC**

---

## 0. Trạng thái hiện tại

### Đã chốt

- Tên repo/thư mục cha: `Reading_App_Project`
- Project NetBeans: `BTL_Book_Reading_App`
- Package chính hiện tại: `com.mycompany.btl_book_reading_app`
- Main class hiện tại: `com.mycompany.btl_book_reading_app.BTL_Book_Reading_App`
- Database: `BookReadingDB`
- UI chính: **Java Swing**
- Theme UI: **FlatLaf**
- Database: **SQL Server**
- Kết nối DB: **JDBC**
- Build/dependency: **Maven**
- IDE chính: **Apache NetBeans**

### Cấu trúc thư mục repo hiện tại

```text
Reading_App_Project/
├── .git/
├── BTL_Book_Reading_App/
│   ├── pom.xml
│   └── src/
├── database/
│   ├── schema.sql
│   └── seed.sql
├── GUIDE_JAVA_DESKTOP_BOOK_APP.md
└── .gitignore
```

---

## 1. Mục tiêu bài tập lớn

Xây dựng lại ứng dụng đọc sách từ bản thiết kế OOAD thành app Java Desktop.

Ứng dụng cần có hai nhóm người dùng chính:

- **User**: người đọc sách
- **Admin**: quản trị viên

Các nhóm chức năng chính:

| Mã | Nhóm chức năng | Mô tả |
|---|---|---|
| UC01 | Quản lý xác thực | Đăng nhập, đăng ký |
| UC02 | Quản lý thông tin cá nhân | Xem/cập nhật hồ sơ, đổi mật khẩu, đăng xuất |
| UC03 | Quản lý đọc sách | Tìm kiếm sách, xem chi tiết, đọc sách, thêm vào thư viện |
| UC04 | Thống kê Admin | Tổng sách, thành viên, bình luận, trích dẫn, top sách |
| UC05 | Quản lý thư viện cá nhân | Sách đã lưu, trạng thái đọc, tiến độ đọc |
| UC06 | Thống kê người đọc | Tổng sách, tổng trang, trạng thái đọc, top thể loại |
| UC07 | Quản lý trích dẫn | Thêm, xem, xóa trích dẫn |
| UC08 | Quản lý tài khoản | Admin xem, tìm, khóa, xóa user |
| UC09 | Quản lý sách | Admin thêm, sửa, xóa, tìm kiếm sách |
| UC10 | Quản lý bình luận/đánh giá | Admin xem/xóa bình luận vi phạm |

---

## 2. Kiến trúc triển khai

Giữ đúng tinh thần mô hình trong báo cáo:

```text
View Swing
   ↓
Controller / Event Handler
   ↓
Service
   ↓
DAO
   ↓
SQL Server Database
```

Trong code Java:

```text
view      → JFrame/JPanel giao diện
service   → xử lý nghiệp vụ
dao       → truy vấn SQL bằng JDBC
model     → class dữ liệu
config    → cấu hình database
util      → tiện ích dùng chung
```

Cấu trúc package đề xuất:

```text
src/main/java/com/mycompany/btl_book_reading_app/
├── BTL_Book_Reading_App.java
├── config/
│   └── DatabaseConnection.java
├── model/
│   ├── User.java
│   ├── Book.java
│   ├── Genre.java
│   ├── ReadingProcess.java
│   ├── Review.java
│   ├── Quote.java
│   └── Notification.java
├── dao/
│   ├── UserDAO.java
│   ├── BookDAO.java
│   ├── GenreDAO.java
│   ├── ReadingProcessDAO.java
│   ├── ReviewDAO.java
│   ├── QuoteDAO.java
│   └── NotificationDAO.java
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   ├── BookService.java
│   ├── LibraryService.java
│   ├── ReadingService.java
│   ├── ReviewService.java
│   ├── QuoteService.java
│   ├── StatisticsService.java
│   └── AdminService.java
├── view/
│   ├── LoginFrame.java
│   ├── RegisterFrame.java
│   ├── UserHomeFrame.java
│   ├── AdminDashboardFrame.java
│   ├── BookManagementFrame.java
│   ├── LibraryFrame.java
│   ├── BookDetailFrame.java
│   ├── ReaderFrame.java
│   ├── QuoteFrame.java
│   └── StatisticsFrame.java
└── util/
    ├── PasswordUtil.java
    ├── ValidationUtil.java
    ├── SessionManager.java
    └── FileStorageUtil.java
```

---

## 3. Maven dependencies

Trong `BTL_Book_Reading_App/pom.xml`, dùng các thư viện chính:

```xml
<dependencies>
    <!-- SQL Server JDBC Driver -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <version>12.4.2.jre11</version>
    </dependency>

    <!-- Modern Look & Feel for Swing -->
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf</artifactId>
        <version>3.6</version>
    </dependency>

    <!-- Layout manager for Swing -->
    <dependency>
        <groupId>com.miglayout</groupId>
        <artifactId>miglayout-swing</artifactId>
        <version>11.4.2</version>
    </dependency>

    <!-- Password hashing -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>

    <!-- PDF processing later -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>3.0.3</version>
    </dependency>
</dependencies>
```

Lưu ý:

- **Swing có sẵn trong JDK**, không cần tải riêng.
- FlatLaf dùng để làm giao diện Swing hiện đại hơn.
- MigLayout giúp layout form dễ hơn.
- BCrypt dùng để mã hóa mật khẩu.
- PDFBox dùng cho chức năng đọc/xử lý PDF về sau.

---

## 4. Database

Database chính:

```sql
CREATE DATABASE BookReadingDB;
GO

USE BookReadingDB;
GO
```

Các bảng chính:

| Bảng | Vai trò |
|---|---|
| `Users` | Lưu thông tin user/admin |
| `Genres` | Lưu thể loại sách |
| `Books` | Lưu thông tin sách |
| `ReadingProcess` | Lưu thư viện cá nhân + tiến độ đọc |
| `Reviews` | Lưu đánh giá/bình luận |
| `Quotes` | Lưu trích dẫn |
| `Notifications` | Lưu nhắc đọc sách |

File script:

```text
database/schema.sql
database/seed.sql
```

Quy trình chạy:

1. Mở SQL Server Management Studio hoặc Azure Data Studio.
2. Chạy `schema.sql`.
3. Chạy `seed.sql`.
4. Test:

```sql
USE BookReadingDB;
GO

SELECT * FROM Users;
SELECT * FROM Genres;
SELECT * FROM Books;
SELECT * FROM ReadingProcess;
SELECT * FROM Reviews;
SELECT * FROM Quotes;
```

---

## 5. Kết nối database từ Java

Tạo package:

```text
com.mycompany.btl_book_reading_app.config
```

Tạo file:

```text
DatabaseConnection.java
```

Code mẫu:

```java
package com.mycompany.btl_book_reading_app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=BookReadingDB;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

Cần sửa:

```java
private static final String PASSWORD = "your_password";
```

thành mật khẩu SQL Server thật trên máy.

---

## 6. Git workflow

Repo đang nằm ở thư mục cha:

```text
Reading_App_Project/
```

Không tạo git lại trong thư mục project con.

### Kiểm tra trạng thái

```bash
git status
```

### Commit theo milestone

```bash
git add .
git commit -m "Add database schema"
git push
```

### `.gitignore`

Đặt ở thư mục `Reading_App_Project/.gitignore`:

```gitignore
# Maven / Java build
target/
*.class
*.log

# NetBeans private files
nbproject/private/
build/
dist/

# Local environment
.env

# App local storage
data/books/
data/covers/

# IDE
.idea/
.vscode/

# OS
.DS_Store
Thumbs.db
```

---

# 7. Milestone Tracker

## Tổng quan tiến độ

| Milestone | Nội dung | Trạng thái |
|---|---|---|
| M0 | Chuẩn bị project, Maven, Git | Hoàn thành |
| M1 | Tạo database SQL Server | Hoàn thành |
| M2 | Kết nối Java ↔ SQL Server | Hoàn thành |
| M3 | Đăng nhập/đăng ký | Hoàn thành |
| M4 | Giao diện chính User/Admin | Hoàn thành |
| M5 | Admin quản lý sách | Hoàn thành |
| M6 | User tìm kiếm/xem chi tiết sách | Đang làm |
| M7 | Thư viện cá nhân + tiến độ đọc | Chưa làm |
| M8 | Đọc PDF cơ bản | Chưa làm |
| M9 | Đánh giá + bình luận | Chưa làm |
| M10 | Trích dẫn | Chưa làm |
| M11 | Thống kê User | Chưa làm |
| M12 | Thống kê Admin | Chưa làm |
| M13 | Quản lý user/bình luận Admin | Chưa làm |
| M14 | Hoàn thiện UI + test + báo cáo | Chưa làm |

---

## M0. Chuẩn bị project, Maven, Git

### Mục tiêu

Tạo project NetBeans Maven và chạy được cửa sổ Swing mẫu.

### Checklist

- [x] Tạo project `BTL_Book_Reading_App`
- [x] Có repo Git ở thư mục cha `Reading_App_Project`
- [x] Sửa `pom.xml`
- [x] Maven `Clean and Build` thành công
- [x] Thêm FlatLaf, MigLayout, JDBC, BCrypt, PDFBox
- [x] Tạo `.gitignore` ở thư mục cha
- [x] Commit milestone M0

### Commit gợi ý

```bash
git add .
git commit -m "Initialize BTL Book Reading App project"
git push
```

---

## M1. Tạo database SQL Server

### Mục tiêu

Tạo database `BookReadingDB` và các bảng chính.

### Checklist

- [x] Tạo folder `database/`
- [x] Tạo `database/schema.sql`
- [x] Tạo `database/seed.sql`
- [x] Tạo database `BookReadingDB`
- [x] Tạo bảng `Users`
- [x] Tạo bảng `Genres`
- [x] Tạo bảng `Books`
- [x] Tạo bảng `ReadingProcess`
- [x] Tạo bảng `Reviews`
- [x] Tạo bảng `Quotes`
- [x] Tạo bảng `Notifications`
- [x] Insert dữ liệu mẫu
- [x] Test bằng `SELECT * FROM Users`
- [x] Commit milestone M1

### Commit gợi ý

```bash
git add database/
git commit -m "Add SQL Server database schema and seed data"
git push
```

---

## M2. Kết nối Java với SQL Server

### Mục tiêu

Java app kết nối được tới `BookReadingDB`.

### Checklist

- [x] Tạo package `config`
- [x] Tạo `DatabaseConnection.java`
- [x] Sửa username/password SQL Server
- [x] Test `SELECT COUNT(*) FROM Users`
- [x] In ra console kết nối thành công
- [x] Commit milestone M2

### Commit gợi ý

```bash
git add .
git commit -m "Add SQL Server database connection"
git push
```

---

## M3. Đăng nhập/đăng ký

### Mục tiêu

Có chức năng đăng nhập và đăng ký tài khoản.

### File cần tạo

```text
model/User.java
dao/UserDAO.java
service/AuthService.java
util/PasswordUtil.java
view/LoginFrame.java
view/RegisterFrame.java
```

### Checklist

- [x] Tạo model `User`
- [x] Tạo `PasswordUtil` dùng BCrypt
- [x] Tạo `UserDAO.findByEmail()`
- [x] Tạo `UserDAO.insertUser()`
- [x] Tạo `AuthService.login()`
- [x] Tạo `AuthService.register()`
- [x] Tạo giao diện `LoginFrame`
- [x] Tạo giao diện `RegisterFrame`
- [x] Login phân biệt `USER` và `ADMIN`
- [x] Commit milestone M3

### Commit gợi ý

```bash
git add .
git commit -m "Implement login and register module"
git push
```

---

## M4. Giao diện chính User/Admin

### Mục tiêu

Sau khi đăng nhập, user/admin vào đúng màn hình.

### File cần tạo

```text
view/UserHomeFrame.java
view/AdminDashboardFrame.java
util/SessionManager.java
```

### Checklist

- [x] Tạo `SessionManager`
- [x] Login USER mở `UserHomeFrame`
- [x] Login ADMIN mở `AdminDashboardFrame`
- [x] Có nút đăng xuất
- [x] Có menu điều hướng
- [x] Commit milestone M4

### Commit gợi ý

```bash
git add .
git commit -m "Add user and admin main screens"
git push
```

---

## M5. Admin quản lý sách

### Mục tiêu

Admin thêm/sửa/xóa/tìm kiếm sách.

### File cần tạo

```text
model/Book.java
model/Genre.java
dao/BookDAO.java
dao/GenreDAO.java
service/BookService.java
view/BookManagementFrame.java
```

### Checklist

- [x] Hiển thị danh sách sách bằng `JTable`
- [x] Thêm sách mới
- [x] Sửa thông tin sách
- [x] Xóa sách
- [x] Tìm kiếm sách
- [ ] Chọn ảnh bìa
- [ ] Chọn file PDF/EPUB
- [ ] Lưu đường dẫn file vào DB
- [ ] Commit milestone M5

### Commit gợi ý

```bash
git add .
git commit -m "Implement admin book management"
git push
```

---

## M6. User tìm kiếm/xem chi tiết sách

### Mục tiêu

User xem kho sách, tìm kiếm sách, xem chi tiết và thêm sách vào thư viện.

### File cần tạo

```text
view/SearchBookPanel.java
view/BookDetailFrame.java
service/LibraryService.java
dao/ReadingProcessDAO.java
```

### Checklist

- [ ] Hiển thị danh sách sách
- [ ] Tìm theo tên sách
- [ ] Tìm theo tác giả
- [ ] Tìm theo thể loại
- [ ] Xem chi tiết sách
- [ ] Thêm vào thư viện
- [ ] Nếu sách đã có thì báo lỗi
- [ ] Commit milestone M6

### Commit gợi ý

```bash
git add .
git commit -m "Add book search and detail features"
git push
```

---

## M7. Thư viện cá nhân + tiến độ đọc

### Mục tiêu

User quản lý sách đã thêm và lưu tiến độ đọc.

### File cần tạo

```text
model/ReadingProcess.java
dao/ReadingProcessDAO.java
service/ReadingService.java
view/LibraryFrame.java
```

### Checklist

- [ ] Hiển thị sách trong thư viện cá nhân
- [ ] Tìm kiếm trong thư viện
- [ ] Cập nhật trạng thái đọc
- [ ] Cập nhật trang hiện tại
- [ ] Xóa sách khỏi thư viện
- [ ] Lưu tracker
- [ ] Commit milestone M7

### Commit gợi ý

```bash
git add .
git commit -m "Implement personal library and reading progress"
git push
```

---

## M8. Đọc PDF cơ bản

### Mục tiêu

Mở file PDF và lưu vị trí đọc.

### File cần tạo

```text
view/ReaderFrame.java
service/PdfReaderService.java
```

### Checklist

- [ ] Mở file PDF từ `file_path`
- [ ] Hiển thị trang hiện tại
- [ ] Chuyển trang trước/sau
- [ ] Lưu `current_page`
- [ ] Tự mở lại trang gần nhất
- [ ] Commit milestone M8

### Commit gợi ý

```bash
git add .
git commit -m "Add basic PDF reader"
git push
```

---

## M9. Đánh giá + bình luận

### Mục tiêu

User đánh giá sách, admin xem/xóa bình luận.

### File cần tạo

```text
model/Review.java
dao/ReviewDAO.java
service/ReviewService.java
view/ReviewPanel.java
view/AdminReviewFrame.java
```

### Checklist

- [ ] User đánh giá sao 1-5
- [ ] User viết bình luận
- [ ] Cập nhật `avg_rating`
- [ ] Admin xem danh sách bình luận
- [ ] Admin xóa bình luận
- [ ] Commit milestone M9

### Commit gợi ý

```bash
git add .
git commit -m "Implement book reviews and comments"
git push
```

---

## M10. Trích dẫn

### Mục tiêu

User lưu, xem, xóa trích dẫn.

### File cần tạo

```text
model/Quote.java
dao/QuoteDAO.java
service/QuoteService.java
view/QuoteFrame.java
```

### Checklist

- [ ] Thêm trích dẫn
- [ ] Xem danh sách trích dẫn
- [ ] Xóa trích dẫn
- [ ] Gắn trích dẫn với user và book
- [ ] Commit milestone M10

### Commit gợi ý

```bash
git add .
git commit -m "Implement quote management"
git push
```

---

## M11. Thống kê User

### Mục tiêu

User xem thống kê quá trình đọc.

### File cần tạo

```text
service/StatisticsService.java
view/UserStatisticsFrame.java
```

### Checklist

- [ ] Tổng số sách
- [ ] Số sách theo trạng thái
- [ ] Tổng số trang đã đọc
- [ ] Tác giả đọc nhiều nhất
- [ ] Top thể loại
- [ ] Sách đánh giá cao nhất
- [ ] Commit milestone M11

### Commit gợi ý

```bash
git add .
git commit -m "Add user reading statistics"
git push
```

---

## M12. Thống kê Admin

### Mục tiêu

Admin xem dashboard tổng quan.

### Checklist

- [ ] Tổng đầu sách
- [ ] Tổng thành viên
- [ ] Tổng bình luận
- [ ] Tổng trích dẫn
- [ ] Top 5 sách đánh giá cao
- [ ] Phân bố thể loại
- [ ] Top người đọc nhiều nhất
- [ ] Commit milestone M12

### Commit gợi ý

```bash
git add .
git commit -m "Add admin dashboard statistics"
git push
```

---

## M13. Quản lý user Admin

### Mục tiêu

Admin quản lý tài khoản user.

### File cần tạo

```text
view/AdminUserFrame.java
service/AdminService.java
```

### Checklist

- [ ] Xem danh sách user
- [ ] Tìm kiếm user
- [ ] Khóa user
- [ ] Mở khóa user
- [ ] Xóa user
- [ ] Không cho admin tự xóa mình
- [ ] Commit milestone M13

### Commit gợi ý

```bash
git add .
git commit -m "Implement admin user management"
git push
```

---

## M14. Hoàn thiện UI + test + báo cáo

### Mục tiêu

Hoàn thiện demo và tài liệu.

### Checklist

- [ ] Làm đẹp UI bằng FlatLaf
- [ ] Kiểm tra toàn bộ luồng user
- [ ] Kiểm tra toàn bộ luồng admin
- [ ] Bổ sung ảnh chụp màn hình
- [ ] Viết README
- [ ] Cập nhật báo cáo triển khai
- [ ] Commit final

### Commit gợi ý

```bash
git add .
git commit -m "Finalize desktop book reading app"
git push
```

---

# 8. Quy tắc làm việc

## Mỗi lần làm chỉ tập trung một milestone

Không nhảy từ login sang thống kê nếu database chưa xong.

Thứ tự ưu tiên hiện tại:

```text
M0 → M1 → M2 → M3 → M4 → M5 → M6 → M7 → M8 → M9 → M10 → M11 → M12 → M13 → M14
```

## Commit sau mỗi milestone

Mỗi milestone xong phải commit ngay để dễ quay lại nếu lỗi.

## Không đẩy file nặng lên Git

Không đẩy:

```text
target/
data/books/
data/covers/
*.pdf
*.epub
```

Nếu cần demo sách mẫu, để riêng link hoặc dùng file nhỏ.

---

# 9. Việc cần làm tiếp theo

Trạng thái hiện tại đang ở:

```text
M1 - Tạo database SQL Server
```

Việc cần làm ngay:

1. Tạo folder `database/`.
2. Tạo `database/schema.sql`.
3. Tạo `database/seed.sql`.
4. Chạy script trong SQL Server.
5. Test `SELECT * FROM Users`.
6. Commit milestone M1.

Sau khi M1 xong, chuyển sang:

```text
M2 - Kết nối Java với SQL Server
```
