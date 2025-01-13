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
VALUES (1, "Yazan Hamarsheh", "Ramallah"),
       (2, "Mohammed Srour", "Nilien"),
       (3, "Naimeh Hirbawi", "Jerusalem"),
       (-1, "Admin (-1)", "Admin (-1)");

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
    description TEXT,
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

INSERT INTO Tests (testId, sampleId, testStatus, testDate, lastModified) VALUES (1, 1, "PENDING", NOW(), NOW());
INSERT INTO Reports (reportId, testId, reportDate, result, lastModified) VALUES (1, 1, NOW(), "No results", NOW());

CREATE TABLE Bookmarks
(
    bookmarkId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    entityId INT NOT NULL,
    entityType VARCHAR(32) NOT NULL, # PATIENT, SAMPLE, REPORT, TEST, INVOICE, USER, DOCTOR
    bookmarkedAt DATE NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(doctorId) ON DELETE CASCADE
);

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

INSERT INTO Samples (sampleId, patientId, sampleType, collectionDate, collectedBy, lastModified) VALUES
       (300001, 852475516, 'BLOOD', '2024-01-01', 1,NOW()),
       (300002, 852475516, 'URINE', '2024-01-05', 2,NOW()),
       (300003, 406297226, 'DNA_ANALYSIS', '2024-01-02', 3,NOW()),
       (300004, 406297226, 'PLASMA', '2024-01-06',1,NOW()),
       (300005, 991075789, 'SERUM', '2024-01-03', 2,NOW()),
       (300006, 991075789, 'TESTOSTERONE', '2024-01-07', 3,NOW()),
       (300007, 949592067, 'VIRAL_DETECTION', '2024-01-04', 2,NOW()),
       (300008, 949592067, 'RESPIRATORY_INFECTION', '2024-01-08', 1,NOW()),
       (300009, 852475518, 'SWAB', '2024-01-05', 1,NOW()),
       (300010, 852475518, 'AMNIOTIC_FLUID', '2024-01-09', 1,NOW()),
       (300011, 406297229, 'SEMEN', '2024-01-06', 1,NOW()),
       (300012, 406297229, 'BONE_MARROW', '2024-01-10', 2,NOW()),
       (300013, 991075791, 'SWEAT', '2024-01-07', 3,NOW()),
       (300014, 991075791, 'CREATINE_PHOSPHOKINASE', '2024-01-11', 2,NOW()),
       (300015, 949592069, 'THYROID_IMMUNITY', '2024-01-08', 3,NOW()),
       (300016, 949592069, 'RETINAL_BLOOD_CELLS', '2024-01-12', 3,NOW()),
       (300017, 854727776, 'PITUITARY_GLAND', '2024-01-09', 2,NOW()),
       (300018, 854727776, 'INSULIN_RESISTANCE', '2024-01-13', 3,NOW()),
       (300019, 852475519, 'POLYCYSTIC_OVARY_SYNDROME', '2024-01-10', 1,NOW()),
       (300020, 852475519, 'HEREDITARY_RECURRENT_MISCARRIAGE', '2024-01-14', 1,NOW()),
       (300021, 406297230, 'IMMUNOLOGICAL_RECURRENT_MISCARRIAGE', '2024-01-11', 1,NOW()),
       (300022, 406297230, 'DIGITAL_BRUCELLOSIS', '2024-01-15', 2,NOW()),
       (300023, 991075792, 'ACCELERATED_HEARTBEAT', '2024-01-12', 2,NOW()),
       (300024, 991075792, 'BLOOD', '2024-01-16', 2,NOW()),
       (300025, 949592070, 'URINE', '2024-01-13', 3,NOW()),
       (300026, 949592070, 'DNA_ANALYSIS', '2024-01-17', 3,NOW()),
       (300027, 852475520, 'PLASMA', '2024-01-14', 3,NOW()),
       (300028, 852475520, 'SERUM', '2024-01-18', 1,NOW()),
       (300029, 406297231, 'TESTOSTERONE', '2024-01-15', 1,NOW());

