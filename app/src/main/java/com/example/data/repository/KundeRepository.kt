package com.example.data.repository

import com.example.data.local.KundeDatabase
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.GroupEntity
import com.example.data.local.entity.MessageEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PageEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class KundeRepository(private val db: KundeDatabase) {

    private val postDao = db.postDao()
    private val commentDao = db.commentDao()
    private val groupDao = db.groupDao()
    private val messageDao = db.messageDao()
    private val notificationDao = db.notificationDao()
    private val userDao = db.userDao()
    private val friendDao = db.friendDao()
    private val pageDao = db.pageDao()

    val allPosts: Flow<List<PostEntity>> = postDao.getAllPosts()
    val allGroups: Flow<List<GroupEntity>> = groupDao.getAllGroups()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val currentUser: Flow<UserEntity?> = userDao.getCurrentUser()
    val allFriends: Flow<List<FriendEntity>> = friendDao.getAllFriends()
    val allPages: Flow<List<PageEntity>> = pageDao.getAllPages()

    fun getComments(postId: String): Flow<List<CommentEntity>> = commentDao.getCommentsForPost(postId)

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.getMessagesForConversation(conversationId)

    suspend fun checkAndSeedInitialData() {
        if (postDao.getCount() == 0) {
            seedDatabase()
        }
    }

    private suspend fun seedDatabase() {
        val now = System.currentTimeMillis()

        // 1. Current user
        val mainUser = UserEntity(
            id = "user_me",
            name = "Josué Kunde",
            handle = "@josue_kunde",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
            bio = "Fils du Congo • Engagé pour la Foi, la Famille et la Paix en Ituri • Kinshasa 🇨🇩",
            location = "Kinshasa, RDC",
            followersCount = 1420,
            followingCount = 380,
            isCurrentUser = true
        )
        userDao.insertUser(mainUser)

        // 2. Groups
        val groups = listOf(
            GroupEntity(
                id = "group_ituri",
                name = "Ensemble pour l'Ituri, unis pour la paix",
                description = "Plateforme citoyenne et fraternelle de solidarité pour la paix durable, la réconciliation et le soutien aux familles déplacées en Ituri et dans l'Est de la RDC.",
                category = "COMMUNAUTE",
                memberCount = 14820,
                isJoined = true,
                bannerColorHex = 0xFF0B1F3A,
                location = "Bunia • Ituri"
            ),
            GroupEntity(
                id = "group_chretiens",
                name = "Chrétiens Unis de Kinshasa & Diaspora",
                description = "Partage biblique quotidien, prières pour la nation congolaise, édification spirituelle et entraide fraternelle.",
                category = "FOI",
                memberCount = 8940,
                isJoined = true,
                bannerColorHex = 0xFF163259,
                location = "Kinshasa • RDC"
            ),
            GroupEntity(
                id = "group_entrepreneurs",
                name = "Entrepreneurs Congo 243",
                description = "Réseau d'innovateurs, commerçants et jeunes bâtisseurs de l'économie locale congolaise.",
                category = "COMMUNAUTE",
                memberCount = 6120,
                isJoined = false,
                bannerColorHex = 0xFF1E6B52,
                location = "Goma • Lubumbashi • Kinshasa"
            ),
            GroupEntity(
                id = "group_famille",
                name = "Famille & Éducation Kongo",
                description = "Conseils matrimoniaux, éducation des enfants selon nos valeurs africaines et foi chrétienne.",
                category = "FAMILLE",
                memberCount = 4350,
                isJoined = false,
                bannerColorHex = 0xFF8A3B14,
                location = "RDC & Diaspora"
            )
        )
        groupDao.insertGroups(groups)

        // 3. Posts
        val posts = listOf(
            PostEntity(
                id = "post_1",
                authorId = "author_ituri",
                authorName = "Coordination Paix Ituri",
                authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80",
                authorLocation = "Bunia, Ituri 🇨🇩",
                timestamp = now - (25 * 60 * 1000), // 25 mins ago
                content = "🕊️ Solidarité pour l'Ituri : Grâce à vos prières et vos dons via la Familia Kunde Connect, 120 kits scolaires et vivres ont été distribués aux orphelins de Bunia ce matin. La paix commence par l'amour du prochain !",
                mediaUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=600&q=80",
                mediaType = "IMAGE",
                category = "COMMUNAUTE",
                groupTag = "Ensemble pour l'Ituri, unis pour la paix",
                likesCount = 342,
                commentsCount = 28,
                sharesCount = 54,
                isLiked = true
            ),
            PostEntity(
                id = "post_2",
                authorId = "author_pasteur",
                authorName = "Pasteur Emmanuel Makiese",
                authorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80",
                authorLocation = "Kinshasa, Gombe 🇨🇩",
                timestamp = now - (2 * 3600 * 1000), // 2 hours ago
                content = "« Ne crains rien, car je suis avec toi; Ne promène pas des regards inquiets, car je suis ton Dieu » (Ésaïe 41:10).\n\nQue la grâce et la paix de notre Seigneur reposent sur chaque foyer congolais en ce dimanche béni. Prions pour l'unité de nos familles et pour le relèvement de notre cher pays. 🙏🏾🇨🇩",
                mediaUrl = null,
                mediaType = "NONE",
                category = "FOI",
                groupTag = "Chrétiens Unis de Kinshasa & Diaspora",
                likesCount = 589,
                commentsCount = 47,
                sharesCount = 89,
                isLiked = false
            ),
            PostEntity(
                id = "post_3",
                authorId = "author_maman",
                authorName = "Maman Marie Claire Tshimanga",
                authorAvatar = "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?auto=format&fit=crop&w=150&q=80",
                authorLocation = "Lubumbashi 🇨🇩",
                timestamp = now - (5 * 3600 * 1000), // 5 hours ago
                content = "Dans notre culture africaine, la richesse véritable d'une personne ne se compte pas en dollars, mais dans la paix de sa maison et l'éducation de ses enfants. Préservons nos valeurs d'écoute, de respect des aînés et de solidarité fraternelle.",
                mediaUrl = "https://images.unsplash.com/photo-1511895426328-dc8714191300?auto=format&fit=crop&w=600&q=80",
                mediaType = "IMAGE",
                category = "FAMILLE",
                groupTag = "Famille & Éducation Kongo",
                likesCount = 215,
                commentsCount = 14,
                sharesCount = 19,
                isLiked = true
            ),
            PostEntity(
                id = "post_4",
                authorId = "user_me",
                authorName = "Josué Kunde",
                authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80",
                authorLocation = "Kinshasa, RDC 🇨🇩",
                timestamp = now - (12 * 3600 * 1000), // 12 hours ago
                content = "Bienvenue sur Kunde Connect ! Une application légère, rapide et bâtie pour nous, pensée pour connecter les cœurs congolais partout dans le monde avec nos valeurs fondamentales : Famille, Foi, Communauté. Biso nionso elongo !",
                mediaUrl = null,
                mediaType = "NONE",
                category = "COMMUNAUTE",
                groupTag = null,
                likesCount = 412,
                commentsCount = 35,
                sharesCount = 61,
                isLiked = false
            )
        )
        postDao.insertPosts(posts)

        // 4. Comments
        val comments = listOf(
            CommentEntity(
                id = "c_1",
                postId = "post_1",
                authorName = "Grâce Mwamba",
                authorAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=100&q=80",
                content = "Matondo mingi mpo na bana ya Ituri ! Que Dieu bénisse cette noble initiative 🙏",
                timestamp = now - (18 * 60 * 1000)
            ),
            CommentEntity(
                id = "c_2",
                postId = "post_1",
                authorName = "Patrick Kanyinda",
                authorAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=100&q=80",
                content = "Fier d'avoir contribué. Nous devons rester unis pour la paix dans tout l'Est de notre RDC.",
                timestamp = now - (12 * 60 * 1000)
            ),
            CommentEntity(
                id = "c_3",
                postId = "post_2",
                authorName = "Dorcas Kabengele",
                authorAvatar = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=100&q=80",
                content = "Amen Pasteur ! Dieu bénisse notre RDC !",
                timestamp = now - (1 * 3600 * 1000)
            )
        )
        commentDao.insertComments(comments)

        // 5. Messages
        val messages = listOf(
            MessageEntity(
                id = "m_1",
                conversationId = "conv_ituri",
                senderName = "Coordination Ituri",
                senderAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=100&q=80",
                content = "Mbote ndeko Josué, merci pour ton relais sur la campagne de paix en Ituri !",
                timestamp = now - (40 * 60 * 1000),
                isFromMe = false
            ),
            MessageEntity(
                id = "m_2",
                conversationId = "conv_ituri",
                senderName = "Josué Kunde",
                senderAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=100&q=80",
                content = "C'est un devoir pour nous tous. Comment se passe la distribution à Bunia cet après-midi ?",
                timestamp = now - (35 * 60 * 1000),
                isFromMe = true
            ),
            MessageEntity(
                id = "m_3",
                conversationId = "conv_ituri",
                senderName = "Coordination Ituri",
                senderAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=100&q=80",
                content = "Très bien par la grâce de Dieu ! Les mamans de la paroisse préparent les colis alimentaires.",
                timestamp = now - (20 * 60 * 1000),
                isFromMe = false
            ),
            MessageEntity(
                id = "m_4",
                conversationId = "conv_pasteur",
                senderName = "Pasteur Emmanuel",
                senderAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=100&q=80",
                content = "Paix du Christ frère Josué. As-tu pu consulter le thème de notre veillée de prière ?",
                timestamp = now - (3 * 3600 * 1000),
                isFromMe = false
            )
        )
        messageDao.insertMessages(messages)

        // 6. Notifications
        val notifications = listOf(
            NotificationEntity(
                id = "notif_1",
                title = "Nouveau don pour l'Ituri",
                description = "La campagne « Ensemble pour l'Ituri » a atteint 85% de son objectif grâce à la communauté !",
                timestamp = now - (15 * 60 * 1000),
                type = "GROUP",
                isRead = false
            ),
            NotificationEntity(
                id = "notif_2",
                title = "Nouveau « J'aime » de Maman Marie",
                description = "Maman Marie Claire a aimé votre publication de bienvenue sur Kunde Connect.",
                timestamp = now - (2 * 3600 * 1000),
                type = "LIKE",
                isRead = false
            ),
            NotificationEntity(
                id = "notif_3",
                title = "Prière collective nationale",
                description = "Rejoignez le groupe Chrétiens Unis ce soir à 20h pour la paix en RDC.",
                timestamp = now - (6 * 3600 * 1000),
                type = "PRAYER",
                isRead = true
            )
        )
        notificationDao.insertNotifications(notifications)

        // 7. Friends (SQL: friends table)
        val initialFriends = listOf(
            FriendEntity(
                id = "fr_1",
                sender = "u_sarah",
                receiver = "user_me",
                receiverName = "Sarah Mwamba",
                receiverAvatar = "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?auto=format&fit=crop&w=200&q=80",
                mutualFriendsCount = 14,
                status = "pending",
                createdAt = now - (1 * 3600 * 1000)
            ),
            FriendEntity(
                id = "fr_2",
                sender = "u_patrick",
                receiver = "user_me",
                receiverName = "Patrick Kalonji",
                receiverAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80",
                mutualFriendsCount = 28,
                status = "pending",
                createdAt = now - (4 * 3600 * 1000)
            ),
            FriendEntity(
                id = "fr_3",
                sender = "u_grace",
                receiver = "user_me",
                receiverName = "Grâce Ilunga",
                receiverAvatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=200&q=80",
                mutualFriendsCount = 9,
                status = "pending",
                createdAt = now - (8 * 3600 * 1000)
            ),
            FriendEntity(
                id = "fr_4",
                sender = "user_me",
                receiver = "u_daniel",
                receiverName = "Daniel Mukendi",
                receiverAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=200&q=80",
                mutualFriendsCount = 42,
                status = "accepted",
                createdAt = now - (24 * 3600 * 1000)
            )
        )
        friendDao.insertFriends(initialFriends)

        // 8. Pages (SQL: pages table)
        val initialPages = listOf(
            PageEntity(
                id = "pg_1",
                name = "Kunde Business",
                description = "Solutions digitales & opportunités d'affaires en RDC",
                ownerId = "user_me",
                followers = 2430,
                initials = "KB",
                isFollowing = true
            ),
            PageEntity(
                id = "pg_2",
                name = "Ituri Renaissance",
                description = "Initiative pour le relèvement communautaire et la paix",
                ownerId = "u_admin",
                followers = 5120,
                initials = "IR",
                isFollowing = false
            ),
            PageEntity(
                id = "pg_3",
                name = "Gospel DRC & Éveil",
                description = "Chants, méditations et louanges pour les familles",
                ownerId = "u_pasteur",
                followers = 8900,
                initials = "GD",
                isFollowing = true
            ),
            PageEntity(
                id = "pg_4",
                name = "Marché Central Kinshasa",
                description = "Annonces, commerce équitable et livraisons locales",
                ownerId = "u_market",
                followers = 3410,
                initials = "MC",
                isFollowing = false
            )
        )
        pageDao.insertPages(initialPages)
    }

    suspend fun createPost(
        content: String,
        category: String,
        groupTag: String?,
        mediaUrl: String?,
        mediaType: String
    ) {
        val user = currentUser.firstOrNull()
        val newPost = PostEntity(
            id = "post_" + UUID.randomUUID().toString().take(8),
            authorId = user?.id ?: "user_me",
            authorName = user?.name ?: "Josué Kunde",
            authorAvatar = user?.avatarUrl ?: "",
            authorLocation = user?.location ?: "Kinshasa, RDC 🇨🇩",
            timestamp = System.currentTimeMillis(),
            content = content,
            mediaUrl = mediaUrl,
            mediaType = mediaType,
            category = category,
            groupTag = groupTag,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isOfflineCreated = true
        )
        postDao.insertPost(newPost)
    }

    suspend fun toggleLike(post: PostEntity) {
        val newLiked = !post.isLiked
        val newLikes = if (newLiked) post.likesCount + 1 else maxOf(0, post.likesCount - 1)
        postDao.updateLike(post.id, newLiked, newLikes)
    }

    suspend fun addComment(postId: String, content: String) {
        val user = currentUser.firstOrNull()
        val comment = CommentEntity(
            id = "c_" + UUID.randomUUID().toString().take(8),
            postId = postId,
            authorName = user?.name ?: "Josué Kunde",
            authorAvatar = user?.avatarUrl ?: "",
            content = content,
            timestamp = System.currentTimeMillis()
        )
        commentDao.insertComment(comment)
        postDao.incrementCommentCount(postId)
    }

    suspend fun sharePost(postId: String) {
        postDao.incrementShareCount(postId)
    }

    suspend fun toggleGroupMembership(group: GroupEntity) {
        val newJoined = !group.isJoined
        val newCount = if (newJoined) group.memberCount + 1 else maxOf(0, group.memberCount - 1)
        groupDao.updateMembership(group.id, newJoined, newCount)
    }

    suspend fun sendMessage(conversationId: String, text: String, senderName: String, senderAvatar: String) {
        val msg = MessageEntity(
            id = "m_" + UUID.randomUUID().toString().take(8),
            conversationId = conversationId,
            senderName = senderName,
            senderAvatar = senderAvatar,
            content = text,
            timestamp = System.currentTimeMillis(),
            isFromMe = true,
            isRead = true,
            messageType = "TEXT"
        )
        messageDao.insertMessage(msg)
    }

    suspend fun updateBio(bio: String) {
        val user = currentUser.firstOrNull() ?: return
        userDao.updateBio(user.id, bio)
    }

    suspend fun markNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun acceptFriend(friendId: String) {
        friendDao.acceptFriend(friendId)
    }

    suspend fun deleteFriend(friendId: String) {
        friendDao.deleteFriend(friendId)
    }

    suspend fun toggleFollowPage(pageId: String, currentStatus: Boolean) {
        pageDao.toggleFollowPage(pageId, !currentStatus)
    }
}
