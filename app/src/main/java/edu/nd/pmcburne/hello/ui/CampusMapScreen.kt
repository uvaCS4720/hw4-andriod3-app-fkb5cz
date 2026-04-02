package edu.nd.pmcburne.hello.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import edu.nd.pmcburne.hello.viewmodel.CampusMapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen (viewModel: CampusMapViewModel){
    // collect data from view model
    val tags by viewModel.allTags.collectAsStateWithLifecycle()
    val selectedTag by viewModel.selectedTag.collectAsStateWithLifecycle()
    val filteredPlaces by viewModel.filteredPlacemarks.collectAsStateWithLifecycle()

    var expanded by remember {mutableStateOf(false)}

    // default center camera on UVA
    val uvaCenter = LatLng(38.0356, -78.5034)
    // control map position and zoom
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "UVA Campus Maps",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            // dropdown filter
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                // only show selected tags
                OutlinedTextField(
                    value = selectedTag,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Filter by tag")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled=true)
                        .fillMaxWidth()
                )

                // dropdown with available tage
                ExposedDropdownMenu(
                    expanded=expanded,
                    onDismissRequest = {expanded = false}
                ) {
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            text = {Text(tag)},
                            // update selected tag in viewmodel
                            onClick = {
                                viewModel.setSelectedTag(tag)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // show how many locations displayed based on tag filter
            Text(
                text = "Showing ${filteredPlaces.size} location(s) for \"$selectedTag\"",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // loading screen if data is being fetched
            if (filteredPlaces.isEmpty()) {
                Column(modifier = Modifier.padding(24.dp)) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Loading campus locations. Please wait.")
                }
            } else {
                // wrap map in a card
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        // add a marker for each filtered location
                        filteredPlaces.forEach { place ->
                            MarkerInfoWindowContent(
                                state = MarkerState(
                                    position = LatLng(place.latitude, place.longitude)
                                ),
                                title = place.name,
                                snippet = place.description
                            ) {
                                    marker ->
                                // custom info window for descriptions
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = marker.title ?: "",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = marker.snippet ?: "",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}