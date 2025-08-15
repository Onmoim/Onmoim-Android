package com.onmoim.feature.groups.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.constant.GroupMemberRole
import com.onmoim.feature.groups.view.ComingScheduleRoute
import com.onmoim.feature.groups.view.CreateScheduleRoute
import com.onmoim.feature.groups.view.GroupCategorySelectRoute
import com.onmoim.feature.groups.view.GroupEditRoute
import com.onmoim.feature.groups.view.GroupOpenCompleteRoute
import com.onmoim.feature.groups.view.GroupOpenRoute
import com.onmoim.feature.groups.view.ScheduleManagementRoute
import com.onmoim.feature.groups.view.groupdetail.GroupDetailRoute
import com.onmoim.feature.groups.view.groupmanagement.GroupManagementRoute
import com.onmoim.feature.groups.view.meetingplacesearch.MeetingPlaceSearchRoute
import com.onmoim.feature.groups.view.mygroup.MyGroupRoute
import com.onmoim.feature.groups.view.post.PostDetailRoute
import com.onmoim.feature.groups.view.post.PostWriteRoute
import com.onmoim.feature.groups.view.post.ReplyRoute
import com.onmoim.feature.groups.viewmodel.ComingScheduleViewModel
import com.onmoim.feature.groups.viewmodel.CreateScheduleViewModel
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import com.onmoim.feature.groups.viewmodel.GroupEditViewModel
import com.onmoim.feature.groups.viewmodel.GroupManagementViewModel
import com.onmoim.feature.groups.viewmodel.GroupOpenViewModel
import com.onmoim.feature.groups.viewmodel.PostDetailViewModel
import com.onmoim.feature.groups.viewmodel.PostWriteViewModel
import com.onmoim.feature.groups.viewmodel.ReplyViewModel
import com.onmoim.feature.groups.viewmodel.ScheduleManagementViewModel
import com.onmoim.feature.location.navigation.LocationNavigationBundleKey
import com.onmoim.feature.location.navigation.navigateToLocationSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

