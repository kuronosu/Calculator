package dev.kuronosu.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.rounded.*
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

fun evaluate(exp: String): Double {
    var num = ""
    var symbol = '+'
    var result = 0.0

    for (i in exp) {
        if (i in '0'..'9' || i == '.')
            num += i
        else {
            when (symbol) {
                '+' -> result += num.toDouble()
                '-' -> result -= num.toDouble()
                '*' -> result *= num.toDouble()
                '/' -> result /= num.toDouble()
                '%' -> result %= num.toDouble()
            }
            num = ""
            symbol = i
        }
    }

    //To calculate the divide by 4 ( result/4 ) in this case
    when (symbol) {
        '+' -> result += num.toDouble()
        '-' -> result -= num.toDouble()
        '*' -> result *= num.toDouble()
        '/' -> result /= num.toDouble()
        '%' -> result %= num.toDouble()
    }
    return result
}

val MediumGray = Color(0xFF2E2E2E)
val LightGray = Color(0xFF818181)
val Orange = Color(0xFFFF9800)

val buttonSpacing = 16.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var text by remember { mutableStateOf("") }
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
