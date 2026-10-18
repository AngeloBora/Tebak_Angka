
package com.example.tebak_angka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val Ungu = Color(0xFF6650A4)
private val Latar = Color(0xFFF5F1FF)
private val Hijau = Color(0xFF26865B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TebakAngkaApp()
            }
        }
    }
}

@Composable
fun TebakAngkaApp() {
    var angkaRahasia by remember {
        mutableIntStateOf(Random.nextInt(0, 101))
    }

    var tebakan by remember { mutableStateOf("") }
    var skor by remember { mutableIntStateOf(100) }
    var kesempatan by remember { mutableIntStateOf(0) }
    var selesai by remember { mutableStateOf(false) }
    var menang by remember { mutableStateOf(false) }

    var batasBawah by remember { mutableIntStateOf(0) }
    var batasAtas by remember { mutableIntStateOf(100) }

    var pesan by remember {
        mutableStateOf("Ayo, temukan angka rahasianya!")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Latar
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "🎯 TEBAK ANGKA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Ungu
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tantang dirimu menemukan angka 0–100!",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏆 SKOR KAMU",
                        color = Ungu,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$skor",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Hijau
                    )

                    LinearProgressIndicator(
                        progress = { skor / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = Hijau
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Kesempatan: ${3 - kesempatan} dari 3"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8E0FF)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💡 PETUNJUK",
                        fontWeight = FontWeight.Bold,
                        color = Ungu
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (selesai) {
                            "Angka rahasianya adalah $angkaRahasia"
                        } else {
                            "Angka berada di antara $batasBawah–$batasAtas"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Batas tebakan: $batasBawah–$batasAtas",
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = tebakan,
                onValueChange = { input ->
                    if (input.isEmpty() ||
                        input.all { it.isDigit() }
                    ) {
                        tebakan = input
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Masukkan angka 0–100")
                },
                placeholder = {
                    Text("Contoh: 50")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                enabled = !selesai
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val angka = tebakan.toIntOrNull()

                    if (angka == null || angka !in 0..100) {
                        pesan = "Masukkan angka yang valid, 0–100!"
                    } else if (
                        angka !in batasBawah..batasAtas
                    ) {
                        pesan = "Ikuti batas angka pada petunjuk!"
                    } else {
                        kesempatan++

                        if (angka == angkaRahasia) {
                            menang = true
                            selesai = true
                            pesan = "🎉 BENAR! Kamu berhasil!"
                        } else {
                            skor = (skor - 10).coerceAtLeast(0)

                            if (angka < angkaRahasia) {
                                batasBawah = maxOf(
                                    batasBawah,
                                    angka + 1
                                )
                                pesan = "⬆️ Terlalu kecil! Coba angka lebih besar."
                            } else {
                                batasAtas = minOf(
                                    batasAtas,
                                    angka - 1
                                )
                                pesan = "⬇️ Terlalu besar! Coba angka lebih kecil."
                            }

                            if (kesempatan >= 3) {
                                selesai = true
                                pesan = "Kesempatan habis! Coba main lagi."
                            }
                        }

                        tebakan = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !selesai,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ungu
                )
            ) {
                Text(
                    text = "TEBAK SEKARANG",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = pesan,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = if (menang) Hijau else Ungu
            )

            if (selesai) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Skor akhir: $skor ${
                        if (menang) "🏆" else ""
                    }",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        angkaRahasia = Random.nextInt(0, 101)
                        tebakan = ""
                        skor = 100
                        kesempatan = 0
                        selesai = false
                        menang = false
                        batasBawah = 0
                        batasAtas = 100
                        pesan = "Ayo, temukan angka rahasianya!"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Hijau
                    )
                ) {
                    Text("🔄 MAIN LAGI")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
