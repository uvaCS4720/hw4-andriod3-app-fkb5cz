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
    val tags by viewModel.allTags.collectAsStateWithLifecycle()
    val selectedTag by viewModel.selectedTag.collectAsStateWithLifecycle()
    val filteredPlaces by viewModel.filteredPlacemarks.collectAsStateWithLifecycle()

    var expanded by remember {mutableStateOf(false)}

    val uvaCenter = LatLng(38.0356, -78.5034)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {Text("UVA Campus Maps")})
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
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

                ExposedDropdownMenu(
                    expanded=expanded,
                    onDismissRequest = {expanded = false}
                ) {
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            text = {Text(tag)},
                            onClick = {
                                viewModel.setSelectedTag(tag)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                filteredPlaces.forEach { place ->
                    MarkerInfoWindowContent(
                        state = MarkerState(
                            position = LatLng(place.latitude, place.longitude)
                        ),
                        title = place.name,
                        snippet = place.description
                    ) {
                        marker ->
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