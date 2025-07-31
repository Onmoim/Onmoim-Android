package com.onmoim.feature.profile.view

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.data.model.Profile
import com.onmoim.core.designsystem.component.CommonChip
import com.onmoim.core.designsystem.component.CommonDialog
import com.onmoim.core.designsystem.component.CommonListItem
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.profile.R
import com.onmoim.feature.profile.constant.GroupType
import com.onmoim.feature.profile.state.ProfileEvent
import com.onmoim.feature.profile.viewmodel.ProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProfileRoute(
    profileViewModel: ProfileViewModel,
    bottomBar: @Composable () -> Unit,
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToGroupList: (GroupType) -> Unit,
    onNavigateToNotificationSetting: () -> Unit
) {
    val profile by profileViewModel.profileState.collectAsStateWithLifecycle()
    val isLoading by profileViewModel.isLoading.collectAsStateWithLifecycle()
    var showWithdrawalDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showWithdrawalDialog) {
        CommonDialog(
            onDismissRequest = {
                showWithdrawalDialog = false
            },
            onClickConfirm = {
                showWithdrawalDialog = false
            },
            onClickDismiss = {
                profileViewModel.withdrawal()
                showWithdrawalDialog = false
            },
            title = stringResource(R.string.profile_somoim),
            content = stringResource(R.string.profile_withdrawal_guide_message),
            confirmText = stringResource(R.string.cancel),
            dismissText = stringResource(R.string.profile_do_withdrawal),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .pointerInteropFilter {
                    isLoading
                }
                .fillMaxSize()
        ) {
            ProfileScreen(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClickProfileEdit = onNavigateToProfileEdit,
                onClickGroup = onNavigateToGroupList,
                onClickNotificationSetting = onNavigateToNotificationSetting,
                onClickWithdrawal = {
                    showWithdrawalDialog = true
                },
                profile = profile
            )
            bottomBar()
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.WithdrawalError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.wait_and_retry),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    onClickProfileEdit: () -> Unit,
    onClickGroup: (GroupType) -> Unit,
    onClickNotificationSetting: () -> Unit,
    onClickWithdrawal: () -> Unit,
    profile: Profile?
) {
    Column(
        modifier = modifier
    ) {
        ProfileAppBar(
            onClickProfileEdit = onClickProfileEdit
        )
        Spacer(Modifier.height(16.dp))
        ProfileCard(
            modifier = Modifier.padding(horizontal = 15.dp),
            profileImgUrl = profile?.profileImgUrl,
            userName = profile?.name ?: "",
            location = profile?.location ?: "",
            birthDate = profile?.birth,
            introduction = profile?.introduction ?: "",
            interestCategories = profile?.interestCategories?.map { it.name } ?: emptyList()
        )
        Spacer(Modifier.height(8.dp))
        MyGroupStatus(
            onClickGroup = onClickGroup,
            modifier = Modifier.fillMaxWidth(),
            favoriteGroupsCount = profile?.favoriteGroupsCount ?: 0,
            recentViewedGroupsCount = profile?.recentViewedGroupsCount ?: 0,
            joinedGroupsCount = profile?.joinedGroupsCount ?: 0
        )
        Spacer(Modifier.height(8.dp))
        CommonListItem(
            onClick = onClickNotificationSetting,
            title = stringResource(R.string.profile_notification_setting),
            leading = {
                Image(
                    painter = painterResource(R.drawable.ic_alarm),
                    contentDescription = null
                )
            },
            trailing = {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        )
        Text(
            text = stringResource(R.string.profile_withdrawal),
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickWithdrawal
                ),
            style = OnmoimTheme.typography.caption1Regular.copy(
                color = OnmoimTheme.colors.gray05,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

@Composable
private fun ProfileAppBar(
    onClickProfileEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(
                top = 7.dp,
                bottom = 7.dp,
                start = 16.dp,
                end = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = OnmoimTheme.typography.title3Bold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        NavigationIconButton(
            onClick = onClickProfileEdit
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    profileImgUrl: String?,
    userName: String,
    location: String,
    birthDate: LocalDate?,
    introduction: String,
    interestCategories: List<String>,
) {
    val profilePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(profileImgUrl)
        }.build()
    )
    val profilePainterState by profilePainter.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        Row {
            Crossfade(
                targetState = profilePainterState,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            ) { state ->
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerBackground()
                        )
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = state.painter,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.ic_user_80),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = userName,
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Text(
                    text = "$location・${
                        birthDate?.let {
                            DateTimeFormatter.ofPattern("yyyy. M. D").format(it)
                        } ?: ""
                    }",
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.gray05
                    )
                )
                Text(
                    text = introduction,
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interestCategories.forEach {
                CommonChip(
                    label = it,
                    textColor = OnmoimTheme.colors.gray05,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MyGroupStatus(
    onClickGroup: (GroupType) -> Unit,
    modifier: Modifier = Modifier,
    favoriteGroupsCount: Int,
    recentViewedGroupsCount: Int,
    joinedGroupsCount: Int
) {
    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp)
        ) {
            val groupStatusItemModifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .weight(1f)
            val groupStatusItemVerticalArrangement = Arrangement.spacedBy(8.dp)
            val groupStatusItemHorizontalArrangement = Alignment.CenterHorizontally
            val groupStatusItemCountTextStyle = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
            val groupStatusItemLabelTextStyle = OnmoimTheme.typography.caption2Regular.copy(
                color = OnmoimTheme.colors.gray05
            )

            Column(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onClickGroup(GroupType.FAVORITE)
                        }
                    )
                    .then(groupStatusItemModifier),
                verticalArrangement = groupStatusItemVerticalArrangement,
                horizontalAlignment = groupStatusItemHorizontalArrangement
            ) {
                Text(
                    text = favoriteGroupsCount.toString(),
                    style = groupStatusItemCountTextStyle
                )
                Text(
                    text = stringResource(R.string.profile_favorit_group),
                    style = groupStatusItemLabelTextStyle
                )
            }
            Column(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onClickGroup(GroupType.RECENT)
                        }
                    )
                    .then(groupStatusItemModifier),
                verticalArrangement = groupStatusItemVerticalArrangement,
                horizontalAlignment = groupStatusItemHorizontalArrangement
            ) {
                Text(
                    text = recentViewedGroupsCount.toString(),
                    style = groupStatusItemCountTextStyle
                )
                Text(
                    text = stringResource(R.string.profile_recent_group),
                    style = groupStatusItemLabelTextStyle
                )
            }
            Column(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onClickGroup(GroupType.JOIN)
                        }
                    )
                    .then(groupStatusItemModifier),
                verticalArrangement = groupStatusItemVerticalArrangement,
                horizontalAlignment = groupStatusItemHorizontalArrangement
            ) {
                Text(
                    text = joinedGroupsCount.toString(),
                    style = groupStatusItemCountTextStyle
                )
                Text(
                    text = stringResource(R.string.profile_join_group),
                    style = groupStatusItemLabelTextStyle
                )
            }
        }
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    OnmoimTheme {
        ProfileScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            onClickProfileEdit = {},
            onClickGroup = {},
            onClickNotificationSetting = {},
            onClickWithdrawal = {},
            profile = Profile(
                id = 0,
                name = "name",
                birth = LocalDate.now(),
                introduction = "introduction",
                interestCategories = List(5) { Profile.Category(it, "category$it") },
                gender = "M",
                locationId = 0,
                location = "location",
                profileImgUrl = null,
                favoriteGroupsCount = 1,
                recentViewedGroupsCount = 2,
                joinedGroupsCount = 3
            )
        )
    }
}