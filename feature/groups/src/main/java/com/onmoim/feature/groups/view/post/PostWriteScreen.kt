package com.onmoim.feature.groups.view.post

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonDialog
import com.onmoim.core.designsystem.component.CommonMenuDialog
import com.onmoim.core.designsystem.component.CommonMenuItem
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.LoadingOverlayBox
import com.onmoim.core.util.FileUtil
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.state.PostWriteEvent
import com.onmoim.feature.groups.state.PostWriteUiState
import com.onmoim.feature.groups.viewmodel.PostWriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@Composable
fun PostWriteRoute(
    postWriteViewModel: PostWriteViewModel,
    isOwner: Boolean,
    onBackAndRefresh: (BoardType) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val uiState by postWriteViewModel.uiState.collectAsStateWithLifecycle()
    var showBoardTypeSelectDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult

        scope.launch(Dispatchers.IO) {
            val copyPaths = uris.mapIndexed { index, uri ->
                async {
                    FileUtil.createTempImageFile(context, uri)?.let {
                        Pair(index, it.absolutePath)
                    }
                }
            }.awaitAll()
                .filterNotNull()
                .sortedBy { it.first }
                .map { it.second }
            postWriteViewModel.addImages(copyPaths)
        }
    }
    var showImageDeleteDialog by remember { mutableStateOf(false) }
    var selectedDeleteImagePath by remember { mutableStateOf("") }

    if (showBoardTypeSelectDialog) {
        CommonMenuDialog(
            onDismissRequest = {
                showBoardTypeSelectDialog = false
            },
            onClickCancel = {
                showBoardTypeSelectDialog = false
            }
        ) {
            BoardType.entries.filter {
                if (!isOwner) it != BoardType.NOTICE else true
            }.forEachIndexed { index, type ->
                CommonMenuItem(
                    onClick = {
                        showBoardTypeSelectDialog = false
                        postWriteViewModel.onBoardTypeChange(type)
                    },
                    label = stringResource(
                        id = when (type) {
                            BoardType.NOTICE -> R.string.notice_board
                            BoardType.INTRO -> R.string.intro_board
                            BoardType.REVIEW -> R.string.review_board
                            BoardType.FREE -> R.string.free_board
                        }
                    ),
                    includeDivider = index < BoardType.entries.lastIndex
                )
            }
        }
    }

    if (showImageDeleteDialog) {
        CommonDialog(
            onDismissRequest = {
                showImageDeleteDialog = false
            },
            title = stringResource(R.string.onmoim),
            content = stringResource(R.string.post_write_delete_image_dialog_content),
            onClickConfirm = {
                showImageDeleteDialog = false
                postWriteViewModel.deleteImagePath(selectedDeleteImagePath)
                selectedDeleteImagePath = ""
            },
            onClickDismiss = {
                showImageDeleteDialog = false
            }
        )
    }

    LoadingOverlayBox(
        loading = uiState.isLoading,
        modifier = Modifier.fillMaxSize()
    ) {
        PostWriteScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            onClickConfirm = postWriteViewModel::createPost,
            uiState = uiState,
            onClickBoardType = {
                showBoardTypeSelectDialog = true
            },
            onTitleChange = postWriteViewModel::onTitleChange,
            onContentChange = postWriteViewModel::onContentChange,
            onClickPhoto = {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onClickDeleteImage = {
                selectedDeleteImagePath = it
                showImageDeleteDialog = true
            }
        )
    }

    LaunchedEffect(Unit) {
        postWriteViewModel.event.collect { event ->
            when (event) {
                is PostWriteEvent.CreatePostFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                PostWriteEvent.CreatePostSuccess -> {
                    val boardType = requireNotNull(uiState.boardType) {
                        "boardType must not be null"
                    }
                    onBackAndRefresh(boardType)
                }
            }
        }
    }
}

@Composable
private fun PostWriteScreen(
    onBack: () -> Unit,
    onClickConfirm: () -> Unit,
    uiState: PostWriteUiState,
    onClickBoardType: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onClickPhoto: () -> Unit,
    onClickDeleteImage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
            .imePadding()
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.post_write),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
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
                Text(
                    text = stringResource(R.string.confirm),
                    modifier = Modifier
                        .clickable(
                            onClick = onClickConfirm,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = uiState.isValid()
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = if (uiState.isValid()) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray04
                        }
                    )
                )
            }
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CommonTextField(
                    value = when (uiState.boardType) {
                        BoardType.NOTICE -> stringResource(R.string.notice_board)
                        BoardType.INTRO -> stringResource(R.string.intro_board)
                        BoardType.REVIEW -> stringResource(R.string.review_board)
                        BoardType.FREE -> stringResource(R.string.free_board)
                        null -> ""
                    },
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickBoardType
                        ),
                    enabled = false,
                    textColor = OnmoimTheme.colors.primaryBlue,
                    textStyle = OnmoimTheme.typography.body2SemiBold,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.post_write_board_category),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    }
                )
                CommonTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.post_write_board_title),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    }
                )
                CommonTextField(
                    value = uiState.content,
                    onValueChange = onContentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.post_write_board_content),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    },
                    singleLine = false,
                    innerFieldAlignment = Alignment.TopStart
                )
            }
            NavigationIconButton(
                onClick = onClickPhoto,
                modifier = Modifier.padding(6.dp),
                indication = null
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_photo),
                    contentDescription = null,
                    tint = OnmoimTheme.colors.textColor
                )
            }
            Spacer(Modifier.height(22.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.imagePaths.forEach { imagePath ->
                    Box(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    onClickDeleteImage(imagePath)
                                }
                            )
                            .aspectRatio(3 / 2f)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).apply {
                                data(imagePath)
                                crossfade(true)
                            }.build(),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PostWriteScreenPreview() {
    OnmoimTheme {
        PostWriteScreen(
            onBack = {},
            onClickConfirm = {},
            uiState = PostWriteUiState(),
            onClickBoardType = {},
            onTitleChange = {},
            onContentChange = {},
            onClickPhoto = {},
            onClickDeleteImage = {}
        )
    }
}