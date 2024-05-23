Langkah pertama: 
Buat dalam localhost server apitask database, di bagan sql masukan kode dibawah

CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    task_detail VARCHAR(255) NOT NULL,
    urgency VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    createdTime VARCHAR(255) NOT NULL,
    finishedTime VARCHAR(255) NOT NULL,
    duration VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

Langkah kedua:
Run app.py dari mana saja, ini akan berguna sebagai penyambung database ke aplikasi.

Langkah ketiga: 
Extract seluruh file android studio kedalam folder AndroidStudioProject

Langkah keempat: 
Gradle sync, download device virtual

Langkah terakhir: 
jalankan app
