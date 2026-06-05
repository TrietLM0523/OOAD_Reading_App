# GUIDE.md — BTL Book Reading App

## 1. Mục đích của guide này

File này là guide triển khai cho project `BTL_Book_Reading_App` sau khi rút gọn phạm vi để phù hợp với tiến độ hiện tại.

Điểm quan trọng:

> Bản guide này **không thay thế hoàn toàn thiết kế OOAD ban đầu**.  
> Nó là bản hướng dẫn triển khai rút gọn, vẫn kế thừa các entity, use case và nghiệp vụ chính đã thống nhất giữ lại từ thiết kế cũ.

Hướng triển khai hiện tại:

```text
Java Swing UI → Service Layer → DAO Layer → JDBC → SQL Server
```

Thiết kế cũ đóng vai trò là nền tảng phân tích nghiệp vụ.  
Bản guide này đóng vai trò là kế hoạch triển khai code sao cho vẫn trace được về thiết kế đó.

---

## 2. Nguyên tắc rút gọn

Rút gọn ở đây nghĩa là:

- Giảm số lượng use case phải demo.
- Đưa một số chức năng phụ sang phần mở rộng.
- Đổi công nghệ triển khai từ mobile/cloud sang desktop/database quan hệ.
- Không làm mất các entity và nghiệp vụ lõi của hệ thống đọc sách.

Không được hiểu rút gọn là:

- Bỏ luôn chức năng đọc sách.
- Bỏ luôn tiến trình đọc.
- Biến app thành app quản lý sách đơn thuần.
- Xóa hết các entity cũ khỏi thiết kế.

---

## 3. Mapping từ thiết kế cũ sang triển khai hiện tại

| Thiết kế OOAD ban đầu | Triển khai hiện tại |
|---|---|
| User | Bảng `Users`, `UserDAO`, `UserService`, `LoginFrame`, `RegisterFrame` |
| Book | Bảng `Books`, `BookDAO`, `BookService`, `BookListPanel`, `BookManagementPanel` |
| Genre | Bảng `Genres`, `GenreDAO`, dùng để phân loại sách |
| ReadingProcess | Bảng `ReadingProcesses`, `ReadingProcessDAO`, `ReadingProcessService`, dùng để lưu tiến trình đọc |
| Review | Bảng `Reviews`, có thể để mức mở rộng nếu không kịp code |
| Quote | Bảng `Quotes`, có thể để mức mở rộng nếu không kịp code |
| Notification | Bảng `Notifications`, có thể để mức mở rộng nếu không kịp code |
| React Native UI | Java Swing UI |
| Firebase Authentication | Xử lý đăng nhập bằng `Users` trong SQL Server |
| Firestore Database | SQL Server |
| Cloud Storage | Lưu file local, database chỉ lưu `filePath`, `coverPath` |
| Firebase SDK | DAO/JDBC |

---

## 4. Use case đã thống nhất giữ sau rút gọn

Sau khi rút gọn, use case nên chia thành 2 nhóm.

### 4.1. Core use case bắt buộc nên triển khai

| Mã | Use case | Actor | Lý do giữ |
|---|---|---|---|
| UC01 | Đăng ký tài khoản | User | Cần có user để dùng hệ thống |
| UC02 | Đăng nhập | User/Admin | Cần xác thực để vào app |
| UC03 | Xem danh sách sách | User | Chức năng nền của app đọc sách |
| UC04 | Đọc/mở sách | User | Chức năng chính, tránh app chỉ là quản lý sách |
| UC05 | Lưu tiến trình đọc | User | Bám sát entity `ReadingProcess` trong thiết kế cũ |
| UC06 | Quản lý sách | Admin | Cho phép thêm/xóa/sửa sách |
| UC07 | Quản lý thể loại | Admin | Hỗ trợ entity `Genre`, có thể làm đơn giản |
| UC08 | Phân quyền User/Admin | System | Để tách user thường và admin |

### 4.2. Extended use case có thể đưa vào mở rộng

