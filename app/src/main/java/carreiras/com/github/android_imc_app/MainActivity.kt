package carreiras.com.github.android_imc_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carreiras.com.github.android_imc_app.ui.theme.AndroidimcappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidimcappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IMCScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IMCScreen(modifier: Modifier = Modifier) {

    val peso = rememberSaveable { mutableStateOf("") }
    val altura = rememberSaveable { mutableStateOf("") } // em cm
    val imc = rememberSaveable { mutableStateOf(0.0) }
    val statusImc = rememberSaveable { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ---- header ---------
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(colorResource(id = R.color.vermelho_fiap))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bmi),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = "Calculadora IMC",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
                )
            }

            // --- formulário
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Card(
                    modifier = Modifier
                        .offset(y = (-30).dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xfff9f6f6)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Seus dados",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.vermelho_fiap),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(32.dp))

                        // PESO (kg)
                        Text(
                            text = "Seu peso (kg)",
                            modifier = Modifier.padding(bottom = 8.dp),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.vermelho_fiap)
                        )
                        OutlinedTextField(
                            value = peso.value,
                            onValueChange = { peso.value = it.replace(',', '.') }, // normaliza vírgula
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ex.: 72.5") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = colorResource(id = R.color.vermelho_fiap),
                                focusedBorderColor = colorResource(id = R.color.vermelho_fiap)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )

                        Spacer(Modifier.height(16.dp))

                        // ALTURA (cm)
                        Text(
                            text = "Sua altura (cm)",
                            modifier = Modifier.padding(bottom = 8.dp),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.vermelho_fiap)
                        )
                        OutlinedTextField(
                            value = altura.value,
                            onValueChange = { altura.value = it.replace(',', '.') },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ex.: 170") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = colorResource(id = R.color.vermelho_fiap),
                                focusedBorderColor = colorResource(id = R.color.vermelho_fiap)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val pesoNum = peso.value.toDoubleOrNull()
                                val alturaCm = altura.value.toDoubleOrNull()

                                if (pesoNum == null || alturaCm == null || pesoNum <= 0.0 || alturaCm <= 0.0) {
                                    imc.value = 0.0
                                    statusImc.value = "Preencha peso e altura válidos"
                                    return@Button
                                }

                                val valorImc = calcularImc(
                                    altura = alturaCm, // em cm (conversão é feita na função)
                                    peso = pesoNum
                                )
                                imc.value = valorImc
                                statusImc.value = determinarClassificacaoIMC(valorImc)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.vermelho_fiap)
                            )
                        ) {
                            Text(
                                text = "CALCULAR",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // -- Card Resultado
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = Color(0xff329F6B)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxSize()
            ) {
                Column {
                    Text(text = "Resultado", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = statusImc.value,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = String.format("%.1f", imc.value),
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 36.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
