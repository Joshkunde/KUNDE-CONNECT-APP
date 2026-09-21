package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.GroupEntity
import com.example.data.local.entity.MessageEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.UserEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.data.repository.KundeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class KundeViewModel(private val repository: KundeRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation & UI state
    private val _selectedTab = MutableStateFlow(0) // 0: Feed, 1: Groups, 2: Messages, 3: Profile
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _feedFilter = MutableStateFlow("ALL") // ALL, FAMILLE, FOI, COMMUNAUTE
    val feedFilter: StateFlow<String> = _feedFilter.asStateFlow()

    private val _language = MutableStateFlow(Language.FRANCAIS)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _dataSaver = MutableStateFlow(true) // 2G/3G optimized default
    val dataSaver: StateFlow<Boolean> = _dataSaver.asStateFlow()

    private val _isOnline = MutableStateFlow(true) // Online / Offline toggleable
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    // Dialogs & Sheets
    private val _showCreatePost = MutableStateFlow(false)
    val showCreatePost: StateFlow<Boolean> = _showCreatePost.asStateFlow()

    private val _showNotifications = MutableStateFlow(false)
    val showNotifications: StateFlow<Boolean> = _showNotifications.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    private val _activePostForComments = MutableStateFlow<PostEntity?>(null)
    val activePostForComments: StateFlow<PostEntity?> = _activePostForComments.asStateFlow()

    private val _activeGroupDetail = MutableStateFlow<GroupEntity?>(null)
    val activeGroupDetail: StateFlow<GroupEntity?> = _activeGroupDetail.asStateFlow()

    // Chat Active Conversation
    private val _activeConversationId = MutableStateFlow("conv_ituri")
    val activeConversationId: StateFlow<String> = _activeConversationId.asStateFlow()

    private val _activeConversationTitle = MutableStateFlow("Coordination Ituri • Paix")
    val activeConversationTitle: StateFlow<String> = _activeConversationTitle.asStateFlow()

    private val _activeConversationAvatar = MutableStateFlow("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=100&q=80")
    val activeConversationAvatar: StateFlow<String> = _activeConversationAvatar.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Repository Data Streams
    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allGroups: StateFlow<List<GroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFriends: StateFlow<List<FriendEntity>> = repository.allFriends
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPages: StateFlow<List<PageEntity>> = repository.allPages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Posts filtered by Category
    val filteredPosts: StateFlow<List<PostEntity>> = combine(
        repository.allPosts,
        _feedFilter
    ) { posts, filter ->
        if (filter == "ALL") posts else posts.filter { it.category == filter }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Comments for active post
    val activeComments: StateFlow<List<CommentEntity>> = _activePostForComments.flatMapLatest { post ->
        if (post == null) flowOf(emptyList()) else repository.getComments(post.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Messages for active conversation
    val activeMessages: StateFlow<List<MessageEntity>> = _activeConversationId.flatMapLatest { convId ->
        repository.getMessages(convId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions
    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun setFeedFilter(filter: String) {
        _feedFilter.value = filter
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
        _showLanguageDialog.value = false
    }

    fun toggleDataSaver() {
        _dataSaver.value = !_dataSaver.value
    }

    fun toggleOnline() {
        _isOnline.value = !_isOnline.value
    }

    fun setShowCreatePost(show: Boolean) {
        _showCreatePost.value = show
    }

    fun setShowNotifications(show: Boolean) {
        _showNotifications.value = show
        if (show) {
            viewModelScope.launch {
                repository.markNotificationsAsRead()
            }
        }
    }

    fun setShowLanguageDialog(show: Boolean) {
        _showLanguageDialog.value = show
    }

    fun setShowAuthDialog(show: Boolean) {
        _showAuthDialog.value = show
    }

    fun openComments(post: PostEntity) {
        _activePostForComments.value = post
    }

    fun closeComments() {
        _activePostForComments.value = null
    }

    fun openGroupDetail(group: GroupEntity) {
        _activeGroupDetail.value = group
    }

    fun closeGroupDetail() {
        _activeGroupDetail.value = null
    }

    fun openConversation(id: String, title: String, avatar: String) {
        _activeConversationId.value = id
        _activeConversationTitle.value = title
        _activeConversationAvatar.value = avatar
        _selectedTab.value = 2 // Switch to chat
    }

    fun toggleLike(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleLike(post)
        }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(postId, text)
        }
    }

    fun sharePost(post: PostEntity) {
        viewModelScope.launch {
            repository.sharePost(post.id)
            val msg = AppStrings.get("shared_success", _language.value)
            _snackbarMessage.value = msg
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun toggleGroupMembership(group: GroupEntity) {
        viewModelScope.launch {
            repository.toggleGroupMembership(group)
            // Update active group detail if opened
            _activeGroupDetail.value?.let { current ->
                if (current.id == group.id) {
                    _activeGroupDetail.value = current.copy(
                        isJoined = !current.isJoined,
                        memberCount = if (!current.isJoined) current.memberCount + 1 else maxOf(0, current.memberCount - 1)
                    )
                }
            }
        }
    }

    fun createPost(content: String, category: String, groupTag: String?, mediaUrl: String?) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.createPost(
                content = content,
                category = category,
                groupTag = groupTag,
                mediaUrl = mediaUrl,
                mediaType = if (mediaUrl != null) "IMAGE" else "NONE"
            )
            _showCreatePost.value = false
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val user = currentUser.value
        viewModelScope.launch {
            repository.sendMessage(
                conversationId = _activeConversationId.value,
                text = text,
                senderName = user?.name ?: "Josué Kunde",
                senderAvatar = user?.avatarUrl ?: ""
            )
        }
    }

    fun updateBio(newBio: String) {
        viewModelScope.launch {
            repository.updateBio(newBio)
        }
    }

    fun acceptFriend(friendId: String) {
        viewModelScope.launch {
            repository.acceptFriend(friendId)
        }
    }

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            repository.deleteFriend(friendId)
        }
    }

    fun toggleFollowPage(pageId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFollowPage(pageId, currentStatus)
        }
    }
}
