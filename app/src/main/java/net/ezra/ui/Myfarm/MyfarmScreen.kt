package net.ezra.ui.Myfarm
import net.ezra.ui.home.DrawerContent
import net.ezra.ui.home.MainContent
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.ezra.R
import kotlin.reflect.KProperty

@Composable
fun MyfarmScreen(navController: NavHostController)


{
LazyColumn {
    item {


    @Composable
    fun InputField(label: String, placeholder: String, keyboardType: KeyboardType) {
        TODO("Not yet implemented")
    }
    Column {



        class LocationService(private val context: Context) {

            private val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            suspend fun getLastKnownLocation(): Location? {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    return fusedLocationClient.lastLocation.await()
                }
                return null
            }

            suspend fun getLocationUpdates(
                intervalMillis: Long,
                minDistanceMeters: Float,
                onLocationChanged: (Location) -> Unit
            ) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val await = fusedLocationClient.requestLocationUpdates(
                        CurrentLocationRequest {
                            var interval = intervalMillis
                            var smallestDisplacement = minDistanceMeters
                            var priority = LocationRequest.QUALITY_HIGH_ACCURACY
                        },
                        object : LocationCallback() {
                            fun onLocationResult(locationResult: LocationResult?) {
                                locationResult ?: return
                                locationResult.locations.forEach { location ->
                                    onLocationChanged(location)
                                }
                            }
                        },
                        Looper.getMainLooper()
                    ).await()
                }
            }

            private fun CurrentLocationRequest(function: () -> Unit): CurrentLocationRequest {
                return TODO("Provide the return value")
            }
        }




        class MyViewModel(private val locationService: LocationService) : ViewModel() {

            private val _locationFlow: MutableStateFlow<Location?> = MutableStateFlow(null)
            val locationFlow: Flow<Location?> = _locationFlow.asStateFlow()

            init {
                viewModelScope.launch(Dispatchers.IO) {
                    locationService.getLastKnownLocation()?.let { location ->
                        _locationFlow.value = location
                    }
                    locationService.getLocationUpdates(10000, 0f) { location ->
                        viewModelScope.launch(Dispatchers.Main) {
                            _locationFlow.value = location
                        }
                    }
                }
            }
        }


                @Composable
                fun MyComposable(viewModel: MyViewModel) {
                    val location by viewModel.locationFlow.collectAsState()
                    location?.let {


                    }
                }


      
                @Composable
                fun InputCard() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "Field Details", style = MaterialTheme.typography.h6)

                            InputField(
                                label = "Enter Name",
                                placeholder = "Enter your name",
                                keyboardType = KeyboardType.Number
                            )
                            InputField(
                                label = "Geographical Location",
                                placeholder = "Enter location",
                                keyboardType = KeyboardType.Number
                            )
                            InputField(label = "Field Size (acres)", placeholder = "Enter size in acres", keyboardType = KeyboardType.Number)
                            InputField(label = "Field Size (hectares)", placeholder = "Enter size in hectares", keyboardType = KeyboardType.Number)

                            Button(onClick = { /* TODO: Implement mapping option */ }) {
                                Text(text = "Mapping Option")
                            }
                        }
                    }
                }

        @Composable
        fun InputField(label: String, placeholder: String, keyboardType: KeyboardType = KeyboardType.Text) {
            var text by remember { mutableStateOf(TextFieldValue()) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = label, style = MaterialTheme.typography.body1)

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (text.text.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray)
                        }
                        innerTextField()
                    },

                )
            }
        }


                @Composable
                fun CropInfoInputCard() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "Crop Information", style = MaterialTheme.typography.h6)

                            InputField(label = "Enter Name", placeholder = "Enter crop name")
                            InputField(label = "Geographical Location", placeholder = "Enter location")

                            Button(onClick = { /* TODO: Implement crop search option */ }) {
                                Text(text = "Search Crops")
                            }
                        }
                    }
                }

        @Composable
        fun InputField(label: String, placeholder: String) {
            var text by remember { mutableStateOf(TextFieldValue()) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = label, style = MaterialTheme.typography.body1)

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (text.text.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
        }

                @Composable
                fun CIIC() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "Crop Information", style = MaterialTheme.typography.h6)

                            InputField(label = "Enter Name", placeholder = "Enter crop name")
                            InputField(label = "Geographical Location", placeholder = "Enter location")

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { /* TODO: Implement delete option */ },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Delete")
                                }

                                Button(
                                    onClick = { /* TODO: Implement save option */ },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Save")
                                }
                            }
                        }
                    }
                }

        @Composable
        fun IField(label: String, placeholder: String) {
            var text by remember { mutableStateOf(TextFieldValue()) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = label, style = MaterialTheme.typography.body1)

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (text.text.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
        }



                @Composable
                fun FertiliserPlanInputCard() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.fertiliser),
                                    contentDescription = "Fertiliser Icon",
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "Fertiliser Plan", style = MaterialTheme.typography.h6)
                            }

                            InputField(label = "Enter Crop Name", placeholder = "Enter crop name")
                            InputField(label = "Geographical Location", placeholder = "Enter location")
                            InputField(label = "Field Size (acres)", placeholder = "Enter size in acres", keyboardType = KeyboardType.Number)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { /* TODO: Implement calculate fertiliser option */ },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Calculate Fertiliser")
                                }

                                Button(
                                    onClick = { /* TODO: Implement save option */ },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Save")
                                }
                            }
                        }
                    }
                }

        @Composable
        fun IF(label: String, placeholder: String, keyboardType: KeyboardType = KeyboardType.Text) {
            var text by remember { mutableStateOf(TextFieldValue()) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = label, style = MaterialTheme.typography.body1)

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (text.text.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray)
                        }
                        innerTextField()
                    },

                )
            }
        }


                @Composable
                fun FarmingTrendCard() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.trend),
                                    contentDescription = "Trending Icon",
                                    modifier = Modifier.size(48.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "Latest Farming Trend", style = MaterialTheme.typography.h6)
                            }

                            Text(
                                text = "Advisory: Consider incorporating more sustainable farming practices to improve soil health and crop yield.",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Button(
                                onClick = { /* TODO: Implement more details option */ },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                modifier = Modifier
                                    .align(Alignment.End)
                            ) {
                                Text(text = "More Details")
                            }
                        }
                    }
                }

                @Composable
                fun HarvestInfoCard() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "Harvest Information", style = MaterialTheme.typography.h6)

                            InputField(label = "Bags Harvested", placeholder = "Enter amount of bags", keyboardType = KeyboardType.Number)
                            InputField(label = "Fertiliser Consumed", placeholder = "Enter amount of fertiliser", keyboardType = KeyboardType.Number)

                            Button(
                                onClick = { /* TODO: Implement save option */ },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Save")
                            }
                        }
                    }
                }

        @Composable
        fun Input(label: String, placeholder: String, keyboardType: KeyboardType = KeyboardType.Text) {
            var text by remember { mutableStateOf(TextFieldValue()) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = label, style = MaterialTheme.typography.body1)

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (text.text.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray)
                        }
                        innerTextField()
                    },

                )
            }
        }

    }


    }
}
}

private fun Any.await(): Any {
    return TODO("Provide the return value")
}

private fun FusedLocationProviderClient.requestLocationUpdates(currentLocationRequest: CurrentLocationRequest, locationCallback: LocationCallback, mainLooper: Looper?): Any {
    return TODO("Provide the return value")
}

private operator fun Unit.getValue(nothing: Nothing?, property: KProperty<*>): Any {

    return TODO("Provide the return value")
}

private fun <T> Flow<T>.collectAsState() {

}
@Composable
fun HomeScreenPreviewLight() = MyfarmScreen(rememberNavController())
