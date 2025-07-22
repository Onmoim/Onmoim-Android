package com.onmoim.navigation

import com.onmoim.core.ui.R
import com.onmoim.feature.category.CategoryRoute
import com.onmoim.feature.groups.navigation.MyGroupRoute
import com.onmoim.feature.home.HomeRoute
import com.onmoim.feature.profile.ProfileRoute

data class TopLevelRoute<T : Any>(
    val labelId: Int,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val route: T
)

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
        route = MyGroupRoute
    ),
    TopLevelRoute(
        labelId = R.string.profile,
        selectedIconId = R.drawable.ic_profile_selected,
        unselectedIconId = R.drawable.ic_profile_unselected,
        route = ProfileRoute
    )
)