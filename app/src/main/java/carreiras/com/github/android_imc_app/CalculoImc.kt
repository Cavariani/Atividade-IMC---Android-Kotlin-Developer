package carreiras.com.github.android_imc_app

import kotlin.math.pow

fun calcularImc(altura: Double, peso: Double): Double {
    return peso / (altura / 100).pow(2.0)
}
