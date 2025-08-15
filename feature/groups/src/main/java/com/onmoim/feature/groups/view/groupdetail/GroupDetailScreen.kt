package com.onmoim.feature.groups.view.groupdetail

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.constant.SocketConnectionState
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.model.MeetingDetail
import com.onmoim.core.data.model.Message
import com.onmoim.core.data.model.Post
import com.onmoim.core.designsystem.component.CommonButton
import com.onmoim.core.designsystem.component.CommonDialog
import com.onmoim.core.designsystem.component.CommonMenuDialog
import com.onmoim.core.designsystem.component.CommonMenuItem
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.group.GroupDetailAppBar
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.LoadingOverlayBox
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.constant.GroupDetailPostType
import com.onmoim.feature.groups.constant.GroupDetailTab
import com.onmoim.feature.groups.constant.GroupMemberRole
import com.onmoim.feature.groups.state.GroupDetailEvent
import com.onmoim.feature.groups.state.GroupDetailUiState
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@Composable
fun GroupDetailRoute(
    groupDetailViewModel: GroupDetailViewModel,
    onNavigateToComingSchedule: (GroupMemberRole) -> Unit,
    onNavigateToPostDetail: (id: Int) -> Unit,
    onNavigateToGroupManagement: () -> Unit,
    onNavigateToPostWrite: (isOwner: Boolean) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val selectedTab by groupDetailViewModel.selectedTab.collectAsStateWithLifecycle()
    val groupDetailUiState by groupDetailViewModel.groupDetailUiState.collectAsStateWithLifecycle()
    val groupDetail = (groupDetailUiState as? GroupDetailUiState.Success)?.groupDetail
    val postFilter by groupDetailViewModel.postFilterState.collectAsStateWithLifecycle()
    val allPostPagingItems = groupDetailViewModel.allPostPagingData.collectAsLazyPagingItems()
    val noticePostPagingItems = groupDetailViewModel.noticePostPagingData.collectAsLazyPagingItems()
    val introPostPagingItems = groupDetailViewModel.introPostPagingData.collectAsLazyPagingItems()
    val reviewPostPagingItems = groupDetailViewModel.reviewPostPagingData.collectAsLazyPagingItems()
    val freePostPagingItems = groupDetailViewModel.freePostPagingData.collectAsLazyPagingItems()
    val chatConnectionState by groupDetailViewModel.chatConnectionState.collectAsStateWithLifecycle()
    val prevChatMessagePagingItems =
        groupDetailViewModel.prevChatMessagePagingData.collectAsLazyPagingItems()
    val newChatMessages by groupDetailViewModel.newChatMessagesState.collectAsStateWithLifecycle()
    val message by groupDetailViewModel.messageState.collectAsStateWithLifecycle()
    var showMenuDialog by remember { mutableStateOf(false) }
    var showHostLeaveDialog by remember { mutableStateOf(false) }
    var showMemberLeaveDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    val isLoading by groupDetailViewModel.isLoading.collectAsStateWithLifecycle()

    if (showMenuDialog) {
        GroupMenuDialog(
            memberStatus = groupDetail?.memberStatus ?: MemberStatus.NONE,
            onDismissRequest = {
                showMenuDialog = false
            },
            onClickCancel = {
                showMenuDialog = false
            },
            onClickLeave = {
                showMenuDialog = false
                if (groupDetail?.memberStatus == MemberStatus.OWNER) {
                    showHostLeaveDialog = true
                } else {
                    showMemberLeaveDialog = true
                }
            },
            onClickDelete = {
                showMenuDialog = false
                groupDetailViewModel.deleteGroup()
            },
            onClickReport = {
                showMenuDialog = false
                showReportDialog = true
            }
        )
    }

    if (showHostLeaveDialog && groupDetail?.memberCount != null) {
        CommonDialog(
            title = groupDetail.title,
            content = stringResource(
                id = if (groupDetail.memberCount > 1) {
                    R.string.group_detail_host_no_leave
                } else {
                    R.string.group_detail_host_leave
                }
            ),
            onDismissRequest = {
                showHostLeaveDialog = false
            },
            onClickConfirm = {
                showHostLeaveDialog = false
                if (groupDetail.memberCount > 1) {
                    onNavigateToGroupManagement()
                } else {
                    groupDetailViewModel.leaveGroup()
                }
            },
            onClickDismiss = {
                showHostLeaveDialog = false
            }
        )
    }

    if (showMemberLeaveDialog) {
        CommonDialog(
            title = groupDetail?.title ?: "",
            content = stringResource(R.string.group_detail_member_leave),
            onDismissRequest = {
                showMemberLeaveDialog = false
            },
            onClickConfirm = {
                showMemberLeaveDialog = false
                groupDetailViewModel.leaveGroup()
            },
            onClickDismiss = {
                showHostLeaveDialog = false
            },
            confirmText = stringResource(R.string.group_detail_leave_btn)
        )
    }

    if (showReportDialog) {
        var reportContent by remember { mutableStateOf("") }

        CommonDialog(
            title = stringResource(R.string.group_detail_report_dialog_title),
            onDismissRequest = {
                showReportDialog = false
            },
            onClickConfirm = {
                showReportDialog = false
                // TODO: api 나오면 연동
            },
            onClickDismiss = {
                showReportDialog = false
            }
        ) {
            CommonTextField(
                value = reportContent,
                onValueChange = {
                    reportContent = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.group_detail_report_input_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                },
                singleLine = false,
                innerFieldAlignment = Alignment.TopStart
            )
        }
    }

    LoadingOverlayBox(
        loading = isLoading,
        modifier = Modifier.fillMaxSize()
    ) {
        GroupDetailScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            selectedTab = selectedTab,
            onTabChange = groupDetailViewModel::onTabChange,
            onClickComingSchedule = onNavigateToComingSchedule,
            onClickPost = onNavigateToPostDetail,
            groupDetailUiState = groupDetailUiState,
            onClickGroupJoin = groupDetailViewModel::joinGroup,
            onClickGroupSetting = onNavigateToGroupManagement,
            onClickMenu = {
                showMenuDialog = true
            },
            onClickFavorite = groupDetailViewModel::favoriteGroup,
            onClickMeetAttend = groupDetailViewModel::attendMeeting,
            onClickMeetLeave = groupDetailViewModel::leaveMeeting,
            postFilter = postFilter,
            onPostFilterChange = groupDetailViewModel::onPostFilterChange,
            postPagingItems = when (postFilter) {
                GroupDetailPostType.ALL -> allPostPagingItems
                GroupDetailPostType.NOTICE -> noticePostPagingItems
                GroupDetailPostType.INTRO -> introPostPagingItems
                GroupDetailPostType.REVIEW -> reviewPostPagingItems
                GroupDetailPostType.FREE -> freePostPagingItems
            },
            onClickPostWrite = onNavigateToPostWrite,
            chatConnectionState = chatConnectionState,
            prevChatMessagePagingItems = prevChatMessagePagingItems,
            newChatMessages = newChatMessages,
            message = message,
            onMessageChange = groupDetailViewModel::onMessageChange,
            onClickSendMessage = groupDetailViewModel::sendMessage
        )
    }

    GroupDetailEventHandler(
        groupDetailEventFlow = groupDetailViewModel.event,
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onRefreshBoard = { type ->
            allPostPagingItems.refresh()
            when (type) {
                BoardType.NOTICE -> noticePostPagingItems.refresh()
                BoardType.INTRO -> introPostPagingItems.refresh()
                BoardType.REVIEW -> reviewPostPagingItems.refresh()
                BoardType.FREE -> freePostPagingItems.refresh()
            }
        }
    )
}

