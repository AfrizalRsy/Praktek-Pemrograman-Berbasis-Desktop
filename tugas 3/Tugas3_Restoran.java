//============================================================================
//  PROGRAM RESTORAN - WARUNG KANG AFRIZAL (Tugas 3 - OOP (Object Oriented Programming))
//  Konsep yang dipakai : Abstract Class, Inheritance, Polymorphism, Encapsulation, Exception Handling, File I/O, ArrayList.
//  lokasi file : "D:\\MY UT\\Praktek Pemrograman Berbasis Desktop\\tugas 3\\menu.txt"
// ============================================================================
// ============================================================================

// ---------- IMPORT: memanggil library bawaan Java yang dibutuhkan ----------
import java.io.*;                          // untuk baca/tulis file (File I/O)
import java.text.NumberFormat;             // untuk format angka ke rupiah
import java.time.LocalDateTime;            // untuk tanggal & jam di struk
import java.time.format.DateTimeFormatter; // untuk mengatur format tanggal
import java.util.ArrayList;                // array dinamis (bisa tambah/hapus)
import java.util.Locale;                   // pengaturan bahasa/negara (Indonesia)
import java.util.Scanner;                  // untuk membaca input dari keyboard


// ============================================================================
//  1. EXCEPTION KHUSUS
// ============================================================================

// Ini membuat class exception sendiri. Dilempar (throw) ketika item menu
// yang dicari/diakses tidak ada di daftar menu.
class ItemTidakDitemukanException extends Exception {
    public ItemTidakDitemukanException(String pesan) {
        super(pesan); // meneruskan pesan error ke class induk (Exception)
    }
}


// ============================================================================
//  2. CLASS ABSTRAK MenuItem (class dasar / induk)
// ============================================================================

// Ini membuat class abstrak MenuItem. Class abstrak tidak bisa dibuat objeknya
// langsung (new MenuItem() tidak boleh), hanya bisa diwariskan ke class turunan.
abstract class MenuItem {

    // Atribut dibuat private -> ENCAPSULATION (tidak bisa diakses langsung
    // dari luar class, harus lewat getter/setter)
    private String nama;
    private double harga;
    private String kategori;

    // Konstruktor: dipanggil saat objek dibuat untuk mengisi atribut awal
    public MenuItem(String nama, double harga, String kategori) {
        setNama(nama);     // memakai setter agar nilai ikut divalidasi
        setHarga(harga);
        this.kategori = kategori;
    }

    // ---------- GETTER: mengambil nilai atribut ----------
    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    // ---------- SETTER: mengubah nilai atribut + validasi ----------
    public void setNama(String nama) {
        // Nama tidak boleh kosong
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        // Karakter '|' dipakai sebagai pemisah di file menu.txt, jadi dilarang
        if (nama.contains("|")) {
            throw new IllegalArgumentException("Nama tidak boleh mengandung karakter '|'.");
        }
        this.nama = nama.trim();
    }

    public void setHarga(double harga) {
        // Harga harus positif
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih besar dari nol.");
        }
        this.harga = harga;
    }

    // ---------- METODE ABSTRAK ----------
    // Metode tanpa isi. WAJIB diisi (di-override) oleh semua class turunan.
    // Inilah dasar POLYMORPHISM: nama metode sama, tampilan bisa berbeda.

    // Menampilkan informasi item ke layar
    public abstract void tampilMenu();

    // Mengubah item menjadi satu baris teks untuk disimpan ke file menu.txt
    public abstract String toFileString();

    // ---------- METODE BANTU ----------
    // Mengubah angka menjadi format rupiah, contoh: 25000 -> "Rp 25.000"
    // static = bisa dipanggil tanpa membuat objek: MenuItem.formatRupiah(...)
    public static String formatRupiah(double nilai) {
        NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
        return "Rp " + format.format(nilai);
    }
}


// ============================================================================
//  3. CLASS TURUNAN: Makanan, Minuman, Diskon  (INHERITANCE)
// ============================================================================

