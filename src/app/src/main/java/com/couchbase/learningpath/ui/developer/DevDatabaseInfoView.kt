package com.couchbase.learningpath.ui.developer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.couchbase.learningpath.ui.components.InventoryAppBar
import com.couchbase.learningpath.ui.theme.LearningPathTheme

@Composable
fun DevDatabaseInfoView(
    navigateUp: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: DevDatabaseInfoViewModel) {
    LearningPathTheme() {
        // A surface container using the 'background' color from the theme
        Scaffold(scaffoldState = scaffoldState,
            topBar = {
                InventoryAppBar(title = "Developer - Database Information",
                    navigationIcon = Icons.Filled.ArrowBack,
                    navigationOnClick = { navigateUp() })
            })
        {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            )
            {
                DeveloperInfoWidget(
                    viewModel.inventoryDatabaseName.value,
                    viewModel.inventoryDatabaseLocation.value,
                    viewModel.locationDatabaseName,
                    viewModel.locationDatabaseLocation,
                    viewModel.currentUsername.value,
                    viewModel.currentTeam.value,
                    viewModel.numberOfUserProfiles.value,
                    viewModel.numberOfLocations.value,
                    viewModel.numberOfProjects.value,
                )
            }
        }
    }
}

@Composable
fun DeveloperInfoWidget(
    inventoryDatabaseName: String,
    inventoryDatabaseLocation: String?,
    locationDatabaseName: () -> String?,
    locationDatabaseLocation: () -> String?,
    currentUser: String,
    currentTeam: String,
    numberOfUserProfiles: Int,
    numberOfLocations: Int,
    numberOfProjects: Int,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Username: $currentUser")
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Team: $currentTeam")
            }
        }
        item {
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )
        }
        inventoryDatabaseLocation?.let {
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Inventory Database Path", fontWeight = FontWeight.Bold)
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 2.dp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                )
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inventory Database Name", fontWeight = FontWeight.Bold)
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(inventoryDatabaseName)
            }
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("User Profile Document Count", fontWeight = FontWeight.Bold)
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$numberOfUserProfiles")
            }
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Project Count", fontWeight = FontWeight.Bold)
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$numberOfProjects")
            }
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )
        }
        locationDatabaseLocation()?.let {
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Location Database Path", fontWeight = FontWeight.Bold)
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 2.dp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                )
            }
        }
        locationDatabaseName()?.let {
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Location Database Name", fontWeight = FontWeight.Bold)
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it)
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 2.dp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                )
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Location Count", fontWeight = FontWeight.Bold)
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$numberOfLocations")
            }
            Divider(
                color = Color.LightGray,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperInfoWidgetPreview() {
    val inventoryDatabaseName = "inventoryDummy"
    val inventoryDatabaseLocation = "/blah/inventory"
    val locationDatabaseName = "locationDummy"
    val locationDatabaseLocation = "/blah/location"
    val currentUser = "demo@example.com"
    val currentTeam = "Santa Clara Team"
    val numberOfDocuments = 1000000000
    val numberOfLocations = 1000000000
    val numberOfProjects = 1000000000

    DeveloperInfoWidget(
        inventoryDatabaseName = inventoryDatabaseName,
        inventoryDatabaseLocation = inventoryDatabaseLocation,
        locationDatabaseName =  { locationDatabaseName },
        locationDatabaseLocation = { locationDatabaseLocation},
        currentUser = currentUser,
        currentTeam = currentTeam,
        numberOfUserProfiles = numberOfDocuments,
        numberOfLocations = numberOfLocations,
        numberOfProjects = numberOfProjects
    )
}