| Mã | Use case | Entity liên quan | Cách xử lý nếu không kịp |
|---|---|---|---|
| UC09 | Viết đánh giá sách | Review | Giữ bảng trong DB, ghi là hướng phát triển |
| UC10 | Lưu trích dẫn | Quote | Giữ bảng trong DB, ghi là hướng phát triển |
| UC11 | Nhận thông báo | Notification | Giữ bảng trong DB, ghi là hướng phát triển |
| UC12 | Tìm kiếm nâng cao | Book, Genre | Có thể làm search cơ bản nếu kịp |
| UC13 | Gợi ý sách | Book, Review | Để hướng phát triển |

Với tiến độ hiện tại, app cần cố gắng hoàn thành chắc nhóm core use case trước.

---

## 5. Kiểm tra milestone có cover đủ use case không

Bảng này là phần quan trọng nhất để đảm bảo milestone không lệch kế hoạch.

| Milestone | Nội dung | Use case được cover | Entity/bảng liên quan | Trạng thái kỳ vọng |
|---|---|---|---|---|
| M1 | Khởi tạo project Maven + Swing + FlatLaf | Nền tảng cho toàn bộ use case | Chưa cần DB | Bắt buộc xong |
| M2 | Tạo database + `DBConnection` | Nền tảng cho UC01-UC08 | Tất cả bảng chính | Bắt buộc xong |
| M3 | Tạo model + DAO + service nền | Nền tảng cho UC01-UC08 | `Users`, `Books`, `Genres`, `ReadingProcesses` | Bắt buộc xong |
| M4 | Đăng ký | UC01 | `Users` | Bắt buộc xong |
| M5 | Đăng nhập | UC02 | `Users` | Bắt buộc xong |
| M6 | MainFrame + điều hướng + session user | UC02, UC03, UC06, UC08 | `Users` | Bắt buộc xong |
| M7 | Quản lý sách + thể loại cơ bản | UC03, UC06, UC07 | `Books`, `Genres` | Bắt buộc |
| M8 | Chọn file sách PDF/EPUB/TXT khi thêm sách | UC04, UC06 | `Books.filePath`, `Books.fileType` | Bắt buộc nếu muốn app đúng hướng đọc sách |
| M9 | Đọc/mở sách + lưu tiến trình | UC04, UC05 | `ReadingProcesses` | Rất quan trọng |
| M10 | Phân quyền và hoàn thiện UI | UC08, hỗ trợ UC06 | `Users.role`, `Users.status` | Bắt buộc ở mức cơ bản |
| M11 | Review/Quote/Notification đơn giản hoặc để mở rộng | UC09-UC11 | `Reviews`, `Quotes`, `Notifications` | Có thể optional |
| M12 | Dọn code + demo + báo cáo | Tất cả use case đã làm | Toàn hệ thống | Bắt buộc |

Kết luận audit:

> Nếu chỉ làm tới M7 thì app mới giống quản lý sách.  
> Để đúng thiết kế app đọc sách, tối thiểu phải có thêm M8 và M9: **lưu file sách + mở/đọc sách + lưu tiến trình đọc**.

---

## 6. Cấu trúc project đề xuất

