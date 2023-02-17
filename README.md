# PEMANFAATAN ALGORITMA GREEDY - ENTELLECT 2021 GALAXIO

Tugas Besar 1 IF2211 Strategi Algoritma Semester II Tahun 2022/2023 Pemanfaatan Algoritma Greedy dalam Aplikasi Permainan “Galaxio”

## Author - Glaxrid

1. Manuella Ivana Uli Sianipar / 13521051
2. Asyifa Nurul Shafira / 13521125
3. Ferindya Aulia Berlianty / 13521161

## Requirements

1. [Java](https://www.oracle.com/java/technologies/downloads/#java) (minimal Java 11)
2. [IntelliJ IDEA](https://www.jetbrains.com/idea/)
3. [Node JS](https://nodejs.org/en/download/)
4. [.Net Core 3.1](https://dotnet.microsoft.com/en-us/download/dotnet/3.1)

## How To Build

1. Download latest Entellect Challenge 2021 [starter-pack](https://github.com/EntelectChallenge/2021-Galaxio/releases/tag/2021.3.2).
2. Clone this repository, if you didn't.
3. Copy the **java** folder inside **src** folder from this repository, then paste in **starter-pack/starter-bots**.
   (_discalimer : if there is exist java folder inside the starter-bots, erase the existing java folder first_)
4. Open java folder with Intellij IDEA.
5. Open up the "Maven Projects" tab on the right side of the screen. 

## How To Run

1. Update game-runner-config.json. Change "player-a" to "./starter-bots/java".
2. Go back to starter-pack folder.
3. Click main.bat


## Greedy Algorithm

### 1. _Greedy dengan Prioritas Merata_

Algoritma greedy ini membuat bot menuju ke arah yang paling aman dari objek-objek pengganggu, musuh, dan map. Arah yang dituju adalah arah yang paling aman. Namun, karena bot akan selalu menjauh dari gangguan, bot tidak akan fokus terhadap makanan sehingga bot tidak akan efektif untuk menangkap makanan.

### 2. _Greedy dengan Prioritas Menghindari Objek Pengganggu_

Algoritma ini membuat bot menuju ke arah yang menghindari objek pengganggu sehingga pergerakan bot akan menjadi lebih efektif. Namun, kemungkinan bot akan lebih mengutamakan menghindari objek gangguan daripada musuh yang berbahaya.

### 3. _Greedy dengan Prioritas Menghindari Musuh_

Algoritma ini membuat bot menuju ke arah yang menghindari musuh berbahaya sehingga bot akan aman dari musuh yang akan memakan. Namun, kemungkinan bot akan lebih memilih untuk mengejar musuh daripada memperhatikan objek sekitarnya yang mungkin saja membuat kapal bot menjadi lebih kecil atau bergerak lebih lambat.

### 4. _Greedy dengan Prioritas Menghindari Ujung Asap_

Algoritma ini membuat bot menuju ke tengah sehingga bot tidak akan terlempar keluar map. Namun, bot kemungkinan tidak akan memperhatikan musuh dan objek sekitarnya.