// Ini membuat class Makanan. "extends MenuItem" artinya Makanan mewarisi
// semua atribut dan metode dari MenuItem.
class Makanan extends MenuItem {

    // Atribut tambahan khusus makanan (mis. Nasi, Lauk, Camilan)
    private String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan"); // memanggil konstruktor class induk
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    // @Override = mengisi metode abstrak dari class induk
    @Override
    public void tampilMenu() {
        System.out.printf("%-16s %-10s %s%n",
                getNama(), "[" + jenisMakanan + "]", formatRupiah(getHarga()));
    }

    @Override
    public String toFileString() {
        // Format di file: MAKANAN|nama|harga|jenis
        return "MAKANAN|" + getNama() + "|" + getHarga() + "|" + jenisMakanan;
    }
}

// Ini membuat class Minuman (turunan MenuItem).
class Minuman extends MenuItem {

    // Atribut tambahan khusus minuman (Dingin / Panas)
    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman() {
        return jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-16s %-10s %s%n",
                getNama(), "[" + jenisMinuman + "]", formatRupiah(getHarga()));
    }

    @Override
    public String toFileString() {
        // Format di file: MINUMAN|nama|harga|jenis
        return "MINUMAN|" + getNama() + "|" + getHarga() + "|" + jenisMinuman;
    }
}

// Ini membuat class Diskon (turunan MenuItem).
// Pada Diskon, atribut "harga" dipakai sebagai BATAS MINIMAL total pesanan
// agar diskon berlaku. Contoh: harga = 100000 -> diskon berlaku jika > Rp100.000
class Diskon extends MenuItem {

    // Atribut tambahan: besar diskon dalam persen (0 - 100)
    private double diskon;

    public Diskon(String nama, double minimalPemesanan, double diskon) {
        super(nama, minimalPemesanan, "Diskon");
        setDiskon(diskon);
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        if (diskon <= 0 || diskon > 100) {
            throw new IllegalArgumentException("Diskon harus di antara 0 (tidak termasuk) dan 100 persen.");
        }
        this.diskon = diskon;
    }

    // Cek apakah diskon berlaku: true jika total pesanan melebihi batas minimal
    public boolean berlakuUntuk(double subtotal) {
        return subtotal > getHarga();
    }

    // Menghitung potongan dalam rupiah (0 jika diskon tidak berlaku)
    public double hitungPotongan(double subtotal) {
        return berlakuUntuk(subtotal) ? subtotal * diskon / 100 : 0;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-16s Diskon %.0f%% untuk pemesanan > %s%n",
                getNama(), diskon, formatRupiah(getHarga()));
    }

    @Override
    public String toFileString() {
        // Format di file: DISKON|nama|minimalPemesanan|persen
        return "DISKON|" + getNama() + "|" + getHarga() + "|" + diskon;
    }
}


// ============================================================================
//  4. CLASS Menu (pengelola seluruh item menu)
// ============================================================================

// Ini membuat class Menu. Tugasnya menyimpan & mengelola semua item
// (Makanan, Minuman, Diskon) di dalam sebuah ArrayList.
class Menu {

    // ArrayList bertipe MenuItem -> bisa menampung Makanan, Minuman, dan Diskon
    // sekaligus (karena semuanya turunan MenuItem)
    private final ArrayList<MenuItem> daftarItem = new ArrayList<>();

    public int jumlahItem() {
        return daftarItem.size();
    }

    public boolean kosong() {
        return daftarItem.isEmpty();
    }

    // Menambah item baru ke menu. Nama tidak boleh kembar.
    public void tambahItem(MenuItem item) {
        if (cariNama(item.getNama()) != null) {
            throw new IllegalArgumentException("Item \"" + item.getNama() + "\" sudah ada di menu.");
        }
        daftarItem.add(item);
    }