```text
BTL_Book_Reading_App/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── mycompany/
│       │           └── btl_book_reading_app/
│       │               │
│       │               ├── config/
│       │               │   └── DBConnection.java
│       │               │
│       │               ├── dao/
│       │               │   ├── UserDAO.java
│       │               │   ├── BookDAO.java
│       │               │   ├── GenreDAO.java
│       │               │   ├── ReadingProcessDAO.java
│       │               │   ├── ReviewDAO.java
│       │               │   ├── QuoteDAO.java
│       │               │   └── NotificationDAO.java
│       │               │
│       │               ├── model/
│       │               │   ├── User.java
│       │               │   ├── Book.java
│       │               │   ├── Genre.java
│       │               │   ├── ReadingProcess.java
│       │               │   ├── Review.java
│       │               │   ├── Quote.java
│       │               │   └── Notification.java
│       │               │
│       │               ├── service/
│       │               │   ├── UserService.java
│       │               │   ├── BookService.java
│       │               │   ├── GenreService.java
│       │               │   └── ReadingProcessService.java
│       │               │
│       │               ├── ui/
│       │               │   ├── LoginFrame.java
│       │               │   ├── RegisterFrame.java
│       │               │   ├── MainFrame.java
│       │               │   └── panels/
│       │               │       ├── BookListPanel.java
│       │               │       ├── BookManagementPanel.java
│       │               │       ├── GenreManagementPanel.java
│       │               │       └── ReadingPanel.java
│       │               │
│       │               └── Main.java
│       │
│       └── resources/
│           ├── covers/
│           └── books/
│
├── database/
│   ├── create_database.sql
│   └── sample_data.sql
│
├── pom.xml
└── README.md
```

Lưu ý:

- `ReviewDAO`, `QuoteDAO`, `NotificationDAO` có thể chưa code ngay, nhưng nếu đã giữ entity trong thiết kế thì nên để trong kế hoạch hoặc phần mở rộng.
- Core bắt buộc nhất vẫn là `UserDAO`, `BookDAO`, `GenreDAO`, `ReadingProcessDAO`.

---

## 7. Database schema nên dùng

### 7.1. Users

```sql
CREATE TABLE Users (
    idUser INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    email NVARCHAR(100) NOT NULL UNIQUE,
    passwordHash NVARCHAR(255) NOT NULL,
    fullName NVARCHAR(100),
    role NVARCHAR(20) NOT NULL DEFAULT 'USER',
    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    createdAt DATETIME DEFAULT GETDATE()
);
```

### 7.2. Genres

```sql
CREATE TABLE Genres (
    idGenre INT IDENTITY(1,1) PRIMARY KEY,
    genreName NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(255)
);
```

### 7.3. Books

```sql
CREATE TABLE Books (
    idBook INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    author NVARCHAR(150),
    description NVARCHAR(MAX),
    idGenre INT,
    coverPath NVARCHAR(255),
    filePath NVARCHAR(255),
    fileType NVARCHAR(20),
    totalPages INT DEFAULT 0,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME,
    FOREIGN KEY (idGenre) REFERENCES Genres(idGenre)
);
```

### 7.4. ReadingProcesses

```sql
CREATE TABLE ReadingProcesses (
    idReadingProcess INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    currentPage INT DEFAULT 0,
    progressPercent FLOAT DEFAULT 0,
    rereadCount INT DEFAULT 0,
    lastReadAt DATETIME,
    updatedAt DATETIME DEFAULT GETDATE(),
    UNIQUE(idUser, idBook),
    FOREIGN KEY (idUser) REFERENCES Users(idUser),
    FOREIGN KEY (idBook) REFERENCES Books(idBook)
);
```

### 7.5. Reviews

```sql
CREATE TABLE Reviews (
    idReview INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    rating INT,
    comment NVARCHAR(MAX),
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME,
    UNIQUE(idUser, idBook),
    FOREIGN KEY (idUser) REFERENCES Users(idUser),
    FOREIGN KEY (idBook) REFERENCES Books(idBook)
);
```

### 7.6. Quotes

```sql
CREATE TABLE Quotes (
    idQuote INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    idBook INT NOT NULL,
    quoteText NVARCHAR(MAX) NOT NULL,
    pageNumber INT,
    createdAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (idUser) REFERENCES Users(idUser),
    FOREIGN KEY (idBook) REFERENCES Books(idBook)
);
```

### 7.7. Notifications

```sql
CREATE TABLE Notifications (
    idNotification INT IDENTITY(1,1) PRIMARY KEY,
    idUser INT NOT NULL,
    title NVARCHAR(200),
    message NVARCHAR(MAX),
    isRead BIT DEFAULT 0,
    createdAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (idUser) REFERENCES Users(idUser)
);
```

---

