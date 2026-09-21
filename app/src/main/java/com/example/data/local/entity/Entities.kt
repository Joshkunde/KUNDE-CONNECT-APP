package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val bio: String,
    val location: String,
    val followersCount: Int,
    val followingCount: Int,
    val isCurrentUser: Boolean = false
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String,
    val authorLocation: String,
    val timestamp: Long,
    val content: String,
    val mediaUrl: String? = null,
    val mediaType: String = "NONE", // NONE, IMAGE, VIDEO
    val category: String = "COMMUNAUTE", // FOI, FAMILLE, COMMUNAUTE
    val groupTag: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isOfflineCreated: Boolean = false
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorName: String,
    val authorAvatar: String,
    val content: String,
    val timestamp: Long
)

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val memberCount: Int,
    val isJoined: Boolean = false,
    val bannerColorHex: Long = 0xFF0B1F3A,
    val location: String = "RDC"
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderName: String,
    val senderAvatar: String,
    val content: String,
    val timestamp: Long,
    val isFromMe: Boolean,
    val isRead: Boolean = true,
    val messageType: String = "TEXT" // TEXT, AUDIO, IMAGE
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val type: String = "INFO", // LIKE, COMMENT, GROUP, PRAYER
    val isRead: Boolean = false
)

// SQL: create table friends (id uuid default gen_random_uuid() primary key, sender uuid, receiver uuid, status text check (status in ('pending','accepted')), created_at timestamp default now());
@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey val id: String,
    val sender: String,
    val receiver: String,
    val receiverName: String,
    val receiverAvatar: String,
    val mutualFriendsCount: Int = 0,
    val status: String = "pending", // 'pending', 'accepted'
    val createdAt: Long = System.currentTimeMillis()
)

// SQL: create table pages (id uuid default gen_random_uuid() primary key, name text, description text, owner_id uuid, followers int default 0);
@Entity(tableName = "pages")
data class PageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val ownerId: String,
    val followers: Int = 0,
    val initials: String = "KB",
    val isFollowing: Boolean = false
)

// Short/Reel entity for Facebook Reels style
data class ShortEntity(
    val id: String,
    val title: String,
    val authorName: String,
    val authorAvatar: String,
    val videoThumbUrl: String,
    val likesCount: Int = 120,
    val commentsCount: Int = 15,
    val audioTitle: String = "Son original - Musique Ituri Bunia",
    val description: String = "Fierté de l'Ituri #KundeConnect #Familia"
)