    // Mengambil item berdasarkan nomor tampilan (mulai dari 1).
    // Jika nomor salah -> melempar ItemTidakDitemukanException.
    public MenuItem getItem(int nomor) throws ItemTidakDitemukanException {
        if (nomor < 1 || nomor > daftarItem.size()) {
            throw new ItemTidakDitemukanException("Nomor menu " + nomor + " tidak ada.");
        }
        return daftarItem.get(nomor - 1); // index ArrayList mulai dari 0
    }

    // Mencari item berdasarkan nomor ATAU nama (input dari pengguna)
    public MenuItem cariItem(String teks) throws ItemTidakDitemukanException {
        try {
            // Coba anggap input sebagai nomor
            return getItem(Integer.parseInt(teks.trim()));
        } catch (NumberFormatException e) {
            // Jika bukan angka, berarti input berupa nama menu
            MenuItem hasil = cariNama(teks);
            if (hasil == null) {
                throw new ItemTidakDitemukanException("Menu \"" + teks + "\" tidak ditemukan.");
            }
            return hasil;
        }
    }

    // Mencari item berdasarkan nama (tidak membedakan huruf besar/kecil).
    // Mengembalikan null jika tidak ada.
    private MenuItem cariNama(String nama) {
        for (MenuItem m : daftarItem) {
            if (m.getNama().equalsIgnoreCase(nama.trim())) {
                return m;
            }
        }
        return null;
    }

    // Menghapus item berdasarkan nomor
    public void hapusItem(int nomor) throws ItemTidakDitemukanException {
        getItem(nomor); // hanya untuk mengecek; melempar exception jika tidak ada
        daftarItem.remove(nomor - 1);
    }

    // Mengambil semua item bertipe Diskon (dipakai saat menghitung total pesanan)
    public ArrayList<Diskon> getDaftarDiskon() {
        ArrayList<Diskon> hasil = new ArrayList<>();
        for (MenuItem m : daftarItem) {
            if (m instanceof Diskon) {        // cek apakah item ini bertipe Diskon
                hasil.add((Diskon) m);        // ubah (cast) ke tipe Diskon
            }
        }
        return hasil;
    }

    // Menampilkan seluruh menu, dikelompokkan per kategori
    public void tampilkanMenu() {
        System.out.println("\n===============================================");
        System.out.println("          WELCOME TO WARUNG KANG AFRIZAL       ");
        System.out.println("===============================================");
        if (daftarItem.isEmpty()) {
            System.out.println("Belum ada menu yang tersedia.");
        } else {
            tampilkanKategori("Makanan");
            tampilkanKategori("Minuman");
            tampilkanKategori("Diskon");
        }
        System.out.println("===============================================");
    }

    // Menampilkan item pada satu kategori. Nomor mengikuti urutan di ArrayList.
    private void tampilkanKategori(String kategori) {
        System.out.println("MENU " + kategori.toUpperCase());
        boolean ada = false;
        for (int i = 0; i < daftarItem.size(); i++) {
            MenuItem m = daftarItem.get(i);
            if (m.getKategori().equals(kategori)) {
                System.out.printf("%2d. ", i + 1);
                // POLYMORPHISM: yang dipanggil tampilMenu() milik Makanan,
                // Minuman, atau Diskon, tergantung jenis objek aslinya
                m.tampilMenu();
                ada = true;
            }
        }
        if (!ada) {
            System.out.println("    (belum ada)");
        }
        System.out.println();
    }

    // ---------------------------- FILE I/O -----------------------------

