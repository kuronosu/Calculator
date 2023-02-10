package dev.kuronosu.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kuronosu.calculator.ui.theme.CalculatorTheme
import kotlin.math.pow

val symbols = arrayOf("+", "-", "*", "/", "(", ")", "%", "^")
val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2, "^" to 3)

fun String.splitKeeping(str: String): List<String> {
    return this.split(str).flatMap { listOf(it, str) }.dropLast(1).filterNot { it.isEmpty() }
}

fun String.splitKeeping(vararg strs: String): List<String> {
    var res = listOf(this)
    strs.forEach { str ->
        res = res.flatMap { it.splitKeeping(str) }
    }
    return res
}

fun isOperand(ch: String): Boolean {
    return !symbols.contains(ch)
}

fun notGreater(stack: List<String>, i: String): Boolean {
    return if (precedence.containsKey(i) && stack.last() in precedence.keys) {
        val a = precedence[i]
        val b = precedence[stack.last()]
        a!! <= b!!
    } else {
        false
    }
}


fun createTokens(exp: String): List<String> {
    val tk = exp.replace(" ", "").splitKeeping("+", "-", "*", "/", "(", ")", "%", "^")
    return tk.filter { it.isNotEmpty() }
}


fun cal(op2: String, op1: String, i: Char): Double {
    return when (i) {
        '*' -> op2.toDouble() * op1.toDouble()
        '/' -> op2.toDouble() / op1.toDouble()
        '+' -> op2.toDouble() + op1.toDouble()
        '-' -> op2.toDouble() - op1.toDouble()
        '^' -> op2.toDouble().pow(op1.toDouble())
        '%' -> op2.toDouble() % op1.toDouble()
        else -> throw IllegalArgumentException("Operador inválido")
    }
}

fun infixToPostfix(exp: String): List<String> {
    val tokens = createTokens(exp)
    val stack = mutableListOf<String>()
    val output = mutableListOf<String>()
    for (token in tokens) {
        if (isOperand(token)) {
            output.add(token)
        } else if (token == "(") {
            stack.add(token)
        } else if (token == ")") {
            while (stack.isNotEmpty() && stack.last() != "(") {
                output.add(stack.removeLast())
            }
            if (stack.isNotEmpty() && stack.last() != "(") {
                return listOf()
            }
            if (stack.isNotEmpty()) {
                stack.removeLast()
            }
        } else {
            while (stack.isNotEmpty() && notGreater(stack, token)) {
                output.add(stack.removeLast())
            }
            stack.add(token)
        }
    }
    while (stack.isNotEmpty()) {
        output.add(stack.removeLast())
    }
    return output
}

fun evaluatePostfix(expr: List<String>): Double {
    val items = mutableListOf<Double>()
    for (i in expr) {
        if (i[0].toString() !in symbols) {
            items.add(i.toDouble())
        } else {
            val op1 = if (items.isEmpty()) 0.0 else items.removeAt(items.lastIndex)
            val op2 = if (items.isEmpty()) 0.0 else items.removeAt(items.lastIndex)
            val result = cal(op2.toString(), op1.toString(), i[0])
            items.add(result)
        }
    }
    return if (items.isEmpty()) 0.0 else items.removeAt(items.lastIndex)
}


fun evaluate(exp: String): Double {
    return try {
        evaluatePostfix(infixToPostfix(exp))
    } catch (e: Exception) {
        println(e)
        0.0
    }
}

val MediumGray = Color(0xFF2E2E2E)
val LightGray = Color(0xFF818181)
val Orange = Color(0xFFFF9800)

val buttonSpacing = 16.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var text by remember { mutableStateOf(evaluate("").toString()) }
            var errorTxt by remember { mutableStateOf("") }
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(buttonSpacing),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(buttonSpacing),
                    ) {
                        Box(
                            modifier = Modifier.weight(1f, true),
                            contentAlignment = Alignment.BottomEnd,
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = errorTxt,
                                    Modifier.padding(16.dp),
                                    textAlign = TextAlign.Right,
                                    style = MaterialTheme.typography.h5
                                )
                                Text(
                                    text = text,
                                    Modifier.padding(16.dp),
                                    textAlign = TextAlign.Right,
                                    style = MaterialTheme.typography.h2
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton(
                                symbol = "AC",
                                color = LightGray,
                                modifier = Modifier
                                    .weight(2f)
                                    .aspectRatio(2f)
                            ) {
                                text = ""
                                errorTxt = ""
                            }
                            /*
                            CalculatorButton(
                                symbol = "( )", color = Orange, modifier = Modifier.weight(1f)
                            ) {

                            }*/
                            CalculatorButton(
                                symbol = "%", color = Orange, modifier = Modifier.weight(1f)
                            ) {
                                if (text.isNotEmpty()) {
                                    text += "%"
                                }
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "/", color = Orange, modifier = Modifier.weight(1f)
                            ) {
                                if (text.isNotEmpty()) {
                                    text += "/"
                                }
                                errorTxt = ""
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton(
                                symbol = "7", modifier = Modifier.weight(1f)
                            ) {
                                text += "7"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "8", modifier = Modifier.weight(1f)
                            ) {
                                text += "8"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "9", modifier = Modifier.weight(1f)
                            ) {
                                text += "9"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "X",
                                color = Orange,
                                icon = Icons.Rounded.Close,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (text.isNotEmpty()) {
                                    text += "*"
                                }
                                errorTxt = ""
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton(
                                symbol = "4", modifier = Modifier.weight(1f)
                            ) {
                                text += "4"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "5", modifier = Modifier.weight(1f)
                            ) {
                                text += "5"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "6", modifier = Modifier.weight(1f)
                            ) {
                                text += "6"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "-", color = Orange, modifier = Modifier.weight(1f)
                            ) {
                                if (text.isNotEmpty()) {
                                    text += "-"
                                }
                                errorTxt = ""
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton(
                                symbol = "1", modifier = Modifier.weight(1f)
                            ) {
                                text += "1"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "2", modifier = Modifier.weight(1f)
                            ) {
                                text += "2"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "3", modifier = Modifier.weight(1f)
                            ) {
                                text += "3"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "plus",
                                color = Orange,
                                icon = Icons.Rounded.Add,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (text.isNotEmpty()) {
                                    text += "+"
                                }
                                errorTxt = ""
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                        ) {
                            CalculatorButton(
                                symbol = "0", modifier = Modifier.weight(1f)
                            ) {
                                text += "0"
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = ".", modifier = Modifier.weight(1f)
                            ) {
                                if (text.isNotEmpty()) {
                                    text += "."
                                }
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "",
                                icon = Icons.Outlined.ArrowBack,
                                modifier = Modifier.weight(1f)
                            ) {
                                text = text.dropLast(1)
                                errorTxt = ""
                            }
                            CalculatorButton(
                                symbol = "=", color = LightGray, modifier = Modifier.weight(1f)
                            ) {
                                try {
                                    text = evaluate(text).toString()
                                    errorTxt = ""
                                } catch (e: Exception) {
                                    errorTxt = "Entrada no valida"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    color: Color = MediumGray,
    textStyle: TextStyle = TextStyle(),
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(color)
            .clickable {
                onClick()
            }
            .then(modifier)
            .aspectRatio(1f)) {
        if (icon != null) {
            Icon(icon, symbol)
        } else {
            Text(
                text = symbol, style = textStyle, fontSize = 36.sp, color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {}
}
