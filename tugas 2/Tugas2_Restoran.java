/*SOAL TUGAS 2
1. Input Menu Restoran : Data menu makanan dan minuman beserta harganya, disimpan dalam array.
   Setiap item menu memiliki nama, harga, dan kategori (makanan, minuman).
2. Pemesanan : Memesan makanan dan minuman berdasarkan daftar menu yang ditampilkan (tidak terbatas),
   sampai pelanggan memasukkan input 'selesai'.
   Jika input di luar pilihan menu yang ada, sistem terus meminta input kembali.
3. Menghitung Total Biaya : Menghitung total biaya pesanan, biaya pajak 10% dan biaya pelayanan Rp. 20.000
   Selain itu, restoran ini juga menerapkan diskon atau penawaran khusus :
    A. Diskon 10% jika total biaya keseluruhan pesanan melebihi Rp 100.000
    B. Penawaran beli satu gratis satu untuk salah satu kategori minuman jika total biaya keseluruhan pesanan melebihi Rp 50.000

4. Mencetak Struk Pesanan : Mencetak struk pesanan yang mencantumkan item-menu yang dipesan, jumlahnya,
   harga per item, total harga per item, total biaya pemesanan, informasi pajak dan biaya pajak, dan biaya pelayanan.
   Jika ada diskon / penawaran, tampilkan informasinya beserta harga setelah diskon / penawaran.

5. Manajemen Menu Aplikasi : Terdapat dua menu utama, yaitu menu untuk pelanggan memesan dan menu pengelolaan menu
   untuk pemilik restoran (tambah menu baru, ubah harga, dan hapus menu).
    - Navigasi menu dapat kembali ke menu sebelumnya (parent).
    - Saat mengubah dan menghapus, tampilkan daftar menu lalu input nomor menu.
    - Sebelum dieksekusi, tampilkan konfirmasi. Jika input 'Ya', daftar menu berubah dan kembali ke menu pengelolaan.
      Jika tidak, tidak ada perubahan.
    - Jika input di luar pilihan menu yang ada, sistem terus meminta input kembali.*/

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Tugas2_Restoran {

    static Scanner input = new Scanner(System.in);

    // Data menu disimpan dalam ArrayList (array dinamis)
    // agar menu dapat ditambah dan dihapus secara fleksibel
    static ArrayList<Menu> daftarMenu = new ArrayList<>();

    // Ketentuan biaya dan promo
    static final double PERSEN_PAJAK = 0.10;
    static final double PERSEN_DISKON = 0.10;
    static final double BATAS_DISKON = 100000;
    static final double BATAS_PROMO_MINUMAN = 50000;
    static final double BIAYA_PELAYANAN = 20000;

    // Class untuk menyimpan data menu (nama, harga, kategori)
    static class Menu {
        String nama;
        double harga;
        String kategori; // "Makanan" atau "Minuman"

        Menu(String nama, double harga, String kategori) {
            this.nama = nama;
            this.harga = harga;
            this.kategori = kategori;
        }
    }

    // Class untuk menyimpan satu item pesanan
    static class Pesanan {
        Menu menu;
        int jumlah;

        Pesanan(Menu menu, int jumlah) {
            this.menu = menu;
            this.jumlah = jumlah;
        }

        double totalHarga() {
            return menu.harga * jumlah;
        }
    }

    // ===================== DATA MENU AWAL =====================

    // Minimal 4 makanan dan 4 minuman
    static void inisialisasiMenu() {
        daftarMenu.add(new Menu("Nasi Uduk", 25000, "Makanan"));
        daftarMenu.add(new Menu("Nasi Rames", 30000, "Makanan"));
        daftarMenu.add(new Menu("Nasi Rawon", 35000, "Makanan"));
        daftarMenu.add(new Menu("Nasi Liwet", 15000, "Makanan"));
        daftarMenu.add(new Menu("Es Teler", 8000, "Minuman"));
        daftarMenu.add(new Menu("Es Teh", 5000, "Minuman"));
        daftarMenu.add(new Menu("Es Jeruk", 2500, "Minuman"));
        daftarMenu.add(new Menu("Kopi Susu", 15000, "Minuman"));
    }

    // ===================== FUNGSI BANTU =====================

    // Mengubah angka menjadi format rupiah, contoh: Rp 25.000
    static String formatRupiah(double nilai) {
        NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
        return "Rp " + format.format(nilai);
    }

    // Membaca bilangan bulat, terus meminta input sampai valid
    static int bacaInteger(String pesan, int minimum, int maksimum) {
        while (true) {
            System.out.print(pesan);
            String teks = input.nextLine().trim();

            try {
                int nilai = Integer.parseInt(teks);

                if (nilai >= minimum && nilai <= maksimum) {
                    return nilai;
                }

                System.out.println("Input harus antara " + minimum + " dan " + maksimum + ". Silakan coba lagi.");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat. Silakan coba lagi.");
            }
        }
    }

    // Membaca harga, terus meminta input sampai berupa angka lebih dari nol
    static double bacaHarga(String pesan) {
        while (true) {
            System.out.print(pesan);
            String teks = input.nextLine().trim();

            try {
                double harga = Double.parseDouble(teks);

                if (harga > 0) {
                    return harga;
                }

                System.out.println("Harga harus lebih besar dari nol. Silakan coba lagi.");
            } catch (NumberFormatException e) {
                System.out.println("Harga harus berupa angka. Silakan coba lagi.");
            }
        }
    }

    // Konfirmasi hanya menerima 'Ya' atau 'Tidak', terus meminta input sampai valid
    static boolean konfirmasi(String pesan) {
        while (true) {
            System.out.print(pesan + " (Ya/Tidak): ");
            String jawaban = input.nextLine().trim();

            if (jawaban.equalsIgnoreCase("Ya")) {
                return true;
            } else if (jawaban.equalsIgnoreCase("Tidak")) {
                return false;
            }

            System.out.println("Input tidak valid. Masukkan Ya atau Tidak.");
        }
    }

    // Mencari menu berdasarkan nomor atau nama, mengembalikan null jika tidak ada
    static Menu cariMenu(String teks) {
        try {
            int nomor = Integer.parseInt(teks);

            if (nomor >= 1 && nomor <= daftarMenu.size()) {
                return daftarMenu.get(nomor - 1);
            }

            return null;
        } catch (NumberFormatException e) {
            for (Menu m : daftarMenu) {
                if (m.nama.equalsIgnoreCase(teks)) {
                    return m;
                }
            }

            return null;
        }
    }

    // ===================== TAMPILAN MENU =====================

    static void tampilkanMenu() {
        System.out.println("\n===============================================");
        System.out.println("          WELCOME TO WARUNG KANG AFRIZAL       ");
        System.out.println("===============================================");

        if (daftarMenu.isEmpty()) {
            System.out.println("Belum ada menu yang tersedia.");
            System.out.println("===============================================");
            return;
        }

        tampilkanKategori("Makanan");
        System.out.println();
        tampilkanKategori("Minuman");
        System.out.println("===============================================");
    }

    // Menampilkan menu per kategori, nomor tetap sesuai urutan daftar menu
    static void tampilkanKategori(String kategori) {
        System.out.println("MENU " + kategori.toUpperCase());

        boolean ada = false;

        for (int i = 0; i < daftarMenu.size(); i++) {
            Menu m = daftarMenu.get(i);

            if (m.kategori.equalsIgnoreCase(kategori)) {
                System.out.printf("%2d. %-14s- %s%n", i + 1, m.nama, formatRupiah(m.harga));
                ada = true;
            }
        }

        if (!ada) {
            System.out.println("(Belum ada menu " + kategori.toLowerCase() + ")");
        }
    }

    // ===================== MENU UTAMA =====================

    static void menuUtama() {
        while (true) {
            System.out.println("\n===============================================");
            System.out.println("              WARUNG KANG AFRIZAL              ");
            System.out.println("===============================================");
            System.out.println("1. Pemesanan Pelanggan");
            System.out.println("2. Pengelolaan Menu Restoran");
            System.out.println("3. Keluar");

            int pilihan = bacaInteger("Pilih menu (1-3): ", 1, 3);

            switch (pilihan) {
                case 1:
                    pemesananPelanggan();
                    break;
                case 2:
                    pengelolaanMenu();
                    break;
                case 3:
                    System.out.println("Terimakasih telah berkunjung ke Warung Kang Afrizal!");
                    return;
            }
        }
    }

    // ===================== 2. PEMESANAN =====================

    static void pemesananPelanggan() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Menu restoran masih kosong. Pemesanan belum dapat dilakukan.");
            return;
        }

        ArrayList<Pesanan> daftarPesanan = new ArrayList<>();

        tampilkanMenu();
        System.out.println("Ketik nomor atau nama menu yang ingin dipesan.");
        System.out.println("Ketik 'selesai' jika pesanan sudah lengkap.");

        while (true) {
            System.out.print("\nPilih menu: ");
            String teks = input.nextLine().trim();

            if (teks.equalsIgnoreCase("selesai")) {
                break;
            }

            Menu menuDipilih = cariMenu(teks);

            // Jika input di luar pilihan menu, sistem meminta input kembali
            if (menuDipilih == null) {
                System.out.println("Menu \"" + teks + "\" tidak tersedia. Masukkan nomor/nama menu yang ada atau 'selesai'.");
                continue;
            }

            int jumlah = bacaInteger("Jumlah " + menuDipilih.nama + ": ", 1, 1000);

            // Jika menu yang sama dipesan lagi, jumlahnya digabungkan
            boolean sudahAda = false;

            for (Pesanan p : daftarPesanan) {
                if (p.menu == menuDipilih) {
                    p.jumlah += jumlah;
                    sudahAda = true;
                    break;
                }
            }

            if (!sudahAda) {
                daftarPesanan.add(new Pesanan(menuDipilih, jumlah));
            }

            System.out.println(menuDipilih.nama + " x" + jumlah + " ditambahkan ke pesanan.");
        }

        if (daftarPesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan yang dibuat.");
            return;
        }

        cetakStruk(daftarPesanan);
    }

    // ===================== 3 & 4. HITUNG TOTAL DAN CETAK STRUK =====================

    static void cetakStruk(ArrayList<Pesanan> daftarPesanan) {
        // Total biaya keseluruhan pesanan sebelum diskon/penawaran
        double totalSebelumPromo = 0;

        for (Pesanan p : daftarPesanan) {
            totalSebelumPromo += p.totalHarga();
        }

        // Promo 1: diskon 10% jika total pesanan melebihi Rp100.000
        double diskon = 0;

        if (totalSebelumPromo > BATAS_DISKON) {
            diskon = totalSebelumPromo * PERSEN_DISKON;
        }

        // Promo 2: beli 1 gratis 1 untuk salah satu minuman
        // jika total pesanan melebihi Rp50.000.
        // Minuman yang mendapat promo adalah minuman pertama yang dipesan,
        // dan potongannya sebesar harga satu porsi minuman tersebut.
        Pesanan minumanPromo = null;

        if (totalSebelumPromo > BATAS_PROMO_MINUMAN) {
            for (Pesanan p : daftarPesanan) {
                if (p.menu.kategori.equalsIgnoreCase("Minuman")) {
                    minumanPromo = p;
                    break;
                }
            }
        }

        double nilaiGratis = (minumanPromo != null) ? minumanPromo.menu.harga : 0;

        double totalSetelahPromo = totalSebelumPromo - diskon - nilaiGratis;
        double pajak = totalSetelahPromo * PERSEN_PAJAK;
        double totalAkhir = totalSetelahPromo + pajak + BIAYA_PELAYANAN;

        System.out.println("\n===============================================");
        System.out.println("                 STRUK PEMESANAN               ");
        System.out.println("===============================================");

        // Item, jumlah, harga per item, dan total harga per item
        for (Pesanan p : daftarPesanan) {
            System.out.println(p.menu.nama + " x" + p.jumlah
                    + " @" + formatRupiah(p.menu.harga)
                    + " = " + formatRupiah(p.totalHarga()));
        }

        System.out.println("-----------------------------------------------");
        System.out.printf("%-34s: %s%n", "Total sebelum diskon / promo", formatRupiah(totalSebelumPromo));

        // Informasi diskon 10%
        if (diskon > 0) {
            System.out.printf("%-34s: -%s%n", "Diskon 10% (pemesanan > Rp100.000)", formatRupiah(diskon));
        } else {
            System.out.println("Diskon 10% tidak berlaku (pemesanan belum melebihi Rp100.000)");
        }

        // Informasi penawaran beli 1 gratis 1
        if (minumanPromo != null) {
            System.out.printf("%-34s: -%s%n",
                    "Beli 1 gratis 1 (" + minumanPromo.menu.nama + ")", formatRupiah(nilaiGratis));
        } else {
            System.out.println("Promo beli 1 gratis 1 tidak berlaku (pemesanan belum melebihi Rp50.000 atau tidak ada minuman)");
        }

        System.out.println("-----------------------------------------------");
        System.out.printf("%-34s: %s%n", "Total setelah diskon / promo", formatRupiah(totalSetelahPromo));
        System.out.printf("%-34s: %s%n", "Pajak (10%)", formatRupiah(pajak));
        System.out.printf("%-34s: %s%n", "Biaya Pelayanan", formatRupiah(BIAYA_PELAYANAN));
        System.out.println("===============================================");
        System.out.printf("%-34s: %s%n", "TOTAL AKHIR YANG HARUS DIBAYARKAN", formatRupiah(totalAkhir));
        System.out.println("===============================================");
        System.out.println("         Terimakasih, Selamat Menikmati!       ");
        System.out.println("===============================================");
    }

    // ===================== 5. PENGELOLAAN MENU =====================

    static void pengelolaanMenu() {
        while (true) {
            System.out.println("\n===== PENGELOLAAN MENU RESTORAN =====");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");

            int pilihan = bacaInteger("Pilih menu (1-4): ", 1, 4);

            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    ubahHargaMenu();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 4:
                    return; // kembali ke menu utama
            }
        }
    }

    // Menambahkan menu baru
    static void tambahMenu() {
        System.out.println("\n===== TAMBAH MENU =====");

        String nama;

        // Terus meminta nama sampai valid (tidak kosong dan belum ada)
        while (true) {
            System.out.print("Masukkan nama menu (ketik 0 untuk kembali): ");
            nama = input.nextLine().trim();

            if (nama.equals("0")) {
                return;
            }

            if (nama.isEmpty()) {
                System.out.println("Nama menu tidak boleh kosong.");
                continue;
            }

            if (cariMenu(nama) != null && !nama.matches("\\d+")) {
                System.out.println("Nama menu tersebut sudah tersedia.");
                continue;
            }

            break;
        }

        double harga = bacaHarga("Masukkan harga menu: ");

        System.out.println("Kategori:");
        System.out.println("1. Makanan");
        System.out.println("2. Minuman");
        int pilihanKategori = bacaInteger("Pilih kategori (1-2): ", 1, 2);
        String kategori = (pilihanKategori == 1) ? "Makanan" : "Minuman";

        System.out.println("\nData menu yang akan ditambahkan:");
        System.out.println("Nama     : " + nama);
        System.out.println("Harga    : " + formatRupiah(harga));
        System.out.println("Kategori : " + kategori);

        if (konfirmasi("Apakah Anda yakin menambahkan menu ini?")) {
            daftarMenu.add(new Menu(nama, harga, kategori));
            System.out.println("Menu berhasil ditambahkan.");
            tampilkanMenu();
        } else {
            System.out.println("Penambahan menu dibatalkan. Tidak ada perubahan.");
        }
    }

    // Mengubah harga menu
    static void ubahHargaMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Tidak ada menu yang dapat diubah.");
            return;
        }

        tampilkanMenu();

        int nomor = bacaInteger("Pilih nomor menu yang akan diubah (0 untuk kembali): ", 0, daftarMenu.size());

        if (nomor == 0) {
            return;
        }

        Menu menu = daftarMenu.get(nomor - 1);

        System.out.println("Nama menu  : " + menu.nama);
        System.out.println("Harga lama : " + formatRupiah(menu.harga));

        double hargaBaru = bacaHarga("Masukkan harga baru: ");

        System.out.println("Harga baru : " + formatRupiah(hargaBaru));

        if (konfirmasi("Apakah Anda yakin mengubah harga menu ini?")) {
            menu.harga = hargaBaru;
            System.out.println("Harga menu berhasil diubah.");
            tampilkanMenu();
        } else {
            System.out.println("Perubahan harga dibatalkan. Tidak ada perubahan.");
        }
    }

    // Menghapus menu
    static void hapusMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Tidak ada menu yang dapat dihapus.");
            return;
        }

        tampilkanMenu();

        int nomor = bacaInteger("Pilih nomor menu yang akan dihapus (0 untuk kembali): ", 0, daftarMenu.size());

        if (nomor == 0) {
            return;
        }

        Menu menu = daftarMenu.get(nomor - 1);

        System.out.println("Menu yang akan dihapus : " + menu.nama);
        System.out.println("Harga                  : " + formatRupiah(menu.harga));

        if (konfirmasi("Apakah Anda yakin menghapus menu ini?")) {
            daftarMenu.remove(nomor - 1);
            System.out.println("Menu berhasil dihapus.");
            tampilkanMenu();
        } else {
            System.out.println("Penghapusan menu dibatalkan. Tidak ada perubahan.");
        }
    }

    // ===================== MAIN =====================

    public static void main(String[] args) {
        inisialisasiMenu();
        menuUtama();
        input.close();
    }
}