package com.kroy.sseditor.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kroy.sseditor.presentation.theme.Primary


@Composable
fun CategoryScreen(onClick: (category: String) -> Unit) {
    val categoryViewmodel: CategoryViewmodel = hiltViewModel()
    val categories: State<List<String>> = categoryViewmodel.categories.collectAsState()
    // Content for the screen goes here
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.Center,
        ){
        items(categories.value.distinct()){
            CategoryItem(category = it,onClick)
        }
    }
  //  CategoryItem(category = "android")
}

@Composable
fun CategoryItem(category: String,onClick : (category:String)->Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick(category)
            }
            .background(Primary)
            .border(1.dp, Color(0xFFEEEEEE)),
        contentAlignment = Alignment.BottomCenter

    ) {
        Text(
            text = category,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(0.dp, 20.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