    // Menyimpan seluruh menu ke file teks (satu item = satu baris)
    public void simpanKeFile(String namaFile) throws IOException {
        // try-with-resources: file otomatis ditutup setelah selesai
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(namaFile)))) {
            for (MenuItem m : daftarItem) {
                out.println(m.toFileString());
            }
        }
    }

    // Memuat menu dari file teks. Baris yang formatnya salah akan dilewati.
    // Mengembalikan jumlah baris yang gagal dimuat.
    public int muatDariFile(String namaFile) throws IOException {
        daftarItem.clear(); // kosongkan dulu sebelum memuat
        int gagal = 0;
        try (BufferedReader in = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = in.readLine()) != null) { // baca baris demi baris
                if (baris.trim().isEmpty()) {
                    continue; // lewati baris kosong
                }
                try {
                    // Pecah baris berdasarkan tanda '|'
                    String[] p = baris.split("\\|");
                    double harga = Double.parseDouble(p[2]);
                    // p[0] = tipe item, menentukan objek yang dibuat
                    switch (p[0]) {
                        case "MAKANAN": tambahItem(new Makanan(p[1], harga, p[3])); break;
                        case "MINUMAN": tambahItem(new Minuman(p[1], harga, p[3])); break;
                        case "DISKON":  tambahItem(new Diskon(p[1], harga, Double.parseDouble(p[3]))); break;
                        default: throw new IllegalArgumentException("Tipe tidak dikenal");
                    }
                } catch (RuntimeException e) {
                    // Menangkap format angka salah, kolom kurang, atau data tidak valid
                    gagal++;
                    System.out.println("Baris dilewati (format salah): " + baris);
                }
            }
        }
        return gagal;
    }
}


// ============================================================================
//  5. CLASS ItemPesanan & Pesanan
// ============================================================================

// Ini membuat class ItemPesanan: satu baris pesanan = item menu + jumlahnya.
class ItemPesanan {
    private final MenuItem item;
    private int jumlah;

    public ItemPesanan(MenuItem item, int jumlah) {
        this.item = item;
        this.jumlah = jumlah;
    }

    public MenuItem getItem() { return item; }
    public int getJumlah() { return jumlah; }

    // Menambah jumlah jika item yang sama dipesan lagi
    public void tambahJumlah(int tambahan) { this.jumlah += tambahan; }

    // Total harga satu baris = harga x jumlah
    public double totalHarga() { return item.getHarga() * jumlah; }
}

// Ini membuat class Pesanan: mencatat pesanan pelanggan, menghitung total,
// serta membuat dan menyimpan struk.
class Pesanan {
    // Konstanta (nilai tetap) untuk pajak dan biaya pelayanan
    public static final double PERSEN_PAJAK = 0.10;
    public static final double BIAYA_PELAYANAN = 20000;

    // ArrayList untuk menyimpan item-item yang dipesan pelanggan
    private final ArrayList<ItemPesanan> daftarPesanan = new ArrayList<>();

    public boolean kosong() {
        return daftarPesanan.isEmpty();
    }

    // Menambah item ke pesanan. Jika item sama dipesan lagi, jumlahnya digabung.
    public void tambahItem(MenuItem item, int jumlah) {
        // Diskon bukan barang yang dipesan, jadi ditolak
        if (item instanceof Diskon) {
            throw new IllegalArgumentException("Diskon tidak dapat dipesan; diterapkan otomatis.");
        }
        if (jumlah < 1) {
            throw new IllegalArgumentException("Jumlah minimal 1.");
        }
        for (ItemPesanan p : daftarPesanan) {
            if (p.getItem() == item) {
                p.tambahJumlah(jumlah);
                return;
            }
        }
        daftarPesanan.add(new ItemPesanan(item, jumlah));
    }

    // Menghitung total harga seluruh pesanan sebelum diskon
    public double hitungSubtotal() {
        double total = 0;
        for (ItemPesanan p : daftarPesanan) {
            total += p.totalHarga();
        }
        return total;
    }

    // Menghitung total potongan dari semua diskon yang berlaku
    public double hitungDiskon(Menu menu) {
        double subtotal = hitungSubtotal();
        double potongan = 0;
        for (Diskon d : menu.getDaftarDiskon()) {
            potongan += d.hitungPotongan(subtotal);
        }
        return Math.min(potongan, subtotal); // potongan tidak boleh melebihi subtotal
    }