INSERT INTO Tests (sampleId, testStatus, testDate, lastModified) VALUES
       (300001, 'PENDING', '2024-01-02', '2024-01-03'),
       (300001, 'POSITIVE', '2024-01-03', '2024-01-03'),
       (300002, 'NEGATIVE', '2024-01-06', '2024-01-07'),
       (300002, 'PENDING', '2024-01-06', '2024-01-07'),
       (300003, 'ERROR', '2024-01-03', '2024-01-04'),
       (300003, 'NEGATIVE', '2024-01-03', '2024-01-04'),
       (300004, 'POSITIVE', '2024-01-07', '2024-01-08'),
       (300005, 'PENDING', '2024-01-04', '2024-01-05'),
       (300005, 'POSITIVE', '2024-01-05', '2024-01-06'),
       (300006, 'NEGATIVE', '2024-01-08', '2024-01-09'),
       (300007, 'ERROR', '2024-01-05', '2024-01-06'),
       (300007, 'POSITIVE', '2024-01-05', '2024-01-06'),
       (300008, 'PENDING', '2024-01-09', '2024-01-10'),
       (300009, 'NEGATIVE', '2024-01-06', '2024-01-07'),
       (300009, 'ERROR', '2024-01-06', '2024-01-07'),
       (300010, 'PENDING', '2024-01-10', '2024-01-11'),
       (300011, 'NEGATIVE', '2024-01-07', '2024-01-08'),
       (300011, 'POSITIVE', '2024-01-07', '2024-01-08'),
       (300012, 'PENDING', '2024-01-11', '2024-01-12'),
       (300013, 'NEGATIVE', '2024-01-08', '2024-01-09'),
       (300013, 'POSITIVE', '2024-01-08', '2024-01-09'),
       (300014, 'ERROR', '2024-01-12', '2024-01-13'),
       (300015, 'PENDING', '2024-01-09', '2024-01-10'),
       (300015, 'NEGATIVE', '2024-01-09', '2024-01-10'),
       (300016, 'POSITIVE', '2024-01-13', '2024-01-14'),
       (300017, 'PENDING', '2024-01-10', '2024-01-11'),
       (300017, 'POSITIVE', '2024-01-10', '2024-01-11'),
       (300018, 'NEGATIVE', '2024-01-14', '2024-01-15'),
       (300019, 'PENDING', '2024-01-11', '2024-01-12'),
       (300019, 'ERROR', '2024-01-11', '2024-01-12'),
       (300020, 'NEGATIVE', '2024-01-15', '2024-01-16'),
       (300021, 'POSITIVE', '2024-01-12', '2024-01-13'),
       (300022, 'PENDING', '2024-01-16', '2024-01-17'),
       (300023, 'ERROR', '2024-01-13', '2024-01-14'),
       (300023, 'POSITIVE', '2024-01-13', '2024-01-14'),
       (300024, 'NEGATIVE', '2024-01-17', '2024-01-18'),
       (300025, 'PENDING', '2024-01-14', '2024-01-15'),
       (300025, 'ERROR', '2024-01-14', '2024-01-15'),
       (300026, 'NEGATIVE', '2024-01-18', '2024-01-19'),
       (300027, 'POSITIVE', '2024-01-15', '2024-01-16'),
       (300028, 'PENDING', '2024-01-19', '2024-01-20'),
       (300029, 'NEGATIVE', '2024-01-16', '2024-01-17'),
       (300029, 'POSITIVE', '2024-01-20', '2024-01-21'),
       (300028, 'PENDING', '2024-01-17', '2024-01-18'),
       (300026, 'NEGATIVE', '2024-01-21', '2024-01-22'),
       (300027, 'POSITIVE', '2024-01-18', '2024-01-19'),
       (300025, 'ERROR', '2024-01-22', '2024-01-23'),
       (300021, 'NEGATIVE', '2024-01-19', '2024-01-20'),
       (300019, 'POSITIVE', '2024-01-23', '2024-01-24'),
       (300015, 'PENDING', '2024-01-20', '2024-01-21'),
       (300010, 'NEGATIVE', '2024-01-24', '2024-01-25'),
       (300012, 'POSITIVE', '2024-01-21', '2024-01-22'),
       (300008, 'PENDING', '2024-01-25', '2024-01-26'),
       (300023, 'ERROR', '2024-01-22', '2024-01-23'),
       (300022, 'NEGATIVE', '2024-01-22', '2024-01-23'),
       (300024, 'POSITIVE', '2024-01-26', '2024-01-27'),
       (300016, 'PENDING', '2024-01-23', '2024-01-24'),
       (300018, 'ERROR', '2024-01-27', '2024-01-28'),
       (300019, 'NEGATIVE', '2024-01-24', '2024-01-25'),
       (300017, 'POSITIVE', '2024-01-28', '2024-01-29'),
       (300007, 'PENDING', '2024-01-25', '2024-01-26'),
       (300011, 'ERROR', '2024-01-29', '2024-01-30'),
       (300021, 'NEGATIVE', '2024-01-26', '2024-01-27'),
       (300020, 'POSITIVE', '2024-01-30', '2024-01-31'),
       (300029, 'POSITIVE', '2024-01-27', '2024-01-28');

