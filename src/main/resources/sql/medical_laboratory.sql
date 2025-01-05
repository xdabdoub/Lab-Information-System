Drop DATABASE Medical_Laboratory;
CREATE DATABASE Medical_Laboratory;
USE Medical_Laboratory;

CREATE TABLE Doctors
(
    doctorId INT PRIMARY KEY,
    name     VARCHAR(32),
    phone    VARCHAR(32),
    address  VARCHAR(32)
);

CREATE TABLE Users
(
    doctorId INT PRIMARY KEY DEFAULT -1,
    username TINYBLOB NOT NULL,
    password TINYBLOB NOT NULL,
    permission VARCHAR(32) DEFAULT 'REGULAR',
    FOREIGN KEY (doctorId) REFERENCES Doctors (doctorId) ON UPDATE CASCADE
);

INSERT INTO Doctors (doctorId, name, address)
VALUES (1, "Yazan", "Ramallah"),
       (2, "Mohammed", "Neileen"),
       (-1, "Admin", "Admin");

CREATE TABLE Patients
(
    patientId   INT PRIMARY KEY,
    name        VARCHAR(32),
    gender      VARCHAR(10),
    dateOfBirth DATE,
    phone       VARCHAR(32),
    address     VARCHAR(32)
);

CREATE TABLE Invoices
(
    invoiceId     INT PRIMARY KEY,
    invoiceDate   DATE,
    amount        DECIMAL(10, 2) NOT NULL,
    patientId     INT         NOT NULL,
    invoiceStatus VARCHAR(32) NOT NULL,
    lastModified DATE,
    FOREIGN KEY (patientId) REFERENCES Patients (patientId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Samples
(
    sampleId       INT AUTO_INCREMENT PRIMARY KEY,
    patientId      INT,
    FOREIGN KEY (patientId) REFERENCES Patients (patientId) ON DELETE CASCADE ON UPDATE CASCADE,
    sampleType     VARCHAR(255) NOT NULL,
    collectionDate DATE        NOT NULL,
    collectedBy    INT,
    lastModified DATE,
    FOREIGN KEY (collectedBy) REFERENCES Doctors (doctorId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Tests
(
    testId     INT AUTO_INCREMENT PRIMARY KEY,
    sampleId   INT         NOT NULL,
    testStatus VARCHAR(32) NOT NULL,
    testDate   DATE,
    lastModified DATE,
    UNIQUE (sampleId, testId),
    FOREIGN KEY (sampleId) REFERENCES Samples (sampleId) ON DELETE CASCADE
);

CREATE TABLE Reports
(
    reportId   INT PRIMARY KEY,
    testId     INT NOT NULL,
    reportDate DATE,
    result     TEXT,
    lastModified DATE,
    UNIQUE (testId, reportId),
    FOREIGN KEY (testId) REFERENCES Tests (testId) ON DELETE CASCADE
);

CREATE TABLE Bookmarks
(
    bookmarkId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    entityId INT NOT NULL,
    entityType VARCHAR(32) NOT NULL, # PATIENT, SAMPLE, REPORT, TEST, INVOICE, USER, DOCTOR
    bookmarkedAt DATE NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(doctorId) ON DELETE CASCADE
);

INSERT INTO Bookmarks (userId, entityId, entityType, bookmarkedAt)
VALUES (1, 88, "PATIENT", NOW()),
       (1, 1, "SAMPLE", NOW()),
       (1, 3232, "PATIENT", NOW());

INSERT IGNORE INTO Patients (patientId, name, gender, dateOfBirth, phone, address)
VALUES (852475516, "تمام محمد سعيد", "Female", "1989-03-21", "0529369051", "قلقيلية"),
       (406297226, "رحيق شريف عبد الهادي مرابعة", "Female", "2001-12-03", "0529369051", "بيت سوريك"),
       (991075789, "جواهر راشد احمد دار شيخ", "Female", "1965-12-07", "0597342059", "نابلس"),
       (949592067, "ادهم موسى حسن بدر", "Male", "1985-07-12", "0568233939", "مستوصف عقربا"),
       (852475518, "خالد سمير يوسف", "Male", "1986-05-05", "0591234567", "القدس"),
       (406297229, "منى سعيد جابر", "Female", "1995-12-23", "0522345678", "أريحا"),
       (991075791, "سليم حسين نبيل", "Male", "1980-11-20", "0597654321", "غزة"),
       (949592069, "عائشة مصطفى إبراهيم", "Female", "1993-07-29", "0568123456", "بيت سوريك"),
       (854727776, "جواد رائد فايز", "Male", "1990-09-09", "0527654320", "نابلس"),
       (852475518, "خالد سمير يوسف", "Male", "1986-05-05", "0591234567", "القدس"),
       (406297229, "منى سعيد جابر", "Female", "1995-03-12", "0522345678", "أريحا"),
       (991075791, "سليم حسين نبيل", "Male", "1980-11-20", "0597654321", "غزة"),
       (949592069, "عائشة مصطفى إبراهيم", "Female", "1993-07-25", "0568123456", "بيت سوريك"),
       (854727776, "جواد رائد فايز", "Male", "1990-09-09", "0527654320", "نابلس"),
       (406297230, "فاطمة عبد الله حسان", "Female", "1989-01-17", "0598234567", "طولكرم"),
       (991075792, "محمود عصام غسان", "Male", "1975-04-10", "0569001122", "جنين"),
       (949592070, "سارة عدنان صالح", "Female", "1991-02-19", "0598765432", "رام الله"),
       (852475519, "ياسر عصام عبد الله", "Male", "1982-12-15", "0592345679", "الخليل"),
       (406297231, "أحلام منير جبر", "Female", "1994-06-05", "0561230987", "قلقيلية"),
       (991075793, "خديجة يوسف فارس", "Female", "1980-10-12", "0599345678", "القدس"),
       (949592071, "سامي حسين مصطفى", "Male", "1983-07-03", "0528736456", "أريحا"),
       (854727777, "ليلى سامي عمران", "Female", "1992-01-25", "0565432109", "غزة"),
       (406297232, "رنا مصطفى هلال", "Female", "1984-11-07", "0594321098", "بيت سوريك"),
       (991075794, "إبراهيم عادل تيسير", "Male", "1988-05-15", "0567876543", "نابلس"),
       (949592072, "نوال ناصر جاد", "Female", "1979-08-22", "0599988777", "رام الله"),
       (852475520, "جمال طارق رباح", "Male", "1995-09-30", "0522334455", "طولكرم"),
       (406297233, "هناء عايد بدر", "Female", "1986-06-03", "0592233445", "الخليل"),
       (991075795, "فهد إسماعيل هلال", "Male", "1981-04-13", "0563214321", "قلقيلية"),
       (949592073, "سلوى حسن يوسف", "Female", "1990-02-08", "0592312345", "غزة"),
       (852475521, "جودت زاهر محمود", "Male", "1992-07-19", "0593456789", "أريحا"),
       (406297234, "مريم سعيد سمير", "Female", "1994-03-03", "0564345670", "القدس"),
       (991075796, "أحمد منصور غيث", "Male", "1982-10-25", "0529892345", "رام الله"),
       (949592074, "مها سليمان فهد", "Female", "1995-01-10", "0591012345", "طولكرم"),
       (854727778, "فايز يوسف عبد الرحمن", "Male", "1988-08-14", "0598645321", "الخليل"),
       (406297235, "فاطمة سليمان علي", "Female", "1991-11-08", "0525678230", "غزة"),
       (991075797, "رائد سامي محمد", "Male", "1987-06-20", "0593245678", "بيت سوريك"),
       (949592075, "عهد سالم شريف", "Female", "1992-05-05", "0569876543", "جنين"),
       (852475522, "محمد غازي كامل", "Male", "1980-09-17", "0521238901", "نابلس"),
       (406297236, "صابرين مصطفى عبد الله", "Female", "1983-03-22", "0597632109", "رام الله"),
       (991075798, "سمير عصام ناصر", "Male", "1975-12-05", "0526543210", "طولكرم"),
       (949592076, "مروة عبد الرحمن فوزي", "Female", "1994-07-21", "0592340987", "القدس"),
       (852475523, "سامي زكي حسن", "Male", "1986-01-11", "0561345678", "أريحا"),
       (406297237, "أميرة هشام ممدوح", "Female", "1990-08-02", "0598765430", "غزة"),
       (991075799, "محمود عادل فرج", "Male", "1994-05-12", "0523216789", "رام الله"),
       (949592077, "شادية حسين فاضل", "Female", "1982-02-16", "0567630192", "نابلس"),
       (852475524, "محمود فهد إبراهيم", "Male", "1992-09-07", "0598532341", "بيت سوريك"),
       (406297238, "نسرين طه سلامة", "Female", "1987-03-09", "0525423123", "الخليل"),
       (991075800, "عادل ناصر سامي", "Male", "1978-06-25", "0592112345", "غزة"),
       (949592078, "ناريمان محمود فادي", "Female", "1990-04-17", "0596543212", "أريحا"),
       (852475525, "عمر جهاد سعيد", "Male", "1985-12-18", "0594532109", "رام الله"),
       (406297239, "مها علي يوسف", "Female", "1992-11-12", "0562320987", "طولكرم"),
       (991075801, "عيسى كمال إبراهيم", "Male", "1993-05-01", "0592233445", "نابلس"),
       (949592079, "دينا رشاد جمال", "Female", "1991-08-03", "0598762321", "القدس");