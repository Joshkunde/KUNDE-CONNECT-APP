package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.UserEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeBorder
import com.example.ui.theme.KundeCongoRed
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeGoldDark
import com.example.ui.theme.KundeGoldLight
import com.example.ui.theme.KundeGreen
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyCard
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.KundeNavyLight
import com.example.ui.theme.KundeSkyBlue
import com.example.ui.theme.KundeSurface
import com.example.ui.theme.KundeTextMuted
import com.example.ui.theme.KundeTextPrimary
import com.example.ui.theme.KundeTextSecondary

data class StoryHighlight(
    val title: String,
    val subtitle: String,
    val flag: String,
    val color: Color
)

@Composable
fun FeedScreen(
    posts: List<PostEntity>,
    currentUser: UserEntity?,
    activeFilter: String,
    language: Language,
    isDataSaver: Boolean,
    onFilterChange: (String) -> Unit,
    onOpenCreatePost: () -> Unit,
    onLikePost: (PostEntity) -> Unit,
    onCommentPost: (PostEntity) -> Unit,
    onSharePost: (PostEntity) -> Unit,
    onGroupClick: (String) -> Unit,
    onNavigateToPages: (() -> Unit)? = null,
    onNavigateToShorts: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val stories = listOf(
        StoryHighlight("Paix Ituri", "120 kits distribués", "🕊️", KundeNavy),
        StoryHighlight("Prière 243", "Veillée nationale", "🙏", KundeGoldDark),
        StoryHighlight("Goma", "Solidarité Kivu", "🇨🇩", KundeGreen),
        StoryHighlight("Marché Kin", "Prix des vivres", "🥬", KundeSkyBlue),
        StoryHighlight("Jeunesse", "Bourse d'études", "🎓", KundeNavyLight)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KundeBackground)
            .testTag("feed_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Quick Publisher trigger (Facebook style: "Quoi de neuf, Josué ?")
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clickable(onClick = onOpenCreatePost)
                    .testTag("whats_on_mind_card"),
                colors = CardDefaults.cardColors(containerColor = KundeSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, KundeBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User avatar
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(KundeNavyLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser?.name?.take(1) ?: "J",
                                color = KundeGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = AppStrings.get("what_new_user", language),
                            color = KundeTextMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = onOpenCreatePost,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = KundeNavy,
                                contentColor = KundeGold
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Post",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.get("post_btn", language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Quick action bar: Pages & Reels shortcuts
                    if (onNavigateToPages != null || onNavigateToShorts != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (onNavigateToShorts != null) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(KundeNavy.copy(alpha = 0.08f))
                                        .clickable(onClick = onNavigateToShorts)
                                        .padding(vertical = 6.dp, horizontal = 8.dp)
                                        .testTag("btn_quick_reels"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = "Reels",
                                            tint = KundeNavy,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = AppStrings.get("tab_reels", language),
                                            color = KundeNavy,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            if (onNavigateToPages != null) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(KundeNavy.copy(alpha = 0.08f))
                                        .clickable(onClick = onNavigateToPages)
                                        .padding(vertical = 6.dp, horizontal = 8.dp)
                                        .testTag("btn_quick_pages"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Pages,
                                            contentDescription = "Pages",
                                            tint = KundeNavy,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = AppStrings.get("tab_pages", language),
                                            color = KundeNavy,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Stories / Familia Highlights
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(stories) { story ->
                    Card(
                        modifier = Modifier
                            .width(115.dp)
                            .height(86.dp),
                        colors = CardDefaults.cardColors(containerColor = story.color),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = story.flag, fontSize = 20.sp)
                            Column {
                                Text(
                                    text = story.title,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = story.subtitle,
                                    color = KundeGold,
                                    fontSize = 9.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            val categories = listOf(
                "ALL" to "filter_all",
                "FOI" to "filter_faith",
                "FAMILLE" to "filter_family",
                "COMMUNAUTE" to "filter_community"
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (code, labelKey) ->
                    val isSelected = activeFilter == code
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterChange(code) },
                        label = {
                            Text(
                                text = AppStrings.get(labelKey, language),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = KundeNavy,
                            selectedLabelColor = KundeGold,
                            containerColor = KundeSurface,
                            labelColor = KundeTextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = KundeBorder,
                            selectedBorderColor = KundeGold
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("filter_chip_$code")
                    )
                }
            }
        }

        // Posts List
        if (posts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune publication pour cette catégorie.",
                        color = KundeTextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(posts, key = { it.id }) { post ->
                PostItemCard(
                    post = post,
                    language = language,
                    isDataSaver = isDataSaver,
                    onLike = { onLikePost(post) },
                    onComment = { onCommentPost(post) },
                    onShare = { onSharePost(post) },
                    onGroupClick = { post.groupTag?.let(onGroupClick) }
                )
            }
        }
    }
}

@Composable
fun PostItemCard(
    post: PostEntity,
    language: Language,
    isDataSaver: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isImageRevealed by remember { mutableStateOf(!isDataSaver) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("post_card_${post.id}"),
        colors = CardDefaults.cardColors(containerColor = KundeSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, KundeBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Author + Location + Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Author avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(KundeNavyDark),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.take(1),
                        color = KundeGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = KundeTextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = KundeGoldDark,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = post.authorLocation,
                            fontSize = 11.sp,
                            color = KundeTextSecondary
                        )
                        Text(
                            text = " • Il y a peu",
                            fontSize = 10.sp,
                            color = KundeTextMuted
                        )
                    }
                }

                // Category pill
                val catColor = when (post.category) {
                    "FOI" -> KundeGoldDark
                    "FAMILLE" -> KundeCongoRed
                    else -> KundeGreen
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(catColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = post.category,
                        color = catColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Group mention banner if attached to a Familia group
            if (!post.groupTag.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(KundeNavy.copy(alpha = 0.07f))
                        .clickable(onClick = onGroupClick)
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = "Group",
                        tint = KundeNavy,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = post.groupTag,
                        color = KundeNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Post Text Content
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = post.content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = KundeTextPrimary
            )

            // Media Preview (Optimized for 2G/3G Congo Networks)
            if (!post.mediaUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                if (isDataSaver && !isImageRevealed) {
                    // 2G/3G Low Data Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(KundeNavy.copy(alpha = 0.05f))
                            .border(1.dp, KundeBorder, RoundedCornerShape(12.dp))
                            .clickable { isImageRevealed = true }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Data Saver",
                                tint = KundeGoldDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Mode Économie 2G/3G actif",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = KundeNavy
                                )
                                Text(
                                    text = "Toucher pour charger l'image (optimisée ~25 Ko)",
                                    fontSize = 11.sp,
                                    color = KundeTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    // Full/Loaded image with rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(KundeNavyDark)
                    ) {
                        AsyncImage(
                            model = post.mediaUrl,
                            contentDescription = "Post image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Action Metrics (Likes, Comments, Shares)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onLike)
                        .padding(4.dp)
                        .testTag("like_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) KundeCongoRed else KundeTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.likesCount}",
                        fontSize = 12.sp,
                        fontWeight = if (post.isLiked) FontWeight.Bold else FontWeight.Medium,
                        color = if (post.isLiked) KundeCongoRed else KundeTextSecondary
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onComment)
                        .padding(4.dp)
                        .testTag("comment_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Comment,
                        contentDescription = "Comment",
                        tint = KundeTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.commentsCount}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = KundeTextSecondary
                    )
                }

                // Share Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onShare)
                        .padding(4.dp)
                        .testTag("share_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = KundeTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.sharesCount}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = KundeTextSecondary
                    )
                }

                // Offline badge if created offline
                if (post.isOfflineCreated) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(KundeGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Stocké localement",
                            color = KundeNavyDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