INSERT INTO Reports (reportId, testId, reportDate, result, lastModified) VALUES
       (400001, 1, '2024-01-03', 'The test is pending. Awaiting results.', '2024-01-03'),
       (400002, 1, '2024-01-03', 'The test returned positive. Pathogen detected in the sample.', '2024-01-03'),
       (400003, 2, '2024-01-07', 'The test result is negative. No abnormalities detected.', '2024-01-07'),
       (400004, 2, '2024-01-07', 'The test is pending. Awaiting sample analysis.', '2024-01-07'),
       (400005, 3, '2024-01-04', 'Error encountered during analysis. Retesting recommended.', '2024-01-04'),
       (400006, 3, '2024-01-04', 'The test result is negative. No abnormalities detected.', '2024-01-04'),
       (400007, 4, '2024-01-08', 'The test returned positive. Evidence of infection detected.', '2024-01-08'),
       (400008, 5, '2024-01-05', 'The test is pending. Results will be processed shortly.', '2024-01-05'),
       (400009, 5, '2024-01-06', 'The test returned positive. Pathogen confirmed in the sample.', '2024-01-06'),
       (400010, 6, '2024-01-09', 'The test result is negative. No issues found in the analysis.', '2024-01-09'),
       (400011, 7, '2024-01-06', 'Error encountered in processing. Sample compromised.', '2024-01-06'),
       (400012, 7, '2024-01-06', 'The test is positive. High presence of target pathogen confirmed.', '2024-01-06'),
       (400013, 8, '2024-01-10', 'The test is pending. Processing underway.', '2024-01-10'),
       (400014, 9, '2024-01-07', 'The test result is negative. No pathogenic elements found.', '2024-01-07'),
       (400015, 9, '2024-01-07', 'Error during processing. Possible contamination.', '2024-01-07'),
       (400016, 10, '2024-01-11', 'The test is pending. Awaiting final validation.', '2024-01-11'),
       (400017, 11, '2024-01-08', 'The test result is negative. No significant findings.', '2024-01-08'),
       (400018, 11, '2024-01-08', 'The test returned positive. Signs of infection detected.', '2024-01-08'),
       (400019, 12, '2024-01-12', 'The test is pending. Laboratory analysis in progress.', '2024-01-12'),
       (400020, 13, '2024-01-09', 'The test result is negative. No unusual markers found.', '2024-01-09'),
       (400021, 13, '2024-01-09', 'The test returned positive. Presence of pathogen markers identified.', '2024-01-09'),
       (400022, 14, '2024-01-13', 'Error in processing. Repeat analysis advised.', '2024-01-13'),
       (400023, 15, '2024-01-10', 'The test is pending. Awaiting quality control clearance.', '2024-01-10'),
       (400024, 15, '2024-01-10', 'The test result is negative. No indicators of concern.', '2024-01-10'),
       (400025, 16, '2024-01-14', 'The test is positive. Confirmed pathogen presence.', '2024-01-14'),
       (400026, 17, '2024-01-11', 'The test is pending. Undergoing further evaluation.', '2024-01-11'),
       (400027, 17, '2024-01-11', 'The test is positive. Indicative markers present.', '2024-01-11'),
       (400028, 18, '2024-01-15', 'The test result is negative. All parameters within normal limits.', '2024-01-15'),
       (400029, 19, '2024-01-12', 'The test is pending. Awaiting secondary validation.', '2024-01-12'),
       (400030, 19, '2024-01-12', 'Error during processing. Recheck the sample.', '2024-01-12'),
       (400031, 20, '2024-01-16', 'The test result is negative. No pathogenic traces found.', '2024-01-16'),
       (400032, 21, '2024-01-13', 'The test is positive. Significant markers detected.', '2024-01-13'),
       (400033, 22, '2024-01-17', 'The test is pending. Report generation in progress.', '2024-01-17'),
       (400034, 23, '2024-01-14', 'Error encountered. Suggest retesting with fresh sample.', '2024-01-14'),
       (400035, 23, '2024-01-14', 'The test is positive. Strong evidence of target pathogen.', '2024-01-14'),
       (400036, 24, '2024-01-18', 'The test result is negative. No pathogenic signals.', '2024-01-18'),
       (400037, 25, '2024-01-15', 'The test is pending. Awaiting laboratory analysis.', '2024-01-15'),
       (400038, 25, '2024-01-15', 'Error during processing. Sample integrity compromised.', '2024-01-15'),
       (400039, 26, '2024-01-19', 'The test result is negative. No abnormalities detected.', '2024-01-19');


