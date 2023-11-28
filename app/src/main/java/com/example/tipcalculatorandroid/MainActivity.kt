package com.example.tipcalculatorandroid

import android.os.Bundle
import android.text.Layout.Alignment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculatorandroid.ui.theme.TipCalculatorAndroidTheme
import java.text.NumberFormat
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculateTip()
                }
            }
        }
    }
}

@Composable
fun CalculateTip() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }


    val amount = amountInput.toDoubleOrNull() ?: 0.0    //elvis operator that returns 0.0 if field is null
    val tip = calculateTip(amount,tipPercent,roundUp)



    Column(modifier = Modifier
        .statusBarsPadding()
        .padding(40.dp)
        .verticalScroll(rememberScrollState())
        .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,

        ) {

        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp),
        )

        EditNumberField(label = R.string.bill_amount,value = amountInput, onValueChange = {amountInput = it },
//            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next)
            ,modifier = Modifier
                .padding(top = 32.dp, bottom = 32.dp)
                .fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        EditNumberField(label = R.string.how_was_the_service, value = tipInput, onValueChange = {tipInput = it },

            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier
                .padding(top = 30.dp, bottom = 32.dp)
                .fillMaxWidth())

        RoundTheTip(roundUp = roundUp, onRoundUpChanged = {roundUp = it},
            modifier = Modifier.padding(top = 28.dp, bottom = 32.dp))

        Text(text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall)


        Spacer(modifier = Modifier.height(150.dp))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(@StringRes label: Int,
//                    @DrawableRes leadingIcon: Int,
                    value: String,
                    onValueChange: (String) -> Unit,
                    keyboardOptions : KeyboardOptions,
                    modifier: Modifier = Modifier){



    TextField(
        value = value,
        onValueChange = onValueChange,
//        leadingIcon = { Icon(painter = painterResource(id = leadingIcon),null ) },
        label = { Text(text = stringResource(id = label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = Modifier,
    )

}


@Composable
fun RoundTheTip(roundUp: Boolean,onRoundUpChanged: (Boolean) -> Unit, modifier : Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),

        horizontalArrangement = Arrangement.Center
    ){
        Text(text = stringResource(id = R.string.round_up_tip))

        Switch(checked = roundUp, onCheckedChange = onRoundUpChanged,
            modifier = modifier
                .wrapContentWidth()
                .fillMaxWidth())
    }
}



@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double, roundUp: Boolean): String{
    var tip = tipPercent/100 * amount
    if (roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipCalculator() {
    TipCalculatorAndroidTheme {
        CalculateTip()
    }
}