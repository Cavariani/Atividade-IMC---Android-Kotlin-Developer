package carreiras.com.github.android_imc_app

import kotlin.math.pow


fun calcularImc(altura: Double, peso: Double): Double {
    // protege contra zero/negativo (retorna 0.0 para evitar crash/divisão por zero)
    if (altura <= 0.0 || peso <= 0.0) return 0.0
    val alturaEmMetros = (altura / 100.0)
    return peso / alturaEmMetros.pow(2.0)
}

fun determinarClassificacaoIMC(imc: Double): String {
    if (imc <= 0.0) return "Preencha peso e altura válidos"

    return when {
        imc < 18.5 -> "Abaixo do peso"
        imc < 25.0 -> "Peso Ideal"
        imc < 30.0 -> "Levemente acima do peso"
        imc < 35.0 -> "Obesidade Grau I"
        imc < 40.0 -> "Obesidade Grau II"
        else -> "Obesidade Grau III"
    }
}