## 8. Chi tiết milestone

## M1 — Khởi tạo project

Mục tiêu:

- Tạo project Maven trong NetBeans.
- Cấu hình `pom.xml`.
- Thêm SQL Server JDBC Driver.
- Thêm FlatLaf.
- Tạo package cơ bản.

Checklist:

- [ ] Project chạy được.
- [ ] `Main.java` mở được frame đầu tiên.
- [ ] Không lỗi Maven dependency.

Use case cover:

- Chưa cover trực tiếp use case nào.
- Là nền tảng cho toàn bộ project.

---

## M2 — Tạo database và kết nối SQL Server

Mục tiêu:

- Tạo database SQL Server.
- Tạo các bảng chính.
- Tạo `DBConnection.java`.
- Test kết nối thành công.

Checklist:

- [ ] Có file `create_database.sql`.
- [ ] Có file `sample_data.sql`.
- [ ] `DBConnection.getConnection()` chạy được.
- [ ] Không hardcode thông tin nhạy cảm nếu đẩy GitHub.

Use case cover:

- Nền tảng cho UC01-UC08.

---

## M3 — Model, DAO, Service nền

Mục tiêu:

Tạo model:

- `User`
- `Book`
- `Genre`
- `ReadingProcess`

Tạo DAO:

- `UserDAO`
- `BookDAO`
- `GenreDAO`
- `ReadingProcessDAO`

Tạo service:

- `UserService`
- `BookService`
- `GenreService`
- `ReadingProcessService`

Checklist:

- [ ] Model có đủ field theo database.
- [ ] DAO dùng `PreparedStatement`.
- [ ] Service kiểm tra nghiệp vụ trước khi gọi DAO.
- [ ] Không gọi SQL trực tiếp từ UI.

Use case cover:

- Nền tảng cho UC01-UC08.

---

## M4 — Đăng ký tài khoản

Mục tiêu:

- Người dùng nhập username, email, password.
- Hệ thống kiểm tra dữ liệu rỗng.
- Hệ thống kiểm tra username/email đã tồn tại.
- Tạo user mới trong bảng `Users`.

Luồng:

```text
RegisterFrame
  → UserService.register()
  → UserDAO.existsByUsernameOrEmail()
  → UserDAO.insert()
  → SQL Server
```

Checklist:

- [ ] Đăng ký thành công ghi thật vào DB.
- [ ] Trùng email/username thì báo lỗi.
- [ ] Input rỗng thì báo lỗi.
- [ ] Sau khi đăng ký có thể quay về đăng nhập.

Use case cover:

- UC01 Đăng ký tài khoản.

---

## M5 — Đăng nhập

Mục tiêu:

- Người dùng nhập username/email và password.
- Hệ thống kiểm tra tài khoản.
- Hệ thống kiểm tra mật khẩu.
- Hệ thống kiểm tra `status = ACTIVE`.
- Đăng nhập thành công thì mở `MainFrame`.

Luồng:

```text
LoginFrame
  → UserService.login()
  → UserDAO.findByUsernameOrEmail()
  → SQL Server
  → MainFrame
```

Checklist:

- [ ] Đăng nhập đúng mở được MainFrame.
- [ ] Sai mật khẩu báo lỗi.
- [ ] Tài khoản không tồn tại báo lỗi.
- [ ] Tài khoản LOCKED không được đăng nhập.
- [ ] Lưu được thông tin user đang đăng nhập.

Use case cover:

- UC02 Đăng nhập.

---

## M6 — MainFrame, session user và điều hướng

Mục tiêu:

- Tạo màn hình chính sau đăng nhập.
- Hiển thị thông tin user.
- Có menu hoặc sidebar điều hướng.
- Điều hướng tới danh sách sách, quản lý sách, đọc sách.
- Phân biệt user thường và admin ở mức cơ bản.

Checklist:

- [ ] MainFrame nhận được object `User currentUser`.
- [ ] User thường thấy chức năng đọc/xem sách.
- [ ] Admin thấy thêm chức năng quản lý sách.
- [ ] Không hiện popup “làm ở milestone sau” cho chức năng đã hoàn thành.