@Composable
private fun GroupDetailEventHandler(
    groupDetailEventFlow: Flow<GroupDetailEvent>,
    onBack: () -> Unit,
    onRefreshBoard: (BoardType) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        groupDetailEventFlow.collect { event ->
            when (event) {
                is GroupDetailEvent.LeaveGroupFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupDetailEvent.LeaveGroupSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_detail_leave_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBack()
                }

                is GroupDetailEvent.DeleteGroupFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupDetailEvent.DeleteGroupSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_detail_delete_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBack()
                }

                is GroupDetailEvent.FavoriteGroupFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                is GroupDetailEvent.AttendMeetingFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupDetailEvent.AttendMeetingOverCapacity -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.attend_meeting_over_capacity),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                GroupDetailEvent.AttendMeetingSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.attend_meeting_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is GroupDetailEvent.LeaveMeetingFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupDetailEvent.LeaveMeetingSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.leave_meeting_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                GroupDetailEvent.MeetingNotFound -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.meeting_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                GroupDetailEvent.JoinGroupBanned -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_detail_group_banned),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is GroupDetailEvent.JoinGroupFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupDetailEvent.JoinGroupNotFound -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_detail_group_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                GroupDetailEvent.JoinGroupOverCapacity -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_detail_group_over_capacity),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                GroupDetailEvent.JoinGroupSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_detail_group_join_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is GroupDetailEvent.RefreshBoard -> {
                    onRefreshBoard(event.type)
                }
            }
        }
    }
}

