/*SOAL TUGAS 1
1. Input Menu Restoran : Data menu makanan dan minuman beserta harganya.
2. Pemesanan : Memesan makanan dan minuman berdasarkan daftar menu yang ditampilkan (maksimal 4 menu). Contoh format input pemesanan : Nasi Padang = 2
3. Menghitung Total Biaya : Menghitung total biaya pesanan, biaya pajak 10% dan biaya pelayanan Rp. 20.000
    Selain itu, restoran ini juga menerapkan diskon atau penawaran khusus :
    A. Diskon 10% jika total biaya keseluruhan pesanan melebihi Rp 100.000
    B. Penawaran beli satu gratis satu untuk salah satu kategori minuman jika total biaya keseluruhan pesanan melebihi Rp 50.000

4. Mencetak Struk Pesanan : Mencetak struk pesanan yang mencantumkan item-menu yang dipesan, jumlahnya, harga, total harga pemesanan, pajak dan biaya pelayanan*/

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Tugas1_Restoran {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //1. Data Menu
        //Minimal 4 Makanan dan 4 Minuman
        Menu[] menu = new Menu[8];
        menu[0] = new Menu("Nasi Uduk", 25000, "Makanan");
        menu[1] = new Menu("Nasi Rames", 30000, "Makanan");
        menu[2] = new Menu("Nasi Rawon", 35000, "Makanan");
        menu[3] = new Menu("Nasi Liwet", 15000, "Makanan");
        menu[4] = new Menu("Es Teler", 8000, "Minuman");
        menu[5] = new Menu("Es Teh", 5000, "Minuman");
        menu[6] = new Menu("Es Jeruk", 2500, "Minuman");
        menu[7] = new Menu("Kopi Susu", 15000, "Minuman");

        tampilkanMenu(menu);

        //2. Pemesanan - Maksimal 4 Menu
        System.out.println("\n=== Welcome To Warung Kang Afrizal");
        System.out.println("Format Pemesanan : Nama Menu = Jumlah (Contoh : Nasi Uduk = 2)");
        System.out.println("Jika hanya ingin pesan < 4 menu, cukup dikosongi atau tekan enter.\n");

        System.out.print("Menu Pertama : ");
        String input1 = scanner.nextLine();
        System.out.print("Menu Kedua : ");
        String input2 = scanner.nextLine();
        System.out.print("Menu Ketiga : ");
        String input3 = scanner.nextLine();
        System.out.print("Menu Keempat : ");
        String input4 = scanner.nextLine();

        //Variable untuk menampung hasil pesanan
        String nama1 = "", nama2 = "", nama3 = "", nama4 = "";
        String kategori1 = "", kategori2 = "", kategori3 = "", kategori4 = "";
        int qty1 = 0, qty2 = 0, qty3 = 0, qty4 = 0;
        double subtotal1 = 0, subtotal2 = 0, subtotal3 = 0, subtotal4 = 0;
        double harga1 = 0, harga2 = 0, harga3 = 0, harga4 = 0;

        //Proses Input Pemesanan Ke-1
        if (input1 != null && input1.trim().length() > 0) {
            String[] parts = input1.split("=");
            String namaDicari = parts[0].trim();
            int qtyDicari = Integer.parseInt(parts[1].trim());
            int idx = cariIndexMenu(namaDicari, menu);
            if (idx != -1) {
                nama1 = menu[idx].getNama();
                kategori1 = menu[idx].getKategori();
                harga1 = menu[idx].getHarga();
                qty1 = qtyDicari;
                subtotal1 = harga1 * qty1;
            } else {
                System.out.println("Peringatan : Menu \"" + namaDicari + "\" tidak ditemukan pada baris ke-1, diabaikan");
            }
        }

        //Proses Input Pemesanan Ke-2
        if (input2 != null && input2.trim().length() > 0) {
            String[] parts = input2.split("=");
            String namaDicari = parts[0].trim();
            int qtyDicari = Integer.parseInt(parts[1].trim());
            int idx = cariIndexMenu(namaDicari, menu);
            if (idx != -1) {
                nama2 = menu[idx].getNama();
                kategori2 = menu[idx].getKategori();
                harga2 = menu[idx].getHarga();
                qty2 = qtyDicari;
                subtotal2 = harga2 * qty2;
            } else {
                System.out.println("Peringatan : Menu \"" + namaDicari + "\" tidak ditemukan pada baris ke-2, diabaikan");
            }
        }

        //Proses Input Pemesanan Ke-3
        if (input3 != null && input3.trim().length() > 0) {
            String[] parts = input3.split("=");
            String namaDicari = parts[0].trim();
            int qtyDicari = Integer.parseInt(parts[1].trim());
            int idx = cariIndexMenu(namaDicari, menu);
            if (idx != -1) {
                nama3 = menu[idx].getNama();
                kategori3 = menu[idx].getKategori();
                harga3 = menu[idx].getHarga();
                qty3 = qtyDicari;
                subtotal3 = harga3 * qty3;
            } else {
                System.out.println("Peringatan : Menu \"" + namaDicari + "\" tidak ditemukan pada baris ke-3, diabaikan");
            }
        }

        //Proses Input Pemesanan Ke-4
        if (input4 != null && input4.trim().length() > 0) {
            String[] parts = input4.split("=");
            String namaDicari = parts[0].trim();
            int qtyDicari = Integer.parseInt(parts[1].trim());
            int idx = cariIndexMenu(namaDicari, menu);
            if (idx != -1) {
                nama4 = menu[idx].getNama();
                kategori4 = menu[idx].getKategori();
                harga4 = menu[idx].getHarga();
                qty4 = qtyDicari;
                subtotal4 = harga4 * qty4;
            } else {
                System.out.println("Peringatan : Menu \"" + namaDicari + "\" tidak ditemukan pada baris ke-4, diabaikan");
            }
        }

        //3. Menghitung total biaya pesanan
        double totalSebelumPromo = subtotal1 + subtotal2 + subtotal3 + subtotal4;

        // promo A = Diskon 10% jika total biaya keseluruhan pesanan > Rp 100.000
        double diskon = 0;
        if (totalSebelumPromo > 100000) {
            diskon = totalSebelumPromo * 0.10;
        }

        // promo B = Beli 1 gratis 1 untuk kategori minuman, jika total > 50.000
        double nilaiGratis = 0;
        String namaMinumanGratis = "";
        if (totalSebelumPromo > 50000) {
            if (kategori1.equals("Minuman")) {
                nilaiGratis = harga1;
                namaMinumanGratis = nama1;
            } else if (kategori2.equals("Minuman")) {
                nilaiGratis = harga2;
                namaMinumanGratis = nama2;
            } else if (kategori3.equals("Minuman")) {
                nilaiGratis = harga3;
                namaMinumanGratis = nama3;
            } else if (kategori4.equals("Minuman")) {
                nilaiGratis = harga4;
                namaMinumanGratis = nama4;
            }
        }

        double totalSetelahPromo = totalSebelumPromo - diskon - nilaiGratis;
        double pajak = totalSetelahPromo * 0.10;
        double biayaPelayanan = 20000;
        double totalAkhir = totalSetelahPromo + pajak + biayaPelayanan;

        //4. Cetak Struk Pembayaran
        cetakStruk(
            nama1, qty1, harga1, subtotal1,
            nama2, qty2, harga2, subtotal2,
            nama3, qty3, harga3, subtotal3,
            nama4, qty4, harga4, subtotal4,
            totalSebelumPromo, diskon, nilaiGratis, namaMinumanGratis,
            totalSetelahPromo, pajak, biayaPelayanan, totalAkhir
        );

        scanner.close();
    }

    private static int cariIndexMenu(String nama, Menu[] menu) {
        if (nama.equalsIgnoreCase(menu[0].getNama())) {
            return 0;
        } else if (nama.equalsIgnoreCase(menu[1].getNama())) {
            return 1;
        } else if (nama.equalsIgnoreCase(menu[2].getNama())) {
            return 2;
        } else if (nama.equalsIgnoreCase(menu[3].getNama())) {
            return 3;
        } else if (nama.equalsIgnoreCase(menu[4].getNama())) {
            return 4;
        } else if (nama.equalsIgnoreCase(menu[5].getNama())) {
            return 5;
        } else if (nama.equalsIgnoreCase(menu[6].getNama())) {
            return 6;
        } else if (nama.equalsIgnoreCase(menu[7].getNama())) {
            return 7;
        } else {
            return -1;
        }
    }

    // Tampilan Menu
    private static void tampilkanMenu(Menu[] menu) {
        System.out.println("===============================================");
        System.out.println("          WELCOME TO WARUNG KANG AFRIZAL       ");
        System.out.println("===============================================");
        System.out.println("MENU MAKANAN");
        System.out.println(String.format("%-12s", menu[0].getNama()) + "- " + formatRupiah(menu[0].getHarga()));
        System.out.println(String.format("%-12s", menu[1].getNama()) + "- " + formatRupiah(menu[1].getHarga()));
        System.out.println(String.format("%-12s", menu[2].getNama()) + "- " + formatRupiah(menu[2].getHarga()));
        System.out.println(String.format("%-12s", menu[3].getNama()) + "- " + formatRupiah(menu[3].getHarga()));
        System.out.println("\n");
        System.out.println("MENU MINUMAN");
        System.out.println(String.format("%-12s", menu[4].getNama()) + "- " + formatRupiah(menu[4].getHarga()));
        System.out.println(String.format("%-12s", menu[5].getNama()) + "- " + formatRupiah(menu[5].getHarga()));
        System.out.println(String.format("%-12s", menu[6].getNama()) + "- " + formatRupiah(menu[6].getHarga()));
        System.out.println(String.format("%-12s", menu[7].getNama()) + "- " + formatRupiah(menu[7].getHarga()));
        System.out.println("===============================================");
    }

    // CETAK STRUK
    private static void cetakStruk(
            String nama1, int qty1, double harga1, double subtotal1,
            String nama2, int qty2, double harga2, double subtotal2,
            String nama3, int qty3, double harga3, double subtotal3,
            String nama4, int qty4, double harga4, double subtotal4,
            double totalSebelumPromo, double diskon, double nilaiGratis, String namaMinumanGratis,
            double totalSetelahPromo, double pajak, double biayaPelayanan, double totalAkhir) {

        System.out.println("\n===============================================");
        System.out.println("                 STRUK PEMESANAN               ");
        System.out.println("===============================================");

        //CETAK TIAP ITEM PESANAN
        if (qty1 > 0) {
            System.out.println(nama1 + " x" + qty1 + " @" + formatRupiah(harga1) + " = " + formatRupiah(subtotal1));
        }
        if (qty2 > 0) {
            System.out.println(nama2 + " x" + qty2 + " @" + formatRupiah(harga2) + " = " + formatRupiah(subtotal2));
        }
        if (qty3 > 0) {
            System.out.println(nama3 + " x" + qty3 + " @" + formatRupiah(harga3) + " = " + formatRupiah(subtotal3));
        }
        if (qty4 > 0) {
            System.out.println(nama4 + " x" + qty4 + " @" + formatRupiah(harga4) + " = " + formatRupiah(subtotal4));
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Total Sebelum diskon / promo      : " + formatRupiah(totalSebelumPromo));

        // Info diskon 10%
        if (diskon > 0) {
            System.out.println("Diskon 10% (belanja > Rp100.000)  : -" + formatRupiah(diskon));
        } else {
            System.out.println("Diskon 10% tidak berlaku (belanja belum melebihi Rp100.000)");
        }

        // Info promo beli 1 gratis 1
        if (nilaiGratis > 0) {
            System.out.println("Promo beli 1 gratis 1 (" + namaMinumanGratis + ") : -" + formatRupiah(nilaiGratis));
        } else {
            System.out.println("Promo beli 1 gratis 1 tidak berlaku (belanja belum melebihi Rp50.000 atau tidak ada minuman)");
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Total setelah diskon / promo      : " + formatRupiah(totalSetelahPromo));
        System.out.println("Pajak (10%)                       : " + formatRupiah(pajak));
        System.out.println("Biaya Pelayanan                   : " + formatRupiah(biayaPelayanan));
        System.out.println("===============================================");
        System.out.println("TOTAL AKHIR YANG HARUS DIBAYARKAN : " + formatRupiah(totalAkhir));
        System.out.println("===============================================");
        System.out.println("         Terimakasih, Selamat Menikmati!       ");
        System.out.println("===============================================");
    }

    //convert angka ke rupiah
    private static String formatRupiah(double angka) {
        NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
        return "Rp " + format.format(angka);
    }

}

class Menu {
    private String nama;
    private double harga;
    private String kategori; //"Makanan atau Minuman"

    public Menu(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }
}