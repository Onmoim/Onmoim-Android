package com.onmoim.core.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.onmoim.R
import com.onmoim.feature.category.view.CategoryRoute
import com.onmoim.feature.home.view.HomeRoute
import com.onmoim.feature.mymeet.view.MyMeetRoute
import com.onmoim.feature.profile.view.ProfileRoute
import kotlinx.serialization.Serializable

data class TopLevelRoute<T : Any>(
    val labelId: Int,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val route: T
)

@Serializable
object HomeRoute

@Serializable
object CategoryRoute

@Serializable
object MyMeetRoute

@Serializable
object ProfileRoute

val topLevelRoutes = listOf(
    TopLevelRoute(
        labelId = R.string.home,
        selectedIconId = R.drawable.ic_home_selected,
        unselectedIconId = R.drawable.ic_home_unselected,
        route = HomeRoute
    ),
    TopLevelRoute(
        labelId = R.string.category,
        selectedIconId = R.drawable.ic_category_selected,
        unselectedIconId = R.drawable.ic_category_unselected,
        route = CategoryRoute
    ),
    TopLevelRoute(
        labelId = R.string.my_meet,
        selectedIconId = R.drawable.ic_meet_selected,
        unselectedIconId = R.drawable.ic_meet_unselected,
        route = MyMeetRoute
    ),
    TopLevelRoute(
        labelId = R.string.profile,
        selectedIconId = R.drawable.ic_profile_selected,
        unselectedIconId = R.drawable.ic_profile_unselected,
        route = ProfileRoute
    )
)

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.topLevelGraph(
    navController: NavController,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    composable<HomeRoute>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        HomeRoute(
            topBar = topBar,
            bottomBar = bottomBar
        )
    }
    composable<CategoryRoute>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        CategoryRoute(
            topBar = topBar,
            bottomBar = bottomBar
        )
    }
    composable<MyMeetRoute>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        MyMeetRoute(
            topBar = topBar,
            bottomBar = bottomBar
        )
    }
    composable<ProfileRoute>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        ProfileRoute(
            topBar = topBar,
            bottomBar = bottomBar
        )
    }
}