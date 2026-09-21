package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.KundeDatabase
import com.example.data.repository.KundeRepository
import com.example.ui.components.KundeBottomBar
import com.example.ui.components.KundeTopBar
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CommentsBottomSheet
import com.example.ui.screens.CreatePostDialog
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.GroupDetailDialog
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.LanguageDialog
import com.example.ui.screens.NotificationsDialog
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.KundeViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KundeConnectApp()
            }
        }
    }
}

@Composable
fun KundeConnectApp() {
    val context = LocalContext.current
    val db = remember { KundeDatabase.getDatabase(context) }
    val repository = remember { KundeRepository(db) }
    val viewModel = remember { KundeViewModel(repository) }

    val selectedTab by viewModel.selectedTab.collectAsState()
    val language by viewModel.language.collectAsState()
    val isDataSaver by viewModel.dataSaver.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val feedFilter by viewModel.feedFilter.collectAsState()

    val posts by viewModel.filteredPosts.collectAsState()
    val groups by viewModel.allGroups.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val comments by viewModel.activeComments.collectAsState()
    val messages by viewModel.activeMessages.collectAsState()

    val showCreatePost by viewModel.showCreatePost.collectAsState()
    val showNotifications by viewModel.showNotifications.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val showAuthDialog by viewModel.showAuthDialog.collectAsState()
    val activePostForComments by viewModel.activePostForComments.collectAsState()
    val activeGroupDetail by viewModel.activeGroupDetail.collectAsState()
    val activeConversationId by viewModel.activeConversationId.collectAsState()
    val activeConversationTitle by viewModel.activeConversationTitle.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val unreadNotifsCount = notifications.count { !it.isRead }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectedTab != 2) {
                KundeTopBar(
                    language = language,
                    isDataSaver = isDataSaver,
                    isOnline = isOnline,
                    unreadNotificationsCount = unreadNotifsCount,
                    onToggleDataSaver = { viewModel.toggleDataSaver() },
                    onOpenLanguage = { viewModel.setShowLanguageDialog(true) },
                    onOpenNotifications = { viewModel.setShowNotifications(true) },
                    onToggleOnline = { viewModel.toggleOnline() }
                )
            }
        },
        bottomBar = {
            KundeBottomBar(
                selectedIndex = selectedTab,
                language = language,
                onSelectTab = { viewModel.setTab(it) }
            )
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1) {
                FloatingActionButton(
                    onClick = { viewModel.setShowCreatePost(true) },
                    containerColor = KundeGold,
                    contentColor = KundeNavyDark,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(bottom = 70.dp)
                        .testTag("fab_create_post")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Post",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> FeedScreen(
                    posts = posts,
                    currentUser = currentUser,
                    activeFilter = feedFilter,
                    language = language,
                    isDataSaver = isDataSaver,
                    onFilterChange = { viewModel.setFeedFilter(it) },
                    onOpenCreatePost = { viewModel.setShowCreatePost(true) },
                    onLikePost = { viewModel.toggleLike(it) },
                    onCommentPost = { viewModel.openComments(it) },
                    onSharePost = { viewModel.sharePost(it) },
                    onGroupClick = { groupName ->
                        val grp = groups.find { it.name == groupName }
                        if (grp != null) viewModel.openGroupDetail(grp)
                    }
                )
                1 -> GroupsScreen(
                    groups = groups,
                    language = language,
                    onToggleJoinGroup = { viewModel.toggleGroupMembership(it) },
                    onOpenGroupDetail = { viewModel.openGroupDetail(it) }
                )
                2 -> ChatScreen(
                    messages = messages,
                    activeConversationId = activeConversationId,
                    conversationTitle = activeConversationTitle,
                    language = language,
                    onSelectConversation = { id, title, avatar ->
                        viewModel.openConversation(id, title, avatar)
                    },
                    onSendMessage = { viewModel.sendMessage(it) }
                )
                3 -> ProfileScreen(
                    user = currentUser,
                    userPosts = posts.filter { it.authorId == (currentUser?.id ?: "user_me") },
                    language = language,
                    isDataSaver = isDataSaver,
                    isOnline = isOnline,
                    onToggleDataSaver = { viewModel.toggleDataSaver() },
                    onToggleOnline = { viewModel.toggleOnline() },
                    onOpenLanguage = { viewModel.setShowLanguageDialog(true) },
                    onOpenAuth = { viewModel.setShowAuthDialog(true) },
                    onUpdateBio = { viewModel.updateBio(it) },
                    onLikePost = { viewModel.toggleLike(it) },
                    onCommentPost = { viewModel.openComments(it) },
                    onSharePost = { viewModel.sharePost(it) }
                )
            }
        }
    }

    // Modal Dialogs
    if (showCreatePost) {
        CreatePostDialog(
            groups = groups,
            language = language,
            onDismiss = { viewModel.setShowCreatePost(false) },
            onSubmit = { content, category, groupTag, mediaUrl ->
                viewModel.createPost(content, category, groupTag, mediaUrl)
            }
        )
    }

    if (showNotifications) {
        NotificationsDialog(
            notifications = notifications,
            language = language,
            onDismiss = { viewModel.setShowNotifications(false) }
        )
    }

    if (showLanguageDialog) {
        LanguageDialog(
            currentLanguage = language,
            onSelectLanguage = { viewModel.setLanguage(it) },
            onDismiss = { viewModel.setShowLanguageDialog(false) }
        )
    }

    if (showAuthDialog) {
        AuthDialog(
            language = language,
            onDismiss = { viewModel.setShowAuthDialog(false) },
            onLoginSuccess = { email ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Bienvenue sur Kunde Connect ($email)")
                }
            }
        )
    }

    activePostForComments?.let { post ->
        CommentsBottomSheet(
            post = post,
            comments = comments,
            language = language,
            onDismiss = { viewModel.closeComments() },
            onAddComment = { text -> viewModel.addComment(post.id, text) }
        )
    }

    activeGroupDetail?.let { group ->
        GroupDetailDialog(
            group = group,
            language = language,
            onDismiss = { viewModel.closeGroupDetail() },
            onToggleJoin = { viewModel.toggleGroupMembership(group) },
            onOpenGroupChat = {
                viewModel.closeGroupDetail()
                viewModel.openConversation(
                    if (group.id == "group_ituri") "conv_ituri" else "conv_pasteur",
                    group.name,
                    ""
                )
            }
        )
    }
}