fun NavGraphBuilder.groupsGraph(
    navController: NavController,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    navigation<GroupsNavigation>(
        startDestination = MyGroupRoute
    ) {
        composable<MyGroupRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            MyGroupRoute(
                topBar = topBar,
                bottomBar = bottomBar,
                onNavigateToGroupCategorySelect = {
                    navController.navigateToGroupCategorySelect()
                },
                onNavigateToComingSchedule = {
                    navController.navigateToComingSchedule()
                },
                onNavigateToGroupDetail = { groupId, tab ->
                    navController.navigateToGroupDetail(groupId, tab)
                }
            )
        }
        composable<ComingScheduleRoute> { backStackEntry ->
            val comingScheduleRoute = backStackEntry.toRoute<ComingScheduleRoute>()
            val comingScheduleViewModel =
                hiltViewModel<ComingScheduleViewModel, ComingScheduleViewModel.Factory> {
                    it.create(comingScheduleRoute.groupId)
                }

            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val isRefresh =
                savedStateHandle?.get<Boolean>(GroupsNavigationBundleKey.COMING_SCHEDULE_REFRESH)
                    ?: false
            savedStateHandle?.remove<Boolean>(GroupsNavigationBundleKey.COMING_SCHEDULE_REFRESH)

            ComingScheduleRoute(
                comingScheduleViewModel = comingScheduleViewModel,
                onNavigateToCreateSchedule = {
                    val groupId = requireNotNull(comingScheduleRoute.groupId) {
                        "groupId가 null이면 안됨"
                    }
                    val role = requireNotNull(comingScheduleRoute.role) {
                        "role이 null이면 안됨"
                    }
                    navController.navigateToCreateSchedule(groupId, role)
                },
                onNavigateToMeetingLocation = {

                }
            )

            LaunchedEffect(Unit) {
                if (isRefresh) {
                    withContext(Dispatchers.Default) {
                        delay(20)
                        comingScheduleViewModel.sendRefreshComingScheduleEvent()
                    }
                }
            }
        }
        composable<GroupDetailRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupDetailRoute>().id
            val tab = backStackEntry.toRoute<GroupDetailRoute>().tab
            val groupDetailViewModel =
                hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory> {
                    it.create(groupId, tab)
                }

            GroupDetailRoute(
                groupDetailViewModel = groupDetailViewModel,
                onNavigateToComingSchedule = { role ->
                    navController.navigateToComingSchedule(groupId, role)
                },
                onNavigateToPostDetail = { postId ->
                    navController.navigateToPostDetail(groupId, postId)
                },
                onNavigateToGroupManagement = {
                    navController.navigateToGroupManagement(groupId)
                },
                onNavigateToPostWrite = { isOwner ->
                    navController.navigateToPostWrite(groupId, isOwner)
                }
            )

            LaunchedEffect(Unit) {
                val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
                val isRefresh =
                    savedStateHandle?.get<Boolean>(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH)
                        ?: false
                val boardRefreshType =
                    savedStateHandle?.get<String>(GroupsNavigationBundleKey.BOARD_REFRESH_TYPE)
                val boardType = when (boardRefreshType) {
                    "NOTICE" -> BoardType.NOTICE
                    "INTRO" -> BoardType.INTRO
                    "REVIEW" -> BoardType.REVIEW
                    "FREE" -> BoardType.FREE
                    else -> null
                }

                if (isRefresh) {
                    groupDetailViewModel.fetchGroupDetailAndUserId(true)
                }

                if (boardType != null) {
                    groupDetailViewModel.sendRefreshBoardEvent(boardType)
                }

                savedStateHandle?.remove<Boolean>(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH)
                savedStateHandle?.remove<String>(GroupsNavigationBundleKey.BOARD_REFRESH_TYPE)
            }
        }
        composable<GroupCategorySelectRoute> {
            GroupCategorySelectRoute(
                onNavigateToGroupOpen = { categoryId, categoryName, categoryImageUrl ->
                    navController.navigateToGroupOpen(categoryId, categoryName, categoryImageUrl)
                }
            )
        }
        composable<GroupOpenRoute> { backStackEntry ->
            val groupOpenRoute = backStackEntry.toRoute<GroupOpenRoute>()
            val groupOpenViewModel = hiltViewModel<GroupOpenViewModel, GroupOpenViewModel.Factory> {
                it.create(groupOpenRoute.categoryId)
            }

            GroupOpenRoute(
                groupOpenViewModel = groupOpenViewModel,
                categoryName = groupOpenRoute.categoryName,
                categoryImageUrl = groupOpenRoute.categoryImageUrl,
                onNavigateToLocationSearch = {
                    navController.navigateToLocationSearch()
                },
                onNavigateToGroupOpenComplete = {
                    navController.navigateToGroupOpenComplete(it, navOptions {
                        popUpTo(MyGroupRoute)
                    })
                }
            )

            LaunchedEffect(Unit) {
                val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
                val locationId =
                    savedStateHandle?.get<Int>(LocationNavigationBundleKey.LOCATION_ID) ?: 0
                val locationName =
                    savedStateHandle?.get<String>(LocationNavigationBundleKey.LOCATION_NAME) ?: ""

                groupOpenViewModel.onLocationChange(locationId, locationName)

                savedStateHandle?.remove<String>(LocationNavigationBundleKey.LOCATION_NAME)
                savedStateHandle?.remove<Int>(LocationNavigationBundleKey.LOCATION_ID)
            }
        }
        composable<GroupOpenCompleteRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupOpenCompleteRoute>().groupId

            GroupOpenCompleteRoute(
                onNavigateToGroupDetail = {
                    navController.navigateToGroupDetail(groupId, navOptions = navOptions {
                        popUpTo(MyGroupRoute)
                    })
                }
            )
        }
        composable<GroupManagementRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupManagementRoute>().groupId
            val groupManagementViewModel =
                hiltViewModel<GroupManagementViewModel, GroupManagementViewModel.Factory> {
                    it.create(groupId)
                }

            GroupManagementRoute(
                groupManagementViewModel = groupManagementViewModel,
                onBackAndRefresh = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH, true)
                    }
                    navController.popBackStack()
                },
                onNavigateToGroupEdit = {
                    navController.navigateToGroupEdit(groupId)
                },
                onNavigateToScheduleManagement = {
                    navController.navigateToScheduleManagement(groupId)
                },
                onNavigateCreateSchedule = {
                    navController.navigateToCreateSchedule(groupId, GroupMemberRole.OWNER)
                }
            )
        }
        composable<GroupEditRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupEditRoute>().groupId
            val groupEditViewModel = hiltViewModel<GroupEditViewModel, GroupEditViewModel.Factory> {
                it.create(groupId)
            }

            GroupEditRoute(
                groupEditViewModel = groupEditViewModel,
                onBackAndRefresh = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH, true)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<ScheduleManagementRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<ScheduleManagementRoute>().groupId
            val scheduleManagementViewModel =
                hiltViewModel<ScheduleManagementViewModel, ScheduleManagementViewModel.Factory> {
                    it.create(groupId)
                }

            ScheduleManagementRoute(
                scheduleManagementViewModel = scheduleManagementViewModel
            )
        }
        composable<CreateScheduleRoute> { backStackEntry ->
            val createScheduleRoute = backStackEntry.toRoute<CreateScheduleRoute>()
            val createScheduleViewModel =
                hiltViewModel<CreateScheduleViewModel, CreateScheduleViewModel.Factory> {
                    it.create(createScheduleRoute.groupId)
                }

            CreateScheduleRoute(
                createScheduleViewModel = createScheduleViewModel,
                groupMemberRole = createScheduleRoute.groupMemberRole,
                onNavigateToMeetingPlaceSearch = {
                    navController.navigateToMeetingLocationSearch()
                },
                onBackAndRefresh = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.COMING_SCHEDULE_REFRESH, true)
                    }
                    navController.popBackStack()
                }
            )

            LaunchedEffect(Unit) {
                val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
                val placeName =
                    savedStateHandle?.get<String>(GroupsNavigationBundleKey.MEETING_PLACE_NAME)
                        ?: ""
                val latitude =
                    savedStateHandle?.get<Double>(GroupsNavigationBundleKey.MEETING_PLACE_LATITUDE)
                val longitude =
                    savedStateHandle?.get<Double>(GroupsNavigationBundleKey.MEETING_PLACE_LONGITUDE)

                createScheduleViewModel.onPlaceChange(placeName, latitude, longitude)

                savedStateHandle?.remove<String>(GroupsNavigationBundleKey.MEETING_PLACE_NAME)
                savedStateHandle?.remove<Double>(GroupsNavigationBundleKey.MEETING_PLACE_LATITUDE)
                savedStateHandle?.remove<Double>(GroupsNavigationBundleKey.MEETING_PLACE_LONGITUDE)
            }
        }
        composable<MeetingPlaceSearchRoute> {
            MeetingPlaceSearchRoute(
                onBackAndSendPlaceInfo = { placeName, latitude, longitude ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.MEETING_PLACE_NAME, placeName)
                        set(GroupsNavigationBundleKey.MEETING_PLACE_LATITUDE, latitude)
                        set(GroupsNavigationBundleKey.MEETING_PLACE_LONGITUDE, longitude)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<PostWriteRoute> { backStackEntry ->
            val postWriteRoute = backStackEntry.toRoute<PostWriteRoute>()
            val postWriteViewModel = hiltViewModel<PostWriteViewModel, PostWriteViewModel.Factory> {
                it.create(postWriteRoute.groupId)
            }

            PostWriteRoute(
                postWriteViewModel = postWriteViewModel,
                isOwner = postWriteRoute.isOwner,
                onBackAndRefresh = { type ->
                    val boardRefreshType = when (type) {
                        BoardType.NOTICE -> "NOTICE"
                        BoardType.INTRO -> "INTRO"
                        BoardType.REVIEW -> "REVIEW"
                        BoardType.FREE -> "FREE"
                    }

                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.BOARD_REFRESH_TYPE, boardRefreshType)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<PostDetailRoute> { backStackEntry ->
            val postDetailRoute = backStackEntry.toRoute<PostDetailRoute>()
            val postDetailViewModel =
                hiltViewModel<PostDetailViewModel, PostDetailViewModel.Factory> {
                    it.create(postDetailRoute.groupId, postDetailRoute.postId)
                }

            PostDetailRoute(
                postDetailViewModel = postDetailViewModel,
                onNavigateToReply = {
                    navController.navigateToReply(
                        postDetailRoute.groupId,
                        postDetailRoute.postId,
                        it
                    )
                }
            )
        }
        composable<ReplyRoute> { backStackEntry ->
            val replyRoute = backStackEntry.toRoute<ReplyRoute>()
            val replyViewModel = hiltViewModel<ReplyViewModel, ReplyViewModel.Factory> {
                it.create(
                    replyRoute.groupId,
                    replyRoute.postId,
                    replyRoute.commentId
                )
            }

            ReplyRoute(
                replyViewModel = replyViewModel
            )
        }
    }
}