INSERT INTO Invoices (invoiceId, invoiceDate, amount, patientId, description, invoiceStatus, lastModified)
VALUES
    (1, '2024-01-01', 200.50, 852475516, 'Blood Test', 'PAID', '2025-01-01'),
    (2, '2024-01-02', 150.75, 406297226, 'X-Ray Examination', 'DUE', '2025-01-02'),
    (3, '2024-01-03', 300.00, 991075789, 'MRI Scan', 'PAID', '2025-01-03'),
    (4, '2024-01-04', 50.00, 949592067, 'Consultation Fee', 'DUE', '2025-01-04'),
    (5, '2024-01-05', 120.00, 852475518, 'Ultrasound', 'PAID', '2025-01-05'),
    (6, '2024-01-06', 90.25, 406297229, 'Vaccination', 'DUE', '2025-01-06'),
    (7, '2024-01-07', 75.50, 991075791, 'Blood Pressure Test', 'PAID', '2025-01-07'),
    (8, '2024-01-08', 250.00, 949592069, 'CT Scan', 'PAID', '2025-01-08'),
    (9, '2024-01-09', 400.00, 854727776, 'Surgery Charges', 'DUE', '2025-01-09'),
    (10, '2024-01-10', 35.00, 406297230, 'Routine Checkup', 'PAID', '2025-01-10'),
    (11, '2024-01-11', 145.00, 991075792, 'Lab Tests', 'PAID', '2025-01-11'),
    (12, '2024-01-12', 500.00, 949592070, 'Emergency Room Services', 'DUE', '2025-01-12'),
    (13, '2025-01-13', 175.00, 852475519, 'Physical Therapy Session', 'PAID', '2025-01-13'),
    (14, '2025-01-14', 225.50, 406297231, 'Dental Cleaning', 'DUE', '2025-01-14'),
    (15, '2025-01-15', 89.99, 991075793, 'Flu Vaccination', 'PAID', '2025-01-15'),
    (16, '2025-01-16', 345.00, 949592071, 'Cardiology Consultation', 'PAID', '2025-01-16'),
    (17, '2025-01-17', 299.99, 854727777, 'Orthopedic Surgery', 'DUE', '2025-01-17'),
    (18, '2025-01-18', 59.75, 406297232, 'Eye Examination', 'PAID', '2025-01-18'),
    (19, '2025-01-19', 125.00, 991075794, 'Diabetes Management Program', 'PAID', '2025-01-19'),
    (20, '2025-01-20', 80.00, 949592072, 'Hearing Test', 'DUE', '2025-01-20'),
    (21, '2025-01-21', 215.00, 852475520, 'Skin Allergy Treatment', 'PAID', '2025-01-21'),
    (22, '2025-01-22', 67.50, 406297233, 'Routine Vaccination', 'DUE', '2025-01-22'),
    (23, '2025-01-23', 400.00, 991075795, 'Fracture Treatment', 'PAID', '2025-01-23'),
    (24, '2025-01-24', 310.00, 949592073, 'Neurological Consultation', 'DUE', '2025-01-24'),
    (25, '2025-01-25', 195.00, 852475521, 'Maternity Checkup', 'PAID', '2025-01-25'),
    (26, '2025-01-26', 500.00, 406297234, 'Surgical Procedure', 'PAID', '2025-01-26'),
    (27, '2025-01-27', 220.00, 991075796, 'Physiotherapy', 'DUE', '2025-01-27'),
    (28, '2025-01-28', 135.50, 949592074, 'Pediatric Care', 'PAID', '2025-01-28'),
    (29, '2025-01-29', 300.00, 854727778, 'Advanced Imaging', 'DUE', '2025-01-29'),
    (30, '2025-01-30', 95.75, 406297235, 'General Health Checkup', 'PAID', '2025-01-30'),
    (31, '2025-01-31', 410.00, 991075797, 'Orthodontics', 'PAID', '2025-01-31');
