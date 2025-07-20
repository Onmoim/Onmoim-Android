package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.data.model.Category
import com.onmoim.core.designsystem.component.CategoryItem
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.viewmodel.GroupCategorySelectViewModel

@Composable
fun GroupCategorySelectRoute(
    groupCategorySelectViewModel: GroupCategorySelectViewModel = hiltViewModel(),
    onNavigateToGroupOpen: (categoryId: Int, categoryName: String, categoryImageUrl: String?) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val categories by groupCategorySelectViewModel.categoriesState.collectAsStateWithLifecycle()
    val selectedCategory by groupCategorySelectViewModel.selectedCategoryState.collectAsStateWithLifecycle()

    GroupCategorySelectScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickNext = {
            selectedCategory?.let {
                onNavigateToGroupOpen(it.id, it.name, it.imageUrl)
            }
        },
        selectedCategoryId = selectedCategory?.id ?: 0,
        onClickCategory = groupCategorySelectViewModel::onClickCategory,
        categories = categories
    )
}

@Composable
private fun GroupCategorySelectScreen(
    onBack: () -> Unit,
    onClickNext: () -> Unit,
    selectedCategoryId: Int,
    onClickCategory: (Category) -> Unit,
    categories: List<Category>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.group_category_select),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
            modifier = Modifier.background(Color.White),
            navigationIcon = {
                NavigationIconButton(
                    onClick = onBack
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            },
            actions = {
                if (selectedCategoryId > 0) {
                    Text(
                        text = stringResource(R.string.next),
                        modifier = Modifier
                            .clickable(
                                onClick = onClickNext,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                            .padding(
                                horizontal = 16.dp,
                                vertical = 14.dp
                            ),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.textColor
                        ),
                    )
                }
            }
        )
        Spacer(Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 43.dp,
                end = 43.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) {
                CategoryItem(
                    onClick = {
                        onClickCategory(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    selected = selectedCategoryId == it.id,
                    imageUrl = it.imageUrl,
                    label = it.name
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupCategorySelectScreenPreview() {
    OnmoimTheme {
        GroupCategorySelectScreen(
            onBack = {},
            onClickNext = {},
            selectedCategoryId = 1,
            onClickCategory = {},
            categories = List(20) {
                Category(
                    id = it + 1,
                    imageUrl = null,
                    name = "카테고리 $it"
                )
            }
        )
    }
}