    // Total akhir = (subtotal - diskon) + pajak + biaya pelayanan
    public double hitungTotalAkhir(Menu menu) {
        double setelahDiskon = hitungSubtotal() - hitungDiskon(menu);
        return setelahDiskon + setelahDiskon * PERSEN_PAJAK + BIAYA_PELAYANAN;
    }

    // Membuat teks struk lengkap (dikembalikan sebagai String)
    public String buatStruk(Menu menu) {
        double subtotal = hitungSubtotal();
        double diskon = hitungDiskon(menu);
        double setelahDiskon = subtotal - diskon;
        double pajak = setelahDiskon * PERSEN_PAJAK;
        String garis = "===============================================\n";
        String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        // StringBuilder: menyusun teks panjang secara bertahap
        StringBuilder sb = new StringBuilder();
        sb.append(garis).append("                 STRUK PEMESANAN\n").append(garis);
        sb.append("Tanggal: ").append(tanggal).append("\n");
        sb.append("-----------------------------------------------\n");

        // Rincian tiap item: nama x jumlah @harga = total
        for (ItemPesanan p : daftarPesanan) {
            sb.append(p.getItem().getNama()).append(" x").append(p.getJumlah())
              .append(" @").append(MenuItem.formatRupiah(p.getItem().getHarga()))
              .append(" = ").append(MenuItem.formatRupiah(p.totalHarga())).append("\n");
        }
        sb.append("-----------------------------------------------\n");
        sb.append(String.format("%-34s: %s%n", "Total sebelum diskon", MenuItem.formatRupiah(subtotal)));

        // Tampilkan setiap diskon yang berlaku
        boolean adaDiskon = false;
        for (Diskon d : menu.getDaftarDiskon()) {
            if (d.berlakuUntuk(subtotal)) {
                adaDiskon = true;
                sb.append(String.format("%-34s: -%s%n",
                        d.getNama() + " (" + (int) d.getDiskon() + "%)",
                        MenuItem.formatRupiah(d.hitungPotongan(subtotal))));
            }
        }
        if (!adaDiskon) {
            sb.append("Tidak ada diskon yang berlaku\n");
        }

        sb.append("-----------------------------------------------\n");
        sb.append(String.format("%-34s: %s%n", "Total setelah diskon", MenuItem.formatRupiah(setelahDiskon)));
        sb.append(String.format("%-34s: %s%n", "Pajak (10%)", MenuItem.formatRupiah(pajak)));
        sb.append(String.format("%-34s: %s%n", "Biaya Pelayanan", MenuItem.formatRupiah(BIAYA_PELAYANAN)));
        sb.append(garis);
        sb.append(String.format("%-34s: %s%n", "TOTAL AKHIR", MenuItem.formatRupiah(setelahDiskon + pajak + BIAYA_PELAYANAN)));
        sb.append(garis).append("         Terimakasih, Selamat Menikmati!\n").append(garis);
        return sb.toString();
    }

    // ------------------------- FILE I/O STRUK --------------------------

    // Menyimpan struk ke file. Parameter "true" pada FileWriter = mode append,
    // artinya struk baru ditambahkan di akhir file (riwayat tidak terhapus).
    public static void simpanStruk(String namaFile, String struk) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(namaFile, true)))) {
            out.println(struk);
        }
    }

    // Membaca seluruh isi file struk untuk ditampilkan kembali
    public static String muatStruk(String namaFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = in.readLine()) != null) {
                sb.append(baris).append("\n");
            }
        }
        return sb.toString();
    }
}


// ============================================================================
//  6. CLASS UTAMA (berisi main & menu utama)
// ============================================================================

// Ini class public. Nama class HARUS sama dengan nama file: Tugas3_Restoran.java
public class Tugas3_Restoran {

    // Nama file untuk menyimpan data
    static final String FILE_MENU = "D:\\MY UT\\Praktek Pemrograman Berbasis Desktop\\tugas 3\\menu.txt";
    static final String FILE_STRUK = "D:\\MY UT\\Praktek Pemrograman Berbasis Desktop\\tugas 3\\struk.txt";

