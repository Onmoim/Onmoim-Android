package com.onmoim.feature.category.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.data.model.Category
import com.onmoim.core.designsystem.component.CategoryItem
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.category.viewmodel.CategoryViewModel

@Composable
fun CategoryRoute(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    onNavigateToGroupDetail: (id: Int) -> Unit,
) {
    val categories by categoryViewModel.categories.collectAsStateWithLifecycle()
    val selectedCategoryId by categoryViewModel.selectedCategoryIdState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        topBar()
        CategoryScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onCategorySelect = categoryViewModel::onCategorySelected,
            onClickGroup = onNavigateToGroupDetail
        )
        bottomBar()
    }
}

@Composable
private fun CategoryScreen(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelect: (id: Int) -> Unit,
    onClickGroup: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(204.dp),
                contentPadding = PaddingValues(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(categories) {
                    CategoryItem(
                        onClick = {
                            onCategorySelect(it.id)
                        },
                        selected = it.id == selectedCategoryId,
                        imageUrl = it.imageUrl,
                        label = it.name
                    )
                }
            }
        }
        item {
            Text(
                text = categories.find { it.id == selectedCategoryId }?.name ?: "",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 21.5.dp),
                style = OnmoimTheme.typography.body1SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
        }
        // TODO: api 연동시 수정
        items(20) {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                GroupItem(
                    onClick = {
                        onClickGroup(4)
                    },
                    imageUrl = "",
                    title = "title",
                    location = "location",
                    memberCount = 123,
                    scheduleCount = 123,
                    categoryName = "categoryName",
                    isRecommended = true,
                    isSignUp = false,
                    isOperating = false,
                    isFavorite = false
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun CategoryScreenPreview() {
    OnmoimTheme {
        CategoryScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            categories = List(20) {
                Category(
                    id = it + 1,
                    imageUrl = null,
                    name = "카테고리 $it"
                )
            },
            selectedCategoryId = 1,
            onCategorySelect = {},
            onClickGroup = {}
        )
    }
}