Use case cover:

- UC02 Đăng nhập.
- UC03 Xem danh sách sách.
- UC06 Quản lý sách.
- UC08 Phân quyền.

---

## M7 — Quản lý sách và thể loại cơ bản

Mục tiêu:

- Hiển thị danh sách sách.
- Thêm sách.
- Xóa sách.
- Cập nhật sách nếu kịp.
- Quản lý thể loại ở mức cơ bản.

Luồng thêm sách:

```text
BookManagementPanel
  → BookService.addBook()
  → BookDAO.insert()
  → SQL Server
  → reload table
```

Luồng xóa sách:

```text
BookManagementPanel
  → BookService.deleteBook()
  → BookDAO.deleteById()
  → SQL Server
  → reload table
```

Checklist:

- [ ] Bảng sách load từ SQL Server.
- [ ] Thêm sách ghi thật vào bảng `Books`.
- [ ] Xóa sách xóa thật khỏi bảng `Books`.
- [ ] Reload table sau khi thêm/xóa.
- [ ] Có combobox thể loại nếu đã làm `Genres`.
- [ ] Không cho user thường thao tác quản lý sách.

Use case cover:

- UC03 Xem danh sách sách.
- UC06 Quản lý sách.
- UC07 Quản lý thể loại.
- UC08 Phân quyền.

---

## M8 — Chọn file sách PDF/EPUB/TXT khi thêm sách

Mục tiêu:

- Khi thêm sách, admin có thể chọn file sách từ máy.
- App lưu file vào thư mục project hoặc lưu path.
- Database lưu `filePath` và `fileType`.

Đây là milestone quan trọng để nối M7 quản lý sách với M9 đọc sách.

Luồng:

```text
Admin
  → BookManagementPanel
  → JFileChooser
  → Chọn file PDF/EPUB/TXT
  → Copy file vào src/main/resources/books/
  → Lưu filePath, fileType vào Books
  → SQL Server
```

Checklist:

- [ ] Có nút chọn file sách.
- [ ] Chỉ nhận `.pdf`, `.epub`, `.txt`.
- [ ] Lưu được `filePath`.
- [ ] Lưu được `fileType`.
- [ ] Khi load bảng sách có thể thấy sách nào có file.
- [ ] Nếu chưa chọn file thì xử lý được, không crash.

Use case cover:

- UC04 Đọc/mở sách.
- UC06 Quản lý sách.

Code chọn file:

```java
private String selectedBookFilePath;
private String selectedBookFileType;

private void chooseBookFile() {
    JFileChooser fileChooser = new JFileChooser();

    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Book files (*.pdf, *.epub, *.txt)",
            "pdf", "epub", "txt"
    );

    fileChooser.setFileFilter(filter);

    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        selectedBookFilePath = selectedFile.getAbsolutePath();
        selectedBookFileType = getFileExtension(selectedFile.getName()).toUpperCase();

        JOptionPane.showMessageDialog(this, "Đã chọn file: " + selectedFile.getName());
    }
}

private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex == -1) {
        return "";
    }
    return fileName.substring(dotIndex + 1);
}
```

---

## M9 — Đọc/mở sách và lưu tiến trình đọc

Mục tiêu:

- User chọn một sách để đọc.
- App mở file sách dựa trên `filePath`.
- Với TXT có thể đọc trực tiếp trong app.
- Với PDF/EPUB có thể mở bằng phần mềm mặc định.
- Khi user đọc, app lưu tiến trình vào `ReadingProcesses`.
- Khi user mở lại sách, app load lại tiến trình cũ.

Đây là milestone bắt buộc nếu muốn app đúng thiết kế đọc sách.

Luồng mở sách:

```text
BookListPanel
  → chọn sách
  → ReadingPanel
  → BookService.getBookById()
  → open filePath
```

Luồng lưu tiến trình:

```text
ReadingPanel
  → ReadingProcessService.saveProgress(idUser, idBook, currentPage, progressPercent)
  → ReadingProcessDAO.upsert()
  → SQL Server
```

Checklist:

- [ ] User mở được sách từ danh sách.
- [ ] Nếu file không tồn tại thì báo lỗi rõ ràng.
- [ ] TXT đọc được trong `JTextArea` nếu làm đọc trong app.
- [ ] PDF/EPUB mở được bằng app mặc định nếu chưa nhúng reader.
- [ ] Có nút lưu tiến trình.
- [ ] `ReadingProcesses` có record theo `idUser`, `idBook`.
- [ ] Mở lại sách load được tiến trình cũ.
- [ ] Không tạo trùng tiến trình nhờ `UNIQUE(idUser, idBook)`.

Use case cover:

- UC04 Đọc/mở sách.
- UC05 Lưu tiến trình đọc.

Code mở file bằng app mặc định:

```java
private void openBookFile(String filePath) {
    try {
        File file = new File(filePath);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy file sách.");
            return;
        }

        Desktop.getDesktop().open(file);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Không thể mở file sách: " + e.getMessage());
    }
}
```

Code đọc TXT trực tiếp:

```java
private void loadTxtBook(String filePath) {
    try {
        String content = Files.readString(Path.of(filePath));
        txtBookContent.setText(content);
        txtBookContent.setCaretPosition(0);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Không thể đọc file TXT: " + e.getMessage());
    }
}
```

---

## M10 — Phân quyền và hoàn thiện UI

Mục tiêu:

- Tách user thường và admin.
- Admin được quản lý sách.
- User thường chỉ xem/đọc sách.
- UI ổn định, không còn popup placeholder cho chức năng đã làm.

Checklist:

- [ ] `Users.role` có `USER` và `ADMIN`.
- [ ] MainFrame kiểm tra role.
- [ ] User thường không thấy hoặc không dùng được quản lý sách.
- [ ] Admin thao tác thêm/xóa/sửa sách.
- [ ] LoginFrame dùng `setSize(450, 400)`.
- [ ] RegisterFrame dùng `setSize(480, 500)`.
- [ ] Các lỗi input có popup rõ ràng.

Use case cover:

- UC08 Phân quyền.
- Hỗ trợ UC06 Quản lý sách.

---

## M11 — Review, Quote, Notification ở mức mở rộng

Mục tiêu:

Không bắt buộc phải hoàn thiện nếu thiếu thời gian, nhưng phải giữ trace với thiết kế cũ.

Cách xử lý an toàn:

- Database vẫn có bảng `Reviews`, `Quotes`, `Notifications`.
- Model có thể tạo trước nếu muốn.
- UI có thể để sau.
- Báo cáo ghi là chức năng mở rộng hoặc triển khai sau.

Nếu có thời gian, có thể làm bản đơn giản:

### Review đơn giản

- User chọn sách.
- Nhập rating/comment.
- Lưu vào `Reviews`.
- Mỗi user chỉ có một review cho một sách.

### Quote đơn giản

- User nhập đoạn quote.
- Chọn pageNumber.
- Lưu vào `Quotes`.

### Notification đơn giản

- Admin tạo thông báo.
- User xem danh sách thông báo.

Use case cover:

- UC09 Review.
- UC10 Quote.
- UC11 Notification.

---

## M12 — Dọn code, demo và báo cáo

Mục tiêu:

- Kiểm tra toàn bộ use case core.
- Chuẩn bị dữ liệu mẫu.
- Dọn code.
- Chuẩn bị demo.
- Cập nhật báo cáo theo app thật.

Checklist demo:

- [ ] Mở app.
- [ ] Đăng ký tài khoản.
- [ ] Đăng nhập user.
- [ ] Xem danh sách sách.
- [ ] Đăng nhập admin.
- [ ] Thêm sách có file.
- [ ] Kiểm tra sách xuất hiện trong database.
- [ ] User mở sách.
- [ ] User lưu tiến trình đọc.
- [ ] Đăng nhập lại và kiểm tra tiến trình còn.
- [ ] Admin xóa sách.
- [ ] Kiểm tra database thay đổi thật.