    // Scanner untuk membaca input keyboard, dipakai oleh semua metode
    static Scanner input = new Scanner(System.in);

    // Objek Menu tunggal yang menampung seluruh item restoran
    static Menu menu = new Menu();

    // ===================== METODE PEMBACA INPUT AMAN =====================
    // Semua metode ini mengulang pertanyaan sampai input yang diberikan valid.

    // Membaca bilangan bulat di antara min dan max
    static int bacaInteger(String pesan, int min, int max) {
        while (true) {
            System.out.print(pesan);
            try {
                int n = Integer.parseInt(input.nextLine().trim());
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.println("Input harus antara " + min + " dan " + max + ".");
            } catch (NumberFormatException e) {
                // Terjadi jika pengguna mengetik huruf / bukan angka
                System.out.println("Input harus berupa angka bulat.");
            }
        }
    }

    // Membaca angka desimal: harus lebih dari min dan maksimal max
    static double bacaDouble(String pesan, double min, double max) {
        while (true) {
            System.out.print(pesan);
            try {
                double n = Double.parseDouble(input.nextLine().trim());
                if (n > min && n <= max) {
                    return n;
                }
                System.out.println("Nilai harus lebih dari " + min + " dan maksimal " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    // Membaca teks: tidak boleh kosong dan tidak boleh mengandung '|'
    static String bacaTeks(String pesan) {
        while (true) {
            System.out.print(pesan);
            String s = input.nextLine().trim();
            if (!s.isEmpty() && !s.contains("|")) {
                return s;
            }
            System.out.println("Input tidak boleh kosong dan tidak boleh mengandung '|'.");
        }
    }

    // Konfirmasi: hanya menerima "Ya" atau "Tidak"
    static boolean konfirmasi(String pesan) {
        while (true) {
            System.out.print(pesan + " (Ya/Tidak): ");
            String j = input.nextLine().trim();
            if (j.equalsIgnoreCase("Ya")) return true;
            if (j.equalsIgnoreCase("Tidak")) return false;
            System.out.println("Masukkan Ya atau Tidak.");
        }
    }

    // ============================ FILE MENU ==============================

    // Mengisi menu awal (dipakai saat menu.txt belum ada)
    static void isiMenuAwal() {
        // 4 makanan
        menu.tambahItem(new Makanan("Nasi Uduk", 25000, "Nasi"));
        menu.tambahItem(new Makanan("Nasi Rames", 30000, "Nasi"));
        menu.tambahItem(new Makanan("Nasi Rawon", 35000, "Nasi"));
        menu.tambahItem(new Makanan("Nasi Liwet", 15000, "Nasi"));
        // 4 minuman
        menu.tambahItem(new Minuman("Es Teler", 8000, "Dingin"));
        menu.tambahItem(new Minuman("Es Teh", 5000, "Dingin"));
        menu.tambahItem(new Minuman("Es Jeruk", 2500, "Dingin"));
        menu.tambahItem(new Minuman("Kopi Susu", 15000, "Panas"));
        // 1 diskon: 10% jika pemesanan > Rp100.000
        menu.tambahItem(new Diskon("Diskon Pemesanan", 100000, 10));
    }

    // Memuat menu dari file saat program dimulai
    static void muatMenu() {
        try {
            int gagal = menu.muatDariFile(FILE_MENU);
            System.out.println("Menu dimuat dari " + FILE_MENU
                    + (gagal > 0 ? " (" + gagal + " baris dilewati)" : "."));
        } catch (FileNotFoundException e) {
            // File belum ada (pertama kali dijalankan) -> pakai menu awal
            System.out.println("File menu belum ada, memakai menu awal.");
            isiMenuAwal();
            simpanMenu();
        } catch (IOException e) {
            // Masalah lain saat membaca file
            System.out.println("Gagal membaca file menu: " + e.getMessage() + ". Memakai menu awal.");
            isiMenuAwal();
        }
    }

    // Menyimpan menu ke file (dipanggil setiap ada perubahan menu)
    static void simpanMenu() {
        try {
            menu.simpanKeFile(FILE_MENU);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan menu ke file: " + e.getMessage());
        }
    }

    // ============================== FITUR ================================

    // Fitur 1: menambah item baru (Makanan / Minuman / Diskon)
    static void tambahItemBaru() {
        System.out.println("\n===== TAMBAH ITEM MENU =====");
        System.out.println("1. Makanan\n2. Minuman\n3. Diskon\n4. Batal");
        int jenis = bacaInteger("Pilih jenis (1-4): ", 1, 4);
        if (jenis == 4) return;

        try {
            String nama = bacaTeks("Nama item: ");
            MenuItem baru; // variabel bertipe induk, bisa diisi Makanan/Minuman/Diskon

            if (jenis == 1) {
                double harga = bacaDouble("Harga: ", 0, 1_000_000_000);
                String jenisMakanan = bacaTeks("Jenis makanan (mis. Nasi/Lauk/Camilan): ");
                baru = new Makanan(nama, harga, jenisMakanan);
            } else if (jenis == 2) {
                double harga = bacaDouble("Harga: ", 0, 1_000_000_000);
                System.out.println("1. Dingin\n2. Panas");
                String jenisMinuman = bacaInteger("Pilih jenis minuman (1-2): ", 1, 2) == 1 ? "Dingin" : "Panas";
                baru = new Minuman(nama, harga, jenisMinuman);
            } else {
                double persen = bacaDouble("Besar diskon (%): ", 0, 100);
                double minimal = bacaDouble("Minimal total pemesanan (Rp): ", 0, 1_000_000_000);
                baru = new Diskon(nama, minimal, persen);
            }

            // Tampilkan ringkasan lalu minta konfirmasi
            System.out.println("\nItem yang akan ditambahkan:");
            baru.tampilMenu();
            if (konfirmasi("Yakin menambahkan item ini?")) {
                menu.tambahItem(baru); // bisa melempar exception jika nama kembar
                simpanMenu();
                System.out.println("Item berhasil ditambahkan.");
            } else {
                System.out.println("Penambahan dibatalkan.");
            }
        } catch (IllegalArgumentException e) {
            // Menangkap data tidak valid / nama kembar
            System.out.println("Gagal: " + e.getMessage());
        }
    }

    // Fitur tambahan: mengubah harga item
    static void ubahHarga() {
        if (menu.kosong()) { System.out.println("Menu kosong."); return; }
        menu.tampilkanMenu();
        int nomor = bacaInteger("Nomor item yang diubah (0 = kembali): ", 0, menu.jumlahItem());
        if (nomor == 0) return;
        try {
            MenuItem item = menu.getItem(nomor);
            System.out.println("Harga lama: " + MenuItem.formatRupiah(item.getHarga()));
            double baru = bacaDouble("Harga baru: ", 0, 1_000_000_000);
            if (konfirmasi("Yakin mengubah harga?")) {
                item.setHarga(baru);
                simpanMenu();
                System.out.println("Harga berhasil diubah.");
            }
        } catch (ItemTidakDitemukanException e) {
            System.out.println(e.getMessage());
        }
    }

    // Fitur tambahan: menghapus item dari menu
    static void hapusItem() {
        if (menu.kosong()) { System.out.println("Menu kosong."); return; }
        menu.tampilkanMenu();
        int nomor = bacaInteger("Nomor item yang dihapus (0 = kembali): ", 0, menu.jumlahItem());
        if (nomor == 0) return;
        try {
            MenuItem item = menu.getItem(nomor);
            if (konfirmasi("Yakin menghapus \"" + item.getNama() + "\"?")) {
                menu.hapusItem(nomor);
                simpanMenu();
                System.out.println("Item berhasil dihapus.");
            }
        } catch (ItemTidakDitemukanException e) {
            System.out.println(e.getMessage());
        }
    }

    // Fitur 3: menerima pesanan, menghitung total, dan mencetak struk
    static void terimaPesanan() {
        if (menu.kosong()) {
            System.out.println("Menu masih kosong. Pemesanan belum dapat dilakukan.");
            return;
        }
        Pesanan pesanan = new Pesanan(); // objek pesanan baru untuk pelanggan ini
        menu.tampilkanMenu();
        System.out.println("Ketik nomor atau nama menu yang dipesan, atau 'selesai'.");

        // Perulangan: terus menerima pesanan sampai pelanggan mengetik "selesai"
        while (true) {
            System.out.print("\nPilih menu: ");
            String teks = input.nextLine().trim();
            if (teks.equalsIgnoreCase("selesai")) break;

            try {
                MenuItem dipilih = menu.cariItem(teks); // exception jika menu tidak ada
                int jumlah = bacaInteger("Jumlah " + dipilih.getNama() + ": ", 1, 1000);
                pesanan.tambahItem(dipilih, jumlah);    // exception jika item berupa Diskon
                System.out.println(dipilih.getNama() + " x" + jumlah + " ditambahkan.");
            } catch (ItemTidakDitemukanException | IllegalArgumentException e) {
                // Menangkap dua jenis exception sekaligus
                System.out.println(e.getMessage());
            }
        }

        if (pesanan.kosong()) {
            System.out.println("Tidak ada pesanan yang dibuat.");
            return;
        }

        // Buat struk, tampilkan di layar, lalu simpan ke file
        String struk = pesanan.buatStruk(menu);
        System.out.println("\n" + struk);
        try {
            Pesanan.simpanStruk(FILE_STRUK, struk);
            System.out.println("Struk disimpan ke " + FILE_STRUK);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan struk: " + e.getMessage());
        }
    }

    // Fitur 4: memuat dan menampilkan riwayat struk dari file
    static void lihatRiwayatStruk() {
        try {
            System.out.println("\n" + Pesanan.muatStruk(FILE_STRUK));
        } catch (FileNotFoundException e) {
            System.out.println("Belum ada struk yang tersimpan.");
        } catch (IOException e) {
            System.out.println("Gagal membaca struk: " + e.getMessage());
        }
    }

    // ============================ MAIN ===================================

    // Titik awal program (program mulai berjalan dari sini)
    public static void main(String[] args) {
        muatMenu(); // 1) muat menu dari file saat program dimulai

        boolean jalan = true;
        while (jalan) { // 2) tampilkan menu utama berulang sampai pilih Keluar
            System.out.println("\n===============================================");
            System.out.println("              WARUNG KANG AFRIZAL              ");
            System.out.println("===============================================");
            System.out.println("1. Tambah Item Menu (Makanan/Minuman/Diskon)");
            System.out.println("2. Tampilkan Menu");
            System.out.println("3. Terima Pesanan & Cetak Struk");
            System.out.println("4. Lihat Riwayat Struk (dari file)");
            System.out.println("5. Ubah Harga Item");
            System.out.println("6. Hapus Item");
            System.out.println("7. Keluar");

            // switch: memilih tindakan sesuai angka yang diketik pengguna
            switch (bacaInteger("Pilih menu (1-7): ", 1, 7)) {
                case 1: tambahItemBaru(); break;
                case 2: menu.tampilkanMenu(); break;
                case 3: terimaPesanan(); break;
                case 4: lihatRiwayatStruk(); break;
                case 5: ubahHarga(); break;
                case 6: hapusItem(); break;
                case 7:
                    simpanMenu(); // simpan menu terakhir sebelum keluar
                    System.out.println("Terimakasih telah berkunjung ke Warung Kang Afrizal!");
                    jalan = false; // menghentikan perulangan
                    break;
            }
        }
        input.close(); // menutup Scanner
    }
}