@Composable
private fun GroupDetailScreen(
    onBack: () -> Unit,
    selectedTab: GroupDetailTab,
    onTabChange: (GroupDetailTab) -> Unit,
    onClickComingSchedule: (GroupMemberRole) -> Unit,
    onClickPost: (id: Int) -> Unit,
    groupDetailUiState: GroupDetailUiState,
    onClickGroupJoin: () -> Unit,
    onClickGroupSetting: () -> Unit,
    onClickMenu: () -> Unit,
    onClickFavorite: (Boolean) -> Unit,
    onClickMeetAttend: (meetingId: Int) -> Unit,
    onClickMeetLeave: (meetingId: Int) -> Unit,
    postFilter: GroupDetailPostType,
    onPostFilterChange: (GroupDetailPostType) -> Unit,
    postPagingItems: LazyPagingItems<Post>,
    onClickPostWrite: (isOwner: Boolean) -> Unit,
    chatConnectionState: SocketConnectionState,
    prevChatMessagePagingItems: LazyPagingItems<Message>,
    newChatMessages: List<Message>,
    message: String,
    onMessageChange: (String) -> Unit,
    onClickSendMessage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OnmoimTheme.colors.backgroundColor)
    ) {
        val groupTitle = (groupDetailUiState as? GroupDetailUiState.Success)?.groupDetail?.title
        val isFavorite =
            (groupDetailUiState as? GroupDetailUiState.Success)?.groupDetail?.isFavorite

        GroupDetailAppBar(
            title = groupTitle ?: "",
            isFavorite = isFavorite ?: false,
            onClickBack = onBack,
            onClickFavorite = {
                isFavorite?.let(onClickFavorite)
            },
            onClickShare = {},
            onClickMenu = onClickMenu
        )
        CommonTabRow(
            selectedTabIndex = GroupDetailTab.entries.indexOf(selectedTab),
            modifier = Modifier.height(40.dp),
        ) {
            GroupDetailTab.entries.forEach { tab ->
                CommonTab(
                    selected = selectedTab == tab,
                    onClick = {
                        onTabChange(tab)
                    },
                    modifier = Modifier.height(40.dp),
                    text = stringResource(
                        id = when (tab) {
                            GroupDetailTab.HOME -> R.string.group_detail_tab_home
                            GroupDetailTab.POST -> R.string.group_detail_tab_post
                            GroupDetailTab.CHAT -> R.string.group_chat
                        }
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (groupDetailUiState) {
                is GroupDetailUiState.Error -> {
                    Text(
                        text = groupDetailUiState.t.message ?: "error",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                GroupDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is GroupDetailUiState.Success -> {
                    val groupDetail = groupDetailUiState.groupDetail
                    val memberStatus = groupDetail.memberStatus

                    when (selectedTab) {
                        GroupDetailTab.HOME -> {
                            GroupDetailHomeContainer(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .background(OnmoimTheme.colors.gray01)
                                    .fillMaxSize()
                                    .padding(bottom = 20.dp)
                                    .navigationBarsPadding(),
                                title = groupDetail.title,
                                imageUrl = groupDetail.imageUrl,
                                location = groupDetail.location,
                                category = groupDetail.category,
                                memberCount = groupDetail.memberCount,
                                description = groupDetail.description,
                                meetings = groupDetail.meetingList,
                                memberStatus = groupDetail.memberStatus,
                                onClickComingSchedule = {
                                    val role = when (groupDetail.memberStatus) {
                                        MemberStatus.OWNER -> GroupMemberRole.OWNER
                                        MemberStatus.MEMBER -> GroupMemberRole.MEMBER
                                        else -> null
                                    }
                                    role?.let(onClickComingSchedule)
                                },
                                onClickMeetAttend = onClickMeetAttend,
                                onClickMeetLeave = onClickMeetLeave,
                                onClickGroupSetting = onClickGroupSetting
                            )
                            if (groupDetail.memberStatus == MemberStatus.NONE) {
                                CommonButton(
                                    onClick = onClickGroupJoin,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 60.dp),
                                    label = stringResource(R.string.group_detail_group_join),
                                    enabledContainerColor = OnmoimTheme.colors.primaryMint
                                )
                            }
                        }

                        GroupDetailTab.POST -> {
                            GroupDetailPostContainer(
                                onClickPost = onClickPost,
                                modifier = Modifier.fillMaxSize(),
                                selectedFilter = postFilter,
                                onFilterChange = onPostFilterChange,
                                postPagingItems = postPagingItems
                            )
                            if (memberStatus == MemberStatus.OWNER || memberStatus == MemberStatus.MEMBER) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 15.dp, bottom = 40.dp)
                                        .align(Alignment.BottomEnd)
                                        .clickable {
                                            onClickPostWrite(memberStatus == MemberStatus.OWNER)
                                        }
                                        .background(
                                            color = OnmoimTheme.colors.primaryBlue,
                                            shape = CircleShape
                                        )
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_pencil),
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        GroupDetailTab.CHAT -> {
                            GroupDetailChatContainer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .imePadding(),
                                userId = groupDetailUiState.userId,
                                chatConnectionState = chatConnectionState,
                                prevChatMessagePagingItems = prevChatMessagePagingItems,
                                newChatMessages = newChatMessages,
                                onClickProfile = {},
                                message = message,
                                onMessageChange = onMessageChange,
                                onClickSend = onClickSendMessage
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupMenuDialog(
    memberStatus: MemberStatus,
    onDismissRequest: () -> Unit,
    onClickCancel: () -> Unit,
    onClickLeave: () -> Unit,
    onClickDelete: () -> Unit,
    onClickReport: () -> Unit
) {
    CommonMenuDialog(
        onDismissRequest = onDismissRequest,
        onClickCancel = onClickCancel
    ) {
        when (memberStatus) {
            MemberStatus.OWNER -> {
                CommonMenuItem(
                    onClick = onClickLeave,
                    label = stringResource(R.string.group_detail_group_leave)
                )
                CommonMenuItem(
                    onClick = onClickDelete,
                    label = stringResource(R.string.group_delete),
                    includeDivider = false
                )
            }

            MemberStatus.MEMBER -> {
                CommonMenuItem(
                    onClick = onClickReport,
                    label = stringResource(R.string.group_detail_group_report)
                )
                CommonMenuItem(
                    onClick = onClickLeave,
                    label = stringResource(R.string.group_detail_group_leave),
                    includeDivider = false
                )
            }

            else -> {
                CommonMenuItem(
                    onClick = onClickReport,
                    label = stringResource(R.string.group_detail_group_report),
                    includeDivider = false
                )
            }
        }
    }
}

private fun getFakePosts(): List<Post> {
    return listOf(
        Post(
            id = 1,
            title = "첫 번째 게시물",
            content = "이것은 첫 번째 게시물의 내용입니다.",
            name = "작성자1",
            profileImageUrl = null,
            type = PostType.NOTICE,
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
            imageUrls = emptyList(),
            likeCount = 10,
            isLiked = false,
            commentCount = 5
        ),
        Post(
            id = 2,
            title = "두 번째 게시물",
            content = "이것은 두 번째 게시물의 내용입니다.",
            name = "작성자2",
            profileImageUrl = null,
            type = PostType.FREE,
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
            imageUrls = emptyList(),
            likeCount = 5,
            isLiked = true,
            commentCount = 2
        )
    )
}

private fun getFakeGroupoDetail(memberStatus: MemberStatus): GroupDetail {
    return GroupDetail(
        id = 1,
        title = "카페에서 문장 한 모금",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
        category = "인문학/책/글",
        categoryIconUrl = "https://picsum.photos/200",
        location = "연남동",
        memberCount = 123,
        imageUrl = "https://picsum.photos/200",
        isFavorite = false,
        meetingList = listOf(
            MeetingDetail(
                id = 1,
                title = "퇴근 후 독서 정모: 각자 독서",
                placeName = "카페 언노운",
                startDate = LocalDateTime.now().plusDays(2),
                cost = 10000,
                joinCount = 6,
                capacity = 8,
                attendance = false,
                isLightning = false,
                imgUrl = null,
                latitude = 0.0,
                longitude = 0.0
            ),
            MeetingDetail(
                id = 2,
                title = "제목제목",
                placeName = "장소장소",
                startDate = LocalDateTime.now().plusDays(7),
                cost = 10000,
                joinCount = 6,
                capacity = 8,
                attendance = false,
                isLightning = false,
                imgUrl = null,
                latitude = 0.0,
                longitude = 0.0
            )
        ),
        memberStatus = memberStatus,
        capacity = 10
    )
}

private fun getFakeChatMessages(): List<Message> {
    return List(20) {
        Message(
            messageSequence = it,
            groupId = it,
            senderId = it,
            userName = "userName $it",
            profileImageUrl = null,
            content = "content $it",
            sendDateTime = LocalDateTime.now(),
            isOwner = it % 2 == 0
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupDetailScreenForHomePreview1() {
    val messagePagingItems =
        MutableStateFlow(PagingData.from(getFakeChatMessages())).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupDetailScreen(
            onBack = {},
            selectedTab = GroupDetailTab.HOME,
            onTabChange = {},
            onClickComingSchedule = {},
            onClickPost = {},
            groupDetailUiState = GroupDetailUiState.Success(
                getFakeGroupoDetail(MemberStatus.NONE),
                0
            ),
            onClickGroupJoin = {},
            onClickGroupSetting = {},
            onClickMenu = {},
            onClickFavorite = {},
            onClickMeetAttend = {},
            onClickMeetLeave = {},
            postFilter = GroupDetailPostType.ALL,
            onPostFilterChange = {},
            postPagingItems = flowOf(PagingData.from(getFakePosts())).collectAsLazyPagingItems(),
            onClickPostWrite = {},
            chatConnectionState = SocketConnectionState.Connected,
            prevChatMessagePagingItems = messagePagingItems,
            newChatMessages = emptyList(),
            message = "",
            onMessageChange = {},
            onClickSendMessage = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupDetailScreenForHomePreview2() {
    val messagePagingItems =
        MutableStateFlow(PagingData.from(getFakeChatMessages())).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupDetailScreen(
            onBack = {},
            selectedTab = GroupDetailTab.HOME,
            onTabChange = {},
            onClickComingSchedule = {},
            onClickPost = {},
            groupDetailUiState = GroupDetailUiState.Success(
                getFakeGroupoDetail(MemberStatus.OWNER),
                0
            ),
            onClickGroupJoin = {},
            onClickGroupSetting = {},
            onClickMenu = {},
            onClickFavorite = {},
            onClickMeetAttend = {},
            onClickMeetLeave = {},
            postFilter = GroupDetailPostType.ALL,
            onPostFilterChange = {},
            postPagingItems = flowOf(PagingData.from(getFakePosts())).collectAsLazyPagingItems(),
            onClickPostWrite = {},
            chatConnectionState = SocketConnectionState.Connected,
            prevChatMessagePagingItems = messagePagingItems,
            newChatMessages = emptyList(),
            message = "",
            onMessageChange = {},
            onClickSendMessage = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupDetailScreenForPostPreview() {
    val messagePagingItems =
        MutableStateFlow(PagingData.from(getFakeChatMessages())).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupDetailScreen(
            onBack = {},
            selectedTab = GroupDetailTab.POST,
            onTabChange = {},
            onClickComingSchedule = {},
            onClickPost = {},
            groupDetailUiState = GroupDetailUiState.Success(
                getFakeGroupoDetail(MemberStatus.OWNER),
                0
            ),
            onClickGroupJoin = {},
            onClickGroupSetting = {},
            onClickMenu = {},
            onClickFavorite = {},
            onClickMeetAttend = {},
            onClickMeetLeave = {},
            postFilter = GroupDetailPostType.ALL,
            onPostFilterChange = {},
            postPagingItems = MutableStateFlow(PagingData.from(getFakePosts())).collectAsLazyPagingItems(),
            onClickPostWrite = {},
            chatConnectionState = SocketConnectionState.Connected,
            prevChatMessagePagingItems = messagePagingItems,
            newChatMessages = emptyList(),
            message = "",
            onMessageChange = {},
            onClickSendMessage = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupDetailScreenForChatPreview() {
    val messagePagingItems =
        MutableStateFlow(PagingData.from(getFakeChatMessages())).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupDetailScreen(
            onBack = {},
            selectedTab = GroupDetailTab.CHAT,
            onTabChange = {},
            onClickComingSchedule = {},
            onClickPost = {},
            groupDetailUiState = GroupDetailUiState.Success(
                getFakeGroupoDetail(MemberStatus.OWNER),
                0
            ),
            onClickGroupJoin = {},
            onClickGroupSetting = {},
            onClickMenu = {},
            onClickFavorite = {},
            onClickMeetAttend = {},
            onClickMeetLeave = {},
            postFilter = GroupDetailPostType.ALL,
            onPostFilterChange = {},
            postPagingItems = emptyFlow<PagingData<Post>>().collectAsLazyPagingItems(),
            onClickPostWrite = {},
            chatConnectionState = SocketConnectionState.Connected,
            prevChatMessagePagingItems = messagePagingItems,
            newChatMessages = emptyList(),
            message = "",
            onMessageChange = {},
            onClickSendMessage = {}
        )
    }
}