Use case cover:

- Tổng hợp UC01-UC08.

---

## 9. Phần PDF/EPUB/TXT nên làm ở mức nào?

Với tiến độ hiện tại, không nên tự viết trình đọc PDF/EPUB phức tạp.

Mức đủ tốt:

| Loại file | Cách xử lý |
|---|---|
| TXT | Đọc trực tiếp trong app bằng `Files.readString()` |
| PDF | Mở bằng app mặc định qua `Desktop.getDesktop().open(file)` |
| EPUB | Mở bằng app mặc định qua `Desktop.getDesktop().open(file)` |

Như vậy app vẫn có chức năng đọc/mở sách, vẫn lưu được tiến trình đọc, và không bị sa vào xử lý thư viện PDF/EPUB quá nặng.

Nếu muốn làm nâng cao:

- PDF: dùng Apache PDFBox hoặc ICEpdf.
- EPUB: dùng epublib.

Nhưng đây nên là phần mở rộng, không phải core bắt buộc.

---

## 10. Những điểm không được bỏ nếu muốn đúng kế hoạch

Không nên bỏ các phần sau:

1. `ReadingProcesses`.
2. Chức năng mở/đọc sách.
3. `filePath` và `fileType` trong `Books`.
4. Phân quyền user/admin.
5. DAO/Service tách khỏi UI.
6. `PreparedStatement`.
7. Bảng `Genres` hoặc ít nhất field thể loại trong `Books`.

Nếu bỏ các phần này, app dễ bị lệch thành app CRUD sách đơn thuần, không còn đúng thiết kế đọc sách ban đầu.

---

## 11. Thứ tự làm từ thời điểm hiện tại

Nếu hiện tại đã xong đăng ký, đăng nhập, MainFrame và quản lý sách cơ bản, thứ tự tiếp theo nên là:

1. Kiểm tra M7: thêm/xóa sách đã ghi thật vào SQL Server chưa.
2. Bổ sung `filePath`, `fileType` vào form thêm sách.
3. Thêm nút chọn file bằng `JFileChooser`.
4. Lưu đường dẫn file vào bảng `Books`.
5. Tạo `ReadingPanel`.
6. Từ danh sách sách, bấm “Đọc” để mở `ReadingPanel`.
7. Mở TXT trong app hoặc PDF/EPUB bằng app mặc định.
8. Tạo `ReadingProcessDAO`.
9. Lưu tiến trình đọc.
10. Load lại tiến trình khi mở lại sách.
11. Chốt phân quyền admin/user.
12. Dọn UI và chuẩn bị demo.

---

## 12. Kết luận audit milestone

Milestone cũ không sai, nhưng cần chỉnh lại thứ tự và trọng tâm:

- M7 không chỉ là thêm/xóa sách, mà phải chuẩn bị cho đọc sách bằng `filePath`.
- M8 phải là chọn/lưu file PDF/EPUB/TXT.
- M9 phải là đọc/mở sách và lưu tiến trình đọc.
- Review/Quote/Notification không nên ép làm ngay, nhưng nên giữ trong thiết kế mở rộng để không mất liên hệ với thiết kế cũ.

Bộ milestone sau khi audit đã cover đủ nhóm use case core:

| Use case core | Milestone cover |
|---|---|
| Đăng ký | M4 |
| Đăng nhập | M5 |
| Xem danh sách sách | M6, M7 |
| Quản lý sách | M7, M8 |
| Đọc/mở sách | M8, M9 |
| Lưu tiến trình đọc | M9 |
| Quản lý thể loại | M7 |
| Phân quyền | M6, M10 |

Vì vậy, chỉ cần đi theo milestone trong guide này thì project vẫn rút gọn được, nhưng không lệch khỏi thiết kế OOAD đã thống nhất.
