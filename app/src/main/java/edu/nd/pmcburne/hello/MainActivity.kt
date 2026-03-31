package edu.nd.pmcburne.hello

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.nd.pmcburne.hello.data.local.AppDatabase
import edu.nd.pmcburne.hello.data.repository.PlacemarkRepository
import edu.nd.pmcburne.hello.ui.CampusMapScreen
import edu.nd.pmcburne.hello.ui.theme.MyApplicationTheme
import edu.nd.pmcburne.hello.viewmodel.CampusMapViewModel
import edu.nd.pmcburne.hello.viewmodel.CampusMapViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: CampusMapViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val repo = PlacemarkRepository(db.placemarkDao())
        CampusMapViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampusMapScreen(viewModel)
        }
    }
}