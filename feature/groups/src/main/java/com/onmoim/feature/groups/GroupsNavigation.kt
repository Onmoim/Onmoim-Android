package com.onmoim.feature.groups

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.onmoim.feature.groups.view.GroupDetailRoute
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
object GroupsNavigation

@Serializable
data class GroupDetailRoute(
    val id: Int
)

fun NavController.navigateToGroupDetail(id: Int, navOptions: NavOptions? = null) {
    navigate(GroupDetailRoute(id), navOptions)
}

fun NavGraphBuilder.groupsGraph(
    navController: NavController
) {
    navigation<GroupsNavigation>(
        startDestination = GroupDetailRoute(0)
    ) {
        composable<GroupDetailRoute> { backStackEntry ->
            val id = backStackEntry.toRoute<GroupDetailRoute>().id
            val groupDetailViewModel =
                hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory> {
                    it.create(id)
                }

            GroupDetailRoute(
                groupDetailViewModel = groupDetailViewModel,
                onNavigateToComingSchedule = {},
                onNavigateToPostDetail = {}
            )
        }
    }
}